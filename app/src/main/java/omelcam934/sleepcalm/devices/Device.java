package omelcam934.sleepcalm.devices;

import java.util.Objects;


//Esto iba a ser una clase abastacta. Sin embargo, para la persistencia con Realm
//la clase a almacenar ha de extender de RealmObject, y java no permite
//extender de 2 distintas
public interface Device {

    String getNombre();

    void setNombre(String nombre);

    int getIcon();

    void setIcon(int icon);

    boolean isActive();

    void setActive(boolean active);

    void apagar();

}
