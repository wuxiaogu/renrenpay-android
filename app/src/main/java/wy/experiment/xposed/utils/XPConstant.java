package wy.experiment.xposed.utils;

/**
 * Created by wy on 2018/11/13.
 */

public class XPConstant {
    private static final String TAG = "XPConstant";

    public static final String URL_IP = "192.168.10.31";

    // alipay package and class name
    public static final String ALI_PACKAGE = "com.eg.android.AlipayGphone";
    public static final String APPLICATION_CLASS = "com.alipay.mobile.quinox.LauncherApplication";
    public static final String MONEY_RPC_CLASS = "com.alipay.transferprod.rpc.CollectMoneyRpc";
    public static final String ZXING_HELPER_CLASS = "com.alipay.android.phone.wallet.ZXingHelper";
    public static final String BARCODE_FORMAT_ENUM = "com.alipay.android.phone.wallet.minizxing.BarcodeFormat";
    public static final String ERROR_CORRECTION_ENUM = "com.alipay.android.phone.wallet.minizxing.ErrorCorrectionLevel";
    public static final String CONTEXT_IMPL_CLASS = "com.alipay.mobile.core.impl.MicroApplicationContextImpl";
    public static final String RPC_SERVICE_CLASS = "com.alipay.mobile.framework.service.common.RpcService";
    public static final String LAUNCHER_AGENT_CLASS = "com.alipay.mobile.framework.LauncherApplicationAgent";
    public static final String ALI_PAY_PAY_SUCCESS = "com.alipay.android.phone.messageboxstatic.biz.dao.ServiceDao";
    public static final String ALI_PAY_PAY_SUCCESS1 = "com.alipay.android.phone.messageboxstatic.biz.dao.TradeDao";
    public static final String ALI_PAY_SAFE_CLASS = "com.alipay.apmobilesecuritysdk.scanattack.common.ScanAttack";


    public static final String ALI_PAY_TEST_ONCREATE = "com.alipay.mobile.quinox.LauncherActivity";


    // wechat package and class name
    public static final String WECAHT_PACKAGE = "com.tencent.mm";
    public static final String WE_NET_REQ_CLASS = "com.tencent.mm.plugin.collect.b.s";
    public static final String WE_INFO_CLAZZ = "com.tencent.mm.model.q";
    public static final String WE_QR_BMP_CLAZZ = "com.tencent.mm.plugin.collect.b.b";
    public static final String WE_TOOL_CLAZZ = "com.tencent.mm.platformtools.y$a";
    public static final String WE_APP_CLAZZ = "com.tencent.tinker.loader.app.TinkerApplication";
    public static final String WE_L_CLAZZ = "com.tencent.mm.ab.l";
    public static final String WE_G_CLAZZ = "com.tencent.mm.kernel.g";
    public static final String WECHAT_DB_PACKAGE = "com.tencent.wcdb.database.SQLiteDatabase";

    public static final String WECHAT_CODE_UI = "com.tencent.mm.plugin.collect.ui.CollectCreateQRCodeUI";

    // IPC service communicate message data key
    public static final String QR_DES = "qr_des";
    public static final String QR_ORDER_ID = "qr_order_id";
    public static final String QR_MONEY = "qr_money";
    public static final String QR_ICON = "qr_icon";
    public static final String QR_SUCCESS = "qr_success";
    public static final String QR_PATH = "qr_path";

    public static final int VERBOSE = 0;
    public static final int DEBUG = 1;
    public static final int INFO = 2;
    public static final int ERROR = 3;

    // message what code
    public static final int ALI_GENERATE_QR = 1000;
    public static final int ALI_JOIN = 1001;
    public static final int TEST_JOIN = 1002;
    public static final int MSG_QR_COMPLETE = 1003;
    public static final int WE_JOIN = 1004;
    public static final int WE_GENERATE_QR = 1005;
    public static final int RECEIVE_PAYMENT = 1006;

}
