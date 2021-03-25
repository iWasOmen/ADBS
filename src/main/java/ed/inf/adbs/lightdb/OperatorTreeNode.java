package ed.inf.adbs.lightdb;

import java.util.HashSet;
import java.util.Set;

/**
 * OperatorTreeNode to construct a tree data structure, including information on operator, leftTreeNode,
 * rightTreeNode, parentNode, leafNodeTableNamesSet.
 */
public class OperatorTreeNode {

    private Operator operator;
    private OperatorTreeNode leftTreeNode;
    private OperatorTreeNode rightTreeNode;
    private OperatorTreeNode parentNode;
    private Set<String> leafNodeTableNamesSet;;

    /**
     * Initialize a OperatorTreeNode.
     * @param operator the operator data stored in this node
     * @param leftTreeNode left child node
     * @param rightTreeNode right child node
     * @param parentNode parent node
     */
    public OperatorTreeNode(Operator operator, OperatorTreeNode leftTreeNode, OperatorTreeNode rightTreeNode, OperatorTreeNode parentNode) {
        this.operator = operator;
        this.leftTreeNode = leftTreeNode;
        this.rightTreeNode = rightTreeNode;
        this.parentNode = parentNode;
        leafNodeTableNamesSet = new HashSet<>();
        if (leftTreeNode != null) {
            leftTreeNode.setParentNode(this);
            leafNodeTableNamesSet.addAll(leftTreeNode.getLeafNodeTableNamesSet());
        }
        if (rightTreeNode != null) {
            rightTreeNode.setParentNode(this);
            leafNodeTableNamesSet.addAll(rightTreeNode.getLeafNodeTableNamesSet());
        }
        if (operator != null) {
            if (operator.getClass() == ScanOperator.class)
                leafNodeTableNamesSet.add(((ScanOperator) operator).getTableName());
        }
    }

    /**
     * Get the operator.
     * @return operator
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * Get the scan operator because there is a getTableName() function within it and there needs a type conversion.
     * @return
     */
    public ScanOperator getScanOperator() {
        return (ScanOperator)operator;
    }

    /**
     * Ser the operator.
     * @param operator operator
     */
    public void setData(Operator operator) {
        this.operator = operator;
    }

    /**
     * Get leftTreeNode.
     * @return leftTreeNode
     */
    public OperatorTreeNode getLeftTreeNode() {
        return leftTreeNode;
    }

    /**
     * Set leftTreeNode.
     * @param leftTreeNode leftTreeNode
     */
    public void setLeftTreeNode(OperatorTreeNode leftTreeNode) {
        this.leftTreeNode = leftTreeNode;
    }

    /**
     * Get rightTreeNode.
     * @return rightTreeNode
     */
    public OperatorTreeNode getRightTreeNode() {
        return rightTreeNode;
    }

    /**
     * Set rightTreeNode.
     * @param rightTreeNode rightTreeNode
     */
    public void setRightTreeNode(OperatorTreeNode rightTreeNode) {
        this.rightTreeNode = rightTreeNode;
    }

    /**
     * Get parentTreeNode.
     * @return parentTreeNode
     */
    public OperatorTreeNode getParentNode() {return parentNode;}
    public void setParentNode(OperatorTreeNode parentNode) {
        this.parentNode = parentNode;
    }

    /**
     * Get rootNode.
     * @return rootNode
     */
    public OperatorTreeNode getRootNode(){
        OperatorTreeNode rootNode = this;
        while(rootNode.parentNode != null) {
            rootNode = rootNode.parentNode;
        }
        return rootNode;
    }

    /**
     * Get LeafNodeTableNamesSet to determine the inclusion relationship of the table in the join expression.
     * @return leafNodeTableNamesSet
     */
    public Set<String> getLeafNodeTableNamesSet(){
        return leafNodeTableNamesSet;
    }
}
