package com.example.profit.profit_service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class OrderController {
  private final List<Order> orders = new ArrayList<>();

  public OrderController() {
    User user = new User(1, "John", "john@mail.com");
    User user2 = new User(2, "Arnold", "arnold@mail.com");
    User user3 = new User(3, "Max", "max@mail.com");
    User user4 = new User(4, "Tom", "tom@mail.com");

    Product product = new Product(1, "Product 1", "Description 1", 10.0, 100);
    Product product2 = new Product(2, "Product 2", "Description 2", 20.0, 200);
    Product product3 = new Product(3, "Product 3", "Description 3", 30.0, 300);
    Product product4 = new Product(4, "Product 4", "Description 4", 40.0, 400);
    Product product5 = new Product(5, "Product 5", "Description 5", 50.0, 500);
    Product product6 = new Product(6, "Product 6", "Description 6", 60.0, 600);

    Order order = new Order(1, user, List.of(product, product2, product3), "2024-03-25T12:00:00Z");
    Order order2 = new Order(2, user3, List.of(product4, product, product6), "2024-03-25T12:00:00Z");
    Order order3 = new Order(3, user2, List.of(product5, product2, product3), "2024-03-25T12:00:00Z");
    Order order4 = new Order(4, user4, List.of(product6, product4, product), "2024-03-25T12:00:00Z");

    orders.add(order);
    orders.add(order2);
    orders.add(order3);
    orders.add(order4);
  }

  @GetMapping("/orders")
  public ResponseEntity<List<Order>> getAllOrders() {
    if (orders.isEmpty()) {
      System.out.println("No orders found");
      return ResponseEntity.noContent().build();

    } else {
      System.out.println("Orders found: " + orders.size());
      return ResponseEntity.ok(orders);
    }
  }

  @GetMapping("/orders/{id}")
  public ResponseEntity<Order> getOrderById(@PathVariable int id) {
    for (Order order : orders) {
      if (order.getId() == id) {
        System.out.println("Order id: " + order.getId() + "found");
        return ResponseEntity.ok(order);
      }
    }
    System.out.println("No orders with id: " + id + " found");
    return ResponseEntity.notFound().build();
  }

  @GetMapping("/orders/total")
  public ResponseEntity<Integer> getTotalOrders() {
    if (orders.isEmpty()) {
      System.out.println("No orders found");
      return ResponseEntity.noContent().build();
    } else {
      System.out.println("Orders found: " + orders.size());
      return ResponseEntity.ok(orders.size());
    }
  }

  @GetMapping("/orders/{id}/totalAmount")
  public ResponseEntity<Double> getTotalAmountById(@PathVariable int id) {
    double totalAmount = 0.0;
    for (Order order : orders) {
      if (order.getId() == id) {
        for (Product product : order.getProducts()) {
          totalAmount += product.getPrice();
        }
        System.out.println("Total Amount of Order id " + order.getId() + " = " + totalAmount);
        return ResponseEntity.ok(totalAmount);
      }
    }
    System.out.println("No orders with id: " + id + " found");
    return ResponseEntity.notFound().build();
  }

  @GetMapping("/orders/totalAmount")
  public ResponseEntity<Double> getTotalAmount() {
    if (orders.isEmpty()) {
      System.out.println("No orders found");
      return ResponseEntity.noContent().build();
    } else {
      double totalAmount = 0.0;
      for (Order order : orders) {
        for (Product product : order.getProducts()) {
          totalAmount += product.getPrice();
        }
      }
      System.out.println("Total Amount of all orders = " + totalAmount);
      return ResponseEntity.ok(totalAmount);
    }
  }

  @GetMapping("/orders/totalAmount/{period}")
  public ResponseEntity<Map<String, Double>> getTotalAmountByPeriod(@PathVariable String period) {
    Map<String, Double> totalAmountByPeriod = new HashMap<>();
    if (orders.isEmpty()) {
      System.out.println("No orders found");
      return ResponseEntity.noContent().build();
    } else {
      switch (period) {
        case "daily":
          calculateTotalAmountByDaily(totalAmountByPeriod);
          break;
        case "monthly":
          calculateTotalAmountByMonthly(totalAmountByPeriod);
          break;
        case "yearly":
          calculateTotalAmountByYearly(totalAmountByPeriod);
          break;
        default:
          System.out.println("Invalid period: " + period);
          return ResponseEntity.badRequest().build();
      }
      System.out.println("Total Amount by " + period + ": " + totalAmountByPeriod);
      return ResponseEntity.ok(totalAmountByPeriod);
    }

  }

  @GetMapping("/**")
  public ResponseEntity<Void> handleInvalidPath() {
    System.out.println("Not found");
    return ResponseEntity.notFound().build();
  }

  private void calculateTotalAmountByDaily(Map<String, Double> totalAmountByPeriod) {
    for (Order order : orders) {
      LocalDate orderDate = LocalDate.parse(order.getTimestamp(), DateTimeFormatter.ISO_DATE_TIME);
      String day = orderDate.format(DateTimeFormatter.ISO_DATE);
      totalAmountByPeriod.put(day, totalAmountByPeriod.getOrDefault(day, 0.0) + calculateOrderTotal(order));
    }
  }

  private void calculateTotalAmountByMonthly(Map<String, Double> totalAmountByPeriod) {
    for (Order order : orders) {
      LocalDate orderDate = LocalDate.parse(order.getTimestamp(), DateTimeFormatter.ISO_DATE_TIME);
      String month = YearMonth.from(orderDate).format(DateTimeFormatter.ofPattern("yyyy-MM"));
      totalAmountByPeriod.put(month, totalAmountByPeriod.getOrDefault(month, 0.0) + calculateOrderTotal(order));
    }
  }

  private void calculateTotalAmountByYearly(Map<String, Double> totalAmountByPeriod) {
    for (Order order : orders) {
      LocalDate orderDate = LocalDate.parse(order.getTimestamp(), DateTimeFormatter.ISO_DATE_TIME);
      String year = String.valueOf(orderDate.getYear());
      totalAmountByPeriod.put(year, totalAmountByPeriod.getOrDefault(year, 0.0) + calculateOrderTotal(order));
    }
  }

  private double calculateOrderTotal(Order order) {
    double totalAmount = 0.0;
    for (Product product : order.getProducts()) {
      totalAmount += product.getPrice();
    }
    return totalAmount;
  }

}