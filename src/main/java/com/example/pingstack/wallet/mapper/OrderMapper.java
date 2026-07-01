package com.example.pingstack.wallet.mapper;


import com.example.pingstack.wallet.dto.CreateOrderRequest;
import com.example.pingstack.wallet.dto.OrderResponse;
import com.example.pingstack.wallet.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {


    public static Order toOrder (CreateOrderRequest createOrderRequest){
        Order newOrder = new Order();
        newOrder.setPaymentMethod(createOrderRequest.getPaymentMethod());
        newOrder.setTransactionNumber(createOrderRequest.getReference());
        return newOrder;
    }


    public  OrderResponse toResponse (Order order){
        return new OrderResponse(
                order.getOrderId(),
                order.getUser().getFirstName() + " " + order.getUser().getLastName(),
                order.getTransactionNumber(),
                order.getPaymentMethod(),
                order.getCreatedAt()
        );
    }
}
