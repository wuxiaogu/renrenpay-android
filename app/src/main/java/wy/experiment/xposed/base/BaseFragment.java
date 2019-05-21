package wy.experiment.xposed.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import wy.experiment.xposed.bean.EventBus;
import wy.experiment.xposed.bean.EventHandler;

/**
 * Created by chenxinyou on 2019/2/26.
 */

public class BaseFragment extends Fragment implements IBaseFragment, View.OnClickListener {

    protected LayoutInflater mInflater;
    private View holdView;
    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (EventHandler.class.isInstance(this))
            EventBus.getDefault().add((EventHandler) this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.mInflater = inflater;
        if (getLayoutId() != 0) {

            if (holdView()) {

                if (holdView == null) {

                    holdView = mInflater.inflate(getLayoutId(), null);

                    initView(holdView);
                    unbinder = ButterKnife.bind(this, holdView);
                } else {

                    ViewGroup parent = (ViewGroup) holdView.getParent();
                    if (parent != null) {
                        parent.removeView(holdView);
                    }
                }
                init(savedInstanceState);
                initView(holdView);
                initData();
            }

        }

        return holdView == null ? super.onCreateView(inflater, container, savedInstanceState) : holdView;
    }

    public void init(Bundle savedInstanceState) {
    }

    protected boolean holdView() {
        return true;
    }

    public void initView(View view) {

    }

    public String getTitle() {
        return "";
    }

    public void initData() {
    }

    public int getLayoutId() {
        return 0;
    }

    @Override
    public void onClick(View v) {

    }
}
