package wy.experiment.xposed.hook;

import java.util.ArrayList;
import java.util.List;

import wy.experiment.xposed.hook.alipay.AlipayHook;
import wy.experiment.xposed.hook.wechat.WechatHook;

public class HookList {
    private List<HookBase> mListHook = new ArrayList<>();

    private static HookList mHookList;

    public synchronized static HookList getInstance() {
        if (mHookList == null) {
            mHookList = new HookList();
        }
        return mHookList;
    }

    public HookList() {
        mListHook.clear();

        //TODO 添加渠道都在这里添加就可以了。
        mListHook.add(WechatHook.getInstance());
        mListHook.add(AlipayHook.getInstance());

        for (HookBase hookBase : mListHook) {
            hookBase.addLocalTaskI();
        }
    }

    public List<HookBase> getmListHook() {
        return mListHook;
    }
}
