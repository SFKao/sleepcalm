package omelcam934.sleepcalm.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

import omelcam934.sleepcalm.endpoint.BackendApiService;
import omelcam934.sleepcalm.endpoint.exceptions.InvalidLoginException;

/**
 * Servicio para encargarse de lo relacionado con el login y persistir los datos del usuario de forma segura
 */
public class LoginService {

    private static String token;

    /**
     * Hace el login de un usuario y guarda el token. Se ha de usar el saveLogin si no es automatico
     * @param usernameOrEmail usuario o email para el login
     * @param password contraseña
     * @param context contexto donde se llama
     * @param saveLogin true si la llamada no es para nuevo token
     * @throws InvalidLoginException si falla el login
     * @throws IOException si hay un problema en la llamada
     */
    public static void login(String usernameOrEmail, String password, Context context, boolean saveLogin) throws InvalidLoginException, IOException {

        token = BackendApiService.login(usernameOrEmail,password);
        
        if(saveLogin) {

            String masterKeyAlias = null;
            try {
                masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            } catch (GeneralSecurityException | IOException e) {
                throw new IOException(e);
            }
            SharedPreferences sharedPreferences = null;
            try {
                sharedPreferences = EncryptedSharedPreferences.create(
                        "loginSleepCalm",
                        masterKeyAlias,
                        context,
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                );
            } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace();
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("usernameOrEmail", usernameOrEmail);
            editor.putString("password", password);
            editor.apply();
            Log.d("MIMIR","Guardados datos");
        }

    }

    public static String getToken() {
        return token;
    }

    public static boolean isLoguedIn(){
        return token != null;
    }

    /**
     * Crea un nuevo token logueandose de nuevo
     * @param context contexto desde donde se llama
     * @throws IOException si no permite la conexion con el back o no se puede acceder al archivo
     * @throws InvalidLoginException si los datos de login son invalidos
     */
    public static void newToken(Context context) throws IOException, InvalidLoginException {
        Log.d("MIMIR","Creando nuevo token");
        String masterKeyAlias = null;
        try {
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        } catch (GeneralSecurityException | IOException e) {
            throw new IOException(e);
        }
        SharedPreferences sharedPreferences = null;
        try {
            sharedPreferences = EncryptedSharedPreferences.create(
                    "loginSleepCalm",
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            throw new IOException(e);
        }

        String usernameOrEmail = sharedPreferences.getString("usernameOrEmail","");
        if(usernameOrEmail.equals("")){
            throw new InvalidLoginException();
        }
        String password = sharedPreferences.getString("password","");

        login(usernameOrEmail,password,context,false);

    }

    /**
     * Hace login al iniciar la app
     * @param context contexto desde donde se llama
     * @throws IOException si no permite la conexion con el back o no se puede acceder al archivo
     * @throws InvalidLoginException si los datos de login son invalidos
     */
    public static void autoLogin(Context context) throws IOException, InvalidLoginException {
        if(token != null)
            return;

        String masterKeyAlias = null;
        try {
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        } catch (GeneralSecurityException | IOException e) {
            throw new IOException(e);
        }
        SharedPreferences sharedPreferences = null;
        try {
            sharedPreferences = EncryptedSharedPreferences.create(
                    "loginSleepCalm",
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            throw new IOException(e);
        }

        String username = sharedPreferences.getString("usernameOrEmail","");
        if(username.equals("")){
            return;
        }
        String usernameOrEmail = sharedPreferences.getString("usernameOrEmail","");
        String password = sharedPreferences.getString("password","");

        login(usernameOrEmail,password,context,false);

    }

    /**
     * Cierra sesion y borra los datos del telefono
     * @param context contexto desde donde se llama
     * @throws IOException si no permite la conexion con el back o no se puede acceder al archivo
     */
    public static void logout(Context context) throws IOException {
        token = null;

        String masterKeyAlias = null;
        try {
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        } catch (GeneralSecurityException | IOException e) {
            throw new IOException(e);
        }
        SharedPreferences sharedPreferences = null;
        try {
            sharedPreferences = EncryptedSharedPreferences.create(
                    "loginSleepCalm",
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("usernameOrEmail");
        editor.remove("password");
        editor.apply();
    }
}
