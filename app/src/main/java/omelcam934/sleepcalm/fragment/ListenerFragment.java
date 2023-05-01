package omelcam934.sleepcalm.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import omelcam934.sleepcalm.R;
import omelcam934.sleepcalm.activity.MainActivity;

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

    boolean listening = true;

    public ListenerFragment() {
        // Required empty public constructor
    }

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

            Toast.makeText(context, "AAAAA", Toast.LENGTH_SHORT).show();
            this.listening = ! this.listening;
            listenButton.setImageDrawable(AppCompatResources.getDrawable(context,this.listening ? R.drawable.visibility_fill0_wght400_grad0_opsz48 : R.drawable.visibility_off_fill0_wght400_grad0_opsz48));
        });
    }

    private void initView() {
        listeningStatusText = (TextView) context.findViewById(R.id.listeningStatusText);
        listenButton = (ImageButton) context.findViewById(R.id.listenButton);
        devicesText = (TextView) context.findViewById(R.id.devicesText);
    }
}