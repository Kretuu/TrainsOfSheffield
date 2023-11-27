package uk.ac.sheffield.com2008.view.staff;

import uk.ac.sheffield.com2008.controller.staff.FormController;
import uk.ac.sheffield.com2008.model.dao.ProductDAO;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.util.FieldsValidationManager;
import uk.ac.sheffield.com2008.view.components.CustomInputField;
import uk.ac.sheffield.com2008.view.modals.ProductSetModal;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;


public class ProductRecordForm extends StaffView {

    private final FormController formController;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private final JButton submitButton;
    private JPanel getInSetPanel;
    private JScrollPane scrollPane;
    private Map<Product, Integer> selectedProductsMap = new HashMap<>();
    List<Product> filteredProducts;
    JComboBox<String> gaugesComboBox;
    JLabel gaugeLabel;
    private final Map<String, CustomInputField> sharedInputFields = new HashMap<>();

    private final String[] categories = {"Locomotive", "Rolling Stock", "Track", "Controller",
            "Train Set", "Track Pack"};
    private final Map<String, Character> catChar = Map.of(
            "Locomotive", 'L',
            "Rolling Stock", 'S',
            "Track", 'R',
            "Controller", 'C',
            "Train Set", 'M',
            "Track Pack", 'P'
    );

    public ProductRecordForm(FormController formController) {
        this.submitButton = new JButton("Save");
        this.submitButton.addActionListener(e -> {
            formController.tryCreateProduct();
        });
        this.formController = formController;

        Map<String, Product.Gauge> gauges = new LinkedHashMap<>();
        gauges.put("OO Gauge (1/76th scale)", Product.Gauge.OO);
        gauges.put("TT Gauge (1/120th scale)", Product.Gauge.TT);
        gauges.put("N gauge (1/148th scale)", Product.Gauge.N);
        gaugesComboBox = new JComboBox<>(gauges.keySet().toArray(new String[0]));
        gaugeLabel = new JLabel("Gauge: ");

        initializeUI();
    }

