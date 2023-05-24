package omelcam934.sleepcalm.endpoint;

import androidx.annotation.NonNull;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EndpointDeviceApiService {

    public static void executeApiCall(String baseURL, String path){
        System.out.println(baseURL+path);
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseURL)
                .build();

        EndpointDeviceApiServiceInterface service = retrofit.create(EndpointDeviceApiServiceInterface.class);

        Call<ResponseBody> call = service.executeApiCall(path);



        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public static void sendTestMessage(String mensaje){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.18.4:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndpointDeviceApiServiceInterface service = retrofit.create(EndpointDeviceApiServiceInterface.class);

        Call<String> stringCall = service.enviarMensajeAlServidorDePrueba(new Mensaje(mensaje));

        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public static void sendClassify(String mensaje){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.18.4:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndpointDeviceApiServiceInterface service = retrofit.create(EndpointDeviceApiServiceInterface.class);

        Call<String> stringCall = service.enviarClassify(new Mensaje(mensaje));

        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public static void sendSegmento(String mensaje){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.18.4:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndpointDeviceApiServiceInterface service = retrofit.create(EndpointDeviceApiServiceInterface.class);

        Call<String> stringCall = service.enviarSegmento(new Mensaje(mensaje));

        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    static class Mensaje{
        public Mensaje(String msg) {
            this.msg = msg;
        }

        public String msg;
    }

}
