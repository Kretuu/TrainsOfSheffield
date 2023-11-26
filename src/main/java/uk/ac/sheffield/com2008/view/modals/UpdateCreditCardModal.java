package uk.ac.sheffield.com2008.view.modals;

import uk.ac.sheffield.com2008.exceptions.BankDetailsEncryptionException;
import uk.ac.sheffield.com2008.model.entities.BankingCard;
import uk.ac.sheffield.com2008.util.DateUtils;
import uk.ac.sheffield.com2008.util.FieldsValidationManager;
import uk.ac.sheffield.com2008.view.components.CustomInputField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public abstract class UpdateCreditCardModal extends JDialog {
    private final JButton submitButton;
    private final BankingCard bankingCard;
    private final JLabel errorMessage;
    private final JPanel content;
    private final Map<String, CustomInputField> inputFields = new HashMap<>();
    private final Map<String, JComboBox<Integer>> comboBoxes = new HashMap<>();

    public UpdateCreditCardModal(JFrame frame, BankingCard bankingCard) {
        super(frame, "Banking Card Details", true);
        this.submitButton = new JButton("Save");
        this.content = new JPanel();
        this.errorMessage = new JLabel(" ");
        this.bankingCard = bankingCard;

        initialiseUI();

        setContentPane(content);
        setMinimumSize(new Dimension(500, 300));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));
        setResizable(false);
        setLocationRelativeTo(frame);
    }

    private void initialiseUI() {
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.setBorder(new EmptyBorder(15, 15, 15, 15));

        errorMessage.setForeground(Color.RED);
        content.add(errorMessage);

        submitButton.setEnabled(false);
        submitButton.addActionListener(e -> updateCardDetails());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        createTextFields();
        populateFieldsWithCardDetails();

        JPanel buttonsPanel = new JPanel(new GridLayout(1,2,10,0));
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(submitButton);
        content.add(buttonsPanel);
    }

    private void createTextFields() {
        CustomInputField cardNumber = new CustomInputField("Card Number", this::updateButtonState, false);
        cardNumber.setValidationFunction(() -> FieldsValidationManager.validateBankingCard(cardNumber.getjTextField().getText()));
        cardNumber.addToPanel(content);
        inputFields.put("cardNumber", cardNumber);

        CustomInputField holderName = new CustomInputField("Holder Name", this::updateButtonState, false);
        holderName.addToPanel(content);
        inputFields.put("holderName", holderName);

        JPanel bottomInformationPanel = new JPanel(new GridLayout(1, 2));


        bottomInformationPanel.add(createExpiryDate());

        CustomInputField securityCode = new CustomInputField("Security Code", this::updateButtonState, false);
        securityCode.setValidationFunction(() -> FieldsValidationManager.validateSecurityCode(securityCode.getjTextField().getText()));
        securityCode.addToPanel(bottomInformationPanel);
        inputFields.put("securityCode", securityCode);

        content.add(bottomInformationPanel);
    }

    private void populateFieldsWithCardDetails() {
        if(this.bankingCard == null) return;

        inputFields.get("cardNumber").getjTextField().setText(bankingCard.getNumber());
        inputFields.get("securityCode").getjTextField().setText(bankingCard.getCvv());
        inputFields.get("holderName").getjTextField().setText(bankingCard.getHolderName());

        Calendar expiryDate = Calendar.getInstance();
        expiryDate.setTime(bankingCard.getExpiryDate());
        int month = expiryDate.get(Calendar.MONTH) + 1;
        int year = expiryDate.get(Calendar.YEAR);

        comboBoxes.get("months").setSelectedItem(month);
        comboBoxes.get("years").setSelectedItem(year);
    }

    private JPanel createExpiryDate() {
        JPanel expiryDate = new JPanel(new GridLayout(2, 1));
        JLabel expiryDateLabel = new JLabel("Expiry Date");

        JPanel expiryDateInputs = new JPanel();
        JComboBox<Integer> months = new JComboBox<>(IntStream.rangeClosed(1, 12).boxed().toArray(Integer[]::new));
        comboBoxes.put("months", months);
        expiryDateInputs.add(months);

        JLabel slash = new JLabel("/");
        expiryDateInputs.add(slash);

        JComboBox<Integer> years = new JComboBox<>(
                IntStream.rangeClosed(Calendar.getInstance().get(Calendar.YEAR), 2050).boxed().toArray(Integer[]::new)
        );
        comboBoxes.put("years", years);
        expiryDateInputs.add(years);

        expiryDate.add(expiryDateLabel);
        expiryDate.add(expiryDateInputs);

        months.setSelectedItem(Calendar.getInstance().get(Calendar.MONTH) + 1);
        months.addItemListener(e -> {
            Integer yearItem = years.getSelectedItem() == null ? null : (Integer) years.getSelectedItem();
            if(e.getStateChange() == ItemEvent.SELECTED && yearItem != null) {
                long dateDiff = new Date().getTime() - DateUtils.getLastDayOfMonth(yearItem, (Integer) e.getItem()).getTime();
                if(dateDiff > 0) {
                    years.setSelectedItem(Calendar.getInstance().get(Calendar.YEAR) + 1);
                }
            }
        });

        years.setSelectedItem(Calendar.getInstance().get(Calendar.YEAR));
        years.addItemListener(e -> {
            Integer monthItem = months.getSelectedItem() == null ? null : (Integer) months.getSelectedItem();
            if(e.getStateChange() == ItemEvent.SELECTED && monthItem != null) {
                long dateDiff = new Date().getTime() - DateUtils.getLastDayOfMonth((Integer) e.getItem(), monthItem).getTime();
                if(dateDiff > 0) {
                    months.setSelectedItem(Calendar.getInstance().get(Calendar.MONTH) + 1);
                }
            }
        });


        return expiryDate;
    }

    private void updateButtonState() {
        submitButton.setEnabled(inputFields.values().stream().allMatch(CustomInputField::isValid));
    }

    private void updateErrorMessage(String message) {
        if(message == null) {
            errorMessage.setText(" ");
            return;
        }
        errorMessage.setText("Error: " + message);
    }

    private void updateCardDetails() {
        Integer month = (Integer) comboBoxes.get("months").getSelectedItem();
        Integer year = (Integer) comboBoxes.get("years").getSelectedItem();
        if(month == null || year == null) {
            updateErrorMessage("Expiry date is invalid");
            return;
        }

        Date expiryDate = DateUtils.getLastDayOfMonth(year, month);
        JTextField cardNumberField = inputFields.get("cardNumber").getjTextField();
        String cardNumber = cardNumberField.getText().replaceAll("\\s", "");

        JTextField securityCodeField = inputFields.get("securityCode").getjTextField();
        String securityCode = securityCodeField.getText();

        JTextField holderNameField = inputFields.get("holderName").getjTextField();
        String holderName = holderNameField.getText();

        try {
            if(onSave(cardNumber, expiryDate, securityCode, holderName)) {
                cardNumberField.setText("");
                securityCodeField.setText("");
                holderNameField.setText("");
                updateErrorMessage(null);
                dispose();
            } else {
                updateErrorMessage("Cannot update banking card. Try again later");
            }
        } catch (SQLException e) {
            updateErrorMessage("Cannot connect to database.");
        } catch (BankDetailsEncryptionException e) {
            updateErrorMessage(e.getMessage());
        }
    }

    public abstract boolean onSave(String cardNumber, Date expiryDate, String securityCode, String holderName) throws SQLException, BankDetailsEncryptionException;

}
