package cc.lyceum.netask.core.event;

import cc.lyceum.netask.core.Context;
import cc.lyceum.netask.core.Node;
import org.springframework.lang.Nullable;

import java.util.Objects;

/**
 * @author Lyceum
 */
public abstract class Event implements Node {

    protected final String id;
    @Nullable
    protected Context context;

    @Nullable
    protected Node right;

    public Event(String id) {
        this.id = id;
    }

    /**
     * event action
     */
    public abstract void action();

    protected void setVariable(Object obj) {
        setVariable(getId(), obj);
    }

    protected void setVariable(String name, Object obj) {
        if (Objects.nonNull(context))
            context.setVariable(name, obj);
    }

    protected Object getVariable(String name) {
        return Objects.nonNull(context) ? context.getVariable(name) : null;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Context getContext() {
        return this.context;
    }

    @Override
    public void setContext(@Nullable Context context) {
        this.context = context;
    }

    @Override
    public Node right() {
        return this.right;
    }

    @Override
    public Node left() {
        throw new UnsupportedOperationException("Event is a single node, please use right()");
    }

    public void setRight(@Nullable Node right) {
        this.right = right;
    }
}
