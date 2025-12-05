package edu.ntnu.idi.idatt.controller;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * Router that executes the action-based TUI loop.
 * Continues executing actions until one returns null.
 */
public class Router {

    private final Scanner in;
    private final PrintStream out;
    private Action currentAction;

    /**
     * Creates a new Router with the given initial action.
     *
     * @param initialAction the first action to execute
     * @param in            Scanner for user input
     * @param out           PrintStream for output
     */
    public Router(Action initialAction, Scanner in, PrintStream out) {
        this.currentAction = initialAction;
        this.in = in;
        this.out = out;
    }

    /**
     * Runs the main application loop.
     * Executes actions until one returns null.
     */
    public void run() {
        while (currentAction != null) {
            currentAction = currentAction.execute(in, out);
            out.println();
        }
        out.println("Goodbye!");
    }
}
