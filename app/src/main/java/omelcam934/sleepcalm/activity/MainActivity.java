package omelcam934.sleepcalm.activity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import omelcam934.sleepcalm.R;
import omelcam934.sleepcalm.endpoint.exceptions.InvalidLoginException;
import omelcam934.sleepcalm.fragment.DevicesFragment;
import omelcam934.sleepcalm.fragment.ListenerFragment;
import omelcam934.sleepcalm.fragment.StadisticsFragment;
import omelcam934.sleepcalm.fragment.UserFragment;
import omelcam934.sleepcalm.services.LoginService;
import omelcam934.sleepcalm.services.SleepService;

/**
 * Activity principal donde esta todo
 */
public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    private static boolean isRealmActive = false;

    /**
     * Se llama al crearse, inicializa las configuraciones la primera vez que se llama
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Inicializar realm
        Realm.init(this);

        SleepService.getSleepService().setContext(this);

        if(!isRealmActive) {
            String realmName = "devices.realm";
            RealmConfiguration config = new RealmConfiguration.Builder()
                    .name(realmName)
                    .allowQueriesOnUiThread(true)
                    .allowWritesOnUiThread(true)
                    .compactOnLaunch()
                    .build();

            Realm.setDefaultConfiguration(config);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            isRealmActive = true;
        }
        setContentView(R.layout.activity_main);
        initView();
    }


    /**
     * Añade los metodos a los botones
     */
    @Override
    protected void onStart() {
        super.onStart();

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_listener:
                        changeFragment(new ListenerFragment());
                        return true;
                    case R.id.menu_dispositivos:
                        changeFragment(new DevicesFragment());
                        return true;
                    case R.id.menu_estadisticas:
                        changeFragment(new StadisticsFragment());
                        return true;
                    case R.id.menu_usuario:
                        changeFragment(new UserFragment());
                        return true;
                }


                return false;
            }
        });

        //Intento obtener un token
        try {
            LoginService.autoLogin(this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidLoginException ignored) {

        }

    }

    /**
     * Cambio el contenido de la pantalla al fragmento elegido
     * @param fragment
     */
    private void changeFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_holder, fragment)
                .commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    /**
     * Inicializa los componentes
     */
    private void initView() {

        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottomNavigation);
    }
}