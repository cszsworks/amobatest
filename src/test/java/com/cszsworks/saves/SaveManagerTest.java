package com.cszsworks.saves;

import com.cszsworks.model.GameConfig;
import com.cszsworks.model.Table;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class SaveManagerTest {

    @Test
    public void testCreateAndLoadSave() throws IOException {
        // létrehozunk egy ideiglenes fájlt
        File tempFile = File.createTempFile("test_save", ".dat");
        tempFile.deleteOnExit(); // automatikusan törlődik a teszt után

        // készítünk egy GameSaveData objektumot
        GameConfig config = new GameConfig("Player1", 3, 3, 3);
        Table table = new Table(3, 3, 3);
        GameSaveData saveData = new GameSaveData(config, table);

        // elmentés
        SaveManager.createSave(saveData, tempFile.getAbsolutePath());

        // betöltés
        GameSaveData loaded = SaveManager.loadSave(tempFile.getAbsolutePath());

        assertNotNull(loaded, "A betöltött mentés nem lehet null");
        assertEquals("Player1", loaded.getConfig().getPlayerName(), "A mentett névnek egyeznie kell");
        assertEquals(3, loaded.getTable().getRows(), "A tábla sorainak egyezniük kell");
        assertEquals(3, loaded.getTable().getCols(), "A tábla oszlopainak egyezniük kell");
    }

    @Test
    public void testLoadSaveFileNotFound() {
        String nonexistent = "nonexistent_file.dat";
        GameSaveData loaded = SaveManager.loadSave(nonexistent);
        assertNull(loaded, "Nem létező fájl esetén null-t kell visszaadnia");
    }
}
