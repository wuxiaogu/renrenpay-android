package wy.experiment.xposed.utils;

import android.content.Context;
import android.content.SharedPreferences;

import wy.experiment.xposed.view.AppContext;


/**
 * Created by chenxinyou on 2019/3/9.
 */

public class ConfigManager {

    private static ConfigManager cfgManager;
    private SharedPreferences configurationInfo;
    private SharedPreferences.Editor save;

    public static final String KEY_INIT_FLAG = "initialFlag";

    public static final String KEY_VERSION = "curr_version";
    public static final String KEY_SHOW_WELCOME = "show_welcome";

    private static final String CONFIG_NAME = "config_file";

    public static ConfigManager getInstance() {

        if(cfgManager == null) {
            cfgManager = new ConfigManager(AppContext.getInstance());
        }
        return cfgManager;
    }

    private ConfigManager(Context context) {
        configurationInfo = context.getSharedPreferences(CONFIG_NAME,
                Context.MODE_PRIVATE);
        save = configurationInfo.edit();

        int initialFlag = configurationInfo.getInt(KEY_INIT_FLAG, -1);

        if (initialFlag == -1) {
            initAppCfg();
        }
    }

    public static String getVersion(Context context) {

        SharedPreferences sp = context.getSharedPreferences(CONFIG_NAME,
                Context.MODE_PRIVATE);

        return sp.getString(KEY_VERSION, "null");
    }

    public static void putVersion(Context context, String version) {

        SharedPreferences sp = context.getSharedPreferences(CONFIG_NAME,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_VERSION, version);
        editor.commit();
    }

    public static void putString(Context context, String key, String value) {

        SharedPreferences sp = context.getSharedPreferences(CONFIG_NAME,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG_NAME,
                Context.MODE_PRIVATE);

        return sp.getBoolean(key, true);
    }

    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG_NAME,
                Context.MODE_PRIVATE);

        return sp.getString(key, "");
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG_NAME,
                Context.MODE_PRIVATE);

        return sp.getBoolean(key, defaultValue);
    }

    public static void setWelcomeEnable(Context context, boolean enable) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG_NAME,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(KEY_SHOW_WELCOME, enable);
        editor.commit();
    }

    public static boolean getWelcomeEnable(Context context) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG_NAME,
                Context.MODE_PRIVATE);

        return sp.getBoolean(KEY_SHOW_WELCOME, true);
    }

    public void initAppCfg() {

    }

    /**
     * Description 从配置文件中写入String型值
     * @param key 键
     * @param value 值
     */
    public void putString(String key, String value) {
        save.putString(key, value);
        save.commit();
    }

    /**
     * Description 从配置文件中写入int型值
     * @param key 键
     * @param value 值
     */
    public void putInt(String key, int value) {
        save.putInt(key, value);
        save.commit();
    }

    /**
     * Description 从配置文件中写入long型值
     * @param key 键
     * @param value 值
     */
    public void putLong(String key, long value) {
        save.putLong(key, value);
        save.commit();
    }

    /**
     * Description 从配置文件中写入boolean型值
     * @param key 键
     * @param value 值
     */
    public void putBoolean(String key,boolean value){
        save.putBoolean(key, value);
        save.commit();
    }

    /**
     * Description 从配置文件中获取int型值
     * @param key 键
     * @return 返回键对应的值，没有对应值则返回-1
     */
    public int getInt(String key) {
        return configurationInfo.getInt(key, -1);
    }

    /**
     * Description 从配置文件中获取int型值
     * @param key 键
     * @return 返回键对应的值，没有对应值则返回-1
     */
    public int getInt(String key, int defaultValue) {
        return configurationInfo.getInt(key, defaultValue);
    }

    /**
     * Description 从配置文件中获取long型值
     * @param key 键
     * @return 返回键对应的值，没有对应值则返回-1
     */
    public long getLong(String key) {
        return configurationInfo.getLong(key, -1);
    }

    /**
     * Description 从配置文件中获取String型值
     * @param key 键
     * @return 返回键对应的值，没有对应值则返回空字符串
     */
    public String getString(String key) {
        return configurationInfo.getString(key, "");
    }

    public Boolean getBoolean(String key){
        return configurationInfo.getBoolean(key, false);
    }

    public Boolean getBoolean(String key, boolean defaultValue){
        return configurationInfo.getBoolean(key, defaultValue);
    }

    /**
     * Description 从配置文件中移除某个键值对
     * @param key 键名
     */
    public void Remove(String key){
        save.remove(key);
        save.commit();
    }
}
