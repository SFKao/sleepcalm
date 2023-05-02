package omelcam934.sleepcalm.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import omelcam934.sleepcalm.BuildConfig;
import omelcam934.sleepcalm.R;
import omelcam934.sleepcalm.activity.MainActivity;
import omelcam934.sleepcalm.services.SleepService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListenerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListenerFragment extends Fragment {

    MainActivity context;
    private TextView listeningStatusText;
    private ImageButton listenButton;
    private TextView devicesText;

    boolean listening = false;

    public ListenerFragment() {
        // Required empty public constructor
    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
        registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result){
                Toast.makeText(context, "Permisos obtenidos", Toast.LENGTH_SHORT).show();
            }else{
                displayPermissionSettingsSnackBar();
            }
        }
    });

    /*
     public static ListenerFragment newInstance(String param1, String param2) {
         ListenerFragment fragment = new ListenerFragment();
         Bundle args = new Bundle();
         args.putString(ARG_PARAM1, param1);
         args.putString(ARG_PARAM2, param2);
         fragment.setArguments(args);
         return fragment;
     }
     */

     public static ListenerFragment newInstance(){
         return new ListenerFragment();
     }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_listener, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = (MainActivity) context;
    }

    @Override
    public void onStart() {
        super.onStart();

        //Obtiene los componentes de la vista
        initView();

        listenButton.setOnClickListener(view ->{
            //Miro los permisos
            if (!checkRecognitionPermission()) {
                Toast.makeText(context, "No se tienen los permisos para funcionar", Toast.LENGTH_SHORT).show();
                requestPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION);
                return;
            }

            //Cambio el estado de escucha
            this.listening = ! this.listening;

            SleepService.getSleepService().changeStatus(listening);

            listenButton.setImageDrawable(AppCompatResources.getDrawable(context,this.listening ? R.drawable.visibility_fill0_wght400_grad0_opsz48 : R.drawable.visibility_off_fill0_wght400_grad0_opsz48));
        });

        listenButton.setImageDrawable(AppCompatResources.getDrawable(context,this.listening ? R.drawable.visibility_fill0_wght400_grad0_opsz48 : R.drawable.visibility_off_fill0_wght400_grad0_opsz48));
    }

    private boolean checkRecognitionPermission(){
        //Mira si se tiene los permisos para seguir al usuario.
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission( context, Manifest.permission.ACTIVITY_RECOGNITION);
    }

    private void displayPermissionSettingsSnackBar() {
        Snackbar.make(
                        context.findViewById(R.id.fragment_holder),
                        R.string.pidiendo_permisos,
                        Snackbar.LENGTH_LONG
                )
                .setAction(R.string.action_settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Build intent that displays the App settings screen.
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts(
                                "package",
                                BuildConfig.APPLICATION_ID,
                                null
                        );
                        intent.setData(uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
            .show();
    }


    private void initView() {
        listeningStatusText = (TextView) context.findViewById(R.id.listeningStatusText);
        listenButton = (ImageButton) context.findViewById(R.id.listenButton);
        devicesText = (TextView) context.findViewById(R.id.devicesText);
    }
}