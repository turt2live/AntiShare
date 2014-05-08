package com.turt2live.antishare.events;

import java.lang.annotation.*;

/**
 * AntiShare Event Listener
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventListener {
}
