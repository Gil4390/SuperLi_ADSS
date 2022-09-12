package DataAccessLayer.DeliveryModuleDal.DControllers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CacheCleaner {
    List<DalController> mappers;
    private final int intervalTime = 5;
    public static ScheduledExecutorService executor;

    public CacheCleaner() {
        mappers = new ArrayList<>();
    }

    public void loadMaps(List<DalController> mappers) {
        this.mappers.addAll(mappers);
    }
    public void clean() {
        executor = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            for(DalController mapper : mappers)
                mapper.cleanCache();
        };
        executor.scheduleWithFixedDelay(task, intervalTime, intervalTime, TimeUnit.MINUTES);
    }
}
