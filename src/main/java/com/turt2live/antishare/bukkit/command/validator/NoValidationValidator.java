package com.turt2live.antishare.bukkit.command.validator;

import org.bukkit.command.CommandSender;

/**
 * A validator that does no validation
 *
 * @author turt2live
 */
public class NoValidationValidator implements ArgumentValidator {

    @Override
    public boolean isValid(CommandSender sender, String input) {
        return true;
    }

    @Override
    public Object get(CommandSender sender, String input) {
        return input;
    }

    @Override
    public String getErrorMessage(CommandSender sender, String input) {
        return "";
    }

    @Override
    public void setArguments(String[] arguments) {
    }
}
