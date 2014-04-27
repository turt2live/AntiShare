package com.turt2live.antishare.bukkit.commands.command;

import com.turt2live.antishare.PermissionNodes;
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
        return PermissionNodes.TOOLS;
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
        return new String[]{"toolbox", "tools"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender; // Validated by CommandHandler

        ToolListener.giveTools(player);
        player.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.TOOL_ON_GET)).withPrefix().build());
        return true;
    }
}
