package wy.experiment.xposed.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import wy.experiment.xposed.R;
import wy.experiment.xposed.tool.AccountManager;
import wy.experiment.xposed.utils.UIHelper;
import wy.experiment.xposed.view.AppContext;


/**
 * Created by chenxinyou on 2019/2/26.
 */

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        delayRedirect();
    }

    private void delayRedirect() {
        AccountManager aManager = AppContext.getInstance().getAccountManager();
        redirectTo(aManager.isLogined());
    }

    /**
     * 跳转到...
     */
    private void redirectTo(boolean isLogin) {

        if(isLogin)
            UIHelper.showMainView(WelcomeActivity.this);
        else
            UIHelper.showLoginView(WelcomeActivity.this);
//        AppManager.getAppManager().finishActivity();
        finish();
    }
}
