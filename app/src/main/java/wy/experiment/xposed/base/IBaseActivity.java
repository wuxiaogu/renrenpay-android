package wy.experiment.xposed.base;

/**
 * Created by chenxinyou on 2019/2/26.
 */

public interface IBaseActivity {
    void initView();

    void initData();

    int getLayoutId();
}
