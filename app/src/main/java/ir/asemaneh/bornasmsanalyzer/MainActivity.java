package ir.asemaneh.bornasmsanalyzer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.method.ScrollingMovementMethod;

import ir.asemaneh.bornasmsanalyzer.databinding.ActivityMainBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity {

    ActivityMainBinding binding;
    Context mContext;
    RestClient.RestApiInterface service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;
        binding.tvLog.setMovementMethod(new ScrollingMovementMethod());

        service = RestClient.getClient(mContext);

        SmsReceiver smsReceiver = new SmsReceiver();

        smsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String sender, String messBody) {
                if(verifiedSMSMatcher(sender,messBody)){
                    binding.tvLog.append(sender+"\r\n"+messBody+"\r\n-------------------\r\n");
                    sendToBale(messBody);
                }
            }
        });


    }

    private void sendToBale(String messBody) {

        Call<BaleResponseSendMessage> call = service.sendMessage("6021883428",messBody);
        call.enqueue(new Callback<BaleResponseSendMessage>() {
            @Override
            public void onResponse(Call<BaleResponseSendMessage> call, Response<BaleResponseSendMessage> response) {
                if (response.code() == 200){
                    if (response.body().ok)
                        binding.tvLog.append("به گروه ارسال شد"+"\r\n**********************\r\n");
                }
            }

            @Override
            public void onFailure(Call<BaleResponseSendMessage> call, Throwable t) {

            }
        });

    }

    private boolean verifiedSMSMatcher(String sender, String messBody) {
        if (sender.equals("9899902318"))
            return true;
        return false;
    }
}