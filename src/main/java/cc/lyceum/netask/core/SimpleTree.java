package cc.lyceum.netask.core;

import cc.lyceum.netask.core.condition.Condition;
import cc.lyceum.netask.core.event.Event;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Lyceum
 */
public class SimpleTree implements Tree {

    private final Node root;
    private final List<Node> nodes;


    public SimpleTree(Node... nodes) {
        this(true, Arrays.asList(nodes));
    }

    public SimpleTree(List<Node> nodes) {
        this(true, nodes);
    }

    public SimpleTree(boolean checkClosedLoop, List<Node> nodes) {
        Assert.notEmpty(nodes, "it must contain at least 1 node");
        this.nodes = nodes;
        this.root = findRootNode();
        if (checkClosedLoop)
            Assert.isTrue(checkClosedLoop(), "nodes form a closed loop");
    }

    @Override
    public Node root() {
        return this.root;
    }

    @Override
    public void setContext(Context context) {
        nodes.forEach(node -> node.setContext(context));
    }

    @Override
    public synchronized void callNodeActionChain() {
        callNodeActionChain(root);
    }

    private void callNodeActionChain(Node node) {
        if (Objects.isNull(node))
            return;

        Node next;
        Class<? extends Node> classOfNode = node.getClass();
        if (Event.class.isAssignableFrom(classOfNode)) {
            ((Event) node).action();
            next = node.right();
        } else if (Condition.class.isAssignableFrom(classOfNode)) {
            next = ((Condition) node).judge();
        } else {
            throw new IllegalArgumentException("unknown Node type");
        }

        // continue
        callNodeActionChain(next);
    }

    /**
     * check if nodes is closed loop
     *
     * @return true if no closed loop or else
     */
    private boolean checkClosedLoop() {
        try {
            createLine();
            return true;
        } catch (IllegalArgumentException ignore) {
            return false;
        }
    }

    @Override
    public List<LinkedList<Node>> createLine() {
        List<LinkedList<Node>> container = new ArrayList<>();
        createLine(container, 0, root);
        return container;
    }

    private void createLine(List<LinkedList<Node>> container, int index, Node next) {
        Assert.notNull(container, "container must be not null");
        if (Objects.isNull(next))
            return;

        LinkedList<Node> line;
        try {
            line = container.get(index);
        } catch (IndexOutOfBoundsException ignore) {
            line = new LinkedList<>();
            container.add(index, line);
        }

        Assert.isTrue(!line.contains(next), "nodes is closed loop");
        line.add(next);

        // right node
        createLine(container, index, next.right());

        // left node
        if (Condition.class.isAssignableFrom(next.getClass()) && null != next.left()) {
            LinkedList<Node> newLine = new LinkedList<>(line);
            newLine.removeLast();
            container.add(++index, newLine);
            createLine(container, index, next.left());
        }
    }

    private Node findRootNode() {
        List<Node> result = this.nodes.stream()
                .filter(node -> this.nodes.parallelStream().noneMatch(n0 -> isChildNode(n0, node)))
                .collect(Collectors.toList());

        int size = result.size();
        Assert.isTrue(size > 0, "cant found root node");
        Assert.isTrue(size < 2, "too many root nodes, should be 1, but found " + size);

        return result.get(0);
    }

    private boolean isChildNode(Node parent, Node child) {
        Assert.notNull(parent, "parent node must be not null");
        Assert.notNull(child, "child node must be not null");
        try {
            return child.equals(parent.right())
                    || child.equals(parent.left());
        } catch (UnsupportedOperationException ignore) {
            // Event type unsupported left() method
            return false;
        }
    }
}
