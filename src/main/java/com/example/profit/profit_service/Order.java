package com.example.profit.profit_service;

import java.util.List;

public class Order {
  private int id;
  private User user;
  private List<Product> products;
  private String timestamp;

  public Order(int id, User user, List<Product> products, String timestamp) {
    this.id = id;
    this.user = user;
    this.products = products;
    this.timestamp = timestamp;

  }

  public int getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public List<Product> getProducts() {
    return products;
  }

  public String getTimestamp() {
    return timestamp;
  }

}
