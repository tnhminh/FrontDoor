package vn.com.tma.model;

public class IgnoreRuleVO {

    private int id;

    private String option;

    private String time;

    public IgnoreRuleVO() {
        super();
    }

    public IgnoreRuleVO(int id, String option, String time) {
        super();
        this.id = id;
        this.option = option;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return id + " | " + option + " | " + time;
    }

}
