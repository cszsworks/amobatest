package com.cszsworks.controller.menu;

import com.cszsworks.persistence.model.HighScoreDisplayEntry;
import com.cszsworks.view.LanternaHighScoreRenderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class HighScoreMenuControllerTest {

    private LanternaHighScoreRenderer renderer;
    private HighScoreMenuController controller;

    @BeforeEach
    public void setup() {
        renderer = mock(LanternaHighScoreRenderer.class);
        controller = new HighScoreMenuController(renderer);
    }

    @Test
    public void testShowHighScoresCallsRenderer() {
        //renderer meghivva
        HighScoreMenuController spyController = spy(controller);

        // igazolja hogy visszajön az adat
        doReturn(List.of(new HighScoreDisplayEntry("Alice", 100))).when(spyController).getTopHighScores();

        spyController.showHighScores();

        verify(renderer, times(1)).render(anyList());
    }
}
