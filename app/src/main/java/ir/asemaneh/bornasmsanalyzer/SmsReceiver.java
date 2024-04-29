package ir.asemaneh.bornasmsanalyzer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsReceiver extends BroadcastReceiver {
    private static SmsListener mListener;
    @Override
    public void onReceive(Context context, Intent intent) {

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

                    if(!msgBody.equals("")){
                        mListener.messageReceived(msg_from,msgBody);
                    }

            } catch (Exception e) {
                //                            Log.d("Exception caught",e.getMessage());
            }
        }
    }
    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
