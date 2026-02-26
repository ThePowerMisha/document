package com.thepowermisha.scheduler.scheduler;

import com.thepowermisha.scheduler.service.DocumentSchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerWorker {
    private final DocumentSchedulerService documentSchedulerService;

    @Scheduled(fixedDelayString = "${app.worker.submit-delay}")
    void submitScheduler() {
        documentSchedulerService.submitProcess();
    }

    @Scheduled(fixedDelayString = "${app.worker.approve-delay}")
    void approveScheduler() {
        documentSchedulerService.approveProcess();
    }
}
