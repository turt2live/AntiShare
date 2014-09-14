/*******************************************************************************
 * Copyright (C) 2014 Travis Ralston (turt2live)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

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
     * Executes this command. Returning false prompts the CommandHandler to send
     * and invalid syntax message to the command sender.
     *
     * @param sender the command sender
     * @param args   the arguments used
     *
     * @return true for success, false otherwise
     */
    public boolean execute(CommandSender sender, String[] args);

}
