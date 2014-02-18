package com.turt2live.antishare.bukkit.command.validator;

import org.bukkit.command.CommandSender;

/**
 * Represents an argument validator
 *
 * @author turt2live
 */
public interface ArgumentValidator {

    /**
     * Determines if the specified input is valid
     *
     * @param input  the input string
     * @param sender the comamnd sender
     * @return true if the input is valid
     */
    public boolean isValid(CommandSender sender, String input);

    /**
     * Gets the value representing the specified input
     *
     * @param input  the input
     * @param sender the comamnd sender
     * @return the output, or null if invalid input
     */
    public Object get(CommandSender sender, String input);

    /**
     * Gets an error message for the supplied input
     *
     * @param input  the input
     * @param sender the command sender. Although passed, error messages should not be displayed to this sender
     * @return the error message
     */
    public String getErrorMessage(CommandSender sender, String input);

    /**
     * Sets the arguments to be used by this validator
     *
     * @param arguments the arguments to use
     */
    public void setArguments(String[] arguments);

}
