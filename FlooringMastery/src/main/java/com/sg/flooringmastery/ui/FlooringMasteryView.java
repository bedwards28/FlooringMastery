/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.ui;

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
        io.print("* 1. Display Orders");
        io.print("* 2. Add an Order");
        io.print("* 3. Edit an Order");
        io.print("* 4. Remove and Order");
        io.print("* 5. Save Current Work");
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
    
}
