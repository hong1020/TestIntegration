package task;

import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.List;

/**
 * Created by hongcheng on 8/23/15.
 */
public class Example {
    public static void main(String[] args) {

        List<TaskSyncClient> taskSyncCilentList = Lists.newArrayList();
        for (int i = 0; i < 2; i++) {
            final String name = "client_" + i;
            String connectionString = "192.168.0.108:2181";
            CuratorFramework client = CuratorFrameworkFactory.newClient(connectionString, new ExponentialBackoffRetry(1000, 3));
            TaskSyncClient taskSyncClient = new TaskSyncClient(client, "/examples/leader");
            taskSyncCilentList.add(taskSyncClient);
            try {
                client.start();
                taskSyncClient.start();
            } catch (Exception e) {
                return;
            }

            ThreadPoolTaskSchedulerZkSync threadPoolTaskSchedulerZkSync = new ThreadPoolTaskSchedulerZkSync();
            threadPoolTaskSchedulerZkSync.setPoolSize(1);
            threadPoolTaskSchedulerZkSync.setTaskSyncClient(taskSyncClient);
            threadPoolTaskSchedulerZkSync.initialize();
            threadPoolTaskSchedulerZkSync.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    System.out.println("i am running as " + name);
                }
            }, 1000);
        }

        try {
            Thread.sleep(20000);
            taskSyncCilentList.get(0).close();
        } catch (Exception e) {

        }

        try {
            Thread.sleep(20000);
            taskSyncCilentList.get(1).close();
        } catch (Exception e) {

        }

    }
}
