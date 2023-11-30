package uk.ac.sheffield.com2008.util.listeners;

import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class AuthorisationActionListener implements ActionListener {
    private final View view;

    public AuthorisationActionListener(View view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (NavigationManager.permissionsValid(view)) {
            action(e);
        }
    }

    public abstract void action(ActionEvent e);

}
