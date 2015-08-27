package task;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by hongcheng on 8/19/15.
 */
public class TaskSyncClient extends LeaderSelectorListenerAdapter implements Closeable {
    private final static Logger logger = LoggerFactory.getLogger(TaskSyncClient.class);

    private final LeaderSelector leaderSelector;
    private final AtomicBoolean isLeader = new AtomicBoolean();

    public TaskSyncClient(CuratorFramework client, String path)
    {
        isLeader.set(false);
        // create a leader selector using the given path for management
        // all participants in a given leader selection must use the same path
        // ExampleClient here is also a LeaderSelectorListener but this isn't required
        leaderSelector = new LeaderSelector(client, path, this);

        // for most cases you will want your instance to requeue when it relinquishes leadership
        leaderSelector.autoRequeue();
    }

    public synchronized void start() throws IOException{
        leaderSelector.start();
    }

    @Override
    public synchronized void close() throws IOException {
        isLeader.set(false);
        leaderSelector.close();
        notify();
    }

    @Override
    public synchronized void takeLeadership(CuratorFramework client) throws Exception {
        logger.info("take the leadership");

        isLeader.set(true);

        try
        {
            wait();
        }
        catch ( InterruptedException e )
        {
            Thread.currentThread().interrupt();
        }
        finally
        {
            isLeader.set(false);
        }

        logger.info("release the leadership");
    }

    public boolean isMainClient() {
        return isLeader.get();
    }
}
