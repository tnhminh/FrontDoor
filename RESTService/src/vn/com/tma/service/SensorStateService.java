package vn.com.tma.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import vn.com.tma.model.StateSensor;
import vn.com.tma.util.TimeConvertUtil;

@Path("/sensor")
public class SensorStateService {

	List<StateSensor> listState = new ArrayList<StateSensor>();

	// This method is called if HTML is request
	@GET
	@Path("/addState")
	public void addState() {

		listState.add(new StateSensor(1, "2015-04-07", "6:45", null));
		listState.add(new StateSensor(2, "2015-05-07", "7:45", null));
		listState.add(new StateSensor(3, "2015-06-07", "8:45", null));
		listState.add(new StateSensor(4, "2015-07-07", "9:45", null));
		listState.add(new StateSensor(5, "2015-08-07", "10:45", null));
		listState.add(new StateSensor(6, "2015-08-08", "10:45", null));
		listState.add(new StateSensor(7, "2015-08-09", "1:56", null));
		listState.add(new StateSensor(8, "2015-08-09", "12:57", null));
		listState.add(new StateSensor(9, "2015-08-09", "13:57", null));
		listState.add(new StateSensor(10, "2015-08-09", "14:00", null));
		listState.add(new StateSensor(11, "2015-08-09", "15:00", null));
		listState.add(new StateSensor(12, "2015-08-09", "16:00", null));
		listState.add(new StateSensor(13, "2015-08-09", "17:00", null));
		listState.add(new StateSensor(14, "2015-08-10", "17:00", null));
		listState.add(new StateSensor(15, "2015-08-10", "18:00", null));
		listState.add(new StateSensor(16, "2015-08-10", "19:00", null));
		listState.add(new StateSensor(17, "2015-08-14", "15:19", null));
		listState.add(new StateSensor(18, "2015-08-14", "15:27", null));
		// -----New day
		listState.add(new StateSensor(19, "2015-08-15", "15:23", null));
		listState.add(new StateSensor(20, "2015-08-15", "15:56", null));

		// ------Newer Day
		listState.add(new StateSensor(21, "2015-08-20", "15:56", null));
		listState.add(new StateSensor(22, "2015-08-21", "17:32", null));
		listState.add(new StateSensor(23, "2015-08-22", "19:21", null));
		listState.add(new StateSensor(24, "2015-08-23", "22:13", null));
		listState.add(new StateSensor(25, "2015-08-24", "22:13", null));
		listState.add(new StateSensor(26, "2015-08-24", "23:13", null));
		
		//-----------------/9
		listState.add(new StateSensor(22, "2015-09-20", "15:56", null));
		listState.add(new StateSensor(23, "2015-09-21", "17:32", null));
		listState.add(new StateSensor(24, "2015-09-22", "19:21", null));
		listState.add(new StateSensor(25, "2015-09-23", "22:13", null));
		listState.add(new StateSensor(26, "2015-09-24", "22:13", null));
		listState.add(new StateSensor(27, "2015-09-24", "23:13", null));

	}

	// This method is called if HTML is request
	@GET
	@Path("/showList/allState")
	public String showAllState() {
		addState();
		String resultOfState = "";
		int i = 0;
		while (i < listState.size()) {
			if (i == 0) {
				resultOfState = resultOfState + " \t{\"id\":\""
						+ listState.get(i).getId() + "\",\"date\":\""
						+ listState.get(i).getDate() + "\" ,\"time\":\""
						+ listState.get(i).getTime() + "\"}\n";
			} else {
				resultOfState = resultOfState + " \t,{\"id\":\""
						+ listState.get(i).getId() + "\",\"date\":\""
						+ listState.get(i).getDate() + "\" ,\"time\":\""
						+ listState.get(i).getTime() + "\"}\n";
			}
			i++;
		}
		resultOfState = "[" + resultOfState + "]";
		return resultOfState;
	}

	@GET
	@Path("/showList/{param}")
	public String showList(@PathParam("param") int x) {
		addState();
		String resultOfState = "";
		int i = listState.size() - x;
		while (i < listState.size()) {
			if (i == (listState.size() - x)) {
				resultOfState = resultOfState + " \t{\"id\":\""
						+ listState.get(i).getId() + "\",\"date\":\""
						+ listState.get(i).getDate() + "\" ,\"time\":\""
						+ listState.get(i).getTime() + "\"}\n";
			} else {
				resultOfState = resultOfState + " \t,{\"id\":\""
						+ listState.get(i).getId() + "\",\"date\":\""
						+ listState.get(i).getDate() + "\" ,\"time\":\""
						+ listState.get(i).getTime() + "\"}\n";
			}
			i++;
		}
		resultOfState = "[" + resultOfState + "]";
		return resultOfState;
	}

	@GET
	@Path("/showList/newerState")
	public String showList() throws ParseException {
		SimpleDateFormat parserDate = new SimpleDateFormat("yyyy-MM-dd");
		String d = TimeConvertUtil.getCurrentDate();
		String t = TimeConvertUtil.getStringTime();
		Date currentday = parserDate.parse(d);
		long currentTime = TimeConvertUtil.getCurrentTime(t);

		addState();
		String resultOfState = "";
		int i = 0;
		while (i < listState.size()) {
			Date dateItem = parserDate.parse(listState.get(i).getDate()
					.toString());
			long timeItem = TimeConvertUtil.getCurrentTime(listState.get(i)
					.getTime());
			if (currentday.compareTo(dateItem) < 0
					|| (currentday.compareTo(dateItem) == 0 && currentTime < timeItem)) {
				if (i == 0) {
					resultOfState = resultOfState + " {\"id\":\""
							+ listState.get(i).getId() + "\",\"date\":\""
							+ listState.get(i).getDate() + "\" ,\"time\":\""
							+ listState.get(i).getTime() + "\"}\n";
				} else {
					resultOfState = resultOfState + " ,{\"id\":\""
							+ listState.get(i).getId() + "\",\"date\":\""
							+ listState.get(i).getDate() + "\" ,\"time\":\""
							+ listState.get(i).getTime() + "\"}\n";
				}
			}
			i++;
		}
		resultOfState = "[" + resultOfState + "]";
		return resultOfState.replace("[ ,{", "[ {");
	}
}
