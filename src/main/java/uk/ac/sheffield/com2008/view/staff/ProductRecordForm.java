package uk.ac.sheffield.com2008.view.staff;

import uk.ac.sheffield.com2008.config.Colors;
import uk.ac.sheffield.com2008.controller.staff.FormController;
import uk.ac.sheffield.com2008.model.dao.ProductDAO;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.model.entities.products.Controller;
import uk.ac.sheffield.com2008.model.entities.products.Locomotive;
import uk.ac.sheffield.com2008.model.entities.products.RollingStock;
import uk.ac.sheffield.com2008.model.entities.products.Track;
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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class ProductRecordForm extends StaffView {

    private final FormController formController;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private final JButton submitButton;


    private Map<Product, Integer> selectedProductsMap = new HashMap<>();


    List<Product> filteredProducts;
    JComboBox<String> gaugesComboBox;

    JLabel gaugeLabel;

    JLabel errorMessage;

    //Panels
    JPanel locomotivePanel;
    JPanel rollingStockPanel;
    JPanel trackPanel;
    JPanel controllerPanel;
    JPanel trainSetPanel;
    JPanel trackPackPanel;
    JPanel currentPanel;

    JComboBox<String> categoryComboBox;

    private final Map<String, CustomInputField> sharedInputFields = new HashMap<>();

    JFormattedTextField quantityField;
    Map<String, Product.Gauge> gauges = new LinkedHashMap<>();

    //Locomotive
    private final Map<String, CustomInputField> locomotiveInputFields = new HashMap<>();
    private JComboBox<String> powerTypeComboBox;

    //Rolling stock
    private final Map<String, CustomInputField> rollingStockInputFields = new HashMap<>();
    private JComboBox<String> classesComboBox;

    //Track
    private final Map<String, CustomInputField> trackInputFields = new HashMap<>();
    private JComboBox<String> trackTypeComboBox;

    //Controller
    private final Map<String, CustomInputField> controllerInputFields = new HashMap<>();
    private JComboBox<String> controllerTypeComboBox;


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

    private final Map<JPanel, Map<String, CustomInputField>> categorySpecificFields = new HashMap<>();

    public ProductRecordForm(FormController formController) {
        this.formController = formController;
        this.submitButton = new JButton("Save");

        categoryComboBox = new JComboBox<>(categories);
        this.submitButton.addActionListener(e -> {
            formController.tryCreateProduct(sharedInputFields.get("productCode").getjTextField().getText());
        });

        gauges.put("OO Gauge (1/76th scale)", Product.Gauge.OO);
        gauges.put("TT Gauge (1/120th scale)", Product.Gauge.TT);
        gauges.put("N gauge (1/148th scale)", Product.Gauge.N);
        gaugesComboBox = new JComboBox<>(gauges.keySet().toArray(new String[0]));
        gaugeLabel = new JLabel("Gauge: ");

        locomotivePanel = locomotivePanel();
        rollingStockPanel = rollingStocksPanel();
        trackPanel = trackPanel();
        controllerPanel = controllersPanel();
        trainSetPanel = trainSetsPanel();
        trackPackPanel = trackPackPanel();
        currentPanel = locomotivePanel;

        categorySpecificFields.put(locomotivePanel, locomotiveInputFields);
        categorySpecificFields.put(rollingStockPanel, rollingStockInputFields);
        categorySpecificFields.put(trackPanel, trackInputFields);
        categorySpecificFields.put(controllerPanel, controllerInputFields);

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

        gbc.insets = new Insets(0, 0, 15, 5);
        errorMessage = new JLabel("");
        errorMessage.setForeground(Colors.TEXT_FIELD_ERROR);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 3;
        add(errorMessage, gbc);
        gbc.insets = new Insets(0, 0, 0, 0);

        gbc.gridy++;
        gbc.gridwidth = 1;
        add(new JLabel("Category: "), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        add(categoryComboBox, gbc);
        categoryComboBox.addActionListener(e -> {
            String selectedCategory = (String) categoryComboBox.getSelectedItem();

            // Remove the panel associated with the previously selected category
            JPanel previousCategory = currentPanel;
            if (previousCategory != null) {
                cardPanel.remove(currentPanel);
            }

            // Add the panel for the selected category
            JPanel newCategory = getCategoryPanel(selectedCategory);
            cardPanel.add(newCategory);
            currentPanel = newCategory;

            // Set the identifier based on the selected category
            cardLayout.show(cardPanel, selectedCategory);
            validateSharedFields();
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
        gbc.gridy++;
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
        sharedInputFields.put("productCode", productCodeField);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        productCodeField.addToPanel(this, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        add(new JLabel("Brand: "), gbc);

        CustomInputField brandField = new CustomInputField("",
                this::updateButtonState,
                false
        );
        brandField.setValidationFunction(
                () -> FieldsValidationManager.validateForLength(
                        brandField.getjTextField().getText(),
                        3));
        sharedInputFields.put("brand", brandField);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        brandField.addToPanel(this, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        add(gaugeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(gaugesComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
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
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        priceField.addToPanel(this, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        add(new JLabel("Quantity: "), gbc);

        NumberFormat integerFormat = NumberFormat.getIntegerInstance();
        NumberFormatter formatter = new NumberFormatter(integerFormat);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        quantityField = new JFormattedTextField(formatter);
        quantityField.setValue(1);
        quantityField.setColumns(10);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(quantityField, gbc);

        // Add cardPanel to the layout
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;

        cardPanel.add(locomotivePanel);
        // Set the identifier based on the selected category
        cardLayout.show(cardPanel, "Locomotive");

        add(cardPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        add(buttonPanel(), gbc);

    }


    private JPanel getCategoryPanel(String category) {
        if ("Locomotive".equals(category)) {
            return locomotivePanel;
        } else if ("Rolling Stock".equals(category)) {
            return rollingStockPanel;
        } else if ("Track".equals(category)) {
            return trackPanel;
        } else if ("Controller".equals(category)) {
            return controllerPanel;
        } else if ("Train Set".equals(category)) {
            return trainSetPanel;
        } else if ("Track Pack".equals(category)){
            return trackPackPanel;
        }else{
            throw new RuntimeException("cant get category panel that doesn't exist: " + category);
        }
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

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy= 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 0, 10);

        // BR Class
        CustomInputField brClassField = new CustomInputField("BR Class:", this::updateButtonState, false);
        brClassField.setValidationFunction(() -> FieldsValidationManager.validateForLength(
                brClassField.getjTextField().getText(),
                2));
        locomotiveInputFields.put("brClass", brClassField);
        brClassField.addToPanel(panel, gbc);

        // Individual Name
        gbc.gridx++;
        CustomInputField indivNameField = new CustomInputField("Individual Name:", this::updateButtonState, false);
        indivNameField.setValidationFunction(() -> FieldsValidationManager.validateForLength(
                indivNameField.getjTextField().getText(),
                3));
        locomotiveInputFields.put("individualName", indivNameField);
        gbc.anchor = GridBagConstraints.WEST;
        indivNameField.addToPanel(panel, gbc);

        // ERA
        gbc.gridx++;
        CustomInputField eraField = new CustomInputField("Era:", this::updateButtonState, false);
        eraField.setValidationFunction(() -> FieldsValidationManager.validateEra(
                eraField.getjTextField().getText()));
        locomotiveInputFields.put("era", eraField);
        gbc.anchor = GridBagConstraints.WEST;
        eraField.addToPanel(panel, gbc);

        // Model Type
        gbc.gridx++;
        gbc.anchor = GridBagConstraints.NORTH;
        panel.add(new JLabel("Model Type:"), gbc);
        String[] powerTypes = Arrays.stream(Locomotive.DCCType.values())
                .map(Locomotive.DCCType::deriveName)
                .toArray(String[]::new);
        powerTypeComboBox = new JComboBox<>(powerTypes);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(powerTypeComboBox, gbc);

        return panel;
    }

    private JPanel rollingStocksPanel() {

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy= 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 0, 10);

        // Mark
        CustomInputField markField = new CustomInputField("Mark:", this::updateButtonState, false);
        markField.setValidationFunction(() -> FieldsValidationManager.validateForLength(
                markField.getjTextField().getText(),
                2));
        rollingStockInputFields.put("mark", markField);
        markField.addToPanel(panel, gbc);

        // Kind
        gbc.gridx++;
        CustomInputField kindField = new CustomInputField("Kind:", this::updateButtonState, false);
        kindField.setValidationFunction(() -> FieldsValidationManager.validateForLength(
                kindField.getjTextField().getText(),
                3));
        rollingStockInputFields.put("kind", kindField);
        gbc.anchor = GridBagConstraints.WEST;
        kindField.addToPanel(panel, gbc);

        // Class
        gbc.gridx += 2;
        gbc.anchor = GridBagConstraints.NORTH;
        panel.add(new JLabel("Class:"), gbc);
        List<String> classes = Arrays.stream(RollingStock.Class_.values())
                .map(RollingStock.Class_::deriveName)
                .collect(Collectors.toList());
        classes.add("Null");
        classesComboBox = new JComboBox<>(classes.toArray(new String[0]));
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(classesComboBox, gbc);

        // Era
        gbc.gridx++;
        CustomInputField eraField = new CustomInputField("Era:", this::updateButtonState, false);
        eraField.setValidationFunction(() -> FieldsValidationManager.validateEra(
                eraField.getjTextField().getText()));
        rollingStockInputFields.put("era", eraField);
        gbc.anchor = GridBagConstraints.WEST;
        eraField.addToPanel(panel, gbc);

        return panel;
    }

    private JPanel trackPanel() {

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy= 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 0, 10);

        // Descriptor
        CustomInputField descriptorField = new CustomInputField("Descriptor: ", this::updateButtonState, false);
        descriptorField.setValidationFunction(() -> FieldsValidationManager.validateForLength(
                descriptorField.getjTextField().getText(), 3));
        trackInputFields.put("descriptor", descriptorField);
        gbc.anchor = GridBagConstraints.WEST;
        descriptorField.addToPanel(panel, gbc);

        // Track Type
        gbc.gridx++;
        gbc.anchor = GridBagConstraints.NORTH;
        panel.add(new JLabel("Track Type:"), gbc);
        String[] trackTypes = Arrays.stream(Track.TrackType.values())
                .map(Track.TrackType::deriveName)
                .toArray(String[]::new);
        trackTypeComboBox = new JComboBox<>(trackTypes);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(trackTypeComboBox,gbc);

        return panel;
    }

    private JPanel controllersPanel() {

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy= 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 0, 10);

        // Descriptor
        CustomInputField descriptorField = new CustomInputField("Descriptor: ", this::updateButtonState, false);
        descriptorField.setValidationFunction(() -> FieldsValidationManager.validateForLength(
                descriptorField.getjTextField().getText(), 3));
        controllerInputFields.put("descriptor", descriptorField);
        gbc.anchor = GridBagConstraints.WEST;
        descriptorField.addToPanel(panel, gbc);

        // Power Type
        gbc.gridx++;
        gbc.anchor = GridBagConstraints.NORTH;
        panel.add(new JLabel("Power Type:"),gbc);
        String[] powerTypes = Arrays.stream(Controller.PowerType.values())
                .map(Enum::name)
                .toArray(String[]::new);
        controllerTypeComboBox = new JComboBox<>(powerTypes);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(controllerTypeComboBox, gbc);

        return panel;
    }

    private JPanel trackPackPanel() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel radioButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JRadioButton starterOval = new JRadioButton("Starter Oval Track Pack");
        JRadioButton extension = new JRadioButton("Extension Track Pack");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(starterOval);
        buttonGroup.add(extension);
        radioButtonsPanel.add(starterOval);
        radioButtonsPanel.add(extension);
        panel.add(radioButtonsPanel);

        //Items in set panel
        JPanel inPackPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(inPackPanel, BoxLayout.Y_AXIS);
        inPackPanel.setLayout(boxLayout);
        inPackPanel.setPreferredSize(new Dimension(500, 200));


        JScrollPane scrollPane = new JScrollPane(inPackPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scrollPane);

        //when any radio button is clicked it should open a modal and add it inside the panel
        starterOval.addActionListener(e -> {

        });

        extension.addActionListener(e -> {

        });

        /*JLabel trackLabel = new JLabel("Track");
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
        });*/


        return panel;

    }

    private JPanel trainSetsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel title = new JLabel("Add products to set: ");
        row1.add(title);
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] itemTypes = {"Locomotive", "Rolling Stock", "Track", "Controller", "Starter Oval Track Pack", "Extension Track Pack"};
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
        JLabel itemSelected = new JLabel(); //it doesnt pass here
        JButton addButton = new JButton("Add");

        //Items in set panel
        JPanel inSetPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(inSetPanel, BoxLayout.Y_AXIS);
        inSetPanel.setLayout(boxLayout);
        inSetPanel.setPreferredSize(new Dimension(500, 300));


        JScrollPane scrollPane = new JScrollPane(inSetPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scrollPane, BorderLayout.SOUTH);

        //Add heading panel to inset panel
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

                JPanel subItemsPanel = new JPanel();
                GridBagLayout gridBagLayout = new GridBagLayout();
                subItemsPanel.setLayout(gridBagLayout);


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

    private void validateSharedFields(){
        for(CustomInputField field : sharedInputFields.values()){
            field.validate(field.getjTextField().getText());
        }
    }

    /**
     * revalidates shared input fields, as well as those of
     * the category selected
     *
     * @return
     */
    public boolean validateAllFields(){
        validateSharedFields();

        Map<String, CustomInputField> categoryFields = categorySpecificFields.get(currentPanel);
        for(CustomInputField field : categoryFields.values()){
            field.validate(field.getjTextField().getText());
        }

        return (sharedInputFields.values().stream().allMatch(CustomInputField::isValid)
                && categoryFields.values().stream().allMatch(CustomInputField::isValid)
        );
    }

    private void updateButtonState() {
        Map<String, CustomInputField> categoryFields = categorySpecificFields.get(currentPanel);

        submitButton.setEnabled(sharedInputFields.values().stream().allMatch(CustomInputField::isValid)
                && categoryFields.values().stream().allMatch(CustomInputField::isValid)
        );
    }

    public void setErrorMessage(String err){
        errorMessage.setText(err);
    }

    /**
     * given the character, will take values from the panel a instantiate a
     * temporary product of given type.
     * @param type L,S,R,M etc..
     * @return a subtype of Product
     */
    public Product getProductFromInputs(Character type){

        switch(type){
            case 'L': {
                Locomotive locomotive = new Locomotive(
                        sharedInputFields.get("productCode").getjTextField().getText(),
                        "PLACEHOLDER",
                        Float.parseFloat(sharedInputFields.get("price").getjTextField().getText()),
                        gauges.get((String) gaugesComboBox.getSelectedItem()),
                        sharedInputFields.get("brand").getjTextField().getText(),
                        false,
                        Integer.parseInt(quantityField.getText()),
                        locomotiveInputFields.get("brClass").getjTextField().getText(),
                        locomotiveInputFields.get("individualName").getjTextField().getText(),
                        Integer.parseInt(locomotiveInputFields.get("era").getjTextField().getText()),
                        Locomotive.DCCType.deriveType((String) powerTypeComboBox.getSelectedItem()));
                locomotive.setName(locomotive.deriveName());
                return locomotive;
            }
            case 'S':{
                RollingStock rollingStock = new RollingStock(
                        sharedInputFields.get("productCode").getjTextField().getText(),
                        "PLACEHOLDER",
                        Float.parseFloat(sharedInputFields.get("price").getjTextField().getText()),
                        gauges.get((String) gaugesComboBox.getSelectedItem()),
                        sharedInputFields.get("brand").getjTextField().getText(),
                        false,
                        Integer.parseInt(quantityField.getText()),
                        rollingStockInputFields.get("mark").getjTextField().getText(),
                        rollingStockInputFields.get("kind").getjTextField().getText(),
                        RollingStock.Class_.deriveType((String)classesComboBox.getSelectedItem()),
                        Integer.parseInt(rollingStockInputFields.get("era").getjTextField().getText()));
                rollingStock.setName(rollingStock.deriveName());
                return rollingStock;
            }
            case 'R':{
                Track track = new Track(
                        sharedInputFields.get("productCode").getjTextField().getText(),
                        "PLACEHOLDER",
                        Float.parseFloat(sharedInputFields.get("price").getjTextField().getText()),
                        gauges.get((String) gaugesComboBox.getSelectedItem()),
                        sharedInputFields.get("brand").getjTextField().getText(),
                        false,
                        Integer.parseInt(quantityField.getText()),
                        trackInputFields.get("descriptor").getjTextField().getText(),
                        Track.TrackType.deriveType((String)trackTypeComboBox.getSelectedItem()));
                track.setName(track.deriveName());
                return track;
            }
            case 'C':{
                Controller controller = new Controller(
                        sharedInputFields.get("productCode").getjTextField().getText(),
                        "PLACEHOLDER",
                        Float.parseFloat(sharedInputFields.get("price").getjTextField().getText()),
                        gauges.get((String) gaugesComboBox.getSelectedItem()),
                        sharedInputFields.get("brand").getjTextField().getText(),
                        false,
                        Integer.parseInt(quantityField.getText()),
                        controllerInputFields.get("descriptor").getjTextField().getText(),
                        Controller.PowerType.valueOf((String)controllerTypeComboBox.getSelectedItem()));
                controller.setName(controller.deriveName());
                return controller;
            }
            default:
                throw new RuntimeException("Unknown Type: " + type);
        }
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
