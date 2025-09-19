package com.lnjoying.justice.commonweb.util.tree;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/10 19:20
 */
public class TreeUtils<T extends TreeI> {

    public final static String TREE_KEY = "tree";

    final Map<String, List<T>> parentIdGroup;

    List<T> dataList;

    private Comparator<Tree<T>> sort = Comparator.comparing(Tree::getId);

    public TreeUtils(List<T> dataList) {
        this.dataList = dataList;
        this.parentIdGroup = dataList.stream().parallel()
                .filter(m -> m.getTreeParentId() != null && !"".equals(m.getTreeParentId()))
                .collect(Collectors.groupingBy(TreeI::getTreeParentId));
    }

    public static <R extends TreeI> List<Tree<R>> getAllLeaf(List<Tree<R>> trees) {
        List<Tree<R>> leafList = new ArrayList<>();

        Stack<Tree<R>> accessStack = new Stack<>();
        accessStack.addAll(trees);

        while (!accessStack.isEmpty()) {
            Tree<R> treeNode = accessStack.pop();
            if (treeNode.getChildren() == null || treeNode.getChildren().size() == 0) {
                leafList.add(treeNode);
            } else {
                accessStack.addAll(treeNode.getChildren());
            }
        }
        return leafList;
    }

    public static <R extends TreeI> List<Tree<R>> toList(List<Tree<R>> trees) {
        List<Tree<R>> list = new ArrayList<>();

        Stack<Tree<R>> accessStack = new Stack<>();
        accessStack.addAll(trees);

        while (!accessStack.isEmpty()) {
            Tree<R> treeNode = accessStack.pop();
            list.add(treeNode);
            accessStack.addAll(treeNode.getChildren());
        }
        return list;
    }

    public static <R extends TreeI> Tree<R> getNode(List<Tree<R>> trees, String nodeId) {
        Stack<Tree<R>> accessStack = new Stack<>();
        accessStack.addAll(trees);

        while (!accessStack.isEmpty()) {
            Tree<R> treeNode = accessStack.pop();
            if (nodeId.equals(treeNode.getId())) {
                return treeNode;
            } else {
                accessStack.addAll(treeNode.getChildren());
            }
        }
        throw new RuntimeException("unable to query the specified tree node");
    }


    public static <R extends TreeI> List<R> getList(Tree<R> trees) {
        Stack<Tree<R>> accessStack = new Stack<>();
        accessStack.add(trees);
        List<R> treeNodeList = new ArrayList<>();

        while (!accessStack.isEmpty()) {
            Tree<R> treeNode = accessStack.pop();
            treeNodeList.add(treeNode.getNodeData());
            accessStack.addAll(treeNode.getChildren());
        }
        return treeNodeList;
    }


    public List<Tree<T>> buildForest() {
        List<String> treeIds = dataList.stream().map(TreeI::getTreeId).collect(Collectors.toList());
        final List<T> rootNode = dataList.stream()
                .filter(m -> m.getTreeParentId() == null || "".equals(m.getTreeParentId())
                        || !treeIds.contains(m.getTreeParentId()))
                .collect(Collectors.toList());

        List<Tree<T>> treeList = new ArrayList<>(rootNode.size());
        for (T data : rootNode) {
            int level = 1;
            List<T> parent = new ArrayList<>();
            treeList.add(buildTree(data, parent, level));
        }
        if (treeList.size() != 0) {
            treeList.sort(sort);
        }
        return treeList;
    }


    private Tree<T> buildTree(T data, List<T> parent, Integer level) {
        final List<T> children = Optional.ofNullable(parentIdGroup.get(data.getTreeId())).orElse(new ArrayList<>());

        List<T> nextParent = new ArrayList<>(parent);
        nextParent.add(data);

        List<Tree<T>> childrenTree = new ArrayList<>(children.size());
        for (T child : children) {
            Integer nextLevel = level + 1;
            Tree<T> childTree = buildTree(child, nextParent, nextLevel);
            childrenTree.add(childTree);
        }
        if (childrenTree.size() != 0) {
            childrenTree.sort(sort);
        }
        return new Tree<>(data, childrenTree, parent, level);
    }

    public TreeUtils<T> setSort(Comparator<Tree<T>> sort) {
        this.sort = sort;
        return this;
    }
}
