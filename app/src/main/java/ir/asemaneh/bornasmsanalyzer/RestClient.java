package ir.asemaneh.bornasmsanalyzer;

import android.content.Context;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;


public class RestClient {

    private static RestApiInterface gitApiInterface ;

    public static String BASE_URL = "https://tapi.bale.ai/bot1660598942:RrFqUetO61z7KWJU1ctTuHBHK3z5tfFvNQVYJCVO/";

    public static RestApiInterface getClient(Context context) {

            Retrofit client = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            gitApiInterface = client.create(RestApiInterface.class);

        return gitApiInterface ;
    }

    public interface RestApiInterface {


//        @GET("GetOtpTerm")
//        Call<StructureGetPass2Receive> getSupportPassCall(@Query("terminal") String terminal, @Query("serial") String serial);

        @FormUrlEncoded
        @POST("sendMessage")
        Call<BaleResponseSendMessage> sendMessage(@Field("chat_id") String chatid, @Field("text") String text);


//        @GET("PmSec/GetOtpTermByDate")
//        Call<StructureGetPass1Receive> getSupportPassCall(
//                @Header("PersonId") String personid,@Header("MToken") String token,
//                @Query("terminal") String terminal, @Query("serial") String serial,@Query("date") String date
//        );
    }

}