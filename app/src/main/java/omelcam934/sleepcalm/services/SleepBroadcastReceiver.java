package omelcam934.sleepcalm.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.SleepClassifyEvent;
import com.google.android.gms.location.SleepSegmentEvent;
import com.google.android.gms.location.SleepSegmentRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class SleepBroadcastReceiver //extends BroadcastReceiver
{
//
//    private PendingIntent sleepReceiverPendingIntent;
//    private final SleepService sleepService;
//    private final Context context;
//
//    public SleepBroadcastReceiver(SleepService sleepService, Context context) {
//        this.sleepService = sleepService;
//        this.context = context;
//    }
//
//    /**
//     * Crea una petición para la api de sueño
//     * @param context contexto de donde se utilice el servicio.
//     * @return Peticion para la api de sueño
//     */
//    private PendingIntent createSleepReceiverPendingIntent(Context context) {
//        Log.d("MIMIR", "create sleep receiver pending intent");
//        return PendingIntent.getBroadcast(context, 0, new Intent(context, SleepBroadcastReceiver.class), PendingIntent.FLAG_CANCEL_CURRENT + PendingIntent.FLAG_MUTABLE);
//    }
//
//
//    /**
//     * Comienza a recibir actualizaciones sobre el sueño del usuario
//     */
//    public void activateListener() {
//        Log.d("MIMIR", "activando listener");
//        ApiService.sendTestMessage("activando listener");
//
//        sleepReceiverPendingIntent = createSleepReceiverPendingIntent(context);
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
//            ApiService.sendTestMessage("Faltan permisos");
//            return;
//        }
//        Task<Void> task = ActivityRecognition.getClient(context).requestSleepSegmentUpdates(
//                sleepReceiverPendingIntent,
//                SleepSegmentRequest.getDefaultSleepSegmentRequest()
//        );
//
//
//        //Comienza la escucha.
//        task.addOnSuccessListener(new OnSuccessListener<Void>() {
//            //Si la escucha ha comenzado correctamente
//            @Override
//            public void onSuccess(Void unused) {
//                Toast.makeText(context, "Escuchando...", Toast.LENGTH_SHORT).show();
//                Log.d("MIMIR", "Escuchando...");
//                ApiService.sendTestMessage("Escuchando...");
//            }
//        });
//    }
//
//    /**
//     * Desactiva el servicio de escucha
//     */
//    public void deactivateListener(){
//        Log.d("MIMIR", "desactivando listener");
//        ApiService.sendTestMessage("desactivando listener");
//        Task<Void> task = ActivityRecognition.getClient(context).removeSleepSegmentUpdates(sleepReceiverPendingIntent);
//
//        task.addOnSuccessListener(new OnSuccessListener<Void>() {
//            //Si la escucha ha parado correctamente
//            @Override
//            public void onSuccess(Void unused) {
//                Log.d("MIMIR", "Parada la escucha.");
//                ApiService.sendTestMessage("Parada la escucha");
//            }
//        });
//    }
//
//    /**
//     * Se llama al recibir un mensaje de los servicios.
//     * @param context contexto desde el que lo recive
//     * @param intent contiene la informacion
//     */
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        ApiService.sendTestMessage("Recivido");
//        //Recivido un evento de sueño
//        if(SleepSegmentEvent.hasEvents(intent)){
//
//            List<SleepSegmentEvent> events = SleepSegmentEvent.extractEvents(intent);
//            Log.d("MIMIR", "eventos de segmento "+events);
//            ApiService.sendSegmento("eventos de segmento "+events);
//
//            //TODO: Enviar al back
//
//        }else if(SleepClassifyEvent.hasEvents(intent)){
//            //Los eventos de classify contienen la confiazna de que el usuario se haya dormido.
//            List<SleepClassifyEvent> events = SleepClassifyEvent.extractEvents(intent);
//            Log.d("MIMIR", "eventos de clasificar "+events);
//            ApiService.sendClassify("eventos de clasificar "+events);
//
//            events.forEach(event -> {
//                sleepService.sendConfidence(event.getConfidence());
//            });
//            //TODO: Enviar al back
//        }
//    }


}
