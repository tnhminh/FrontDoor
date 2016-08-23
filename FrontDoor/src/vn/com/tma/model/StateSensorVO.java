package vn.com.tma.model;

public class StateSensorVO {
    private int id;

    private String date;

    private String time;

    private String state;

    public StateSensorVO() {
        super();
    }

    public StateSensorVO(int id, String date, String time, String state) {
        super();
        this.id = id;
        this.date = date;
        this.time = time;
        this.state = state;
    }

    public StateSensorVO(String date, String time, String state) {
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
