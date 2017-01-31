package com.twt.service.service.push;

import android.util.Log;

import com.twt.service.WePeiYangApp;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Created by nero on 16-7-4.
 */
public class TwtMqtt {
    private String broker;
    private String userName;
    private String password;
    private String topic;
    private String msg;
    private Integer qos;
    private String clientId;
    private boolean isConnected = false;
    private String TAG="twtmqtt";

    private MemoryPersistence persistence;//
    private MqttClient mqttClient;
    private MqttConnectOptions mqttConnectOptions;

    private PushService mPushService;

    public TwtMqtt(PushService pushService) {
        this.mPushService = pushService;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getQos() {
        return qos;
    }

    public void setQos(Integer qos) {
        this.qos = qos;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isConnected() {
        return mqttClient.isConnected();
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    void init() {
        persistence = new MemoryPersistence();
        mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setKeepAliveInterval(18330);
        mqttConnectOptions.setUserName(userName);
        mqttConnectOptions.setPassword(password.toCharArray());
        try {
            mqttClient = new MqttClient(broker, clientId, persistence);
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                    isConnected = false;
                    Log.d(TAG,"Lost connection");
                    try {
                        mqttClient.connect(mqttConnectOptions);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG,"connecting.....");
                }

                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    Log.d(TAG,"message arrived:"+mqttMessage.toString());
                    Log.d(TAG,"message topic:"+s);
                    if (mqttMessage.toString().equals(PushService.MQTT_KEEP_ALIVE_MESSAGE))
                    {
                        PushService.actionKeepalive(WePeiYangApp.getContext());
                        Log.d(TAG,"keepalive message received");
                    }else {
                    mPushService.sendNotification(mqttMessage.toString());}
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    System.out.println(iMqttDeliveryToken.toString());
                }
            });
            if(!mqttClient.isConnected()){
                mqttClient.connect(mqttConnectOptions);
                Log.d(TAG,"connecting.....");
            }
            isConnected = true;
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

    void subscribe(){

        try {
            mqttClient.subscribe(topic);
            mqttClient.subscribe(PushService.KeepAliveTopic);
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }

    }
    void disconnect()
    {
        if (mqttClient.isConnected()) {
            try {
                mqttClient.disconnect();

            } catch (MqttException e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }
        }
    }
    void publish(String topic,MqttMessage mqttMessage)
    {
        try {
            mqttClient.publish(topic,mqttMessage);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
