package com.example.profit.profit_service.controller;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.profit.profit_service.model.Order;
import com.example.profit.profit_service.model.Product;
import com.example.profit.profit_service.service.OrderService;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private OrderService orderService;

  @Test
  public void testGetOrders() throws Exception {
    Order order = new Order();
    order.setId(1L);
    order.setProducts(Arrays.asList(new Product(), new Product()));

    when(orderService.getOrders()).thenReturn(Arrays.asList(order));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/orders"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].products", Matchers.hasSize(2)));
  }

  @Test
  public void testGetOrderById() throws Exception {
    Order order = new Order();
    order.setId(1L);
    order.setProducts(Arrays.asList(new Product(), new Product()));

    when(orderService.getOrderById(1L)).thenReturn(Optional.of(order));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/1"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.products", Matchers.hasSize(2)));
  }

  @Test
  public void testCreateOrder() throws Exception {
    Order order = new Order();
    order.setId(1L);
    order.setProducts(Arrays.asList(new Product(), new Product()));

    when(orderService.createOrder(order)).thenReturn(order);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
        .contentType("application/json")
        .content("{\"id\":1,\"products\":[{},{}]}"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.products", Matchers.hasSize(2)));
  }

}
