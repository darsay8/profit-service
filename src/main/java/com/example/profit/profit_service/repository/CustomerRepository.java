package com.example.profit.profit_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.profit.profit_service.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
