package wy.experiment.xposed.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import wy.experiment.xposed.R;
import wy.experiment.xposed.adapter.AppsAdapter;
import wy.experiment.xposed.base.BaseFragment;
import wy.experiment.xposed.bean.EventHandler;
import wy.experiment.xposed.bean.LocalEvent;
import wy.experiment.xposed.db.ApiResponse;
import wy.experiment.xposed.db.model.Apps;
import wy.experiment.xposed.db.model.AppsList;
import wy.experiment.xposed.db.model.User;
import wy.experiment.xposed.net.HttpResHandler;
import wy.experiment.xposed.net.NetHelper;
import wy.experiment.xposed.tool.ToastTool;
import wy.experiment.xposed.utils.APKVersionCodeUtils;
import wy.experiment.xposed.view.AppContext;

/**
 * Created by chenxinyou on 2019/2/26.
 */

public class UserFragment extends BaseFragment implements EventHandler {

    @BindView(R.id.user_nickname)
    TextView userNickname;
    @BindView(R.id.user_mail)
    TextView userMail;
    @BindView(R.id.user_amount)
    TextView userAmount;
    @BindView(R.id.app_versions)
    TextView appVersion;
    @BindView(R.id.img_vip_lv)
    ImageView imgVipLv;
    @BindView(R.id.app_list)
    RecyclerView appList;

    private User user;
    private List<Apps> apps = new ArrayList<>();
    private String TAG = "cxyUser";
    private AppsAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_users;
    }

    @Override
    public void initData() {
        userNickname.setText(user.getNickname().isEmpty() ? "未设置" : user.getNickname());
        userMail.setText(user.getEmail() == null ? "未设置邮箱" : user.getEmail());
        userAmount.setText((user.getBalance() * 0.01f) + "");
        Drawable bitmap = user.getLv() == 2 ? getResources().getDrawable(R.mipmap.vip_gold) : user.getLv() == 1 ? getResources().getDrawable(R.mipmap.vip_silver) : null;
        imgVipLv.setImageDrawable(bitmap);
        String versionName = APKVersionCodeUtils.getVerName(AppContext.getInstance());
        appVersion.setText(versionName);
    }

    @Override
    public void initView(View view) {
        user = AppContext.getInstance().getUser();
        getAppsData();
    }

    private void getAppsData() {
        NetHelper.getAppList(new HttpResHandler() {
            @Override
            public void onFailure(int errorCode, String data) {
                Log.d(TAG, "errorCode -> " + errorCode);
            }

            @Override
            public void onSuccess(ApiResponse res) {
                AppsList appsList = JSONObject.parseObject(res.getData().toString(), AppsList.class);
                apps = appsList.getRows();
                adapter = new AppsAdapter(apps);
                appList.setAdapter(adapter);
                LinearLayoutManager layoutManager = new LinearLayoutManager(AppContext.getInstance());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                appList.setLayoutManager(layoutManager);
            }
        });
    }

    @OnClick(R.id.btn_clear_code)
    public void clickClearCode() {
        android.app.AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setTitle("清除付款码").setMessage("如果切换微信请清除付款码！！").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NetHelper.clearWxPayCode(new HttpResHandler() {
                    @Override
                    public void onFailure(int errorCode, String data) {
                        Log.d(TAG, "onFailure errorCode -> " + errorCode);
                    }

                    @Override
                    public void onSuccess(ApiResponse res) {
                        Log.d(TAG, "成功");
                        ToastTool.showToast("清除付款码成功");
                    }

                    @Override
                    public void onError(String msg) {
                        Log.d(TAG, "onError -> " + msg);
                    }
                });
            }
        }).setNegativeButton("取消", null).create();
        alertDialog.show();

    }

    @Override
    public void onEvent(LocalEvent event) {
    }
}
