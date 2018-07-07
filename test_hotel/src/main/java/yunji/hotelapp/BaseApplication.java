package yunji.hotelapp;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        Bugly.init(this, "6a048422cb",true);
        CrashReport.initCrashReport(getApplicationContext(), "6a048422cb", true);
    }
}
