package com.turt2live.antishare.bukkit.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents an argument list
 *
 * @author turt2live
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface ArgumentList {

    /**
     * Arguments
     *
     * @return arguments
     */
    Argument[] args();
}
