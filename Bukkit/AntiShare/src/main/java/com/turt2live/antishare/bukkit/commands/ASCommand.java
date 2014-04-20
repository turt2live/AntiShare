package com.turt2live.antishare.bukkit.commands;

import org.bukkit.command.CommandSender;

/**
 * AntiShare Command
 *
 * @author turt2live
 */
public interface ASCommand {

    /**
     * Gets the permission required to run this command
     *
     * @return the permission node, or null for none
     */
    public String getPermission();

    /**
     * Determines if this command can only be run by players
     *
     * @return true for players only
     */
    public boolean isPlayersOnly();

    /**
     * Gets the usage of this command
     *
     * @return the command usage
     */
    public String getUsage();

    /**
     * Gets the command description
     *
     * @return the description of the command
     */
    public String getDescription();

    /**
     * Gets a listing of valid arguments to access this command, such as "help".
     *
     * @return the alternatives
     */
    public String[] getAlternatives();

    /**
     * Executes this command
     *
     * @param sender the command sender
     * @param args   the arguments used
     * @return true for success, false otherwise
     */
    public boolean execute(CommandSender sender, String[] args);

}
