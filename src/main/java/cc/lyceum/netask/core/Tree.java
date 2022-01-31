package cc.lyceum.netask.core;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Lyceum
 */
public interface Tree {

    /**
     * @return root node
     */
    Node root();

    /**
     * set context to all nodes
     *
     * @param context context
     */
    void setContext(Context context);

    /**
     * invoke node actions from the root node
     */
    void callNodeActionChain();

    /**
     * create possible node relation line
     *
     * @return container to receive results
     * @throws IllegalArgumentException nodes closed loop
     */
    List<LinkedList<Node>> createLine();
}
