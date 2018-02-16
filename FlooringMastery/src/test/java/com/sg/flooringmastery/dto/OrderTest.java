/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author blake
 */
public class OrderTest {
    
    Order order;
    
    public OrderTest() {
        order = new Order(1000);
        order.setOrderDate(LocalDate.of(1990, 1, 1));
        order.setState("PA");
        order.setTaxRate(new BigDecimal("6.75"));
        order.setProductType("Carpet");
        order.setArea(new BigDecimal("1000"));
        order.setCostPerSquareFoot(new BigDecimal("10"));
        order.setLaborCostPerSquareFoot(new BigDecimal("20"));
    }
    
    /**
     * Test of getMaterialCost method, of class Order.
     */
    @Test
    public void testGetMaterialCost() {
        assertEquals(new BigDecimal("10000"), order.getMaterialCost());
    }

    /**
     * Test of getLaborCost method, of class Order.
     */
    @Test
    public void testGetLaborCost() {
        assertEquals(new BigDecimal("20000"), order.getLaborCost());
    }

    /**
     * Test of getTotalTax method, of class Order.
     */
    @Test
    public void testGetTotalTax() {
        assertEquals(new BigDecimal("2025.00"), order.getTotalTax());
    }

    /**
     * Test of getOrderTotal method, of class Order.
     */
    @Test
    public void testGetOrderTotal() {
        assertEquals(new BigDecimal("32025.00"), order.getOrderTotal());
    }
    
}
