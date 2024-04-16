package com.example.profit.profit_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.profit.profit_service.model.Customer;
import com.example.profit.profit_service.model.Order;
import com.example.profit.profit_service.model.Product;
import com.example.profit.profit_service.service.OrderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class OrderController {

  private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

  @Autowired
  private OrderService orderService;

  @GetMapping("/orders")
  public ResponseEntity<List<Order>> getOrders() {
    List<Order> orders = orderService.getOrders();
    if (orders.isEmpty()) {
      logger.info("No orders found");
      return ResponseEntity.noContent().build();
    } else {
      logger.info("Orders found: {}", orders.size());
      return ResponseEntity.ok(orders);
    }
  }

  @GetMapping("/orders/{id}")
  public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
    return orderService.getOrderById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> {
          logger.warn("Order with id {} not found.", id);
          return ResponseEntity.notFound().build();
        });
  }

  @PostMapping("/orders")
  public ResponseEntity<Order> createOrder(@RequestBody Order order) {
    if (order == null) {
      logger.error("Order object cannot be null");
      return ResponseEntity.badRequest().build();
    }
    logger.info("Creating a new Order");
    Order createdOrder = orderService.createOrder(order);
    return ResponseEntity.ok(createdOrder);
  }

  @PostMapping("/customers")
  public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
    if (customer == null) {
      logger.error("Customer object cannot be null");
      return ResponseEntity.badRequest().build();
    }
    logger.info("Creating a new customer.");
    Customer createdCustomer = orderService.createCustomer(customer);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
  }

  @PostMapping("/orders/{id}/products")
  public ResponseEntity<Product> createProduct(@PathVariable Long id, @RequestBody Product product) {
    if (product == null) {
      logger.error("Product object cannot be null");
      return ResponseEntity.badRequest().build();
    }
    logger.info("Creating a new product.");
    Product createdProduct = orderService.addProductToOrder(id, product);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
  }

  @GetMapping("/products")
  public ResponseEntity<List<Product>> getProducts() {
    List<Product> products = orderService.getProducts();
    if (products.isEmpty()) {
      logger.info("No products found");
      return ResponseEntity.noContent().build();
    } else {
      logger.info("Products found: {}", products.size());
      return ResponseEntity.ok(products);
    }
  }

  @GetMapping("/orders/total")
  public ResponseEntity<Integer> getTotalOrders() {
    List<Order> orders = orderService.getOrders();
    if (orders.isEmpty()) {
      logger.info("No orders found");
      return ResponseEntity.noContent().build();
    } else {
      logger.info("Orders found: {}", orders.size());
      return ResponseEntity.ok(orders.size());
    }
  }

  @GetMapping("/orders/{id}/totalAmount")
  public ResponseEntity<Double> getTotalAmountById(@PathVariable int id) {
    List<Order> orders = orderService.getOrders();
    for (Order order : orders) {
      if (order.getId() == id) {
        double totalAmount = 0.0;
        for (Product product : order.getProducts()) {
          totalAmount += product.getPrice();
        }
        logger.info("Total Amount of Order id {}: {}", order.getId(), totalAmount);
        return ResponseEntity.ok(totalAmount);
      }
    }
    logger.info("No orders with id: {} found", id);
    return ResponseEntity.notFound().build();
  }

  @GetMapping("/orders/totalAmount")
  public ResponseEntity<Double> getTotalAmount() {
    List<Order> orders = orderService.getOrders();
    if (orders.isEmpty()) {
      logger.error("No orders found");
      return ResponseEntity.noContent().build();
    } else {
      double totalAmount = 0.0;
      for (Order order : orders) {
        for (Product product : order.getProducts()) {
          totalAmount += product.getPrice();
        }
      }
      logger.info("Total Amount of all orders = {}", totalAmount);
      return ResponseEntity.ok(totalAmount);
    }
  }

  @GetMapping("/orders/totalAmount/{period}")
  public ResponseEntity<Map<String, Double>> getTotalAmountByPeriod(@PathVariable String period) {
    double totalAmount = orderService.getTotalAmountByPeriod(period);
    if (totalAmount == 0.0) {
      logger.error("No orders found or invalid period: ", period);
      return ResponseEntity.badRequest().build();
    }
    Map<String, Double> response = new HashMap<>();
    response.put(period, totalAmount);
    logger.info("Total Amount = {}", totalAmount);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/**")
  public ResponseEntity<Void> handleInvalidPath() {
    logger.error("Not found");
    return ResponseEntity.notFound().build();
  }

}