    private void initializeUI() {
        submitButton.setEnabled(false);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Create a panel with CardLayout to hold different category panels
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        add(new JLabel("Category: "), gbc);

        JComboBox<String> categoryComboBox = new JComboBox<>(categories);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        add(categoryComboBox, gbc);
        categoryComboBox.addActionListener(e -> {
            String selectedCategory = (String) categoryComboBox.getSelectedItem();

            // Remove the panel associated with the previously selected category
            String previousCategory = getCurrentlyDisplayedCategory();
            if (previousCategory != null) {
                cardPanel.remove(getCategoryPanel(previousCategory));
            }

            // Add the panel for the selected category
            cardPanel.add(getCategoryPanel(selectedCategory), selectedCategory);

            // Set the identifier based on the selected category
            cardLayout.show(cardPanel, selectedCategory);
            updateButtonState();
            revalidate();  // Ensure the layout manager updates the container

            if (selectedCategory.equals("Controller")) {
                gaugesComboBox.setVisible(false);
                gaugeLabel.setVisible(false);
            } else {
                gaugesComboBox.setVisible(true);
                gaugeLabel.setVisible(true);
            }
            repaint();
        });

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(new JLabel("Product Code: "), gbc);

        CustomInputField productCodeField = new CustomInputField("",
                this::updateButtonState,
                false
        );
        productCodeField.setValidationFunction(
                () -> FieldsValidationManager.validateProductCode(
                        productCodeField.getjTextField().getText(),
                        catChar.get((String) categoryComboBox.getSelectedItem())));
        sharedInputFields.put("productcode", productCodeField);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        productCodeField.addToPanel(this, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        add(gaugeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(gaugesComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        add(new JLabel("Price: "), gbc);

        CustomInputField priceField = new CustomInputField("",
                this::updateButtonState,
                false
        );
        priceField.setValidationFunction(
                () -> FieldsValidationManager.validatePrice(priceField.getjTextField().getText()));
        sharedInputFields.put("price", priceField);
        gbc.gridx = 1;
        gbc.gridy = 5; // Move to the next row
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        priceField.addToPanel(this, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        add(new JLabel("Quantity: "), gbc);

        NumberFormat integerFormat = NumberFormat.getIntegerInstance();
        NumberFormatter formatter = new NumberFormatter(integerFormat);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        JFormattedTextField quantityField = new JFormattedTextField(formatter);
        quantityField.setValue(1);
        quantityField.setColumns(10);

        gbc.gridx = 1;
        gbc.gridy = 6; // Move to the next row
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(quantityField, gbc);

        // Add cardPanel to the layout
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;

        cardPanel.add(getCategoryPanel("Locomotive"), "Locomotive");
        // Set the identifier based on the selected category
        cardLayout.show(cardPanel, "Locomotive");

        add(cardPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        add(buttonPanel(), gbc);

    }


    private JPanel getCategoryPanel(String category) {
        if ("Locomotive".equals(category)) {
            return locomotivePanel();
        } else if ("Rolling Stock".equals(category)) {
            return rollingStocksPanel();
        } else if ("Track".equals(category)) {
            return trackPanel();
        } else if ("Controller".equals(category)) {
            return controllersPanel();
        } else if ("Train Set".equals(category)) {
            return trainSetsPanel();
        } else if ("Track Pack".equals(category)) {
            return trackPackPanel();
        } else {
            buttonPanel();
            return new JPanel();
        }
    }


    private String getCurrentlyDisplayedCategory() {
        for (Component component : cardPanel.getComponents()) {
            if (cardPanel.isVisible()) {
                return cardPanel.getComponentZOrder(component) == 0 ? null : component.getName();
            }
        }
        return null;
    }

    private JPanel buttonPanel() {
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton cancelButton = new JButton("Cancel");
        buttonsPanel.add(cancelButton);
        cancelButton.addActionListener(e -> formController.getNavigation().navigate(Navigation.PRODUCT_RECORD));
        buttonsPanel.add(submitButton);

        return buttonsPanel;
    }

    private JPanel locomotivePanel() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        panel.add(new JLabel("BR Class:"));
        JTextField classTextField = new JTextField();
        classTextField.setPreferredSize(new Dimension(25, classTextField.getPreferredSize().height));
        panel.add(classTextField);

        panel.add(new JLabel("Individual Name:"));
        JTextField individualNameTextField = new JTextField();
        individualNameTextField.setPreferredSize(new Dimension(100, individualNameTextField.getPreferredSize().height));
        panel.add(individualNameTextField);

        panel.add(new JLabel("ERA:"));
        JTextField eraTextField = new JTextField();
        eraTextField.setPreferredSize(new Dimension(25, eraTextField.getPreferredSize().height));
        panel.add(eraTextField);

        panel.add(new JLabel("Model Type:"));
        String[] powerTypes = {"Analogue", "DCC-Ready", "DCC-Fitted", "DCC-Sound"};
        JComboBox<String> powerTypeComboBox = new JComboBox<>(powerTypes);
        panel.add(powerTypeComboBox);

        return panel;
    }

    private JPanel rollingStocksPanel() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        panel.add(new JLabel("Mark:"));
        JTextField markTextField = new JTextField();
        markTextField.setPreferredSize(new Dimension(25, markTextField.getPreferredSize().height));
        panel.add(markTextField);

        panel.add(new JLabel("Kind:"));
        JTextField kindTextField = new JTextField();
        kindTextField.setPreferredSize(new Dimension(100, kindTextField.getPreferredSize().height));
        panel.add(kindTextField);

        panel.add(new JLabel("Class:"));
        String[] classes = {"FIRST", "SECOND", "THIRD", "STANDARD"};
        JComboBox<String> classesComboBox = new JComboBox<>(classes);
        panel.add(classesComboBox);


        panel.add(new JLabel("ERA:"));
        JTextField eraTextField = new JTextField();
        eraTextField.setPreferredSize(new Dimension(25, eraTextField.getPreferredSize().height));
        panel.add(eraTextField);

        return panel;
    }

    private JPanel trackPanel() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        panel.add(new JLabel("Descriptor:"));
        JTextField descriptorTextField = new JTextField();
        descriptorTextField.setPreferredSize(new Dimension(100, descriptorTextField.getPreferredSize().height));
        panel.add(descriptorTextField);

        panel.add(new JLabel("Track Type:"));
        String[] trackTypes = {"Straight", "Curve"};
        JComboBox<String> trackTypeComboBox = new JComboBox<>(trackTypes);
        panel.add(trackTypeComboBox);

        return panel;
    }

    private JPanel controllersPanel() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        panel.add(new JLabel("Descriptor:"));
        JTextField descriptorTextField = new JTextField();
        descriptorTextField.setPreferredSize(new Dimension(100, descriptorTextField.getPreferredSize().height));
        panel.add(descriptorTextField);

        panel.add(new JLabel("Power Type:"));
        String[] trackTypes = {"Analogue", "Digital (DCC)"};
        JComboBox<String> trackTypeComboBox = new JComboBox<>(trackTypes);
        panel.add(trackTypeComboBox);

        return panel;
    }

    private JPanel trackPackPanel() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JCheckBox starterOval = new JCheckBox("Starter Oval Track Pack");
        JCheckBox extension = new JCheckBox("Extension Track Pack");

        JLabel trackLabel = new JLabel("Track");
        JSpinner extensionSpinner = createSpinner();
        extensionSpinner.setEnabled(false);
        extension.addItemListener(e -> {
            if (extension.isSelected()) {

                panel.add(trackLabel);
                panel.add(extensionSpinner);
                extensionSpinner.setEnabled(true);

            } else {
                panel.remove(extensionSpinner);
                extensionSpinner.setEnabled(false);
                panel.remove(trackLabel);
            }

            panel.revalidate();
            panel.repaint();
        });

        panel.add(starterOval);
        panel.add(extension);


        return panel;

    }

