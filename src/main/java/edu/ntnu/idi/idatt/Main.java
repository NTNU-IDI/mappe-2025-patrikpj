package edu.ntnu.idi.idatt;

/**
 * Application entry point.
 */
public class Main {

  /**
   * Main method.
   *
   * @param args command line arguments (not used)
   */
  public static void main(String[] args) {
    DiaryApp app = new DiaryApp();
    app.init();
    app.start();
  }
}
