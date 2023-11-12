package uk.ac.sheffield.com2008.controller;

import uk.ac.sheffield.com2008.view.FrameManager;

import javax.swing.*;
import java.awt.*;

public class ViewController {

    void setFrameContent(JPanel panel){
        FrameManager.setView(panel);
    }
}
