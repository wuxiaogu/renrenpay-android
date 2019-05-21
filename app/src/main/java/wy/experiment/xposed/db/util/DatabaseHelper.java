package wy.experiment.xposed.db.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.sql.SQLException;

import wy.experiment.xposed.db.model.Logs;
import wy.experiment.xposed.db.model.User;
import wy.experiment.xposed.view.AppContext;


/**
 * Created by chenxinyou on 2019/3/9.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    public static final String DATABASE_TEMP = "/data/data/wy.experiment.xposed/databases/main";
    private static final String DATABASE_NAME = "payPhone";
    private static DatabaseHelper dbHelper;
    private boolean mainTmpDirSet = false;

    public static DatabaseHelper getDbHelper() {

        if (dbHelper == null) {

            synchronized (DatabaseHelper.class) {

                if (dbHelper == null)
                    dbHelper = new DatabaseHelper(AppContext.getInstance(),
                            DATABASE_NAME, null, 3);
            }
        }

        return dbHelper;
    }

    public DatabaseHelper(Context context, String databaseName,
                          SQLiteDatabase.CursorFactory factory, int databaseVersion) {

        super(context, databaseName, factory, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Logs.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource, User.class,true);
            TableUtils.dropTable(connectionSource, Logs.class,true);
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Logs.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {

        SQLiteDatabase dbInstance = super.getWritableDatabase();

        if (!mainTmpDirSet) {

            new File(DATABASE_TEMP).mkdir();

            dbInstance.execSQL("PRAGMA temp_store_directory = '"
                    + DATABASE_TEMP + "'");
            mainTmpDirSet = true;
        }

        return dbInstance;
    }
}
