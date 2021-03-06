/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 *
 * @author blake
 */
public interface FlooringMasteryDao {
    
    Order addOrder(Order order) throws FlooringMasteryPersistenceException;
    
    Order removeOrder(Order order) throws FlooringMasteryPersistenceException;
    
    List<Order> getAllOrdersForDate(LocalDate date) throws FlooringMasteryPersistenceException;
    
    Order getOrder(LocalDate date, int orderId) throws FlooringMasteryPersistenceException;
    
    void saveCurrentChanges() throws FlooringMasteryPersistenceException;
    
    Map<String, Product> getProductList() throws FlooringMasteryPersistenceException;
    
    Map<String, Tax> getStateTaxRatesList() throws FlooringMasteryPersistenceException;

    boolean getSystemState() throws FlooringMasteryPersistenceException;
    
    void clearOrderList();
    
    Map<Integer, Order> getOrderList();

}
