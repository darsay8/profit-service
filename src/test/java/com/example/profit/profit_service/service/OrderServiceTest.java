package com.example.profit.profit_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.profit.profit_service.model.Customer;
import com.example.profit.profit_service.model.Order;
import com.example.profit.profit_service.model.Product;
import com.example.profit.profit_service.repository.CustomerRepository;
import com.example.profit.profit_service.repository.OrderRepository;

import oracle.jdbc.proxy.annotation.Post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

  @InjectMocks
  private OrderServiceImpl orderService;

  @Mock
  private CustomerRepository customerRepository;

  @Mock
  private OrderRepository orderRepository;

  @Test
  public void testCreateOrder() {

    Customer customer = new Customer();
    customer.setName("Test user");

    when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

    Order order = new Order();
    order.setId(1L);
    order.setCustomer(customer);
    order.setProducts(new ArrayList<Product>());
    order.setTimestamp(LocalDateTime.now());

    when(orderRepository.save(any(Order.class))).thenReturn(order);

    Order savedOrder = orderService.createOrder(order);

    assertEquals(1L, savedOrder.getId());
  }

  @Test
  public void testGetOrders() {
    Order order = new Order();
    order.setId(1L);
    order.setCustomer(new Customer());
    order.setProducts(new ArrayList<Product>());
    order.setTimestamp(LocalDateTime.now());

    when(orderRepository.findAll()).thenReturn(Arrays.asList(order));

    List<Order> orders = orderService.getOrders();

    assertFalse(orders.isEmpty());
    assertEquals(1, orders.size());
  }

  @Test
  public void testUpdateOrder() {
    Customer customer = new Customer();
    customer.setName("Test user");

    when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

    Order order = new Order();
    order.setId(1L);
    order.setCustomer(customer);
    order.setProducts(new ArrayList<Product>());
    order.setTimestamp(LocalDateTime.now());

    when(orderRepository.save(any(Order.class))).thenReturn(order);

    Order savedOrder = orderService.createOrder(order);

    assertEquals(1L, savedOrder.getId());

    Order updatedOrder = new Order();
    updatedOrder.setId(1L);
    updatedOrder.setCustomer(customer);
    updatedOrder.setProducts(new ArrayList<Product>());
    updatedOrder.setTimestamp(LocalDateTime.now());

    when(orderRepository.findById(updatedOrder.getId())).thenReturn(Optional.of(savedOrder));
    when(orderRepository.save(any(Order.class))).thenReturn(updatedOrder);

    Order newOrder = orderService.updateOrder(updatedOrder.getId(), updatedOrder);

    assertEquals(1L, newOrder.getId());
  }

  @Test
  public void testDeleteOrder() {
    Customer customer = new Customer();
    customer.setName("Test User");

    when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

    Order order = new Order();
    order.setCustomer(customer);
    order.setProducts(new ArrayList<Product>());

    when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Order savedOrder = orderService.createOrder(order);

    when(orderRepository.existsById(savedOrder.getId())).thenReturn(true);

    orderService.deleteOrder(savedOrder.getId());

    verify(orderRepository, times(1)).deleteById(savedOrder.getId());

  }
}
