package uk.ac.sheffield.com2008.view.staff;

import uk.ac.sheffield.com2008.config.Colors;
import uk.ac.sheffield.com2008.controller.staff.EditFormController;
import uk.ac.sheffield.com2008.controller.staff.FormController;
import uk.ac.sheffield.com2008.model.domain.data.ProductSetItem;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.model.entities.products.*;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.util.FieldsValidationManager;
import uk.ac.sheffield.com2008.view.components.CustomInputField;
import uk.ac.sheffield.com2008.view.modals.ProductSetModal;
import uk.ac.sheffield.com2008.view.components.Button;
import uk.ac.sheffield.com2008.view.modals.UpdateProductSetModal;


import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class EditProductRecordForm extends StaffView{

    private final EditFormController editFormController;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private final JButton submitButton;
    private Map<String, Class<?>> classMap = new HashMap<>();
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
    private Map<Product, Integer> selectedProductsMap = new HashMap<>();

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

    private final Map<String, CustomInputField> trainSetInputFields = new HashMap<>();
    private final Map<String, CustomInputField> trackPackInputFields = new HashMap<>();
    JRadioButton starterOval;
    JRadioButton extension;
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

    private Map<JPanel, Map<String, CustomInputField>> categorySpecificFields = new HashMap<>();

    JLabel itemSelectedTP;
    JLabel itemSelectedTS;
    JPanel inSetPanel;
    JPanel inPackPanel;
    Product selectedSetProduct;

    public EditProductRecordForm(EditFormController editFormController) {
        this.editFormController = editFormController;
        this.submitButton = new Button("Save");

        categoryComboBox = new JComboBox<>(categories);
        this.submitButton.addActionListener(e -> {
            editFormController.tryUpdateProduct(editFormController.getProductUnderEdit());
        });

        gauges.put("OO Gauge (1/76th scale)", Product.Gauge.OO);
        gauges.put("TT Gauge (1/120th scale)", Product.Gauge.TT);
        gauges.put("N gauge (1/148th scale)", Product.Gauge.N);
        gaugesComboBox = new JComboBox<>(gauges.keySet().toArray(new String[0]));
        gaugeLabel = new JLabel("Gauge: ");

        inSetPanel = new JPanel();
        inPackPanel = new JPanel();
        itemSelectedTP = new JLabel("None");
        itemSelectedTS = new JLabel("None");

        classMap.put("Starter Oval Track Pack", Track.class);
        classMap.put("Extension Track Pack", Track.class);
        classMap.put("Locomotive", Locomotive.class);
        classMap.put("Rolling Stock", RollingStock.class);
        classMap.put("Track", Track.class);
        classMap.put("Controller", Controller.class);
        classMap.put("Track Pack", TrackPack.class);
    }

    public void onRefresh(){
        removeAll();
        selectedProductsMap = new HashMap<>();
        initializeUI();
        revalidate();
        repaint();

        // TODO: fill in fields
    }

    private Map<Product, Integer> getProductsInSet(ProductSet set){
        ArrayList<ProductSetItem> setItems = (ArrayList<ProductSetItem>) set.getSetItems();
        Map<Product, Integer> productsMap = new HashMap<>();
        setItems.forEach(psi -> productsMap.put(psi.getProduct(), psi.getQuantity()));
        return productsMap;
    }

    private void initializeUI() {

        Product productUnderEdit = editFormController.getProductUnderEdit();

        JPanel content = new JPanel();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        selectedProductsMap = new HashMap<>();
        inSetPanel.removeAll();
        inPackPanel.removeAll();
        submitButton.setEnabled(false);
        content.setLayout(new GridBagLayout());
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
        content.add(errorMessage, gbc);
        gbc.insets = new Insets(0, 0, 0, 0);

        gbc.gridy++;
        gbc.gridwidth = 1;
        content.add(new JLabel("Editing Product Code: " + productUnderEdit.getProductCode()), gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        content.add(new JLabel("Brand: "), gbc);

        CustomInputField brandField = new CustomInputField("",
                this::updateButtonState,
                false
        );
        brandField.setValidationFunction(
                () -> FieldsValidationManager.validateForLength(
                        brandField.getjTextField().getText(),
                        3));
        sharedInputFields.put("brand", brandField);
        brandField.getjTextField().setText(productUnderEdit.getBrand());


        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        brandField.addToPanel(content, gbc);

        if(!(productUnderEdit instanceof Controller)){
            gbc.gridx = 0;
            gbc.gridy++;
            gbc.gridwidth = 1;
            content.add(gaugeLabel, gbc);

            gbc.gridx = 1;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            content.add(gaugesComboBox, gbc);

            Product.Gauge searchValue = productUnderEdit.getGauge();

            String foundKey = gauges.entrySet().stream()
                    .filter(entry -> Objects.equals(entry.getValue(), searchValue))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse("NULL");
            gaugesComboBox.setSelectedItem(foundKey);
        }

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        content.add(new JLabel("Price: "), gbc);

        CustomInputField priceField = new CustomInputField("",
                this::updateButtonState,
                false
        );
        priceField.setValidationFunction(
                () -> FieldsValidationManager.validatePrice(priceField.getjTextField().getText()));
        sharedInputFields.put("price", priceField);

        float price = productUnderEdit.getPrice();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String formattedPrice = decimalFormat.format(price);
        priceField.getjTextField().setText(formattedPrice);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        priceField.addToPanel(content, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        content.add(new JLabel("Quantity: "), gbc);

        NumberFormat integerFormat = NumberFormat.getIntegerInstance();
        NumberFormatter formatter = new NumberFormatter(integerFormat);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        quantityField = new JFormattedTextField(formatter);
        quantityField.setValue(productUnderEdit.getStock());
        quantityField.setColumns(10);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        content.add(quantityField, gbc);

        // Add cardPanel to the layout
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;

        categorySpecificFields = new HashMap<>();
        if(productUnderEdit instanceof Locomotive){
            locomotivePanel = locomotivePanel();
            cardPanel.add(locomotivePanel);
            categorySpecificFields.put(locomotivePanel, locomotiveInputFields);
            currentPanel = locomotivePanel;
        }
        else if(productUnderEdit instanceof RollingStock){
            rollingStockPanel = rollingStocksPanel();
            cardPanel.add(rollingStockPanel);
            categorySpecificFields.put(rollingStockPanel, rollingStockInputFields);
            currentPanel = rollingStockPanel;
        }
        else if(productUnderEdit instanceof Track){
            trackPanel = trackPanel();
            cardPanel.add(trackPanel);
            categorySpecificFields.put(trackPanel, trackInputFields);
            currentPanel = trackPanel;
        }
        else if(productUnderEdit instanceof Controller){
            controllerPanel = controllersPanel();
            cardPanel.add(controllerPanel);
            categorySpecificFields.put(controllerPanel, controllerInputFields);
            currentPanel = controllerPanel;
        }
        else if(productUnderEdit instanceof TrackPack){
            trackPackPanel = trackPackPanel();
            cardPanel.add(trackPackPanel);
            categorySpecificFields.put(trackPackPanel, trackPackInputFields);
            currentPanel = trackPackPanel;
        }
        else if(productUnderEdit instanceof TrainSet){
            trainSetPanel = trainSetsPanel();
            cardPanel.add(trainSetPanel);
            categorySpecificFields.put(trainSetPanel, trainSetInputFields);
            currentPanel = trainSetPanel;
        }
        else{
            // throw error?
        }

        cardLayout.show(cardPanel, "panel");
        content.add(cardPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        content.add(buttonPanel(), gbc);

        JScrollPane pageScroll = new JScrollPane(content);
        pageScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(pageScroll);
    }

    private JPanel locomotivePanel() {

        Locomotive editingLocomotive = (Locomotive) editFormController.getProductUnderEdit();
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 0, 10);

        // BR Class
        CustomInputField brClassField = new CustomInputField("BR Class:", this::updateButtonState, false);
        brClassField.setValidationFunction(() -> FieldsValidationManager.validateForLength(
                brClassField.getjTextField().getText(),
                2));
        locomotiveInputFields.put("brClass", brClassField);
        brClassField.getjTextField().setText(editingLocomotive.getBrClass());
        brClassField.addToPanel(panel, gbc);

        // Individual Name
        gbc.gridx++;
        CustomInputField indivNameField = new CustomInputField("Individual Name:", this::updateButtonState, false);
        indivNameField.setValidationFunction(() -> FieldsValidationManager.validateForLength(
                indivNameField.getjTextField().getText(),
                3));
        locomotiveInputFields.put("individualName", indivNameField);
        indivNameField.getjTextField().setText(editingLocomotive.getIndividualName());
        gbc.anchor = GridBagConstraints.WEST;
        indivNameField.addToPanel(panel, gbc);

        // ERA
        gbc.gridx++;
        CustomInputField eraField = new CustomInputField("Era:", this::updateButtonState, false);
        eraField.setValidationFunction(() -> FieldsValidationManager.validateEra(
                eraField.getjTextField().getText()));
        locomotiveInputFields.put("era", eraField);
        eraField.getjTextField().setText(String.valueOf(editingLocomotive.getEra()));
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
        powerTypeComboBox.setSelectedItem(editingLocomotive.getDccType().deriveName());
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(powerTypeComboBox, gbc);

        return panel;
    }

    private JPanel rollingStocksPanel() {

        RollingStock editingRollingStock = (RollingStock) editFormController.getProductUnderEdit();
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 0, 10);

        // Mark
        CustomInputField markField = new CustomInputField("Mark:", this::updateButtonState, false);
        markField.setValidationFunction(() -> FieldsValidationManager.validateForLength(
                markField.getjTextField().getText(),
                2));
        rollingStockInputFields.put("mark", markField);
        markField.getjTextField().setText(editingRollingStock.getMark() == null ? "" : editingRollingStock.getMark());
        markField.addToPanel(panel, gbc);

        // Kind
        gbc.gridx++;
        CustomInputField kindField = new CustomInputField("Kind:", this::updateButtonState, false);
        kindField.setValidationFunction(() -> FieldsValidationManager.validateForLength(
                kindField.getjTextField().getText(),
                3));
        rollingStockInputFields.put("kind", kindField);
        gbc.anchor = GridBagConstraints.WEST;
        kindField.getjTextField().setText(editingRollingStock.getKind() == null ? "" : editingRollingStock.getKind());
        kindField.addToPanel(panel, gbc);

        // Class
        gbc.gridx += 2;
        gbc.anchor = GridBagConstraints.NORTH;
        panel.add(new JLabel("Class:"), gbc);
        java.util.List<String> classes = Arrays.stream(RollingStock.Class_.values())
                .map(RollingStock.Class_::deriveName)
                .collect(Collectors.toList());
        classes.add("Null");
        classesComboBox = new JComboBox<>(classes.toArray(new String[0]));
        gbc.anchor = GridBagConstraints.WEST;
        classesComboBox.setSelectedItem(editingRollingStock.getClass_().deriveName());
        panel.add(classesComboBox, gbc);

        // Era
        gbc.gridx++;
        CustomInputField eraField = new CustomInputField("Era:", this::updateButtonState, false);
        eraField.setValidationFunction(() -> FieldsValidationManager.validateEra(
                eraField.getjTextField().getText()));
        rollingStockInputFields.put("era", eraField);
        gbc.anchor = GridBagConstraints.WEST;
        eraField.getjTextField().setText(String.valueOf(editingRollingStock.getEra()));
        eraField.addToPanel(panel, gbc);

        return panel;
    }

    private JPanel trackPanel() {
        Track editingTrack = (Track) editFormController.getProductUnderEdit();
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 0, 10);

        // Descriptor
        CustomInputField descriptorField = new CustomInputField("Descriptor: ", this::updateButtonState, false);
        descriptorField.setValidationFunction(() -> FieldsValidationManager.validateForLength(
                descriptorField.getjTextField().getText(), 3));
        trackInputFields.put("descriptor", descriptorField);
        gbc.anchor = GridBagConstraints.WEST;
        descriptorField.getjTextField().setText(editingTrack.getDescriptor() == null ? "" : editingTrack.getDescriptor());
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
        trackTypeComboBox.setSelectedItem(editingTrack.getTrackType().deriveName());
        panel.add(trackTypeComboBox, gbc);

        return panel;
    }

    private JPanel controllersPanel() {
        Controller editingController = (Controller) editFormController.getProductUnderEdit();
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 0, 10);

        // Descriptor
        CustomInputField descriptorField = new CustomInputField("Descriptor: ", this::updateButtonState, false);
        descriptorField.setValidationFunction(() -> FieldsValidationManager.validateForLength(
                descriptorField.getjTextField().getText(), 3));
        controllerInputFields.put("descriptor", descriptorField);
        gbc.anchor = GridBagConstraints.WEST;
        descriptorField.getjTextField().setText(editingController.getDescriptor());
        descriptorField.addToPanel(panel, gbc);

        // Power Type
        gbc.gridx++;
        gbc.anchor = GridBagConstraints.NORTH;
        panel.add(new JLabel("Power Type:"), gbc);
        String[] powerTypes = Arrays.stream(Controller.PowerType.values())
                .map(Controller.PowerType::deriveName)
                .toArray(String[]::new);
        controllerTypeComboBox = new JComboBox<>(powerTypes);
        gbc.anchor = GridBagConstraints.WEST;
        controllerTypeComboBox.setSelectedItem(editingController.getPowerType().deriveName());
        panel.add(controllerTypeComboBox, gbc);

        return panel;
    }

    private JPanel trackPackPanel() {
        TrackPack editingTrackPack = (TrackPack) editFormController.getProductUnderEdit();
        selectedProductsMap = getProductsInSet(editingTrackPack);
        editingTrackPack.PrintFullSet();
        System.out.println(selectedProductsMap.size());
        java.util.List<Product> allProducts = editFormController.getAllProducts();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Set Name
        CustomInputField setNameField = new CustomInputField("Set Name:", this::updateButtonState, false);
        setNameField.setValidationFunction(() -> FieldsValidationManager.validateForLength(
                setNameField.getjTextField().getText(),
                3));
        trackPackInputFields.put("setName", setNameField);
        setNameField.getjTextField().setText(editingTrackPack.getSetName());
        setNameField.addToPanel(panel);

        JPanel radioButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        starterOval = new JRadioButton("Starter Oval Track Pack");
        extension = new JRadioButton("Extension Track Pack");
        ButtonGroup buttonGroup = new ButtonGroup();

        if(editingTrackPack.getTrackPackType() == TrackPack.TrackPackType.STARTER){
            starterOval.setSelected(true);
        }
        else{
            extension.setSelected(true);
        }

        buttonGroup.add(extension);
        buttonGroup.add(starterOval);
        radioButtonsPanel.add(starterOval);
        radioButtonsPanel.add(extension);
        panel.add(radioButtonsPanel);

        // Header panel
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel title = new JLabel("Add products to Track Pack: ");
        row1.add(title);
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton findButton = new JButton("Add a Track:");
        findButton.addActionListener(e -> {
            String className = "Track";
            Class<?> classType = classMap.get(className);
            List<Product> filteredProducts = allProducts.stream().filter(classType::isInstance).toList();
            // Pass the correct list of filtered products to the modal
            UpdateProductSetModal modal = new UpdateProductSetModal(editFormController, (JFrame) SwingUtilities.getWindowAncestor(findButton), EditProductRecordForm.this, filteredProducts);
            modal.setVisible(true);
        });
        row2.add(findButton);
        headerPanel.add(row1);
        headerPanel.add(row2);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Selected panel
        JPanel selectedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel selected = new JLabel("Item Selected: ");

        JButton addButton = new JButton("Add");

        selectedPanel.add(selected);
        selectedPanel.add(itemSelectedTP);
        selectedPanel.add(addButton);
        panel.add(selectedPanel);

        //Items in set pane;
        inPackPanel.setLayout(new BoxLayout(inPackPanel, BoxLayout.Y_AXIS));
        inPackPanel.setPreferredSize(new Dimension(500, 200));

        JScrollPane scrollPane = new JScrollPane(inPackPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        Dimension preferredSize = inPackPanel.getPreferredSize();
        preferredSize.setSize(preferredSize.getWidth() + 30, preferredSize.getHeight());
        scrollPane.setMinimumSize(preferredSize);
        panel.add(scrollPane);

        populateInSetPanel(selectedProductsMap, inPackPanel);
        addButton.addActionListener(e -> {
            if (selectedSetProduct != null && !selectedProductsMap.containsKey(selectedSetProduct)) {
                selectedProductsMap.put(selectedSetProduct, 1);
            }
            populateInSetPanel(selectedProductsMap, inPackPanel);
        });
        return panel;

    }
    private JPanel trainSetsPanel() {
        TrainSet editingTrainSet = (TrainSet) editFormController.getProductUnderEdit();
        selectedProductsMap = getProductsInSet(editingTrainSet);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Set Name
        CustomInputField setNameField = new CustomInputField("Set Name:", this::updateButtonState, false);
        setNameField.setValidationFunction(() -> FieldsValidationManager.validateForLength(
                setNameField.getjTextField().getText(),
                3));
        trainSetInputFields.put("setName", setNameField);
        setNameField.getjTextField().setText(editingTrainSet.getSetName());
        setNameField.addToPanel(panel);

        // Header panel
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel title = new JLabel("Add products to set: ");
        row1.add(title);
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] itemTypes = {"Locomotive", "Rolling Stock", "Controller", "Track Pack"};
        JComboBox<String> itemTypesComboBox = new JComboBox<>(itemTypes);
        row2.add(itemTypesComboBox);
        List<Product> allProducts = editFormController.getAllProducts();

        JButton findButton = new JButton("Find");
        findButton.addActionListener(e -> {
            String className = (String) itemTypesComboBox.getSelectedItem();
            Class<?> classType = classMap.get(className);
            List<Product> filteredProducts = allProducts.stream().filter(classType::isInstance).toList();
            // Pass the correct list of filtered products to the modal
            UpdateProductSetModal modal = new UpdateProductSetModal(editFormController, (JFrame) SwingUtilities.getWindowAncestor(findButton), EditProductRecordForm.this, filteredProducts);
            modal.setVisible(true);
        });
        row2.add(findButton);
        headerPanel.add(row1);
        headerPanel.add(row2);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Selected panel
        JPanel selectedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel selected = new JLabel("Item Selected: ");

        JButton addButton = new JButton("Add");

        selectedPanel.add(selected);
        selectedPanel.add(itemSelectedTS);
        selectedPanel.add(addButton);
        panel.add(selectedPanel, BorderLayout.CENTER);

        //Items in set panel
        inSetPanel = new JPanel();
        inSetPanel.setLayout(new BoxLayout(inSetPanel, BoxLayout.Y_AXIS));
        inSetPanel.setPreferredSize(new Dimension(500, 300));


        JScrollPane scrollPane = new JScrollPane(inSetPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        Dimension preferredSize = inSetPanel.getPreferredSize();
        scrollPane.setMinimumSize(preferredSize);
        panel.add(scrollPane);

        //Add heading panel to inset panel
        JPanel inSetHeadingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel displayItemsLabel = new JLabel("Items in set:  ");
        inSetHeadingPanel.add(displayItemsLabel);
        inSetPanel.add(inSetHeadingPanel);

        populateInSetPanel(selectedProductsMap, inSetPanel);
        addButton.addActionListener(e -> {
            if (selectedSetProduct != null && !selectedProductsMap.containsKey(selectedSetProduct)) {
                selectedProductsMap.put(selectedSetProduct, 1);
            }
            populateInSetPanel(selectedProductsMap, inSetPanel);
        });

        return panel;
    }

    private JPanel buttonPanel() {
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton cancelButton = new Button("Cancel");
        buttonsPanel.add(cancelButton);
        cancelButton.addActionListener(e -> editFormController.getNavigation().navigate(Navigation.PRODUCT_RECORD));
        buttonsPanel.add(submitButton);

        return buttonsPanel;
    }

    private void populateInSetPanel(Map<Product, Integer> products, JPanel inSetPanel) {
        // Add a new subItemsPanel for each selected product in the map
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        inSetPanel.removeAll();
        inSetPanel.setLayout(new BoxLayout(inSetPanel, BoxLayout.Y_AXIS));
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            String selectedProductName = entry.getKey().printName();
            System.out.println(selectedProductName);
            String selectedProductCode = entry.getKey().getProductCode();
            Integer qty = entry.getValue();

            JPanel subItemsPanel = new JPanel();
            GridBagLayout gridBagLayout = new GridBagLayout();
            subItemsPanel.setLayout(gridBagLayout);

            gbc.gridx = 0;
            gbc.anchor = GridBagConstraints.NORTHWEST;
            gbc.insets = new Insets(5, 5, 5, 5);

            Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
            subItemsPanel.setBorder(emptyBorder);

            JLabel itemCodeLabel = new JLabel(selectedProductCode);
            JLabel itemNameLabel = new JLabel(selectedProductName);
            JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel((int) qty, 1, Integer.MAX_VALUE, 1));
            quantitySpinner.addChangeListener(e -> {
                int newValue = (int) quantitySpinner.getValue();
                selectedProductsMap.put(entry.getKey(), newValue);
            });
            Dimension spinnerPreferredSize = quantitySpinner.getPreferredSize();
            spinnerPreferredSize.width = 40; // Adjust the width as needed
            quantitySpinner.setPreferredSize(spinnerPreferredSize);
            JButton removeItemButton = new JButton("X");
            removeItemButton.addActionListener(e -> {
                selectedProductsMap.remove(entry.getKey());
                populateInSetPanel(products, inSetPanel); //essentially repaint
            });

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
    }

    public void setSelectedSetProduct(Product p) {
        selectedSetProduct = p;
        itemSelectedTP.setText(selectedSetProduct.getName());
        itemSelectedTS.setText(selectedSetProduct.getName());
    }

    private void validateSharedFields() {
        for (CustomInputField field : sharedInputFields.values()) {
            field.validate(field.getjTextField().getText());
        }
    }

    /**
     * revalidates shared input fields, as well as those of
     * the category selected
     *
     * @return
     */
    public boolean validateAllFields() {
        validateSharedFields();

        Map<String, CustomInputField> categoryFields = categorySpecificFields.get(currentPanel);
        for (CustomInputField field : categoryFields.values()) {
            field.validate(field.getjTextField().getText());
        }

        return (sharedInputFields.values().stream().allMatch(CustomInputField::isValid)
                && categoryFields.values().stream().allMatch(CustomInputField::isValid)
        );
    }

    private void updateButtonState() {
        if(currentPanel == null){
            return;
        }

        Map<String, CustomInputField> categoryFields = categorySpecificFields.get(currentPanel);
        if(categoryFields == null){ //this is aids but it works
            return;
        }

        submitButton.setEnabled(sharedInputFields.values().stream().allMatch(CustomInputField::isValid)
                && categoryFields.values().stream().allMatch(CustomInputField::isValid)
        );
    }

    public void setErrorMessage(String err) {
        errorMessage.setText(err);
    }

    public Product loadProductByType(Character type, Product productUnderEdit) {

        switch (type) {
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
            case 'S': {
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
                        RollingStock.Class_.deriveType((String) classesComboBox.getSelectedItem()),
                        Integer.parseInt(rollingStockInputFields.get("era").getjTextField().getText()));
                rollingStock.setName(rollingStock.deriveName());
                return rollingStock;
            }
            case 'R': {
                Track track = new Track(
                        sharedInputFields.get("productCode").getjTextField().getText(),
                        "PLACEHOLDER",
                        Float.parseFloat(sharedInputFields.get("price").getjTextField().getText()),
                        gauges.get((String) gaugesComboBox.getSelectedItem()),
                        sharedInputFields.get("brand").getjTextField().getText(),
                        false,
                        Integer.parseInt(quantityField.getText()),
                        trackInputFields.get("descriptor").getjTextField().getText(),
                        Track.TrackType.deriveType((String) trackTypeComboBox.getSelectedItem()));
                track.setName(track.deriveName());
                return track;
            }
            case 'C': {
                Controller controller = new Controller(
                        sharedInputFields.get("productCode").getjTextField().getText(),
                        "PLACEHOLDER",
                        Float.parseFloat(sharedInputFields.get("price").getjTextField().getText()),
                        gauges.get((String) gaugesComboBox.getSelectedItem()),
                        sharedInputFields.get("brand").getjTextField().getText(),
                        false,
                        Integer.parseInt(quantityField.getText()),
                        controllerInputFields.get("descriptor").getjTextField().getText(),
                        Controller.PowerType.deriveType((String) controllerTypeComboBox.getSelectedItem()));
                controller.setName(controller.deriveName());
                return controller;
            }
            case 'M': {
                ArrayList<ProductSetItem> setItems = new ArrayList<>();
                for (Map.Entry<Product, Integer> entry : selectedProductsMap.entrySet()) {
                    ProductSetItem setItem = new ProductSetItem(entry.getKey(), entry.getValue());
                    setItems.add(setItem);
                }
                TrainSet trainSet = new TrainSet(
                        sharedInputFields.get("productCode").getjTextField().getText(),
                        "PLACEHOLDER",
                        Float.parseFloat(sharedInputFields.get("price").getjTextField().getText()),
                        gauges.get((String) gaugesComboBox.getSelectedItem()),
                        sharedInputFields.get("brand").getjTextField().getText(),
                        true,
                        1,
                        trainSetInputFields.get("setName").getjTextField().getText(),
                        setItems
                );
                trainSet.setName(trainSet.deriveName());
                return trainSet;
            }
            case 'P': {
                ArrayList<ProductSetItem> setItems = new ArrayList<>();
                for (Map.Entry<Product, Integer> entry : selectedProductsMap.entrySet()) {
                    ProductSetItem setItem = new ProductSetItem(entry.getKey(), entry.getValue());
                    setItems.add(setItem);
                }
                TrackPack.TrackPackType tpType = TrackPack.TrackPackType.STARTER;
                if (extension.isSelected()) {
                    tpType = TrackPack.TrackPackType.EXTENSION;
                }

                TrackPack trackPack = new TrackPack(
                        sharedInputFields.get("productCode").getjTextField().getText(),
                        "PLACEHOLDER",
                        Float.parseFloat(sharedInputFields.get("price").getjTextField().getText()),
                        gauges.get((String) gaugesComboBox.getSelectedItem()),
                        sharedInputFields.get("brand").getjTextField().getText(),
                        true,
                        1,
                        trackPackInputFields.get("setName").getjTextField().getText(),
                        tpType,
                        setItems
                );
                trackPack.setName(trackPack.deriveName());
                return trackPack;
            }
            default:
                throw new RuntimeException("Unknown Type: " + type);
        }
    }
}
