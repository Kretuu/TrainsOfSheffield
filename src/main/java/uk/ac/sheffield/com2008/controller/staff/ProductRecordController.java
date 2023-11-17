package uk.ac.sheffield.com2008.controller.staff;

import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.staff.ManageStockView;
import uk.ac.sheffield.com2008.view.staff.ProductRecordView;

public class ProductRecordController extends ViewController {
    private final ProductRecordView productRecordView;

    public ProductRecordController(NavigationManager navigationManager, Navigation id){
        super(navigationManager, id);
        view = new ProductRecordView(this);
        productRecordView = (ProductRecordView) view;
    }
}
