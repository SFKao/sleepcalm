package omelcam934.sleepcalm.endpoint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InvalidObjectException;

import omelcam934.sleepcalm.endpoint.dto.LoginDto;
import omelcam934.sleepcalm.endpoint.dto.RegistroDto;
import omelcam934.sleepcalm.endpoint.exceptions.EmailOrUsernameInUseException;
import omelcam934.sleepcalm.endpoint.exceptions.InvalidEmailException;
import omelcam934.sleepcalm.endpoint.exceptions.InvalidLoginException;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BackendApiService {

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://sfkao.bounceme.net:25577/api/")
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
            .build();

    /**
     * Registra a un usuario. Si lo consigue continuara la ejecucion. Si falla lanzara una excepcion dependiendo del error
     * @param username username del nuevo usuario
     * @param email email del nuevo usuario
     * @param password contraseña del nuevo usuario
     * @throws IOException si hay un problema haciendo la peticion
     * @throws EmailOrUsernameInUseException si el email o username esta en uso
     * @throws InvalidEmailException si el email es invalido
     */
    public static void registro(String username, String email, String password) throws IOException, EmailOrUsernameInUseException, InvalidEmailException, InvalidLoginException {

        BackendApiServiceInterface backendApiServiceInterface = retrofit.create(BackendApiServiceInterface.class);
        RegistroDto payload = new RegistroDto(username,email,password);
        Call<String> call = backendApiServiceInterface.registrar(payload);
        Response<String> response = call.execute();

        if(!response.isSuccessful()){
            switch (response.code()){
                case 400: //Bad request
                    throw new EmailOrUsernameInUseException();
                case 401:
                    throw new InvalidEmailException();
                default:
                    throw new IOException(response.message());
            }
        }
        login(username, password);
    }

    /**
     * Hace login y devuelve el token
     * @param usernameOrEmail nombre de usuario o email del usuario
     * @param password contraseña
     * @return token
     * @throws IOException si hay un error al hacer la llamada
     * @throws InvalidLoginException si los datos son invalidos
     */
    public static String login(String usernameOrEmail, String password) throws IOException, InvalidLoginException {
        BackendApiServiceInterface backendApiServiceInterface = retrofit.create(BackendApiServiceInterface.class);
        LoginDto payload = new LoginDto(usernameOrEmail, password);
        Call<String> call = backendApiServiceInterface.login(payload);
        Response<String> response = call.execute();

        if(response.isSuccessful()){
            return response.body();
        }else{
            switch (response.code()){
                case 401: //Si no permite loguearse
                    throw new InvalidLoginException();
                default:
                    throw new IOException(response.message());
            }
        }
    }

    public static boolean checkToken(String token) throws IOException {
        BackendApiServiceInterface backendApiServiceInterface = retrofit.create(BackendApiServiceInterface.class);
        Call<Boolean> call = backendApiServiceInterface.checkToken("Bearer "+token);
        Response<Boolean> response = call.execute();
        return response.isSuccessful();
    }

}
