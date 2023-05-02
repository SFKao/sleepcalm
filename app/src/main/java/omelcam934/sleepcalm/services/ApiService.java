package omelcam934.sleepcalm.services;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

    public static void sendTestMessage(String mensaje){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.18.4:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServiceInterface service = retrofit.create(ApiServiceInterface.class);

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

        ApiServiceInterface service = retrofit.create(ApiServiceInterface.class);

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

        ApiServiceInterface service = retrofit.create(ApiServiceInterface.class);

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
