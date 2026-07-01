package com.example.pingstack.wallet.controller;

import com.example.pingstack.wallet.dto.ChartResponse;
import com.example.pingstack.wallet.dto.OrderResponse;
import com.example.pingstack.wallet.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getAllOrders (@RequestHeader("Authorization") String authHeader, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        Page<OrderResponse> data = orderService.getAllOrders(authHeader, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }
    @GetMapping("/charts")
    public ResponseEntity<List<ChartResponse>> getChartsForOrdersPerMonth(@RequestHeader("Authorization") String authHeader){
        List<ChartResponse> data = orderService.getOrderChart(authHeader);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }
}
