package ir.asemaneh.bornasmsanalyzer;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SmsReceiver extends BroadcastReceiver {
    private static SmsListener mListener;

    Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {

        mContext = context;

        Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
        SmsMessage[] msgs = null;

        if (bundle != null) {
            //---retrieve the SMS message received---
            try {
                Object[] pdus = (Object[]) bundle.get("pdus");
                msgs = new SmsMessage[pdus.length];
                String msgBody = "";
                String msg_from = "";
                for (int i = 0; i < msgs.length; i++) {
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    msg_from = msgs[i].getOriginatingAddress();
                    msgBody += msgs[i].getMessageBody();
                }

                if(!msgBody.equals("") && verifiedSMSMatcher(msg_from,msgBody)){
                    if (mListener != null)
                        mListener.messageReceived(msg_from,msgBody);

//                    checkWIFIStatus();

                    sendToBale(msg_from,msgBody);
                }

            } catch (Exception e) {
                //                            Log.d("Exception caught",e.getMessage());
            }
        }
    }
    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }

    private boolean verifiedSMSMatcher(String sender, String messBody) {
        return sender.equals("9899902318") | messBody.contains("چرخه حیات") | messBody.contains("تاپیار")|
                sender.equals("9899902") | messBody.contains("همراه لوتوس");
    }

    private void checkWIFIStatus() {
        WifiManager wifiManager =(WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);

        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
    }

    private void sendToBale(String sender,String messBody) {

        RestClient.RestApiInterface service = RestClient.getClient(mContext);

        Call<BaleResponseSendMessage> call = service.sendMessage("6021883428",messBody);  //ای دی گروه برنا اداری در بله
        call.enqueue(new Callback<BaleResponseSendMessage>() {
            @Override
            public void onResponse(Call<BaleResponseSendMessage> call, Response<BaleResponseSendMessage> response) {
                if (response.code() == 200){

                    assert response.body() != null;
                    if (response.body().ok) {
                        if (mListener != null)
                            mListener.messageSendToBale(sender,messBody);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaleResponseSendMessage> call, Throwable t) {

            }
        });

    }
    private void markMessageRead(Context context, String number, String body) {

        Uri uri = Uri.parse("content://sms/inbox");
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        try{

            while (cursor.moveToNext()) {
                if ((cursor.getString(cursor.getColumnIndexOrThrow("address")).equals(number)) && (cursor.getInt(cursor.getColumnIndexOrThrow("read")) == 0)) {
                    if (cursor.getString(cursor.getColumnIndexOrThrow("body")).startsWith(body)) {
                        String SmsMessageId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                        ContentValues values = new ContentValues();
                        values.put("read", true);
                        int a = context.getContentResolver().update(Uri.parse("content://sms/inbox"), values, "_id = " + SmsMessageId, null);
                        return;
                    }
                }
            }
            cursor.close();
        }catch(Exception e)
        {
            Log.e("Mark Read", "Error in Read: "+e.toString());
            cursor.close();

        }
    }
}
