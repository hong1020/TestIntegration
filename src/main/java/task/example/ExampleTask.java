package task.example;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by hongcheng on 8/24/15.
 */
@Service
public class ExampleTask {

    @Scheduled(fixedDelay = 1000)
    public void run() {
        System.out.println(Thread.currentThread().getId() + " is running");
    }
}
