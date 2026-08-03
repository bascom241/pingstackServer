package com.example.pingstack.messaging.service;


import com.example.pingstack.auth.model.User;
import com.example.pingstack.exceptions.notfound.ResourceNotFoundException;
import com.example.pingstack.exceptions.unauthorized.AccessDeniedException;
import com.example.pingstack.messaging.dtos.*;
import com.example.pingstack.messaging.enums.SubscriberStatus;
import com.example.pingstack.messaging.mapper.AudienceMapper;
import com.example.pingstack.messaging.mapper.SubscriberMapper;
import com.example.pingstack.messaging.model.AudienceList;
import com.example.pingstack.messaging.model.Subscriber;
import com.example.pingstack.messaging.repository.AudienceListRepository;
import com.example.pingstack.messaging.repository.SubscriberRepository;
import com.example.pingstack.wallet.utils.ExtractUserInfoFromAuthHeader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AudienceService {

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private AudienceListRepository audienceListRepository;

    @Autowired
    private SubscriberMapper subscriberMapper;

    @Autowired
    private AudienceMapper audienceMapper;

    @Autowired
    private ExtractUserInfoFromAuthHeader extractUserInfoFromAuthHeader;


    // Utils to upload csv
    private List<Subscriber> parseCsv(MultipartFile file, AudienceList audienceList){
        try(Reader reader = new BufferedReader(new BufferedReader(new InputStreamReader(file.getInputStream())))){
            HeaderColumnNameMappingStrategy<AudienceCSVRepresentation> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(AudienceCSVRepresentation.class);
            List<AudienceCSVRepresentation> csvToBeanBuilder = new CsvToBeanBuilder<AudienceCSVRepresentation>(reader)
                    .withMappingStrategy(strategy)
                    .withIgnoreEmptyLine(true)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();
            return csvToBeanBuilder.stream().map(csvRow ->{

                System.out.println("Name: " + csvRow.getName());
                System.out.println("Number: " + csvRow.getdNumber());
                System.out.println("Status: " + csvRow.getStatus());
                Subscriber subscriber = subscriberMapper.mapToSubscriber(csvRow);
                subscriber.setAudienceList(audienceList);
                return subscriber;
            }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Upload Audience List
    public AudienceResponse createAudienceList (String authHeader, CreateAudienceRequest createAudienceRequest){
        User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);
        AudienceList audienceList = audienceMapper.toEntity(createAudienceRequest);
        audienceList.setUser(user);
        AudienceList savedAudienceList = audienceListRepository.save(audienceList);
        return audienceMapper.toResponse(savedAudienceList);
    }


    // Get Audience List
    public List<AudienceListSummary> getAllAudienceList(String authHeader){
        User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);
        return audienceListRepository.findAudienceListByUserId(user.getUserId());
    }

    // Get Single Audience
    public AudienceResponse getSingleAudience(UUID listId) {
        AudienceList audienceList = audienceListRepository.findById(listId).orElseThrow(()-> new RuntimeException("Audience not found"));
        return audienceMapper.toResponse(audienceList);
    }


    // Upload SingleSubscriber to A single Audience List
    @Transactional
    public CreateSubscriberResponse addSingleMember(CreateSubscriberRequest createSubscriberRequest){
        AudienceList list = audienceListRepository.findById(createSubscriberRequest.getListId()).orElseThrow(() -> new RuntimeException("audience not found"));
        Subscriber subscriber = subscriberMapper.toEntity(createSubscriberRequest);
        subscriber.setAudienceList(list);
        Subscriber savedSubscriber = subscriberRepository.save(subscriber);
        return subscriberMapper.toResponse(savedSubscriber);
    }


    // Upload Subscriber Lists to an Audience List
    @Transactional
    public Integer uploadBulkSubscribers(UUID listId , MultipartFile file){
        AudienceList audienceList = audienceListRepository.findById(listId).orElseThrow(()-> new RuntimeException("Audience List not found"));
        List<Subscriber> subscribers = parseCsv(file, audienceList);
        List<Subscriber> savedSubs = subscriberRepository.saveAll(subscribers);
        return savedSubs.size();
    }

    // This get the Subscribers for a list
    public Page<CreateSubscriberResponse> getPaginatedSubscribers(UUID listId, String query, int page , int size){
        Pageable pageable = PageRequest.of(page,size, Sort.by("name").ascending());
        Page<Subscriber> subscriberPage = subscriberRepository.findByListAndQuery(listId,query,pageable);
        return subscriberPage.map(subscriber -> subscriberMapper.toResponse(subscriber));
    }

    // This fetch a single Subscriber
    public CreateSubscriberResponse getSingleSubscriber (UUID subscriberId){
        Subscriber subscriber = subscriberRepository.findById(subscriberId).orElseThrow(()-> new ResourceNotFoundException("subscriber id not found"));
        return subscriberMapper.toResponse(subscriber);
    }

    // This update a single Subscriber
    public CreateSubscriberResponse updateSingleSubscriber(UpdateSubscriberRequest updateSubscriberRequest){
        AudienceList audienceList = audienceListRepository.findById(updateSubscriberRequest.getListId())
                .orElseThrow(() -> new ResourceNotFoundException("Audience not found"));
        Subscriber subscriber = subscriberRepository.findById(updateSubscriberRequest.getSubscriberId())
                .orElseThrow(() -> new ResourceNotFoundException("Subscriber not found"));
        if (!subscriber.getAudienceList().getId().equals(audienceList.getId())) {
            throw new IllegalArgumentException("Subscriber does not belong to this audience.");
        }
        if (updateSubscriberRequest.getDestinationNumber() != null
                && !updateSubscriberRequest.getDestinationNumber().isBlank()) {
            subscriber.setDestinationNumber(updateSubscriberRequest.getDestinationNumber());
        }
        if (updateSubscriberRequest.getName() != null
                && !updateSubscriberRequest.getName().isBlank()) {
            subscriber.setName(updateSubscriberRequest.getName());
        }
        if (updateSubscriberRequest.getStatus() != null) {
            subscriber.setStatus(updateSubscriberRequest.getStatus());
        }
        subscriberRepository.save(subscriber);
        return subscriberMapper.toResponse(subscriber);
    }

    public void deleteSubscriber ( UUID subscriberId, UUID audienceId,  String authHeader){
        User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);
        AudienceList audienceList = audienceListRepository.findById(audienceId).orElseThrow(() -> new ResourceNotFoundException("audience not found"));
        if(!audienceList.getUser().getUserId().equals(user.getUserId())){
            throw new AccessDeniedException("You are not authorized to this audience list");
        }

        Subscriber subscriber = subscriberRepository.findById(subscriberId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscriber not found"));
        subscriberRepository.delete(subscriber);
    }

}
