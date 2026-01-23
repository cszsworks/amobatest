package com.cszsworks.controller.menu;

import com.cszsworks.view.LanternaMenuRenderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MainMenuControllerTest {

    private LanternaMenuRenderer renderer;

    @BeforeEach
    public void setup() {
        renderer = mock(LanternaMenuRenderer.class);
    }

    @Test
    public void testMenuSelections() {
        // kiválasztott menü mock objektumok
        when(renderer.renderMenu(anyString(), any(), anyInt())).thenReturn(0);
        MainMenuController controller = new MainMenuController(0, renderer);
        assertEquals(AppState.NEW_GAME_SETUP, controller.startMainMenu());

        when(renderer.renderMenu(anyString(), any(), anyInt())).thenReturn(1);
        controller = new MainMenuController(1, renderer);
        assertEquals(AppState.LOAD_GAME, controller.startMainMenu());

        when(renderer.renderMenu(anyString(), any(), anyInt())).thenReturn(2);
        controller = new MainMenuController(2, renderer);
        assertEquals(AppState.HIGH_SCORE_SCREEN, controller.startMainMenu());

        when(renderer.renderMenu(anyString(), any(), anyInt())).thenReturn(3);
        controller = new MainMenuController(3, renderer);
        assertEquals(AppState.EXIT, controller.startMainMenu());

        when(renderer.renderMenu(anyString(), any(), anyInt())).thenReturn(99);
        controller = new MainMenuController(99, renderer);
        assertEquals(AppState.MAIN_MENU, controller.startMainMenu());
    }
}
