package ed.inf.adbs.lightdb;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;

import java.util.*;

public class QueryPlan {
    private FromItem firstTable;
    private Expression exp;
    private ExpressionList selectExpressionList;
    private ExpressionList whereExpressionList;
    List<SelectItem> selectItems;
    List<Join> joinTables;
    List<String> selectTableNames;
    //List<String> whereTableNames;
    Operator rootOperator;
    DBCatalog dbc = DBCatalog.getInstance();

    public QueryPlan(Statement statement) {
        Select select = (Select) statement;
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        this.firstTable =  plainSelect.getFromItem();
        this.exp = plainSelect.getWhere();
        this.selectItems = plainSelect.getSelectItems();
        this.joinTables = plainSelect.getJoins();
        if(exp!=null) {
            ExtractWhere extractWhere = new ExtractWhere(exp);
            this.selectExpressionList = extractWhere.getSelectExpressionList();
            this.whereExpressionList = extractWhere.getWhereExpressionList();
            this.selectTableNames = extractWhere.getSelectTableNames();
            //this.whereTableNames = extractWhere.getWhereTableNames();

            System.out.println("--------------------");
            System.out.println("selectTableNames:" + selectTableNames);
            System.out.println("selectExpressionList:" + selectExpressionList.getExpressions());
            //System.out.println("whereTableNames:" + whereTableNames);
            System.out.println("getWhereExpressionList:" + whereExpressionList);
            System.out.println("--------------------");
        }
        //selectExpressionList.getExpressions().add(new AndExpression());


        System.out.println("Select body: " + select.getSelectBody());
        System.out.println("PlainSelect.getWhere: " + plainSelect.getWhere());
////             System.out.println("PlainSelect.getWhereClass: " + plainSelect.getWhere().getClass());
        //System.out.println("PlainSelect.getFromItem: " + plainSelect.getFromItem());
        //System.out.println("PlainSelect.getSelectItems: " + plainSelect.getSelectItems());
//        System.out.println("PlainSelect.join: " + plainSelect.getJoins());
//        System.out.println("PlainSelect.joinclass: " + plainSelect.getJoins().getClass());
        //System.out.println("PlainSelect.jfromclass: " + plainSelect.getFromItem().getClass());
//        System.out.println("PlainSelect.joinninerclass: " + plainSelect.getJoins().toString());
////              System.out.println("PlainSelect.getSelectItems: " + plainSelect.getSelectItems().get(0).getClass());
//        System.out.println("selectExpressionList:" + selectExpressionList.getExpressions());
        //System.out.println("getWhereExpressionList:" + whereExpressionList);
        //System.out.println("selectTableNames:" + selectTableNames);
        //System.out.println("whereTableNames:" + whereTableNames);
        //System.out.println("whereTableNames:" + whereTableNames);
        //System.out.println("getWhereExpressionList" + whereExpressionList.getExpressions().get(0));
        constractQueryPlan();
    }


    /**
     * Conncet select expressions for same table, and build a TableNames-Expression Map
     * @return the TableNames-Expression Map for select expressions
     */
    private HashMap<String,Expression> conncetSelectExpressions() {
        HashMap<String,Expression> selectTableNamesExpressionMap = new HashMap<>();
        for(int i = 0; i < selectTableNames.size(); i++){
            if(selectTableNamesExpressionMap.containsKey(selectTableNames.get(i))){
                Expression leftExpression = selectTableNamesExpressionMap.get(selectTableNames.get(i));
                Expression rightExpression = selectExpressionList.getExpressions().get(i);
                Expression newExpression = new AndExpression(leftExpression,rightExpression);
                selectTableNamesExpressionMap.replace(selectTableNames.get(i),newExpression);
            }
//            else if(selectTableNames.get(i).equals(dbc.getConstantTableName())){
//
//            }
            else
                selectTableNamesExpressionMap.put(selectTableNames.get(i),selectExpressionList.getExpressions().get(i));
        }

        System.out.println("selectTableNamesExpressionMap:"+selectTableNamesExpressionMap);

        return selectTableNamesExpressionMap;

    }

