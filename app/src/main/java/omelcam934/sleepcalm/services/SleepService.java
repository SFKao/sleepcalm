package omelcam934.sleepcalm.services;

import omelcam934.sleepcalm.activity.MainActivity;
import omelcam934.sleepcalm.devices.DevicesRealm;
import omelcam934.sleepcalm.endpoint.EndpointDeviceApiService;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.SleepClassifyEvent;
import com.google.android.gms.location.SleepSegmentEvent;
import com.google.android.gms.location.SleepSegmentRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

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
    private int[] sleepTracker = new int[CONSECUTIVE_SLEEP_EVENTS_TO_BE_CONSIDERED_ASLEEP];

    private static final int CONFIDENCE_TO_BE_CONSIDERED_ASLEEP = 92;

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


    /**
     * Comienza a recibir actualizaciones sobre el sueño del usuario
     */
    @SuppressLint("MissingPermission")
    private void activateListener() {

        userAsleep = false;

        Log.d("MIMIR", "activando listener");
        EndpointDeviceApiService.sendTestMessage("activando listener");

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
                EndpointDeviceApiService.sendTestMessage("Escuchando...");
            }
        });
    }

    /**
     * Desactiva el servicio de escucha
     */
    private void deactivateListener(){
        Log.d("MIMIR", "desactivando listener");
        EndpointDeviceApiService.sendTestMessage("desactivando listener");
        Task<Void> task = ActivityRecognition.getClient(context).removeSleepSegmentUpdates(sleepReceiverPendingIntent);

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            //Si la escucha ha parado correctamente
            @Override
            public void onSuccess(Void unused) {
                Log.d("MIMIR", "Parada la escucha.");
                EndpointDeviceApiService.sendTestMessage("Parada la escucha");
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
     * @param intent contiene la informacion del evento
     * @param intent contiene la informacion
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        //Recivido un evento de sueño
        if(SleepSegmentEvent.hasEvents(intent)){

            List<SleepSegmentEvent> events = SleepSegmentEvent.extractEvents(intent);
            Log.d("MIMIR", "eventos de segmento "+events);
            EndpointDeviceApiService.sendSegmento("eventos de segmento "+events);

            EndpointDeviceApiService.sendTestMessage(DevicesRealm.getAllDevices().toString());
            //TODO: Enviar al back

        }else if(SleepClassifyEvent.hasEvents(intent)){
            //Los eventos de classify contienen la confiazna de que el usuario se haya dormido.
            List<SleepClassifyEvent> events = SleepClassifyEvent.extractEvents(intent);
            Log.d("MIMIR", "eventos de clasificar "+events);
            EndpointDeviceApiService.sendClassify("eventos de clasificar "+events);

            EndpointDeviceApiService.sendTestMessage(DevicesRealm.getAllDevices().toString());

            events.forEach(event -> {
                addSleepTrack(event.getConfidence());
            });
            //TODO: Enviar al back
        }
    }

    /**
     * Añade la confianza del ultimo evento.
     * @param confidence confianza de que el usuario esta dormido
     */
    public void addSleepTrack(int confidence){
        //Si el usuario esta dormido no continuaremos
        if(userAsleep)
            return;

        //Comienzo shifteando las posiciones dejando la primera vacia.
        if (sleepTracker.length - 1 >= 0)
            System.arraycopy(sleepTracker, 0, sleepTracker, 1, sleepTracker.length - 1);

        sleepTracker[0] = confidence;
        //Si alguna de las entradas no es mayor que la confianza requerida lo desactivo.
        for (int j : sleepTracker)
            if (j < CONFIDENCE_TO_BE_CONSIDERED_ASLEEP)
                return;

        //Si ha llegado hasta aqui, es que el usuario esta dormido.
        IOTService.getIotService().executeCommands();
        userAsleep = true;

        //No apagamos el servicio ya que apagarlo dejara de obtener los datos para mas adelante las estadisticas.
    }
}

