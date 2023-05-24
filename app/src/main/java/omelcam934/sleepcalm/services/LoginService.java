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
        Log.d("MIMIR","Iniciando login");
        token = BackendApiService.login(usernameOrEmail,password);
        Log.d("MIMIR","TOKEN: "+token);
        
        if(saveLogin) {
            Log.d("MIMIR","Guardando datos del login");
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
        Log.d("MIMIR","Terminado login");
    }

    public static String getToken() {
        return token;
    }

    public static boolean isLoguedIn(){
        return token != null;
    }

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
        Log.d("MIMIR","Terminado de crear nuevo token");
    }

    public static void autoLogin(Context context) throws IOException, InvalidLoginException {
        if(token != null)
            return;
        Log.d("MIMIR","Obteniendo datos para auto login");
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
        Log.d("MIMIR","Lanzando login con datos recogidos");
        login(usernameOrEmail,password,context,false);
        Log.d("MIMIR","Terminado auto login");
    }
}