    private HashMap<Set<String>,Expression> conncetWhereExpressions(List<String> tableNames){
        Set<Expression> whereExpressionSet = new HashSet<>(whereExpressionList.getExpressions());
        HashMap<Set<String>,Expression> whereTableNamesExpressionMap = new HashMap<>();
        for(int i = 0; i < tableNames.size()-1; i++){
            Expression newExpression = null;
            Set<String> whereTableNamesKey = new HashSet<>();
            for(Expression oneWhereExpression:whereExpressionSet) {
                if (oneWhereExpression.toString().contains(tableNames.get(i + 1))) {
                    for (int j = 0; j < i + 1; j++)
                        if (oneWhereExpression.toString().contains(tableNames.get(j))) {
                            whereTableNamesKey.add(tableNames.get(i + 1));
                            whereTableNamesKey.add(tableNames.get(j));
                            if(newExpression == null)
                                newExpression = oneWhereExpression;
                            else {
                                Expression leftExpression = oneWhereExpression;
                                Expression rightExpression = newExpression;
                                newExpression = new AndExpression(leftExpression, rightExpression);
                            }
                        }
                }
            }
            if(newExpression != null)
                whereTableNamesExpressionMap.put(whereTableNamesKey,newExpression);
        }
//        for(int i = 0; i< whereTableNames.size(); i = i + 2){
//            Set<String> whereTableNamesKey = new HashSet<>();
//            whereTableNamesKey.add(whereTableNames.get(i));
//            whereTableNamesKey.add(whereTableNames.get(i+1));
//            whereTableNamesExpressionMap.put(whereTableNamesKey,whereExpressionList.getExpressions().get(i/2));
//        }

        System.out.println("whereTableNamesExpressionMap:"+whereTableNamesExpressionMap);
        return whereTableNamesExpressionMap;

    }

    /**
     * generate a list of table names according to the order in FROM.
     * @return the list of table names
     */
    private List<String> generateAllTableNames(){

        List<String> tableNames = new ArrayList<>();
        String firstTableName = firstTable.toString();
        tableNames.add(firstTableName);
        if(joinTables!=null) {
            for (Join eachTable : joinTables) {
                tableNames.add(eachTable.toString());
            }
        }

        System.out.println("tableNames:" + tableNames);

        return tableNames;

    }


