package uk.ac.sheffield.com2008.controller;

import uk.ac.sheffield.com2008.view.ManageStockView;

/**
 * Controller responsible for Staff Screens
 */
public class StaffController extends ViewController{

    public ManageStockView manageStockView;
    public StaffController(){
        manageStockView = new ManageStockView(this);
        setFrameContent(manageStockView);
    }


}
