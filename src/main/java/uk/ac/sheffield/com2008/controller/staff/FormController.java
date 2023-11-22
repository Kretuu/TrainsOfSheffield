package uk.ac.sheffield.com2008.controller.staff;

import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.staff.ProductRecordForm;


public class FormController extends ViewController {

    private final ProductRecordForm productRecordForm;

    public FormController(NavigationManager navigationManager, Navigation id){
        super(navigationManager, id);
        view = new ProductRecordForm(this);
        productRecordForm = (ProductRecordForm) view;

    }

}
