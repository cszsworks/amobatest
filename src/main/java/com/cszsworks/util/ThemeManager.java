package com.cszsworks.util;

import com.googlecode.lanterna.bundle.LanternaThemes;
import com.googlecode.lanterna.graphics.Theme;
import java.util.Collection;


public class ThemeManager {
    private static Theme currentTheme = LanternaThemes.getRegisteredTheme("default");

    //név alapján témát vált
    public static boolean setTheme(String name) {
        Theme theme = LanternaThemes.getRegisteredTheme(name.toLowerCase());
        if (theme != null) {
            currentTheme = theme;
            return true;
        } else {
            System.out.println("Theme not found: " + name);
            return false;
        }
    }

    //jelenlegi theme visszaadása
    public static Theme getTheme() {
        return currentTheme;
    }

    //választható lanterna themek listája
    public static Collection<String> getAvailableThemes() {
        return LanternaThemes.getRegisteredThemes();
    }
}
