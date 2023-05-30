package omelcam934.sleepcalm.services.sleeptrack;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import io.realm.Realm;

/**
 * Permite acceder y modificar a distinta información del LocalSleepTrack para modificarlos. El modo escucha no puede almacenar datos en el y por ello hace falta esto.
 */
public class LocalSleepTrackRealm {

    /**
     * Borra y crea un nuevo sleep track para vaciarlo
     * @return
     */
    public static LocalSleepTrack recreateLocalSleepTrack(){
        Realm realm = Realm.getDefaultInstance();
        AtomicReference<LocalSleepTrack> localSleepTrack = new AtomicReference<>();
        realm.executeTransaction(realm1 -> {
            realm1.delete(LocalSleepTrack.class);
            localSleepTrack.set(realm1.createObject(LocalSleepTrack.class, LocalSleepTrack.ID));
        });
        return localSleepTrack.get();
    }

    /**
     * Obtiene la suma de confianza
     * @return suma de las confianzas
     */
    public static int getSumOfConfidences(){
        Realm realm = Realm.getDefaultInstance();
        AtomicInteger sum = new AtomicInteger();
        realm.executeTransaction(realm1 -> {
            sum.set(Objects.requireNonNull(realm1.where(LocalSleepTrack.class).findFirst()).getSumOfConfidences());
        });
        return sum.get();
    }

    /**
     * Añade un nuevo valor de confianza de que el usuario esta dormido y devuelve su suma
     * @param newConfidence nueva confianza
     * @return suma de las cofianzas
     */
    public static int addConfidence(int newConfidence){
        Realm realm = Realm.getDefaultInstance();
        AtomicInteger sum = new AtomicInteger();
        realm.executeTransaction(realm1 -> {
            LocalSleepTrack localSleepTrack = realm1.where(LocalSleepTrack.class).findFirst();
            localSleepTrack.setConfidence3(localSleepTrack.getConfidence2());
            localSleepTrack.setConfidence2(localSleepTrack.getConfidence1());
            localSleepTrack.setConfidence1(newConfidence);
            realm1.insertOrUpdate(localSleepTrack);
            sum.set(localSleepTrack.getSumOfConfidences());
        });
        return sum.get();
    }

    /**
     * Devuelve si el usuario esta dormido
     * @return si el usuario esta dormido
     */
    public static boolean isUserAsleep(){
        Realm realm = Realm.getDefaultInstance();
        AtomicBoolean ret = new AtomicBoolean(false);
        realm.executeTransaction(realm1 -> {
            LocalSleepTrack localSleepTrack = realm1.where(LocalSleepTrack.class).findFirst();
            if(localSleepTrack!=null)
                ret.set(localSleepTrack.isUserAsleep());
        });
        return ret.get();
    }

    /**
     * Almacena si el usuario esta dormido
     * @param newAsleep nuevo estado de dormido
     */
    public static void setUserAsleep(boolean newAsleep){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {
            LocalSleepTrack lst = realm1.where(LocalSleepTrack.class).findFirst();
            lst.setUserAsleep(newAsleep);
            realm1.insertOrUpdate(lst);
        });
    }

    /**
     * Obtiene la hora de inicio de sueño del usuario
     * @return hora a la que se durmio si tiene
     */
    public static Date getHoraDeiInicio(){
        Realm realm = Realm.getDefaultInstance();
        AtomicReference<Date> ret = new AtomicReference<>();
        realm.executeTransaction(realm1 -> {
            ret.set(realm1.where(LocalSleepTrack.class).findFirst().getHoraDeInicio());
        });
        return ret.get();
    }

    /**
     * Almacena la hora a la que se durmio el usuario
     * @param newHoraDeInicio hora a la que se durmio
     */
    public static void setHoraDeInicio(Date newHoraDeInicio){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {
            LocalSleepTrack lst = realm1.where(LocalSleepTrack.class).findFirst();
            lst.setHoraDeInicio(newHoraDeInicio);
            realm1.insertOrUpdate(lst);
        });
    }


}
