package omelcam934.sleepcalm.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import omelcam934.sleepcalm.R;
import omelcam934.sleepcalm.fragment.DevicesFragment;
import omelcam934.sleepcalm.fragment.ListenerFragment;
import omelcam934.sleepcalm.services.ApiService;
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
                }


                return false;
            }
        });

        bottomNavigation.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

            }
        });

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