package omelcam934.sleepcalm.services;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiServiceInterface {

    @POST("/sleep")
    Call<String> enviarMensajeAlServidorDePrueba(@Body ApiService.Mensaje mensaje);

}
