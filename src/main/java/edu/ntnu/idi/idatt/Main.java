package edu.ntnu.idi.idatt;

/**
 * Application entry point.
 */
public class Main {

  /**
   * Starts the Diary application.
   *
   * @param args command line arguments (not used)
   */
  public static void main(String[] args) {
    DiaryApp app = new DiaryApp();
    app.init();
    app.start();
  }
}