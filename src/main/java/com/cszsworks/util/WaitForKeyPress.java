package com.cszsworks.util;

import com.cszsworks.view.LanternaGameRenderer;
import com.cszsworks.view.LanternaHighScoreRenderer;
import com.cszsworks.view.LanternaMenuRenderer;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

public class WaitForKeyPress {
    public static void waitForEnter(LanternaGameRenderer renderer) throws Exception {
        while (true) {
            KeyStroke key = renderer.getScreen().readInput();
            if (key == null) continue;
            if (key.getKeyType() == KeyType.Enter || key.getKeyType() == KeyType.Escape) return;
        }
    }


}
