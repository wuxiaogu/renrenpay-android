package wy.experiment.xposed.db.model;

import java.util.List;

/**
 * Created by chenxinyou on 2019/3/21.
 */

public class AppsList {
    private int count;
    private List<Apps> rows;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Apps> getRows() {
        return rows;
    }

    public void setRows(List<Apps> rows) {
        this.rows = rows;
    }
}
