package omelcam934.sleepcalm.services.sleeptrack;


import java.util.Date;
import java.util.Objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Clase para persistencia local de los datos del listener.
 */
public class LocalSleepTrack extends RealmObject {

    public static int ID = 1;

    //Solo queremos almacenar 1 para mantener persistencia de la noche.
    @PrimaryKey
    private int id = ID;

    private int confidence1 = 0;
    private int confidence2 = 0;
    private int confidence3 = 0;

    private boolean userAsleep = false;

    private Date horaDeInicio;

    public LocalSleepTrack() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConfidence1() {
        return confidence1;
    }

    public void setConfidence1(int confidence1) {
        this.confidence1 = confidence1;
    }

    public int getConfidence2() {
        return confidence2;
    }

    public void setConfidence2(int confidence2) {
        this.confidence2 = confidence2;
    }

    public int getConfidence3() {
        return confidence3;
    }

    public void setConfidence3(int confidence3) {
        this.confidence3 = confidence3;
    }

    public int getSumOfConfidences(){
        return confidence1+confidence2+confidence3;//
    }

    public boolean isUserAsleep() {
        return userAsleep;
    }

    public void setUserAsleep(boolean userAsleep) {
        this.userAsleep = userAsleep;
    }

    public Date getHoraDeInicio() {
        return horaDeInicio;
    }

    public void setHoraDeInicio(Date horaDeInicio) {
        this.horaDeInicio = horaDeInicio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocalSleepTrack)) return false;
        LocalSleepTrack that = (LocalSleepTrack) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "LocalSleepTrack{" +
                "id=" + id +
                ", confidence1=" + confidence1 +
                ", confidence2=" + confidence2 +
                ", confidence3=" + confidence3 +
                ", userAsleep=" + userAsleep +
                ", horaDeInicio=" + horaDeInicio +
                '}';
    }
}
