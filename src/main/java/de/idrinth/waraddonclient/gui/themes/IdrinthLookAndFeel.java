package de.idrinth.waraddonclient.gui.themes;

import java.awt.Color;
import javax.swing.UIDefaults;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class IdrinthLookAndFeel extends NimbusLookAndFeel {

    private UIDefaults uiDefaults;

    @Override
    public String getName() {
        return "Idrinth";
    }

    @Override
    public String getID() {
        return "Idrinth";
    }

    @Override
    public String getDescription() {
        return "Idrinth's Look and Feel";
    }

    @Override
    public UIDefaults getDefaults() {
        if (uiDefaults != null) {
            return uiDefaults;
        }
        uiDefaults = super.getDefaults();
        addColor("dark", 50, 50, 50, 255);
        addColor("light", 200, 200, 200, 255);
        addColor("grey", 100, 100, 100, 255);
        addColor("colored", 50, 0, 0, 255);
        overwriteSynth();
        overwriteNimbus();
        return uiDefaults;
    }

    private void overwriteSynth() {
    }

    private void overwriteNimbus() {
        //The global style definition
        addColor("nimbusBase", "grey");
        addColor("nimbusFocus", "colored");
        addColor("nimbusSelectionBackground", "colored");
        addColor("nimbusDisabledText", "grey");
        addColor("info", "dark");
        addColor("nimbusOrange", "colored");
        addColor("nimbusGreen", "colored");
        addColor("nimbusRed", "colored");
        addColor("nimbusAlertYellow", "colored");
        addColor("nimbusLightBackground", "dark");
        addColor("nimbusSelectedText", "dark");
        addColor("nimbusInfoBlue", "colored");
        addColor("control", "dark");
        addColor("text", "light");
        addColor("controlText", "text");
        addColor("textHighlight", "colored");
        addColor("textHighlightText", "dark");
        addColor("textInactiveText", "grey");
        addColor("menuText", "light");
        addColor("menu", "grey");
        addColor("scrollbar", "nimbusBlueGrey", 0.006944418f, 0.07296763f, -0.09019607f, 0);
        addColor("nimbusBlueGrey", "colored", -0.1f, -0.1f, -0.1f, 0f);
        addColor("nimbusBorder", "nimbusBlueGrey", 0.0f, 0.017358616f, 0.11372548f, 0);
        addColor("infoText", "text");
        addColor("textForeground", "text");
        addColor("textBackground", "nimbusSelectionBackground");
        addColor("background", "control");
        addColor("nimbusSelection", "nimbusBase", 0.010750473f, 0.04875779f, 0.007843137f, 0);
        addColor("controlHighlight", "nimbusBlueGrey", 0.0f, 0.07333623f, -0.20392156f, 0);
        addColor("controlLHighlight", "nimbusBlueGrey", 0.0f, 0.098526314f, -0.2352941f, 0);
        addColor("controlShadow", "nimbusBlueGrey", 0.0027777553f, 0.0212406f, -0.13333333f, 0);
        addColor("controlDkShadow", "nimbusBlueGrey", 0.0027777553f, 0.0018306673f, 0.02352941f, 0);
        addColor("desktop", "dark", 0.009207249f, 0.13984653f, 0.07450983f, 0);
        addColor("activeCaption", "nimbusBlueGrey", 0.0f, 0.049920253f, -0.031372547f, 0);
        addColor("inactiveCaption", "nimbusBlueGrey", 0.00505054f, 0.055526316f, -0.039215684f, 0);

        //Initialize Button
        addColor("Button[Default+Pressed].textForeground", "nimbusSelectedText");
        addColor("Button[Disabled].textForeground", "nimbusDisabledText");

        //Initialize ToggleButton
        addColor("ToggleButton[Disabled].textForeground", "nimbusDisabledText");
        addColor("ToggleButton[Disabled+Selected].textForeground", "nimbusDisabledText");

        //Initialize RadioButton
        addColor("RadioButton[Disabled].textForeground", "nimbusDisabledText");

        //Initialize CheckBox
        addColor("CheckBox[Disabled].textForeground", "nimbusDisabledText");

        //Initialize ColorChooser
        addColor("ColorChooser.swatchesDefaultRecentColor", "dark");

        //Initialize ComboBox
        addColor("ComboBox:\"ComboBox.textField\"[Disabled].textForeground", "nimbusDisabledText");
        addColor("ComboBox:\"ComboBox.textField\"[Selected].textForeground", "nimbusSelectedText");
        addColor("ComboBox:\"ComboBox.listRenderer\".background", "nimbusLightBackground");
        addColor("ComboBox:\"ComboBox.listRenderer\"[Disabled].textForeground", "nimbusDisabledText");
        addColor("ComboBox:\"ComboBox.listRenderer\"[Selected].textForeground", "nimbusSelectedText");
        addColor("ComboBox:\"ComboBox.listRenderer\"[Selected].background", "nimbusSelectionBackground");
        addColor("ComboBox:\"ComboBox.renderer\"[Disabled].textForeground", "nimbusDisabledText");
        addColor("ComboBox:\"ComboBox.renderer\"[Selected].textForeground", "nimbusSelectedText");
        addColor("ComboBox:\"ComboBox.renderer\"[Selected].background", "nimbusSelectionBackground");

        //Initialize InternalFrame
        addColor("InternalFrame:InternalFrameTitlePane[Enabled].textForeground", "nimbusDisabledText");

        //Initialize Label
        addColor("Label[Disabled].textForeground", "nimbusDisabledText");

        //Initialize List
        addColor("List.background", "nimbusLightBackground");
        addColor("List.dropLineColor", "nimbusFocus");
        addColor("List[Selected].textForeground", "nimbusLightBackground");
        addColor("List[Selected].textBackground", "nimbusSelectionBackground");
        addColor("List[Disabled+Selected].textBackground", "nimbusSelectionBackground");
        addColor("List[Disabled].textForeground", "nimbusDisabledText");
        addColor("List:\"List.cellRenderer\"[Selected].textForeground", "nimbusLightBackground");
        addColor("List:\"List.cellRenderer\"[Selected].background", "nimbusSelectionBackground");
        addColor("List:\"List.cellRenderer\"[Disabled+Selected].background", "nimbusSelectionBackground");
        addColor("List:\"List.cellRenderer\"[Disabled].textForeground", "nimbusDisabledText");

        //Initialize MenuBar
        addColor("MenuBar:Menu.background", "colored");
        addColor("MenuBar:Menu[Disabled].textForeground", "nimbusDisabledText");
        addColor("MenuBar:Menu[Enabled].textForeground", "text");
        addColor("MenuBar:Menu[Selected].textForeground", "dark");

        //Initialize MenuItem
        addColor("MenuItem.background", "colored");
        addColor("MenuItem[Disabled].textForeground", "nimbusDisabledText");
        addColor("MenuItem[Enabled].textForeground", "dark");
        addColor("MenuItem[MouseOver].textForeground", "dark");
        addColor("MenuItem:MenuItemAccelerator[Disabled].textForeground", "nimbusDisabledText");
        addColor("MenuItem:MenuItemAccelerator[MouseOver].textForeground", "dark");

        //Initialize RadioButtonMenuItem
        addColor("RadioButtonMenuItem.background", "colored");
        addColor("RadioButtonMenuItem[Disabled].textForeground", "nimbusDisabledText");
        addColor("RadioButtonMenuItem[Enabled].textForeground", "dark");
        addColor("RadioButtonMenuItem[MouseOver].textForeground", "dark");
        addColor("RadioButtonMenuItem[MouseOver+Selected].textForeground", "dark");
        addColor("RadioButtonMenuItem:MenuItemAccelerator[MouseOver].textForeground", "dark");

        //Initialize CheckBoxMenuItem
        addColor("CheckBoxMenuItem.background", "colored");
        addColor("CheckBoxMenuItem[Enabled].textForeground", "dark");
        addColor("CheckBoxMenuItem[MouseOver].textForeground", "dark");
        addColor("CheckBoxMenuItem[MouseOver+Selected].textForeground", "dark");
        addColor("CheckBoxMenuItem:MenuItemAccelerator[MouseOver].textForeground", "dark");

        //Initialize Menu
        addColor("Menu.background", "colored");
        addColor("Menu[Disabled].textForeground", "nimbusDisabledText");
        addColor("Menu[Enabled].textForeground", "dark");
        addColor("Menu[Enabled+Selected].textForeground", "dark");
        addColor("Menu:MenuItemAccelerator[MouseOver].textForeground", "dark");

        //Initialize ProgressBar
        addColor("ProgressBar[Disabled].textForeground", "nimbusDisabledText");

        //Initialize Slider
        addColor("Slider.tickColor", "grey");

        //Initialize Spinner
        addColor("Spinner:Panel:\"Spinner.formattedTextField\"[Disabled].textForeground", "nimbusDisabledText");
        addColor("Spinner:Panel:\"Spinner.formattedTextField\"[Selected].textForeground", "nimbusSelectedText");
        addColor("Spinner:Panel:\"Spinner.formattedTextField\"[Focused+Selected].textForeground", "nimbusSelectedText");

        //Initialize TabbedPane
        addColor("TabbedPane.shadow", "nimbusDisabledText");
        addColor("TabbedPane.darkShadow", "text");
        addColor("TabbedPane.highlight", "nimbusLightBackground");
        addColor("TabbedPane:TabbedPaneTab[Disabled].textForeground", "nimbusDisabledText");
        addColor("TabbedPane:TabbedPaneTab[Pressed+Selected].textForeground", "dark");
        addColor("TabbedPane:TabbedPaneTab[Focused+Pressed+Selected].textForeground", "dark");

        //Initialize Table
        addColor("Table.textForeground", "text");
        addColor("Table.background", "nimbusLightBackground");
        addColor("Table.alternateRowColor", "nimbusLightBackground", 0.0f, 0.0f, 0.05098039f, 0);
        addColor("Table.dropLineColor", "nimbusFocus");
        addColor("Table.dropLineShortColor", "nimbusOrange");
        addColor("Table[Enabled+Selected].textForeground", "nimbusLightBackground");
        addColor("Table[Enabled+Selected].textBackground", "nimbusSelectionBackground");
        addColor("Table[Disabled+Selected].textBackground", "nimbusSelectionBackground");
        addColor("Table:\"Table.cellRenderer\".background", "nimbusLightBackground");

        //Initialize \"Table.editor\"
        addColor("\"Table.editor\".background", "nimbusLightBackground");
        addColor("\"Table.editor\"[Disabled].textForeground", "nimbusDisabledText");
        addColor("\"Table.editor\"[Selected].textForeground", "nimbusSelectedText");

        //Initialize \"Tree.cellEditor\"
        addColor("\"Tree.cellEditor\".background", "nimbusLightBackground");
        addColor("\"Tree.cellEditor\"[Disabled].textForeground", "nimbusDisabledText");
        addColor("\"Tree.cellEditor\"[Selected].textForeground", "nimbusSelectedText");

        //Initialize TextField
        addColor("TextField.background", "nimbusLightBackground");
        addColor("TextField[Disabled].textForeground", "nimbusDisabledText");
        addColor("TextField[Selected].textForeground", "nimbusSelectedText");
        addColor("TextField[Disabled].textForeground", "nimbusDisabledText");

        //Initialize FormattedTextField
        addColor("FormattedTextField[Disabled].textForeground", "nimbusDisabledText");
        addColor("FormattedTextField[Selected].textForeground", "nimbusSelectedText");
        addColor("FormattedTextField[Disabled].textForeground", "nimbusDisabledText");

        //Initialize PasswordField
        addColor("PasswordField[Disabled].textForeground", "nimbusDisabledText");
        addColor("PasswordField[Selected].textForeground", "nimbusSelectedText");
        addColor("PasswordField[Disabled].textForeground", "nimbusDisabledText");

        //Initialize TextArea
        addColor("TextArea[Disabled].textForeground", "nimbusDisabledText");
        addColor("TextArea[Disabled+NotInScrollPane].textForeground", "nimbusDisabledText");
        addColor("TextArea[Selected].textForeground", "nimbusSelectedText");
        addColor("TextArea[Disabled+NotInScrollPane].textForeground", "nimbusDisabledText");

        //Initialize TextPane
        addColor("TextPane[Disabled].textForeground", "nimbusDisabledText");
        addColor("TextPane[Selected].textForeground", "nimbusSelectedText");

        //Initialize EditorPane
        addColor("EditorPane[Disabled].textForeground", "nimbusDisabledText");
        addColor("EditorPane[Selected].textForeground", "nimbusSelectedText");

        //Initialize ToolBar
        addColor("ToolBar:ToggleButton[Disabled+Selected].textForeground", "nimbusDisabledText");

        //Initialize ToolBarSeparator
        addColor("ToolBarSeparator.textForeground", "nimbusBorder");

        //Initialize Tree
        addColor("Tree.textForeground", "text");
        addColor("Tree.textBackground", "nimbusLightBackground");
        addColor("Tree.background", "nimbusLightBackground");
        addColor("Tree.selectionForeground", "nimbusSelectedText");
        addColor("Tree.selectionBackground", "nimbusSelectionBackground");
        addColor("Tree.dropLineColor", "nimbusFocus");
        addColor("Tree:TreeCell[Enabled].background", "nimbusLightBackground");
        addColor("Tree:TreeCell[Enabled+Focused].background", "nimbusLightBackground");
        addColor("Tree:TreeCell[Focused+Selected].textForeground", "dark");
        addColor("Tree:\"Tree.cellRenderer\"[Disabled].textForeground", "nimbusDisabledText");

        //Initialize RootPane
        addColor("RootPane.background", "control");
    }

    private void addColor(String uin, int red, int green, int blue, int alpha) {
        Color color = new ColorUIResource(new Color(red, green, blue, alpha));
        uiDefaults.put(uin, color);
    }

    private void addColor(String uin, String parentUin, float red, float green, float blue, float alpha) {
        Color parentColor = (Color) uiDefaults.get(parentUin);
        addColor(
                uin,
                (int) (parentColor.getRed() * (1 + red)),
                (int) (parentColor.getGreen() * (1 + green)),
                (int) (parentColor.getBlue() * (1 + blue)),
                (int) (parentColor.getAlpha() * (1 + alpha))
        );
    }

    private void addColor(String uin, String parentUin) {
        Color parentColor = (Color) uiDefaults.get(parentUin);
        addColor(
                uin,
                parentColor.getRed(),
                parentColor.getGreen(),
                parentColor.getBlue(),
                parentColor.getAlpha()
        );
    }
}
