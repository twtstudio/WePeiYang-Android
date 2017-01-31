package com.twt.service.service.push;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.twt.service.R;

import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import java.util.Locale;

/**
 * Created by jcy on 2016/7/4.
 */

public class PushService extends Service {
    private static final String TAG = "twtmqtt";
    private boolean mStarted = false;
    private TwtMqtt mqtt;

    public static final int MQTT_QOS_0 = 0; // QOS Level 0 ( Delivery Once no confirmation )
    public static final int MQTT_QOS_1 = 1; // QOS Level 1 ( Delevery at least Once with confirmation )
    public static final int MQTT_QOS_2 = 2; // QOS Level 2 ( Delivery only once with confirmation with handshake )
    private static final String ACTION_START = TAG + ".START"; // Action to start
    private static final String ACTION_STOP = TAG + ".STOP"; // Action to stop
    private static final String ACTION_KEEPALIVE = TAG + ".KEEPALIVE"; // Action to keep alive used by alarm manager
    private static final String ACTION_RECONNECT = TAG + ".RECONNECT"; // Action to reconnect
    private static final int MQTT_KEEP_ALIVE = 60001; // KeepAlive Interval in MS
    public static final String KeepAliveTopic="keepalive";
    public static final String   MQTT_KEEP_ALIVE_MESSAGE = "-1"; // Keep Alive message to send
    private static final int MQTT_KEEP_ALIVE_QOS = MQTT_QOS_0; // Default Keepalive QOS

    private AlarmManager mAlarmManager;
    private ConnectivityManager mConnectivityManager;

    public static void actionStart(Context ctx) {
        Intent i = new Intent(ctx, PushService.class);
        i.setAction(ACTION_START);
        ctx.startService(i);
    }

    public static void actionStop(Context ctx) {
        Intent i = new Intent(ctx, PushService.class);
        i.setAction(ACTION_STOP);
        ctx.startService(i);
    }

    public static void actionKeepalive(Context ctx) {
        Intent i = new Intent(ctx, PushService.class);
        i.setAction(ACTION_KEEPALIVE);
        ctx.startService(i);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "service created");

        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        mConnectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");

        String action = intent.getAction();

        if (action == null) {
            Log.i(TAG, "Starting service with no action\n Probably from a crash");
        } else {
            if (action.equals(ACTION_START)) {
                Log.i(TAG, "Received ACTION_START");
                start();
            } else if (action.equals(ACTION_STOP)) {
                stop();
            } else if (action.equals(ACTION_KEEPALIVE)) {
                keepAlive();
            } else if (action.equals(ACTION_RECONNECT)) {
                if (isNetworkAvailable()) {
                    reconnectIfNecessary();
                }
            }
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        mqtt.disconnect();
        super.onDestroy();
        Log.d(TAG, "onDestroy() executed");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void sendNotification(String message) {
        //发出通知
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher).setDefaults(NotificationCompat.DEFAULT_SOUND).
                setContentTitle("TwtPush").setContentText(message);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(2, mBuilder.build());
    }

