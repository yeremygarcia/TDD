package com.testdrivendevelopment.OrderManagementSystem.controller;

import com.testdrivendevelopment.OrderManagementSystem.model.Order;
import com.testdrivendevelopment.OrderManagementSystem.repository.OrderRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc
class OrderControllerTest {
    //@Autowired to wire dependencies, this case being by field injection
    @Autowired
    MockMvc mockMvc;
    //mocking repository
    @MockBean
    private OrderRepository orderRepository;

    //CRUD FUNCTIONALITY TESTING:
    //CREATE
    @Test
    public void createOrderTest() throws Exception {
        //Given - mock order
        Order order = new Order("Shrek", LocalDate.now(), "123 Swamp", 100.0);
        order.setId(1L);

        //When - mock behavior of orderRepository.save() and return saved order
        when(orderRepository.save(Mockito.any(Order.class))).thenAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            savedOrder.setId(1L);
            return savedOrder;
        });

        //Then - perform POST request to create new order
        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerName\": \"Shrek\", \"shippingAddress\": \"123 Swamp\", \"total\": 100.0}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    //READ
    @Test
    public void getOrderByIdTest() throws Exception {
        //Given - mock order
        Order order = new Order();
        order.setId(1L);
        order.setCustomerName("Shrek");
        order.setOrderDate(LocalDate.parse("2023-06-11"));
        order.setShippingAddress("123 Swamp");
        order.setTotal(100.0);

        //When - mock behavior of orderRepository.findById() and return specified order
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        //Then - preform GET request to retrieve order by id
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName", Matchers.is("Shrek")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderDate", Matchers.is("2023-06-11")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shippingAddress", Matchers.is("123 Swamp")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.is(100.0)));
    }

    @Test
    public void getAllOrdersTest() throws Exception {
        //Given - mock orders
        Order order1 = new Order("Shrek", LocalDate.now(), "123 Swamp", 100.0);
        Order order2 = new Order("Donkey", LocalDate.now(), "Swamp Neighbor", 200.0);
        order1.setId(1L);
        order2.setId(2L);
        List<Order> orderList = new ArrayList<>();
        orderList.add(order1);
        orderList.add(order2);

        //When - mock behavior of the orderRepository.findAll() and return list of orders
        when(orderRepository.findAll()).thenReturn(orderList);

        //Then - preform GET request to retrieve all orders
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(2))).andDo(print());

    }

    //UPDATE
    @Test
    public void updateOrderTest() throws Exception {
        //Given - mock order
        Order order = new Order("Shrek", LocalDate.now(), "123 Swamp", 100.0);
        order.setId(1L);

        //When - mock behavior of the orderRepository.findById and .save
        when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(order));
        when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);

        //Then - preform PUT request to update existing order
        mockMvc.perform(MockMvcRequestBuilders.put("/api/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerName\": \"Fiona\", \"shippingAddress\": \"123 Swamp Ave\", \"total\": 200.0}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName").value("Fiona"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shippingAddress").value("123 Swamp Ave"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(200.0));
    }

    //DELETE
    @Test
    public void deleteOrderTest() throws Exception {
        //Given -mock order
        Order order = new Order("Shrek", LocalDate.now(), "123 Swamp", 100.0);
        order.setId(1L);

        //When - mock behavior of orderRepository.findById
        when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(order));

        //Then - preform DELETE request to delete order
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/orders/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    //VALIDATION TEST CASES:
    @Test
    public void createOrder_ErrorsTest() throws Exception {
        //Given - mock order with invalid properties
        Order order = new Order("", LocalDate.now(), "", -100.0);
        order.setId(1L);

        //Then - preform POST request to create new order with validation errors
        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerName\": \"\", \"shippingAddress\": \"\", \"total\": -100.0}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(containsString("Customer name is required")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Shipping address is required")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Total must be a positive value")));

    }

    @Test
    public void updateOrder_OrderDoesNotExist() throws Exception {
        //Given - mock order
        Order order = new Order("Shrek", LocalDate.now(), "123 Swamp", 100.0);
        order.setId(1L);

        //When - mock behavior of orderRepository.findById
        when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        //Then - preform PUT request to update an order that is nonexistent
        mockMvc.perform(MockMvcRequestBuilders.put("/api/orders/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerName\": \"Fiona\", \"shippingAddress\": \"123 Swamp Ave\", \"total\": 200.0}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void deleteOrder_OrderDoesNotExist() throws Exception {
        //Given - mock order
        Order order = new Order();
        order.setId(7L);

        //When - mock orderRepository.findById
        when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        //Then - preform DELETE request to delete a nonexistent order
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/orders/7"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
