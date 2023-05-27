package omelcam934.sleepcalm.endpoint;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import omelcam934.sleepcalm.endpoint.dto.LoginDto;
import omelcam934.sleepcalm.endpoint.dto.RegistroDto;
import omelcam934.sleepcalm.endpoint.dto.SleepTrackDto;
import omelcam934.sleepcalm.endpoint.dto.WeekDto;
import omelcam934.sleepcalm.endpoint.exceptions.EmailOrUsernameInUseException;
import omelcam934.sleepcalm.endpoint.exceptions.InvalidEmailException;
import omelcam934.sleepcalm.endpoint.exceptions.InvalidLoginException;
import omelcam934.sleepcalm.services.LoginService;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BackendApiService {

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://sfkao.bounceme.net:25577/api/")
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
            .build();

    public static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.forLanguageTag("es-ES"));

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
            if (response.code() == 401) { //Si no permite loguearse
                throw new InvalidLoginException();
            }
            throw new IOException(response.message());
        }
    }

    public static boolean checkToken() throws IOException {
        BackendApiServiceInterface backendApiServiceInterface = retrofit.create(BackendApiServiceInterface.class);
        Call<Boolean> call = backendApiServiceInterface.checkToken("Bearer "+LoginService.getToken());
        Response<Boolean> response = call.execute();
        return response.isSuccessful();
    }

    public static WeekDto getCurrentWeek(Context context) throws InvalidLoginException, IOException {
        return getCurrentWeek(context, false);
    }

    private static WeekDto getCurrentWeek(Context context, boolean triedLogin) throws InvalidLoginException, IOException {
        if(!LoginService.isLoguedIn()){
            LoginService.autoLogin(context);
            triedLogin = true;
        }
        Log.d("MIMIR", "Iniciando busqueda de la semana");
        BackendApiServiceInterface backendApiServiceInterface = retrofit.create(BackendApiServiceInterface.class);
        Call<WeekDto> call = backendApiServiceInterface.getCurrentWeek("Bearer "+LoginService.getToken());
        Response<WeekDto> response = call.execute();
        if(response.isSuccessful()){
            return response.body();
        }else{
            if (response.code() == 401) { //Si no permite loguearse
                Log.d("MIMIR", "Fallada busqueda de la semana");
                if(triedLogin){
                    throw new InvalidLoginException();
                }else{
                    Log.d("MIMIR", "Logueandose y intentando busqueda de la semana");
                    LoginService.newToken(context);
                    getCurrentWeek(context,true);
                }
            }
            throw new IOException(response.message());
        }
    }

    public static WeekDto getWeek(Context context, Date date) throws InvalidLoginException, IOException {
        return getWeek(context, date, false);
    }

    private static WeekDto getWeek(Context context, Date date, boolean triedLogin) throws InvalidLoginException, IOException {
        if(!LoginService.isLoguedIn()){
            LoginService.autoLogin(context);
            triedLogin = true;
        }
        BackendApiServiceInterface backendApiServiceInterface = retrofit.create(BackendApiServiceInterface.class);
        Call<WeekDto> call = backendApiServiceInterface.getWeek("Bearer "+LoginService.getToken(), dateFormat.format(date));
        Response<WeekDto> response = call.execute();
        if(response.isSuccessful()){
            return response.body();
        }else{
            if (response.code() == 401) { //Si no permite loguearse
                if(triedLogin){
                    throw new InvalidLoginException();
                }else{
                    LoginService.newToken(context);
                    getWeek(context, date,true);
                }
            }
            throw new IOException(response.message());
        }
    }

    public static SleepTrackDto postSleepTrack(Context context, Date horaDeInicio, Date horaDeFin) throws InvalidLoginException, IOException {

        LoginService.autoLogin(context);

        SleepTrackDto sleepTrackDto = new SleepTrackDto(dateFormat.format(horaDeInicio), dateFormat.format(horaDeFin),null);

        BackendApiServiceInterface backendApiServiceInterface = retrofit.create(BackendApiServiceInterface.class);
        Call<SleepTrackDto> call = backendApiServiceInterface.postSleepTrack("Bearer "+LoginService.getToken(), sleepTrackDto);
        Response<SleepTrackDto> response = call.execute();
        if(response.isSuccessful()){
            return response.body();
        }else{
            if (response.code() == 401) {
                throw new InvalidLoginException();
            }
            throw new IOException(response.message());
        }
    }







}
