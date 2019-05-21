package wy.experiment.xposed.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenxinyou on 2019/2/26.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> list = null;
    private String[] title;
    private FragmentManager fragmetnmanager;

    public FragmentAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        // TODO Auto-generated constructor stub
        if (list == null) {
            this.list = new ArrayList<>();
        }
        fragmetnmanager = fm;
        this.list = list;
    }

    /**
     * 设置页面title
     * <p>Title: setPageTitle
     * <p>Description:
     */
    public void setData(ArrayList<Fragment> list){
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // TODO Auto-generated method stub
        if (title != null && title.length > position) {
            return title[position];
        }
        return super.getPageTitle(position);
    }
}
