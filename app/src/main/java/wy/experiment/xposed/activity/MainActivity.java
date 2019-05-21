package wy.experiment.xposed.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.alibaba.fastjson.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import wy.experiment.xposed.R;
import wy.experiment.xposed.adapter.FragmentAdapter;
import wy.experiment.xposed.base.BaseActivity;
import wy.experiment.xposed.bean.EventBus;
import wy.experiment.xposed.bean.EventHandler;
import wy.experiment.xposed.bean.LocalEvent;
import wy.experiment.xposed.db.ApiResponse;
import wy.experiment.xposed.db.model.Logs;
import wy.experiment.xposed.db.model.Order;
import wy.experiment.xposed.db.model.User;
import wy.experiment.xposed.db.util.LogDao;
import wy.experiment.xposed.fragment.LogFragment;
import wy.experiment.xposed.fragment.StatisticsFragment;
import wy.experiment.xposed.fragment.UserFragment;
import wy.experiment.xposed.hook.QrBean;
import wy.experiment.xposed.hook.wechat.WechatHook;
import wy.experiment.xposed.net.HttpResHandler;
import wy.experiment.xposed.net.NetHelper;
import wy.experiment.xposed.service.ServiceMain;
import wy.experiment.xposed.service.ServiceProtect;
import wy.experiment.xposed.service.UploadLogService;
import wy.experiment.xposed.tool.ToastTool;
import wy.experiment.xposed.utils.NoScrollViewPager;
import wy.experiment.xposed.utils.UIHelper;
import wy.experiment.xposed.utils.XPConstant;
import wy.experiment.xposed.view.AppContext;
import wy.experiment.xposed.websocket.AddNewOrderInterface;
import wy.experiment.xposed.websocket.SocketService;

public class MainActivity extends BaseActivity implements EventHandler, BottomNavigationView.OnNavigationItemSelectedListener, AddNewOrderInterface {
    @BindView(R.id.navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.main_pager)
    NoScrollViewPager mainPager;

    private FragmentAdapter mAdapter;
    private List<Fragment> fragments;
    private int currentTab = 0;
    private static User user;

    private String TAG = "cxyMain";
    private static Map<String, Order> orders = new HashMap<>();
    private SocketService.SocketBinder socketBinder = new SocketService.SocketBinder();

    private ServiceConnection sConnection = new ServiceConnection() {
        //重写onServiceConnected()方法和onServiceDisconnected()方法
        // 在Activity与Service建立关联和解除关联的时候调用
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
        //

        // 在Activity与Service解除关联的时候调用
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //实例化Service的内部类myBinder
            // 通过向下转型得到了MyBinder的实例
            socketBinder = (SocketService.SocketBinder) service;
            //在Activity调用Service类的方法
            socketBinder.service_connect_Activity();
            socketBinder.addNewOrderInterface(MainActivity.this);
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_mains;
    }

    @Override
    protected void initActivity() {
        super.initActivity();
    }

    @Override
    public int getToolBarId() {
        return R.id.main_toolbar;
    }

    @Override
    public void initView() {
        mainPager.setScroll(false);
        addFragment();
        addModels();
    }

    @Override
    public void initData() {
        currentTab = 0;
        mainPager.setCurrentItem(currentTab);
        mAdapter.notifyDataSetChanged();
        user = AppContext.getInstance().getUser();
        if (user == null) {
//            new Thread(new SocketService(user.getApp_id())).start();
            ToastTool.showToast("未设置应用");
        } else {
//            ToastTool.showToast("未选择应用");
            Intent intent = new Intent(getApplicationContext(), SocketService.class);
            getApplicationContext().bindService(intent, sConnection, BIND_AUTO_CREATE);
            getApplicationContext().startService(intent);
        }
        getPermissions();
        //有的手机就算已经静态注册服务还是不行启动，我再手动启动一下吧。
        startService(new Intent(this, ServiceMain.class));
        startService(new Intent(this, ServiceProtect.class));
        startService(new Intent(this, UploadLogService.class));
        LogDao.add(new Logs(user.getUserid(), "程序打开", System.currentTimeMillis(), XPConstant.INFO));
    }

