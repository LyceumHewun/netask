package cc.lyceum.netask.task;

import cc.lyceum.netask.core.Tree;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.Trigger;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Lyceum
 */
public class Task implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(Task.class);

    private final String id;
    @JsonIgnore
    private final Tree mainTree;
    private final Trigger trigger;

    private ScheduledFuture<?> future;
    private final AtomicInteger count = new AtomicInteger(0);

    public Task(String id, Tree mainTree, Trigger trigger) {
        this.id = id;
        this.mainTree = mainTree;
        this.trigger = trigger;
    }

    @Override
    public void run() {
        try {
            mainTree.callNodeActionChain();
            LOG.info("task [{}] completed {} times", getId(), count.incrementAndGet());
        } catch (Throwable e) {
            e.printStackTrace();
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
