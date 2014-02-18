package com.turt2live.antishare.bukkit.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to register auction commands
 *
 * @author turt2live
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface Command {

    /**
     * A constant to be used for "no permission needed"
     */
    public static final String NO_PERMISSION = "";

    /**
     * The command root, as defined in the plugin.yml
     *
     * @return the command root
     */
    String root();

    /**
     * The sub argument (eg: "start" in "/auc start")
     *
     * @return the sub argument
     */
    String subArgument();

    /**
     * Alternate sub arguments
     *
     * @return alternate sub arguments
     */
    String[] alternateSubArgs() default {};

    /**
     * The permission needed to run this sub-command. If no permission is needed, use {@link #NO_PERMISSION}
     *
     * @return the permission needed to run this command
     */
    String permission() default NO_PERMISSION;

    /**
     * The usage string of this command
     *
     * @return the usage string
     */
    String usage();

    /**
     * Defines whether or not only players can use this command
     *
     * @return true if players can only use this command
     */
    boolean playersOnly() default false;
}
