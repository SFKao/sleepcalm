package omelcam934.sleepcalm.services;

public class IOTService {

    private static IOTService iotService;

    public static IOTService getIotService(){
        if(iotService == null){
            iotService = new IOTService();
        }
        return iotService;
    }

    public void executeCommands(){
        ApiService.sendTestMessage("*\n*\nUSUARIO DORMIDO\n*\n*");
    }

}
