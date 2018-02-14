/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.controller;

import com.sg.flooringmastery.dao.FlooringMasteryPersistenceException;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.service.FlooringMasteryServiceLayer;
import com.sg.flooringmastery.service.InvalidProductException;
import com.sg.flooringmastery.service.InvalidStateException;
import com.sg.flooringmastery.ui.FlooringMasteryView;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 *
 * @author blake
 */
public class FlooringMasteryController {

    FlooringMasteryServiceLayer service;
    FlooringMasteryView view;

    public void run() {
        boolean keepGoing = true;
        int menuSelection = 0;

        try {
            while (keepGoing) {

                menuSelection = getMenuSelection();

                switch (menuSelection) {
                    case 1:
                        displayOrders();
                        break;
                    case 2:
                        createOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
                        break;
                    case 5:
                        saveCurrentWork();
                        break;
                    case 6:
                        keepGoing = false;
                        break;
                    default:
                        unknownCommand();
                }

            } // end main while loop            
        } catch (Exception e) {
            System.out.println(e.getClass());
        }

        exitMessage();

    } // end run method

    public FlooringMasteryController(FlooringMasteryServiceLayer service, FlooringMasteryView view) {
        this.service = service;
        this.view = view;
    }

    private int getMenuSelection() {
        return view.displayMainMenuAndGetSelection();
    }

    private void displayOrders() throws FlooringMasteryPersistenceException {
        LocalDate date = view.promptUserForDate();
        view.displaySearchResultsBanner();
        try {
            List<Order> orders = service.getOrdersListByDate(date);
            view.displayOrderList(orders);
        } catch (FlooringMasteryPersistenceException e) {
            System.out.println("No orders found for date entered. Returning to main menu.");
        }
    }

    private void createOrder() throws
            FlooringMasteryPersistenceException,
            InvalidStateException {

        int orderNumber = service.getNewOrderNumber();
        Order newOrder = view.getNewOrderDetails(orderNumber);

        // get tax data for order
        while (true) {
            try {
                newOrder = service.getTaxDetails(newOrder);
                break;
            } catch (InvalidStateException e) {
                view.displayInvalidStateBanner();
                newOrder.setState(getValidState());
            }
        }
        
        // get product details
        while (true) {
            try {
                newOrder = service.getProductDetails(newOrder);
                break;
            } catch (InvalidProductException e) {
                view.displayInvalidProductBanner();
                newOrder.setProductType(getValidProduct());
            }
        }

        // display order summary
        view.displayOrder(newOrder);

        // ask user if they want to commit changes
        // if no, discard and return to main
        // if yes, save changes and return to main
    }

    private void editOrder() {
        System.out.println("edit order");
    }

    private void removeOrder() {
        System.out.println("remove order");
    }

    private void saveCurrentWork() {
        System.out.println("save work");
    }

    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }

    private void exitMessage() {
        view.displayExitBanner();
    }
    
    private String getValidState() throws FlooringMasteryPersistenceException {
        Set<String> validStates = service.getTaxStates();
        return view.promptUserForValidState(validStates).toUpperCase();
    }

    private String getValidProduct() throws FlooringMasteryPersistenceException {
        Set<String> validProducts = service.getValidProductsList();
        return view.promptUserForValidProduct(validProducts);
    }

}
