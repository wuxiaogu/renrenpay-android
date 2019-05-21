package wy.experiment.xposed.db.model;

import java.util.List;

public class OrderResList {
    private List<Order> rows;
    private int count;

    public List<Order> getRows() {
        return rows;
    }

    public void setRows(List<Order> rows) {
        this.rows = rows;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
