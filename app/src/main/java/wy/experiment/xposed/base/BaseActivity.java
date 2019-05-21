package wy.experiment.xposed.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.ButterKnife;
import wy.experiment.xposed.R;
import wy.experiment.xposed.tool.CommonTool;
import wy.experiment.xposed.view.AppManager;

/**
 * Created by chenxinyou on 2019/2/26.
 */

public class BaseActivity extends AppCompatActivity implements IBaseActivity {
    protected LayoutInflater mInflater;
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivity();
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        // 通过注解绑定控件
        ButterKnife.bind(this);
        mInflater = getLayoutInflater();
        AppManager.getAppManager().addActivity(this);

        if(getToolBarId() != 0) {
            mToolbar = findViewById(getToolBarId());
            setSupportActionBar(mToolbar);
//            getSupportActionBar().setDisplayShowTitleEnabled(false);
            if(hasBackClick()) {
                mToolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.toolbar_back_icon));
                mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CommonTool.HideKb(BaseActivity.this, getCurrentFocus());
                        onBackPressed();
                    }
                });
            }
        }
        init(savedInstanceState);
        initView();
        initData();
    }

    protected boolean hasBackClick() {
        return true;
    }

    protected void initActivity() {
    }

    protected void init(Bundle savedInstanceState) {
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    public int getToolBarId() {
        return 0;
    }
}
