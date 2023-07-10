package com.testdrivendevelopment.OrderManagementSystem.controller;

import com.testdrivendevelopment.OrderManagementSystem.model.Order;
import com.testdrivendevelopment.OrderManagementSystem.repository.OrderRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    //bringing in the repo for functionality
    private final OrderRepository orderRepository;
    //@Autowired to wire dependencies, in this case via the constructor
    @Autowired
    OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    //CRUD FUNCTIONALITIES BELOW:
    //CREATE
    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody Order order) {     //Validation required
        Order createdOrder = orderRepository.save(order);   //saving order in repository
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);    //return response with created order (201 = CREATED)
    }

    //READ
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);   //find order with parametered id
        if (optionalOrder.isPresent()) {    //if order exists
            Order order = optionalOrder.get();
            return ResponseEntity.ok(order);    //return status OK (200)
        } else {    //else
            return ResponseEntity.notFound().build();   //return status not found (400)
        }
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orderList = orderRepository.findAll();  //retrieve all orders from orderRepository
        return ResponseEntity.ok(orderList);    //return response with items listed and OK (200)
    }

    //UPDATE
    @PutMapping("{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) {
        Optional<Order> optionalOrder = orderRepository.findById(id);   //find order with parameterized id
        if (optionalOrder.isPresent()) {    //if the order exists, update the properties
            Order eOrder = optionalOrder.get();
            eOrder.setCustomerName(order.getCustomerName());
            eOrder.setOrderDate(order.getOrderDate());
            eOrder.setShippingAddress(order.getShippingAddress());
            eOrder.setTotal(order.getTotal());
            Order updatedOrder = orderRepository.save(eOrder);  //save updates order in the repository
            return ResponseEntity.ok(updatedOrder);     //return updated order with status OK (200)
        } else {    //else
            return ResponseEntity.notFound().build();   //return response status NOT FOUND (404)
        }
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);   //find order by id that is in the parameter
        if (optionalOrder.isPresent()) {    //if the order exists
            orderRepository.delete(optionalOrder.get());    //delete from the orderRepository
            return ResponseEntity.noContent().build();  //return response status NO CONTENT (204)
        } else {    //else
            return ResponseEntity.notFound().build();   //return response NOT FOUND (404)
        }
    }

    //handling exceptions for invalid arguments, like validation errors
    //retrieve validation errors and reformat at strings
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        return ResponseEntity.badRequest().body(errors.toString());     //return response with validation errors and status BAD REQUEST (400)
    }

}