    private void addFragment() {
        fragments = new ArrayList<>();
        fragments.add(new UserFragment());
        fragments.add(new StatisticsFragment());
        fragments.add(new LogFragment());

        mAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        mainPager.setAdapter(mAdapter);
        mainPager.setCurrentItem(currentTab);
        mAdapter.notifyDataSetChanged();
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        EventBus.getDefault().add(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        LogDao.add(new Logs(user.getUserid(), "程序被关闭", System.currentTimeMillis(), XPConstant.ERROR));
        EventBus.getDefault().remove(this);
    }

    @Override
    protected boolean hasBackClick() {
        return false;
    }

    private void addModels() {
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public void onEvent(LocalEvent event) {
        if (event.getEventType() == LocalEvent.RECEIVE_WECHAT_MSG) {
            QrBean qrBean = (QrBean) event.getEventData();
            Log.d(TAG, "wechat " + qrBean.toString());
            Logs logs = new Logs(user.getUserid(), "收到微信的付款；" + qrBean.toString(), System.currentTimeMillis(), XPConstant.DEBUG);
            LogDao.add(logs);
            NetHelper.paySuccessNotify(2, qrBean.getMoney(), qrBean.getMark_sell(), qrBean.getOrder_id(), new HttpResHandler() {
                @Override
                public void onFailure(int errorCode, String data) {
                    Log.d(TAG, "errorCode -> " + errorCode + "; data -> " + data);
                    Logs logsFailure = new Logs(user.getUserid(), "上传微信收钱信息失败 errorCode -> " + errorCode, System.currentTimeMillis(), XPConstant.ERROR);
                    LogDao.add(logsFailure);
                }

                @Override
                public void onSuccess(ApiResponse res) {
                    Log.d(TAG, "支付成功");
                    Logs logsFailure = new Logs(user.getUserid(), "上传微信收钱信息成功", System.currentTimeMillis(), XPConstant.INFO);
                    LogDao.add(logsFailure);
                }

                @Override
                public void onError(String msg) {
                    super.onError(msg);
                    Log.d(TAG, msg);
                    Logs logsFailure = new Logs(user.getUserid(), "上传微信收钱信息错误 -> " + msg, System.currentTimeMillis(), XPConstant.ERROR);
                    LogDao.add(logsFailure);

                }
            });
        } else if (event.getEventType() == LocalEvent.RECEIVE_ALIPAY_MSG) {
            QrBean qrBean = (QrBean) event.getEventData();
            Log.d(TAG, "alipay " + qrBean.toString());
            Logs logs = new Logs(user.getUserid(), "收到支付宝的付款；" + qrBean.toString(), System.currentTimeMillis(), XPConstant.DEBUG);
            LogDao.add(logs);
            NetHelper.paySuccessNotify(1, qrBean.getMoney(), qrBean.getMark_sell(), qrBean.getOrder_id(), new HttpResHandler() {

                @Override
                public void onFailure(int errorCode, String data) {
                    Log.d(TAG, "errorCode -> " + errorCode + "; data -> " + data);
                    Logs logsFailure = new Logs(user.getUserid(), "上传支付宝收钱信息失败 errorCode -> " + errorCode, System.currentTimeMillis(), XPConstant.ERROR);
                    LogDao.add(logsFailure);
                }

                @Override
                public void onSuccess(ApiResponse res) {
                    Log.d(TAG, "支付成功");
                    Logs logsFailure = new Logs(user.getUserid(), "上传支付宝收钱信息成功", System.currentTimeMillis(), XPConstant.INFO);
                    LogDao.add(logsFailure);
                }

                @Override
                public void onError(String msg) {
                    super.onError(msg);
                    Log.d(TAG, msg);
                    Logs logsFailure = new Logs(user.getUserid(), "上传支付宝收钱信息失败 msg -> " + msg, System.currentTimeMillis(), XPConstant.ERROR);
                    LogDao.add(logsFailure);

                }
            });
        } else if (event.getEventType() == LocalEvent.SEND_WECHAT_MSG) {
            QrBean qrBean = (QrBean) event.getEventData();
            Order order = orders.get(qrBean.getMark_sell());
            Log.d(TAG, order.toString());
            Logs logs = new Logs(user.getUserid(), "上传微信付款码；" + order.toString(), System.currentTimeMillis(), XPConstant.DEBUG);
            LogDao.add(logs);
            NetHelper.receivePayCode(qrBean.getUrl(), order.getOrderid(), order.getAppid(), qrBean.getMoney(), 2, qrBean.getMark_sell(), user.getUserid() + "", new HttpResHandler() {

                @Override
                public void onFailure(int errorCode, String data) {
                    Log.d(TAG, "errorCode -> " + errorCode + "; data -> " + data);
                    Logs logs = new Logs(user.getUserid(), "失败；", System.currentTimeMillis(), XPConstant.ERROR);
                    LogDao.add(logs);
                }

                @Override
                public void onSuccess(ApiResponse res) {
                    Log.d(TAG, "上传支付二维码成功, " + order.toString());
                    Logs logs = new Logs(user.getUserid(), "上传支付二维码成功；", System.currentTimeMillis(), XPConstant.INFO);
                    LogDao.add(logs);
                }
            });
        } else if (event.getEventType() == LocalEvent.HOOK_MESSAGE) {
            Logs logs = new Logs(user.getUserid(), "Hook Message -> " + event.getEventMsg(), System.currentTimeMillis(), XPConstant.ERROR);
            LogDao.add(logs);
        } else if (event.getEventType() == LocalEvent.RUN_TASK_LOG) {
            List<Logs> logsList = LogDao.getLogsUpload();
            Log.d(TAG, "logsList -> " + JSONObject.toJSONString(logsList));
            if(logsList == null || logsList.size() == 0) {
                Log.d(TAG, "not upload logs");
                return;
            }
            NetHelper.paySetLogs(JSONObject.toJSONString(logsList), new HttpResHandler() {
                @Override
                public void onFailure(int errorCode, String data) {
                    Log.d(TAG, "上传日志失败 -> " + errorCode);
                    Logs logs = new Logs(user.getUserid(), "上传日志失败 -> " + errorCode, System.currentTimeMillis(), XPConstant.ERROR);
                    LogDao.add(logs);
                }

                @Override
                public void onSuccess(ApiResponse res) {
                    Log.d(TAG, "上传日志成功");
                    for (Logs logs : logsList) {
                        logs.setUpload(true);
                        LogDao.motifyStatus(logs);
                    }
                }

                @Override
                public void onError(String msg) {
                    Log.d(TAG, "上传日志错误 -> " + msg);
                    Logs logs = new Logs(user.getUserid(), "上传日志错误 -> " + msg, System.currentTimeMillis(), XPConstant.ERROR);
                    LogDao.add(logs);
                }
            });
        } else if(event.getEventType() == LocalEvent.NET_WORK_CHANGE) {
            Logs logs;
            if(event.getEventValue() == 1) {
                logs = new Logs(user.getId(),"网络已连接", System.currentTimeMillis(),  XPConstant.INFO);

            } else {
                logs = new Logs(user.getId(),"网络已断开", System.currentTimeMillis(), XPConstant.ERROR);
            }
            LogDao.add(logs);
        }
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
//                Log.d(TAG, "clock exit");

                AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("退出登陆").setMessage("是否要退出登陆").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        ToastTool.showToast("退出登陆");
                        AppContext.getInstance().userLogin(0);
                        AppContext.getInstance().getAccountManager().exit();
                        UIHelper.showLoginView(MainActivity.this);
                        Logs logs = new Logs(user.getUserid(), "退出登陆", System.currentTimeMillis(), XPConstant.INFO);
                        LogDao.add(logs);
                        MainActivity.this.finish();
                    }
                }).setNegativeButton("取消", null).create();
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                currentTab = 0;
                mainPager.setCurrentItem(currentTab);
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.navigation_dashboard:
                currentTab = 1;
                mainPager.setCurrentItem(currentTab);
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.navigation_notifications:
                currentTab = 2;
                mainPager.setCurrentItem(currentTab);
                mAdapter.notifyDataSetChanged();
                return true;
        }
        return false;
    }

    @Override
    public void addNewData(String s) {
        Log.d(TAG, s);
        Logs logs = new Logs(user.getUserid(), "收到websocket消息" + s, System.currentTimeMillis(), XPConstant.DEBUG);
        LogDao.add(logs);
        try {
            Order order = JSONObject.parseObject(s, Order.class);
            if (order.getPayway() == 1 || order.getPayway() == 2) {
                if (order.getPayway() == 2) {
                    orders.put(order.getPay_code(), order);
                    WechatHook.getInstance().creatQrTask(order.getAmount(), order.getPay_code());
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Json 解析错误!!!");
        }
    }

    /**
     * 获取权限。。有些手机很坑，明明是READ_PHONE_STATE权限，却问用户是否允许拨打电话，汗。
     */
    private void getPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        List<String> sa = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请READ_PHONE_STATE权限。。。。
            sa.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            sa.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            sa.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (sa.size() < 1) {
            return;
        }
        ActivityCompat.requestPermissions(this, sa.toArray(new String[]{}), 1);
    }
}
