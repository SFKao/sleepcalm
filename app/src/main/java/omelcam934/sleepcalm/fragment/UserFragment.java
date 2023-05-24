package omelcam934.sleepcalm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.IOException;

import omelcam934.sleepcalm.R;
import omelcam934.sleepcalm.activity.LoginActivity;
import omelcam934.sleepcalm.endpoint.BackendApiService;
import omelcam934.sleepcalm.services.LoginService;


public class UserFragment extends Fragment {

    private Button goToLoginButton;
    private Button checkTokenButton;

    public UserFragment() {
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
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();

        goToLoginButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        checkTokenButton.setOnClickListener(view -> {
            try {
                if(BackendApiService.checkToken(LoginService.getToken())){
                    Toast.makeText(getContext(), "LOGUED IN", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "NOT", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "CRASH", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        goToLoginButton = (Button) getActivity().findViewById(R.id.goToLoginButton);
        checkTokenButton = (Button) getActivity().findViewById(R.id.checkTokenButton);
    }
}