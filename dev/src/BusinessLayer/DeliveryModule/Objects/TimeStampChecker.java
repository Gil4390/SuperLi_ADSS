package BusinessLayer.DeliveryModule.Objects;

import java.time.Duration;
import java.time.Instant;

public abstract class TimeStampChecker {

    private Instant timeStamp;

    public TimeStampChecker(){
        timeStamp = Instant.now();
    }

    protected void updateTimeStamp() {
        timeStamp = Instant.now();
    }

    public boolean shouldCleanCache() {
        Duration duration = Duration.between( timeStamp , Instant.now());
        Duration limit = Duration.ofMinutes(5);
        return (duration.compareTo( limit ) > 0);
    }

}
