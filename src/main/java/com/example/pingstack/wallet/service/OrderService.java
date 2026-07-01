package com.example.pingstack.wallet.service;


import com.example.pingstack.auth.model.User;
import com.example.pingstack.auth.repository.UserRepository;
import com.example.pingstack.wallet.dto.ChartResponse;
import com.example.pingstack.wallet.dto.CreateOrderRequest;
import com.example.pingstack.wallet.dto.OrderResponse;
import com.example.pingstack.wallet.mapper.OrderMapper;
import com.example.pingstack.wallet.model.Order;
import com.example.pingstack.wallet.repository.OrderRepository;
import com.example.pingstack.wallet.utils.ExtractUserInfoFromAuthHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private UserRepository userRepository;


   @Autowired
   private OrderRepository orderRepository;

   @Autowired
   private ExtractUserInfoFromAuthHeader extractUserInfoFromAuthHeader;

   @Autowired
   private OrderMapper orderMapper;

   public void createOrder (String email , CreateOrderRequest createOrderRequest){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("user not found"));
        Optional<Order> existingOrder = orderRepository.findByTransactionNumber(createOrderRequest.getReference());
        if(existingOrder.isPresent()){
            throw new RuntimeException("Order already exits");
        }
        Order order = OrderMapper.toOrder(createOrderRequest);
        order.setUser(user);
        orderRepository.save(order);
    }

    public Page<OrderResponse> getAllOrders (String authHeader, int page, int size){
       User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);
       Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
       Page<Order> orders = orderRepository.findOrderByUser(user,pageable);
       return orders.map(orderMapper::toResponse);
    }

    public List<ChartResponse> getOrderChart (String authHeader){
        User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);
        List<Order> orders = orderRepository.findOrderByUser(user);
        Map<LocalDate, Long> groupedOrders = orders.stream().collect(Collectors.groupingBy(order -> order.getCreatedAt().toLocalDate(), TreeMap::new,Collectors.counting()));

        return groupedOrders.entrySet()
                .stream()
                .map(localDateLongEntry -> new ChartResponse(
                        localDateLongEntry.getKey().atStartOfDay().toString(),
                        localDateLongEntry.getValue()
                )).toList();
    }



}
