package uk.ac.sheffield.com2008.controller.staff;

import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.staff.ProductRecordView;
import uk.ac.sheffield.com2008.view.staff.SalesView;

public class SalesController extends ViewController {

    private final SalesView salesView;
    public SalesController(NavigationManager navigationManager, Navigation id){
        super(navigationManager, id);
        view = new SalesView(this);
        salesView = (SalesView) view;
    }
}
