package com.intrans.reactor.workzone.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Date Utility Functions.
 * 
 * @author Vamsi Krishna J <br />
 *         <b>Date:</b> Feb 6, 2017
 *
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

	public static final String PATTERN_MM_DD_YYYY_HH_MM_SS_AA_Z = "MM/dd/yyyy hh:mm:ss aa z";
	public static final String PATTERN_MM_DD_YYYY_HH_MM_SS_AA = "MM/dd/yyyy hh:mm:ss aa";
	public static final String PATTERN_MM_DD_YYYY = "MM-dd-yyyy";
	public static final String PATTERN_DD_MMMM_YYYY = "dd MMMM, yyyy";
	public static final String PATTERN_DD_MON_YY_HH_MM_SS_AA_Z = "dd MMM''yy, hh.mm:ss aa z";
	public static final String PATTERN_DD_MON_YYYY_HH_MM_SS_AA_Z = "dd MMM, yyyy hh.mm:ss aa z";
	public static final String PATTERN_DATERANGE_SEARCH = "yyyy-MM-dd";
	public static final String PATTERN_YYYYMMDDHHMMSSZ = "yyyyMMDDHHmmssZ";

	/**
	 * Returns current date for default time zone
	 * 
	 * @return
	 */
	public static Date getCurrentDate() {
		return new Date();
	}

	/**
	 * Returns previous day date for the default time zone
	 * 
	 * @return
	 */
	public static Date getPreviousDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		return calendar.getTime();
	}

	/**
	 * Returns previous day date for the given number of days for the default
	 * time zone
	 * 
	 * @return
	 */
	public static Date getPreviousDate(int numberOfDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -numberOfDays);
		return calendar.getTime();
	}

	/**
	 * Returns the next day date for the default time zone
	 * 
	 * @return
	 */
	public static Date getNextDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 1);
		return calendar.getTime();
	}

	/**
	 * Returns the next day for the given date
	 * 
	 * @param date
	 * @return Next day of the given date
	 */
	public static Date getNextDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 1);
		return cal.getTime();
	}

	/**
	 * Returns previous date to the given date.
	 * 
	 * @param date
	 * @return
	 */
	public static Date getPreviousDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	/**
	 * Returns nth previous date to the given date.
	 * 
	 * @param date
	 * @param numberOfDays
	 * @return
	 */
	public static Date getPreviousDate(Date date, int numberOfDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -numberOfDays);
		return calendar.getTime();
	}

	/**
	 * Returns date for specified milliseconds in default time zone
	 * 
	 * @return
	 * @see Date#Date(long)
	 */
	public static Date getDate(long millis) {
		return new Date(millis);
	}

	/**
	 * Returns current time stamp
	 * 
	 * @return
	 */
	public static Timestamp getCurrentTimeStamp() {
		return new Timestamp(Calendar.getInstance().getTimeInMillis());
	}

	/**
	 * Returns the current time in milliseconds
	 * 
	 * @return timestamp
	 */
	public static long getCurrentTime() {
		return System.currentTimeMillis();
	}

	/**
	 * Returns the provided date truncated with time and set to zero hour
	 * 
	 * @return
	 */
	public static Date truncateDate(Date date) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.AM_PM, Calendar.AM);
		return calendar.getTime();
	}

	/**
	 * Returns the current default time zone date truncated with time and set to
	 * zero hour
	 * 
	 * @return
	 */
	public static Date truncateCurrentDate() {
		return truncateDate(getCurrentDate());
	}

	/**
	 * Returns date in MM-DD-YYYY format.
	 * 
	 * @return
	 */
	public static String getDefaultCurrentDateStr() {
		return getCurrentDateStr(PATTERN_MM_DD_YYYY);
	}

	public static String getCurrentDateStr(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(getCurrentDate());
	}

	public static LocalTime getTimeofDayFromTimestamp(long millis) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		return LocalTime.of(cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), cal.get(Calendar.AM_PM));
	}

	public static String formatDate(long millis, String pattern) {
		return formatDate(new Date(millis), pattern);
	}

	public static String formatDate(Date date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

}
