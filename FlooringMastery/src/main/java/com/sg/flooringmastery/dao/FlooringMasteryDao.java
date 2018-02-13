/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Customer;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author blake
 */
public interface FlooringMasteryDao {
    
    Order addOrder(Order order) throws FlooringMasteryPersistenceException;
    
    Order removeOrder(LocalDate date, int orderId) throws FlooringMasteryPersistenceException;
    
    List<Order> getAllOrdersForDate(LocalDate date) throws FlooringMasteryPersistenceException;
    
    Order getOrder(LocalDate date, int orderId) throws FlooringMasteryPersistenceException;
    
    Order editOrder(LocalDate date, int orderId) throws FlooringMasteryPersistenceException;
    
    void saveCurrentChanges() throws FlooringMasteryPersistenceException;
    
    Customer addCustomer(Customer customer) throws FlooringMasteryPersistenceException;
    
    Customer removeCustomer(int customerId) throws FlooringMasteryPersistenceException;
    
    Customer editCustomer(int customerId, Customer customer) throws FlooringMasteryPersistenceException;
    
    List<Customer> getAllCustomers() throws FlooringMasteryPersistenceException;
    
    List<Product> getAllProducts() throws FlooringMasteryPersistenceException;
    
    List<Tax> getTaxRatesList() throws FlooringMasteryPersistenceException;
            
}