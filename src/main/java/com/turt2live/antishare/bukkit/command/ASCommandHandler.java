package com.turt2live.antishare.bukkit.command;

import com.turt2live.antishare.bukkit.AntiShare;
import com.turt2live.antishare.bukkit.PermissionNode;
import com.turt2live.antishare.bukkit.command.validator.ArgumentValidator;
import com.turt2live.antishare.bukkit.lang.Lang;
import com.turt2live.antishare.bukkit.lang.LangBuilder;
import com.turt2live.antishare.bukkit.listener.ToolListener;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the auction command handler
 *
 * @author turt2live
 */
public class ASCommandHandler implements CommandExecutor {

    private class CommandInfo {
        Command annotation;
        Method method;

        public CommandInfo(Command annotation, Method method) {
            this.annotation = annotation;
            this.method = method;
        }
    }

    private Map<String, List<CommandInfo>> commands = new HashMap<String, List<CommandInfo>>();
    private AntiShare plugin;

    public ASCommandHandler(AntiShare plugin) {
        this.plugin = plugin;

        Class<?>[] expectedArguments = new Class[]{
                CommandSender.class,
                Map.class
        };
        for (Method method : getClass().getMethods()) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Command) {
                    if (method.getParameterTypes() == null || (method.getReturnType() != boolean.class && method.getReturnType() != Boolean.class) || method.getParameterTypes().length != expectedArguments.length) {
                        plugin.getLogger().severe("[1] Weird command registration on method " + getClass().getName() + "#" + method.getName());
                        break;
                    } else {
                        boolean valid = true;
                        for (int i = 0; i < expectedArguments.length; i++) {
                            if (expectedArguments[i] != method.getParameterTypes()[i]) {
                                plugin.getLogger().severe("[2] Weird command registration on method " + getClass().getName() + "#" + method.getName());
                                valid = false;
                                break;
                            }
                        }
                        if (!valid) break;
                    }
                    Command auc = (Command) annotation;
                    List<CommandInfo> existing = commands.get(auc.root());
                    if (existing == null) existing = new ArrayList<CommandInfo>();
                    existing.add(new CommandInfo(auc, method));
                    commands.put(auc.root(), existing);
                    break; // We're done here
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
        List<CommandInfo> commandHandlers = commands.get(command.getName());
        if (commandHandlers == null || commandHandlers.isEmpty()) {
            plugin.getLogger().severe("No command handler for command: " + command.getName());
            // TODO: Lang
            sender.sendMessage(new LangBuilder(ChatColor.RED + "Severe internal error. Please contact your administrator.").withPrefix().build());
            return true;
        }
        if (args.length < 1) {
            // TODO: Lang
            sender.sendMessage(ChatColor.RED + "Did you mean " + ChatColor.YELLOW + "/" + command.getName() + " help" + ChatColor.RED + "?");
            return true;
        }
        for (CommandInfo handler : commandHandlers) {
            Command annotation = handler.annotation;
            Method method = handler.method;
            if (annotation.subArgument().equalsIgnoreCase(args[0]) || contains(annotation.alternateSubArgs(), args[0])) {
                if (annotation.playersOnly() && !(sender instanceof Player)) {
                    // TODO: Lang
                    sender.sendMessage(new LangBuilder(ChatColor.RED + "You need to be a player to run that command.").withPrefix().build());
                    return true;
                }
                if (!annotation.permission().equalsIgnoreCase(Command.NO_PERMISSION) && !sender.hasPermission(annotation.permission())) {
                    // TODO: Lang
                    sender.sendMessage(new LangBuilder(ChatColor.RED + "No permission").withPrefix().build());
                    return true;
                }
                Map<String, Object> arguments = new HashMap<String, Object>();
                int numNonOptional = 0;
                for (Annotation annotation1 : method.getAnnotations()) {
                    if (annotation1 instanceof ArgumentList) {
                        ArgumentList list = (ArgumentList) annotation1;
                        for (Argument arg : list.args()) {
                            if (!arg.optional())
                                numNonOptional++;
                            if (arg.validator() != null) {
                                int realIndex = arg.index() + 1;
                                if (realIndex < args.length) {
                                    try {
                                        ArgumentValidator validator = arg.validator().newInstance();
                                        validator.setArguments(arg.validatorArguments());
                                        String input = args[realIndex];
                                        if (validator.isValid(sender, input)) {
                                            arguments.put(arg.subArgument(), validator.get(sender, input));
                                        } else {
                                            // TODO: Lang
                                            sender.sendMessage(validator.getErrorMessage(sender, input));
                                            return true;
                                        }
                                    } catch (InstantiationException e) {
                                        e.printStackTrace();
                                        // TODO: Lang
                                        sender.sendMessage(new LangBuilder(ChatColor.RED + "Severe internal error. Please contact your administrator.").withPrefix().build());
                                        return true;
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                        // TODO: Lang
                                        sender.sendMessage(new LangBuilder(ChatColor.RED + "Severe internal error. Please contact your administrator.").withPrefix().build());
                                        return true;
                                    }
                                } else if (!arg.optional()) {
                                    // TODO: Lang
                                    sender.sendMessage(ChatColor.RED + "Incorrect syntax. Try " + ChatColor.YELLOW + annotation.usage());
                                    return true;
                                }
                            } else {
                                plugin.getLogger().severe("Invalid argument handler for: /" + command.getName() + " " + args[0]);
                                // TODO: Lang
                                sender.sendMessage(new LangBuilder(ChatColor.RED + "Severe internal error. Please contact your administrator.").withPrefix().build());
                                return true;
                            }
                        }
                    }
                }
                if (numNonOptional > arguments.size()) {
                    // TODO: Lang
                    sender.sendMessage(ChatColor.RED + "Incorrect syntax. Try " + ChatColor.YELLOW + annotation.usage());
                    return true;
                }
                try {
                    return (Boolean) method.invoke(this, sender, arguments);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    // TODO: Lang
                    sender.sendMessage(new LangBuilder(ChatColor.RED + "Severe internal error. Please contact your administrator.").withPrefix().build());
                    return true;
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    // TODO: Lang
                    sender.sendMessage(new LangBuilder(ChatColor.RED + "Severe internal error. Please contact your administrator.").withPrefix().build());
                    return true;
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    // TODO: Lang
                    sender.sendMessage(new LangBuilder(ChatColor.RED + "Severe internal error. Please contact your administrator.").withPrefix().build());
                    return true;
                }
            }
        }
        return false;
    }

    private boolean contains(String[] strings, String arg) {
        for (String s : strings) {
            if (s.equalsIgnoreCase(arg)) return true;
        }
        return false;
    }

    @Command(
            root = "antishare",
            subArgument = "toolbox",
            alternateSubArgs = {"tools", "tb"},
            usage = "/as toolbox",
            permission = PermissionNode.TOOLS,
            playersOnly = true
    )
    public boolean toolboxCommand(CommandSender sender, Map<String, Object> arguments) {
        Player player = (Player) sender; // Validated by @Command

        ToolListener.giveTools(player);
        player.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.TOOL_ON_GET)).withPrefix().build());

        return true;
    }

}
