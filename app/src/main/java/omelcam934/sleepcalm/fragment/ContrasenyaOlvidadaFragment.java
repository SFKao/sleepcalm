package omelcam934.sleepcalm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.IOException;

import omelcam934.sleepcalm.R;
import omelcam934.sleepcalm.activity.MainActivity;
import omelcam934.sleepcalm.endpoint.BackendApiService;


public class ContrasenyaOlvidadaFragment extends DialogFragment {

    private View context;

    private EditText usernameOrEmailEdit;
    private Button enviarCorreoConContrasenyaButton;

    public ContrasenyaOlvidadaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = view;
        initView();
        enviarCorreoConContrasenyaButton.setOnClickListener(ignored -> {
            try {
                BackendApiService.forgotPassword(usernameOrEmailEdit.getText().toString());
                Toast.makeText(getContext(), (int)R.string.enviada_contra_temporal, Toast.LENGTH_LONG).show();
                dismiss();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), R.string.no_conexion_back, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contrasenya_olvidada, container, false);
    }

    private void initView() {
        usernameOrEmailEdit = context.findViewById(R.id.usernameOrEmailEdit);
        enviarCorreoConContrasenyaButton = context.findViewById(R.id.enviarCorreoConContrasenyaButton);
    }
}
