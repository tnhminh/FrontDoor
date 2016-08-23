package vn.com.tma.model;

public class StateSensor {
	int id;
	String date;
	String time;
	String state;

	public StateSensor() {
		super();
	}

	public StateSensor(int id, String date, String time, String state) {
		super();
		this.id = id;
		this.date = date;
		this.time = time;
		this.state = state;
	}

	public StateSensor(String date, String time, String state) {
		super();
		this.date = date;
		this.time = time;
		this.state = state;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return id + "/ " + date + " | " + time + " | " + state;
	}

}
