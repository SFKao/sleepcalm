package omelcam934.sleepcalm.devices;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Clase para gestionar la presistencia de los Endpoint Devices en Realm
 */
public class EndpointDeviceRealm {

    /**
     * Inserta o actualiza un dispositivo
     * @param endpointDevice dispositivo a persistir
     */
    public static void insertOrUpdate(EndpointDevice endpointDevice){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> realm1.insertOrUpdate(endpointDevice));
    }

    /**
     * Obtiene todos los dispositivos de endpoint
     * @return dispositivos
     */
    public static List<Device> getEndpointDevices(){
        Realm realm = Realm.getDefaultInstance();
        final ArrayList<Device> devices = new ArrayList<>();
        realm.executeTransaction(realm1 -> {
            RealmResults<EndpointDevice> all = realm1.where(EndpointDevice.class).findAll();
            devices.addAll(all);
        });
        return devices;
    }

    /**
     * Borra un dispositivo de endpoint
     * @param endpointDevice dispositivo a borrar
     */
    public static void borrarEndpointDevice(EndpointDevice endpointDevice){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> endpointDevice.deleteFromRealm());
    }

    /**
     * Actualiza el estado de un dispositivo
     * @param endpointDevice dispositivo a actualizar
     * @param newActiveStatus nuevo estado
     */
    public static void updateActiveStatus(EndpointDevice endpointDevice, boolean newActiveStatus){
        Realm.getDefaultInstance().executeTransaction(realm -> {
            endpointDevice.setActive(newActiveStatus);
            realm.insertOrUpdate(endpointDevice);
        });
    }

    /**
     * Obtiene todos los dispositivos activos de endpoint
     * @return lista de dispositivos
     */
    public static List<Device> getActiveEndpointDevices(){
        Realm realm = Realm.getDefaultInstance();
        final ArrayList<Device> devices = new ArrayList<>();
        realm.executeTransaction(realm1 -> {
            RealmResults<EndpointDevice> all = realm1.where(EndpointDevice.class).equalTo("active",true).findAll();
            devices.addAll(all);
        });
        return devices;
    }



}
