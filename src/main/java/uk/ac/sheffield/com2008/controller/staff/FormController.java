package uk.ac.sheffield.com2008.controller.staff;

import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.model.dao.ProductDAO;
import uk.ac.sheffield.com2008.model.domain.managers.ProductManager;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.model.entities.products.ProductSet;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.staff.ProductRecordForm;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class FormController extends ViewController {

    private List<Product> allProducts = new ArrayList<>();
    ProductRecordForm productRecordForm;

    public FormController(NavigationManager navigationManager, Navigation id){
        super(navigationManager, id);
        view = new ProductRecordForm(this);
        productRecordForm = (ProductRecordForm) view;
    }

    public void onNavigateTo(){
        try {
            allProducts = ProductManager.getAllProducts();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        productRecordForm.onRefresh();
    }


    public List<Product> getAllProducts(){
        return allProducts;
    }


    /**
     * When called with revalidate all fields, and will check if the product
     * code is unique. If no errors, will build a product from passed in parameters
     * @param productCode
     */
    public void tryCreateProduct(String productCode ){
        productRecordForm.setErrorMessage("");

        if(!productRecordForm.validateAllFields()){
            productRecordForm.setErrorMessage("Error: Invalid Fields");
            return;
        }
        //Check if product code already exists
        Product existsProduct = null;
        try {
            existsProduct = ProductDAO.getProductByCode(productCode);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(existsProduct != null){
            productRecordForm.setErrorMessage("Error: Product of code " + productCode + " already exists");
            return;
        }

        //otherwise create a product of the passed in type
        Product loadedProduct = productRecordForm.getProductFromInputs(productCode.charAt(0));

        if(loadedProduct instanceof ProductSet){
            String errorMsg = ((ProductSet) loadedProduct).validateSet();
            if(errorMsg != null){
                productRecordForm.setErrorMessage(errorMsg);
                return;
            }
        }

        try{
            ProductDAO.createProduct(loadedProduct);
        }catch(SQLException e){
            navigation.setLayoutMessage("Insertion Error",
                    "Could not connect to database",
                    true);
            e.printStackTrace();
        }

        //finally navigate back
        getNavigation().navigate(Navigation.PRODUCT_RECORD);
    }

}
