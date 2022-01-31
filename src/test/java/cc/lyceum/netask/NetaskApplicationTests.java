package cc.lyceum.netask;

import cc.lyceum.netask.core.Context;
import cc.lyceum.netask.core.SimpleTree;
import cc.lyceum.netask.core.condition.Condition;
import cc.lyceum.netask.core.condition.SpelExpressionCondition;
import cc.lyceum.netask.core.event.HttpRequestEvent;
import org.jsoup.Connection;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

@SpringBootTest
class NetaskApplicationTests {

    @Test
    void contextLoads() {
        Context context = new Context();
        HttpRequestEvent e0 = new HttpRequestEvent("e0");
        e0.setUrl("http://www.baidu.com");

        Condition c0 = new SpelExpressionCondition("c0", "#e0.statusCode == 200");

        HttpRequestEvent e1 = new HttpRequestEvent("e1");
        e1.setUrl("http://lyceum.cc");

        HttpRequestEvent e2 = new HttpRequestEvent("e2");
        e2.setUrl("http://alibaba.com");


        e0.setRight(c0);
        c0.setRight(e1);
        c0.setLeft(e2);

        SimpleTree tree = new SimpleTree(e0, e1, c0, e2);
        tree.setContext(context);

        tree.callNodeActionChain();
        System.out.println(context.getVariables());
        context.getVariables().values().stream()
                .filter(obj -> Connection.Response.class.isAssignableFrom(obj.getClass()))
                .map(Connection.Response.class::cast)
                .map(Connection.Response::statusCode)
                .forEach(System.out::println);
    }

    @Test
    void testYaml() {
        Yaml yaml = new Yaml();
        Object o = yaml.load("cron: 10 * * * * *\n" +
                "node:\n" +
                "  - id: event0\n" +
                "    type: HTTP_REQUEST_EVENT\n" +
                "    url: http://www.baidu.com/\n" +
                "    right: cond0\n" +
                "    method: POST\n" +
                "    headers:\n" +
                "      Content-Type: \"application/json\"\n" +
                "      Referer: http://baidu.com/\n" +
                "  - id: cond0\n" +
                "    type: SPEL_COND\n" +
                "    expression: \"#e0.statusCode == 200\"\n" +
                "    right: event1\n" +
                "  - id: event1\n" +
                "    type: HTTP_REQUEST_EVENT\n" +
                "    url: http://lyceum.cc/");
        System.out.println(((Map<String, Object>)o));
    }
}
