/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author blake
 */
public class FlooringMasteryDaoTest {
    
    FlooringMasteryDao dao;
    
    public FlooringMasteryDaoTest() {
        ApplicationContext ctx = 
                new ClassPathXmlApplicationContext("applicationContext.xml");
        dao = ctx.getBean("dao", FlooringMasteryDao.class);
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        dao.clearOrderList();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of addOrder method, of class FlooringMasteryDao.
     */
    @Test
    public void testAddGetOrder() throws Exception {
        Order order = new Order(100);
        order.setOrderDate(LocalDate.now());
        order.setCustomerName("Test Customer");
        order.setState("MN");
        order.setTaxRate(new BigDecimal("10"));
        order.setProductType("TILE");
        order.setArea(new BigDecimal("100"));
        order.setCostPerSquareFoot(new BigDecimal("5"));
        order.setLaborCostPerSquareFoot(new BigDecimal("20"));
        order.setMaterialCost();
        order.setLaborCost();
        order.setTotalTax();
        order.setOrderTotal();
        
        dao.addOrder(order);
        Order fromDao = dao.getOrder(LocalDate.now(), 100);
        assertEquals(order, fromDao);
    }
    
    /**
     * Test of removeOrder method, of class FlooringMasteryDao.
     */
    @Test
    public void testRemoveOrder() throws Exception {
        Order order = new Order(100);
        order.setOrderDate(LocalDate.of(1990,1,1));
        order.setCustomerName("Test Customer");
        order.setState("MN");
        order.setTaxRate(new BigDecimal("10"));
        order.setProductType("TILE");
        order.setArea(new BigDecimal("100"));
        order.setCostPerSquareFoot(new BigDecimal("5"));
        order.setLaborCostPerSquareFoot(new BigDecimal("20"));
        order.setMaterialCost();
        order.setLaborCost();
        order.setTotalTax();
        order.setOrderTotal();
        dao.addOrder(order);
        
        assertFalse(order.isDeleted());
        
        dao.removeOrder(order);
        assertTrue(order.isDeleted());
    }

    /**
     * Test of getAllOrdersForDate method, of class FlooringMasteryDao.
     */
    @Test
    public void testGetAllOrdersForDate() throws Exception {
        Order order = new Order(100);
        order.setOrderDate(LocalDate.of(1990, 1, 1));
        order.setCustomerName("Test Customer");
        order.setState("MN");
        order.setTaxRate(new BigDecimal("10"));
        order.setProductType("TILE");
        order.setArea(new BigDecimal("100"));
        order.setCostPerSquareFoot(new BigDecimal("5"));
        order.setLaborCostPerSquareFoot(new BigDecimal("20"));
        order.setMaterialCost();
        order.setLaborCost();
        order.setTotalTax();
        order.setOrderTotal();
        dao.addOrder(order);
        
        assertEquals(1, dao.getAllOrdersForDate(LocalDate.of(1990, 1, 1)).size());
        
        Order order2 = new Order(200);
        order2.setOrderDate(LocalDate.of(1990, 1, 1));
        order2.setCustomerName("Test Customer2");
        order2.setState("MN");
        order2.setTaxRate(new BigDecimal("1"));
        order2.setProductType("TILE");
        order2.setArea(new BigDecimal("1000"));
        order2.setCostPerSquareFoot(new BigDecimal("50"));
        order2.setLaborCostPerSquareFoot(new BigDecimal("25"));
        order2.setMaterialCost();
        order2.setLaborCost();
        order2.setTotalTax();
        order2.setOrderTotal();
        dao.addOrder(order2);
        
        assertEquals(2, dao.getAllOrdersForDate(LocalDate.of(1990, 1, 1)).size());
        
        dao.removeOrder(order);
        dao.removeOrder(order2);
        dao.saveCurrentChanges();
        assertEquals(0, dao.getAllOrdersForDate(LocalDate.of(1990, 1, 1)).size());
        
    }

    /**
     * Test of saveCurrentChanges method, of class FlooringMasteryDao.
     */
    @Test
    public void testSaveCurrentChanges() throws Exception {
        assertFalse(new File("orders/Orders_02162018.txt").exists());
        Order newOrder = new Order(200);
        newOrder.setOrderDate(LocalDate.now());
        newOrder.setCustomerName("Test Customer");
        newOrder.setState("MN");
        newOrder.setTaxRate(new BigDecimal("10"));
        newOrder.setProductType("TILE");
        newOrder.setArea(new BigDecimal("100"));
        newOrder.setCostPerSquareFoot(new BigDecimal("5"));
        newOrder.setLaborCostPerSquareFoot(new BigDecimal("20"));
        newOrder.setMaterialCost();
        newOrder.setLaborCost();
        newOrder.setTotalTax();
        newOrder.setOrderTotal();
        dao.addOrder(newOrder);
        dao.saveCurrentChanges();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
        String dateString = formatter.format(LocalDate.now());
        String fileName = "orders/Orders_" + dateString + ".txt";
        assertTrue(new File(fileName).exists());
    }

    /**
     * Test of getProductList method, of class FlooringMasteryDao.
     */
    @Test
    public void testGetProductList() throws Exception {
        // product file has 4 products
        assertEquals(4, dao.getProductList().values().size());
    }

    /**
     * Test of getStateTaxRatesList method, of class FlooringMasteryDao.
     */
    @Test
    public void testGetStateTaxRatesList() throws Exception {
        // state file has 4 states
        assertEquals(4, dao.getStateTaxRatesList().values().size());
    }

    /**
     * Test of getSystemState method, of class FlooringMasteryDao.
     */
    @Test
    public void testGetSystemState() throws Exception {
        // file is set to prod
        assertTrue(dao.getSystemState());
    }
    
    @Test
    public void testClearOrderList() throws Exception {
        Order order = new Order(100);
        order.setOrderDate(LocalDate.of(1990, 1, 1));
        order.setCustomerName("Test Customer");
        order.setState("MN");
        order.setTaxRate(new BigDecimal("10"));
        order.setProductType("TILE");
        order.setArea(new BigDecimal("100"));
        order.setCostPerSquareFoot(new BigDecimal("5"));
        order.setLaborCostPerSquareFoot(new BigDecimal("20"));
        order.setMaterialCost();
        order.setLaborCost();
        order.setTotalTax();
        order.setOrderTotal();
        dao.addOrder(order);
        
        Order order2 = new Order(200);
        order2.setOrderDate(LocalDate.of(1990, 1, 1));
        order2.setCustomerName("Test Customer2");
        order2.setState("MN");
        order2.setTaxRate(new BigDecimal("1"));
        order2.setProductType("TILE");
        order2.setArea(new BigDecimal("1000"));
        order2.setCostPerSquareFoot(new BigDecimal("50"));
        order2.setLaborCostPerSquareFoot(new BigDecimal("25"));
        order2.setMaterialCost();
        order2.setLaborCost();
        order2.setTotalTax();
        order2.setOrderTotal();
        dao.addOrder(order2);
        
        assertEquals(2, dao.getOrderList().values().size());
        dao.clearOrderList();
        assertEquals(0, dao.getOrderList().values().size());
    }
    
}
