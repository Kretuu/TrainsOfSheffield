package uk.ac.sheffield.com2008.view.modals;

import uk.ac.sheffield.com2008.cache.AppSessionCache;
import uk.ac.sheffield.com2008.controller.customer.BrowseItemsController;
import uk.ac.sheffield.com2008.model.domain.data.ProductSetItem;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.model.entities.products.ProductSet;
import uk.ac.sheffield.com2008.util.math.Rounding;
import uk.ac.sheffield.com2008.view.components.Button;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddProductToCartModal extends JDialog {

    private JLabel productName;
    private JSpinner quantitySpinner;
    private JLabel totalPriceLabel;
    private JLabel noStockLabel;
    private int selectedQuantity = 1;

    public AddProductToCartModal(BrowseItemsController browseItemsController, JFrame parentFrame, Product product) {
        super(parentFrame, "", true); // Set modal dialog with no title and bound to parent frame
        // Create a panel to hold the content
        JPanel panel = new JPanel(new BorderLayout());

        // Give the panel some padding
        Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        panel.setBorder(emptyBorder);

        // Create a panel for the top section
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcTop = new GridBagConstraints();

        JPanel productDetails = new JPanel();
        productDetails.setLayout(new BoxLayout(productDetails, BoxLayout.Y_AXIS));

        // Create "Add To Cart" label at the top
        JLabel titleLabel = new JLabel("Add To Cart:");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16));
        productDetails.add(titleLabel);

        // Create a label that can be customized
        productName = new JLabel("<html><div style='width: 100%;'>" + product.printName() + "</div></html>");
        productDetails.add(productName);

        //create a list of the set items if this product is a set
        if(product instanceof ProductSet productSet){
            StringBuilder htmlList = new StringBuilder("<html><ul>");
            for(ProductSetItem psi : ((ProductSet) product).getSetItems()){
                htmlList.append("<li style='width: 100%;'> ")
                        .append(psi.getProduct().printName())
                        .append("</li>");
            }

            htmlList.append("</ul></html>");
            JLabel productSetItemsLabel = new JLabel(htmlList.toString());
            productDetails.add(productSetItemsLabel);
        }

        gbcTop.gridx = 0;
        gbcTop.gridy = 0;
        gbcTop.weightx = 1.0;
        gbcTop.anchor = GridBagConstraints.WEST;
        gbcTop.fill = GridBagConstraints.HORIZONTAL;
        topPanel.add(productDetails, gbcTop);

        //extra details
        JPanel extraDetails = new JPanel(new GridBagLayout());
        GridBagConstraints gbcTopExtra = new GridBagConstraints();
        gbcTopExtra.insets = new Insets(5, 15, 5, 15);

        //gauge label
        gbcTopExtra.gridx = 0;
        gbcTopExtra.gridy = 0;
        JLabel gaugeLabel = new JLabel("OO");
        gaugeLabel.setFont(gaugeLabel.getFont().deriveFont(Font.BOLD, 12));
        gbcTopExtra.anchor = GridBagConstraints.CENTER;
        extraDetails.add(gaugeLabel, gbcTopExtra);

        //gauge subtitle
        gbcTopExtra.gridx = 0;
        gbcTopExtra.gridy = 1;
        JLabel gaugeSub = new JLabel("Gauge");
        gaugeSub.setFont(gaugeSub.getFont().deriveFont(Font.BOLD, 10));
        gaugeSub.setForeground(new Color(117, 117, 117));
        gbcTopExtra.anchor = GridBagConstraints.CENTER;
        extraDetails.add(gaugeSub, gbcTopExtra);

        //brand label
        gbcTopExtra.gridx = 1;
        gbcTopExtra.gridy = 0;
        JLabel brandLabel = new JLabel("Hornby");
        brandLabel.setFont(brandLabel.getFont().deriveFont(Font.BOLD, 12));
        gbcTopExtra.anchor = GridBagConstraints.CENTER;
        extraDetails.add(brandLabel, gbcTopExtra);

        //brand label
        gbcTopExtra.gridx = 1;
        gbcTopExtra.gridy = 1;
        JLabel brandSub = new JLabel("Brand");
        brandSub.setFont(brandSub.getFont().deriveFont(Font.BOLD, 10));
        brandSub.setForeground(new Color(117, 117, 117));
        gbcTopExtra.anchor = GridBagConstraints.CENTER;
        extraDetails.add(brandSub, gbcTopExtra);

        gbcTopExtra.gridx = 0;
        gbcTopExtra.gridy = 1;
        gbcTopExtra.anchor = GridBagConstraints.NORTH;
        topPanel.add(extraDetails, gbcTopExtra);

        panel.add(topPanel, BorderLayout.NORTH);

        // Create a panel for quantity and add to cart button
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Create "Quantity:" label
        JLabel quantityLabel = new JLabel("Quantity:");
        buttonPanel.add(quantityLabel);

        // Create spinner for quantity selection
        int quantityInBasket = 0;
        Order userBasket = AppSessionCache.getInstance().getUserLoggedIn().getBasket();
        userBasket.PrintFullOrder();
        if(userBasket.hasProduct(product)){
            System.out.println("User has this item in basket");
            quantityInBasket = userBasket.getProductQuantity(product);
        }
        System.out.println(quantityInBasket);
        int stockAvailableToAdd = product.getStock() - quantityInBasket;

        SpinnerModel spinnerModel = new SpinnerNumberModel(
                Math.min(1, stockAvailableToAdd),
                Math.min(1, stockAvailableToAdd),
                stockAvailableToAdd,
                1);
        quantitySpinner = new JSpinner(spinnerModel);
        quantitySpinner.addChangeListener(e -> {
            // Method called when the spinner value changes
            selectedQuantity = (int) quantitySpinner.getValue();
            float newPrice = product.getPrice() * selectedQuantity;
            updateTotalPrice(newPrice);
        });
        buttonPanel.add(quantitySpinner);

        // Create "Add To Cart" button
        JButton addToCartButton = new Button("Add To Cart");
        addToCartButton.addActionListener(e -> {
            browseItemsController.addProductToBasket(product, selectedQuantity);
            // Close the modal dialog
            dispose();
        });
        buttonPanel.add(addToCartButton);

        JPanel summaryInfoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        totalPriceLabel = new JLabel("Total Price: " + product.getPrice());
        JLabel stockInBasketLabel = new JLabel("already in basket: " + quantityInBasket);
        stockInBasketLabel.setForeground(new Color(117, 117, 117));

        if(quantityInBasket > 0){
            summaryInfoPanel.add(stockInBasketLabel, gbc);
        }
        if(product.getStock() - quantityInBasket > 0){
            gbc.gridy++;
            summaryInfoPanel.add(totalPriceLabel, gbc);
        }

        summaryInfoPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        bottomPanel.add(summaryInfoPanel);
        bottomPanel.add(buttonPanel);

        if(stockAvailableToAdd == 0){
            noStockLabel = new JLabel("No Stock Left");
            noStockLabel.setForeground(Color.RED);
            noStockLabel.setFont(noStockLabel.getFont().deriveFont(Font.BOLD));
            bottomPanel.add(noStockLabel);
            JPanel noStockPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            noStockPanel.add(noStockLabel);
            addToCartButton.setEnabled(false);
            bottomPanel.add(noStockPanel);
        }

        panel.add(bottomPanel, BorderLayout.SOUTH);

        // Set panel to the content pane of the dialog
        setContentPane(panel);
        setMinimumSize(new Dimension(300, 200));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 800));
        pack();
        setResizable(false);
        setLocationRelativeTo(parentFrame);
    }

    private void updateTotalPrice(float newPrice){
        totalPriceLabel.setText("Total Price: " + Rounding.roundToDecimalPlaces(newPrice, 2));
    }
}
