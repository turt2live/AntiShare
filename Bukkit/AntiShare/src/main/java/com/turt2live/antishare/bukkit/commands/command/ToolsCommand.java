/*******************************************************************************
 * Copyright (C) 2014 Travis Ralston (turt2live)
 *
 * This software is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.turt2live.antishare.bukkit.commands.command;

import com.turt2live.antishare.APermission;
import com.turt2live.antishare.bukkit.commands.ASCommand;
import com.turt2live.antishare.bukkit.lang.Lang;
import com.turt2live.antishare.bukkit.lang.LangBuilder;
import com.turt2live.antishare.bukkit.listener.ToolListener;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * /as tools
 */
public class ToolsCommand implements ASCommand {

    @Override
    public String getPermission() {
        return APermission.TOOLS;
    }

    @Override
    public boolean isPlayersOnly() {
        return true;
    }

    @Override
    public String getUsage() {
        return "/as tools";
    }

    @Override
    public String getDescription() {
        return new LangBuilder(Lang.getInstance().getFormat(Lang.HELP_CMD_TOOLS)).build();
    }

    @Override
    public String[] getAlternatives() {
        return new String[] {"toolbox", "tools"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender; // Validated by CommandHandler

        ToolListener.giveTools(player);
        player.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.TOOL_ON_GET)).withPrefix().build());
        return true;
    }
}
