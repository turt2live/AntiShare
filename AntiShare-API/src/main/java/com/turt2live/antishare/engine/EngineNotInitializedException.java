package com.turt2live.antishare.engine;

/**
 * An exception to be used when an engine is not initialized
 */
public class EngineNotInitializedException extends RuntimeException {

    public EngineNotInitializedException() {
        super();
    }

    public EngineNotInitializedException(String message) {
        super(message);
    }

}
