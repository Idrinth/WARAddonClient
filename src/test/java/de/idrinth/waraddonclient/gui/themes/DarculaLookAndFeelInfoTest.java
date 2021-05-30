package de.idrinth.waraddonclient.gui.themes;

import com.bulenkov.darcula.DarculaLaf;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DarculaLookAndFeelInfoTest {
    @Test
    public void testGetName() {
        Assertions.assertEquals("Darcula", new DarculaLookAndFeelInfo().getName());
    }

    @Test
    public void testGetClassName() {
        Assertions.assertEquals(DarculaLaf.class.getName(), new DarculaLookAndFeelInfo().getClassName());
    }
}
