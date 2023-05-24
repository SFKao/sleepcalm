package omelcam934.sleepcalm.endpoint;

import omelcam934.sleepcalm.endpoint.dto.LoginDto;
import omelcam934.sleepcalm.endpoint.dto.RegistroDto;
import omelcam934.sleepcalm.endpoint.dto.SleepTrackDto;
import omelcam934.sleepcalm.endpoint.dto.UserDto;
import omelcam934.sleepcalm.endpoint.dto.WeekDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BackendApiServiceInterface {

    @POST("auth/registro")
    Call<String> registrar(@Body RegistroDto registroDto);

    @POST("auth/login")
    Call<String> login(@Body LoginDto loginDto);

    @GET("user")
    Call<UserDto> getUser(@Header("Authoritation")String authHeader);

    @PUT("user")
    Call<UserDto> putUser(@Header("Authoritation")String authHeader, @Body UserDto userDto);

    @GET("sleeptrack")
    Call<WeekDto> getCurrentWeek(@Header("Authoritation")String authHeader);

    @POST("sleeptrack")
    Call<SleepTrackDto> postSleepTrack(@Header("Authoritation")String authHeader, @Body SleepTrackDto sleepTrackDto);

    @PUT("sleeptrack")
    Call<SleepTrackDto> putSleepTrack(@Header("Authoritation")String authHeader, @Body SleepTrackDto sleepTrackDto);

    @GET("sleeptrack/week=diaSemana={dia}")
    Call<WeekDto> getWeek(@Header("Authoritation")String authHeader, @Path("dia") String dia);

    @GET("user/checktoken")
    Call<Boolean> checkToken(@Header("Authoritation")String authHeader);

}
