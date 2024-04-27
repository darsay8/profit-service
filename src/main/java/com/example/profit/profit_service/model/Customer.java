package com.example.profit.profit_service.model;

import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "customer")
@EqualsAndHashCode(callSuper = false)
public class Customer extends RepresentationModel<Customer> {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_customer")
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "email")
  private String email;
}
