package omelcam934.sleepcalm.devices;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase intermedia entre las vista y el realm. Se encarga de acceder al realm que toca para hacer alli las peticiones
 */
public class DevicesRealm {

    /**
     * Obtiene los dispositivos de todos los Realms
     * @return lista con todos los dispositivos
     */
    public static List<Device> getAllDevices(){
        ArrayList<Device> devices = new ArrayList<>();
        devices.addAll(EndpointDeviceRealm.getEndpointDevices());
        return devices;
    }

    /**
     * Actualiza un dispositivo a estar activo o no
     * @param device dispositivo
     * @param newActiveStatus si esta activo o no
     */
    public static void updateActiveStatus(Device device, boolean newActiveStatus){
        if(device instanceof EndpointDevice)
            EndpointDeviceRealm.updateActiveStatus((EndpointDevice) device, newActiveStatus);
    }

    /**
     * Añade un dispositivo a Realm
     * @param device dispositivo a añadir
     */
    public static void addDevice(Device device){
        if(device instanceof EndpointDevice)
            EndpointDeviceRealm.insertOrUpdate((EndpointDevice) device);
    }

    /**
     * Borra un dispositivo de Realm
     * @param device dispositivo
     */
    public static void borrarDevice(Device device){
        if(device instanceof EndpointDevice)
            EndpointDeviceRealm.borrarEndpointDevice((EndpointDevice) device);
    }

    /**
     * Obtiene todos los dispositivos activos
     * @return lista con los dispositivos activos
     */
    public static List<Device> getAllActiveDevices(){
        List<Device> devices = new ArrayList<>();
        devices.addAll(EndpointDeviceRealm.getActiveEndpointDevices());

        return devices;
    }

}
