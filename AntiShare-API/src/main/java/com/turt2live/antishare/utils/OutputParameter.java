package com.turt2live.antishare.utils;

/**
 * Represents an output parameter which can be used similar to C#'s out keyword
 *
 * @param <T> the type
 */
public class OutputParameter<T> {

    private T value;
    private boolean wasCalled = false;

    /**
     * Creates an output parameter with a default null value
     */
    public OutputParameter() {
    }

    /**
     * Creates an output parameter with a default specified value
     *
     * @param def the default value, can be null
     */
    public OutputParameter(T def) {
        this.value = def;
    }

    /**
     * Sets this output parameter to a value
     *
     * @param value the value to set, can be null
     */
    public void setValue(T value) {
        this.value = value;
        wasCalled = true;
    }

    /**
     * Determines if {@link #getValue()} is a non-null value
     *
     * @return true if not null
     */
    public boolean hasValue() {
        return getValue() != null;
    }

    /**
     * Gets the stored value
     *
     * @return the stored value, may be null
     */
    public T getValue() {
        return value;
    }

    /**
     * Determines if {@link #setValue(Object)} was called at least once
     *
     * @return true if called at least once
     */
    public boolean wasCalled() {
        return wasCalled;
    }

}
