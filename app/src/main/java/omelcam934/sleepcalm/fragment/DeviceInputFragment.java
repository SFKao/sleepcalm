package omelcam934.sleepcalm.fragment;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import omelcam934.sleepcalm.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeviceInputFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceInputFragment extends DialogFragment {

    public DeviceInputFragment() {
        // Required empty public constructor
    }

    public static DeviceInputFragment newInstance(String param1, String param2) {
        DeviceInputFragment fragment = new DeviceInputFragment();
        return fragment;
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
}