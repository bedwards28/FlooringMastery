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
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 *
 * @author blake
 */
public class FlooringMasteryDaoFileImpl implements FlooringMasteryDao {

    private final String CUSTOMER_FILE = "customers.txt";
    private final String TAX_FILE = "Taxes.txt";
    private final String PRODUCTS_FILE = "Products.txt";
    private final String ORDER_HISTORY_FILE = "orderHistory.txt";

    private final String ORDER_FILE_PREFIX = "orders/Orders_";
    private final String DELIMITER = ",";

    private String orderFile;

    private Map<Integer, Order> orderList = new HashMap<>();
    private Map<String, Tax> stateTaxList = new HashMap<>();
    private Map<String, Product> productList = new HashMap<>();

    @Override
    public Order addOrder(Order order) throws FlooringMasteryPersistenceException {
        try {
            loadOrdersList(LocalDate.now());  // load current date file if needed before adding new order
        } catch (FlooringMasteryPersistenceException e) {
            // This catch will never trigger.  If the loadOrdersList throws an exception, or not,
            // we want the finally block to add the order to the orderlist hashmap
        } finally {
            orderList.put(order.getOrderNumber(), order);
        }
        
        return order;
    }
    
//    @Override
//    public Order removeOrder(Order order) throws FlooringMasteryPersistenceException {
//        Order removedOrder = orderList.remove(order.getOrderNumber());
//        return removedOrder;
//    }

    @Override
    public Order removeOrder(LocalDate date, int orderId) throws FlooringMasteryPersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Order> getAllOrdersForDate(LocalDate date) throws FlooringMasteryPersistenceException {
        loadOrdersList(date);
        return orderList.values()
                .stream()
                .filter(o -> o.getOrderDate().equals(date))
                .collect(Collectors.toList());
    }

    @Override
    public Order getOrder(LocalDate date, int orderId) throws FlooringMasteryPersistenceException {
        try {
            loadOrdersList(date);  // load current date file if needed before adding new order
        } catch (FlooringMasteryPersistenceException e) {
            return null;
        }
        
        return orderList.get(orderId);
    }

    @Override
    public Order editOrder(LocalDate date, int orderId) throws FlooringMasteryPersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void saveCurrentChanges() throws FlooringMasteryPersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Customer addCustomer(Customer customer) throws FlooringMasteryPersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Customer removeCustomer(int customerId) throws FlooringMasteryPersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Customer editCustomer(int customerId, Customer customer) throws FlooringMasteryPersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Customer> getAllCustomers() throws FlooringMasteryPersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, Product> getProductList() throws FlooringMasteryPersistenceException {
        loadProductList();
        return productList;
    }

    @Override
    public Map<String, Tax> getStateTaxRatesList() throws FlooringMasteryPersistenceException {
        loadStateTaxList();
        return stateTaxList;
    }

//    private void loadAllFiles() {
//
//    }
    
    private void loadOrdersList(LocalDate date) throws FlooringMasteryPersistenceException {

        // clear out old data in order list before populating 
        // with new items for selected date
//        Set<Integer> keys = orderList.keySet();
//        for (Integer orderKey : keys) {
//            orderList.remove(orderKey);
//        }
        // need to check if date has already been loaded into memory before 
        if (!isDateLoaded(date)) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
            String dateFormatted = date.format(formatter);
            String fileName = ORDER_FILE_PREFIX + dateFormatted + ".txt";

            Scanner scanner;

            try {
                scanner = new Scanner(new File(fileName));
            } catch (FileNotFoundException e) {
                throw new FlooringMasteryPersistenceException(
                        "Could not load orders file into memory.", e);
            }

            // Get rid of header row
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            String currentLine;
            String[] currentTokens;

            while (scanner.hasNextLine()) {
                currentLine = scanner.nextLine();
                currentTokens = currentLine.split(DELIMITER);
                Order currentOrder = new Order(Integer.parseInt(currentTokens[0]));
                currentOrder.setCustomerName(currentTokens[1]);
                currentOrder.setState(currentTokens[2]);
                currentOrder.setTaxRate(new BigDecimal(currentTokens[3]));
                currentOrder.setProductType(currentTokens[4]);
                currentOrder.setArea(new BigDecimal(currentTokens[5]));
                currentOrder.setCostPerSquareFoot(new BigDecimal(currentTokens[6]));
                currentOrder.setLaborCostPerSquareFoot(new BigDecimal(currentTokens[7]));
                currentOrder.setMaterialCost(new BigDecimal(currentTokens[8]));
                currentOrder.setLaborCost(new BigDecimal(currentTokens[9]));
                currentOrder.setTotalTax(new BigDecimal(currentTokens[10]));
                currentOrder.setOrderTotal(new BigDecimal(currentTokens[11]));
//                currentOrder.setDeleted(Boolean.parseBoolean(currentTokens[12]));

                currentOrder.setOrderDate(date);

                orderList.put(currentOrder.getOrderNumber(), currentOrder);
            }

            scanner.close();

        } // end if(isDateLoaded)

    } // end loadOrdersList

    private void loadStateTaxList() throws FlooringMasteryPersistenceException {
        Scanner scanner;

        try {
            scanner = new Scanner(new File(TAX_FILE));
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryPersistenceException(
                    "Could not load tax data file into memory.", e);
        }

        // clear header
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        String currentLine;
        String[] currentTokens;

        while (scanner.hasNext()) {
            currentLine = scanner.nextLine();
            currentTokens = currentLine.split(DELIMITER);
            Tax tax = new Tax(currentTokens[0], new BigDecimal(currentTokens[1]));
            stateTaxList.put(tax.getState().toUpperCase(), tax);
        }

        scanner.close();
    } // end loadStateTaxList

    private void loadProductList() throws FlooringMasteryPersistenceException {
        Scanner scanner;

        try {
            scanner = new Scanner(new File(PRODUCTS_FILE));
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryPersistenceException(
                    "Could not load product list into memory", e);
        }
        
        String currentLine;
        String[] currentTokens;
        
        // clear header row
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
        
        while (scanner.hasNext()) {
            currentLine = scanner.nextLine();
            currentTokens = currentLine.split(DELIMITER);
            Product currentProduct = new Product();
            currentProduct.setProductType(currentTokens[0]);
            currentProduct.setCostPerSquareFoot(new BigDecimal(currentTokens[1]));
            currentProduct.setLaborCostPerSquareFoot(new BigDecimal(currentTokens[2]));
            productList.put(currentProduct.getProductType().toUpperCase(), currentProduct);
        }
        
        scanner.close();
    } // end loadProductsList

    private boolean isDateLoaded(LocalDate date) {
        List<Order> orders = new ArrayList<>(orderList.values());

        if (orders.isEmpty()) {
            return false;
        }

        for (Order currentOrder : orders) {
            if (date.equals(currentOrder.getOrderDate())) {
                return true;
            }
        }

        return false;
    } // end isDateLoaded

}
