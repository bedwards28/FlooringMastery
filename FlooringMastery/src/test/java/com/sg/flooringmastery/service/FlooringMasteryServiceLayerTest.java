/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dto.Order;
import java.math.BigDecimal;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author blake
 */
public class FlooringMasteryServiceLayerTest {
    
    private FlooringMasteryServiceLayer service;
    
    public FlooringMasteryServiceLayerTest() {
        ApplicationContext ctx = 
                new ClassPathXmlApplicationContext("applicationContext.xml");
        service = ctx.getBean("service", FlooringMasteryServiceLayer.class);
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getNewOrderNumber method, of class FlooringMasteryServiceLayer.
     */
    @Test
    public void testGetNewOrderNumber() throws Exception {
        int orderNum = service.getNewOrderNumber();
        assertEquals(101, orderNum);
    }

    /**
     * Test of getTaxDetails method, of class FlooringMasteryServiceLayer.
     */
    @Test
    public void testGetTaxDetails() throws Exception {
        Order order = new Order(90);
        order.setState("PA");
        service.getTaxDetails(order);
        assertEquals(new BigDecimal("6.75"), order.getTaxRate());
        
        order.setState("MN");
        try {
            service.getTaxDetails(order);
            fail("Expected InvalidStateException was not thrown");
        } catch (Exception e) {
        }
    }

    /**
     * Test of getProductDetails method, of class FlooringMasteryServiceLayer.
     */
    @Test
    public void testGetProductDetails() throws Exception {
        Order order = new Order(91);
        order.setProductType("Carpet");
        service.getProductDetails(order);
        assertEquals(new BigDecimal("2.25"), order.getCostPerSquareFoot());
        assertEquals(new BigDecimal("2.10"), order.getLaborCostPerSquareFoot());
        
        order.setProductType("Steel");
        try {
            service.getProductDetails(order);
            fail("Expected InvalidProductException not thrown");
        } catch (Exception e) {
        }
    }
    
}
