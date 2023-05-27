package omelcam934.sleepcalm.services;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.SleepClassifyEvent;
import com.google.android.gms.location.SleepSegmentEvent;
import com.google.android.gms.location.SleepSegmentRequest;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import omelcam934.sleepcalm.activity.MainActivity;
import omelcam934.sleepcalm.endpoint.BackendApiService;
import omelcam934.sleepcalm.endpoint.EndpointDeviceApiService;
import omelcam934.sleepcalm.endpoint.exceptions.InvalidLoginException;
import omelcam934.sleepcalm.services.sleeptrack.LocalSleepTrack;
import omelcam934.sleepcalm.services.sleeptrack.LocalSleepTrackRealm;


/**
 * Servicio encargado de lo relacionado con el sueño
 */
public class SleepService extends BroadcastReceiver {


    private static SleepService sleepService;
    private PendingIntent sleepReceiverPendingIntent;

    private MainActivity context;
    private boolean active = false;

    private static final int CONFIDENCE_TO_BE_CONSIDERED_ASLEEP = 265;
    private static final int CONFIDENCE_TO_BE_AWAKE = 40;

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

    public boolean isActive() {
        return active;
    }

    /**
     * Comienza a recibir actualizaciones sobre el sueño del usuario
     */
    @SuppressLint("MissingPermission")
    private void activateListener() {

        Log.d("MIMIR", "activando listener");
        EndpointDeviceApiService.sendTestMessage("activando listener");

        sleepReceiverPendingIntent = createSleepReceiverPendingIntent(context);
        Task<Void> task = ActivityRecognition.getClient(context).requestSleepSegmentUpdates(
                sleepReceiverPendingIntent,
                SleepSegmentRequest.getDefaultSleepSegmentRequest()
        );


        //Comienza la escucha.
        //Si la escucha ha comenzado correctamente
        task.addOnSuccessListener(unused -> {
            Toast.makeText(context, "Escuchando...", Toast.LENGTH_SHORT).show();
            Log.d("MIMIR", "Escuchando...");
            EndpointDeviceApiService.sendTestMessage("Escuchando...");

            if(LocalSleepTrackRealm.isUserAsleep())
                sendDataToBack();

            LocalSleepTrackRealm.recreateLocalSleepTrack();
        });
    }

    /**
     * Desactiva el servicio de escucha
     */
    private void deactivateListener(){
        Log.d("MIMIR", "desactivando listener");
        EndpointDeviceApiService.sendTestMessage("desactivando listener");
        Task<Void> task = ActivityRecognition.getClient(context).removeSleepSegmentUpdates(sleepReceiverPendingIntent);

        //Si la escucha ha parado correctamente
        task.addOnSuccessListener(unused -> {
            Log.d("MIMIR", "Parada la escucha.");
            EndpointDeviceApiService.sendTestMessage("Parada la escucha");
            if(LocalSleepTrackRealm.isUserAsleep()){
                sendDataToBack();
                this.active = false;
            }
        });

    }

    private void sendDataToBack(){
        EndpointDeviceApiService.sendTestMessage(LocalSleepTrackRealm.getHoraDeiInicio().toString());
        EndpointDeviceApiService.sendTestMessage(new Date().toString());

        try {
            BackendApiService.postSleepTrack(context, LocalSleepTrackRealm.getHoraDeiInicio(), new Date());
        } catch (InvalidLoginException | IOException ignored) {}

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
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        //Recivido un evento de sueño
        if(SleepSegmentEvent.hasEvents(intent)){

            List<SleepSegmentEvent> events = SleepSegmentEvent.extractEvents(intent);
            //De momento los envio al server de prueba pero probablemente se vaya
            EndpointDeviceApiService.sendSegmento("eventos de segmento "+events);
        }else if(SleepClassifyEvent.hasEvents(intent)){
            //Los eventos de classify contienen la confiazna de que el usuario se haya dormido.
            List<SleepClassifyEvent> events = SleepClassifyEvent.extractEvents(intent);
            Log.d("MIMIR", "eventos de clasificar "+events);
            EndpointDeviceApiService.sendClassify("eventos de clasificar "+events);

            events.forEach(event -> addSleepTrack(event.getConfidence()));
        }
    }

    /**
     * Añade la confianza del ultimo evento.
     * @param confidence confianza de que el usuario esta dormido
     */
    public void addSleepTrack(int confidence){
        //Si el usuario esta dormido vamos a mirar si se desperto
        if(LocalSleepTrackRealm.isUserAsleep()) {
            if(LocalSleepTrackRealm.addConfidence(confidence)<CONFIDENCE_TO_BE_AWAKE)
                this.changeStatus(false);
            return;
        }

        //Si no esta dormido, vamos a añadir la confianza y miraremos si es menos que la necesaria para considerarlo dormido

        if (LocalSleepTrackRealm.addConfidence(confidence) < CONFIDENCE_TO_BE_CONSIDERED_ASLEEP)
            return;

        //Si ha llegado hasta aqui, es que el usuario esta dormido.

        IOTService.getIotService().executeCommands();
        LocalSleepTrackRealm.setUserAsleep(true);
        LocalSleepTrackRealm.setHoraDeInicio(new Date());

        //No apagamos el servicio ya que apagarlo dejara de obtener los datos para mas adelante las estadisticas.
    }
}

