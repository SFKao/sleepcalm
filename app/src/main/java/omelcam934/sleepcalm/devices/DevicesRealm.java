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

}
