package vn.com.tma.task;

import java.util.TimerTask;

public class Timer extends java.util.Timer {
    private long interval;

    public Timer(long delay) {
        this.interval = delay;
    }

    @Override
    public void schedule(TimerTask task, long delay) {
        delay = this.interval;
        super.schedule(task, delay);
    }

}
