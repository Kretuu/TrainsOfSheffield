package uk.ac.sheffield.com2008.controller.staff;

import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.exceptions.InvalidDatabaseDataException;
import uk.ac.sheffield.com2008.model.dao.ProductDAO;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.model.entities.products.ProductSet;
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

    public EditFormController(NavigationManager navigationManager, Navigation id) {
        super(navigationManager, id);
        view = new EditProductRecordForm(this);
        editProductRecordForm = (EditProductRecordForm) view;
    }

    public void onNavigateTo() {
        try {
            allProducts = ProductDAO.getAllProducts();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void forceRefresh() {
        editProductRecordForm.onRefresh();
    }


    /**
     * Pass in the product into the database if there is no erroe
     *
     * @param productUnderEdit
     */
    public void tryUpdateProduct(Product productUnderEdit) throws InvalidDatabaseDataException {
        editProductRecordForm.setErrorMessage("");

        if (!editProductRecordForm.validateAllFields()) {
            editProductRecordForm.setErrorMessage("Error: Invalid Fields");
            return;
        }

        //Check if product code already exists
        Product existsProduct = null;
        try {
            existsProduct = ProductDAO.getProductByCode(productUnderEdit.getProductCode());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (existsProduct == null) {
            editProductRecordForm.setErrorMessage("Error: Product of code " +
                    productUnderEdit.getProductCode() +
                    " does not exist");
            return;
        }

        //load the product with the new values
        productUnderEdit = editProductRecordForm.loadProductByType(
                productUnderEdit
        );

        //validate if it is a set
        if (productUnderEdit instanceof ProductSet) {
            String errorMsg = ((ProductSet) productUnderEdit).validateSet();
            if (errorMsg != null) {
                editProductRecordForm.setErrorMessage(errorMsg);
                return;
            }
        }

        //update the database to reflect the new state of this set
        try{
            ProductDAO.updateProduct(productUnderEdit);
        }catch(SQLException e){
            navigation.setLayoutMessage("Update Error",
                    "Could not connect to database",
                    true);
            e.printStackTrace();
        }

        //finally navigate back
        getNavigation().navigate(Navigation.PRODUCT_RECORD);
    }

    public List<Product> getAllProducts() {
        return allProducts;
    }

    public Product getProductUnderEdit() {
        return productUnderEdit;
    }

    public void setProductUnderEdit(Product productUnderEdit) {
        this.productUnderEdit = productUnderEdit;
    }
}
