package com.turt2live.antishare.bukkit.commands;

import com.turt2live.antishare.bukkit.AntiShare;
import com.turt2live.antishare.bukkit.lang.Lang;
import com.turt2live.antishare.bukkit.lang.LangBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * AntiShare Command Handler
 *
 * @author turt2live
 */
public class CommandHandler implements CommandExecutor, ASCommand {

    private List<ASCommand> commands = new ArrayList<ASCommand>();
    private AntiShare plugin = AntiShare.getInstance();

    public CommandHandler() {
        registerCommand(this);
    }

    /**
     * Registers a command with the handler. This will overwrite the existing
     * entry, if any. There are no restrictions on commands.
     *
     * @param executor the executor for this argument
     */
    public void registerCommand(ASCommand executor) {
        if (executor == null) throw new IllegalArgumentException("invalid arguments");

        commands.add(executor);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args == null || args.length == 0) {
            sender.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.ERROR_HELP_SUGGEST)).withPrefix().build());
        } else {
            ASCommand command1 = attemptFind(args[0]);
            if (command1 != null) {
                if (canExecute(command1, sender, true)) {
                    if (!command1.execute(sender, rebuildArgs(args))) {
                        sender.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.ERROR_SYNTAX)).withPrefix().setReplacement(LangBuilder.SELECTOR_VARIABLE, command1.getUsage()).build());
                    }
                }
            } else {
                sender.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.ERROR_HELP_SUGGEST)).withPrefix().build());
            }
        }
        return true;
    }

    private ASCommand attemptFind(String arg) {
        for (ASCommand command : commands) {
            for (String subArg : command.getAlternatives()) {
                if (subArg.equalsIgnoreCase(arg)) {
                    return command;
                }
            }
        }
        return null;
    }

    private String[] rebuildArgs(String[] args) {
        String[] newArgs = new String[args.length - 1 < 0 ? 0 : args.length - 1];
        for (int i = 1; i < args.length; i++) {
            newArgs[i - 1] = args[i];
        }
        return newArgs;
    }

    private boolean canExecute(ASCommand command, CommandSender sender, boolean sendErrors) {
        if (command.isPlayersOnly() && !(sender instanceof Player)) {
            if (sendErrors)
                sender.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.ERROR_NOT_A_PLAYER)).withPrefix().build());
        } else {
            String permission = command.getPermission();
            if (permission != null && !sender.hasPermission(permission)) {
                if (sendErrors)
                    sender.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.ERROR_NO_PERMISSION)).withPrefix().build());
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean isPlayersOnly() {
        return false;
    }

    @Override
    public String getUsage() {
        return "/as help";
    }

    @Override
    public String getDescription() {
        return new LangBuilder(Lang.getInstance().getFormat(Lang.HELP_CMD_HELP)).build();
    }

    @Override
    public String[] getAlternatives() {
        return new String[]{"help", "?", "what", "how2plugin"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.HELP_TITLE)).withPrefix().build());
        for (ASCommand command : commands) {
            if (canExecute(command, sender, false)) {
                String line = new LangBuilder(Lang.getInstance().getFormat(Lang.HELP_LINE)).withPrefix()
                        .setReplacement(LangBuilder.SELECTOR_VARIABLE + "1", command.getUsage())
                        .setReplacement(LangBuilder.SELECTOR_VARIABLE + "2", command.getDescription()).build();
                sender.sendMessage(line);
            }
        }
        return true;
    }
}
