package com.Stefan.BibliotecaUnical.service;

import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class PrecisionScheduler {

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);

    public void scheduleWithDelay(Runnable task)
    {
        executor.schedule(task, 15, TimeUnit.MINUTES);
    }

    @PreDestroy
    public void shutdown()
    {
        executor.shutdownNow();
    }
}
