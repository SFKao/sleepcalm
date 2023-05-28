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
import retrofit2.http.Query;

public interface BackendApiServiceInterface {

    @POST("auth/registro")
    Call<Boolean> registrar(@Body RegistroDto registroDto);

    @POST("auth/login")
    Call<String> login(@Body LoginDto loginDto);

    @GET("user")
    Call<UserDto> getUser(@Header("Authorization")String authHeader);

    @PUT("user")
    Call<UserDto> putUser(@Header("Authorization")String authHeader, @Body UserDto userDto);

    @GET("sleeptrack")
    Call<WeekDto> getCurrentWeek(@Header("Authorization")String authHeader);

    @POST("sleeptrack")
    Call<SleepTrackDto> postSleepTrack(@Header("Authorization")String authHeader, @Body SleepTrackDto sleepTrackDto);

    @PUT("sleeptrack")
    Call<SleepTrackDto> putSleepTrack(@Header("Authorization")String authHeader, @Body SleepTrackDto sleepTrackDto);

    @GET("sleeptrack/week")
    Call<WeekDto> getWeek(@Header("Authorization")String authHeader, @Query("diaSemana") String dia);

    @GET("user/checktoken")
    Call<Boolean> checkToken(@Header("Authorization")String authHeader);

}
