package omelcam934.sleepcalm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import java.io.IOException;
import java.util.Objects;

import omelcam934.sleepcalm.R;
import omelcam934.sleepcalm.activity.RegisterActivity;
import omelcam934.sleepcalm.endpoint.BackendApiService;

public class CambiarContrasenyaFragment extends DialogFragment {

    private EditText emailEdit;
    private EditText antiguaContrasenyaEdit;
    private EditText nuevaContrasenyaEdit;
    private Button enviarCorreoConContrasenyaButton;

    public CambiarContrasenyaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        initView(requireView());

        enviarCorreoConContrasenyaButton.setOnClickListener(view -> {
            if(validate()){
                try {
                    BackendApiService.changePassword(emailEdit.getText().toString(),
                            antiguaContrasenyaEdit.getText().toString(),
                            nuevaContrasenyaEdit.getText().toString());
                    Toast.makeText(getContext(), (int)R.string.contra_cambiada, Toast.LENGTH_SHORT).show();
                    dismiss();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(),(int)R.string.no_conexion_back, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private boolean validate(){
        if(!emailEdit.getText().toString().matches(RegisterActivity.EMAILPATTERN)){
            Toast.makeText(getContext(), (int)R.string.se_requiere_email, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(antiguaContrasenyaEdit.getText().toString().equals("")) {
            Toast.makeText(getContext(), (int)R.string.se_requiere_contrase_a, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(nuevaContrasenyaEdit.getText().toString().equals("")) {
            Toast.makeText(getContext(), (int)R.string.se_requiere_contrase_a, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cambiar_contrasenya, container, false);
    }

    private void initView(View context) {
        emailEdit = (EditText) context.findViewById(R.id.emailEdit);
        antiguaContrasenyaEdit = (EditText) context.findViewById(R.id.antiguaContrasenyaEdit);
        nuevaContrasenyaEdit = (EditText) context.findViewById(R.id.nuevaContrasenyaEdit);
        enviarCorreoConContrasenyaButton = (Button) context.findViewById(R.id.enviarCorreoConContrasenyaButton);
    }
}