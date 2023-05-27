package omelcam934.sleepcalm.services;

import omelcam934.sleepcalm.devices.Device;
import omelcam934.sleepcalm.devices.DevicesRealm;
import omelcam934.sleepcalm.endpoint.EndpointDeviceApiService;

public class IOTService {

    private static IOTService iotService;

    public static IOTService getIotService(){
        if(iotService == null){
            iotService = new IOTService();
        }
        return iotService;
    }

    public void executeCommands(){
        EndpointDeviceApiService.sendTestMessage("*\n*\nUSUARIO DORMIDO\n*\n*");
        DevicesRealm.getAllActiveDevices().forEach(Device::apagar);
    }

}
