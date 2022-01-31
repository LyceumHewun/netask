package cc.lyceum.netask.core.condition;

import cc.lyceum.netask.core.Context;
import cc.lyceum.netask.core.Node;
import cc.lyceum.netask.util.KeyUtils;
import org.springframework.lang.Nullable;

/**
 * @author Lyceum
 */
public abstract class Condition implements Node {

    protected final String id;

    protected Node right;
    protected Node left;

    @Nullable
    protected Context context;
    protected final String expression;

    public Condition(String expression) {
        this("cond_" + KeyUtils.uuidV4NoDashStr(), expression);
    }

    protected Condition(String id, String expression) {
        this.id = id;
        this.expression = expression;
    }

    /**
     * judging the returned node according to the expression
     *
     * @return right node if true or left node else
     */
    public abstract Node judge();

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Node right() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    @Override
    public Node left() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    @Nullable
    public Context getContext() {
        return context;
    }

    @Override
    public void setContext(@Nullable Context context) {
        this.context = context;
    }

    public String getExpression() {
        return this.expression;
    }
}
