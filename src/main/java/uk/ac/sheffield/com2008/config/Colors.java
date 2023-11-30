package uk.ac.sheffield.com2008.config;

import java.awt.*;

public class Colors {
    //Pallette
    private static final Color greenLight = Color.decode("#5BBA6F");
    private static final Color greenDark = Color.decode("#4FA161");
    private static final Color greenLightest = Color.decode("#f0fdf4");

    private static final Color white = Color.decode("#FBFBFB");
    private static final Color charcoal = Color.decode("#464655");
    private static final Color black = Color.decode("#353535");
    private static final Color gray = Color.decode("#d4d4d8");


    public static Color TEXT_FIELD_FOCUSED = black;
    public static Color TEXT_FIELD_UNFOCUSED = charcoal;
    public static Color TEXT_FIELD_ERROR = Color.RED;

    public static Color BACKGROUND = greenLightest;
    public static Color WHITE_BACKGROUND = white;

    public static Color TABLE_HEADER = greenDark;
    public static Color TABLE_CONTENT = white;


    public static Color BUTTON_BORDER = charcoal;
    public static Color BUTTON_CONTENT = greenDark;
    public static Color BUTTON_CONTENT_FOCUS = greenLight;
    public static Color BUTTON_CONTENT_CLICK = greenLight;
    public static Color BUTTON_TEXT = white;
    public static Color BUTTON_DISABLED = gray;
}
