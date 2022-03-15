package utilities;

import utilities.interfaces.ITimer;

import java.time.Duration;
import java.util.TimerTask;

public class Timer implements ITimer {
    private static final String TIME_FORMAT = "%d:%02d";
    private String timeAsString;

    private java.util.Timer timer;

    public String getTimeAsString() {
        return timeAsString;
    }

    public void setTimeAsString(Duration duration) {
        this.timeAsString = String.format(TIME_FORMAT, duration.toMinutesPart(), duration.toSecondsPart());
    }

    public void cancel() {
        this.timer.cancel();
        this.timer.purge();
    }

    public void startTimer(long delay, long period, TimerTask task) {
        if (this.timer != null) {
            this.timer.cancel();
        }

        this.timer = new java.util.Timer();
        this.timer.scheduleAtFixedRate(task, delay, period);
    }
}
