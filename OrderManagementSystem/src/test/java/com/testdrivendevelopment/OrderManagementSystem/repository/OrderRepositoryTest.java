package com.testdrivendevelopment.OrderManagementSystem.repository;

import com.testdrivendevelopment.OrderManagementSystem.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class OrderRepositoryTest {
    //@Autowired to wire dependencies, this case being by field injection
    @Autowired
    private OrderRepository orderRepository;

    //Before each test is run, delete orders in the repository created for test purposes
    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
    }

    //CRUD FUNCTIONALITY TESTS BELOW:
    //CREATE
    @Test
    void shouldSaveOrder() {
        //Given - mock order
        Order order = new Order("Fiona", LocalDate.now(), "123 Swamp Ave", 40.45);

        //When - save order in repository
        Order savedOrder = orderRepository.save(order);

        //Then - check if order was saved
        Optional<Order> optionalOrder = orderRepository.findById(savedOrder.getId());
        assertThat(optionalOrder)
                .isPresent()
                .hasValueSatisfying(o -> {
                    //asser that the retrieved order is equal to original order
                    assertThat(o).isEqualToComparingFieldByField(order);
                });
    }

    @Test
    void shouldFindOrderById() {
        // Given - mock order
        Order order = new Order("Fiona", LocalDate.now(), "123 Swamp Ave", 40.45);
        Order savedOrder = orderRepository.save(order);

        // When - retrieve order by id from repository
        Optional<Order> optionalOrder = orderRepository.findById(savedOrder.getId());

        // Then - assert that the retrieved order is equal to the saved order
        assertThat(optionalOrder)
                .isPresent()
                .hasValueSatisfying(o -> {
                    assertThat(o).isEqualToComparingFieldByField(savedOrder);
                });
    }

    @Test
    void shouldUpdateOrder() {
        // Given - mock order
        Order order = new Order("Fiona", LocalDate.now(), "123 Swamp Ave", 40.45);
        Order savedOrder = orderRepository.save(order);

        // When - modify saved order in customer name
        savedOrder.setCustomerName("Updated Name");
        Order updatedOrder = orderRepository.save(savedOrder);

        // Then - retrieve updated order from repository
        Optional<Order> optionalOrder = orderRepository.findById(updatedOrder.getId());
        assertThat(optionalOrder)
                .isPresent()
                .hasValueSatisfying(o -> {
                    //assert that retrieved order is equal to updated order
                    assertThat(o).isEqualToComparingFieldByField(updatedOrder);
                });
    }

    @Test
    void shouldDeleteOrder() {
        // Given - mock order
        Order order = new Order("Fiona", LocalDate.now(), "123 Swamp Ave", 40.45);
        Order savedOrder = orderRepository.save(order);

        // When - delete the saved order from repository
        orderRepository.delete(savedOrder);

        // Then - verify that the order no longer exists in the repository
        Optional<Order> optionalOrder = orderRepository.findById(savedOrder.getId());
        assertThat(optionalOrder).isEmpty();
    }

}
