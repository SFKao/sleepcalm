package omelcam934.sleepcalm.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
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

    public StadisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();

        if(LoginService.isLoguedIn()) {
            try {
                printWeek(BackendApiService.getCurrentWeek(context), new Date());
            } catch (InvalidLoginException e) {
                Toast.makeText(context, (int)R.string.se_ha_cerrado_sesion, Toast.LENGTH_SHORT).show();
                try {
                    LoginService.logout(context);
                } catch (IOException ex) {
                    Toast.makeText(context, (int) R.string.no_se_ha_podido_hacer_logout, Toast.LENGTH_SHORT).show();
                    ex.printStackTrace();
                }
            } catch (IOException e) {
                Toast.makeText(context, (int)R.string.no_conexion_back, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            weekButton.setOnClickListener(view -> showDatePickerDialog());


        }else{
            Toast.makeText(context, (int)R.string.necesitas_iniciar_sesion, Toast.LENGTH_SHORT).show();
        }
    }

    private void printWeek(WeekDto week, Date searchDate){
        weekButton.setText(buttonDateFormat.format(searchDate));

        //Si no hay dias esa semana
        if(week.getWeekSleepTracks().size()==0){
            horasLunesText.setText(R.string.interrogation);
            horasMartesText.setText(R.string.interrogation);
            horasMiercolesText.setText(R.string.interrogation);
            horasJuevesText.setText(R.string.interrogation);
            horasViernesText.setText(R.string.interrogation);
            horasSabadoText.setText(R.string.interrogation);
            horasDomingoText.setText(R.string.interrogation);
            horasDormidasDia.setText(R.string.interrogation);
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(searchDate);
        int buttonDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        DateFormat dateFormat = BackendApiService.dateFormat;
        boolean diaActualCambiado = false;
        try {
            for (SleepTrackDto dia : week.getWeekSleepTracks()) {
                calendar.setTime(Objects.requireNonNull(dateFormat.parse(dia.getDia())));
                int indiceDia = calendar.get(Calendar.DAY_OF_WEEK);
                String horasYMinutos = calculateTimeBetweenSleepTrack(dia);
                if(indiceDia == buttonDayOfWeek){
                    horasDormidasDia.setText(horasYMinutos);
                    diaActualCambiado = true;
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
            if(!diaActualCambiado)
                horasDormidasDia.setText(R.string.interrogation);
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

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context);
        datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            Date searchDate = calendar.getTime();
            try {
                printWeek(BackendApiService.getWeek(context, searchDate), searchDate);
            } catch (InvalidLoginException | IOException e) {
                Toast.makeText(context, "No se ha podido conectar al backend", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });

        datePickerDialog.show();
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
        horasDormidasDia = context.findViewById(R.id.horasDormidasDia);
        horasLunesText = context.findViewById(R.id.horasLunesText);
        horasMartesText = context.findViewById(R.id.horasMartesText);
        horasMiercolesText = context.findViewById(R.id.horasMiercolesText);
        horasJuevesText = context.findViewById(R.id.horasJuevesText);
        horasViernesText = context.findViewById(R.id.horasViernesText);
        horasSabadoText = context.findViewById(R.id.horasSabadoText);
        horasDomingoText = context.findViewById(R.id.horasDomingoText);
        weekButton = context.findViewById(R.id.weekButton);
    }
}