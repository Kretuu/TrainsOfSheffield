package uk.ac.sheffield.com2008.view.staff;
import uk.ac.sheffield.com2008.controller.staff.SalesController;
import uk.ac.sheffield.com2008.view.View;

import java.awt.*;

public class SalesView extends View{
    SalesController salesController;

    public SalesView(SalesController salesController){
        this.salesController = salesController;
        InitializeUI();
    }

    public void InitializeUI() {
        setLayout(new BorderLayout());
    }
}
