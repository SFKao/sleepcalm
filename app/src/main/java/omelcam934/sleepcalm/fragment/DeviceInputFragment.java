package omelcam934.sleepcalm.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import omelcam934.sleepcalm.R;
import omelcam934.sleepcalm.activity.MainActivity;
import omelcam934.sleepcalm.adapter.IconDeviceSpinnerAdapter;
import omelcam934.sleepcalm.devices.Device;
import omelcam934.sleepcalm.devices.DevicesRealm;
import omelcam934.sleepcalm.devices.EndpointDevice;

/**
 * Fragmento de dialogo para añadir o editar un dispositivo
 */
public class DeviceInputFragment extends DialogFragment {

    private Device deviceEdit;

    private MainActivity context;
    private Spinner protocolSpinner;
    private EditText deviceNameEdit;
    private Spinner iconSpinner;
    private FloatingActionButton backDeviceButton;
    private FloatingActionButton helpFloatingButton;
    private Button testDeviceButton;
    private Button addDeviceButton;

    private DeviceDataFragment currentDeviceDataFragment;

    /**
     * Constructor vacio, si se utiliza este estará en modo añadir
     */
    public DeviceInputFragment() {
        // Required empty public constructor
    }

    /**
     * Constructor con parametro, si se utiliza este estará en modo editar
     * @param device
     */
    public DeviceInputFragment(Device device){
        deviceEdit = device;
    }

    /**
     * Cuando se cree se añaden los metodos a todos los botones
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView(view);

        iconSpinner.setAdapter(new IconDeviceSpinnerAdapter(context,
                new Integer[]{R.drawable.ac_unit_fill0_wght400_grad0_opsz48,
                        R.drawable.lightbulb_fill0_wght400_grad0_opsz48,
                        R.drawable.power_fill0_wght400_grad0_opsz48,
                        R.drawable.tv_fill0_wght400_grad0_opsz48
                }
        ));

        protocolSpinner.setAdapter(new ArrayAdapter<String>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, new String[]{
                EndpointDevice.TYPE
        }));


        protocolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (protocolSpinner.getSelectedItem().toString()) {
                    case EndpointDevice.TYPE:
                        if(currentDeviceDataFragment instanceof  EndpointDeviceFragment)
                            return;
                        currentDeviceDataFragment = new EndpointDeviceFragment();
                        changeFragment((Fragment) currentDeviceDataFragment);
                        break;
                    default:
                        Toast.makeText(context, (int)R.string.opcion_incorrecta, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        testDeviceButton.setOnClickListener(v -> {
            try{
                getDevice().apagar();
            }catch (IllegalStateException e){
                Toast.makeText(context, "Hay un error en "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        addDeviceButton.setOnClickListener(v -> {
            try {
                Device dev = getDevice();
                if (deviceEdit != null) {
                    dev.setId(deviceEdit.getId());
                }
                DevicesRealm.addDevice(dev);
                dismiss();
            } catch (IllegalStateException e) {
                Toast.makeText(context, "Hay un error en " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        backDeviceButton.setOnClickListener(v -> {
            DeviceInputFragment.this.dismiss();
        });

        helpFloatingButton.setOnClickListener(v -> {
            Toast.makeText(context, "TODO", Toast.LENGTH_SHORT).show();
            //TODO: Enviar a web con info en backend
        });

        //Dependiendo del modo se colocan los datos del objeto o no
        if(deviceEdit==null) {
            currentDeviceDataFragment = new EndpointDeviceFragment();
            changeFragment((Fragment) currentDeviceDataFragment);
        }else{

            deviceNameEdit.setText(deviceEdit.getNombre());
            iconSpinner.setSelection(((IconDeviceSpinnerAdapter)(iconSpinner.getAdapter())).getPositionOfIcon(deviceEdit.getIcon()));
            protocolSpinner.setSelection(((ArrayAdapter)protocolSpinner.getAdapter()).getPosition(deviceEdit.getType()));

            switch (protocolSpinner.getSelectedItem().toString()) {
                case EndpointDevice.TYPE:
                    if(currentDeviceDataFragment instanceof  EndpointDeviceFragment)
                        return;
                    currentDeviceDataFragment = new EndpointDeviceFragment();
                    changeFragment((Fragment) currentDeviceDataFragment);
                    EndpointDevice endpointDevice = (EndpointDevice) deviceEdit;
                    currentDeviceDataFragment.setData(
                            new String[]{
                                    endpointDevice.getIp(),
                                    endpointDevice.getPuerto(),
                                    endpointDevice.getEndpoint(),
                                    endpointDevice.getParametros()
                                }
                    );
                    break;
                default:
                    Toast.makeText(context, (int) R.string.opcion_incorrecta, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Obtiene el dispositivo a partir de los datos
     * @return
     * @throws IllegalStateException
     */
    private Device getDevice() throws IllegalStateException{
        if(deviceNameEdit.getText().toString().equals(""))
            throw new IllegalStateException("Nombre");

        switch (protocolSpinner.getSelectedItem().toString()){
            case EndpointDevice.TYPE:
                String[] data = currentDeviceDataFragment.getData();
                return new EndpointDevice(deviceNameEdit.getText().toString(),
                        (int) iconSpinner.getSelectedItem(),
                        true,
                        data[0],
                        data[1],
                        data[2],
                        data[3]
                        );
            default:
                Toast.makeText(context, R.string.no_hay_protocolo_seleccionado, Toast.LENGTH_SHORT).show();
        }
        throw new IllegalStateException("Protocolo");
    }

    /**
     * Cambia el fragmento de los campos dependientes del tipo de dispositivo
     * @param fragment fragmento al que cambiar
     */
    private void changeFragment(Fragment fragment) {
        this.getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentHolderDeviceData, fragment)
                .commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_device_input, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = (MainActivity) context;
    }

    /**
     * Inicializa los componentes
     * @param view
     */
    private void initView(View view) {
        protocolSpinner = (Spinner) view.findViewById(R.id.protocolSpinner);
        deviceNameEdit = (EditText) view.findViewById(R.id.deviceNameEdit);
        iconSpinner = (Spinner) view.findViewById(R.id.iconSpinner);
        backDeviceButton = (FloatingActionButton) view.findViewById(R.id.backDeviceButton);
        helpFloatingButton = (FloatingActionButton) view.findViewById(R.id.helpFloatingButton);
        testDeviceButton = (Button) view.findViewById(R.id.testDeviceButton);
        addDeviceButton = (Button) view.findViewById(R.id.addDeviceButton);
    }
}