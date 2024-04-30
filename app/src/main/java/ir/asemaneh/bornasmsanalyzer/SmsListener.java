package ir.asemaneh.bornasmsanalyzer;

import android.telephony.SmsMessage;

public interface SmsListener {
    public void messageReceived(String sender, String messBody);
    public void messageSendToBale(String sender, String messBody);
}
