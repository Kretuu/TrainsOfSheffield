package uk.ac.sheffield.com2008.view.customer;

import uk.ac.sheffield.com2008.controller.customer.BasketController;
import uk.ac.sheffield.com2008.view.View;

import javax.swing.*;

public class BasketView extends View {
    BasketController basketController;
    public BasketView(BasketController controller){
        basketController = controller;
        InitializeUI();
    }

    public void onRefresh(){
        removeAll();
        InitializeUI();
    }

    public void InitializeUI(){
        JLabel basketLabel = new JLabel("Your Basket");
        add(basketLabel);
    }
}
