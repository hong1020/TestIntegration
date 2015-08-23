package task;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by hongcheng on 8/19/15.
 */
public class ThreadPoolTaskSchedulerZkSync extends ThreadPoolTaskScheduler {
    private  TaskSyncClient taskSyncClient;

    public TaskSyncClient getTaskSyncClient() {
        return taskSyncClient;
    }

    public void setTaskSyncClient(TaskSyncClient taskSyncClient) {
        this.taskSyncClient = taskSyncClient;
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable task, Date startTime) {
        return super.schedule(task, startTime);
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable task, Trigger trigger) {
        return super.schedule(getTaskProxy(task), trigger);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Date startTime, long period) {
        return super.scheduleAtFixedRate(getTaskProxy(task), startTime, period);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long period) {
        return super.scheduleAtFixedRate(getTaskProxy(task), period);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, Date startTime, long delay) {
        return super.scheduleWithFixedDelay(getTaskProxy(task), startTime, delay);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long delay) {
        return super.scheduleWithFixedDelay(getTaskProxy(task), delay);
    }

    Runnable getTaskProxy(final Runnable task) {
        return new Runnable() {
            @Override
            public void run() {
                if (taskSyncClient.isMainClient()) {
                    task.run();
                }
            }
        };
    }
}

