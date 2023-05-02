package omelcam934.sleepcalm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import omelcam934.sleepcalm.R;
import omelcam934.sleepcalm.services.SleepService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SleepService.getSleepService().setContext(this);
        setContentView(R.layout.activity_main);
    }

}