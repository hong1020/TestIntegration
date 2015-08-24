package task.example;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import task.TaskSyncClient;
import task.ThreadPoolTaskSchedulerZkSync;

/**
 * Created by hongcheng on 8/24/15.
 */
@Configuration
public class ExampleConfig {

    @Value("${zk_connection_string}")
    String connectionString;

    @Value("${zk_task_scheduler_leader}")
    String scheculerLeaderPath;

    @Bean(name="taskScheduler")
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectionString, new ExponentialBackoffRetry(1000, 3));
        TaskSyncClient taskSyncClient = new TaskSyncClient(client, scheculerLeaderPath);

        try {
            client.start();
            taskSyncClient.start();
        } catch (Exception e) {
            throw new RuntimeException("zk can not start.");
        }

        ThreadPoolTaskSchedulerZkSync threadPoolTaskSchedulerZkSync = new ThreadPoolTaskSchedulerZkSync();
        threadPoolTaskSchedulerZkSync.setPoolSize(1);
        threadPoolTaskSchedulerZkSync.setTaskSyncClient(taskSyncClient);
        threadPoolTaskSchedulerZkSync.initialize();

        return threadPoolTaskSchedulerZkSync;
    }
}
