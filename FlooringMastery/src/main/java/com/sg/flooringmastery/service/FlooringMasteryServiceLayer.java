/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.FlooringMasteryPersistenceException;
import com.sg.flooringmastery.dto.Order;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 *
 * @author blake
 */
public interface FlooringMasteryServiceLayer {
    
    public List<Order> getOrdersListByDate(LocalDate date) throws FlooringMasteryPersistenceException;
    
    public int getNewOrderNumber() throws FlooringMasteryPersistenceException;
    
    public Order getTaxDetails(Order order) throws 
            FlooringMasteryPersistenceException,
            InvalidStateException;

    public Set getTaxStates() throws FlooringMasteryPersistenceException;

    public Order getProductDetails(Order newOrder) throws 
            FlooringMasteryPersistenceException, 
            InvalidProductException;

    public Set<String> getValidProductsList() throws FlooringMasteryPersistenceException;

    public Order saveOrder(Order newOrder) throws FlooringMasteryPersistenceException;

    public Order getOrder(LocalDate date, int orderNumber) throws FlooringMasteryPersistenceException;

    public boolean getSystemState() throws FlooringMasteryPersistenceException;

    public void saveCurrentChanges() throws FlooringMasteryPersistenceException;
    
}
