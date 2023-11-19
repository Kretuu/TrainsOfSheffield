package uk.ac.sheffield.com2008.view.customer;

import uk.ac.sheffield.com2008.controller.customer.BasketViewController;
import uk.ac.sheffield.com2008.controller.customer.BrowseItemsController;
import uk.ac.sheffield.com2008.model.domain.data.OrderLine;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.model.entities.Product;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;

import static uk.ac.sheffield.com2008.util.math.Rounding.roundToDecimalPlaces;

public class BasketView extends CustomerView{
    BasketViewController basketViewController;
    public BasketView(BasketViewController basketViewController){
        super();
        this.basketViewController = basketViewController;
        InitializeUI();
    }

    public void onRefresh(){
        removeAll();
        InitializeUI();
    }

    public void InitializeUI(){
        setLayout(new BorderLayout());

        ArrayList<OrderLine> orderLines = new ArrayList<>();
        Order userBasket = basketViewController.getBasket();
        if(userBasket != null){
            orderLines  = (ArrayList<OrderLine>) userBasket.getOrderLines();
        }

        orderLines.forEach(orderLine -> System.out.println(orderLine.getProduct().getName()));

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

        //create orderlines
        for(OrderLine orderLine : orderLines){

            Product lineProduct = orderLine.getProduct();
            JLabel orderLineText = new JLabel(lineProduct.getName());
            JLabel orderLineUnitPrice = new JLabel(lineProduct.getPrice() + " x ");
            orderLineUnitPrice.setForeground(new Color(117, 117, 117));

            JLabel orderLineTotalPrice = new JLabel(String.valueOf(roundToDecimalPlaces(
                    lineProduct.getPrice() * orderLine.getQuantity(), 2)));
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
                //should also change the orderLineTotalPrice

                // TODO: OrderLine setQuantity, recalculate price
                // TODO: Order reculculate price, save both

                String orderLineTotal = String.valueOf(roundToDecimalPlaces(
                        (int) spinnerModel.getValue() * lineProduct.getPrice(), 2));
                orderLineTotalPrice.setText(orderLineTotal);
                //which in turns changes to overall total cost of the order

                //changing the quantity here should change the database quantity as well as
                //quantity in the order object.
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
        JScrollPane scrollPane = new JScrollPane(basketPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(scrollPane, BorderLayout.NORTH);
    }
}