    /**
     * Attempts connect to the Mqtt Broker
     * and listen for Connectivity changes
     * via ConnectivityManager.CONNECTVITIY_ACTION BroadcastReceiver
     */
    private synchronized void start() {
        if (mStarted) {
            Log.i(TAG, "Attempt to start while already started");
            return;
        }

        if (hasScheduledKeepAlives()) {
            stopKeepAlives();
        }

        connect();

        registerReceiver(mConnectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    /**
     * Attempts to stop the Mqtt client
     * as well as halting all keep alive messages queued
     * in the alarm manager
     */
    private synchronized void stop() {
        if (!mStarted) {
            Log.i(TAG, "Attemtpign to stop connection that isn't running");
            return;
        }

        if (mqtt != null) {
            mqtt.disconnect();
            mqtt = null;
            mStarted = false;

            stopKeepAlives();
        }
        unregisterReceiver(mConnectivityReceiver);
    }

    private synchronized void connect() {
        mqtt = new TwtMqtt(this);
        mqtt.setBroker("tcp://121.42.157.180:61613");
        mqtt.setTopic("twtandroid1");
        mqtt.setUserName("twtandroid3");
        mqtt.setPassword("twtandroid3");
        mqtt.setQos(1);
        mqtt.setClientId("twtandroid3");
        mqtt.init();
        mqtt.subscribe();
        mStarted = true;
        Log.d(TAG, "successfully connected and subscribed starting keep alives");
        startKeepAlives();
    }

    /**
     * Schedules keep alives via a PendingIntent
     * in the Alarm Manager
     */
    private void startKeepAlives() {
        Intent i = new Intent();
        i.setClass(this, PushService.class);
        i.setAction(ACTION_KEEPALIVE);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + MQTT_KEEP_ALIVE,
                MQTT_KEEP_ALIVE, pi);
        Log.i(TAG,"alarmmanger set");
    }

    /**
     * Cancels the Pending Intent
     * in the alarm manager
     */
    private void stopKeepAlives() {
        Intent i = new Intent();
        i.setClass(this, PushService.class);
        i.setAction(ACTION_KEEPALIVE);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        mAlarmManager.cancel(pi);
    }

    /**
     * Publishes a KeepALive to the topic
     * in the broker
     */
    private synchronized void keepAlive() {
        if (!isConnected()) {
            sendKeepAlive();
            Log.d(TAG,"send........");
        }
    }

    /**
     * Checkes the current connectivity
     * and reconnects if it is required.
     */
    private synchronized void reconnectIfNecessary() {
        if (mStarted && mqtt == null) {
            connect();
        }
    }

    /**
     * Query's the NetworkInfo via ConnectivityManager
     * to return the current connected state
     *
     * @return boolean true if we are connected false otherwise
     */
    private boolean isNetworkAvailable() {
        NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();

        return info != null && info.isConnected();
    }

    /**
     * Verifies the client State with our local connected state
     *
     * @return true if its a match we are connected false if we aren't connected
     */
    private boolean isConnected() {
        if (mStarted && mqtt != null && !mqtt.isConnected()) {
            Log.i(TAG, "Mismatch between what we think is connected and what is connected");
        }

        if (mqtt != null) {
            return (mStarted && mqtt.isConnected());
        }

        return false;
    }

    /**
     * Receiver that listens for connectivity chanes
     * via ConnectivityManager
     */
    private final BroadcastReceiver mConnectivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "Connectivity Changed...");
        }
    };

    /**
     * Sends a Keep Alive message to the specified topic
     *
     * @return MqttDeliveryToken specified token you can choose to wait for completion
     */
   /* private synchronized MqttDeliveryToken sendKeepAlive()
            throws PushService.MqttConnectivityException, MqttPersistenceException, MqttException {
        if (!isConnected())
            throw new PushService.MqttConnectivityException();

        if (mKeepAliveTopic == null) {
            Log.d(TAG,"gg");
            mKeepAliveTopic = mqtt.getTopic();
                    //String.format(Locale.US, MQTT_KEEP_ALIVE_TOPIC_FORAMT, mDeviceId));
        }

        Log.i(TAG, "Sending Keepalive to " );

        MqttMessage message = new MqttMessage(MQTT_KEEP_ALIVE_MESSAGE);
        message.setQos(MQTT_KEEP_ALIVE_QOS);

        return mKeepAliveTopic.publish(message);
    }*/

    private synchronized void  sendKeepAlive()
    {
//        if(!isConnected()) throw
        MqttMessage mqttMessage=new MqttMessage(MQTT_KEEP_ALIVE_MESSAGE.getBytes());
        mqttMessage.setQos(MQTT_KEEP_ALIVE_QOS);
        mqtt.publish(KeepAliveTopic,mqttMessage);
        Log.d(TAG,"keepalivemessage sent");
    }

    /**
     * Query's the AlarmManager to check if there is
     * a keep alive currently scheduled
     *
     * @return true if there is currently one scheduled false otherwise
     */
    private synchronized boolean hasScheduledKeepAlives() {
        Intent i = new Intent();
        i.setClass(this, PushService.class);
        i.setAction(ACTION_KEEPALIVE);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_NO_CREATE);
        return (pi != null);
    }
    private class MqttConnectivityException extends Exception {
        private static final long serialVersionUID = -7385866796799469420L;
    }
}

