package edu.ntnu.idi.idatt.view._components;

/**
 * Generates and displays the colored ASCII banner for the Diary System.
 */
public class DiarySystemBanner {

  /**
   * Builds and returns the colored ASCII banner string.
   *
   * @return the formatted banner as a string
   */
  public static String getColoredBanner() {
    StringBuilder sb = new StringBuilder();

    String[][] letters = {
        AsciiLetters.D,
        AsciiLetters.I,
        AsciiLetters.A,
        AsciiLetters.R,
        AsciiLetters.Y,
        AsciiLetters.DASH,
        AsciiLetters.C,
        AsciiLetters.L,
        AsciiLetters.I,
    };

    String[] colors = {
        AnsiColors.RED,    // D
        AnsiColors.GREEN,  // I
        AnsiColors.YELLOW, // A
        AnsiColors.BLUE,   // R
        AnsiColors.PURPLE, // Y
        AnsiColors.CYAN,   // -
        AnsiColors.RED,    // S
        AnsiColors.GREEN,  // Y
        AnsiColors.YELLOW, // S
    };

    int rows = 6;

    for (int row = 0; row < rows; row++) {
      for (int i = 0; i < letters.length; i++) {
        String[] letter = letters[i];
        String color = colors[i % colors.length];
        String line = row < letter.length ? letter[row] : "";
        sb.append(color).append(line).append(" ");
      }
      sb.append(AnsiColors.RESET).append("\n");
    }

    return sb.toString();
  }

  /**
   * Prints the banner to the console. Used for testing.
   *
   * @param args command line arguments (not used)
   */
  public static void main(String[] args) {
    System.out.println(getColoredBanner());
  }
}