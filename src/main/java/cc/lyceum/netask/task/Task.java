package cc.lyceum.netask.task;

import cc.lyceum.netask.core.Tree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.Trigger;

import java.util.concurrent.ScheduledFuture;

/**
 * @author Lyceum
 */
public class Task implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(Task.class);

    private final String id;
    private final Tree mainTree;
    private final Trigger trigger;

    private ScheduledFuture<?> future;

    public Task(String id, Tree mainTree, Trigger trigger) {
        this.id = id;
        this.mainTree = mainTree;
        this.trigger = trigger;
    }

    @Override
    public void run() {
        try {
            mainTree.callNodeActionChain();
        } catch (Throwable e) {
            LOG.error("task [{}] error occurred: {}", getId(), e.toString());
        }
    }

    public String getId() {
        return id;
    }

    public Tree getMainTree() {
        return mainTree;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public ScheduledFuture<?> getFuture() {
        return future;
    }

    public void setFuture(ScheduledFuture<?> future) {
        this.future = future;
    }
}
