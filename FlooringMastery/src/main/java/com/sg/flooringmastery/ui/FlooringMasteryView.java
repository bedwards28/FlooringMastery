/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.ui;

import com.sg.flooringmastery.dto.Order;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;

/**
 *
 * @author blake
 */
public class FlooringMasteryView {

    private UserIO io;

    public FlooringMasteryView(UserIO io) {
        this.io = io;
    }

    public int displayMainMenuAndGetSelection() {
        io.print("***********************************");
        io.print("* <<Flooring Program>>");
        io.print("* 1. Display Orders by Date");
        io.print("* 2. Create an Order");
        io.print("* 3. Edit an Order");
        io.print("* 4. Delete an Order");
        io.print("* 5. Save Current Changes");
        io.print("* 6. Quit");
        io.print("*");
        io.print("***********************************");

        return io.readInt("Please select from the choices listed above: ", 1, 6);
    }

    public void displayUnknownCommandBanner() {
        io.print("Unknown Command!!!");
    }

    public void displayExitBanner() {
        io.print("Good Bye!!!");
    }

    public LocalDate promptUserForDate() {
        LocalDate date;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");

        while (true) {
            String input = io.readString("Please enter the search date as 'MMDDYYYY'");

            try {
                date = LocalDate.parse(input, formatter);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format");
            }
        }

        return date;
    }

    public void displayOrderList(List<Order> orders) {
        for (Order currentOrder : orders) {
            if (!currentOrder.isDeleted()) {
                System.out.println(
                        "Order No: " + currentOrder.getOrderNumber() + " | "
                        + "Customer: " + currentOrder.getCustomerName() + " | "
                        + "State: " + currentOrder.getState() + " | "
                        + "Product: " + currentOrder.getProductType() + " | "
                        + "Material Cost: " + currentOrder.getMaterialCost() + " | "
                        + "Labor Cost: " + currentOrder.getLaborCost() + " | "
                        + "Tax Total: " + currentOrder.getTotalTax() + " | "
                        + "Order Total: " + currentOrder.getOrderTotal());
            }
        }
    }

    public void displayOrder(Order order) {
        io.print("*****  New Order Summary *****");
        io.print("Order No: " + order.getOrderNumber() + " | "
                + "Customer: " + order.getCustomerName() + " | "
                + "State: " + order.getState() + " | "
                + "Tax Rate: " + order.getTaxRate() + " | "
                + "Product: " + order.getProductType() + " | "
                + "Area: " + order.getArea() + " | "
                + "Material Cost PSqF: " + order.getCostPerSquareFoot() + " | "
                + "Labor Cost PSqF: " + order.getLaborCostPerSquareFoot() + " | "
                + "Material Cost: " + order.getMaterialCost() + " | "
                + "Labor Cost: " + order.getLaborCost() + " | "
                + "Tax Total: " + order.getTotalTax() + " | "
                + "Order Total: " + order.getOrderTotal());
        
        io.print("");
    }

    public void displaySearchResultsBanner() {
        io.print("******  Search Results ******");
    }

    public Order getNewOrderDetails(int orderNumber) {
        Order newOrder = new Order(orderNumber);
        newOrder.setOrderDate(LocalDate.now());
        newOrder.setCustomerName(io.readString("Enter the customer's name:"));
        newOrder.setState(io.readString("Enter the state:").toUpperCase());
//        newOrder.setTaxRate(io.readBigDecimal("Enter the tax rate:")); // remove this from final build, must have state determine tax rate
        newOrder.setProductType(io.readString("Enter the product type:").toUpperCase()); // this must be validated against products file
        newOrder.setArea(io.readBigDecimal("Enter the total square footage of the project:"));
//        newOrder.setCostPerSquareFoot(io.readBigDecimal("Enter the material cost per square foot:"));
//        newOrder.setLaborCostPerSquareFoot(io.readBigDecimal("Enter the labor cost per square foot:"));
//        newOrder.setMaterialCost(io.readBigDecimal("Enter the total material cost:"));
//        newOrder.setLaborCost(io.readBigDecimal("Enter the total labor cost:"));

        io.print("");
        
        return newOrder;
    }

    public void displayInvalidStateBanner() {
        io.print("***** Invalid State Entered *****");
    }

    public String promptUserForValidState(Set<String> states) {
        io.print("Please enter a valid state.");
        for (String state : states) {
            io.print(state);
        }
        
        String state = io.readString("Choose from the states listed above:").toUpperCase();
        io.print("");
        return state;
    }

    public void displayInvalidProductBanner() {
        io.print("***** Invalid Product Entered *****");
    }

    public String promptUserForValidProduct(Set<String> validProducts) {
        io.print("Please enter a valid product.");
        for (String product : validProducts) {
            io.print(product);
        }
        
        String product = io.readString("Choose from the products listed above.").toUpperCase();
        io.print("");
        return product;
    }

}
