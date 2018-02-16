/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author blake
 */
public class FlooringMasteryDaoStubImpl implements FlooringMasteryDao {
    
    Order onlyOrder;
    Map<Integer, Order> orderList = new HashMap<>();
    Map<String, Product> productList = new HashMap<>();
    Map<String, Tax> stateTaxList = new HashMap<>();

    public FlooringMasteryDaoStubImpl() {
        Order onlyOrder = new Order(100);
        onlyOrder.setOrderDate(LocalDate.of(1990,1,1));
        onlyOrder.setCustomerName("Test Customer");
        onlyOrder.setState("MN");
        onlyOrder.setTaxRate(new BigDecimal("10"));
        onlyOrder.setProductType("TILE");
        onlyOrder.setArea(new BigDecimal("100"));
        onlyOrder.setCostPerSquareFoot(new BigDecimal("5"));
        onlyOrder.setLaborCostPerSquareFoot(new BigDecimal("20"));
        onlyOrder.setMaterialCost();
        onlyOrder.setLaborCost();
        onlyOrder.setTotalTax();
        onlyOrder.setOrderTotal();
        orderList.put(onlyOrder.getOrderNumber(), onlyOrder);
        
        productList.put("CARPET", new Product("Carpet", new BigDecimal("2.25"), new BigDecimal("2.10")));
        productList.put("LAMINATE", new Product("Laminate", new BigDecimal("1.75"), new BigDecimal("2.10")));
        productList.put("TILE", new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15")));
        productList.put("WOOD", new Product("Wood", new BigDecimal("5.15"), new BigDecimal("4.75")));
        
        stateTaxList.put("OH", new Tax("OH", new BigDecimal("6.25")));
        stateTaxList.put("PA", new Tax("PA", new BigDecimal("6.75")));
        stateTaxList.put("MI", new Tax("MI", new BigDecimal("5.75")));
        stateTaxList.put("IN", new Tax("IN", new BigDecimal("6.00")));
    }

    @Override
    public Order addOrder(Order order) throws FlooringMasteryPersistenceException {
        return orderList.put(order.getOrderNumber(), order);
    }
    
    @Override
    public Order removeOrder(Order order) throws FlooringMasteryPersistenceException {
        return orderList.remove(order.getOrderNumber());
    }    

    @Override
    public List<Order> getAllOrdersForDate(LocalDate date) throws FlooringMasteryPersistenceException {
        return orderList.values()
                .stream()
                .filter(o -> o.getOrderDate().equals(LocalDate.of(1990, 1, 1)))
                .collect(Collectors.toList());
    }

    @Override
    public Order getOrder(LocalDate date, int orderId) throws FlooringMasteryPersistenceException {
        return orderList.get(orderId);
    }

    @Override
    public Map<String, Product> getProductList() throws FlooringMasteryPersistenceException {
        return productList;
    }

    @Override
    public Map<String, Tax> getStateTaxRatesList() throws FlooringMasteryPersistenceException {
        return stateTaxList;
    }

    @Override
    public boolean getSystemState() throws FlooringMasteryPersistenceException {
        return true;  // system file is set to production
    }

    @Override
    public void clearOrderList() {
        // do nothing
    }

    @Override
    public void saveCurrentChanges() throws FlooringMasteryPersistenceException {
        // do nothing
    }


    
}
