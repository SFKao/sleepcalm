package omelcam934.sleepcalm.endpoint.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class WeekDto implements Serializable {
    private final List<SleepTrackDto> weekSleepTracks;

    public WeekDto(List<SleepTrackDto> weekSleepTracks) {
        this.weekSleepTracks = weekSleepTracks;
    }

    public List<SleepTrackDto> getWeekSleepTracks() {
        return weekSleepTracks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeekDto)) return false;
        WeekDto weekDto = (WeekDto) o;
        return Objects.equals(weekSleepTracks, weekDto.weekSleepTracks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weekSleepTracks);
    }

    @Override
    public String toString() {
        return "WeekDto{" +
                "weekSleepTracks=" + weekSleepTracks +
                '}';
    }
}
