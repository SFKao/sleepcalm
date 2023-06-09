package omelcam934.sleepcalm.endpoint;

import android.content.Context;
import android.util.Log;

import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import omelcam934.sleepcalm.endpoint.dto.LoginDto;
import omelcam934.sleepcalm.endpoint.dto.NewPasswordDTO;
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

/**
 * Clase para acceder al endpoint del backend
 */
public class BackendApiService {

    private static final Retrofit retrofit = new Retrofit.Builder()
            //.baseUrl("http://sfkao.bounceme.net:25577/api/")
            .baseUrl("http://192.168.18.4:8080/api/") //IP Local para poder acceder desde mi hogar ya que mi ISP no permite conexiones boomerang
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())) //Activamo setLenient para que permita obtener el token si tiene un enter en este
            .build();

    //Formato con el que el backend se comunica con fechas
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
    public static void registro(String username, String email, String password, Context context) throws IOException, EmailOrUsernameInUseException, InvalidEmailException, InvalidLoginException {

        BackendApiServiceInterface backendApiServiceInterface = retrofit.create(BackendApiServiceInterface.class);
        RegistroDto payload = new RegistroDto(username,email,password);
        Call<Boolean> call = backendApiServiceInterface.registrar(payload);
        Response<Boolean> response = call.execute();

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
        LoginService.login(username, password, context, true);
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

    /**
     * Mira si un token es valido, para pruebas
     * @return true si es valido
     * @throws IOException Si no se ha podido conectar
     */
    public static boolean checkToken() throws IOException {
        BackendApiServiceInterface backendApiServiceInterface = retrofit.create(BackendApiServiceInterface.class);
        Call<Boolean> call = backendApiServiceInterface.checkToken("Bearer "+LoginService.getToken());
        Response<Boolean> response = call.execute();
        return response.isSuccessful();
    }

    /**
     * Obtiene la semana actual con las fechas de inicio y fin de sueño
     * @param context contexto desde donde se llama
     * @return la semana
     * @throws InvalidLoginException si no se ha podido hacer login en caso de que el token este fallido
     * @throws IOException si no se ha podido conectar al backend
     */
    public static WeekDto getCurrentWeek(Context context) throws InvalidLoginException, IOException {
        return getCurrentWeek(context, false);
    }

    /**
     * Obtiene la semana actual con las fechas de inicio y fin de sueño. Recursiva
     * @param context contexto desde donde se llama
     * @param triedLogin si se ha intentado hacer login en caso de que sea desautorizado
     * @return la semana
     * @throws InvalidLoginException si no se ha podido hacer login en caso de que el token este fallido
     * @throws IOException si no se ha podido conectar al backend
     */
    private static WeekDto getCurrentWeek(Context context, boolean triedLogin) throws InvalidLoginException, IOException {
        if(!LoginService.isLoguedIn()){
            LoginService.autoLogin(context);
            triedLogin = true;
        }

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
                    LoginService.newToken(context);
                    getCurrentWeek(context,true);
                }
            }
            throw new IOException(response.message());
        }
    }

    /**
     * Obtiene la semana de la fecha elegida con las fechas de inicio y fin de sueño
     * @param context contexto desde donde se llama
     * @return la semana
     * @throws InvalidLoginException si no se ha podido hacer login en caso de que el token este fallido
     * @throws IOException si no se ha podido conectar al backend
     */
    public static WeekDto getWeek(Context context, Date date) throws InvalidLoginException, IOException {
        return getWeek(context, date, false);
    }

    /**
     * Obtiene la semana de la fecha elegida con las fechas de inicio y fin de sueño. Recursiva
     * @param context contexto desde donde se llama
     * @param triedLogin si se ha intentado hacer login en caso de que sea desautorizado
     * @return la semana
     * @throws InvalidLoginException si no se ha podido hacer login en caso de que el token este fallido
     * @throws IOException si no se ha podido conectar al backend
     */
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

    /**
     * Envia al backend el inicio y final de sueño del usuario para mantener las estadisticas. Hace login antes ya que se llamara por la noche y el token habra expirado
     * @param context contexto desde donde se llama
     * @param horaDeInicio hora cuando se durmio el usuario
     * @param horaDeFin hora cuando se desperto
     * @return el dato con el campo de dia rellenado
     * @throws InvalidLoginException si no se ha podido hacer login en el servidor
     * @throws IOException si no se ha podido conectar con el servidor
     */
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

    /**
     * Notifica al backend que el email seleccionado requiere una nueva contraseña
     * @param email email de la cuenta que requiere un nuevo mail
     * @return si ha funcionado
     * @throws IOException si no se ha podido realizar la conexion
     */
    public static boolean forgotPassword(String email) throws IOException {
        BackendApiServiceInterface backendApiServiceInterface = retrofit.create(BackendApiServiceInterface.class);
        Call<Void> call = backendApiServiceInterface.forgotPassword(email);
        Response<Void> response = call.execute();
        return response.isSuccessful();
    }

    /**
     * Envia la nueva contraseña al backend
     * @param email email del usuario que quiere cambiar la contraseña
     * @param lastPassword contraseña actual
     * @param newPassword nueva contraseña
     * @return si ha funcionado
     * @throws IOException si no se ha podido realizar la conexion
     */
    public static boolean changePassword(String email, String lastPassword, String newPassword) throws IOException {
        BackendApiServiceInterface backendApiServiceInterface = retrofit.create(BackendApiServiceInterface.class);
        Call<Void> call = backendApiServiceInterface.updatePassword(new NewPasswordDTO(email, lastPassword, newPassword));
        Response<Void> response = call.execute();
        return response.isSuccessful();
    }





}
