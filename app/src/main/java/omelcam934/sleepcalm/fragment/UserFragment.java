package omelcam934.sleepcalm.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import omelcam934.sleepcalm.R;
import omelcam934.sleepcalm.activity.LoginActivity;
import omelcam934.sleepcalm.activity.MainActivity;
import omelcam934.sleepcalm.endpoint.BackendApiService;
import omelcam934.sleepcalm.endpoint.exceptions.InvalidLoginException;
import omelcam934.sleepcalm.services.LoginService;


public class UserFragment extends Fragment {

    private MainActivity context;

    private ConstraintLayout linearLayout;
    private TextView avisoIniciarSesion;
    private AppCompatButton editarPerfilButton;
    private AppCompatButton editarRegistroButton;
    private AppCompatButton borrarRegistrosButton;
    private AppCompatButton loginButton;
    private ImageButton logoutButton;

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

        if(LoginService.isLoguedIn()){
            avisoIniciarSesion.setVisibility(View.GONE);
            loginButton.setVisibility(View.GONE);
        }else{
            editarRegistroButton.setVisibility(View.GONE);
            borrarRegistrosButton.setVisibility(View.GONE);
            editarPerfilButton.setVisibility(View.GONE);
            logoutButton.setVisibility(View.GONE);

            avisoIniciarSesion.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.VISIBLE);
        }

        loginButton.setOnClickListener(view -> {
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(view -> {
            try {
                LoginService.logout(context);
                Toast.makeText(context, "Cerrada sesion correctamente", Toast.LENGTH_SHORT).show();

                editarRegistroButton.setVisibility(View.GONE);
                borrarRegistrosButton.setVisibility(View.GONE);
                editarPerfilButton.setVisibility(View.GONE);
                logoutButton.setVisibility(View.GONE);

                avisoIniciarSesion.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "No se ha podido hacer logout", Toast.LENGTH_SHORT).show();
            }
        });

        editarRegistroButton.setOnClickListener(view -> {
            Toast.makeText(context, "TODO", Toast.LENGTH_SHORT).show();
        });

        borrarRegistrosButton.setOnClickListener(view -> {
            Toast.makeText(context, "TODO", Toast.LENGTH_SHORT).show();

        });

        editarPerfilButton.setOnClickListener(view -> {
            Toast.makeText(context, "TODO", Toast.LENGTH_SHORT).show();
        });


    }

    private void showDatePickerDialog(DatePickerDialog.OnDateSetListener onDateSetListener) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context);
        datePickerDialog.setOnDateSetListener(onDateSetListener);
        datePickerDialog.show();
    }

    private void showTimePickerDialog(TimePickerDialog.OnTimeSetListener onTimeSetListener){
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, onTimeSetListener,0,0,true);
        timePickerDialog.show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = (MainActivity) context;
    }

    private void initView() {
        avisoIniciarSesion = (TextView) context.findViewById(R.id.avisoIniciarSesion);
        editarPerfilButton = (AppCompatButton) context.findViewById(R.id.editarPerfilButton);
        editarRegistroButton = (AppCompatButton) context.findViewById(R.id.editarRegistroButton);
        borrarRegistrosButton = (AppCompatButton) context.findViewById(R.id.borrarRegistrosButton);
        loginButton = (AppCompatButton) context.findViewById(R.id.loginButton);
        logoutButton = (ImageButton) context.findViewById(R.id.logoutButton);
    }
}