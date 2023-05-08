package omelcam934.sleepcalm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;

import omelcam934.sleepcalm.R;
import omelcam934.sleepcalm.services.ApiService;
import omelcam934.sleepcalm.services.SleepService;

public class MainActivity extends AppCompatActivity {

    private SleepService sleepService;
    private ServiceConnection sleepServiceConnection;
    private boolean isSleepServiceBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sleepServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                sleepService = ((SleepService.SleepBinder)service).getService();
                sleepService.setContext(MainActivity.this);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                sleepService = null;
            }
        };
        doBindService();
        setContentView(R.layout.activity_main);
    }

    private void doBindService() {
        System.out.println("AAAAAAAA");
        System.out.println(bindService(new Intent(this,SleepService.class), sleepServiceConnection, Context.BIND_AUTO_CREATE));
        isSleepServiceBound = true;
    }

    private void doUnbindService(){
        if(isSleepServiceBound){
            if(sleepService!=null){
                ApiService.sendTestMessage("Matando servicio");
                unbindService(sleepServiceConnection);
                sleepService = null;
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        doUnbindService();
    }

    public SleepService getSleepService() {
        return sleepService;
    }
}