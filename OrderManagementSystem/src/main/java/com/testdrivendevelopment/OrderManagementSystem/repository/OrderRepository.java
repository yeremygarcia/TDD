package com.testdrivendevelopment.OrderManagementSystem.repository;

import com.testdrivendevelopment.OrderManagementSystem.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository("orderRepository")
public interface OrderRepository extends JpaRepository<Order, Long> {
}
