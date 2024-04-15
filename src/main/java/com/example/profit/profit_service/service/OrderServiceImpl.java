package com.example.profit.profit_service.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.profit.profit_service.model.Customer;
import com.example.profit.profit_service.model.Order;
import com.example.profit.profit_service.model.Product;
import com.example.profit.profit_service.repository.CustomerRepository;
import com.example.profit.profit_service.repository.OrderRepository;
import com.example.profit.profit_service.repository.ProductRepository;

@Service
public class OrderServiceImpl implements OrderService {

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private OrderRepository orderRepository;

  @Override
  public List<Order> getOrders() {
    if (orderRepository.findAll().isEmpty()) {
      throw new IllegalStateException("No orders found");
    }
    return orderRepository.findAll();
  }

  @Override
  public Optional<Order> getOrderById(Long id) {

    if (!orderRepository.existsById(id)) {
      throw new IllegalArgumentException("Order not found with ID: " + id);
    }
    return orderRepository.findById(id);
  }

  @Override
  public Order createOrder(Order order) {
    if (order == null) {
      throw new IllegalArgumentException("Order object cannot be null");
    }

    Customer customer = order.getCustomer();
    Optional<Customer> optionalCustomer = customerRepository.findById(customer.getId());
    order.setCustomer(optionalCustomer.get());
    order.setProducts(order.getProducts());
    order.setTimestamp(LocalDateTime.now());
    return orderRepository.save(order);
  }

  @Override
  public Order updateOrder(Long id, Order order) {
    Order existingOrder = orderRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + id));

    if (order == null) {
      throw new IllegalArgumentException("Order object cannot be null");
    }
    if (order.getCustomer() == null) {
      throw new IllegalArgumentException("Order must have a customer");
    }

    existingOrder.setCustomer(order.getCustomer());
    existingOrder.setProducts(order.getProducts());
    existingOrder.setTimestamp(order.getTimestamp());

    return orderRepository.save(existingOrder);

  }

  @Override
  public void deleteOrder(Long id) {
    if (!orderRepository.existsById(id)) {
      throw new IllegalArgumentException("Order not found with ID: " + id);
    }
    orderRepository.deleteById(id);
  }

  @Override
  public int getTotalOrders() {
    if (orderRepository.findAll().isEmpty()) {
      throw new IllegalStateException("No orders found");
    }
    return orderRepository.findAll().size();
  }

  @Override
  public double getTotalAmount() {
    double totalAmount = 0.0;
    for (Order order : orderRepository.findAll()) {
      if (order.getProducts().isEmpty()) {
        throw new IllegalStateException("Order " + order.getId() + " contains no products");
      }
      for (Product product : order.getProducts()) {
        if (product.getPrice() < 0) {
          throw new IllegalStateException("Invalid price found for product in order " + order.getId());
        }
        totalAmount += product.getPrice();
      }
    }
    return totalAmount;
  }

  @Override
  public double getTotalAmountById(Long id) {
    Order order = orderRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + id));
    double totalAmount = 0.0;
    if (order.getProducts().isEmpty()) {
      throw new IllegalStateException("Order " + order.getId() + " contains no products");
    }

    for (Product product : order.getProducts()) {
      if (product.getPrice() < 0) {
        throw new IllegalStateException("Invalid price found for product in order " + id);
      }
      totalAmount += product.getPrice();
    }
    return totalAmount;
  }

  @Override
  public double getTotalAmountByPeriod(String period) {
    List<Order> orders = orderRepository.findAll();
    double totalAmount = 0.0;

    switch (period) {
      case "daily":
        totalAmount = calculateTotalAmountByDaily(orders);
        break;
      case "monthly":
        totalAmount = calculateTotalAmountByMonthly(orders);
        break;
      case "yearly":
        totalAmount = calculateTotalAmountByYearly(orders);
        break;
      default:
        throw new IllegalArgumentException("Invalid period: " + period);
    }

    return totalAmount;
  }

  @Override
  public List<Customer> getAllCustomers() {
    return customerRepository.findAll();
  }

  @Override
  public Customer createCustomer(Customer customer) {
    return customerRepository.save(customer);
  }

  @Override
  public Product addProductToOrder(Long id, Product product) {

    Order order = orderRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + id));

    product.setOrder(order);

    return productRepository.save(product);
  }

  @Override
  public List<Product> geProducts() {
    return productRepository.findAll();
  }

  // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
  // Functions to Calculate Total Amount by Period
  // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

  private double calculateTotalAmountByDaily(List<Order> orders) {
    double totalAmount = 0.0;
    LocalDate today = LocalDate.now();

    for (Order order : orders) {
      LocalDate orderDate = order.getTimestamp().toLocalDate();
      if (orderDate.equals(today)) {
        for (Product product : order.getProducts()) {
          totalAmount += product.getPrice();
        }
      }
    }

    return totalAmount;
  }

  private double calculateTotalAmountByMonthly(List<Order> orders) {
    double totalAmount = 0.0;
    LocalDate today = LocalDate.now();
    int currentMonth = today.getMonthValue();

    for (Order order : orders) {
      LocalDate orderDate = order.getTimestamp().toLocalDate();
      if (orderDate.getMonthValue() == currentMonth) {
        for (Product product : order.getProducts()) {
          totalAmount += product.getPrice();
        }
      }
    }

    return totalAmount;
  }

  private double calculateTotalAmountByYearly(List<Order> orders) {
    double totalAmount = 0.0;
    LocalDate today = LocalDate.now();
    int currentYear = today.getYear();

    for (Order order : orders) {
      LocalDate orderDate = order.getTimestamp().toLocalDate();
      if (orderDate.getYear() == currentYear) {
        for (Product product : order.getProducts()) {
          totalAmount += product.getPrice();
        }
      }
    }

    return totalAmount;
  }

}
