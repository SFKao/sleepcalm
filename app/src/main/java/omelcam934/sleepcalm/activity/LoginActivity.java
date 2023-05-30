package omelcam934.sleepcalm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import omelcam934.sleepcalm.R;
import omelcam934.sleepcalm.endpoint.exceptions.InvalidLoginException;
import omelcam934.sleepcalm.services.LoginService;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameOrEmailEdit;
    private EditText passwordEdit;
    private Button loginButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();

        loginButton.setOnClickListener(view -> {
            if(!validate())
                return;

            try {
                LoginService.login(usernameOrEmailEdit.getText().toString(), passwordEdit.getText().toString(),this, true);
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
            } catch (InvalidLoginException e) {
                e.printStackTrace();
                Toast.makeText(this, (int)R.string.datos_invalidos, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, (int)R.string.no_conexion_back, Toast.LENGTH_SHORT).show();
            }

        });

        registerButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

    }
    
    private boolean validate(){
        if(usernameOrEmailEdit.getText().toString().equals("")){
            Toast.makeText(this, (int)R.string.se_requiere_usuario_o_email, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(passwordEdit.getText().toString().equals("")){
            Toast.makeText(this, (int)R.string.se_requiere_contrase_a, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void initView() {
        usernameOrEmailEdit = (EditText) findViewById(R.id.usernameOrEmailEdit);
        passwordEdit = (EditText) findViewById(R.id.passwordEdit);
        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (Button) findViewById(R.id.registerButton);
    }
}