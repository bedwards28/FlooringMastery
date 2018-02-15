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
        boolean saveChanges = false;
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
                        saveChanges = view.promptUserToSaveChanges();
                        if (saveChanges) {
                            saveCurrentWork();
                        }
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

        validateStateAndTaxRate(newOrder);

        validateProductDetails(newOrder);

        // set remaining order details
        newOrder.setOrderDate(LocalDate.now());
        newOrder.setMaterialCost();
        newOrder.setLaborCost();
        newOrder.setTotalTax();
        newOrder.setOrderTotal();

        view.displayOrder(newOrder);

        boolean commit = view.promptUserToCommitCreate();

        if (commit) {
            try {
                service.saveOrder(newOrder);
                view.displayOrderCreateSuccessBanner();
            } catch (FlooringMasteryPersistenceException e) {
                view.displaySaveErrorBanner();
            }

        } else {
            view.displayOrderCreateUnsuccessfulBanner();
        }
    }

    private void editOrder() throws FlooringMasteryPersistenceException {
        view.displayEditOrderBanner();
        LocalDate date = view.promptUserForDate();
        int orderNumber = view.promptUserForOrderNumber();
        Order editOrder = service.getOrder(date, orderNumber);

        if (editOrder != null) {
            view.promptUserForEdits(editOrder);

            validateStateAndTaxRate(editOrder);

            validateProductDetails(editOrder);

            view.displayEditSuccessMessage();
            view.displayOrder(editOrder);

        } else {
            view.displayNoOrderFoundMessage();
        }
    }

    private void removeOrder() throws FlooringMasteryPersistenceException {
        boolean commit;

        view.displayRemoveOrderBanner();
        LocalDate date = view.promptUserForDate();
        int orderNumber = view.promptUserForOrderNumber();

        Order removedOrder = service.getOrder(date, orderNumber);

        if (removedOrder != null) {
            view.displayOrder(removedOrder);
            commit = view.promptUserToCommitDelete();
            if (commit) {
                removedOrder.setDeleted(true);
                view.displayMarkedForDeleteBanner();
            }
        } else {
            view.displayNoOrderFoundMessage();
        }
    }

    private void saveCurrentWork() throws FlooringMasteryPersistenceException {
        boolean isProduction = service.getSystemState();
        if (isProduction) {
            service.saveCurrentChanges();
        } else {
            view.displayTrainingBanner();
        }
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

    private void validateStateAndTaxRate(Order order) throws FlooringMasteryPersistenceException {
        while (true) {
            try {
                service.getTaxDetails(order);
                break;
            } catch (InvalidStateException e) {
                view.displayInvalidStateBanner();
                order.setState(getValidState());
            }
        }
    }

    private void validateProductDetails(Order order) throws FlooringMasteryPersistenceException {
        while (true) {
            try {
                service.getProductDetails(order);
                break;
            } catch (InvalidProductException e) {
                view.displayInvalidProductBanner();
                order.setProductType(getValidProduct());
            }
        }
    }

}
