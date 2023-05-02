package omelcam934.sleepcalm.services;

import omelcam934.sleepcalm.activity.MainActivity;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;

import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.SleepClassifyEvent;
import com.google.android.gms.location.SleepSegmentEvent;
import com.google.android.gms.location.SleepSegmentRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class SleepService extends BroadcastReceiver {


    private static SleepService sleepService;
    private PendingIntent sleepReceiverPendingIntent;

    public static SleepService getSleepService() {
        if (sleepService == null) {
            sleepService = new SleepService();
        }
        return sleepService;
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    public PendingIntent createSleepReceiverPendingIntent(MainActivity context) {
        return PendingIntent.getBroadcast(context, 0, new Intent(context, SleepService.class), PendingIntent.FLAG_CANCEL_CURRENT+PendingIntent.FLAG_MUTABLE);
    }

    private MainActivity context;
    private boolean active = false;

    public void changeStatus(boolean newStatus) {
        if(newStatus != active){
            if(newStatus)
                activateListener();
            else
                deactivateListener();
            active = newStatus;
        }
    }


    @SuppressLint("MissingPermission")
    private void activateListener() {

        Log.d("MIMIR", "activando listener");
        ApiService.sendTestMessage("activando listener");

        sleepReceiverPendingIntent = createSleepReceiverPendingIntent(context);
        Task<Void> task = ActivityRecognition.getClient(context).requestSleepSegmentUpdates(
                sleepReceiverPendingIntent,
                SleepSegmentRequest.getDefaultSleepSegmentRequest()
        );

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Escuchando...", Toast.LENGTH_SHORT).show();
                Log.d("MIMIR", "Escuchando...");
                ApiService.sendTestMessage("Escuchando...");
            }
        });


    }



    private void deactivateListener(){
        Log.d("MIMIR", "desactivando listener");
        ApiService.sendTestMessage("desactivando listener");
        Task<Void> task = ActivityRecognition.getClient(context).removeSleepSegmentUpdates(sleepReceiverPendingIntent);

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("MIMIR", "Parada la escucha.");
                ApiService.sendTestMessage("Parada la escucha");
            }
        });

    }

    public void setContext(MainActivity mainActivity){
        context = mainActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        //Recivido un evento de sueño
        if(SleepSegmentEvent.hasEvents(intent)){

            List<SleepSegmentEvent> events = SleepSegmentEvent.extractEvents(intent);
            Log.d("MIMIR", "eventos de segmento "+events);
            ApiService.sendSegmento("eventos de segmento "+events);

            //TODO: Enviar al back

        }else if(SleepClassifyEvent.hasEvents(intent)){
            List<SleepClassifyEvent> events = SleepClassifyEvent.extractEvents(intent);
            Log.d("MIMIR", "eventos de clasificar "+events);
            ApiService.sendClassify("eventos de clasificar "+events);

            //TODO: Enviar al back
        }
    }
}
