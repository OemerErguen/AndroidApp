package de.uni_stuttgart.informatik.sopra.sopraapp.util;


import android.os.Handler;
import android.os.Looper;

/**
 * implementation of regular task
 */
public class PeriodicTask {
    private Handler handler = new Handler(Looper.getMainLooper());

    private Runnable internalWraperRunnable;

    private int INTERVAL = 10000;

    /**
     * constructor
     *
     * @param regularTask
     */
    private PeriodicTask(final Runnable regularTask) {
        internalWraperRunnable = new Runnable() {
            @Override
            public void run() {
                regularTask.run();
                handler.postDelayed(this, INTERVAL);
            }
        };
    }

    /**
     * constructor
     *
     * @param uiUpdater
     * @param interval
     */
    public PeriodicTask(Runnable uiUpdater, int interval){
        this(uiUpdater);
        INTERVAL = interval;
    }

    public synchronized void start(){
        internalWraperRunnable.run();
    }

    public synchronized void stop(){
        handler.removeCallbacks(internalWraperRunnable);
    }
}