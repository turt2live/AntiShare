package com.turt2live.antishare.bukkit.command;


import com.turt2live.antishare.bukkit.command.validator.ArgumentValidator;
import com.turt2live.antishare.bukkit.command.validator.NoValidationValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to define an argument for a command
 *
 * @author turt2live
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface Argument {

    /**
     * The argument index where index is the number in this example: "/auc start 0 1 2 3"
     *
     * @return the argument index
     */
    int index() default 0;

    /**
     * Flags the argument as optional
     *
     * @return true if optional
     */
    boolean optional() default false;

    /**
     * The argument name
     *
     * @return
     */
    String subArgument();

    /**
     * The argument validator
     *
     * @return the argument validator
     */
    Class<? extends ArgumentValidator> validator() default NoValidationValidator.class;

    /**
     * Arguments for the validator
     *
     * @return the arguments
     */
    String[] validatorArguments() default {};
}
