package io.github.farmageddon.ultilites;

import com.badlogic.gdx.Gdx;

public class Timer_ {

    public final static float REALTIME = 1;
    public final static float GAMETIME = 600; // 1 real sec = 10 minutes

    //how long timer has been running
    private double secondsSinceStart;
    // how long should the timer run, the desired end time
    private double timerEndTime;

    private float realToTimerRatio; // 1 real second = this many timer seconds

    // if timer is paused, no delta is added
    private boolean paused;
    // chrono timers have no end time, these timers are essentially clocks, run continously
    private boolean chrono;

    private int daysPassed;

    public Timer_() {
        this.timerEndTime = 0;
        this.secondsSinceStart = 0;
        this.daysPassed = 0;
        this.paused = true;
        this.realToTimerRatio = Timer_.GAMETIME;
        this.chrono = false;
    }

    // called every frame (in render())
    public void tick() {
        // tăng thời gian cho bộ đếm
        if (!(secondsSinceStart >= timerEndTime) || (chrono)) {
            if (!paused) {
                secondsSinceStart += (Gdx.graphics.getDeltaTime() * realToTimerRatio);

                // normalize chronological timers
                if (chrono) {
                    if (secondsSinceStart > 86400) {
                        secondsSinceStart -= 86400;
                        daysPassed++;
                    }
                }
            }
        }
    }

    // hàm set thời gian cụ thể
    public void setStartTime(int days, int hour, int min, int sec) {
        daysPassed = days;
        secondsSinceStart = sec + (min * 60) + (hour * 3600);
    }

    // nếu thời gian chạm đích
    public boolean isDone() {
        if (secondsSinceStart >= timerEndTime) {
            return true;
        }
        return false;
    }

    public void setToChrono() {
        chrono = true;
    }

    public void setTimeRatio(float ratio) {
        realToTimerRatio = ratio;
    }

    public boolean isPaused() {
        return paused;
    }

    public void Pause() {
        paused = true;
    }

    public void Start() {
        paused = false;
    }

    public String getFormattedElapsed() {
        return format(secondsSinceStart);
    }

    public String getFormattedRemaining() {
        return format(timerEndTime - secondsSinceStart);
    }

    // đưa thời gian về dạng HH:MM AM/PM
    public String getFormattedTimeofDay() {
        int hours = (int) Math.floor(secondsSinceStart / 3600);
        String sub = " ";
        if (hours == 0) {
            hours = 12;
            sub = sub.concat("AM");
        } else if (hours >= 12) {
            if (hours > 12)
                hours -= 12;
            sub = sub.concat("PM");
        } else if ((hours < 12) && (hours > 0)) {
            sub = sub.concat("AM");
        }

        int minutes = (int) Math.floor((secondsSinceStart % 3600) / 60);

        String res = String.format("%1$d:%2$02d", hours, minutes);
        return res.concat(sub);
    }

    // đưa thời gian về dạng HH:MM:SS
    private String format(double d) {

        int hours = (int) Math.floor(d / 3600);
        int minutes = (int) Math.floor((d % 3600) / 60);
        double seconds = d % 60;

        return String.format("%1$02d:%2$02d:%3$03.1f", hours, minutes,
            seconds);
    }

    public double getElapsedInSeconds() {
        return secondsSinceStart;
    }

    public double getElapsedInMinutes() {
        return (secondsSinceStart / 60);
    }

    public double getElapsedInHours() {
        return (secondsSinceStart / 3600);
    }

    public double getRemaining() {
        return (timerEndTime - secondsSinceStart);
        // this returns a negative number if timer is done, however could be modded to return 0 instead.
    }

    public int getDaysPassed() {
        return daysPassed;
    }

    // starts a new timer
    public void StartNew(float timerEndTime, boolean startNow,
                         boolean chronological) {
        this.timerEndTime = timerEndTime;
        secondsSinceStart = 0;
        paused = !startNow;
        chrono = chronological;
    }

    public void setDaysPassed(int daysPassed) {
        this.daysPassed = daysPassed;
    }

    public float getTimeRatio() {
        return realToTimerRatio;
    }
}
