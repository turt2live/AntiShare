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

package com.turt2live.antishare.bukkit.commands.command;

import com.turt2live.antishare.APermission;
import com.turt2live.antishare.bukkit.AntiShare;
import com.turt2live.antishare.bukkit.commands.ASCommand;
import com.turt2live.antishare.bukkit.lang.Lang;
import com.turt2live.antishare.bukkit.lang.LangBuilder;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * /as reload
 */
public class ReloadCommand implements ASCommand {

    @Override
    public String getPermission() {
        return APermission.PLUGIN_RELOAD;
    }

    @Override
    public boolean isPlayersOnly() {
        return false;
    }

    @Override
    public String getUsage() {
        return "/as reload";
    }

    @Override
    public String getDescription() {
        return Lang.getInstance().getFormat(Lang.HELP_CMD_RELOAD);
    }

    @Override
    public String[] getAlternatives() {
        return new String[] {"rl"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.GENERAL_RELOADING)).withPrefix().build());
        int reloads = AntiShare.getInstance().reloadPlugin();

        if (reloads >= 5) {
            List<String> messages = Lang.getInstance().getFormatList(reloads >= 20 ? Lang.GENERAL_RELOAD_CRITICAL : Lang.GENERAL_RELOAD_WARNING);
            for (String message : messages) {
                sender.sendMessage(new LangBuilder(message).withPrefix().setReplacement(LangBuilder.SELECTOR_VARIABLE, reloads + "").build());
            }
        }

        sender.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.GENERAL_RELOADED)).withPrefix().build());
        return true;
    }
}
