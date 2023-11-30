package uk.ac.sheffield.com2008.config;

import java.awt.*;

public class Colors {
    //Pallette
    private static final Color greenLight = Color.decode("#5BBA6F");
    private static final Color greenDark = Color.decode("#4FA161");
    private static final Color white = Color.decode("#FBFBFB");
    private static final Color charcoal = Color.decode("#464655");
    private static final Color black = Color.decode("#353535");


    public static Color TEXT_FIELD_FOCUSED = Color.BLACK;
    public static Color TEXT_FIELD_UNFOCUSED = Color.DARK_GRAY;
    public static Color TEXT_FIELD_ERROR = Color.RED;


    public static Color TABLE_HEADER = greenDark;
    public static Color TABLE_CONTENT = white;


    public static Color BUTTON_BORDER = charcoal;
    public static Color BUTTON_CONTENT = greenDark;
    public static Color BUTTON_CONTENT_FOCUS = greenLight;
    public static Color BUTTON_CONTENT_CLICK = greenLight;
    public static Color BUTTON_TEXT = white;
}
