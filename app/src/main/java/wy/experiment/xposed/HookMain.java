package wy.experiment.xposed;

import android.content.pm.ApplicationInfo;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import wy.experiment.xposed.hook.HookBase;
import wy.experiment.xposed.hook.HookList;

public class HookMain implements IXposedHookLoadPackage {
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.appInfo == null || (lpparam.appInfo.flags & (ApplicationInfo.FLAG_SYSTEM |
                ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)) != 0) {
            return;
        }
        final String packageName = lpparam.packageName;
        final String processName = lpparam.processName;


        for (HookBase hookBase : HookList.getInstance().getmListHook()) {
            //下面的hookCountIndex为2是在vxp里的值，如果手机已root是在xp里运行请改为1，然后把我这行中文删除即可
            hookBase.hook(packageName, processName, 1);
            //LogUtils.show("很多人在倒卖这套系统，大家请不要上当！QQ937013765");
        }
    }
}
