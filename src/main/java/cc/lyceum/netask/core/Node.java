package cc.lyceum.netask.core;

import org.springframework.lang.Nullable;

/**
 * @author Lyceum
 */
public interface Node {

    /**
     * @return id
     */
    String getId();

    /**
     * @return right node
     */
    @Nullable
    Node right();

    /**
     * @return left node
     */
    @Nullable
    Node left();

    /**
     * @return context
     */
    Context getContext();

    void setContext(Context context);
}
