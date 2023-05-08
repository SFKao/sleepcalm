package omelcam934.sleepcalm.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiServiceInterface {

    @GET
    Call<ResponseBody> executeApiCall(@Url String url);

    @POST("/sleep")
    Call<String> enviarMensajeAlServidorDePrueba(@Body ApiService.Mensaje mensaje);

    @POST("/sleepsegment")
    Call<String> enviarSegmento(@Body ApiService.Mensaje mensaje);

    @POST("/sleepclassify")
    Call<String> enviarClassify(@Body ApiService.Mensaje mensaje);

}
