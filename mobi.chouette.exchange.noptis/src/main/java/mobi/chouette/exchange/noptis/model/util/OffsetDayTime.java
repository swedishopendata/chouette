package mobi.chouette.exchange.noptis.model.util;

import java.time.LocalDateTime;

public class OffsetDayTime {
    private static final int HOURS_PER_DAY = 24;
    private static final int MINUTES_PER_HOUR = 60;
    private static final int SECONDS_PER_MINUTE = 60;
    private static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;
    private static final int SECONDS_PER_DAY = HOURS_PER_DAY * SECONDS_PER_HOUR;

    private final int day;
    private final int hour;
    private final int minute;
    private final int second;

    public OffsetDayTime(int day, int hour, int minute, int second) {
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public OffsetDayTime(LocalDateTime dateTime) {
        this(dateTime.getDayOfYear() - 1, dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond());
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public String getAsHourMinuteSecondString() {
        return twoDigitString(getDaysAndHoursAsHours()) + ":" + twoDigitString(minute) + ":" + twoDigitString(second);
    }

    private int getDaysAndHoursAsHours() {
        return day * HOURS_PER_DAY + hour;
    }

    public OffsetDayTime plusSeconds(int seconds) {
        int totalSeconds = day * SECONDS_PER_DAY
                + hour * SECONDS_PER_HOUR
                + minute * SECONDS_PER_MINUTE
                + second + seconds;
        int newDay = totalSeconds / SECONDS_PER_DAY;
        int newHour = (totalSeconds / SECONDS_PER_HOUR) % HOURS_PER_DAY;
        int newMinute = (totalSeconds / SECONDS_PER_MINUTE) % MINUTES_PER_HOUR;
        int newSecond = totalSeconds % SECONDS_PER_MINUTE;
        return new OffsetDayTime(newDay, newHour, newMinute, newSecond);
    }

    private String twoDigitString(int value) {
        if (value < 100) {
            StringBuffer buf = new StringBuffer(2);
            buf.append((char) (value / 10 + '0'));
            buf.append((char) (value % 10 + '0'));
            return buf.toString();
        } else
            return Integer.toString(value);
    }

    @Override
    public String toString() {
        return "OffsetDayTime{"
                + "day=" + day
                + ", hour=" + hour
                + ", minute=" + minute
                + ", second=" + second
                + '}';
    }
}
