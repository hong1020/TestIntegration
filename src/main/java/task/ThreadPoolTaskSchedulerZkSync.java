package task;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by hongcheng on 8/19/15.
 */
public class ThreadPoolTaskSchedulerZkSync extends ThreadPoolTaskScheduler{
    private TaskSyncClient taskSyncClient = null;
    private CuratorFramework client = null;

    private String connectionString = "";
    private String leaderPath = "";


    public ThreadPoolTaskSchedulerZkSync() {super();}

    public ThreadPoolTaskSchedulerZkSync(String connectionString, String leaderPath) {
        this.connectionString = connectionString;
        this.leaderPath = leaderPath;

    }

    @Override
    public void initialize() {
        super.initialize();

        client = CuratorFrameworkFactory.newClient(connectionString, new ExponentialBackoffRetry(1000, 3));
        taskSyncClient = new TaskSyncClient(client, leaderPath);

        try {
            client.start();
            taskSyncClient.start();
        } catch (Exception e) {
            throw new RuntimeException("zk can not start.");
        }
    }

    @Override
    public void shutdown() {
        super.shutdown();

        try {
            taskSyncClient.close();
            client.close();

        } catch (Exception e) {
            throw new RuntimeException("zk can not close.");
        }
    }

    public TaskSyncClient getTaskSyncClient() {
        return taskSyncClient;
    }

    public void setTaskSyncClient(TaskSyncClient taskSyncClient) {
        this.taskSyncClient = taskSyncClient;
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable task, Date startTime) {
        return super.schedule(getTaskProxy(task), startTime);
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

