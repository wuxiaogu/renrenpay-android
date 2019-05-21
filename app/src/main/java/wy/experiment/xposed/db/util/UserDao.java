package wy.experiment.xposed.db.util;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import wy.experiment.xposed.db.model.User;
/**
 * Created by chenxinyou on 2019/3/9.
 */

public class UserDao {
    public static User add(User user) {

        User res = null;
        try {
            Dao<User, Integer> dao = DatabaseHelper.getDbHelper().getDao(User.class);
            User old = dao.queryBuilder().where().eq("mobile_phone", user.getMobile_phone()).queryForFirst();
            if(old == null) {
                dao.create(user);
                res = user;
            } else {
                old.setToken(user.getToken());
                old.setUserid(user.getUserid());
                dao.update(old);
                res = old;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static boolean delete(User user) {
        try {
            Dao<User, Integer> dao = DatabaseHelper.getDbHelper().getDao(User.class);

            return dao.delete(user) > 0;
        } catch(SQLException e) {
//            ExceptionUtils.report(e);
            e.printStackTrace();
            return false;
        }
    }

    public static boolean delete(int id) {
        try {
            Dao<User, Integer> dao = DatabaseHelper.getDbHelper().getDao(User.class);
            return dao.deleteById(id) > 0;
        } catch(SQLException e) {
//            ExceptionUtils.report(e);
            e.printStackTrace();
            return false;
        }
    }

    public static User findById(int id) {
        User user = null;
        try {
            Dao<User, Integer> dao = DatabaseHelper.getDbHelper().getDao(User.class);
            user = dao.queryForId(id);
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static void update(User user) {
        try {
            Dao<User, Integer> dao = DatabaseHelper.getDbHelper().getDao(User.class);
            dao.update(user);
        } catch(SQLException e) {
//            ExceptionUtils.report(e);
            e.printStackTrace();
        }
    }
}
