package com.example.profit.profit_service.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.profit.profit_service.model.Customer;
import com.example.profit.profit_service.model.Order;
import com.example.profit.profit_service.model.Product;
import com.example.profit.profit_service.service.OrderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class OrderController {

  private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

  @Autowired
  private OrderService orderService;

  @GetMapping("/orders")
  public CollectionModel<EntityModel<Order>> getOrders() {
    List<Order> orders = orderService.getOrders();

    if (orders.isEmpty()) {
      logger.info("No orders found");
      return CollectionModel.empty();
    } else {
      logger.info("Orders found: {}", orders.size());

      List<EntityModel<Order>> orderResources = orders.stream()
          .map(order -> EntityModel.of(order, WebMvcLinkBuilder
              .linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getOrderById(order.getId())).withSelfRel()))
          .collect(Collectors.toList());

      CollectionModel<EntityModel<Order>> resources = CollectionModel.of(orderResources,
          WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class).getOrders()).withSelfRel());

      return resources;
    }
  }

  @GetMapping("/orders/{id}")
  public EntityModel<Order> getOrderById(@PathVariable Long id) {
    Optional<Order> order = orderService.getOrderById(id);

    if (order.isPresent()) {
      logger.info("Order found: {}", order.get().getId());
      EntityModel<Order> postEntityModel = EntityModel.of(order.get(),
          WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getOrderById(id)).withSelfRel(),
          WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getOrders()).withRel("orders"));
      return postEntityModel;

    } else {
      logger.warn("Order with id {} not found.", id);
      throw new OrderNotFoundException("Order with id " + id + " not found");
    }

  }

  @PostMapping("/orders")
  public EntityModel<Order> createOrder(@RequestBody Order order) {
    logger.info("Creating a new order.");
    Order createdOrder = orderService.createOrder(order);
    EntityModel<Order> postEntityModel = EntityModel.of(createdOrder,
        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getOrderById(createdOrder.getId()))
            .withSelfRel(),
        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getOrders()).withRel("orders"));
    return postEntityModel;
  }

  @PutMapping("/orders/{id}")
  public EntityModel<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) {
    logger.info("Updating order with id {}.", id);
    Order updatedOrder = orderService.updateOrder(id, order);
    EntityModel<Order> postEntityModel = EntityModel.of(updatedOrder,
        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getOrderById(id)).withSelfRel(),
        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getOrders()).withRel("orders"));
    return postEntityModel;
  }

  @DeleteMapping("/orders/{id}")
  public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
    logger.info("Deleting order with id {}.", id);
    orderService.deleteOrder(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/customers")
  public CollectionModel<EntityModel<Customer>> getAllCustomers() {
    List<Customer> customers = orderService.getAllCustomers();

    if (customers.isEmpty()) {
      logger.info("No customers found.");
      return CollectionModel.empty();
    } else {
      logger.info("Returning {} customers.", customers.size());

      List<EntityModel<Customer>> customerResources = customers.stream()
          .map(author -> EntityModel.of(author))
          .collect(Collectors.toList());

      CollectionModel<EntityModel<Customer>> resources = CollectionModel.of(customerResources);

      return resources;
    }
  }

  @PostMapping("/customers")
  public EntityModel<Customer> createCustomer(@RequestBody Customer customer) {
    logger.info("Creating a new customer.");
    Customer createdCustomer = orderService.createCustomer(customer);

    return EntityModel.of(createdCustomer, WebMvcLinkBuilder
        .linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllCustomers()).withRel("customers"));

  }

  @PostMapping("/orders/{id}/products")
  public EntityModel<Product> createProduct(@PathVariable Long id, @RequestBody Product product) {
    logger.info("Creating a new product for order with id {}.", id);

    Product createdProduct = orderService.addProductToOrder(id, product);
    EntityModel<Product> productEntityModel = EntityModel.of(createdProduct,
        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getOrderById(id)).withSelfRel());
    return productEntityModel;
  }

  @GetMapping("/products")
  public CollectionModel<EntityModel<Product>> getProducts() {
    List<Product> products = orderService.getProducts();
    if (products.isEmpty()) {
      logger.info("No products found");
      return CollectionModel.empty();
    } else {
      logger.info("Products found: {}", products.size());

      List<EntityModel<Product>> productResources = products.stream()
          .map(product -> EntityModel.of(product, WebMvcLinkBuilder
              .linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getOrderById(product.getOrder().getId()))
              .withSelfRel()))
          .collect(Collectors.toList());

      CollectionModel<EntityModel<Product>> resources = CollectionModel.of(productResources);

      return resources;

    }
  }

  @GetMapping("/orders/total")
  public EntityModel<Map<String, Integer>> getTotalOrders() {
    logger.info("Getting total orders");

    Integer totalOrders = orderService.getTotalOrders();

    Map<String, Integer> totalMap = new HashMap<>();
    totalMap.put("totalOrders", totalOrders);

    return EntityModel.of(totalMap,
        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getTotalOrders()).withSelfRel());
  }

  @GetMapping("/orders/{id}/totalAmount")
  public EntityModel<Map<String, Double>> getTotalAmountById(@PathVariable Long id) {
    double totalAmount = orderService.getTotalAmountById(id);

    Map<String, Double> response = new HashMap<>();
    response.put("totalAmount", totalAmount);
    return EntityModel.of(response,
        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getTotalAmountById(id)).withSelfRel());
  }

  @GetMapping("/orders/totalAmount")
  public EntityModel<Map<String, Double>> getTotalAmount() {

    List<Order> orders = orderService.getOrders();
    double totalAmount = 0.0;
    for (Order order : orders) {
      for (Product product : order.getProducts()) {
        totalAmount += product.getPrice();
      }
    }
    Map<String, Double> response = new HashMap<>();
    response.put("totalAmount", totalAmount);
    return EntityModel.of(response,
        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getTotalAmount()).withSelfRel());
  }

  @GetMapping("/orders/totalAmount/{period}")
  public EntityModel<Map<String, Double>> getTotalAmountByPeriod(@PathVariable String period) {
    double totalAmount = orderService.getTotalAmountByPeriod(period);

    Map<String, Double> response = new HashMap<>();
    response.put(period, totalAmount);
    logger.info("Total Amount = {}", totalAmount);

    return EntityModel.of(response,
        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getTotalAmountByPeriod(period))
            .withSelfRel());
  }

  @GetMapping("/**")
  public ResponseEntity<Void> handleInvalidPath() {
    logger.error("Not found");
    return ResponseEntity.notFound().build();
  }

}