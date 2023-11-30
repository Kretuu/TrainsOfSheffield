package uk.ac.sheffield.com2008.view.customer;

import uk.ac.sheffield.com2008.cache.AppSessionCache;
import uk.ac.sheffield.com2008.config.Colors;
import uk.ac.sheffield.com2008.config.Symbols;
import uk.ac.sheffield.com2008.controller.customer.BasketViewController;
import uk.ac.sheffield.com2008.exceptions.BankDetailsEncryptionException;
import uk.ac.sheffield.com2008.model.dao.UserDAO;
import uk.ac.sheffield.com2008.model.domain.data.OrderLine;
import uk.ac.sheffield.com2008.model.domain.managers.UserManager;
import uk.ac.sheffield.com2008.model.entities.BankingCard;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.view.components.Button;
import uk.ac.sheffield.com2008.view.modals.NotificationModal;
import uk.ac.sheffield.com2008.view.modals.OrderModal;
import uk.ac.sheffield.com2008.view.modals.UpdateCreditCardModal;
import uk.ac.sheffield.com2008.view.modals.VerifyPasswordModal;
import uk.ac.sheffield.com2008.view.user.UserView;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

import static uk.ac.sheffield.com2008.util.math.Rounding.roundToDecimalPlaces;

public class BasketView extends UserView {
    BasketViewController basketViewController;
    JLabel totalTextLabel;
    private final JLabel errorMessage = new JLabel(" ");
    public BasketView(BasketViewController basketViewController){
        this.basketViewController = basketViewController;
        InitializeUI();
    }

    public void onRefresh(){
        removeAll();
        InitializeUI();
        revalidate();
        repaint();

        String orderTotal = "Total: " + Symbols.getChar("£") + roundToDecimalPlaces(basketViewController.getBasket().getTotalPrice(), 2);
        totalTextLabel.setText(orderTotal);
    }

    public void InitializeUI(){
        setLayout(new BorderLayout());

        ArrayList<OrderLine> orderLines = new ArrayList<>();
        Order userBasket = basketViewController.getBasket();
        if(userBasket != null){
            orderLines  = (ArrayList<OrderLine>) userBasket.getOrderLines();
        }

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        // Create a JPanel for the scroll panel with orderline rows
        JPanel basketPanel = new JPanel();
        basketPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5); // Add some padding

        // Create empty border for inner padding
        Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        basketPanel.setBorder(emptyBorder);

        JLabel basketTitle = new JLabel("Your Basket");
        basketTitle.setFont(basketTitle.getFont().deriveFont(Font.BOLD, 24));
        basketTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        Border titleBorder = BorderFactory.createEmptyBorder(10,10,10,10);
        basketTitle.setBorder(titleBorder);

        topPanel.add(basketTitle, BorderLayout.NORTH);

        //create orderlines
        for(OrderLine orderLine : orderLines){

            Product lineProduct = orderLine.getProduct();
            JLabel orderLineText = new JLabel(lineProduct.getName());
            JLabel orderLineUnitPrice = new JLabel(Symbols.getChar("£") + lineProduct.getPrice() + " x ");
            orderLineUnitPrice.setForeground(new Color(117, 117, 117));

            JLabel orderLineTotalPrice = new JLabel(Symbols.getChar("£") + roundToDecimalPlaces(
                    lineProduct.getPrice() * orderLine.getQuantity(), 2));
            orderLineTotalPrice.setFont(orderLineTotalPrice.getFont().deriveFont(Font.BOLD, 12));

            //Quantity Spinner
            int quantityInOrderline = orderLine.getQuantity();
            int maxStock = lineProduct.getStock();

            SpinnerModel spinnerModel = new SpinnerNumberModel(
                    quantityInOrderline,
                    1,
                    maxStock,
                    1);
            JSpinner quantitySpinner = new JSpinner(spinnerModel);
            quantitySpinner.addChangeListener(e -> {
                changeQuantityOfOrderline(orderLine, (int) spinnerModel.getValue(), orderLineTotalPrice);
            });

            gbc.anchor = GridBagConstraints.WEST;
            basketPanel.add(orderLineText, gbc);

            gbc.gridx++;
            gbc.weightx = 1.0;
            gbc.anchor = GridBagConstraints.EAST;
            basketPanel.add(orderLineUnitPrice, gbc);

            gbc.gridx++;
            gbc.anchor = GridBagConstraints.EAST;
            gbc.weightx = 0.0;
            basketPanel.add(quantitySpinner, gbc);

            gbc.gridx++;
            gbc.anchor = GridBagConstraints.EAST;
            basketPanel.add(orderLineTotalPrice, gbc);

            // Add the delete button at the end of the row
            JButton deleteButton = new Button("X");
            deleteButton.addActionListener(e -> {
                deleteOrderline(orderLine);
            });
            gbc.gridx = GridBagConstraints.RELATIVE; // Move to the next cell
            gbc.anchor = GridBagConstraints.CENTER;
            basketPanel.add(deleteButton, gbc);

            // Reset grid constraints for the next row
            gbc.gridx = 0;
            gbc.gridy++;
            gbc.weightx = 0.0; // Reset the weight
        }

