package tellh.com.recyclertreeview_lib;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tlh on 2016/10/1 :)
 */

public class TreeNode<T extends LayoutItemType> implements Cloneable {
    private T content;
    private TreeNode parent;
    private List<TreeNode> childList;
    private boolean isExpand;
    private boolean isLocked;
    private String oidValue = null;
    private String nodeTitle = null;

    //the tree high
    private int height = UNDEFINE;

    private static final int UNDEFINE = -1;

    public TreeNode(@NonNull T content) {
        this.content = content;
        this.childList = new ArrayList<>();
    }

    public TreeNode(@NonNull T content, @NonNull String oidValue, @NonNull String nodeTitle) {
        this.content = content;
        this.childList = new ArrayList<>();
        this.oidValue = oidValue;
        this.nodeTitle = nodeTitle;
    }

    public int getHeight() {
        if (isRoot())
            height = 0;
        else if (height == UNDEFINE)
            height = parent.getHeight() + 1;
        return height;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isLeaf() {
        return childList == null || childList.isEmpty();
    }

    public boolean hasOID() {
        if (oidValue != null && !oidValue.isEmpty()) {
            return true;
        }
        return false;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public T getContent() {
        return content;
    }

    public List<TreeNode> getChildList() {
        return childList;
    }

    public void setChildList(List<TreeNode> childList) {
        this.childList.clear();
        for (TreeNode treeNode : childList) {
            addChild(treeNode);
        }
    }

    public TreeNode addChild(TreeNode node) {
        if (childList == null)
            childList = new ArrayList<>();
        childList.add(node);
        node.parent = this;
        return this;
    }

    public String getOidValue() {
        return oidValue;
    }

    public void setOidValue(String oidValue) {
        this.oidValue = oidValue;
    }

    public boolean toggle() {
        isExpand = !isExpand;
        return isExpand;
    }

    public void collapse() {
        if (isExpand) {
            isExpand = false;
        }
    }

    public void collapseAll() {
        if (childList == null || childList.isEmpty()) {
            return;
        }
        for (TreeNode child : this.childList) {
            child.collapseAll();
        }
    }

    public void expand() {
        if (!isExpand) {
            isExpand = true;
        }
    }

    public void expandAll() {
        expand();
        if (childList == null || childList.isEmpty()) {
            return;
        }
        for (TreeNode child : this.childList) {
            child.expandAll();
        }
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public TreeNode getParent() {
        return parent;
    }

    public TreeNode<T> lock() {
        isLocked = true;
        return this;
    }

    public TreeNode<T> unlock() {
        isLocked = false;
        return this;
    }

    public boolean isLocked() {
        return isLocked;
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "content=" + this.content +
                ", parent=" + (parent == null ? "null" : parent.getContent().toString()) +
                ", childList=" + (childList == null ? "null" : childList.toString()) +
                ", isExpand=" + isExpand +
                '}';
    }

    @Override
    protected TreeNode<T> clone() throws CloneNotSupportedException {
        TreeNode<T> clone = new TreeNode<>(this.content);
        clone.isExpand = this.isExpand;
        return clone;
    }

    public String getNodeTitle() {
        return nodeTitle;
    }

    public void setNodeTitle(String nodeTitle) {
        this.nodeTitle = nodeTitle;
    }

    public boolean isQueryable() {
        if (!hasOID()) {
            return false;
        }
        if (null != getParent() && getParent().isTableOrEntry()) {
            return false;
        }
        return true;
    }

    public boolean isTableOrEntry() {
        if (!hasOID()) {
            return false;
        }
        if (getNodeTitle() != null) {
            return getNodeTitle().endsWith("Table") ||
                    getNodeTitle().endsWith("Entry");
        }
        return false;
    }

    public boolean isDeep() {
        int beforeSize = getNodeTitle().length();
        // checks if it contains at least 3 dots
        return Math.abs(beforeSize - getNodeTitle().replace(".", "").length() ) > 3;
    }
}
