package de.idrinth.waraddonclient.gui.themes;

import java.awt.Color;
import javax.swing.UIDefaults;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class IdrinthLookAndFeel extends NimbusLookAndFeel {

    private UIDefaults uiDefaults;
    
    private static final String GREY = "grey";
    
    private static final String LIGHT = "light";
    
    private static final String DARK = "dark";
    
    private static final String COLORED = "colored";

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
        addColor(DARK, 25, 20, 20, 255);
        addColor(LIGHT, 200, 200, 200, 255);
        addColor(GREY, 100, 100, 100, 255);
        addColor(COLORED, 50, 0, 0, 255);
        overwriteNimbus();
        return uiDefaults;
    }

    private void overwriteNimbus() {
        //The global style definition
        addColor("nimbusBase", GREY);
        addColor("nimbusFocus", COLORED);
        addColor("nimbusSelectionBackground", COLORED);
        addColor("nimbusDisabledText", GREY);
        addColor("info", DARK);
        addColor("nimbusOrange", COLORED);
        addColor("nimbusGreen", COLORED);
        addColor("nimbusRed", COLORED);
        addColor("nimbusAlertYellow", COLORED);
        addColor("nimbusLightBackground", DARK);
        addColor("nimbusSelectedText", DARK);
        addColor("nimbusInfoBlue", COLORED);
        addColor("control", DARK);
        addColor("text", LIGHT);
        addColor("controlText", "text");
        addColor("textHighlight", COLORED);
        addColor("textHighlightText", DARK);
        addColor("textInactiveText", GREY);
        addColor("menuText", LIGHT);
        addColor("menu", GREY);
        addColor("nimbusBlueGrey", COLORED, -0.1f, -0.1f, -0.1f, 0f);
        addColor("scrollbar", COLORED);
        addColor("nimbusBorder", "nimbusBlueGrey", 0.0f, 0.017358616f, 0.11372548f, 0);
        addColor("infoText", LIGHT);
        addColor("textForeground", LIGHT);
        addColor("textBackground", COLORED);
        addColor("background", DARK);
        addColor("nimbusSelection", GREY, 0.010750473f, 0.04875779f, 0.007843137f, 0);
        addColor("controlHighlight", "nimbusBlueGrey", 0.0f, 0.07333623f, -0.20392156f, 0);
        addColor("controlLHighlight", "nimbusBlueGrey", 0.0f, 0.098526314f, -0.2352941f, 0);
        addColor("controlShadow", "nimbusBlueGrey", 0.0027777553f, 0.0212406f, -0.13333333f, 0);
        addColor("controlDkShadow", "nimbusBlueGrey", 0.0027777553f, 0.0018306673f, 0.02352941f, 0);
        addColor("desktop", DARK, 0.009207249f, 0.13984653f, 0.07450983f, 0);
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
        addColor("ColorChooser.swatchesDefaultRecentColor", DARK);

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
        addColor("MenuBar:Menu.background", COLORED);
        addColor("MenuBar:Menu[Disabled].textForeground", "nimbusDisabledText");
        addColor("MenuBar:Menu[Enabled].textForeground", "text");
        addColor("MenuBar:Menu[Selected].textForeground", DARK);

        //Initialize MenuItem
        addColor("MenuItem.background", COLORED);
        addColor("MenuItem[Disabled].textForeground", "nimbusDisabledText");
        addColor("MenuItem[Enabled].textForeground", DARK);
        addColor("MenuItem[MouseOver].textForeground", DARK);
        addColor("MenuItem:MenuItemAccelerator[Disabled].textForeground", "nimbusDisabledText");
        addColor("MenuItem:MenuItemAccelerator[MouseOver].textForeground", DARK);

        //Initialize RadioButtonMenuItem
        addColor("RadioButtonMenuItem.background", COLORED);
        addColor("RadioButtonMenuItem[Disabled].textForeground", "nimbusDisabledText");
        addColor("RadioButtonMenuItem[Enabled].textForeground", DARK);
        addColor("RadioButtonMenuItem[MouseOver].textForeground", DARK);
        addColor("RadioButtonMenuItem[MouseOver+Selected].textForeground", DARK);
        addColor("RadioButtonMenuItem:MenuItemAccelerator[MouseOver].textForeground", DARK);

        //Initialize CheckBoxMenuItem
        addColor("CheckBoxMenuItem.background", COLORED);
        addColor("CheckBoxMenuItem[Enabled].textForeground", DARK);
        addColor("CheckBoxMenuItem[MouseOver].textForeground", DARK);
        addColor("CheckBoxMenuItem[MouseOver+Selected].textForeground", DARK);
        addColor("CheckBoxMenuItem:MenuItemAccelerator[MouseOver].textForeground", DARK);

        //Initialize Menu
        addColor("Menu.background", COLORED);
        addColor("Menu[Disabled].textForeground", "nimbusDisabledText");
        addColor("Menu[Enabled].textForeground", DARK);
        addColor("Menu[Enabled+Selected].textForeground", DARK);
        addColor("Menu:MenuItemAccelerator[MouseOver].textForeground", DARK);

        //Initialize ProgressBar
        addColor("ProgressBar[Disabled].textForeground", "nimbusDisabledText");

        //Initialize Slider
        addColor("Slider.tickColor", GREY);

        //Initialize Spinner
        addColor("Spinner:Panel:\"Spinner.formattedTextField\"[Disabled].textForeground", "nimbusDisabledText");
        addColor("Spinner:Panel:\"Spinner.formattedTextField\"[Selected].textForeground", "nimbusSelectedText");
        addColor("Spinner:Panel:\"Spinner.formattedTextField\"[Focused+Selected].textForeground", "nimbusSelectedText");

        //Initialize TabbedPane
        addColor("TabbedPane.shadow", "nimbusDisabledText");
        addColor("TabbedPane.darkShadow", "text");
        addColor("TabbedPane.highlight", COLORED);
        addColor("TabbedPane:TabbedPaneTab[Disabled].textForeground", "nimbusDisabledText");
        addColor("TabbedPane:TabbedPaneTab[Pressed+Selected].textForeground", DARK);
        addColor("TabbedPane:TabbedPaneTab[Focused+Pressed+Selected].textForeground", DARK);

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
        addColor("Tree:TreeCell[Focused+Selected].textForeground", DARK);
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
