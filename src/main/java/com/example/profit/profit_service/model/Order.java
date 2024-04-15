package com.example.profit.profit_service.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "order")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_order")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "id_costumer")
  private Customer customer;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  @Column(name = "products")
  private List<Product> products;

  @Column(name = "timestamp")
  private LocalDateTime timestamp;
}