    private void constractQueryPlan() {

        List<String> tableNames = generateAllTableNames();
        HashMap<String, Expression> selectTableNamesExpressionMap = null;
        HashMap<Set<String>,Expression> whereTableNamesExpressionMap = null;
        if (selectTableNames != null)
            selectTableNamesExpressionMap = conncetSelectExpressions();
        if(whereExpressionList != null)
            whereTableNamesExpressionMap = conncetWhereExpressions(tableNames);


        //initialize Scan Operator
//        List<ScanOperator> scanOperators = new ArrayList<>();
//        for(String eachTableName:tableNames){
//            scanOperators.add(new ScanOperator(eachTableName));
//        }
//
//
//        List<SelectOperator> selectOperators = new ArrayList<>();
//        for(ScanOperator scanOperator:scanOperators){
//            SelectOperator selectOperator;
//            if(selectTableNamesExpressionMap.containsKey(scanOperator.getTableName())) {
//                selectOperator = new SelectOperator(selectTableNamesExpressionMap.get(scanOperator.getTableName()),scanOperator);
//                selectOperators.add(selectOperator);
//            }
//        }


        //initialize Scan Operator way 2
        //List<Operator> scanOperators = new ArrayList<>();
        List<OperatorTreeNode> scanOperatorNodesList = new ArrayList<>();
        for (String eachTableName : tableNames) {
            ScanOperator scanOperator = new ScanOperator(eachTableName);
            OperatorTreeNode scanOperatorNode = new OperatorTreeNode(scanOperator, null, null, null);
            scanOperatorNodesList.add(scanOperatorNode);
        }


        if (selectTableNamesExpressionMap != null) {
            //List<OperatorTreeNode> selectOperatorNodesList = new ArrayList<>();
            SelectOperator selectOperator;
            for (OperatorTreeNode scanOperatorNode : scanOperatorNodesList) {
                String tableName = scanOperatorNode.getOperator().getTableName();
                if (selectTableNamesExpressionMap.containsKey(tableName)) {
                    selectOperator = new SelectOperator(selectTableNamesExpressionMap.get(tableName), scanOperatorNode.getOperator());
                    OperatorTreeNode selectOperatorNode = new OperatorTreeNode(selectOperator, scanOperatorNode, null, null);
                    //selectOperatorNodesList.add(selectOperatorNode);
                }
            }
            if (selectTableNamesExpressionMap.containsKey(dbc.getConstantTableName())) {
                selectOperator = new SelectOperator(selectTableNamesExpressionMap.get(dbc.getConstantTableName()), scanOperatorNodesList.get(0).getRootNode().getOperator());
                OperatorTreeNode selectOperatorNode = new OperatorTreeNode(selectOperator,scanOperatorNodesList.get(0).getRootNode(), null, null);
                //selectOperatorNodesList.add(selectOperatorNode);
            }
        }

        Set<Expression> haveAddedExpressionsSet = new HashSet<>();
        if(tableNames.size() > 1){
            JoinOperator joinOperator;
            for(int i =0;i<scanOperatorNodesList.size()-1;i++)
            {
                OperatorTreeNode leftChildNode = scanOperatorNodesList.get(i).getRootNode();
                OperatorTreeNode rightChildNode = scanOperatorNodesList.get(i+1).getRootNode();
                joinOperator = new JoinOperator(null,leftChildNode.getOperator(),rightChildNode.getOperator());
                OperatorTreeNode joinOperatorNode = new OperatorTreeNode(joinOperator,leftChildNode, rightChildNode, null);
                for(Set<String> whereTableNamesKey: whereTableNamesExpressionMap.keySet()){
                    if(joinOperatorNode.getLeafNodeTableNamesSet().containsAll(whereTableNamesKey)& !haveAddedExpressionsSet.contains(whereTableNamesExpressionMap.get(whereTableNamesKey))){
                        haveAddedExpressionsSet.add(whereTableNamesExpressionMap.get(whereTableNamesKey));
                        joinOperator.setExpression(whereTableNamesExpressionMap.get(whereTableNamesKey));
                        joinOperatorNode.setData(joinOperator);
                    }
                }
                joinOperator.initialize();
            }
        }

        //System.out
        //System.out.println("qqqqqqqqqqqqqq"+selectItems.get(0).toString());
        if(selectItems.get(0).toString() != "*"){
            ProjectOperator projectOperator = new ProjectOperator(selectItems,scanOperatorNodesList.get(0).getRootNode().getOperator());
            OperatorTreeNode projectOperatorNode = new OperatorTreeNode(projectOperator,scanOperatorNodesList.get(0).getRootNode(),null,null);
        }


        //System.out.println("this is root node:"+scanOperatorNodesList.get(0).getRootNode().getOperator().toString());
            //Operator projectOperator = new ProjectOperator(selectItems, null);
            //Operator rootOperator = selectOperatorNodesList.get(scanOperatorNodesList.size()-1).getOperator();
            //Operator rootOperator = scanOperatorNodesList.get(scanOperatorNodesList.size()-1).getOperator();
        Operator operator = scanOperatorNodesList.get(0).getRootNode().getOperator();
        Operator operator2 = scanOperatorNodesList.get(0).getRootNode().getOperator();
        this.rootOperator = scanOperatorNodesList.get(0).getRootNode().getOperator();
        Operator operator3 = scanOperatorNodesList.get(0).getRootNode().getOperator();


        }


    public Operator getRootOpertaor(){
//        ScanOperator scan1 = new ScanOperator(tableName);
//        ScanOperator scan2 = new ScanOperator("Reserves");
//        //scan.dump();
//        SelectOperator sel = new SelectOperator(selectExpressionList.getExpressions().get(0),scan1);
//        JoinOperator join = new JoinOperator(whereExpressionList.getExpressions().get(0),sel,scan2);
//        ProjectOperator pro = new ProjectOperator(selectItems,join);
        //sel.getNextTuple();
        //sel.reset();
        //sel.dump();
        //sel.reset();
        //join.dump();
        //pro.dump();
        //TreeMap<String,String> tree = new TreeMap<>();
        //BinaryTree
        return rootOperator;
    }
}
