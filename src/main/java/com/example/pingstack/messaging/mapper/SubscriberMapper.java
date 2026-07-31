package com.example.pingstack.messaging.mapper;

import com.example.pingstack.messaging.dtos.CreateSubscriberRequest;
import com.example.pingstack.messaging.dtos.CreateSubscriberResponse;
import com.example.pingstack.messaging.enums.SubscriberStatus;
import com.example.pingstack.messaging.model.Subscriber;
import com.example.pingstack.messaging.service.AudienceCSVRepresentation;
import org.springframework.stereotype.Component;


@Component
public class SubscriberMapper {

    public Subscriber toEntity (CreateSubscriberRequest createSubscriberRequest){
        Subscriber subscriber = new Subscriber();
        subscriber.setName(createSubscriberRequest.getName());
        subscriber.setDestinationNumber(createSubscriberRequest.getDestinationNumber());
        subscriber.setStatus(createSubscriberRequest.getStatus() != null ? createSubscriberRequest.getStatus() : SubscriberStatus.SUBSCRIBED);
        return subscriber;
    }




    public CreateSubscriberResponse toResponse (Subscriber subscriber){
        return new CreateSubscriberResponse(
               subscriber.getSubscriberId(),
                subscriber.getName(),
                subscriber.getDestinationNumber(),
                subscriber.getStatus(),
                subscriber.getCreatedAt() != null
                        ? subscriber.getCreatedAt().toString()
                        : null
        );
    }

    public Subscriber mapToSubscriber(AudienceCSVRepresentation csvRow) {
        Subscriber subscriber = new Subscriber();
        subscriber.setName(csvRow.getName());
        subscriber.setDestinationNumber(csvRow.getdNumber());
        subscriber.setStatus(csvRow.getStatus());
        return subscriber;
    }
}
