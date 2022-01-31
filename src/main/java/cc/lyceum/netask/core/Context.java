package cc.lyceum.netask.core;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.lang.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Lyceum
 */
public class Context {

    private final Map<String, Object> variables = new ConcurrentHashMap<>();

    public void setVariable(String name, Object obj) {
        variables.put(name, obj);
    }

    @Nullable
    public Object getVariable(String name) {
        return variables.get(name);
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public EvaluationContext toEvaluationContext() {
        return putInEvaluationContext(null);
    }

    /**
     * put the {@link #variables} in {@link EvaluationContext}
     *
     * @param target source target
     * @return target if none null or else new {@link StandardEvaluationContext}
     */
    public EvaluationContext putInEvaluationContext(@Nullable EvaluationContext target) {
        EvaluationContext result;
        if (Objects.nonNull(target)) {
            result = target;
        } else {
            result = new StandardEvaluationContext();
        }
        for (Map.Entry<String, Object> entry : this.variables.entrySet()) {
            result.setVariable(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
