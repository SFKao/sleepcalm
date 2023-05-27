package omelcam934.sleepcalm.fragment;

import android.content.Context;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import omelcam934.sleepcalm.R;
import omelcam934.sleepcalm.activity.MainActivity;
import omelcam934.sleepcalm.endpoint.BackendApiService;
import omelcam934.sleepcalm.endpoint.dto.SleepTrackDto;
import omelcam934.sleepcalm.endpoint.dto.WeekDto;
import omelcam934.sleepcalm.endpoint.exceptions.InvalidLoginException;
import omelcam934.sleepcalm.services.LoginService;

public class StadisticsFragment extends Fragment {

    private MainActivity context;

    private TextView horasDormidasDia;
    private TextView horasLunesText;
    private TextView horasMartesText;
    private TextView horasMiercolesText;
    private TextView horasJuevesText;
    private TextView horasViernesText;
    private TextView horasSabadoText;
    private TextView horasDomingoText;
    private Button weekButton;

    private final DateFormat buttonDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("es-ES"));
    private Date buttonDate = new Date();

    public StadisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();

        if(LoginService.isLoguedIn()) {
            try {
                printWeek(BackendApiService.getCurrentWeek(context));
                weekButton.setText(buttonDateFormat.format(buttonDate));
            } catch (InvalidLoginException e) {
                Toast.makeText(context, "Se ha cerrado sesion", Toast.LENGTH_SHORT).show();
                LoginService.logout();
            } catch (IOException e) {
                Toast.makeText(context, "No se ha podido realizar la conexion con el back", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }else{
            Toast.makeText(context, "Necesitas iniciar sesión para ello", Toast.LENGTH_SHORT).show();
        }

    }

    private void printWeek(WeekDto week){
        //Si no hay dias esa semana
        if(week.getWeekSleepTracks().size()==0){
            horasLunesText.setText("?");
            horasMartesText.setText("?");
            horasMiercolesText.setText("?");
            horasJuevesText.setText("?");
            horasViernesText.setText("?");
            horasSabadoText.setText("?");
            horasDomingoText.setText("?");
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(buttonDate);
        int buttonDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        DateFormat dateFormat = BackendApiService.dateFormat;

        try {
            for (SleepTrackDto dia : week.getWeekSleepTracks()) {
                calendar.setTime(Objects.requireNonNull(dateFormat.parse(dia.getDia())));
                int indiceDia = calendar.get(Calendar.DAY_OF_WEEK);
                String horasYMinutos = calculateTimeBetweenSleepTrack(dia);
                if(indiceDia == buttonDayOfWeek){
                    horasDormidasDia.setText(horasYMinutos);
                }
                switch (indiceDia){
                    case 1:
                        horasDomingoText.setText(horasYMinutos);
                        break;
                    case 2:
                        horasLunesText.setText(horasYMinutos);
                        break;
                    case 3:
                        horasMartesText.setText(horasYMinutos);
                        break;
                    case 4:
                        horasMiercolesText.setText(horasYMinutos);
                        break;
                    case 5:
                        horasJuevesText.setText(horasYMinutos);
                        break;
                    case 6:
                        horasViernesText.setText(horasYMinutos);
                        break;
                    case 7:
                        horasSabadoText.setText(horasYMinutos);
                        break;
                }
            }
        } catch (ParseException e) {
            Toast.makeText(context, "El formato del dateFormat es invalido", Toast.LENGTH_SHORT).show();
        }
    }

    private static String calculateTimeBetweenSleepTrack(SleepTrackDto sleepTrackDto) throws ParseException {
        DateFormat dateFormat = BackendApiService.dateFormat;
        Date horaInicio = dateFormat.parse(sleepTrackDto.getHoraDeInicio());
        Date horaFinal = dateFormat.parse(sleepTrackDto.getHoraDeFin());

        long hours = ChronoUnit.HOURS.between(horaInicio.toInstant(), horaFinal.toInstant());
        long minutes = ChronoUnit.MINUTES.between(horaInicio.toInstant(), horaFinal.toInstant())%60;
        return hours+":"+(minutes<10?"0"+minutes:minutes);
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = (MainActivity) context;
    }


    private void initView() {
        horasDormidasDia = (TextView) context.findViewById(R.id.horasDormidasDia);
        horasLunesText = (TextView) context.findViewById(R.id.horasLunesText);
        horasMartesText = (TextView) context.findViewById(R.id.horasMartesText);
        horasMiercolesText = (TextView) context.findViewById(R.id.horasMiercolesText);
        horasJuevesText = (TextView) context.findViewById(R.id.horasJuevesText);
        horasViernesText = (TextView) context.findViewById(R.id.horasViernesText);
        horasSabadoText = (TextView) context.findViewById(R.id.horasSabadoText);
        horasDomingoText = (TextView) context.findViewById(R.id.horasDomingoText);
        weekButton = (Button) context.findViewById(R.id.weekButton);
    }
}