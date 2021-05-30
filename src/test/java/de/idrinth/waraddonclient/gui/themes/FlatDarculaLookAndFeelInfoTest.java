package de.idrinth.waraddonclient.gui.themes;

import com.formdev.flatlaf.FlatDarculaLaf;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FlatDarculaLookAndFeelInfoTest {
    @Test
    public void testGetName() {
        Assertions.assertEquals("Flat Darcula", new FlatDarculaLookAndFeelInfo().getName());
    }

    @Test
    public void testGetClassName() {
        Assertions.assertEquals(FlatDarculaLaf.class.getName(), new FlatDarculaLookAndFeelInfo().getClassName());
    }
}
