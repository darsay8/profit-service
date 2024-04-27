package com.example.profit.profit_service.controller;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class OrderNotFoundException extends RuntimeException {

  public OrderNotFoundException(String message) {
    super(message);
  }

}
