package fpt.vulq.ass2adr2;

public class Gratitude {
    private int id;
    private String content;
    private long date;
    private int userId;

    public Gratitude(int id, String content, long date, int userId) {
        this.id = id;
        this.content = content;
        this.date = date;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public long getDate() {
        return date;
    }

    public int getUserId() {
        return userId;
    }

}
