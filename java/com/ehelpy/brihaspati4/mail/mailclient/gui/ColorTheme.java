package com.ehelpy.brihaspati4.mail.mailclient.gui;

public enum ColorTheme {
    LIGHT,
    DEFAULT,
    DARK,
    PURPLE,
    RED,
    BLUE;

    public static String getCssPath(ColorTheme colorTheme){
        switch (colorTheme) {
            case LIGHT:
                return "css/themeLight.css";
            case DEFAULT:
                return "css/themeDefault.css";
            case DARK:
                return "css/themeDark.css";
            case PURPLE:
                return "css/themePurple.css";
            case RED:
                return "css/themeRed.css";
            case BLUE:
                return "css/themeBlue.css";
            default:
                return null;
        }
    }

}
