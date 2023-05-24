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

public class MainActivity extends AppCompatActivity {

    private SleepService sleepService;
    private FragmentContainerView fragmentHolder;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Inicializar realm
        Realm.init(this);

        SleepService.getSleepService().setContext(this);

        String realmName = "Devices";
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(realmName)
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .compactOnLaunch()
                .build();
        Realm.setDefaultConfiguration(config);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);
        initView();
    }



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

        bottomNavigation.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

            }
        });

        try {
            LoginService.autoLogin(this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidLoginException e) {

        }

    }

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

    public SleepService getSleepService() {
        return sleepService;
    }

    private void initView() {
        fragmentHolder = (FragmentContainerView) findViewById(R.id.fragment_holder);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottomNavigation);
    }
}