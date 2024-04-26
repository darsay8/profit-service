package com.example.profit.profit_service.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.profit.profit_service.model.Order;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryTest {

  @Autowired
  private OrderRepository orderRepository;

  @Test
  public void testOrderRepositorySave() {
    Order order = new Order();

    Order savedOrder = orderRepository.save(order);

    assertNotNull(savedOrder);
  }

  @Test
  public void testOrderRepositoryFindById() {
    Order order = new Order();

    Order savedOrder = orderRepository.save(order);

    Order foundOrder = orderRepository.findById(savedOrder.getId()).get();

    assertEquals(savedOrder.getId(), foundOrder.getId());
  }

  @Test
  public void testOrderRepositoryFindAll() {
    Order order = new Order();

    orderRepository.save(order);

    assertFalse(orderRepository.findAll().isEmpty());
  }

}
