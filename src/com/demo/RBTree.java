package com.demo;

public class RBTree<T extends Comparable<T>> {

    private RBTNode<T> mRoot; //根节点

    private static final boolean RED = false;

    private static final boolean BLACK = true;

    public class RBTNode<T extends Comparable<T>> {
        // 颜色
        boolean color;

        // 关键字(键值)
        T key;

        // 左孩子
        RBTNode<T> left;

        // 右孩子
        RBTNode<T> right;

        // 父节点
        RBTNode<T> parent;

        public RBTNode(T key, boolean color, RBTNode<T> parent, RBTNode<T> left, RBTNode<T> right) {
            this.key = key;
            this.color = color;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }

        public T getKey() {
            return key;
        }

        public String toString() {
            return "" + key + (this.color == RED ? "(R)" : "B");
        }
    }

    public RBTree() {
        mRoot = null;
    }

    private RBTNode<T> parentOf(RBTNode<T> node) {
        return node != null ? node.parent : null;
    }

    private boolean colorOf(RBTNode<T> node) {
        return node != null ? node.color : BLACK;
    }

    private boolean isRed(RBTNode<T> node) {
        return ((node != null) && (node.color == RED)) ? true : false;
    }

    private boolean isBlack(RBTNode<T> node) {
        return !isRed(node);
    }

    private void setBlack(RBTNode<T> node) {
        if (node != null)
            node.color = BLACK;
    }

    private void setRed(RBTNode<T> node) {
        if (node != null)
            node.color = RED;
    }

    private void setParent(RBTNode<T> node, RBTNode<T> parent) {
        if (node != null)
            node.parent = parent;
    }

    private void setColor(RBTNode<T> node, boolean color) {
        if (node != null)
            node.color = color;
    }

    /**
     * 对红黑树的节点进行左旋转
     */
    private void leftRotate(RBTNode<T> x) {
        RBTNode<T> xRight = x.right;

        x.right = xRight.left;

        if (null != xRight.left) {
            xRight.left.parent = x;
        }

        xRight.parent = x.parent;

        if (null == x.parent) {
            this.mRoot = xRight;
        } else {
            if (x.parent.left == x) {
                x.parent.left = xRight;
            } else {
                x.parent.right = xRight;
            }
        }

        xRight.left = x;

        x.parent = xRight;
    }

    /**
     * 对红黑树的节点进行右旋转
     */
    private void rightRotate(RBTNode<T> x) {

        RBTNode<T> xLeft = x.left;

        x.left = xLeft.right;

        if (null != xLeft.right) {
            xLeft.right.parent = x;
        }

        if (null == x.parent) {
            this.mRoot = xLeft;
        } else {
            if (x == x.parent.left) {
                x.parent.left = xLeft;
            } else {
                x.parent.right = xLeft;
            }
        }

        xLeft.right = x;

        x.parent = xLeft;
    }

    /**
     * 将结点插入到红黑树中
     */
    private void insert(RBTNode<T> node) {
        int cmp;

        RBTNode<T> y = null;

        RBTNode<T> x = this.mRoot;

        // 当做一颗二叉查找树，将节点添加到二叉查找树上
        while (x != null) {
            y = x;
            cmp = node.key.compareTo(x.key);

            if (cmp < 0) {
                x = x.left;
            } else {
                x = x.right;
            }
        }

        node.parent = y;
        if (null != y) {
            cmp = node.key.compareTo(y.key);
            if (cmp < 0) {
                y.left = node;
            } else {
                y.right = node;
            }
        } else {
            this.mRoot = node;
        }

        node.color = RED;

        insertFixUp(node);
    }

    /**
     * 新建结点key,并将其插入到红黑树中
     */
    public void insert(T key) {
        RBTNode<T> node = new RBTNode<T>(key, BLACK, null, null, null);

        if (null != node) {
            insert(node);
        }
    }

    /**
     * 红黑树插入修正函数
     */
    private void insertFixUp(RBTNode<T> node) {
        RBTNode<T> parent, gparent;

        while (((parent = parentOf(node)) != null) && isRed(parent)) {
            gparent = parentOf(parent);

            // 若“父节点”是“祖父节点的左孩子”
            if (parent == gparent.left) {
                // Case 1条件：叔叔节点是红色
                RBTNode<T> uncle = gparent.right;
                if ((uncle != null) && isRed(uncle)) {
                    setBlack(uncle);
                    setBlack(parent);
                    setRed(gparent);
                    node = gparent;
                    continue;
                }

                // Case 2条件：叔叔是黑色，且当前节点是右孩子
                if (parent.right == node) {
                    RBTNode<T> tmp;
                    leftRotate(parent);
                    tmp = parent;
                    parent = node;
                    node = tmp;
                }

                // Case 3条件：叔叔是黑色，且当前节点是左孩子。
                setBlack(parent);
                setRed(gparent);
                rightRotate(gparent);
            } else { //若"父节点"是"祖父节点"的右孩子
                // Case 1条件：叔叔节点是红色
                RBTNode<T> uncle = gparent.left;
                if ((uncle != null) && isRed(uncle)) {
                    setBlack(uncle);
                    setBlack(parent);
                    setRed(gparent);
                    node = gparent;
                    continue;
                }

                // Case 2条件：叔叔是黑色，且当前节点是左孩子
                if (parent.left == node) {
                    RBTNode<T> tmp;
                    rightRotate(parent);
                    tmp = parent;
                    parent = node;
                    node = tmp;
                }

                // Case 3条件：叔叔是黑色，且当前节点是右孩子。
                setBlack(parent);
                setRed(gparent);
                leftRotate(gparent);
            }
        }
        setBlack(this.mRoot);
    }
}
