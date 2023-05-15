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

import java.util.Arrays;
import java.util.List;


/**
 * Servicio encargado de lo relacionado con el sueño
 */
public class SleepService extends BroadcastReceiver {


    private static SleepService sleepService;
    private PendingIntent sleepReceiverPendingIntent;

    private MainActivity context;
    private boolean active = false;

    private static final int CONSECUTIVE_SLEEP_EVENTS_TO_BE_CONSIDERED_ASLEEP = 3;
    private final int[] sleepTracker = new int[CONSECUTIVE_SLEEP_EVENTS_TO_BE_CONSIDERED_ASLEEP];
    private int newSleepTrackerPos = 0;

    private static final int CONFIDENCE_TO_BE_CONSIDERED_ASLEEP = 265;

    public boolean userAsleep = false;

    /**
     * Obtener el objeto singleton del servicio.
     * @return unico servicio
     */
    public static SleepService getSleepService() {
        if (sleepService == null) {
            sleepService = new SleepService();
        }
        return sleepService;
    }

    /**
     * Crea una petición para la api de sueño
     * @param context contexto de donde se utilice el servicio.
     * @return Peticion para la api de sueño
     */
    private PendingIntent createSleepReceiverPendingIntent(MainActivity context) {
        return PendingIntent.getBroadcast(context, 0, new Intent(context, SleepService.class), PendingIntent.FLAG_CANCEL_CURRENT+PendingIntent.FLAG_MUTABLE);
    }

    /**
     * Activa y desactiva el servicio.
     * @param newStatus activar el servicio
     */
    public void changeStatus(boolean newStatus) {
        if(newStatus != active){
            if(newStatus)
                activateListener();
            else
                deactivateListener();
            active = newStatus;
        }
    }

    public boolean getStatus(){
        return active;
    }


    /**
     * Comienza a recibir actualizaciones sobre el sueño del usuario
     */
    @SuppressLint("MissingPermission")
    private void activateListener() {

        userAsleep = false;

        Log.d("MIMIR", "activando listener");
        ApiService.sendTestMessage("activando listener");

        sleepReceiverPendingIntent = createSleepReceiverPendingIntent(context);
        Task<Void> task = ActivityRecognition.getClient(context).requestSleepSegmentUpdates(
                sleepReceiverPendingIntent,
                SleepSegmentRequest.getDefaultSleepSegmentRequest()
        );


        //Comienza la escucha.
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            //Si la escucha ha comenzado correctamente
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Escuchando...", Toast.LENGTH_SHORT).show();
                Log.d("MIMIR", "Escuchando...");
                ApiService.sendTestMessage("Escuchando...");
            }
        });
    }

    /**
     * Desactiva el servicio de escucha
     */
    private void deactivateListener(){
        Log.d("MIMIR", "desactivando listener");
        ApiService.sendTestMessage("desactivando listener");
        Arrays.fill(sleepTracker, 0);

        Task<Void> task = ActivityRecognition.getClient(context).removeSleepSegmentUpdates(sleepReceiverPendingIntent);

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            //Si la escucha ha parado correctamente
            @Override
            public void onSuccess(Void unused) {
                Log.d("MIMIR", "Parada la escucha.");
                ApiService.sendTestMessage("Parada la escucha");
            }
        });

    }

    /**
     * Obtiene el contexto del servicio. Es requerido para funcionar
     * @param mainActivity la main activity donde se utiliza y esta el fragmento de sueño.
     */
    public void setContext(MainActivity mainActivity){
        context = mainActivity;
    }

    /**
     * Se llama al recibir un mensaje de los servicios.
     * @param context contexto desde el que lo recive
     * @param intent contiene la informacion
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        //Recivido un evento de sueño
        if(SleepSegmentEvent.hasEvents(intent)){

            List<SleepSegmentEvent> events = SleepSegmentEvent.extractEvents(intent);
            Log.d("MIMIR", "eventos de segmento "+events);
            ApiService.sendSegmento("eventos de segmento "+events);

            //TODO: Enviar al back

        }else if(SleepClassifyEvent.hasEvents(intent)){
            //Los eventos de classify contienen la confiazna de que el usuario se haya dormido.
            List<SleepClassifyEvent> events = SleepClassifyEvent.extractEvents(intent);
            Log.d("MIMIR", "eventos de clasificar "+events);
            ApiService.sendClassify("eventos de clasificar "+events);

            events.forEach(event -> {

                //Si el usuario esta dormido no continuaremos
                if(userAsleep)
                    return;

                sleepTracker[newSleepTrackerPos++%sleepTracker.length] = event.getConfidence();

                //Si alguna de las entradas no es mayor que la confianza requerida lo desactivo.
                int sum = 0;
                for (int j : sleepTracker)
                    sum += j;
                ApiService.sendClassify("ARRAY: "+Arrays.toString(sleepTracker)+ " SUM:"+sum+" TRACKERPOS: "+newSleepTrackerPos);
                if(sum<CONFIDENCE_TO_BE_CONSIDERED_ASLEEP)
                    return;

                //Si ha llegado hasta aqui, es que el usuario esta dormido.
                IOTService.getIotService().executeCommands();
                userAsleep = true;

                //No apagamos el servicio ya que apagarlo dejara de obtener los datos para mas adelante las estadisticas.


            });
            //TODO: Enviar al back
        }
    }
}
