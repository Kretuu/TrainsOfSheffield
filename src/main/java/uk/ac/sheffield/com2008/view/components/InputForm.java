package uk.ac.sheffield.com2008.view.components;

import uk.ac.sheffield.com2008.config.Colors;
import uk.ac.sheffield.com2008.util.listeners.AuthorisationActionListener;
import uk.ac.sheffield.com2008.view.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public abstract class InputForm extends JPanel {
    private final JButton submitButton;
    private final JPanel content;
    private final JLabel errorMessage;
    private final JButton cancelButton;
    private final View view;

    /**
     * Constructs Input Form which is reusable over whole app
     * @param view need to pass current view to have properly working role-based authorisation system
     * @param submitButtonLabel label for Submit button
     * @param cancelButtonLabel (optional) label for Cancel button. If not provided, button will have label "Cancel"
     *                          by default.
     */
    public InputForm(View view, String submitButtonLabel, String cancelButtonLabel) {
        this.content = new JPanel();
        this.submitButton = new JButton(submitButtonLabel);
        this.errorMessage = new JLabel(" ");
        this.view = view;
        this.cancelButton = new JButton(cancelButtonLabel == null ? "Cancel" : cancelButtonLabel);

        initialiseUI();
        add(content);
    }

    public InputForm(View view, String submitButtonLabel) {
        this(view, submitButtonLabel, null);
    }

    private void initialiseUI() {
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel errorMessageHolder = new JPanel();
        errorMessage.setForeground(Colors.TEXT_FIELD_ERROR);
        errorMessageHolder.add(errorMessage);
        content.add(errorMessageHolder);

        submitButton.setEnabled(false);
        submitButton.addActionListener(new AuthorisationActionListener(view) {
            @Override
            public void action(ActionEvent e) {
                onSubmit();
            }
        });

        cancelButton.addActionListener(new AuthorisationActionListener(view) {
            @Override
            public void action(ActionEvent e) {
                onCancel();
            }
        });

        createTextFields(content);

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2,10,0));
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(submitButton);
        content.add(buttonsPanel);
    }

    /**
     * Updates error message which is displayed in top of the form
     * @param message error message
     */
    public void updateErrorMessage(String message) {
        if(message == null) {
            errorMessage.setText(" ");
            return;
        }
        errorMessage.setText("Error: " + message);
    }

    protected void updateSubmitButtonState() {
        submitButton.setEnabled(submitEnabled());
    }

    /**
     * In this function you can add anything to the panel provided as an argument
     * @param panel JPanel where all components will be added
     */
    protected abstract void createTextFields(JPanel panel);

    /**
     * This function is called when submit button is clicked
     */
    protected abstract void onSubmit();

    /**
     * This function is called when cancel button is clicked
     */
    protected abstract void onCancel();

    /**
     * This button is handy when you want to enable submit button under certain conditions.
     * For example when all fields are properly filled. This function is called when any of the fields content changes
     * so do not put functions which may cause performance issues or ones which perform db calls.
     * @return true if submit button should be enabled, false otherwise
     */
    protected abstract boolean submitEnabled();
}
