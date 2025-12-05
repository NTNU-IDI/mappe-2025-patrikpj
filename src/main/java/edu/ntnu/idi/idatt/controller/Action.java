package edu.ntnu.idi.idatt.controller;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * Represents a single action/screen in the TUI.
 * Controllers return Actions to indicate the next step in navigation.
 * Return null to exit the application.
 */
@FunctionalInterface
public interface Action {
    /**
     * Executes this action, rendering output and handling input.
     *
     * @param in  Scanner for user input
     * @param out PrintStream for output
     * @return the next Action to execute, or null to exit
     */
    Action execute(Scanner in, PrintStream out);
}

