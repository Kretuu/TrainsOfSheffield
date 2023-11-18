package uk.ac.sheffield.com2008.view.components;

import uk.ac.sheffield.com2008.config.Colors;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.concurrent.Callable;

public class CustomInputField implements DocumentListener {
    private JTextField jTextField;
    private JPasswordField jPasswordField;
    private JLabel label;
    private JLabel errorMessage;
    private final Runnable updateButtonState;
    private Callable<String> validationFunction = null;
    private boolean isValid = false;

    public JTextField getjTextField() {
        return jTextField;
    }

    public JPasswordField getjPasswordField() {
        return jPasswordField;
    }

    public boolean isValid() {
        return isValid;
    }

    public CustomInputField(String label, Runnable updateButtonState) {
        this(label, updateButtonState, false);
    }

    public CustomInputField(String label, Runnable updateButtonState, boolean isPassword) {
        this.updateButtonState = updateButtonState;
        createComponents(label, isPassword);
        addListeners();
    }

    public void setValidationFunction(Callable<String> validationFunction) {
        this.validationFunction = validationFunction;
    }

    private void createComponents(String label, boolean isPassword) {
        if(isPassword) {
            this.jPasswordField = new JPasswordField(20);
            this.jTextField = jPasswordField;
        } else {
            this.jTextField = new JTextField(20);
        }

        this.label = new JLabel(label);
        this.errorMessage = new JLabel(" ");
        this.errorMessage.setForeground(Colors.TEXT_FIELD_ERROR);
    }

    private void addListeners() {
        jTextField.getDocument().addDocumentListener(this);
        jTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                jTextField.setBorder(new LineBorder(Color.BLACK));
            }

            @Override
            public void focusLost(FocusEvent e) {
                jTextField.setBorder(new LineBorder(Color.DARK_GRAY));
            }
        });
    }


    public void addToPanel(JPanel panel) {
        JPanel internalPanel = new JPanel(new GridLayout(3, 1));
        internalPanel.add(label);
        internalPanel.add(jTextField);
        internalPanel.add(errorMessage);
        panel.add(internalPanel);
    }

    public void validate(String text) {
        String error = null;
        if(validationFunction != null) {
            try {
                error = validationFunction.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        boolean newValidState;
        if(error == null) {
            Color textFieldColor = jTextField.hasFocus() ? Colors.TEXT_FIELD_FOCUSED : Colors.TEXT_FIELD_UNFOCUSED;
            jTextField.setBorder(new LineBorder(textFieldColor));
            errorMessage.setText(" ");
            newValidState = true;
        } else {
            errorMessage.setText(error);
            jTextField.setBorder(new LineBorder(Colors.TEXT_FIELD_ERROR));
            newValidState = false;
        }
        if(text.isEmpty()) {
            errorMessage.setText(label.getText() + " cannot be empty");
            jTextField.setBorder(new LineBorder(Colors.TEXT_FIELD_ERROR));
            newValidState = false;
        }
        if(newValidState != isValid) {
            isValid = newValidState;
            updateButtonState.run();
        }

    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        verifyInput(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        verifyInput(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }

    private void verifyInput(DocumentEvent e) {
        try {
            String text = e.getDocument().getText(0, e.getDocument().getLength());
            validate(text);
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }
    }
}
