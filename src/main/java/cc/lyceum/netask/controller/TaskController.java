package cc.lyceum.netask.controller;

import cc.lyceum.netask.common.ResMsg;
import cc.lyceum.netask.task.Task;
import cc.lyceum.netask.task.TaskScheduler;
import cc.lyceum.netask.task.factory.YamlParseFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Objects;

/**
 * @author Lyceum
 */
@RestController
@RequestMapping("task")
public class TaskController {

    private final TaskScheduler scheduler;

    public TaskController(TaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @PostMapping("buildAndStartup/yaml")
    public ResMsg<?> buildAndStartup(@RequestBody String yaml) {
        Task task = YamlParseFactory.parse(yaml);
        scheduler.schedule(task);
        return ResMsg.success();
    }

    @GetMapping("list")
    public ResMsg<Collection<Task>> taskList() {
        return ResMsg.success(scheduler.getRegisterTaskList());
    }

    @PostMapping("stop")
    public ResMsg<?> stop(@RequestParam String taskId, @RequestParam(required = false) Boolean force) {
        if (Objects.isNull(force))
            force = false;
        return ResMsg.judge(scheduler.shutdown(taskId, force));
    }

    @PostMapping("startup")
    public ResMsg<?> startup(@RequestParam String taskId) {
        scheduler.schedule(taskId);
        return ResMsg.success();
    }

    @PostMapping("execute")
    public ResMsg<?> execute(@RequestParam String taskId) {
        scheduler.execute(taskId);
        return ResMsg.success();
    }

    @PostMapping("startup")
    public ResMsg<?> remove(@RequestParam String taskId) {
        scheduler.remove(taskId);
        return ResMsg.success();
    }
}
