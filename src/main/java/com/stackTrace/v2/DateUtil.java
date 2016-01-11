package com.stackTrace.v2;

import java.util.Calendar;

public class DateUtil {
    public static final int DAY = 1000 * 3600 * 24;
    public static final char SPLIT_DATE = '-';
    public static final char SPLIT_BLANK = ' ';
    public static final char SPLIT_TIME = ':';
    
    public static String getDateAndTimeStr(long milliTime) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTimeInMillis(milliTime);
    	
    	StringBuffer buffer = new StringBuffer();
    	buffer.append(calendar.get(Calendar.YEAR)).append(SPLIT_DATE);
    	appendTwoDigit(buffer, calendar.get(Calendar.MONTH) + 1);
	    buffer.append(SPLIT_DATE);
	    appendTwoDigit(buffer, calendar.get(Calendar.DAY_OF_MONTH));
	    buffer.append(SPLIT_BLANK);
	    
	    appendTwoDigit(buffer, calendar.get(Calendar.HOUR_OF_DAY));
	    buffer.append(SPLIT_TIME);
	    appendTwoDigit(buffer, calendar.get(Calendar.MINUTE));
	    buffer.append(SPLIT_TIME);
    	appendTwoDigit(buffer, calendar.get(Calendar.SECOND));
        return buffer.toString();
    }
    
    public static String getDateStr(long milliTime) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTimeInMillis(milliTime);
    	
    	return getDateStr(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }
    
    public static String getDateStr(int year, int month, int day) {
    	StringBuffer buffer = new StringBuffer();
    	buffer.append(year).append(SPLIT_DATE);
    	appendTwoDigit(buffer, month);
	    buffer.append(SPLIT_DATE);
	    appendTwoDigit(buffer, day);
	    
        return buffer.toString();
    }
    
    public static int getDay(long milliTime) {
    	return (int) (milliTime / DAY);
    }
    
    public static StringBuffer getDateAndTime(boolean showTimeOnly) {
    	Calendar calendar = Calendar.getInstance();
    	
    	StringBuffer buffer = new StringBuffer();
    	if(!showTimeOnly) {
		    buffer.append(calendar.get(Calendar.YEAR)).append(SPLIT_DATE);
	    	appendTwoDigit(buffer, calendar.get(Calendar.MONTH) + 1);
		    buffer.append(SPLIT_DATE);
		    appendTwoDigit(buffer, calendar.get(Calendar.DAY_OF_MONTH));
		    buffer.append(SPLIT_BLANK);
    	}
	    appendTwoDigit(buffer, calendar.get(Calendar.HOUR_OF_DAY));
	    buffer.append(SPLIT_TIME);
	    appendTwoDigit(buffer, calendar.get(Calendar.MINUTE));
	    buffer.append(SPLIT_TIME);
    	appendTwoDigit(buffer, calendar.get(Calendar.SECOND));
    	if(calendar.get(Calendar.MILLISECOND) < 100) {
    		buffer.append('.').append('0');
    		appendTwoDigit(buffer, calendar.get(Calendar.MILLISECOND));
    	} else {
    		buffer.append('.').append(Integer.toString(calendar.get(Calendar.MILLISECOND)));
    	}
    	return buffer;
    }

    private static void appendTwoDigit(final StringBuffer buffer, final int value) {
    	if (value < 100) {
            buffer.append((char)(value / 10 + 48));
            buffer.append((char)(value % 10 + 48));
        } else {
            buffer.append(Integer.toString(value));
        }
    }
}