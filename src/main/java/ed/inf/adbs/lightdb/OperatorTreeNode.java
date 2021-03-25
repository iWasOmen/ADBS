package ed.inf.adbs.lightdb;

import javax.swing.tree.TreeNode;
import java.util.HashSet;
import java.util.Set;

public class OperatorTreeNode {

    private Operator operator;
    private OperatorTreeNode leftTreeNode;
    private OperatorTreeNode rightTreeNode;
    private OperatorTreeNode parentNode;
    private Set<String> leafNodeTableNamesSet;;

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
    public Operator getOperator() {
        return operator;
    }
    public ScanOperator getScanOperator() {
        return (ScanOperator)operator;
    }
    public void setData(Operator operator) {
        this.operator = operator;
    }

    public OperatorTreeNode getLeftTreeNode() {
        return leftTreeNode;
    }
    public void setLeftTreeNode(OperatorTreeNode leftTreeNode) {
        this.leftTreeNode = leftTreeNode;
    }
    public OperatorTreeNode getRightTreeNode() {
        return rightTreeNode;
    }
    public void setRightTreeNode(OperatorTreeNode rightTreeNode) {
        this.rightTreeNode = rightTreeNode;
    }
    public OperatorTreeNode getParentNode() {return parentNode;}
    public void setParentNode(OperatorTreeNode parentNode) {
        this.parentNode = parentNode;
    }
    public OperatorTreeNode getRootNode(){
        OperatorTreeNode rootNode = this;
        while(rootNode.parentNode != null) {
            rootNode = rootNode.parentNode;
        }
        return rootNode;
    }

    public Set<String> getLeafNodeTableNamesSet(){
        return leafNodeTableNamesSet;
    }
}
