package edu.ntnu.idi.idatt.ui;

/**
 * Provides string presets for colored printing.
 * 
 * <p>This is a utility class and should not be instantiated.
 */
public class AnsiColors {

  /**
   * Private constructor to prevent instantiation of this utility class.
   */
  private AnsiColors() {
    throw new AssertionError("Utility class cannot be instantiated");
  }

  public static final String RESET = "\u001B[0m";
  public static final String BLACK = "\u001B[30m";
  public static final String RED = "\u001B[31m";
  public static final String GREEN = "\u001B[32m";
  public static final String YELLOW = "\u001B[33m";
  public static final String BLUE = "\u001B[34m";
  public static final String PURPLE = "\u001B[35m";
  public static final String CYAN = "\u001B[36m";
  public static final String WHITE = "\u001B[37m";

  public static final String[] PALETTE = {
      RED, GREEN, YELLOW, BLUE, PURPLE, CYAN, WHITE
  };
}