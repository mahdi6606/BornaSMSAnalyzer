package ir.asemaneh.bornasmsanalyzer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.WindowManager;

import ir.asemaneh.bornasmsanalyzer.databinding.ActivityMainBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity {

    ActivityMainBinding binding;
    Context mContext;

    WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;
        binding.tvLog.setMovementMethod(new ScrollingMovementMethod());


        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String sender, String messBody) {
                binding.tvLog.append(sender+"\r\n"+messBody+"\r\n-------------------\r\n");
            }

            @Override
            public void messageSendToBale(String sender, String messBody) {
                binding.tvLog.append("به گروه ارسال شد" + "\r\n**********************\r\n");
            }
        });


    }




}