package uk.ac.sheffield.com2008.controller.customer;

import uk.ac.sheffield.com2008.cache.AppSessionCache;
import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.exceptions.*;
import uk.ac.sheffield.com2008.model.dao.BankingDetailsDAO;
import uk.ac.sheffield.com2008.model.domain.data.OrderLine;
import uk.ac.sheffield.com2008.model.domain.managers.OrderManager;
import uk.ac.sheffield.com2008.model.domain.managers.UserManager;
import uk.ac.sheffield.com2008.model.entities.BankingCard;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.customer.BasketView;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class BasketViewController extends ViewController {
    public BasketView basketView;
    private Order userBasket;
    private User user;
    public BasketViewController(NavigationManager navigationManager, Navigation id){
        //initialise view link
        super(navigationManager, id);
        view = new BasketView(this);
        basketView = (BasketView) view;

    }

    public void onNavigateTo(){
        user = AppSessionCache.getInstance().getUserLoggedIn();
        userBasket = user.getBasket();
        userBasket.PrintFullOrder();
        basketView.onRefresh();
    }

    public Order getBasket(){
        return userBasket;
    }

    public void changeOrderlineQuantity(OrderLine orderline, int newQty){
        orderline.setQuantity(newQty);
        userBasket.calculateTotalPrice();
    }

    public void deleteOrderline(OrderLine orderline){
        System.out.println("deleting: " + orderline.getProduct().getName());
        OrderManager.deleteOrderline(userBasket, orderline);
        basketView.onRefresh();
    }

    /**
     * confirm the basket order
     * FOR NOW JUST SAVES THE ORDER
     */
    public void confirmOrder(){
        System.out.println("PRESSED CONFIRM");
        userBasket.PrintFullOrder();
        String errorMessage = null;
        try {
            OrderManager.confirmOrder(userBasket, user);
            basketView.openConfirmationScreen(userBasket);
            user.setBasket(OrderManager.createNewOrder(user));
            userBasket = user.getBasket();
            basketView.onRefresh();
        } catch (SQLException e) {
            errorMessage = "Cannot connect to database.";
        } catch (BankDetailsNotValidException e) {
            errorMessage = e.getMessage();
            basketView.startCardUpdateProcess();
        } catch (InvalidOrderStateException e) {
            errorMessage = e.getMessage();
        } catch (OrderQuantitiesInvalidException e) {
            errorMessage = e.getMessage();
            basketView.onRefresh();
        } catch (OrderOutdatedException e) {
            OrderManager.updateUserBasket(user);
            errorMessage = e.getMessage();
            onNavigateTo();
        } finally {
            basketView.updateErrorMessage(errorMessage);
        }
    }

    public void onNavigateLeave(){
        OrderManager.saveFullOrderState(userBasket);
    }

    public BankingCard getBankingCard(char[] password) throws SQLException, BankDetailsEncryptionException {
        try {
            return UserManager.fetchUserBankDetails(user, password);
        } catch (UserHasNoBankDetailsException e) {
            return null;
        }
    }

    public boolean hasUserBankingCard() throws SQLException {
        return BankingDetailsDAO.hasUserBankingDetails(user);
    }

    public void updateUserBankDetails(BankingCard bankingCard, char[] password)
            throws SQLException, BankDetailsEncryptionException {
        if(!user.hasRole(User.Role.CUSTOMER)) {
            user.addRole(User.Role.CUSTOMER);
            UserManager.updateUser(user);
            navigation.refreshNavigation();
        }

        UserManager.updateUserBankDetails(user, bankingCard, password);
        CompletableFuture.runAsync(this::confirmOrder);
    }

    public User getUser() {
        return user;
    }
}
