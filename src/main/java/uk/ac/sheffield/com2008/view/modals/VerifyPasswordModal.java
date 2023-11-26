package uk.ac.sheffield.com2008.view.modals;

import uk.ac.sheffield.com2008.exceptions.BankDetailsEncryptionException;
import uk.ac.sheffield.com2008.view.View;
import uk.ac.sheffield.com2008.view.components.CustomInputField;
import uk.ac.sheffield.com2008.view.components.InputForm;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

public abstract class VerifyPasswordModal extends JDialog {
    private final JPanel content;
    private final View view;
    private CustomInputField password;

    public VerifyPasswordModal(JFrame frame, View view) {
        super(frame, "Banking Card Details", true);
        this.content = new JPanel();
        this.view = view;

        initialiseUI();

        setContentPane(content);
        setMinimumSize(new Dimension(300, 200));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));
        setResizable(false);
        setLocationRelativeTo(frame);
    }

    private void initialiseUI() {
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.setBorder(new EmptyBorder(15, 15, 15, 15));

        InputForm inputForm = createInputForm();
        content.add(inputForm);
    }

    private InputForm createInputForm() {
        return new InputForm(view, "Continue") {
            @Override
            protected void createTextFields(JPanel panel) {
                password = new CustomInputField("Password", this::updateSubmitButtonState, false, true);
                password.addToPanel(panel);
            }

            @Override
            protected void onSubmit() {
                try {
                    String errorMessage = onConfirm(password.getjPasswordField().getPassword());
                    this.updateErrorMessage(errorMessage);
                    if(errorMessage == null) dispose();
                } catch (SQLException ex) {
                    this.updateErrorMessage("Cannot connect to database.");
                } catch (Exception ex) {
                    this.updateErrorMessage(ex.getMessage());
                }
            }

            @Override
            protected void onCancel() {
                dispose();
            }

            @Override
            protected boolean submitEnabled() {
                return password.isValid();
            }
        };
    }

    public abstract String onConfirm(char[] password) throws Exception;
}
