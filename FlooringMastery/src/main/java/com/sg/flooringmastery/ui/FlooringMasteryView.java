/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.ui;

import com.sg.flooringmastery.dto.Order;
import java.math.BigDecimal;
import java.text.NumberFormat;
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
        io.print("");
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
        io.print("");
        io.print("GOOD BYE!!!");
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
        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        for (Order currentOrder : orders) {
            if (!currentOrder.isDeleted()) {
                System.out.println(
                        "Order No: " + currentOrder.getOrderNumber() + " | "
                        + "Customer: " + currentOrder.getCustomerName() + " | "
                        + "State: " + currentOrder.getState() + " | "
                        + "Product: " + currentOrder.getProductType() + " | "
                        + "Material Cost: " + formatter.format(currentOrder.getMaterialCost()) + " | "
                        + "Labor Cost: " + formatter.format(currentOrder.getLaborCost()) + " | "
                        + "Tax Total: " + formatter.format(currentOrder.getTotalTax()) + " | "
                        + "Order Total: " + formatter.format(currentOrder.getOrderTotal()));
            }
        }
        io.print("");
    }

    public void displayOrder(Order order) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        io.print("*****  Order Summary *****");
        io.print("Order No: " + order.getOrderNumber() + "\n"
                + "Customer: " + order.getCustomerName() + "\n"
                + "State: " + order.getState() + "\n"
                + "Tax Rate: " + order.getTaxRate() + "\n"
                + "Product: " + order.getProductType() + "\n"
                + "Area: " + order.getArea() + "\n"
                + "Material Cost (per square foot): " + formatter.format(order.getCostPerSquareFoot()) + "\n"
                + "Labor Cost (per square foot): " + formatter.format(order.getLaborCostPerSquareFoot()) + "\n"
                + "Material Cost: " + formatter.format(order.getMaterialCost()) + "\n"
                + "Labor Cost: " + formatter.format(order.getLaborCost()) + "\n"
                + "Tax Total: " + formatter.format(order.getTotalTax()) + "\n"
                + "Order Total: " + formatter.format(order.getOrderTotal()));

        io.print("");
    }

    public void displaySearchResultsBanner() {
        io.print("");
        io.print("******  Search Results ******");
    }

    public Order getNewOrderDetails(int orderNumber) {
        Order newOrder = new Order(orderNumber);
        newOrder.setOrderDate(LocalDate.now());
        newOrder.setCustomerName(io.readString("Enter the customer's name:"));
        newOrder.setState(io.readString("Enter the state:").toUpperCase());
        newOrder.setProductType(io.readString("Enter the product type:").toUpperCase());
        newOrder.setArea(io.readBigDecimal("Enter the total square footage of the project:"));

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

    public boolean promptUserToCommitCreate() {
        while (true) {
            String userInputString
                    = io.readString("Would you like to create this order? Enter (y)es or (n)o:");

            switch (userInputString.toLowerCase().charAt(0)) {
                case 'y':
                    return true;
                case 'n':
                    return false;
                default:
                    io.print("Invalid entry.  Please enter yes or no to continue.");
                    break;
            }
        }
    }

    public boolean promptUserToCommitDelete() {
        while (true) {
            String userInputString
                    = io.readString("Are you sure you would like to delete this order? Enter (y)es or (n)o:");

            switch (userInputString.toLowerCase().charAt(0)) {
                case 'y':
                    return true;
                case 'n':
                    return false;
                default:
                    io.print("Invalid entry.  Please enter yes or no to continue.");
                    break;
            }
        }
    }

    public void displayOrderCreateUnsuccessfulBanner() {
        io.print("");
        io.print("*****  Order was not created  *****");
    }

    public void displayOrderCreateSuccessBanner() {
        io.print("");
        io.print("*****  Temporary order created successfully  *****");
        io.print("Please select option 5 (Save current work) from the main menu to make the create permanent.");
    }

    public void displaySaveErrorBanner() {
        io.print("");
        io.print("*****  Unable to save order  *****");
    }

    public void displayRemoveOrderBanner() {
        io.print("");
        io.print("*****  Delete Order  *****");
    }

    public int promptUserForOrderNumber() {
        return io.readInt("Enter the order number:");
    }

    public void displayNoOrderFoundMessage() {
        io.print("No orders found matching search criteria.");
    }

    public void displayMarkedForDeleteBanner() {
        io.print("*****  Order is marked for deletion  *****");
        io.print("Select option 5 from the main menu to make the deletion permanent.");
    }

    public void displayEditOrderBanner() {
        io.print("");
        io.print("*****  Edit Order  *****");
    }

    public Order promptUserForEdits(Order editOrder) {
        io.print("Enter edits below.  Hitting enter without making edits will retain original order information.");
        io.print("The original order information is in parentheses.");
        String customerName = io.readString("Enter customer name (" + editOrder.getCustomerName() + "): ");
        String state = io.readString("Enter state (" + editOrder.getState() + "): ").toUpperCase();
        String productType = io.readString("Enter product type (" + editOrder.getProductType() + "): ").toUpperCase();
        String area = io.readString("Enter total area (" + editOrder.getArea() + "): ");

        if (!"".equals(customerName)) {
            editOrder.setCustomerName(customerName);
        }

        if (!"".equals(state)) {
            editOrder.setState(state);
        }

        if (!"".equals(productType)) {
            editOrder.setProductType(productType);
        }

        if (!"".equals(area)) {
            try {
                BigDecimal areaBd = new BigDecimal(area);
                editOrder.setArea(areaBd);
            } catch (NumberFormatException e) {
                io.print("Invalid area entry. No changes made to order.");
            }
        }

        return editOrder;
    }

    public void displayEditSuccessMessage() {
        io.print("The order has been successfully edited.  These edits are temporary.");
        io.print("Select option 5 (Save current work) at the main menu to make the edits permanent.");
        io.print("");
    }

    public void displayTrainingBanner() {
        io.print("******  SYSTEM IS IN TRAINING MODE. NO CHANGES CAN BE SAVED  ******");
    }

    public boolean promptUserToSaveChanges() {
        io.print("");
        io.print("All work for the current session must be saved in order to be retained.");
        io.print("Any changes made since the last system save (Option 5 on the main menu) will be lost if not saved.");
        io.print("Would you like to save your current changes before exiting?");

        while (true) {
            String input = io.readString("Enter (y)es or (n)o to continue.");

            switch (input.toLowerCase().charAt(0)) {
                case 'y':
                    return true;
                case 'n':
                    return false;
                default:
                    io.print("Invalid entry.");
                    break;
            }
        }
    }

}
