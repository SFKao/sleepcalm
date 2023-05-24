package omelcam934.sleepcalm.devices;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import omelcam934.sleepcalm.endpoint.EndpointDeviceApiService;

public class EndpointDevice extends RealmObject implements Device{

    public static final String TYPE = "Endpoint";

    @PrimaryKey
    private String id = ObjectId.get().toHexString();

    @Required
    private String nombre;
    private int icon; //Referencia al icono.
    private boolean active;

    @Required
    private String ip;
    @Required
    private String puerto;
    @Required
    private String endpoint;
    private String parametros;

    public EndpointDevice() {
    }

    public EndpointDevice(String nombre, int icon, boolean active, String ip, String puerto, String endpoint, String parametros) {
        this.nombre = nombre;
        this.icon = icon;
        this.active = active;
        this.ip = ip;
        this.puerto = puerto;
        this.endpoint = endpoint;
        this.parametros = parametros;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int getIcon() {
        return icon;
    }

    @Override
    public void setIcon(int icon) {
        this.icon = icon;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPuerto() {
        return puerto;
    }

    public void setPuerto(String puerto) {
        this.puerto = puerto;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getParametros() {
        return parametros;
    }

    public void setParametros(String parametros) {
        this.parametros = parametros;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public void apagar() {
        String url = endpoint;
        if(parametros != null)
            url+="?"+parametros;
        EndpointDeviceApiService.executeApiCall("http://"+ip+":"+puerto+"/", url);
    }
}
