package com.hiten_khatri.xmusic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.hiten_khatri.xmusic.ApplicationClass.ACTION_DELETE;
import static com.hiten_khatri.xmusic.ApplicationClass.ACTION_NEXT;
import static com.hiten_khatri.xmusic.ApplicationClass.ACTION_PLAY;
import static com.hiten_khatri.xmusic.ApplicationClass.ACTION_PREVIOUS;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String actionName=intent.getAction();
        Intent serviceIntent=new Intent(context,MusicService.class);
        if(actionName!=null)
        {
            switch(actionName)
            {
                case ACTION_PLAY:
                    serviceIntent.putExtra("ActionName","pause");
                    context.startService(serviceIntent);
                    break;
                case ACTION_NEXT:
                    serviceIntent.putExtra("ActionName","next");
                    context.startService(serviceIntent);
                    break;
                case ACTION_PREVIOUS:
                    serviceIntent.putExtra("ActionName","previous");
                    context.startService(serviceIntent);
                    break;
                case ACTION_DELETE:
                    serviceIntent.putExtra("ActionName","delete");
                    context.startService(serviceIntent);
                    break;
            }
        }
    }
}
