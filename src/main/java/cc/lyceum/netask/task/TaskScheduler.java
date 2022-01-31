package cc.lyceum.netask.task;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Lyceum
 */
@Component("netaskTaskScheduler")
public class TaskScheduler {

    private final ThreadPoolTaskScheduler scheduler;

    // key is task id
    private final Map<String, Task> taskMap = new ConcurrentHashMap<>();

    public TaskScheduler(ThreadPoolTaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void execute(String taskId) {
        Task task = taskMap.get(taskId);
        Assert.notNull(task, "the task is not registered");
        execute(task);
    }

    public void execute(Task task) {
        scheduler.execute(task);
    }

    public void schedule(String taskId) {
        Task task = taskMap.get(taskId);
        Assert.notNull(task, "the task is not registered");
        schedule(task);
    }

    public void schedule(Task task) {
        String id = task.getId();
        registerTask(task);
        Assert.isTrue(notRunning(id), "the task has already run, must stop first");
        ScheduledFuture<?> future = scheduler.schedule(task, task.getTrigger());
        task.setFuture(future);
    }

    public boolean notRunning(String taskId) {
        return !isRunning(taskId);
    }

    public boolean isRunning(String taskId) {
        return taskMap.containsKey(taskId)
                && null != taskMap.get(taskId).getFuture()
                && !taskMap.get(taskId).getFuture().isDone();
    }

    /**
     * @param taskId task id
     * @param force  true if the thread executing this task should be interrupted; otherwise, in-progress tasks are allowed to complete
     * @return false if the task could not be cancelled, typically because it has already completed normally; true otherwise
     */
    public boolean shutdown(String taskId, boolean force) {
        return isRunning(taskId)
                && taskMap.get(taskId).getFuture().cancel(force);
    }

    public void registerTask(Task task) {
        Assert.isTrue(!taskMap.containsKey(task.getId()), "the task id is duplicated");
        taskMap.put(task.getId(), task);
    }

    public Collection<Task> getRegisterTaskList() {
        return taskMap.values();
    }

    public void remove(String taskId) {
        taskMap.remove(taskId);
    }
}
