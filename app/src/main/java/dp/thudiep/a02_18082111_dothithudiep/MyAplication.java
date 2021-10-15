package dp.thudiep.a02_18082111_dothithudiep;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class MyAplication extends Application {

    public static final String CHANEL_ID = "chanel_service_example";

    @Override
    public void onCreate() {
        super.onCreate();
        
        createChanelNotification();
    }

    private void createChanelNotification() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANEL_ID,
                    "Chanel Service Example",
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.setSound(null,null);

            NotificationManager manager = getSystemService(NotificationManager.class);
            if(manager != null) {
                manager.createNotificationChannel(channel);
            }

        }
    }
    // Xong add vào Mainfest
}
