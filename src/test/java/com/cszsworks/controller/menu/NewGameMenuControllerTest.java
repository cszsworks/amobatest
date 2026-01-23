package com.cszsworks.controller.menu;

import com.cszsworks.exception.InvalidInputException;
import com.cszsworks.model.GameConfig;
import com.cszsworks.view.LanternaNewGameRenderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NewGameMenuControllerTest {

    private LanternaNewGameRenderer renderer;
    private NewGameMenuController controller;

    @BeforeEach
    public void setup() {
        renderer = mock(LanternaNewGameRenderer.class);
        controller = new NewGameMenuController(renderer);
    }

    @Test
    public void testStartNewMenuCreatesConfig() {
        when(renderer.getSizeText()).thenReturn("5");
        when(renderer.getWinLengthText()).thenReturn("3");

        try {
            controller.startNewMenu("Bob");
        } catch (InvalidInputException e) {
            throw new RuntimeException(e);
        }
        GameConfig config = controller.getGameConfig();

        assertNotNull(config);
        assertEquals("Bob", config.getPlayerName());
        assertEquals(5, config.getRows());
        assertEquals(5, config.getCols());
        assertEquals(3, config.getWinLength());
    }

    @Test
    public void testStartNewMenuExtremeValues() {
        when(renderer.getSizeText()).thenReturn("100");  // túl nagy
        when(renderer.getWinLengthText()).thenReturn("1"); // túl kicsi

        try {
            controller.startNewMenu("Eve");
        } catch (InvalidInputException e) {
            throw new RuntimeException(e);
        }
        GameConfig config = controller.getGameConfig();

        assertEquals(25, config.getRows());
        assertEquals(25, config.getCols());
        assertEquals(5, config.getWinLength());
    }
}
