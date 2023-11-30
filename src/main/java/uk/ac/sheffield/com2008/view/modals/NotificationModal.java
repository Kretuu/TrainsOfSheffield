package uk.ac.sheffield.com2008.view.modals;

import uk.ac.sheffield.com2008.util.listeners.AuthorisationActionListener;
import uk.ac.sheffield.com2008.view.View;
import uk.ac.sheffield.com2008.view.components.Button;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public abstract class NotificationModal extends JDialog {

    public NotificationModal(JFrame frame, View view, String modalName, String notificationMessage) {
        super(frame, modalName, true);
        JPanel content = new JPanel(new BorderLayout());

        content.setBorder(new EmptyBorder(15, 15, 15, 15));
        JPanel labelPanel = new JPanel(new BorderLayout());
        JLabel notificationLabel = new JLabel("<html>" + notificationMessage + "</html>");
        notificationLabel.setFont(new Font(null, Font.PLAIN, 14));
        labelPanel.add(notificationLabel);

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2,10,0));
        JButton submitButton = new Button("Confirm");
        submitButton.addActionListener(new AuthorisationActionListener(view) {
            @Override
            public void action(ActionEvent e) {
                dispose();
                onSubmit();
            }
        });

        JButton cancelButton = new Button("Cancel");
        cancelButton.addActionListener(e -> dispose());
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(submitButton);

        content.add(labelPanel, BorderLayout.CENTER);
        content.add(buttonsPanel, BorderLayout.SOUTH);

        setContentPane(content);
        setMinimumSize(new Dimension(300, 200));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));
        setResizable(false);
        setLocationRelativeTo(frame);
    }

    public abstract void onSubmit();
}
