package omelcam934.sleepcalm.services.sleeptrack;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import io.realm.Realm;

public class LocalSleepTrackRealm {

    public static LocalSleepTrack recreateLocalSleepTrack(){
        Realm realm = Realm.getDefaultInstance();
        AtomicReference<LocalSleepTrack> localSleepTrack = new AtomicReference<>();
        realm.executeTransaction(realm1 -> {
            realm1.delete(LocalSleepTrack.class);
            localSleepTrack.set(realm1.createObject(LocalSleepTrack.class, LocalSleepTrack.ID));
        });
        return localSleepTrack.get();
    }

    public static int getSumOfConfidences(){
        Realm realm = Realm.getDefaultInstance();
        AtomicInteger sum = new AtomicInteger();
        realm.executeTransaction(realm1 -> {
            sum.set(Objects.requireNonNull(realm1.where(LocalSleepTrack.class).findFirst()).getSumOfConfidences());
        });
        return sum.get();
    }

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

    public static void setUserAsleep(boolean newAsleep){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {
            LocalSleepTrack lst = realm1.where(LocalSleepTrack.class).findFirst();
            lst.setUserAsleep(newAsleep);
            realm1.insertOrUpdate(lst);
        });
    }

    public static Date getHoraDeiInicio(){
        Realm realm = Realm.getDefaultInstance();
        AtomicReference<Date> ret = new AtomicReference<>();
        realm.executeTransaction(realm1 -> {
            ret.set(realm1.where(LocalSleepTrack.class).findFirst().getHoraDeInicio());
        });
        return ret.get();
    }

    public static void setHoraDeInicio(Date newHoraDeInicio){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {
            LocalSleepTrack lst = realm1.where(LocalSleepTrack.class).findFirst();
            lst.setHoraDeInicio(newHoraDeInicio);
            realm1.insertOrUpdate(lst);
        });
    }


}
