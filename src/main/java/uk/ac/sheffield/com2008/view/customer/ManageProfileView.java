package uk.ac.sheffield.com2008.view.customer;

import uk.ac.sheffield.com2008.controller.customer.ManageProfileController;

import java.awt.*;

public class ManageProfileView extends CustomerView {
    private ManageProfileController controller;

    public ManageProfileView(ManageProfileController controller) {
        this.controller = controller;

        initialiseUI();
    }

    private void initialiseUI() {
        setLayout(new BorderLayout());


    }
}
