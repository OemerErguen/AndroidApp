package de.uni_stuttgart.informatik.sopra.sopraapp.util;

import java.util.Observable;

/**
 * this class is an implementation of a java {@link Observable}.
 * it basically keeps tracking state of a single boolean variable "state".
 * Call {@link #setValue(boolean)} to update the value and call
 * {@link #notifyObservers()} to trigger obervers
 */
public class BooleanObservable extends Observable {
    private boolean state = false;

    /**
     * constructor
     *
     * @param initialState
     */
    public BooleanObservable(boolean initialState) {
        this.state = initialState;
    }

    /**
     * set value + notify observers implicitely
     *
     * @param newValue
     */
    public void setValueAndTriggerObservers(boolean newValue) {
        setValue(newValue);
        this.notifyObservers();
    }

    public void setValue(boolean newValue) {
        if(state != newValue) {
            state = newValue;
            setChanged();
        }
    }

    public boolean getValue() {
        return state;
    }
}
