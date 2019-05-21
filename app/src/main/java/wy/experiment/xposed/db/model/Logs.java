package wy.experiment.xposed.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "logs")
public class Logs {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "user_id")
    private int userId;

    @DatabaseField(columnName = "content")
    private String content;

    @DatabaseField(columnName = "time")
    private Long time;

    @DatabaseField(columnName = "upload")
    private boolean upload = false;

    @DatabaseField(columnName = "log_lv")
    private int logLv = 0;

    public int getId() {
        return id;
    }

    public Logs(int userId, String content, Long time, int logLv) {
        this.userId = userId;
        this.content = content;
        this.time = time;
        this.logLv = logLv;
    }

    public Logs(String content, Long time, int logLv) {
        this.content = content;
        this.time = time;
        this.logLv = logLv;
    }

    public Logs() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public boolean isUpload() {
        return upload;
    }

    public void setUpload(boolean upload) {
        this.upload = upload;
    }

    public int getLogLv() {
        return logLv;
    }

    public void setLogLv(int logLv) {
        this.logLv = logLv;
    }

    @Override
    public String toString() {
        return "content -> " + content;
    }
}
