package wy.experiment.xposed.db.util;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import wy.experiment.xposed.db.model.Logs;

public class LogDao {
    public static void add(Logs logs) {
        try {
            Dao<Logs, Integer> dao = DatabaseHelper.getDbHelper().getDao(Logs.class);
            dao.create(logs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Logs> getLogs(int userId) {
        List<Logs> res = new ArrayList<>();
        try {
            Dao<Logs, Integer> dao = DatabaseHelper.getDbHelper().getDao(Logs.class);
            res = dao.queryBuilder().orderBy("id", false).where().eq("user_id", userId).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static List<Logs> getLogsUpload() {
        List<Logs> res = new ArrayList<>();
        try {
            Dao<Logs, Integer> dao = DatabaseHelper.getDbHelper().getDao(Logs.class);
            res = dao.queryBuilder().orderBy("id", false).limit(100).where().eq("upload", false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static void motifyStatus(Logs log) {
        try {
            Dao<Logs, Integer> dao = DatabaseHelper.getDbHelper().getDao(Logs.class);
            dao.update(log);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
