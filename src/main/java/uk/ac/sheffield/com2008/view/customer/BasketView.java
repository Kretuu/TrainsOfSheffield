package uk.ac.sheffield.com2008.view.customer;

import uk.ac.sheffield.com2008.config.Symbols;
import uk.ac.sheffield.com2008.controller.customer.BasketViewController;
import uk.ac.sheffield.com2008.model.domain.data.OrderLine;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.view.user.UserView;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

import static uk.ac.sheffield.com2008.util.math.Rounding.roundToDecimalPlaces;

public class BasketView extends UserView {
    BasketViewController basketViewController;
    JLabel totalTextLabel;
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
            JButton deleteButton = new JButton("X");
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
        bottomSection.setLayout(new BoxLayout(bottomSection, BoxLayout.Y_AXIS));
        totalTextLabel = new JLabel("Total: ");
        totalTextLabel.setFont(totalTextLabel.getFont().deriveFont(Font.BOLD, 24));

        JButton confirmButton = new JButton("Confirm & Pay");
        confirmButton.setFont(confirmButton.getFont().deriveFont(Font.BOLD, 24));
        confirmButton.addActionListener(e -> {
            confirmOrderButton();
        });

        // Set alignments to right
        totalTextLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        confirmButton.setAlignmentX(Component.RIGHT_ALIGNMENT);

        bottomSection.add(totalTextLabel);
        bottomSection.add(Box.createVerticalStrut(10));
        bottomSection.add(confirmButton);
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

    private void confirmOrderButton(){
        basketViewController.confirmOrder();
    }
}