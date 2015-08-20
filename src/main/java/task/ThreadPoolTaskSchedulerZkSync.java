package task;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ScheduledExecutorService;

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
    public ScheduledExecutorService getScheduledExecutor() throws IllegalStateException {
        ScheduledExecutorService r = super.getScheduledExecutor();
        if (r != null) {
            //TODO - use silent mode.
            if (!taskSyncClient.isMainClient()) {
                throw new IllegalStateException("not main client");
            }
        }
        return r;
    }
}
