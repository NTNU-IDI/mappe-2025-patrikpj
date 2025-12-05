package edu.ntnu.idi.idatt.controller;

import edu.ntnu.idi.idatt.view.mainmenu.MainMenuView;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Controller for the main menu.
 * Handles navigation to other controllers.
 */
public class MainMenuController {

    private final MainMenuView view;
    
    // Navigation references (set via setters to avoid circular dependency)
    private DiaryController diaryController;
    private AuthorController authorController;

    /**
     * Creates a new MainMenuController.
     *
     * @param view the main menu view
     */
    public MainMenuController(MainMenuView view) {
        this.view = view;
    }

    /**
     * Sets the diary controller for navigation.
     *
     * @param diaryController the diary controller
     */
    public void setDiaryController(DiaryController diaryController) {
        this.diaryController = diaryController;
    }

    /**
     * Sets the author controller for navigation.
     *
     * @param authorController the author controller
     */
    public void setAuthorController(AuthorController authorController) {
        this.authorController = authorController;
    }

    /**
     * Shows the main menu and handles user input.
     *
     * @param in  Scanner for user input
     * @param out PrintStream for output
     * @return the next action to execute
     */
    public Action showMenu(Scanner in, PrintStream out) {
        view.renderMenu(out);

        while (true) {
            String choice = in.nextLine().trim().toLowerCase();

            switch (choice) {
                case "1" -> { return (in2, out2) -> diaryController.showEntriesMenu(in2, out2); }
                case "2" -> { return (in2, out2) -> authorController.showAuthorMenu(in2, out2); }
                case "3" -> { return (in2, out2) -> showStatistics(in2, out2); }
                case "q" -> { return null; }
                default -> {
                    view.showError("Invalid selection. Try again.", out);
                    view.prompt(out);
                }
            }
        }
    }

    /**
     * Shows statistics (placeholder).
     */
    private Action showStatistics(Scanner in, PrintStream out) {
        view.showNotImplemented("Statistics", out);
        view.promptContinue(out);
        in.nextLine();
        return this::showMenu;
    }
}

