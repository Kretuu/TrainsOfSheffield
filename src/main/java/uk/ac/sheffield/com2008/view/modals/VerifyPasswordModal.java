package uk.ac.sheffield.com2008.view.modals;

import uk.ac.sheffield.com2008.view.components.CustomInputField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public abstract class VerifyPasswordModal extends JDialog {
    private final JButton submitButton;
    private final JPanel content;
    private final JLabel errorMessage;
    private CustomInputField password;

    public VerifyPasswordModal(JFrame frame) {
        super(frame, "Banking Card Details", true);
        this.submitButton = new JButton("Save");
        this.content = new JPanel();
        this.errorMessage = new JLabel(" ");

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

        password = new CustomInputField("Password", this::updateButtonState, false, true);
        password.addToPanel(content);

        submitButton.setEnabled(false);
        submitButton.addActionListener(e -> {
            String errorMessage = onConfirm(password.getjPasswordField().getPassword());
            updateErrorMessage(errorMessage);
            if(errorMessage == null) dispose();
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        JPanel buttonsPanel = new JPanel(new GridLayout(1,2,10,0));
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(submitButton);
        content.add(buttonsPanel);
    }

    private void updateButtonState() {
        submitButton.setEnabled(password.isValid());
    }

    private void updateErrorMessage(String message) {
        if(message == null) {
            errorMessage.setText(" ");
            return;
        }
        errorMessage.setText("Error: " + message);
    }

    public abstract String onConfirm(char[] password);
}
