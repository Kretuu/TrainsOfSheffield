package uk.ac.sheffield.com2008.util.listeners;

import uk.ac.sheffield.com2008.navigation.NavigationManager;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class NavigationFrameWindowListener implements WindowListener {
    private final NavigationManager navigationManager;

    public NavigationFrameWindowListener(NavigationManager navigationManager) {
        this.navigationManager = navigationManager;
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        navigationManager.getCurrentController().onNavigateLeave();
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
