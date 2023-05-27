package omelcam934.sleepcalm.devices;

import java.util.ArrayList;
import java.util.List;

public class DevicesRealm {

    public static List<Device> getAllDevices(){
        ArrayList<Device> devices = new ArrayList<>();
        devices.addAll(EndpointDeviceRealm.getEndpointDevices());
        return devices;
    }

    public static void updateActiveStatus(Device device, boolean newActiveStatus){
        if(device instanceof EndpointDevice)
            EndpointDeviceRealm.updateActiveStatus((EndpointDevice) device, newActiveStatus);
    }

    public static void addDevice(Device device){
        if(device instanceof EndpointDevice)
            EndpointDeviceRealm.insertOrUpdate((EndpointDevice) device);
    }

    public static void borrarDevice(Device device){
        if(device instanceof EndpointDevice)
            EndpointDeviceRealm.borrarEndpointDevice((EndpointDevice) device);
    }

    public static List<Device> getAllActiveDevices(){
        List<Device> devices = new ArrayList<>();
        devices.addAll(EndpointDeviceRealm.getActiveEndpointDevices());

        return devices;
    }

}
