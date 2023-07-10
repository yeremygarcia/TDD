package com.testdrivendevelopment.OrderManagementSystem.utils;

import com.testdrivendevelopment.OrderManagementSystem.model.Order;
import com.testdrivendevelopment.OrderManagementSystem.repository.OrderRepository;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDate;
import org.slf4j.Logger;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    @Bean
    CommandLineRunner initDatabase(OrderRepository orderRepository) {
        return args -> {
            orderRepository.save(new Order("Shrek", LocalDate.now(), "123 Swamp", 23.45));
            orderRepository.save(new Order("Donkey", LocalDate.now(), "456 Dragon's Den", 45.32));

            orderRepository.findAll().forEach(order -> {
                log.info("Preloaded: " + order);
            });
        };
    }
}
