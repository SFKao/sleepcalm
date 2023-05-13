package omelcam934.sleepcalm.devices;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class EndpointDeviceRealm {

    public static void insertOrUpdate(EndpointDevice endpointDevice){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> realm1.insertOrUpdate(endpointDevice));
    }

    public static List<Device> getEndpointDevices(){
        Realm realm = Realm.getDefaultInstance();
        final ArrayList<Device> devices = new ArrayList<>();
        realm.executeTransaction(realm1 -> {
            RealmResults<EndpointDevice> all = realm1.where(EndpointDevice.class).findAll();
            devices.addAll(all);
        });
        return devices;
    }

    public static void borrarEndpointDevice(EndpointDevice endpointDevice){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> endpointDevice.deleteFromRealm());
    }

    public static void updateActiveStatus(EndpointDevice endpointDevice, boolean newActiveStatus){
        Realm.getDefaultInstance().executeTransaction(realm -> {
            endpointDevice.setActive(newActiveStatus);
            realm.insertOrUpdate(endpointDevice);
        });
    }



}
