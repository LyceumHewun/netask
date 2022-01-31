package cc.lyceum.netask.task.factory;

import cc.lyceum.netask.core.Context;
import cc.lyceum.netask.core.Node;
import cc.lyceum.netask.core.SimpleTree;
import cc.lyceum.netask.core.Tree;
import cc.lyceum.netask.core.condition.Condition;
import cc.lyceum.netask.core.condition.SpelExpressionCondition;
import cc.lyceum.netask.core.enums.NodeType;
import cc.lyceum.netask.core.event.Event;
import cc.lyceum.netask.core.event.HttpRequestEvent;
import cc.lyceum.netask.task.Task;
import cc.lyceum.netask.util.KeyUtils;
import org.jsoup.Connection;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.Assert;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Lyceum
 */
public class YamlParseFactory {

    @SuppressWarnings("unchecked")
    public static Task parse(String yaml) {
        Yaml y0 = new Yaml();
        Map<String, Object> main = y0.load(yaml);

        // cron trigger
        Assert.isTrue(main.containsKey("cron"), "[cron] parameter not found");
        String cron = (String) main.get("cron");
        CronTrigger cronTrigger = new CronTrigger(cron);

        // nodes
        Assert.isTrue(main.containsKey("node"), "[node] parameter not found");
        List<Map<String, Object>> nodeInfos = (List<Map<String, Object>>) main.get("node");
        Map<String, Node> nodeMap = new HashMap<>();
        for (Map<String, Object> nodeInfo : nodeInfos) {
            String id = (String) nodeInfo.get("id");
            NodeType type = NodeType.valueOf((String) nodeInfo.get("type"));
            Node node;
            if (type.equals(NodeType.HTTP_REQUEST_EVENT)) {
                HttpRequestEvent httpRequestEvent = new HttpRequestEvent(id);
                if (nodeInfo.containsKey("url")) {
                    httpRequestEvent.setUrl((String) nodeInfo.get("url"));
                }
                if (nodeInfo.containsKey("body")) {
                    httpRequestEvent.setBody((String) nodeInfo.get("body"));
                }
                if (nodeInfo.containsKey("method")) {
                    httpRequestEvent.setMethod(Connection.Method.valueOf((String) nodeInfo.get("method")));
                }
                if (nodeInfo.containsKey("data")) {
                    httpRequestEvent.setData(((Map<String, Object>) nodeInfo.get("data")).entrySet()
                            .stream()
                            .collect(Collectors.toMap(Map.Entry::getKey, entry -> String.valueOf(entry.getValue()))));
                }
                if (nodeInfo.containsKey("headers")) {
                    httpRequestEvent.setHeaders((Map<String, String>) nodeInfo.get("headers"));
                }
                if (nodeInfo.containsKey("cookies")) {
                    httpRequestEvent.setCookies((Map<String, String>) nodeInfo.get("cookies"));
                }
                if (nodeInfo.containsKey("timeout")) {
                    httpRequestEvent.setTimeout((int) nodeInfo.get("timeout"));
                }
                if (nodeInfo.containsKey("use-context-cookies")) {
                    httpRequestEvent.setUseContextCookies((boolean) nodeInfo.get("use-context-cookies"));
                }
                node = httpRequestEvent;
            } else if (type.equals(NodeType.SPEL_COND)) {
                node = new SpelExpressionCondition(id, (String) nodeInfo.get("expression"));
            } else {
                throw new IllegalArgumentException("unknown node type");
            }
            Assert.isTrue(!nodeMap.containsKey(id), "duplicate node id [" + id + "]");
            nodeMap.put(id, node);
        }

        // build tree
        for (Map<String, Object> nodeInfo : nodeInfos) {
            String id = (String) nodeInfo.get("id");
            Node node = nodeMap.get(id);

            if (Event.class.isAssignableFrom(node.getClass())) {
                if (nodeInfo.containsKey("right")) {
                    ((Event) node).setRight(nodeMap.get((String) nodeInfo.get("right")));
                }
            } else if (Condition.class.isAssignableFrom(node.getClass())) {
                if (nodeInfo.containsKey("right")) {
                    ((Condition) node).setRight(nodeMap.get((String) nodeInfo.get("right")));
                }
                if (nodeInfo.containsKey("left")) {
                    ((Condition) node).setLeft(nodeMap.get((String) nodeInfo.get("left")));
                }
            }
        }
        boolean checkClosedLoop = true;
        if (main.containsKey("check-closed-loop")) {
            checkClosedLoop = (boolean) main.get("check-closed-loop");
        }
        Tree tree = new SimpleTree(checkClosedLoop, new ArrayList<>(nodeMap.values()));
        tree.setContext(new Context());

        // build task
        return new Task(KeyUtils.uuidV4NoDashStr(), tree, cronTrigger);
    }
}
