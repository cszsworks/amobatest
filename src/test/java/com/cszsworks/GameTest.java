package com.cszsworks;

import com.cszsworks.controller.game.GameController;
import com.cszsworks.controller.menu.*;
import com.cszsworks.model.GameConfig;
import com.cszsworks.saves.SaveManager;
import com.cszsworks.view.*;
import com.googlecode.lanterna.screen.Screen;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class GameTest {

    // --- Teszt: High Score menü elérése, majd kilépés ---
    @Test
    void run_hitsHighScoreScreen_andExits() throws Exception {
        Screen screen = mock(Screen.class);

        // egyszerű int tömb a lambda belső számláláshoz
        int[] callCount = {0};

        try (
                MockedConstruction<MainMenuController> menuMock =
                        mockConstruction(MainMenuController.class,
                                (mock, ctx) -> when(mock.startMainMenu())
                                        .thenAnswer(inv -> {
                                            if (callCount[0] == 0) {
                                                callCount[0]++;
                                                return AppState.HIGH_SCORE_SCREEN; // első hívás: HighScore
                                            } else {
                                                return AppState.EXIT; // második hívás: kilépés
                                            }
                                        })
                        );

                MockedConstruction<HighScoreMenuController> highScoreMock =
                        mockConstruction(HighScoreMenuController.class,
                                (mock, ctx) -> doNothing().when(mock).showHighScores()) // HighScore render mock
        ) {

            Game game = new Game(
                    "TestPlayer",
                    screen,
                    mock(LanternaGameRenderer.class),
                    mock(LanternaMenuRenderer.class),
                    mock(LanternaHighScoreRenderer.class),
                    mock(LanternaNewGameRenderer.class)
            );

            game.run();

            // ellenőrzés: HighScore képernyő megjelenítése megtörtént
            verify(highScoreMock.constructed().get(0)).showHighScores();
            // ellenőrzés: Screen lezárása megtörtént
            verify(screen).close();
        }
    }

    // --- Teszt: Load Game menü hívása, mentés nem található, majd kilépés ---
    @Test
    void run_hitsLoadGame_noSaveFound_andExits() throws Exception {
        Screen screen = mock(Screen.class);
        int[] callCount = {0};

        try (
                MockedConstruction<MainMenuController> menuMock =
                        mockConstruction(MainMenuController.class,
                                (mock, ctx) -> when(mock.startMainMenu())
                                        .thenAnswer(inv -> {
                                            if (callCount[0] == 0) {
                                                callCount[0]++;
                                                return AppState.LOAD_GAME; // első hívás: Load Game
                                            } else {
                                                return AppState.EXIT; // második hívás: kilépés
                                            }
                                        })
                        );

                MockedStatic<SaveManager> saveMock =
                        mockStatic(SaveManager.class)
        ) {

            // mentés visszatérése null, nincs mentés
            saveMock.when(() -> SaveManager.loadSave(anyString())).thenReturn(null);

            Game game = new Game(
                    "TestPlayer",
                    screen,
                    mock(LanternaGameRenderer.class),
                    mock(LanternaMenuRenderer.class),
                    mock(LanternaHighScoreRenderer.class),
                    mock(LanternaNewGameRenderer.class)
            );

            game.run();

            // ellenőrzés: SaveManager meghívva a helyes fájlnevekkel
            saveMock.verify(() -> SaveManager.loadSave("TestPlayer_save.dat"));
            verify(screen).close();
        }
    }

    // --- Teszt: New Game menü hívása, új játék elindítása, majd kilépés ---
    @Test
    void run_hitsNewGameSetup_andExits() throws Exception {
        Screen screen = mock(Screen.class);
        int[] callCount = {0}; // a lambdán belül, mivel a java lényegében 1 class-t képez a lambdához

        try (
                MockedConstruction<MainMenuController> menuMock =
                        mockConstruction(MainMenuController.class,
                                (mock, ctx) -> when(mock.startMainMenu())
                                        .thenAnswer(inv -> {
                                            if (callCount[0] == 0) {
                                                callCount[0]++;
                                                return AppState.NEW_GAME_SETUP; // első hívás: New Game
                                            } else {
                                                return AppState.EXIT; // második hívás: kilépés
                                            }
                                        })
                        );

                MockedConstruction<NewGameMenuController> newGameMock =
                        mockConstruction(NewGameMenuController.class,
                                (mock, ctx) -> {
                                    doNothing().when(mock).startNewMenu(anyString()); // új játék elindítás mock
                                    when(mock.getGameConfig())
                                            .thenReturn(new GameConfig("Test", 3, 3, 3)); // visszaad egy teszt configot
                                });

                MockedConstruction<GameController> gameCtrlMock =
                        mockConstruction(GameController.class,
                                (mock, ctx) -> when(mock.gameLoop()).thenReturn(AppState.EXIT)) // játék ciklus mock
        ) {

            Game game = new Game(
                    "TestPlayer",
                    screen,
                    mock(LanternaGameRenderer.class),
                    mock(LanternaMenuRenderer.class),
                    mock(LanternaHighScoreRenderer.class),
                    mock(LanternaNewGameRenderer.class)
            );

            game.run();

            verify(gameCtrlMock.constructed().get(0)).gameLoop();
            verify(screen).close();
        }
    }

}
