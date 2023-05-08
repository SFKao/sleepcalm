package omelcam934.sleepcalm.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.Arrays;


/**
 * Servicio encargado de lo relacionado con el sueño
 */
public class SleepService extends Service {
    private Context context;
    private SleepBroadcastReceiver sleepBroadcastReceiver;
    private final SleepBinder sleepBinder = new SleepBinder();

    private boolean active = false;

    private static final int CONSECUTIVE_SLEEP_EVENTS_TO_BE_CONSIDERED_ASLEEP = 3;
    private final int[] sleepTracker = new int[CONSECUTIVE_SLEEP_EVENTS_TO_BE_CONSIDERED_ASLEEP];
    private int newSleepTrackerPos = 0;

    private static final int CONFIDENCE_TO_BE_CONSIDERED_ASLEEP = 265;

    public boolean userAsleep = false;

    public SleepService() {
    }

    public void setContext(Context context) {
        this.context = context;
        sleepBroadcastReceiver = new SleepBroadcastReceiver(this,this);
    }

    public class SleepBinder extends Binder{
        public SleepService getService(){
            return SleepService.this;
        }
    }

    /**
     * Activa y desactiva el servicio.
     * @param newStatus activar el servicio
     */
    public void changeStatus(boolean newStatus) {
        if(newStatus != active){
            if(newStatus)
                sleepBroadcastReceiver.activateListener();
            else
                sleepBroadcastReceiver.deactivateListener();
            active = newStatus;
        }
    }

    public boolean getStatus(){
        return active;
    }

    public synchronized void sendConfidence(int confidence){

        //Si el usuario esta dormido no continuaremos
        if(userAsleep)
            return;

        sleepTracker[newSleepTrackerPos++%sleepTracker.length] = confidence;

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

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sleepBinder;
    }

    @Override
    public void onDestroy() {
        ApiService.sendTestMessage("Servicio muerto");
        super.onDestroy();
    }
}
