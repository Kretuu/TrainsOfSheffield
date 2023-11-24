package uk.ac.sheffield.com2008.controller.staff;

import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.staff.EditProductRecordForm;

public class EditFormController extends ViewController {
    private final EditProductRecordForm editProductRecordForm;

    public EditFormController(NavigationManager navigationManager, Navigation id){
        super(navigationManager, id);
        view = new EditProductRecordForm(this);
        editProductRecordForm = (EditProductRecordForm) view;
    }
}
