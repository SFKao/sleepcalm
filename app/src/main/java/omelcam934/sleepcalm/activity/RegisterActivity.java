package omelcam934.sleepcalm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.io.IOException;
import java.util.regex.Pattern;

import omelcam934.sleepcalm.R;
import omelcam934.sleepcalm.endpoint.BackendApiService;
import omelcam934.sleepcalm.endpoint.exceptions.EmailOrUsernameInUseException;
import omelcam934.sleepcalm.endpoint.exceptions.InvalidEmailException;
import omelcam934.sleepcalm.endpoint.exceptions.InvalidLoginException;
import omelcam934.sleepcalm.services.LoginService;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEdit;
    private EditText emailEdit;
    private EditText passwordEdit;
    private EditText repeatPasswordEdit;
    private SwitchMaterial terminosSwitch;
    private Button registrarseButton;
    private Button volverButton;

    private static final String EMAILPATTERN = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    @Override
    protected void onStart() {
        super.onStart();

        volverButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        registrarseButton.setOnClickListener(view -> {
            if(usernameEdit.getText().toString().equals("")) {
                Toast.makeText(this, (int)R.string.se_require_username, Toast.LENGTH_SHORT).show();
                return;
            }
            if(!emailEdit.getText().toString().matches(EMAILPATTERN)){
                Toast.makeText(this, (int)R.string.se_requiere_email, Toast.LENGTH_SHORT).show();
                return;
            }
            if(passwordEdit.getText().toString().equals("")) {
                Toast.makeText(this, (int)R.string.se_requiere_contrase_a, Toast.LENGTH_SHORT).show();
                return;
            }
            if(!repeatPasswordEdit.getText().toString().equals(passwordEdit.getText().toString())) {
                Toast.makeText(this, (int)R.string.las_contrase_as_han_de_coincidir, Toast.LENGTH_SHORT).show();
                return;
            }
            if(!terminosSwitch.isChecked()){
                Toast.makeText(this, (int)R.string.terminos_y_condiciones, Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                BackendApiService.registro(usernameEdit.getText().toString(), emailEdit.getText().toString(), passwordEdit.getText().toString(), this);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } catch (IOException e) {
                Toast.makeText(this, (int)R.string.no_conexion_back, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (EmailOrUsernameInUseException e) {
                Toast.makeText(this, (int)R.string.username_o_email_en_uso, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (InvalidEmailException e) {
                Toast.makeText(this, (int)R.string.email_invalido, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (InvalidLoginException e) {
                Toast.makeText(this, (int)R.string.no_se_ha_podido_iniciar_sesion, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        usernameEdit = (EditText) findViewById(R.id.usernameEdit);
        emailEdit = (EditText) findViewById(R.id.emailEdit);
        passwordEdit = (EditText) findViewById(R.id.passwordEdit);
        repeatPasswordEdit = (EditText) findViewById(R.id.repeatPasswordEdit);
        terminosSwitch = (SwitchMaterial) findViewById(R.id.terminosSwitch);
        registrarseButton = (Button) findViewById(R.id.registrarseButton);
        volverButton = (Button) findViewById(R.id.volverButton);
    }
}