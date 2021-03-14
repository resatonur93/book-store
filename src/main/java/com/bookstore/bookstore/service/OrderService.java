package com.bookstore.bookstore.service;

import com.bookstore.bookstore.model.Book;
import com.bookstore.bookstore.model.Order;
import com.bookstore.bookstore.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

@Service
public class OrderService {


    private final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final BookService bookService;
    private final OrderRepository orderRepository;
    public OrderService(BookService bookService, OrderRepository orderRepository){
        this.bookService = bookService;
        this.orderRepository = orderRepository;
    }
    public Order putAnOrder(List<Integer> bookIdList, String userName){
        List<Optional<Book>> bookList = bookIdList.stream()
                .map(bookService::findBookById).collect(Collectors.toList());

        Double totalPrice = bookIdList.stream()
                .map(bookService::findBookById)
                .map(optionalBook -> optionalBook.map(Book::getPrice).orElse(0.0))
                .reduce(0.0, Double::sum );


        Order order= Order.builder()
                .bookList(bookIdList)
                .totalPrice(totalPrice)
                .userName(userName)
                .build();
        return orderRepository.save(order);
    }
    public  List<Order> getAllOrders(){ return orderRepository.findAll();}
}
