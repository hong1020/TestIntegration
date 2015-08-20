package task;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by hongcheng on 8/19/15.
 */
public class TaskSyncClient extends LeaderSelectorListenerAdapter implements Closeable {

    private final LeaderSelector leaderSelector;
    private final AtomicBoolean isLeader = new AtomicBoolean();
    private final Object lock = new Object();

    public TaskSyncClient(CuratorFramework client, String path)
    {
        isLeader.set(false);
        // create a leader selector using the given path for management
        // all participants in a given leader selection must use the same path
        // ExampleClient here is also a LeaderSelectorListener but this isn't required
        leaderSelector = new LeaderSelector(client, path, this);

        // for most cases you will want your instance to requeue when it relinquishes leadership
        leaderSelector.autoRequeue();

        leaderSelector.start();
    }

    @Override
    public void close() throws IOException {
        isLeader.set(false);
        leaderSelector.close();
    }

    @Override
    public void takeLeadership(CuratorFramework client) throws Exception {
        isLeader.set(true);

        try
        {
            synchronized (lock) {
                lock.wait();
            }
        }
        catch ( InterruptedException e )
        {
            Thread.currentThread().interrupt();
        }
        finally
        {
            isLeader.set(false);
        }
    }

    public boolean isMainClient() {
        return isLeader.get();
    }
}
