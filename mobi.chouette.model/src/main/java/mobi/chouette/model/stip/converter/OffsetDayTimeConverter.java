package mobi.chouette.model.stip.converter;

import mobi.chouette.model.stip.util.OffsetDayTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Converter(autoApply = true)
public class OffsetDayTimeConverter implements AttributeConverter<OffsetDayTime, Date> {

    @Override
    public Date convertToDatabaseColumn(OffsetDayTime offsetDayTime) {
        GregorianCalendar calendar = new GregorianCalendar(1900, 0, offsetDayTime.getDay() + 1, offsetDayTime.getHour(), offsetDayTime.getMinute(),
                offsetDayTime.getSecond());
        return calendar.getTime();
    }

    @Override
    public OffsetDayTime convertToEntityAttribute(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_YEAR) - 1;
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        return new OffsetDayTime(day, hour, minute, second);
    }
}
