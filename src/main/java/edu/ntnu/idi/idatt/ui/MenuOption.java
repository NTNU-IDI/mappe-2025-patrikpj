package edu.ntnu.idi.idatt.ui;

/**
 * Represents a single option in a menu with a label and an action or submenu.
 */
public class MenuOption {
  private final String label;
  private final Runnable action;
  private final Menu submenu;
  private final String numberColor;

  /**
   * Creates a menu option with a custom color and action.
   *
   * @param label       the option label
   * @param numberColor the ANSI color code for the option number
   * @param action      the action to execute when selected
   */
  public MenuOption(String label, String numberColor, Runnable action) {
    this.label = label;
    this.numberColor = numberColor;
    this.action = action;
    this.submenu = null;
  }

  /**
   * Creates a menu option with the default color and an action.
   *
   * @param label  the option label
   * @param action the action to execute when selected
   */
  public MenuOption(String label, Runnable action) {
    this(label, AnsiColors.CYAN, action);
  }

  /**
   * Creates a menu option with a custom color and a submenu.
   *
   * @param label       the option label
   * @param numberColor the ANSI color code for the option number
   * @param submenu     the submenu to display when selected
   */
  public MenuOption(String label, String numberColor, Menu submenu) {
    this.label = label;
    this.numberColor = numberColor;
    this.submenu = submenu;
    this.action = null;
  }

  /**
   * Creates a menu option with the default color and a submenu.
   *
   * @param label   the option label
   * @param submenu the submenu to display when selected
   */
  public MenuOption(String label, Menu submenu) {
    this(label, AnsiColors.CYAN, submenu);
  }

  /**
   * Returns the label of this option.
   *
   * @return the option label
   */
  public String getLabel() {
    return label;
  }

  /**
   * Returns the ANSI color code for the option number.
   *
   * @return the color code
   */
  public String getNumberColor() {
    return numberColor;
  }

  /**
   * Executes the action or displays the submenu.
   */
  public void execute() {
    if (action != null) {
      action.run();
    } else if (submenu != null) {
      submenu.show();
    }
  }
}