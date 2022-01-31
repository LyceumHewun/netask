package cc.lyceum.netask.core.condition;

import cc.lyceum.netask.core.Node;
import cc.lyceum.netask.util.ExpressionUtils;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * @author Lyceum
 */
public class SpelExpressionCondition extends Condition {

    public SpelExpressionCondition(String id, String expression) {
        super(id, expression);
    }

    @Override
    public Node judge() {
        Expression spelExpression = ExpressionUtils.SPEL_EXPRESSION_PARSER.parseExpression(expression);
        try {
            Boolean result;
            if (Objects.nonNull(context)) {
                result = spelExpression.getValue(context.toEvaluationContext(), Boolean.class);
            } else {
                result = spelExpression.getValue(Boolean.class);
            }
            Assert.notNull(result, "unexpected result");
            return result ? right : left;
        } catch (EvaluationException ex) {
            if (ex.getMessage().contains("Type conversion problem")) {
                throw new RuntimeException("[" + expression + "] not a relational expression");
            }
            throw new RuntimeException("problem in spel expression evaluation", ex);
        }
    }
}
