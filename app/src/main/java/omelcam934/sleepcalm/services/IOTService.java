package omelcam934.sleepcalm.services;

import omelcam934.sleepcalm.devices.Device;
import omelcam934.sleepcalm.devices.DevicesRealm;
import omelcam934.sleepcalm.endpoint.EndpointDeviceApiService;

/**
 * Singleton para trabajar con los dispositivos inteligentes.
 * Esta clase almacenará lo necesario para trabajar con distintos protocolos, como la conexion con el broken en caso de MQTT
 */
public class IOTService {

    private static IOTService iotService;

    public static IOTService getIotService(){
        if(iotService == null){
            iotService = new IOTService();
        }
        return iotService;
    }

    /**
     * Ejecuta los comandos de apagar en todos los dispositivos
     */
    public void executeCommands(){
        EndpointDeviceApiService.sendTestMessage("*\n*\nUSUARIO DORMIDO\n*\n*");
        DevicesRealm.getAllActiveDevices().forEach(Device::apagar);
    }

}
