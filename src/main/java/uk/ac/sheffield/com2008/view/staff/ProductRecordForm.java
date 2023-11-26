package uk.ac.sheffield.com2008.view.staff;

import uk.ac.sheffield.com2008.controller.staff.FormController;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.util.FieldsValidationManager;
import uk.ac.sheffield.com2008.view.components.CustomInputField;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class ProductRecordForm extends StaffView {

    private final FormController formController;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    private final JButton submitButton;
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
            revalidate();  // Ensure the layout manager updates the container

            if(selectedCategory.equals("Controller")){
                gaugesComboBox.setVisible(false);
                gaugeLabel.setVisible(false);
            }
            else{
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
        } else if ("Track Pack".equals(category)){
            return trackPackPanel();
        }else{
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

        JLabel trackLabel =new JLabel("Track");
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
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        panel.add(new JLabel("Locomotive"));
        JSpinner locomotiveSpinner = createSpinner();
        panel.add(locomotiveSpinner);
        panel.add(new JLabel("Carriage"));
        JSpinner carriageSpinner = createSpinner();
        panel.add(carriageSpinner);
        panel.add(new JLabel("Wagon"));
        JSpinner wagonSpinner = createSpinner();
        panel.add(wagonSpinner);
        panel.add(new JLabel("Controller"));
        JSpinner controllerSpinner = createSpinner();
        panel.add(controllerSpinner);
        panel.add(new JLabel("Starter Oval Track Pack"));
        JSpinner starterOvalTrackPackSpinner = createSpinner();
        panel.add(starterOvalTrackPackSpinner);
        panel.add(new JLabel("Extension Track Pack"));
        JSpinner extensionTrackPackSpinner = createSpinner();
        panel.add(extensionTrackPackSpinner);

        return panel;

    }
        /*private JPanel trainSetsPanel() {
            JPanel panel = new JPanel(new GridLayout(3, 4));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

            panel.add(new JLabel("Locomotive"));
            panel.add(new JButton("Add"));

            panel.add(new JLabel("Rolling Stock"));
            panel.add(new JButton("Add"));

            panel.add(new JLabel("Controller"));
            panel.add(new JButton("Add"));

            panel.add(new JLabel("Starter Oval Track Pack"));
            panel.add(new JButton("Add"));

            panel.add(new JLabel("Extension Track Pack"));
            panel.add(new JButton("Add"));


            return panel;

        }*/


    private JSpinner createSpinner() {
        SpinnerModel model = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
        return new JSpinner(model);
    }

    private void updateButtonState() {
        submitButton.setEnabled(sharedInputFields.values().stream().allMatch(CustomInputField::isValid));
    }
}

    /*private JPanel trainSetsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5); // Add padding around components

        panel.add(new JLabel("Locomotive"), gbc);
        gbc.gridx = 1;
        JSpinner locomotiveSpinner = createSpinner();
        panel.add(locomotiveSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Carriage"), gbc);
        gbc.gridx = 1;
        JSpinner carriageSpinner = createSpinner();
        panel.add(carriageSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Wagon"), gbc);
        gbc.gridx = 1;
        JSpinner wagonSpinner = createSpinner();
        panel.add(wagonSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Controller"), gbc);
        gbc.gridx = 1;
        JSpinner controllerSpinner = createSpinner();
        panel.add(controllerSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Starter Oval Track Pack"), gbc);
        gbc.gridx = 1;
        JSpinner starterOvalTrackPackSpinner = createSpinner();
        panel.add(starterOvalTrackPackSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Extension Track Pack"), gbc);
        gbc.gridx = 1;
        JSpinner extensionTrackPackSpinner = createSpinner();
        panel.add(extensionTrackPackSpinner, gbc);

        // Adjust spinner size
        Dimension spinnerSize = new Dimension(30, 20);
        locomotiveSpinner.setPreferredSize(spinnerSize);
        carriageSpinner.setPreferredSize(spinnerSize);
        wagonSpinner.setPreferredSize(spinnerSize);
        controllerSpinner.setPreferredSize(spinnerSize);
        starterOvalTrackPackSpinner.setPreferredSize(spinnerSize);
        extensionTrackPackSpinner.setPreferredSize(spinnerSize);

        return panel;
    }

    private JSpinner createSpinner() {
        // Create and customize your spinner as needed
        JSpinner spinner = new JSpinner();
        return spinner;
    }




}
*/
