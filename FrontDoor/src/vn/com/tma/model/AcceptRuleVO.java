package vn.com.tma.model;

public class AcceptRuleVO {
    private int id;

    private String name;

    private String from;

    private String to;

    private boolean isChecked;

    public AcceptRuleVO() {
        super();
    }

    public AcceptRuleVO(int id, String name, String from, String to, boolean isChecked) {
        super();
        this.id = id;
        this.name = name;
        this.from = from;
        this.to = to;
        this.isChecked = isChecked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    @Override
    public String toString() {
        return id + "|" + name + "|" + from + "|" + to + "|" + isChecked;
    }

}
