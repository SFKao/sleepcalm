package omelcam934.sleepcalm.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import omelcam934.sleepcalm.R;
import omelcam934.sleepcalm.activity.MainActivity;
import omelcam934.sleepcalm.adapter.DevicesAdapter;
import omelcam934.sleepcalm.devices.EndpointDevice;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DevicesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DevicesFragment extends Fragment {

    private MainActivity context;
    private FloatingActionButton menuFloatingButton;
    private FloatingActionButton editarFloatingButton;
    private FloatingActionButton anyadirFloatingButton;
    private FloatingActionButton borrarFloatingButton;
    private boolean showingMenu = false;

    private RecyclerView recyclerDispositivos;
    private DevicesAdapter devicesAdapter;

    public DevicesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        initView(context);

        menuFloatingButton.setOnClickListener(v -> {
            if(showingMenu){
                editarFloatingButton.setVisibility(View.INVISIBLE);
                anyadirFloatingButton.setVisibility(View.INVISIBLE);
                borrarFloatingButton.setVisibility(View.INVISIBLE);
                menuFloatingButton.setImageResource(R.drawable.ic_baseline_toc_24);
            }else{
                editarFloatingButton.setVisibility(View.VISIBLE);
                anyadirFloatingButton.setVisibility(View.VISIBLE);
                borrarFloatingButton.setVisibility(View.VISIBLE);
                menuFloatingButton.setImageResource(R.drawable.arrow_back_fill0_wght400_grad0_opsz48);
            }
            showingMenu = !showingMenu;
        });

        anyadirFloatingButton.setOnClickListener(v -> {
            new DeviceInputFragment().show(context.getSupportFragmentManager(), "Añadir dispositivo");
        });

        devicesAdapter = new DevicesAdapter();
        recyclerDispositivos.setAdapter(devicesAdapter);

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DevicesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DevicesFragment newInstance(String param1, String param2) {
        DevicesFragment fragment = new DevicesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_devices, container, false);
    }

    private void initView(MainActivity context) {
        menuFloatingButton = (FloatingActionButton) context.findViewById(R.id.menuFloatingButton);
        editarFloatingButton = (FloatingActionButton) context.findViewById(R.id.editarFloatingButton);
        anyadirFloatingButton = (FloatingActionButton) context.findViewById(R.id.anyadirFloatingButton);
        borrarFloatingButton = (FloatingActionButton) context.findViewById(R.id.borrarFloatingButton);
        recyclerDispositivos = (RecyclerView) context.findViewById(R.id.recyclerDispositivos);
    }
}