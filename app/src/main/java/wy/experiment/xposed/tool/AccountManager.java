package wy.experiment.xposed.tool;

import android.util.Log;

import com.scottyab.aescrypt.AESCrypt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import wy.experiment.xposed.db.model.User;
import wy.experiment.xposed.view.AppContext;

/**
 * Created by chenxinyou on 2019/3/13.
 */

public class AccountManager {
    private String phone;
    private int userId;
    private String token = "";
    private boolean logined;
    private File accountIni;
    private long lastLoginTime;
    private String aesSeed = "payPhone";

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isLogined() {
        return logined;
    }

    public void setLogined(boolean logined) {
        this.logined = logined;
    }

    @Override
    public String toString() {
        return "userId -> " + userId + "; phone -> " + phone + ";login -> " + (logined ? "登陆" : "未登陆");
    }

    public AccountManager() {

        accountIni = new File(AppContext.getInstance().getFilesDir()
                .getAbsolutePath() + File.separator + "push.ini");

        if (checkFile())
            readInfo();
    }

    public synchronized void save(int id, String phoneNum, String token) {

        checkFile();

        BufferedWriter out = null;

        try {

            out = new BufferedWriter(new FileWriter(accountIni));

            logined = true;
            lastLoginTime = System.currentTimeMillis();
            this.userId = id;
            this.phone = phoneNum;
            this.token = token;
            Log.d("cxy", "userId -> " + id);

            Random rand = new Random();
            StringBuilder result = new StringBuilder();

            for (int i = 0; i < 10; i++) {

                String reandDouble = String.valueOf(rand.nextDouble());
                result.append(AESCrypt.encrypt(aesSeed, Base64.encode(reandDouble.substring(2, reandDouble.length() - 1))));
                result.append("\r\n");
            }

            result.append(Base64.encode("userid=" + id));
            result.append("\r\n");
            result.append(Base64.encode("serverUid=PGdfjFDSIJGfkds489462"));
            result.append("\r\n");
            result.append(Base64.encode(String.valueOf(lastLoginTime)));
            result.append("\r\n");
            result.append(Base64.encode("use payPhone server"));
            result.append("\r\n");
            result.append(Base64.encode(phoneNum));
            result.append("\r\n");
            result.append(token);
            result.append("\r\n");

            for (int i = 0; i < 12; i++) {

                String reandDouble = String.valueOf(rand.nextDouble());
                result.append(Base64.encode(reandDouble.substring(2, reandDouble.length() - 1)));
                result.append("\r\n");
            }

            out.write(result.toString());
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
            }
        }
    }

    private boolean checkFile() {

        if (!accountIni.exists()) {

            BufferedWriter out = null;

            try {

                accountIni.createNewFile();
                out = new BufferedWriter(new FileWriter(accountIni));

                Random rand = new Random();
                StringBuilder result = new StringBuilder();

                for (int i = 0; i < 30; i++) {

                    String reandDouble = String.valueOf(rand.nextDouble());
                    result.append(Base64.encode(reandDouble.substring(2, reandDouble.length() - 1)));
                    result.append("\r\n");
                }
                out.append(result.toString());
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null)
                        out.close();
                } catch (IOException e) {
                }
            }
            return false;
        }

        return true;
    }

    private boolean readInfo() {

        BufferedReader in = null;

        try {

            in = new BufferedReader(new FileReader(accountIni));

            String line;
            int lineCount = 0;
            while ((line = in.readLine()) != null) {

                if (lineCount++ < 10)
                    continue;
                if (lineCount <= 15)
                    line = Base64.decode(line);

                if (line.contains("userid") && line.contains("=")) {
                    userId = Integer.valueOf(line.split("=")[1]);
                    continue;
                } else if (line.contains("serverUid") && line.contains("=")) {
                    continue;
                }
                if (lineCount == 13)
                    lastLoginTime = Long.valueOf(line);
                else if (lineCount == 14)
                    logined = line.contains(aesSeed);
                else if (lineCount == 15)
                    phone = line;
                else if (lineCount == 16)
                    token = line;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
            }
        }
    }

    public synchronized void exit() {

        checkFile();

        BufferedWriter out = null;

        try {

            out = new BufferedWriter(new FileWriter(accountIni));

            Random rand = new Random();
            StringBuilder result = new StringBuilder();

            for(int i = 0; i < 10; i++) {

                String reandDouble = String.valueOf(rand.nextDouble());
                result.append(Base64.encode(reandDouble.substring(2, reandDouble.length() - 1)));
                result.append("\r\n");
            }

            result.append(Base64.encode("userid=" + 0));
            result.append("\r\n");
            result.append(Base64.encode("serverUid=" + 0));
            result.append("\r\n");
            result.append(Base64.encode(String.valueOf(lastLoginTime)));
            result.append("\r\n");
            result.append("you exited the app");
            result.append("\r\n");
            result.append("");
            result.append("\r\n");
            result.append("");
            result.append("\r\n");


            for(int i = 0; i < 13; i++) {

                String reandDouble = String.valueOf(rand.nextDouble());
                result.append(Base64.encode(reandDouble.substring(2, reandDouble.length() - 1)));
                result.append("\r\n");
            }

            out.write(result.toString());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(out != null)
                    out.close();
            } catch (IOException e) {}
        }

        logined = false;

        AppContext.getInstance().setUser(null);

    }
}
