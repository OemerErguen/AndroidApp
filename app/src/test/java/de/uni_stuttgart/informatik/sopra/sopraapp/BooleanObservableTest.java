package de.uni_stuttgart.informatik.sopra.sopraapp;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.Observable;
import java.util.Observer;

import de.uni_stuttgart.informatik.sopra.sopraapp.util.BooleanObservable;

/**
 * test util class {@link BooleanObservable}
 */
public class BooleanObservableTest {

    private static boolean wasCalled = false;
    private static Observer testObserver = new Observer() {
        @Override
        public void update(Observable observable, Object o) {
            BooleanObservableTest.wasCalled = true;
        }
    };

    @Before
    public void setup() {
        wasCalled = false;
    }

    @Test
    public void testBooleanObservableMethods() {
        BooleanObservable bo = new BooleanObservable(true);
        Assert.assertTrue(bo.getValue());
        bo.setValue(false);
        Assert.assertFalse(bo.getValue());
        Assert.assertFalse(wasCalled);
        bo.addObserver(testObserver);
        Assert.assertFalse(wasCalled);
        bo.setValue(false);
        Assert.assertFalse(wasCalled);
        bo.notifyObservers();
        Assert.assertTrue(wasCalled);
        Assert.assertFalse(bo.getValue());
    }

    @Test
    public void testObservableInstantUpdateMethod() {
        BooleanObservable bo = new BooleanObservable(true);
        bo.addObserver(testObserver);
        Assert.assertTrue(bo.getValue());
        Assert.assertFalse(wasCalled);
        bo.setValueAndTriggerObservers(false);
        Assert.assertFalse(bo.hasChanged());
        Assert.assertTrue(wasCalled);
        Assert.assertFalse(bo.getValue());
    }

    @Test
    public void testObservableOnlyUpdateIfUpdate() {
        BooleanObservable bo = new BooleanObservable(true);
        bo.addObserver(testObserver);
        Assert.assertFalse(wasCalled);
        bo.setValue(true);
        Assert.assertFalse(bo.hasChanged());
        bo.setValue(false);
        Assert.assertTrue(bo.hasChanged());
    }
}
