package omelcam934.sleepcalm.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.regex.Pattern;

import omelcam934.sleepcalm.R;
import omelcam934.sleepcalm.activity.MainActivity;

public class EndpointDeviceFragment extends Fragment implements DeviceDataFragment {

    private MainActivity context;
    private EditText deviceEndpointIp;
    private EditText deviceEndpointPuerto;
    private EditText deviceEndpointEndpoint;
    private EditText deviceEndpointParametro;

    private String[] data;

    public EndpointDeviceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = (MainActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_endpoint_device, container, false);
        initView(view);

        if(data!=null){
            deviceEndpointIp.setText(data[0]);
            deviceEndpointPuerto.setText(data[1]);
            deviceEndpointEndpoint.setText(data[2]);
            deviceEndpointParametro.setText(data[3]);
        }

        return view;
    }

    @Override
    public String[] getData() {
        if(!Pattern.matches("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$", deviceEndpointIp.getText().toString()))
            throw new IllegalStateException("IP");

        int puerto = 80;
        if(!deviceEndpointPuerto.getText().toString().equals(""))
            try{
            puerto = Integer.parseInt(deviceEndpointPuerto.getText().toString());
            }catch (NumberFormatException e) {
                throw new IllegalStateException("Puerto");
            }
        if(!(puerto>0 && puerto < 65565))
            throw new IllegalStateException("Puerto");

        return new String[]{
                deviceEndpointIp.getText().toString(),
                deviceEndpointPuerto.getText().toString(),
                deviceEndpointEndpoint.getText().toString(),
                deviceEndpointParametro.getText().toString()
        };
    }

    @Override
    public void setData(String[] data) {
        this.data = data;
    }

    private void initView(View context) {
        deviceEndpointIp = (EditText) context.findViewById(R.id.deviceEndpointIp);
        deviceEndpointPuerto = (EditText) context.findViewById(R.id.deviceEndpointPuerto);
        deviceEndpointEndpoint = (EditText) context.findViewById(R.id.deviceEndpointEndpoint);
        deviceEndpointParametro = (EditText) context.findViewById(R.id.deviceEndpointParametro);
    }
}