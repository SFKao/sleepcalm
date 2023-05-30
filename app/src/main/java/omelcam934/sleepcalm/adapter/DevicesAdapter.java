package omelcam934.sleepcalm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import omelcam934.sleepcalm.R;
import omelcam934.sleepcalm.devices.Device;
import omelcam934.sleepcalm.devices.DevicesRealm;
import omelcam934.sleepcalm.devices.EndpointDevice;
import omelcam934.sleepcalm.devices.EndpointDeviceRealm;

/**
 * Adapta los dispositivos para mostrarlos en un Recycler view
 */
public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.DevicesViewHolder> {

    private List<Device> devices;
    private OnItemClickListener onItemClickListener;

    /**
     * Constructor sin listener, obtiene los dispositivos de la bbdd
     */
    public DevicesAdapter() {
        devices = DevicesRealm.getAllDevices();
    }

    /**
     * Constructor con un listener para los dispositivos, obitene los dispositivos de la bbdd
     * @param onItemClickListener listener del evento
     */
    public DevicesAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        devices = DevicesRealm.getAllDevices();
    }

    /**
     * Obtiene el layout para colocar los dispositivos
     * @param parent
     * @param viewType
     * @return la vista
     */
    @NonNull
    @Override
    public DevicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device_item, parent, false);
        return new DevicesViewHolder(view);
    }

    /**
     * Coloca un dispositivo en una tarjeta, se llama automaticamente
     * @param holder donde colocarlo
     * @param position posicion a colocar
     */
    @Override
    public void onBindViewHolder(@NonNull DevicesViewHolder holder, int position) {
        final int pos = holder.getAdapterPosition();
        holder.deviceName.setText(devices.get(pos).getNombre());
        if(devices.get(pos).getIcon()!=0)
            holder.deviceIcon.setImageResource(devices.get(pos).getIcon());
        holder.deviceSwitch.setChecked(devices.get(pos).isActive());
        holder.deviceSwitch.setOnClickListener(v -> {
            DevicesRealm.updateActiveStatus(devices.get(pos),holder.deviceSwitch.isChecked());
        });
        //Si mantengo sobre el nombre apago el dispositivo
        holder.deviceName.setOnLongClickListener( v -> {
            devices.get(pos).apagar();
            return true;
        });
        //Añado el listenerpara poder mas tarde editar y borrarlos
        holder.deviceName.setOnClickListener(v -> {
            if(onItemClickListener!=null)
                onItemClickListener.onItemClicked(devices.get(pos));
        });


    }

    /**
     * Obtiene cuantos elementos puede mostrar
     * @return numero de elementos
     */
    @Override
    public int getItemCount() {
        return devices.size();
    }

    /**
     * Representa las tarjetas de los dispositivos
     */
    public static class DevicesViewHolder extends RecyclerView.ViewHolder {
        private final TextView deviceName;
        private final ImageView deviceIcon;
        private final SwitchCompat deviceSwitch;

        public DevicesViewHolder(@NonNull View view) {
            super(view);
            deviceIcon = view.findViewById(R.id.icon_device);
            deviceName = view.findViewById(R.id.name_device);
            deviceSwitch = view.findViewById(R.id.switch_device);
        }
    }

    /**
     * Actualiza los elementos del adapter
     */
    public void update(){
        devices = DevicesRealm.getAllDevices();
        notifyDataSetChanged();
    }

    /**
     * Interfaz para crear listener a pulsar en sus dispositivos
     */
    public interface OnItemClickListener{
        boolean onItemClicked(Device device);
    }
}