        // Create a JScrollPane and set the basket panel as its view
        if(!orderLines.isEmpty()){
            JScrollPane scrollPane = new JScrollPane(basketPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            topPanel.add(scrollPane, BorderLayout.CENTER);
        }
        else{

        }

        add(topPanel, BorderLayout.NORTH);

        //total cost and confirm order button
        JPanel bottomSection = new JPanel();
        // Create empty border for inner padding
        Border emptyBorder2 = BorderFactory.createEmptyBorder(5, 0, 70, 30);
        bottomSection.setBorder(emptyBorder2);
        bottomSection.setLayout(new BorderLayout());
        totalTextLabel = new JLabel("Total: ");
        totalTextLabel.setFont(totalTextLabel.getFont().deriveFont(Font.BOLD, 24));

        JButton confirmButton = new Button("Confirm & Pay");
        confirmButton.setFont(confirmButton.getFont().deriveFont(Font.BOLD, 24));
        confirmButton.addActionListener(e -> basketViewController.confirmOrder());

        JPanel errorMessageHolder = new JPanel();
        errorMessage.setForeground(Colors.TEXT_FIELD_ERROR);
        errorMessageHolder.add(errorMessage);
        bottomSection.add(errorMessageHolder, BorderLayout.CENTER);

        // Set alignments to right
        totalTextLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        confirmButton.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JPanel eastBottomSectionHolder = new JPanel();
        eastBottomSectionHolder.setLayout(new BoxLayout(eastBottomSectionHolder, BoxLayout.Y_AXIS));
        eastBottomSectionHolder.add(totalTextLabel);
        eastBottomSectionHolder.add(Box.createVerticalStrut(10));
        eastBottomSectionHolder.add(confirmButton);
        bottomSection.add(eastBottomSectionHolder, BorderLayout.EAST);

        add(bottomSection, BorderLayout.SOUTH);
    }

    private void changeQuantityOfOrderline(OrderLine orderLine, int qty, JLabel orderLineTotalLabel){
        basketViewController.changeOrderlineQuantity(orderLine, qty);
        String orderLineTotal = Symbols.getChar("£") + roundToDecimalPlaces(orderLine.getPrice(), 2);
        orderLineTotalLabel.setText(orderLineTotal);
        String orderTotal = "Total: " + Symbols.getChar("£") + roundToDecimalPlaces(basketViewController.getBasket().getTotalPrice(), 2);
        totalTextLabel.setText(orderTotal);
    }

    private void deleteOrderline(OrderLine orderLine){
        basketViewController.deleteOrderline(orderLine);
    }

    public void updateErrorMessage(String message) {
        if(message == null) {
            errorMessage.setText(" ");
            return;
        }
        errorMessage.setText("Error: " + message);
    }

    public void startCardUpdateProcess() {
        String notification = "Your banking card is expired. You need to update the card in order to confirm order.";
        try {
            if(!basketViewController.hasUserBankingCard()) {
                notification = "You need to provide your banking details in order to confirm order.";
            }
        } catch (SQLException e) {
            updateErrorMessage("Cannot connect to database.");
            return;
        }

        new NotificationModal(
                basketViewController.getNavigation().getFrame(), this,
                "Update card details", notification
        ) {
            @Override
            public void onSubmit() {
                new VerifyPasswordModal(basketViewController.getNavigation().getFrame(), basketViewController.getView()) {

                    @Override
                    public String onConfirm(char[] password) throws SQLException, BankDetailsEncryptionException {
                        if(UserDAO.verifyPassword(basketViewController.getUser().getEmail(), password) != null) {
                            BankingCard bankingCard = basketViewController.getBankingCard(password);
                            CompletableFuture.runAsync(() -> openChangeBankDetailsModal(bankingCard, password));
                            return null;
                        }
                        return "Incorrect password";
                    }
                }.setVisible(true);
            }
        }.setVisible(true);
    }

    private void openChangeBankDetailsModal(BankingCard bankingCard, char[] password) {
        new UpdateCreditCardModal(
                basketViewController.getNavigation().getFrame(), bankingCard, this) {
            @Override
            public void onSave(
                    String cardNumber, Date expiryDate, String securityCode, String holderName
            ) throws SQLException, BankDetailsEncryptionException {
                basketViewController.updateUserBankDetails(
                        new BankingCard(holderName, cardNumber, expiryDate, securityCode), password
                );
            }
        }.setVisible(true);
    }

    public void openConfirmationScreen(Order order) {
        new OrderModal(
                basketViewController.getNavigation().getFrame(), order,
                "Order details", "Your order has been confirmed. Below you can find summary."
        ).setVisible(true);
    }
}