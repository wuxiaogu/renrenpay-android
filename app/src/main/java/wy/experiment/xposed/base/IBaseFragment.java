package wy.experiment.xposed.base;

import android.view.View;

/**
 * Created by chenxinyou on 2019/2/26.
 */

public interface IBaseFragment {
    void initView(View view);

    void initData();

    int getLayoutId();
}
