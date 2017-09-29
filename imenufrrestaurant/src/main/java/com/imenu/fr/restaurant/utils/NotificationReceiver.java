package com.imenu.fr.restaurant.utils;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.google.gson.Gson;
import com.imenu.fr.restaurant.HomeActivity;
import com.imenu.fr.restaurant.R;
import com.imenu.fr.restaurant.activity.DetailsActivity;
import com.imenu.fr.restaurant.app.Imenufr;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;


/**
 * Created by android on 5/12/16.
 */

public class NotificationReceiver extends WakefulBroadcastReceiver {
    private static final
    String MESSAGE =
            "gcm.notification.body";
    String TITLE =
            "gcm.notification.title";
    String ORDER_ID = "gcm.data.order_id";
    private String mMessage = "";
    private String mTitle = "Imenufr Restaurant";
    private int mOrderId;
    private int mOrderStatus;
    private int NOTIID;
    public static MediaPlayer mPlayer;

    public static String intentToString(Intent intent) {
        if (intent == null)
            return "";

        StringBuilder stringBuilder = new StringBuilder("action: ")
                .append(intent.getAction())
                .append(" data: ")
                .append(intent.getDataString())
                .append(" extras: ")
                ;
        for (String key : intent.getExtras().keySet())
            stringBuilder.append(key).append("=").append(intent.getExtras().get(key)).append(" ");

        return stringBuilder.toString();

    }
    @Override
    public void onReceive(final Context context, Intent data) {

        /*
        *********************** if user logout the from app to avoid notifications device token is not deleted from backend while logout
         */
        if (!Utils.getInstance().getValue(Constants.LOGGED_IN, false, context))
            return;

       // Log.e("data",intentToString(data));

        try {
            mMessage = data.getStringExtra("message");
            mTitle = data.getStringExtra("title");
            if (data.getStringExtra("order_id") != null)
                mOrderId = Integer.parseInt(data.getStringExtra("order_id"));
            if (data.getStringExtra("order_status") != null)
                mOrderStatus = Integer.parseInt(data.getStringExtra("order_status"));

           // Log.e("orderid", data.getStringExtra("order_id") + "");
        } catch (Exception ex) {
            Log.e("pushdata", ex.getMessage() + "");


        }
        Log.e("orderid,orderstatus",mOrderId+","+mOrderStatus);


        showNotificaiton(context);


    }

    private void showNotificaiton(Context context) {

//try {
//    //increate notification count in dashboard
//    if (event_type.compareTo("0") != 0) {
//        int count = Utilities.getInstance().getValueFromSharedPreference(Constants.NOTI_COUNT, 0, context);
//        count++;
//        Utilities.getInstance().saveValueToSharedPreference(Constants.NOTI_COUNT, count, context);
//    }
//    // if app in foreground to update notification count and show batch to dashboard
//    if ((Utilities.getInstance().getDashRef() != null)) {
//        Utilities.getInstance().getDashRef().updateNotiCount();
//
//    }
//}
//catch (Exception ex)
//{
//
//}
        //Log.e("status", isAppIsInBackground(context) + "'");

        NOTIID = (int) System.currentTimeMillis();
        //   Logger.e("notiid",NOTIID+"");
        Intent intent;
        intent = new Intent(context, DetailsActivity.class);

        intent.putExtra(Constants.ORDER_ID, mOrderId);
        intent.putExtra(Constants.ORDER_STATUS, mOrderStatus);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIID, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat
                .Builder(context)
                .setSmallIcon(R.drawable.app_logo)
                .setContentTitle(mTitle)
                .setContentText(mMessage)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        notificationBuilder.setSmallIcon(getNotificationIcon(notificationBuilder));

        notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        notificationBuilder.setDefaults(Notification.DEFAULT_LIGHTS);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIID, notificationBuilder.build());

        //to abort default notificaiton receiver of firebase
        abortBroadcast();

        switch (mOrderStatus)
        {
            case Constants.PENDING:
                EventBus.getDefault().post(1d); //refresh the pending tab
            break;
            case Constants.REJECTED:
                EventBus.getDefault().post(1d); //refresh the pending tab
             break;
            case Constants.ACCEPTED:
                EventBus.getDefault().post(1d); //refresh the pending tab
                break;
            case Constants.DISPATCHED:
                EventBus.getDefault().post(3d); //refresh the pending tab
            break;
            case Constants.COMPLETED:
                EventBus.getDefault().post(4d); //refresh the pending tab
            break;
            case Constants.EXPIRED:
                EventBus.getDefault().post(1d); //refresh the pending tab
                break;
            default:

        }
        playMusic(context);
    }

    private void playMusic(Context context)
    {
        try {

            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            playSound(context,notification);

        } catch (Exception e) {
            Log.e("error1",e.getMessage()+"");
        }
    }
    private void playSound(Context context,Uri alert){

        try{

            if(mPlayer!=null)
            {
                mPlayer.setLooping(false);
                mPlayer.pause();
                mPlayer.stop();
                mPlayer.release();
                mPlayer=null;
            }

            mPlayer=new MediaPlayer();
            mPlayer.setDataSource(context,alert);
            final AudioManager am=(AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
            if(am.getStreamVolume(AudioManager.STREAM_ALARM)!=0)
            {
                mPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mPlayer.prepare();
                mPlayer.setLooping(true);
                mPlayer.start();
            }
            else
            {
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mPlayer.prepare();
                mPlayer.setLooping(true);
                mPlayer.start();
            }
			/*mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mediaPlayer) {
					mediaPlayer.start();
				}
			});*/

        }
        catch(IOException e)
        {
            Log.e("AlaramReciever", "no audio file");
        }
    }


    /**
     * ************************ notification icon below and above lollipop **********************
     *
     * @param notificationBuilder
     * @return
     */
    private int getNotificationIcon(NotificationCompat.Builder notificationBuilder) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = Color.parseColor("#EF6A4A");
            notificationBuilder.setColor(color);
            return R.drawable.app_logo;

        } else {
            return R.drawable.app_logo;
        }
    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

}
