package cc.lyceum.netask.core.enums;

import cc.lyceum.netask.core.Node;
import cc.lyceum.netask.core.condition.SpelExpressionCondition;
import cc.lyceum.netask.core.event.HttpRequestEvent;

/**
 * @author Lyceum
 */
public enum NodeType {

    // event
    HTTP_REQUEST_EVENT(HttpRequestEvent.class),
    // condition
    SPEL_COND(SpelExpressionCondition.class)
    ;

    private final Class<? extends Node> classOfNode;

    NodeType(Class<? extends Node> classOfNode) {
        this.classOfNode = classOfNode;
    }

    public Class<? extends Node> getClassOfNode() {
        return classOfNode;
    }
}
