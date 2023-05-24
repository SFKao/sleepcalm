package omelcam934.sleepcalm.endpoint;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface EndpointDeviceApiServiceInterface {

    @GET
    Call<ResponseBody> executeApiCall(@Url String url);

    //Lo de abajo son metodos de prueba, ya que es dificil ver el log de la app mientras duermes

    @POST("/sleep")
    Call<String> enviarMensajeAlServidorDePrueba(@Body EndpointDeviceApiService.Mensaje mensaje);

    @POST("/sleepsegment")
    Call<String> enviarSegmento(@Body EndpointDeviceApiService.Mensaje mensaje);

    @POST("/sleepclassify")
    Call<String> enviarClassify(@Body EndpointDeviceApiService.Mensaje mensaje);

}
