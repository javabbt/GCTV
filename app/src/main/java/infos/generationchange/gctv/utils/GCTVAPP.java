package infos.generationchange.gctv.utils;

import android.app.Application;

import com.onesignal.OneSignal;

public class GCTVAPP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }
}
