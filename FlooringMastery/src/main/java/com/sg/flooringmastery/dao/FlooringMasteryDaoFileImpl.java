/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author blake
 */
public class FlooringMasteryDaoFileImpl implements FlooringMasteryDao {

    private final String TAX_FILE = "Taxes.txt";
    private final String PRODUCTS_FILE = "Products.txt";
    private final String SYSTEM_FILE = "System.txt";

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
            return orderList.put(order.getOrderNumber(), order);
        }

        return orderList.put(order.getOrderNumber(), order);

    }

    @Override
    public Order removeOrder(Order order) throws FlooringMasteryPersistenceException {
        try {
            loadOrdersList(order.getOrderDate());
        } catch (FlooringMasteryPersistenceException e) {
            return orderList.remove(order.getOrderNumber());
        }

        return orderList.remove(order.getOrderNumber());
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
    public void saveCurrentChanges() throws FlooringMasteryPersistenceException {
        PrintWriter out;
        Set<LocalDate> datesWritten = new HashSet<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMddyyyy");

        for (Order currentOrder : orderList.values()) {
            LocalDate date = currentOrder.getOrderDate();
            String fileName = ORDER_FILE_PREFIX + dateFormatter.format(date) + ".txt";

            if (datesWritten.contains(date)) {
                // append order to file
                try {
                    out = new PrintWriter(new FileWriter(fileName, true));
                } catch (IOException e) {
                    throw new FlooringMasteryPersistenceException(
                            "Could not save order data to file.");
                }

                writeOrder(currentOrder, out);

                out.flush();
                out.close();
            } else {
                // overwrite file with first order
                try {
                    out = new PrintWriter(new FileWriter(fileName));
                } catch (IOException e) {
                    throw new FlooringMasteryPersistenceException(
                            "Could not save order data to file.");
                }

                writeOrderHeaderRow(out);

                writeOrder(currentOrder, out);

                out.flush();
                out.close();

                datesWritten.add(date);
            }
        }
    } // end saveCurrentChanges

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

    @Override
    public boolean getSystemState() throws FlooringMasteryPersistenceException {
        Scanner scanner;
        boolean isProduction = false;

        try {
            scanner = new Scanner(new File(SYSTEM_FILE));
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryPersistenceException(
                    "Could not load system file.");
        }

        if (scanner.hasNext()) {
            isProduction = scanner.nextBoolean();
        }

        return isProduction;
    }

    @Override
    public void clearOrderList() {
        orderList.clear();
    }

    private void writeOrder(Order order, PrintWriter out) {
        if (order.isDeleted() == false) {
            out.println(order.getOrderNumber() + DELIMITER
                    + order.getCustomerName() + DELIMITER
                    + order.getState() + DELIMITER
                    + order.getTaxRate() + DELIMITER
                    + order.getProductType() + DELIMITER
                    + order.getArea() + DELIMITER
                    + order.getCostPerSquareFoot() + DELIMITER
                    + order.getLaborCostPerSquareFoot() + DELIMITER
                    + order.getMaterialCost() + DELIMITER
                    + order.getLaborCost() + DELIMITER
                    + order.getTotalTax() + DELIMITER
                    + order.getOrderTotal());
        }
    }
    
    private void writeOrderHeaderRow(PrintWriter out) {
        out.println("OrderNumber" + DELIMITER
                        + "CustomerName" + DELIMITER
                        + "State" + DELIMITER
                        + "TaxRate" + DELIMITER
                        + "ProductType" + DELIMITER
                        + "Area" + DELIMITER
                        + "CostPerSquareFoot" + DELIMITER
                        + "LaborCostPerSquareFoot" + DELIMITER
                        + "MaterialCost" + DELIMITER
                        + "LaborCost" + DELIMITER
                        + "Tax" + DELIMITER
                        + "Total");
    }

    private void loadOrdersList(LocalDate date) throws FlooringMasteryPersistenceException {

        // need to check if date has already been loaded into memory
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
