package omelcam934.sleepcalm.endpoint.dto;

import java.io.Serializable;
import java.util.Objects;

public class SleepTrackDto implements Serializable {
    private final String horaDeInicio;
    private final String horaDeFin;
    private final String dia;

    public SleepTrackDto(String horaDeInicio, String horaDeFin, String dia) {
        this.horaDeInicio = horaDeInicio;
        this.horaDeFin = horaDeFin;
        this.dia = dia;
    }

    public String getHoraDeInicio() {
        return horaDeInicio;
    }

    public String getHoraDeFin() {
        return horaDeFin;
    }

    public String getDia() {
        return dia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SleepTrackDto entity = (SleepTrackDto) o;
        return Objects.equals(this.horaDeInicio, entity.horaDeInicio) &&
                Objects.equals(this.horaDeFin, entity.horaDeFin) &&
                Objects.equals(this.dia, entity.dia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(horaDeInicio, horaDeFin, dia);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "horaDeInicio = " + horaDeInicio + ", " +
                "horaDeFin = " + horaDeFin + ", " +
                "dia = " + dia + ")";
    }
}
