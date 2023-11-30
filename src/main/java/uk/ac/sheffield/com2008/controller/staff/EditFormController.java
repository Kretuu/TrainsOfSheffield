package uk.ac.sheffield.com2008.controller.staff;

import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.model.dao.ProductDAO;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.staff.EditProductRecordForm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EditFormController extends ViewController {
    private final EditProductRecordForm editProductRecordForm;
    private List<Product> allProducts = new ArrayList<>();

    private Product productUnderEdit;

    public EditFormController(NavigationManager navigationManager, Navigation id){
        super(navigationManager, id);
        view = new EditProductRecordForm(this);
        editProductRecordForm = (EditProductRecordForm) view;
    }

    public void onNavigateTo(){
        try {
            allProducts = ProductDAO.getAllProducts();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void forceRefresh(){
        editProductRecordForm.onRefresh();
        System.out.println("editing product: " + productUnderEdit.printName());
    }

    public List<Product> getAllProducts(){
        return allProducts;
    }

    public void setProductUnderEdit(Product productUnderEdit) {
        this.productUnderEdit = productUnderEdit;
    }
    public Product getProductUnderEdit(){
        return productUnderEdit;
    }
}
