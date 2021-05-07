package de.idrinth.waraddonclient.gui.themes;

import java.awt.Color;
import javax.swing.UIDefaults;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class IdrinthLookAndFeel extends NimbusLookAndFeel {
    private UIDefaults uiDefaults;
    @Override public String getName() {
        return "Idrinth";
    }
    @Override public String getID() {
        return "Idrinth";
    }
    @Override public String getDescription() {
        return "Idrinth Look and Feel";
    }
    @Override public UIDefaults getDefaults() {
        if (uiDefaults != null){
            return uiDefaults;
        }
        uiDefaults = super.getDefaults();
        addColor(uiDefaults, "white", 50, 50, 50, 255);
        addColor(uiDefaults, "black", 200, 200, 200, 255);
        addColor(uiDefaults, "grey", 100, 100, 100, 255);
        addColor(uiDefaults, "colored", 50, 0, 0, 255);
        overwriteSynth();
        overwriteNimbus();
        return uiDefaults;
    }
    private void overwriteSynth() {
    }
    private void overwriteNimbus() {
        //The global style definition
        addColor(uiDefaults, "nimbusBase", "grey");
        addColor(uiDefaults, "nimbusFocus", "colored");
        addColor(uiDefaults, "nimbusSelectionBackground", "colored");
        addColor(uiDefaults, "nimbusDisabledText", "grey");
        addColor(uiDefaults, "info", "white");
        addColor(uiDefaults, "nimbusOrange", "colored");
        addColor(uiDefaults, "nimbusGreen", "colored");
        addColor(uiDefaults, "nimbusRed", "colored");
        addColor(uiDefaults, "nimbusAlertYellow", "colored");
        addColor(uiDefaults, "nimbusLightBackground", "white");
        addColor(uiDefaults, "nimbusSelectedText", "white");
        addColor(uiDefaults, "nimbusInfoBlue", "colored");
        addColor(uiDefaults, "control", "white");
        addColor(uiDefaults, "text", "black");
        addColor(uiDefaults, "controlText", "text");
        addColor(uiDefaults, "textHighlight", "nimbusSelectionBackground");
        addColor(uiDefaults, "textHighlightText", "nimbusSelectedText");
        addColor(uiDefaults, "textInactiveText", "nimbusDisabledText");
        addColor(uiDefaults, "menuText", "text");
        addColor(uiDefaults, "menu", "nimbusBase");
        addColor(uiDefaults, "scrollbar", "nimbusBlueGrey", 0.006944418f, 0.07296763f, -0.09019607f, 0);
        addColor(uiDefaults, "nimbusBlueGrey", "colored", -0.1f, -0.1f, -0.1f, 0f);
        addColor(uiDefaults, "nimbusBorder", "nimbusBlueGrey", 0.0f, 0.017358616f, 0.11372548f, 0);
        addColor(uiDefaults, "infoText", "text");
        addColor(uiDefaults, "textForeground", "text");
        addColor(uiDefaults, "textBackground", "nimbusSelectionBackground");
        addColor(uiDefaults, "background", "control");
        addColor(uiDefaults, "nimbusSelection", "nimbusBase", 0.010750473f, 0.04875779f, 0.007843137f, 0);
        addColor(uiDefaults, "controlHighlight", "nimbusBlueGrey", 0.0f, 0.07333623f, -0.20392156f, 0);
        addColor(uiDefaults, "controlLHighlight", "nimbusBlueGrey", 0.0f, 0.098526314f, -0.2352941f, 0);
        addColor(uiDefaults, "controlShadow", "nimbusBlueGrey", 0.0027777553f, 0.0212406f, -0.13333333f, 0);
        addColor(uiDefaults, "controlDkShadow", "nimbusBlueGrey", 0.0027777553f, 0.0018306673f, 0.02352941f, 0);
        addColor(uiDefaults, "desktop", "white", 0.009207249f, 0.13984653f, 0.07450983f, 0);
        addColor(uiDefaults, "activeCaption", "nimbusBlueGrey", 0.0f, 0.049920253f, -0.031372547f, 0);
        addColor(uiDefaults, "inactiveCaption", "nimbusBlueGrey", 0.00505054f, 0.055526316f, -0.039215684f, 0);

        //Initialize Button
        addColor(uiDefaults, "Button[Default+Pressed].textForeground", "nimbusSelectedText");
        addColor(uiDefaults, "Button[Disabled].textForeground", "nimbusDisabledText");

        //Initialize ToggleButton
        addColor(uiDefaults, "ToggleButton[Disabled].textForeground", "nimbusDisabledText");
        addColor(uiDefaults, "ToggleButton[Disabled+Selected].textForeground", "nimbusDisabledText");

        //Initialize RadioButton
        addColor(uiDefaults, "RadioButton[Disabled].textForeground", "nimbusDisabledText");

        //Initialize CheckBox
        addColor(uiDefaults, "CheckBox[Disabled].textForeground", "nimbusDisabledText");

        //Initialize ColorChooser
        addColor(uiDefaults, "ColorChooser.swatchesDefaultRecentColor", "white");

        //Initialize ComboBox
        addColor(uiDefaults, "ComboBox:\"ComboBox.textField\"[Disabled].textForeground", "nimbusDisabledText");
        addColor(uiDefaults, "ComboBox:\"ComboBox.textField\"[Selected].textForeground", "nimbusSelectedText");
        addColor(uiDefaults, "ComboBox:\"ComboBox.listRenderer\".background", "nimbusLightBackground");
        addColor(uiDefaults, "ComboBox:\"ComboBox.listRenderer\"[Disabled].textForeground", "nimbusDisabledText");
        addColor(uiDefaults, "ComboBox:\"ComboBox.listRenderer\"[Selected].textForeground", "nimbusSelectedText");
        addColor(uiDefaults, "ComboBox:\"ComboBox.listRenderer\"[Selected].background", "nimbusSelectionBackground");
        addColor(uiDefaults, "ComboBox:\"ComboBox.renderer\"[Disabled].textForeground", "nimbusDisabledText");
        addColor(uiDefaults, "ComboBox:\"ComboBox.renderer\"[Selected].textForeground", "nimbusSelectedText");
        addColor(uiDefaults, "ComboBox:\"ComboBox.renderer\"[Selected].background", "nimbusSelectionBackground");

        //Initialize InternalFrame
        addColor(uiDefaults, "InternalFrame:InternalFrameTitlePane[Enabled].textForeground", "nimbusDisabledText");

        //Initialize Label
        addColor(uiDefaults, "Label[Disabled].textForeground", "nimbusDisabledText");

        //Initialize List
        addColor(uiDefaults, "List.background", "nimbusLightBackground");
        addColor(uiDefaults, "List.dropLineColor", "nimbusFocus");
        addColor(uiDefaults, "List[Selected].textForeground", "nimbusLightBackground");
        addColor(uiDefaults, "List[Selected].textBackground", "nimbusSelectionBackground");
        addColor(uiDefaults, "List[Disabled+Selected].textBackground", "nimbusSelectionBackground");
        addColor(uiDefaults, "List[Disabled].textForeground", "nimbusDisabledText");
        addColor(uiDefaults, "List:\"List.cellRenderer\"[Selected].textForeground", "nimbusLightBackground");
        addColor(uiDefaults, "List:\"List.cellRenderer\"[Selected].background", "nimbusSelectionBackground");
        addColor(uiDefaults, "List:\"List.cellRenderer\"[Disabled+Selected].background", "nimbusSelectionBackground");
        addColor(uiDefaults, "List:\"List.cellRenderer\"[Disabled].textForeground", "nimbusDisabledText");

        //Initialize MenuBar
        addColor(uiDefaults, "MenuBar:Menu.background", "colored");
        addColor(uiDefaults, "MenuBar:Menu[Disabled].textForeground", "nimbusDisabledText");
        addColor(uiDefaults, "MenuBar:Menu[Enabled].textForeground", "text");
        addColor(uiDefaults, "MenuBar:Menu[Selected].textForeground", "white");

        //Initialize MenuItem
        addColor(uiDefaults, "MenuItem.background", "colored");
        addColor(uiDefaults, "MenuItem[Disabled].textForeground", "nimbusDisabledText");
        addColor(uiDefaults, "MenuItem[Enabled].textForeground", "white");
        addColor(uiDefaults, "MenuItem[MouseOver].textForeground", "white");
        addColor(uiDefaults, "MenuItem:MenuItemAccelerator[Disabled].textForeground", "nimbusDisabledText");
        addColor(uiDefaults, "MenuItem:MenuItemAccelerator[MouseOver].textForeground", "white");

        //Initialize RadioButtonMenuItem
        addColor(uiDefaults, "RadioButtonMenuItem.background", "colored");
        addColor(uiDefaults, "RadioButtonMenuItem[Disabled].textForeground", "nimbusDisabledText");
        addColor(uiDefaults, "RadioButtonMenuItem[Enabled].textForeground", "white");
        addColor(uiDefaults, "RadioButtonMenuItem[MouseOver].textForeground", "white");
        addColor(uiDefaults, "RadioButtonMenuItem[MouseOver+Selected].textForeground", "white");
        addColor(uiDefaults, "RadioButtonMenuItem:MenuItemAccelerator[MouseOver].textForeground", "white");

        //Initialize CheckBoxMenuItem
        addColor(uiDefaults, "CheckBoxMenuItem.background", "colored");
        addColor(uiDefaults, "CheckBoxMenuItem[Enabled].textForeground", "white");
        addColor(uiDefaults, "CheckBoxMenuItem[MouseOver].textForeground", "white");
        addColor(uiDefaults, "CheckBoxMenuItem[MouseOver+Selected].textForeground", "white");
        addColor(uiDefaults, "CheckBoxMenuItem:MenuItemAccelerator[MouseOver].textForeground", "white");

        //Initialize Menu
        addColor(uiDefaults, "Menu.background", "colored");
        addColor(uiDefaults, "Menu[Disabled].textForeground", "nimbusDisabledText");
        addColor(uiDefaults, "Menu[Enabled].textForeground", "white");
        addColor(uiDefaults, "Menu[Enabled+Selected].textForeground", "white");
        addColor(uiDefaults, "Menu:MenuItemAccelerator[MouseOver].textForeground", "white");

        //Initialize ProgressBar
        addColor(uiDefaults, "ProgressBar[Disabled].textForeground", "nimbusDisabledText");

        //Initialize Slider
        addColor(uiDefaults, "Slider.tickColor", "grey");

        //Initialize Spinner
        addColor(uiDefaults, "Spinner:Panel:\"Spinner.formattedTextField\"[Disabled].textForeground", "nimbusDisabledText");
        addColor(uiDefaults, "Spinner:Panel:\"Spinner.formattedTextField\"[Selected].textForeground", "nimbusSelectedText");
        addColor(uiDefaults, "Spinner:Panel:\"Spinner.formattedTextField\"[Focused+Selected].textForeground", "nimbusSelectedText");

        //Initialize TabbedPane
        addColor(uiDefaults, "TabbedPane.shadow", "nimbusDisabledText");
        addColor(uiDefaults, "TabbedPane.darkShadow", "text");
        addColor(uiDefaults, "TabbedPane.highlight", "nimbusLightBackground");
        addColor(uiDefaults, "TabbedPane:TabbedPaneTab[Disabled].textForeground", "nimbusDisabledText");
        addColor(uiDefaults, "TabbedPane:TabbedPaneTab[Pressed+Selected].textForeground", "white");
        addColor(uiDefaults, "TabbedPane:TabbedPaneTab[Focused+Pressed+Selected].textForeground", "white");

        //Initialize Table
        addColor(uiDefaults, "Table.textForeground", "text");
        addColor(uiDefaults, "Table.background", "nimbusLightBackground");
        addColor(uiDefaults, "Table.alternateRowColor", "nimbusLightBackground", 0.0f, 0.0f, 0.05098039f, 0);
        addColor(uiDefaults, "Table.dropLineColor", "nimbusFocus");
        addColor(uiDefaults, "Table.dropLineShortColor", "nimbusOrange");
        addColor(uiDefaults, "Table[Enabled+Selected].textForeground", "nimbusLightBackground");
        addColor(uiDefaults, "Table[Enabled+Selected].textBackground", "nimbusSelectionBackground");
        addColor(uiDefaults, "Table[Disabled+Selected].textBackground", "nimbusSelectionBackground");
        addColor(uiDefaults, "Table:\"Table.cellRenderer\".background", "nimbusLightBackground");

        //Initialize \"Table.editor\"
        addColor(uiDefaults, "\"Table.editor\".background", "nimbusLightBackground");
        addColor(uiDefaults, "\"Table.editor\"[Disabled].textForeground", "nimbusDisabledText");
        addColor(uiDefaults, "\"Table.editor\"[Selected].textForeground", "nimbusSelectedText");

        //Initialize \"Tree.cellEditor\"
        addColor(uiDefaults, "\"Tree.cellEditor\".background", "nimbusLightBackground");
        addColor(uiDefaults, "\"Tree.cellEditor\"[Disabled].textForeground", "nimbusDisabledText");
        addColor(uiDefaults, "\"Tree.cellEditor\"[Selected].textForeground", "nimbusSelectedText");

        //Initialize TextField
        addColor(uiDefaults, "TextField.background", "nimbusLightBackground");
        addColor(uiDefaults, "TextField[Disabled].textForeground", "nimbusDisabledText");
        addColor(uiDefaults, "TextField[Selected].textForeground", "nimbusSelectedText");
        addColor(uiDefaults, "TextField[Disabled].textForeground", "nimbusDisabledText");

        //Initialize FormattedTextField
        addColor(uiDefaults, "FormattedTextField[Disabled].textForeground", "nimbusDisabledText");
        addColor(uiDefaults, "FormattedTextField[Selected].textForeground", "nimbusSelectedText");
        addColor(uiDefaults, "FormattedTextField[Disabled].textForeground", "nimbusDisabledText");

        //Initialize PasswordField
        addColor(uiDefaults, "PasswordField[Disabled].textForeground", "nimbusDisabledText");
        addColor(uiDefaults, "PasswordField[Selected].textForeground", "nimbusSelectedText");
        addColor(uiDefaults, "PasswordField[Disabled].textForeground", "nimbusDisabledText");

        //Initialize TextArea
        addColor(uiDefaults, "TextArea[Disabled].textForeground", "nimbusDisabledText");
        addColor(uiDefaults, "TextArea[Disabled+NotInScrollPane].textForeground", "nimbusDisabledText");
        addColor(uiDefaults, "TextArea[Selected].textForeground", "nimbusSelectedText");
        addColor(uiDefaults, "TextArea[Disabled+NotInScrollPane].textForeground", "nimbusDisabledText");

        //Initialize TextPane
        addColor(uiDefaults, "TextPane[Disabled].textForeground", "nimbusDisabledText");
        addColor(uiDefaults, "TextPane[Selected].textForeground", "nimbusSelectedText");

        //Initialize EditorPane
        addColor(uiDefaults, "EditorPane[Disabled].textForeground", "nimbusDisabledText");
        addColor(uiDefaults, "EditorPane[Selected].textForeground", "nimbusSelectedText");

        //Initialize ToolBar
        addColor(uiDefaults, "ToolBar:ToggleButton[Disabled+Selected].textForeground", "nimbusDisabledText");

        //Initialize ToolBarSeparator
        addColor(uiDefaults, "ToolBarSeparator.textForeground", "nimbusBorder");

        //Initialize Tree
        addColor(uiDefaults, "Tree.textForeground", "text");
        addColor(uiDefaults, "Tree.textBackground", "nimbusLightBackground");
        addColor(uiDefaults, "Tree.background", "nimbusLightBackground");
        addColor(uiDefaults, "Tree.selectionForeground", "nimbusSelectedText");
        addColor(uiDefaults, "Tree.selectionBackground", "nimbusSelectionBackground");
        addColor(uiDefaults, "Tree.dropLineColor", "nimbusFocus");
        addColor(uiDefaults, "Tree:TreeCell[Enabled].background", "nimbusLightBackground");
        addColor(uiDefaults, "Tree:TreeCell[Enabled+Focused].background", "nimbusLightBackground");
        addColor(uiDefaults, "Tree:TreeCell[Focused+Selected].textForeground", "white");
        addColor(uiDefaults, "Tree:\"Tree.cellRenderer\"[Disabled].textForeground", "nimbusDisabledText");

        //Initialize RootPane
        addColor(uiDefaults, "RootPane.background", "control");
    }
    private void addColor(UIDefaults uiDefaults, String uin, int red, int green, int blue, int alpha) {
        Color color = new ColorUIResource(new Color(red, green, blue, alpha));
        uiDefaults.put(uin, color);
    }
    private void addColor(UIDefaults uiDefaults, String uin, String parentUin, float red, float green, float blue, float alpha) {
        Color parentColor = (Color) uiDefaults.get(parentUin);
        addColor(
            uiDefaults,
            uin,
            (int) (parentColor.getRed()* (1 +red)),
            (int) (parentColor.getGreen()* (1 +green)),
            (int) (parentColor.getBlue()* (1 +blue)),
            (int) (parentColor.getAlpha()* (1 +alpha))
       );
    }
    private void addColor(UIDefaults uiDefaults, String uin, String parentUin) {
        Color parentColor = (Color) uiDefaults.get(parentUin);
        addColor(
            uiDefaults,
            uin,
            parentColor.getRed(),
            parentColor.getGreen(),
            parentColor.getBlue(),
            parentColor.getAlpha()
       );
    }
}
