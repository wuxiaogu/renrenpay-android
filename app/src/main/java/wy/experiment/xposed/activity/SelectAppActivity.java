package wy.experiment.xposed.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import wy.experiment.xposed.R;
import wy.experiment.xposed.adapter.AppsAdapter;
import wy.experiment.xposed.base.BaseActivity;
import wy.experiment.xposed.db.ApiResponse;
import wy.experiment.xposed.db.model.Apps;
import wy.experiment.xposed.db.model.ResList;
import wy.experiment.xposed.db.model.User;
import wy.experiment.xposed.net.HttpResHandler;
import wy.experiment.xposed.net.NetHelper;
import wy.experiment.xposed.tool.ToastTool;
import wy.experiment.xposed.utils.UIHelper;
import wy.experiment.xposed.view.AppContext;

/**
 * Created by chenxinyou on 2019/3/8.
 */

public class SelectAppActivity extends BaseActivity {

    @BindView(R.id.select_app)
    RecyclerView recyclerView;
    private List<Apps> apps = new ArrayList<>();
    private AppsAdapter adapter;
    private Apps selectApp;
    private User user;
    @Override
    public int getLayoutId() {
        return R.layout.activity_select_app;
    }

    @Override
    public void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public int getToolBarId() {
        return R.id.select_toolbar;
    }

    @Override
    public void initData() {
        user = AppContext.getInstance().getUser();
        initNet();
    }

    private void initNet() {
        NetHelper.getAppList(new HttpResHandler() {
            @Override
            public void onFailure(int errorCode, String data) {
                ToastTool.showToast("获取app列表失败 code -> " + errorCode);
            }

            @Override
            public void onSuccess(ApiResponse res) {
                ToastTool.showToast("获取列表成功");
                ResList datas = JSONObject.parseObject(res.getData().toString(), ResList.class);
                apps = datas.getRows();
                adapter = new AppsAdapter(apps);
                recyclerView.setAdapter(adapter);
                adapter.setItemListener(new AppsAdapter.onRecyclerItemClickerListener() {
                    @Override
                    public void onRecyclerItemClick(View view, Object data, int position) {
                        clearSelect(apps);
                        selectApp = apps.get(position);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_save:
//                user.setApp_id(selectApp.getApp_id());
//                user.setApp_secret(selectApp.getApp_secret());
//                user.setApp_status(selectApp.getStatus());
//                user.setReceipt_limit(selectApp.getReceipt_limit());
//                user.setIp_white_list(selectApp.getIp_white_list());
//                user.setApp_name(selectApp.getName());
//                user.setSelected_app(true);
                AppContext.getInstance().updateUser(user);
                UIHelper.showMainView(SelectAppActivity.this);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void clearSelect(List<Apps> apps) {
        for (Apps item: apps) {
        }
    }
}
