package edu.ntnu.idi.idatt.ui;

public class MenuOption {
  private final String label;
  private final Runnable action;
  private final Menu submenu;
  private final String numberColor;

  public MenuOption(String label, String numberColor, Runnable action) {
    this.label = label;
    this.numberColor = numberColor;
    this.action = action;
    this.submenu = null;
  }

  public MenuOption(String label, Runnable action) {
    this(label, AnsiColors.CYAN, action); // default color
  }

  public MenuOption(String label, String numberColor, Menu submenu) {
    this.label = label;
    this.numberColor = numberColor;
    this.submenu = submenu;
    this.action = null;
  }

  public MenuOption(String label, Menu submenu) {
    this(label, AnsiColors.CYAN, submenu); // default color
  }

  public String getLabel() {
    return label;
  }

  public String getNumberColor() {
    return numberColor;
  }

  public void execute() {
    if (action != null) {
      action.run();
    } else if (submenu != null) {
      submenu.show();
    }
  }
}