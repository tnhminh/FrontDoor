package vn.com.tma.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeConvertUtil {

	public static String getCurrentDate() {
		SimpleDateFormat parserDate = new SimpleDateFormat("yyyy-MM-dd");
		String dateCurently;
		dateCurently = parserDate.format(new Date());
		return dateCurently;

	}

	public static String getStringTime() {
		SimpleDateFormat parserTime = new SimpleDateFormat("HH:mm");
		String timeCurently;
		timeCurently = parserTime.format(new Date());
		return timeCurently;

	}

	public static Long getCurrentTime(String t) {
		long currentTime = (Long.parseLong(t.substring(0, t.indexOf(":"))) * 60 * 60)
				+ Long.parseLong(t.substring(t.indexOf(":") + 1)) * 60;
		return currentTime;
	}

	// public static void main(String[] args) throws ParseException {
	// String d = TimeConvertUtil.getCurrentDate();
	// String t = TimeConvertUtil.getCurrentTime();
	// System.out.print(d + "|" + t);
	// SimpleDateFormat parserDate = new SimpleDateFormat("yyyy-MM-dd");
	//
	// Date date = parserDate.parse(d);
	// System.out.println(date);
	//
	// }

}
