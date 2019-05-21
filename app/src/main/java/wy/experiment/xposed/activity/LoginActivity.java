package wy.experiment.xposed.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import wy.experiment.xposed.R;
import wy.experiment.xposed.base.BaseActivity;
import wy.experiment.xposed.db.ApiResponse;
import wy.experiment.xposed.db.model.Apps;
import wy.experiment.xposed.db.model.AppsList;
import wy.experiment.xposed.db.model.Logs;
import wy.experiment.xposed.db.model.User;
import wy.experiment.xposed.db.util.LogDao;
import wy.experiment.xposed.db.util.UserDao;
import wy.experiment.xposed.net.HttpResHandler;
import wy.experiment.xposed.net.NetHelper;
import wy.experiment.xposed.tool.Md5Utils;
import wy.experiment.xposed.tool.ToastTool;
import wy.experiment.xposed.utils.UIHelper;
import wy.experiment.xposed.utils.XPConstant;
import wy.experiment.xposed.view.AppContext;

/**
 * Created by chenxinyou on 2019/2/26.
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.btn_login)
    ImageButton btnLogin;
    @BindView(R.id.edit_username)
    EditText username;
    @BindView(R.id.edit_password)
    EditText password;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_logins;
    }

    @OnClick(R.id.btn_login)
    public void loginBtnClick() {
        Log.d("cxy", "click login");
        final String phone = username.getText().toString();
        final String pwdStr = Md5Utils.encode(this.password.getText().toString());
        NetHelper.getSalt(phone, new HttpResHandler() {
            @Override
            public void onFailure(int errorCode, String data) {
                ToastTool.showToast("获取salt失败， code:" + errorCode + "; data:" + data);
                Log.d("cxy", "获取salt失败， code:" + errorCode + "; data:" + data);
            }

            @Override
            public void onSuccess(ApiResponse res) {
                Map datas = JSON.parseObject(res.getData().toString());
                String password = Md5Utils.encode(pwdStr + datas.get("salt"));
                NetHelper.userLogin(phone, password, new HttpResHandler() {
                    @Override
                    public void onFailure(int errorCode, String data) {
                        ToastTool.showToast("登陆失败， code:" + errorCode + "; data:" + data);
                        Log.d("cxy", "登陆失败， code:" + errorCode + "; data:" + data);
                    }

                    @Override
                    public void onSuccess(ApiResponse res) {
                        ToastTool.showToast("登陆成功");
                        Map datas = JSON.parseObject(res.getData().toString());
                        User user = JSON.parseObject(datas.get("user").toString(), User.class);
                        user.setToken(datas.get("token").toString());
                        User users = UserDao.add(user);
                        Logs logs = new Logs(user.getUserid(), "用户登陆,用户id:" + user.getUserid(), System.currentTimeMillis(), XPConstant.DEBUG);
                        LogDao.add(logs);
                        Log.d("cxy", users.toString());
                        AppContext.getInstance().userLogin(users.getId());
                        AppContext.getInstance().getAccountManager().save(user.getId(), phone, user.getToken());
                        AppContext.getInstance().setUser(user);
                        UIHelper.showMainView(LoginActivity.this);
                        LoginActivity.this.finish();
                    }

                    @Override
                    public void onError(String msg) {
                        Log.d("cxy", msg);
                    }
                });
            }
        });
    }
}
