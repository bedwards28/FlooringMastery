/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.FlooringMasteryDao;
import com.sg.flooringmastery.dao.FlooringMasteryPersistenceException;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 *
 * @author blake
 */
public class FlooringMasteryServiceLayerImpl implements FlooringMasteryServiceLayer {

    private FlooringMasteryDao dao;

    public FlooringMasteryServiceLayerImpl(FlooringMasteryDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Order> getOrdersListByDate(LocalDate date) throws FlooringMasteryPersistenceException {
        return dao.getAllOrdersForDate(date);
    }

    @Override
    public int getNewOrderNumber() throws FlooringMasteryPersistenceException {
        int orderNumber = -1;
        List<Order> orders = new ArrayList<>();
        int dayShift = 0;

        while (true) {
            try {
                orders = getOrdersListByDate(LocalDate.now().minusDays(dayShift));

            } catch (FlooringMasteryPersistenceException e) {
                dayShift++;
                continue;
            }

            try {
                orderNumber = orders.stream()
                        .mapToInt(Order::getOrderNumber)
                        .max()
                        .getAsInt() + 1;
                break;
            } catch (NoSuchElementException e) {
                dayShift++;
                continue;
            }
        }

        return orderNumber;
    } // end getNewOrderNumber

    @Override
    public Order getTaxDetails(Order order) throws
            FlooringMasteryPersistenceException,
            InvalidStateException {

        Map<String, Tax> taxList = dao.getStateTaxRatesList();
        Set keys = taxList.keySet();

        if (keys.contains(order.getState().toUpperCase())) {
            BigDecimal taxRate = taxList.get(order.getState()).getTaxRate();
            order.setTaxRate(taxRate);
        } else {
            throw new InvalidStateException("Invalid state entered.");
        }

        return order;
    }

    @Override
    public Set getTaxStates() throws FlooringMasteryPersistenceException {
        return dao.getStateTaxRatesList().keySet();
    }

    @Override
    public Order getProductDetails(Order order) throws
            FlooringMasteryPersistenceException,
            InvalidProductException {

        Map<String, Product> productList = dao.getProductList();
        Set<String> productKeys = productList.keySet();

        if (productKeys.contains(order.getProductType().toUpperCase())) {
            order.setCostPerSquareFoot(productList.get(order.getProductType().toUpperCase()).getCostPerSquareFoot());
            order.setLaborCostPerSquareFoot(productList.get(order.getProductType().toUpperCase()).getLaborCostPerSquareFoot());
        } else {
            throw new InvalidProductException("Invalid Product Entered");
        }

        return order;
    }

    @Override
    public Set<String> getValidProductsList() throws FlooringMasteryPersistenceException {
        return dao.getProductList().keySet();
    }

    @Override
    public Order saveOrder(Order newOrder) throws FlooringMasteryPersistenceException {
        dao.addOrder(newOrder);
        return newOrder;
    }

    @Override
    public Order getOrder(LocalDate date, int orderNumber) throws FlooringMasteryPersistenceException {
        return dao.getOrder(date, orderNumber);
    }

    @Override
    public boolean getSystemState() throws FlooringMasteryPersistenceException {
        return dao.getSystemState();
    }

    @Override
    public void saveCurrentChanges() throws FlooringMasteryPersistenceException {
        dao.saveCurrentChanges();
    }

    @Override
    public Order deleteOrder(Order order) throws FlooringMasteryPersistenceException {
        return dao.removeOrder(order);
    }

}