    private JPanel trainSetsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // HEADER panel
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel title = new JLabel("Add products to set: ");
        row1.add(title);
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] itemTypes = {"Locomotive", "Rolling Stock", "Track", "Controller", "Train Set", "Track Pack"};
        JComboBox<String> itemTypesComboBox = new JComboBox<>(itemTypes);
        row2.add(itemTypesComboBox);
        JButton findButton = new JButton("Find");
        findButton.addActionListener(e -> {
            String selectedCategory = (String) itemTypesComboBox.getSelectedItem();
            String initialLetter = getInitialLetter(selectedCategory);
            filteredProducts = ProductDAO.getProductsByCategory(initialLetter);
            ProductSetModal modal = new ProductSetModal(formController, (JFrame)
                    SwingUtilities.getWindowAncestor(findButton), ProductRecordForm.this, filteredProducts);
            modal.setVisible(true);
        });
        row2.add(findButton);
        headerPanel.add(row1);
        headerPanel.add(row2);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Selected panel
        JPanel selectedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel selected = new JLabel("Item Selected: ");
        JLabel itemSelected = new JLabel();
        JButton addButton = new JButton("Add");

        //Items in set panel
        JPanel inSetPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(inSetPanel, BoxLayout.Y_AXIS);
        inSetPanel.setLayout(boxLayout);
        inSetPanel.setPreferredSize(new Dimension(200, 200));
        scrollPane = new JScrollPane(inSetPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        inSetPanel.add(scrollPane, BorderLayout.SOUTH);


        JPanel inSetHeadingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel displayItemsLabel = new JLabel("Items in set:  ");
        inSetHeadingPanel.add(displayItemsLabel);

        inSetPanel.add(inSetHeadingPanel);


        addButton.addActionListener(e -> {
            // Add a new subItemsPanel for each selected product in the map
            inSetPanel.removeAll();
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridy = 0;
            for (Map.Entry<Product, Integer> entry : selectedProductsMap.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();

                JPanel subItemsPanel = new JPanel(new GridBagLayout());

                gbc.gridx = 0;
                gbc.anchor = GridBagConstraints.NORTHWEST;
                gbc.insets = new Insets(5, 5, 5, 5);

                Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
                subItemsPanel.setBorder((emptyBorder));

                JLabel itemCodeLabel = new JLabel(product.getProductCode());
                JLabel itemNameLabel = new JLabel(product.getName());
                JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(quantity, 0, Integer.MAX_VALUE, 1));
                Dimension spinnerPreferredSize = quantitySpinner.getPreferredSize();
                spinnerPreferredSize.width = 40; // Adjust the width as needed
                quantitySpinner.setPreferredSize(spinnerPreferredSize);
                JButton removeItemButton = new JButton("X");

                gbc.gridx++;
                gbc.weightx = 1.0;
                gbc.anchor = GridBagConstraints.WEST;
                subItemsPanel.add(itemCodeLabel, gbc);

                gbc.gridx++;
                gbc.anchor = GridBagConstraints.WEST;
                subItemsPanel.add(itemNameLabel, gbc);

                gbc.gridx++;
                gbc.anchor = GridBagConstraints.EAST;
                gbc.weightx = 0.0;
                subItemsPanel.add(quantitySpinner, gbc);

                gbc.gridx = GridBagConstraints.RELATIVE; // Move to the next cell
                gbc.anchor = GridBagConstraints.CENTER;
                subItemsPanel.add(removeItemButton, gbc);

                inSetPanel.add(subItemsPanel);

                // Reset GridBagConstraints for the next iteration
                gbc.gridx = 0;
                gbc.gridy++;
                gbc.weightx = 0.0;
            }
            // Revalidate and repaint the panel to reflect the changes
            inSetPanel.revalidate();
            inSetPanel.repaint();
        });

        selectedPanel.add(selected);
        selectedPanel.add(itemSelected);
        selectedPanel.add(addButton);
        panel.add(selectedPanel, BorderLayout.CENTER);



        return panel;
    }

    public void updateItemSelectedLabel(String selectedProductName, String selectedProductCode) {
        // Find the selected product
        Product selectedProduct = findProductByCode(filteredProducts, selectedProductCode);

        if (selectedProduct != null) {
            // Add the selected product to the map with an initial quantity of 1
            selectedProductsMap.put(selectedProduct, selectedProductsMap.getOrDefault(selectedProduct, 0) + 1);

            // Update the itemSelected label with the map of selected products
            updateItemSelectedLabel();
        }
    }

    private Product findProductByCode(List<Product> products, String productCode) {
        for (Product product : products) {
            if (product.getProductCode().equals(productCode)) {
                return product;
            }
        }
        return null;
    }

    private void updateItemSelectedLabel() {
        StringBuilder labelText = new StringBuilder("Items Selected: ");
        for (Map.Entry<Product, Integer> entry : selectedProductsMap.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            labelText.append(product.getName()).append(" (").append(product.getProductCode()).append(") x ").append(quantity).append(", ");
        }

    }

    private JSpinner createSpinner() {
        SpinnerModel model = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
        return new JSpinner(model);
    }

    /**
     * revalidates shared input fields, as well as those of
     * the category selected
     *
     * @return
     */
    public boolean validateAllFields() {
        for (CustomInputField field : sharedInputFields.values()) {
            field.validate(field.getjTextField().getText());
        }
        return sharedInputFields.values().stream().allMatch(CustomInputField::isValid);
    }

    private void updateButtonState() {
        submitButton.setEnabled(sharedInputFields.values().stream().allMatch(CustomInputField::isValid));
    }

    private String getInitialLetter(String selectedCategory) {
        if ("Locomotive".equals(selectedCategory)) {
            return "L";
        } else if ("Controller".equals(selectedCategory)) {
            return "C";
        } else if ("Track".equals(selectedCategory)) {
            return "R";
        } else if ("Rolling Stock".equals(selectedCategory)) {
            return "S";
        } else if ("Train Set".equals(selectedCategory)) {
            return "M";
        } else if ("Track Pack".equals(selectedCategory)) {
            return "P";
        } else {
            return "";
        }
    }


}
