package com.lnjoying.justice.commonweb.util.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/10 19:20
 */
public class Tree<T extends TreeI> {
    /**
     * the id of the parameter and the id of the value
     */
    private String id;

    /**
     * The name of the tree node, usually using the name in value.
     */
    private String name;

    /**
     * data stored in the node
     */
    private T nodeData;

    /**
     * number of child nodes included
     */
    private Integer proNum;

    /**
     * parent node node
     */
    private List<T> parent;
    /**
     * child node
     */
    private List<Tree<T>> children;
    /**
     * current tree depth starting at 1
     */
    private Integer level;

    public Tree() {
    }

    public Tree(T t, List<Tree<T>> children, List<T> parent, Integer level) {
        this.id = t.getTreeId();
        this.name = t.getTreeName();
        this.nodeData = t;
        this.proNum = children.size();
        this.children = children;
        this.parent = parent;
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getNodeData() {
        return nodeData;
    }

    public void setNodeData(T nodeData) {
        this.nodeData = nodeData;
    }

    public Integer getProNum() {
        return proNum;
    }

    public void setProNum(Integer proNum) {
        this.proNum = proNum;
    }

    public List<Tree<T>> getChildren() {
        return children;
    }

    public void setChildren(List<Tree<T>> children) {
        this.children = children;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<T> getParent() {
        return new ArrayList<>(parent);
    }

    public void setParent(List<T> parent) {
        this.parent = parent;
    }
}
