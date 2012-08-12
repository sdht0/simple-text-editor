package sEditor;

import javax.swing.KeyStroke;

public class MenuDetail {
    public String menuName;
    public String actionLabel;
    public char ac;
    public KeyStroke keystroke;
    public boolean seperatorBelow;

    public MenuDetail(String label, String name, char ac, KeyStroke keystroke,
            boolean sepBelow) {
        this.menuName = label;
        this.actionLabel = name;
        this.ac = ac;
        this.keystroke = keystroke;
        this.seperatorBelow = sepBelow;
    }
}