package utilities.interfaces;

import java.time.Duration;
import java.util.TimerTask;

public interface ITimer {
    String getTimeAsString();
    void setTimeAsString(Duration duration);

    void cancel();
    void startTimer(long delay, long period, TimerTask task);
}
