package io.filpool.scheduled;

import io.filpool.scheduled.service.MarketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class MarketTask {
    @Autowired
    private MarketService marketService;

    @Scheduled(fixedDelay = 10 * 1000)
    public void sendFilInfo() {
        try {
            marketService.sendFilInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
