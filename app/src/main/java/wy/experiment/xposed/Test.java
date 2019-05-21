package wy.experiment.xposed;

import android.util.Log;

public class Test {
    public static void main(String[] args) {
        String content = "{\"content\":\"￥0.10\",\"assistMsg1\":\"收到一笔转账\",\"assistMsg2\":\"test_alipay\",\"linkName\":\"\",\"buttonLink\":\"\",\"templateId\":\"WALLET-FWC@remindDefaultText\"}";
//        Log.d("cxy", content.contains("收到一笔转账") ? "成功": "失败");
        System.out.print(content.contains("收到一笔转账"));
    }
}
