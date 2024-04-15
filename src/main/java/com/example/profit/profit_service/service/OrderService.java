package com.example.profit.profit_service.service;

import java.util.List;
import java.util.Optional;

import com.example.profit.profit_service.model.Customer;
import com.example.profit.profit_service.model.Order;
import com.example.profit.profit_service.model.Product;

public interface OrderService {

  List<Order> getOrders();

  Optional<Order> getOrderById(Long id);

  Order createOrder(Order order);

  Order updateOrder(Long id, Order order);

  void deleteOrder(Long id);

  int getTotalOrders();

  double getTotalAmount();

  double getTotalAmountById(Long id);

  double getTotalAmountByPeriod(String period);

  List<Customer> getAllCustomers();

  Customer createCustomer(Customer customer);

  Product addProductToOrder(Long id, Product product);

  List<Product> geProducts();

}
