package omelcam934.sleepcalm.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import omelcam934.sleepcalm.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StadisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StadisticsFragment extends Fragment {

    public StadisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stadistics, container, false);
    }
}