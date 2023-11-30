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

    public BasketViewController(NavigationManager navigationManager, Navigation id) {
        //initialise view link
        super(navigationManager, id);
        view = new BasketView(this);
        basketView = (BasketView) view;

    }

    public void onNavigateTo() {
        user = AppSessionCache.getInstance().getUserLoggedIn();
        userBasket = user.getBasket();
        basketView.onRefresh();
    }

    public void synchroniseDataWithDatabase() {
        try {
            OrderManager.updateUserBasket(user);
        } catch (SQLException e) {
            basketView.updateErrorMessage("Cannot connect to database");
        }
    }

    public Order getBasket() {
        return userBasket;
    }

    public void changeOrderlineQuantity(OrderLine orderline, int newQty) {
        orderline.setQuantity(newQty);
        userBasket.calculateTotalPrice();
    }

    public void deleteOrderline(OrderLine orderline) {
        try {
            OrderManager.deleteOrderline(userBasket, orderline);
            basketView.onRefresh();
        } catch (SQLException e) {
            basketView.updateErrorMessage("Cannot connect to database");
        }
    }

    /**
     * confirm the basket order and clear the basket view.
     * @throws SQLException If there is an SQL exception during the database update.
     * @throws BankDetailsNotValidException If bank details provided are not valid.
     * @throws InvalidOrderStateException If the order state is invalid.
     * @throws OrderQuantitiesInvalidException if order quantity is invalid.
     * @throws OrderOutdatedException if the order is outdated and synchronize the database.
     */
    public void confirmOrder() {
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
            e.addMessage("All quantities have been set to maximum available in stock.");
            errorMessage = e.getMessage();
            basketView.onRefresh();
        } catch (OrderOutdatedException e) {
            synchroniseDataWithDatabase();
            errorMessage = e.getMessage();
            onNavigateTo();
        } finally {
            basketView.updateErrorMessage(errorMessage);
        }
    }

    public void onNavigateLeave() {
        try {
            OrderManager.saveFullOrderState(userBasket);
        } catch (SQLException e) {
            navigation.setLayoutMessage("Basket error", "Basket could not be saved", true);
        }
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

    /**
     * Updates the user's bank details with the provided banking card and password.
     *
     * checks if a user has a CUSTOMER role. If not, assign them to customer role.
     * updates user and refreshes the navigation. Then, user's bank details are updated.
     * the provided banking card and password. If bank details are valid, order status is changed to confirmed.
     *
     * @param bankingCard banking card details to update.
     * @param password password for authentication.
     * @throws SQLException If there is an SQL exception during the database update.
     * @throws BankDetailsEncryptionException If there is an issue with encrypting the bank details.
     */
    public void updateUserBankDetails(BankingCard bankingCard, char[] password)
            throws SQLException, BankDetailsEncryptionException {
        if (!user.hasRole(User.Role.CUSTOMER)) {
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
