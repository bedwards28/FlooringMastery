/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.controller;

import com.sg.flooringmastery.service.FlooringMasteryServiceLayer;
import com.sg.flooringmastery.ui.FlooringMasteryView;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
        
        while (keepGoing) {
            
            menuSelection = getMenuSelection();
            
            switch(menuSelection) {
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
        
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
        String text = date.format(formatter);
        System.out.println(text);
        
        exitMessage();
        
    } // end run method

    public FlooringMasteryController(FlooringMasteryServiceLayer service, FlooringMasteryView view) {
        this.service = service;
        this.view = view;
    }
    
    private int getMenuSelection() {
        return view.displayMainMenuAndGetSelection();
    }
    
    private void displayOrders() {
        System.out.println("display orders");
    }
    
    private void createOrder() {
        System.out.println("create order");
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
    
}
