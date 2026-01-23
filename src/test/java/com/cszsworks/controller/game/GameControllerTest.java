package com.cszsworks.controller.game;

import com.cszsworks.controller.menu.AppState;
import com.cszsworks.model.CellVO;
import com.cszsworks.model.GameConfig;
import com.cszsworks.model.Table;
import com.cszsworks.view.LanternaGameRenderer;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.cszsworks.persistence.HighscoreJson;
import com.cszsworks.util.WaitForKeyPress;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameControllerTest {

    private Table table;
    private GameConfig config;
    private LanternaGameRenderer renderer;
    private Screen screen;

    @BeforeEach
    public void setup() throws Exception {
        // 3x3 teszt tábla
        table = new Table(3, 3, 3);
        config = new GameConfig("TestPlayer", 3, 3, 3);
        config.setPlayerTurn(true);

        // Mock a renderer és a screen
        renderer = mock(LanternaGameRenderer.class);
        screen = mock(Screen.class);
        when(renderer.getScreen()).thenReturn(screen);

        // alapértelmezett: nincs input
        when(screen.readInput()).thenReturn(null);
    }

    @Test
    public void testConstructorInitializesBoard() {
        GameController controller = new GameController(table, config, renderer);
        // középső X legyen
        assertEquals(CellVO.Value.X, table.getCell(1, 1).value());
        assertFalse(config.isPlayerTurn());
    }

    @Test
    public void testMovePossible() {
        GameController controller = new GameController(table, config, renderer);

        assertTrue(controller.movePossible(0, 0));

        table.setCell(0, 0, CellVO.Value.X);
        assertFalse(controller.movePossible(0, 0));
    }

    @Test
    public void testAiMovePlacesOAndSwitchesTurn() {
        GameController controller = new GameController(table, config, renderer);
        config.setPlayerTurn(false); // AI lépése

        boolean moved = controller.aiMove();
        assertTrue(moved);

        boolean hasO = false;
        for (int i = 0; i < table.getRows(); i++) {
            for (int j = 0; j < table.getCols(); j++) {
                if (table.getCell(i, j).value() == CellVO.Value.O) {
                    hasO = true;
                    break;
                }
            }
        }
        assertTrue(hasO);
        assertTrue(config.isPlayerTurn());
    }

    @Test
    public void testGameLoopImmediateWinX() throws Exception {
        Table tinyTable = new Table(1, 1, 1);
        tinyTable.setCell(0, 0, CellVO.Value.X);
        GameController controller = new GameController(tinyTable, config, renderer);

        // Static void metódus mockolása helyesen
        try (MockedStatic<HighscoreJson> highscoreMock = mockStatic(HighscoreJson.class);
             MockedStatic<WaitForKeyPress> waitMock = mockStatic(WaitForKeyPress.class)) {

            highscoreMock.when(() -> HighscoreJson.saveHighscore(any())).thenAnswer(invocation -> null);
            waitMock.when(() -> WaitForKeyPress.waitForEnter(any())).thenAnswer(invocation -> null);

            AppState state = controller.gameLoop();

            assertEquals(AppState.MAIN_MENU, state);
            assertEquals(GameState.X_WINS, controller.state);
        }
    }

    @Test
    public void testGameLoopImmediateDraw() throws Exception {
        Table tinyTable = new Table(2, 2, 3);
        tinyTable.setCell(0, 0, CellVO.Value.X);
        tinyTable.setCell(0, 1, CellVO.Value.O);
        tinyTable.setCell(1, 0, CellVO.Value.O);
        tinyTable.setCell(1, 1, CellVO.Value.X);

        GameController controller = new GameController(tinyTable, config, renderer);

        try (MockedStatic<HighscoreJson> highscoreMock = mockStatic(HighscoreJson.class);
             MockedStatic<WaitForKeyPress> waitMock = mockStatic(WaitForKeyPress.class)) {

            highscoreMock.when(() -> HighscoreJson.saveHighscore(any())).thenAnswer(invocation -> null);
            waitMock.when(() -> WaitForKeyPress.waitForEnter(any())).thenAnswer(invocation -> null);

            AppState state = controller.gameLoop();

            assertEquals(AppState.MAIN_MENU, state);
            assertEquals(GameState.DRAW, controller.state);
        }
    }

    // --- Teljes ág lefedettség a handlePlayerInput metódushoz ---
    @Test
    public void testHandlePlayerInput_allBranches() throws Exception {
        GameController controller = new GameController(table, config, renderer);

        // Reflection a private mezőkhöz
        java.lang.reflect.Field cursorRowField = GameController.class.getDeclaredField("cursorRow");
        cursorRowField.setAccessible(true);
        java.lang.reflect.Field cursorColField = GameController.class.getDeclaredField("cursorCol");
        cursorColField.setAccessible(true);

        // Reflection a private metódushoz
        java.lang.reflect.Method handleMethod = GameController.class.getDeclaredMethod("handlePlayerInput");
        handleMethod.setAccessible(true);

        java.lang.reflect.Field messageField = GameController.class.getDeclaredField("message");
        messageField.setAccessible(true);

        // --- ArrowUp ---
        cursorRowField.set(controller, 1);
        cursorColField.set(controller, 1);
        KeyStroke upKey = mock(KeyStroke.class);
        when(upKey.getKeyType()).thenReturn(KeyType.ArrowUp);
        when(screen.readInput()).thenReturn(upKey);
        AppState result = (AppState) handleMethod.invoke(controller);
        assertEquals(0, cursorRowField.get(controller));
        assertEquals(AppState.IN_GAME, result);

        // --- ArrowDown ---
        cursorRowField.set(controller, 0);
        KeyStroke downKey = mock(KeyStroke.class);
        when(downKey.getKeyType()).thenReturn(KeyType.ArrowDown);
        when(screen.readInput()).thenReturn(downKey);
        result = (AppState) handleMethod.invoke(controller);
        assertEquals(1, cursorRowField.get(controller));

        // --- ArrowLeft ---
        cursorColField.set(controller, 1);
        KeyStroke leftKey = mock(KeyStroke.class);
        when(leftKey.getKeyType()).thenReturn(KeyType.ArrowLeft);
        when(screen.readInput()).thenReturn(leftKey);
        result = (AppState) handleMethod.invoke(controller);
        assertEquals(0, cursorColField.get(controller));

        // --- ArrowRight ---
        cursorColField.set(controller, 0);
        KeyStroke rightKey = mock(KeyStroke.class);
        when(rightKey.getKeyType()).thenReturn(KeyType.ArrowRight);
        when(screen.readInput()).thenReturn(rightKey);
        result = (AppState) handleMethod.invoke(controller);
        assertEquals(1, cursorColField.get(controller));

        // --- Character w/s/a/d ---
        // w
        cursorRowField.set(controller, 1);
        cursorColField.set(controller, 1);
        KeyStroke charKeyW = mock(KeyStroke.class);
        when(charKeyW.getKeyType()).thenReturn(KeyType.Character);
        when(charKeyW.getCharacter()).thenReturn('w');
        when(screen.readInput()).thenReturn(charKeyW);
        result = (AppState) handleMethod.invoke(controller);
        assertEquals(0, cursorRowField.get(controller));

        // s
        cursorRowField.set(controller, 0);
        KeyStroke charKeyS = mock(KeyStroke.class);
        when(charKeyS.getKeyType()).thenReturn(KeyType.Character);
        when(charKeyS.getCharacter()).thenReturn('s');
        when(screen.readInput()).thenReturn(charKeyS);
        result = (AppState) handleMethod.invoke(controller);
        assertEquals(1, cursorRowField.get(controller));

        // a
        cursorColField.set(controller, 1);
        KeyStroke charKeyA = mock(KeyStroke.class);
        when(charKeyA.getKeyType()).thenReturn(KeyType.Character);
        when(charKeyA.getCharacter()).thenReturn('a');
        when(screen.readInput()).thenReturn(charKeyA);
        result = (AppState) handleMethod.invoke(controller);
        assertEquals(0, cursorColField.get(controller));

        // d
        cursorColField.set(controller, 0);
        KeyStroke charKeyD = mock(KeyStroke.class);
        when(charKeyD.getKeyType()).thenReturn(KeyType.Character);
        when(charKeyD.getCharacter()).thenReturn('d');
        when(screen.readInput()).thenReturn(charKeyD);
        result = (AppState) handleMethod.invoke(controller);
        assertEquals(1, cursorColField.get(controller));

        // --- Enter üres cellára ---
        cursorRowField.set(controller, 1);
        cursorColField.set(controller, 1);
        table.setCell(0, 0, CellVO.Value.X); // connectivity biztosítás
        KeyStroke enterKey = mock(KeyStroke.class);
        when(enterKey.getKeyType()).thenReturn(KeyType.Enter);
        when(screen.readInput()).thenReturn(enterKey);
        result = (AppState) handleMethod.invoke(controller);
        assertEquals(CellVO.Value.X, table.getCell(1, 1).value());
        assertFalse(config.isPlayerTurn());

        // --- Enter foglalt cellára ---
        table.setCell(1, 1, CellVO.Value.X);
        cursorRowField.set(controller, 1);
        cursorColField.set(controller, 1);
        result = (AppState) handleMethod.invoke(controller);
        assertEquals("Cell is not available!", messageField.get(controller));

        // --- Escape / F5 ---
        KeyStroke escapeKey = mock(KeyStroke.class);
        when(escapeKey.getKeyType()).thenReturn(KeyType.Escape);
        when(screen.readInput()).thenReturn(escapeKey);
        result = (AppState) handleMethod.invoke(controller);
        assertEquals(AppState.MAIN_MENU, result);
    }
}
