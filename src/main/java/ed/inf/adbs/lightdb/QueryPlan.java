package ed.inf.adbs.lightdb;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import java.util.*;

/**
 * QueryPlan Class to construct a query plan for a Statement,
 * and returns the query plan back to the interpreter.
 * Part of details about explanation of extracting join conditions from the WHERE clause.
 */
public class QueryPlan {
    private String firstTableName;
    private Expression exp;
    private ExpressionList selectExpressionList;
    private ExpressionList joinExpressionList;
    private Distinct distinct;
    private List<SelectItem> selectItems;
    private List<Join> joinTables;
    private List<String> selectTableNames;
    private List<String> oringinalTableNames;
    private List<String> joinTableNames;
    private List<OrderByElement> orderByElements;
    private Operator rootOperator;
    private DBCatalog dbc = DBCatalog.getInstance();

    /**
     * Read the statement and decompose it into many parts and assign values to the corresponding variables.
     * @param statement the statement of sql
     * @throws JSQLParserException
     */
    public QueryPlan(Statement statement) throws JSQLParserException {
        Select select = (Select) statement;
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        oringinalTableNames = new ArrayList<>();
        if(plainSelect.getFromItem().getAlias() != null) {
            this.firstTableName = plainSelect.getFromItem().toString().split(" ")[1];
            oringinalTableNames.add(plainSelect.getFromItem().toString().split(" ")[0]);
        }
        else {
            this.firstTableName = plainSelect.getFromItem().toString();
            oringinalTableNames.add(plainSelect.getFromItem().toString());
        }
        System.out.println("\n---------------------start-------------------------");
        System.out.println("Select body: " + select.getSelectBody());
        System.out.println("firstTable:"+ firstTableName);
        this.exp = plainSelect.getWhere();
        this.selectItems = plainSelect.getSelectItems();
        this.joinTables = plainSelect.getJoins();
        if(exp!=null) {
            /*
            @explanation
            selectExpressionList to store each select expression. Format example: [B.D <= 104, 1 = 1, R.H = 104, B.F = 2]
            joinExpressionList to store each join expression. Format example: (Boats.D = R.H, S.C < Boats.D, S.A = R.G, S.B < Boats.E)
            selectTableNames to store each table name in select expression. Format example: [B, CONSTANT_TABLE, R, B]
            joinTableNames to store each table name in join expression. Format example: [Boats, R, S, Boats, S, R, S, Boats]
             */
            ExtractWhere extractJoin = new ExtractWhere(exp);
            this.selectExpressionList = extractJoin.getSelectExpressionList();
            this.joinExpressionList = extractJoin.getJoinExpressionList();
            this.selectTableNames = extractJoin.getSelectTableNames();
            this.joinTableNames = extractJoin.getWhereTableNames();

            System.out.println("--------------------");
            System.out.println("selectTableNames:" + selectTableNames);
            System.out.println("selectExpressionList:" + selectExpressionList.getExpressions());
            System.out.println("joinTableNames:" + joinTableNames);
            System.out.println("getJoinExpressionList:" + joinExpressionList);
            System.out.println("--------------------");
        }
        this.orderByElements = plainSelect.getOrderByElements();
        this.distinct = plainSelect.getDistinct();

//        System.out.println("orderByElements: " + orderByElements);
//        System.out.println("distinct: " + distinct);
//        System.out.println("PlainSelect.getWhere: " + plainSelect.getWhere());
//        System.out.println("PlainSelect.getWhereClass: " + plainSelect.getWhere().getClass());
//        System.out.println("PlainSelect.getFromItem: " + plainSelect.getFromItem());
//        System.out.println("PlainSelect.getSelectItems: " + plainSelect.getSelectItems());
//        System.out.println("PlainSelect.join: " + plainSelect.getJoins());
//        System.out.println("PlainSelect.joinclass: " + plainSelect.getJoins().getClass());
//        System.out.println("PlainSelect.jfromclass: " + plainSelect.getFromItem().getClass());
//        System.out.println("PlainSelect.joinninerclass: " + plainSelect.getJoins().toString());
//        System.out.println("PlainSelect.getSelectItems: " + plainSelect.getSelectItems().get(0).getClass());
//        System.out.println("selectExpressionList:" + selectExpressionList.getExpressions());
//        System.out.println("getWhereExpressionList:" + whereExpressionList);
//        System.out.println("selectTableNames:" + selectTableNames);
//        System.out.println("whereTableNames:" + whereTableNames);
//        System.out.println("whereTableNames:" + whereTableNames);
//        System.out.println("getWhereExpressionList" + whereExpressionList.getExpressions().get(0));
        constractQueryPlan();
    }


    /**
     * Connect select expressions for same table, and build a TableNames-Expression Map.
     * @return the TableNames-Expression Map for select expressions
     */
    private HashMap<String,Expression> conncetSelectExpressions() {
        HashMap<String,Expression> selectTableNamesExpressionMap = new HashMap<>();
        for(int i = 0; i < selectTableNames.size(); i++){
            /*
            @explanation
            construct an AND expression if two expression are from same table and put the table name and expression in the map.
            */
            if(selectTableNamesExpressionMap.containsKey(selectTableNames.get(i))){
                Expression leftExpression = selectTableNamesExpressionMap.get(selectTableNames.get(i));
                Expression rightExpression = selectExpressionList.getExpressions().get(i);
                Expression newExpression = new AndExpression(leftExpression,rightExpression);
                selectTableNamesExpressionMap.replace(selectTableNames.get(i),newExpression);
            }
            else
                selectTableNamesExpressionMap.put(selectTableNames.get(i),selectExpressionList.getExpressions().get(i));
        }

        /*
        @explanation
        selectTableNamesExpressionMap. Format example:{B=B.D <= 104 AND B.F = 2, R=R.H = 104, CONSTANT_TABLE=1 = 1}
         */
        System.out.println("selectTableNamesExpressionMap:"+selectTableNamesExpressionMap);
        return selectTableNamesExpressionMap;

    }

    /**
     * Connect join expressions for same table, and build a TableNames-Expression Map.
     * @param tableNames the table names in FROM
     * @return the TableNames-Expression Map
     * @throws JSQLParserException
     */
    private HashMap<Set<String>,Expression> conncetJoinExpressions(List<String> tableNames) throws JSQLParserException {
        Set<Expression> joinExpressionSet = new HashSet<>();
        if(joinTableNames.size() != 0)
            joinExpressionSet = new HashSet<>(joinExpressionList.getExpressions());
        HashMap<Set<String>,Expression> joinTableNamesExpressionMap = new HashMap<>();
        Set<String> lastJoinTableNamesKey = new HashSet<>();
        lastJoinTableNamesKey.add(tableNames.get(0));

        /*
        @explanation
        The query plan tree is left deep, which means that from the second table in FROM, each of table join with previous tables.
        So we traverse tables from left to right, check whether each table is included in the joinExpressionSet,
        if it does, it means that the table has a join condition,
        and then check which (several) table before it has a join condition with it and when there is a join condition pair,
        put the table names set and join expression into the map,
        and construct an AND expression if expression are from same table.
        */
        for(int i = 0; i < tableNames.size()-1; i++){
            Expression newExpression = null;
            Set<String> joinTableNamesKey = new HashSet<>();
            joinTableNamesKey.addAll(lastJoinTableNamesKey);
            joinTableNamesKey.add(tableNames.get(i + 1));
            if(joinTableNames.size() != 0) {
                for (Expression oneJoinExpression : joinExpressionSet) {
                    if (oneJoinExpression.toString().contains(tableNames.get(i + 1))) {
                        for (int j = 0; j < i + 1; j++) {
                            if (oneJoinExpression.toString().contains(tableNames.get(j))) {
                                if (newExpression == null)
                                    newExpression = oneJoinExpression;
                                else {
                                    Expression leftExpression = oneJoinExpression;
                                    Expression rightExpression = newExpression;
                                    newExpression = new AndExpression(leftExpression, rightExpression);
                                }
                            }
                        }
                    }
                }
//                if (newExpression == null) {
//                    Expression fixExpression = new EqualsTo(new LongValue("1"),new LongValue("1"));
//                    //Expression leftExpression = fixExpression;
//                    //Expression rightExpression = newExpression;
//                    //newExpression = new AndExpression(leftExpression, rightExpression);
//                    joinTableNamesExpressionMap.put(joinTableNamesKey, fixExpression);
//                }
//                else
                joinTableNamesExpressionMap.put(joinTableNamesKey, newExpression);
            }

        }

        /*
        @explanation
        joinTableNamesExpressionMap. Format example:{[S, Boats]=S.C < Boats.D AND S.B < Boats.E, [R, S]=Boats.D = R.H AND S.A = R.G}
         */
        System.out.println("joinTableNamesExpressionMap:"+ joinTableNamesExpressionMap);
        return joinTableNamesExpressionMap;

    }

    /**
     * Generate a list of table names according to the order in FROM in order to satisfy left deep tree later.
     * @return the list of table names
     */
    private List<String> generateAllTableNames(){

        List<String> tableNames = new ArrayList<>();
        tableNames.add(firstTableName);
        if(joinTables!=null) {
            for (Join eachTable : joinTables) {
                System.out.println("eachTable:"+eachTable);
                if(eachTable.toString().split(" ").length>1)
                {
                    oringinalTableNames.add(eachTable.toString().split(" ")[0]);
                    tableNames.add(eachTable.toString().split(" ")[1]);
                }
                else {
                    oringinalTableNames.add(eachTable.toString());
                    tableNames.add(eachTable.toString());
                }
            }
        }
        DBCatalog dbc = DBCatalog.getInstance();
        dbc.setTableSchema(tableNames,oringinalTableNames);

        System.out.println("tableNames:" + tableNames);

        return tableNames;

    }

    /**
     * Constract the query plan, following by scan, select, join, project, order, distinct order.
     * @throws JSQLParserException
     */
    private void constractQueryPlan() throws JSQLParserException {

        List<String> tableNames = generateAllTableNames();
        HashMap<String, Expression> selectTableNamesExpressionMap = null;
        HashMap<Set<String>,Expression> joinTableNamesExpressionMap = null;
        if (selectExpressionList != null)
            selectTableNamesExpressionMap = conncetSelectExpressions();
        if(oringinalTableNames.size() > 1) {
            //System.out.println("whereExpressionList:" + whereTableNames);
            joinTableNamesExpressionMap = conncetJoinExpressions(tableNames);
        }




        /*
        @explanation
        Initialize Scan Operator.
        To fellow the left deep tree, we need a list to save the order information of scanOperator.
        And also imply a OperatorTreeNode Class to every operator to connect operators.
         */
        List<OperatorTreeNode> scanOperatorNodesList = new ArrayList<>();
        for(int i = 0; i < tableNames.size(); i++){
            ScanOperator scanOperator = new ScanOperator(tableNames.get(i), oringinalTableNames.get(i));
            OperatorTreeNode scanOperatorNode = new OperatorTreeNode(scanOperator, null, null, null);
            scanOperatorNodesList.add(scanOperatorNode);
        }


        /*
        @explanation
        Initialize select operator.
        Build select Operator based on canOperatorNodesList and selectTableNamesExpressionMap.
        If there is a constant table, add a select operator to the first table.
         */
        if (selectTableNamesExpressionMap != null) {
            SelectOperator selectOperator;
            for (OperatorTreeNode scanOperatorNode : scanOperatorNodesList) {
                String tableName = scanOperatorNode.getScanOperator().getTableName();
                if (selectTableNamesExpressionMap.containsKey(tableName)) {
                    selectOperator = new SelectOperator(selectTableNamesExpressionMap.get(tableName), scanOperatorNode.getOperator());
                    OperatorTreeNode selectOperatorNode = new OperatorTreeNode(selectOperator, scanOperatorNode, null, null);
                }
            }
            if (selectTableNamesExpressionMap.containsKey(dbc.getConstantTableName())) {
                selectOperator = new SelectOperator(selectTableNamesExpressionMap.get(dbc.getConstantTableName()), scanOperatorNodesList.get(0).getRootNode().getOperator());
                OperatorTreeNode selectOperatorNode = new OperatorTreeNode(selectOperator,scanOperatorNodesList.get(0).getRootNode(), null, null);
            }
        }

        /*
        @explanation
        Initialize join operator.
        Based on whereTableNamesExpressionMap and the name sets of leaf node of root node,
        determine their containment relationship, if the latter contains the former, assign join expression,
        or assign a fixed 1=1 join condition.
         */
        Set<Expression> haveAddedExpressionsSet = new HashSet<>();
        if(tableNames.size() > 1){
            JoinOperator joinOperator;
            for(int i =0;i<scanOperatorNodesList.size()-1;i++)
            {
                OperatorTreeNode leftChildNode = scanOperatorNodesList.get(i).getRootNode();
                OperatorTreeNode rightChildNode = scanOperatorNodesList.get(i+1).getRootNode();
                /*
                @explanation
                In order to satisfy the k-1 join conditions,
                no matter whether each group of keys has a corresponding expression or not, a join condition is required.
                Therefore, we pre-add a 1=1 join condition (equivalent to compute a cross product later)
                */
                Expression fixExpression =  new EqualsTo(new LongValue("1"),new LongValue("1"));
                joinOperator = new JoinOperator(fixExpression,leftChildNode.getOperator(),rightChildNode.getOperator());
                OperatorTreeNode joinOperatorNode = new OperatorTreeNode(joinOperator,leftChildNode, rightChildNode, null);
                for(Set<String> joinTableNamesKey : joinTableNamesExpressionMap.keySet()){
                    if(joinOperatorNode.getLeafNodeTableNamesSet().containsAll(joinTableNamesKey)& !haveAddedExpressionsSet.contains(joinTableNamesExpressionMap.get(joinTableNamesKey))){
                        haveAddedExpressionsSet.add(joinTableNamesExpressionMap.get(joinTableNamesKey));
                        joinOperator.setExpression(joinTableNamesExpressionMap.get(joinTableNamesKey));
                        joinOperatorNode.setData(joinOperator);
                    }
                }
                joinOperator.initialize();
            }
        }


        /*
        @explanation
        Initialize project operator.
         */
        if(selectItems.get(0).toString() != "*"){
            ProjectOperator projectOperator = new ProjectOperator(selectItems,scanOperatorNodesList.get(0).getRootNode().getOperator());
            OperatorTreeNode projectOperatorNode = new OperatorTreeNode(projectOperator,scanOperatorNodesList.get(0).getRootNode(),null,null);
        }

        /*
        @explanation
        Initialize orderBy operator.
         */
        if(orderByElements != null){
            SortOperator sortOperator = new SortOperator(orderByElements,scanOperatorNodesList.get(0).getRootNode().getOperator());
            OperatorTreeNode sortOperatorNode = new OperatorTreeNode(sortOperator,scanOperatorNodesList.get(0).getRootNode(),null,null);
        }

        /*
        @explanation
        Initialize DuplicateElimination operator.
         */
        if(distinct != null){
            SortOperator sortOperator = new SortOperator(scanOperatorNodesList.get(0).getRootNode().getOperator());
            OperatorTreeNode sortOperatorNode = new OperatorTreeNode(sortOperator,scanOperatorNodesList.get(0).getRootNode(),null,null);
            DuplicateEliminationOperator duplicateEliminationOperator = new DuplicateEliminationOperator(sortOperator);
            OperatorTreeNode DuplicateEliminationOperatorNode = new OperatorTreeNode(duplicateEliminationOperator,scanOperatorNodesList.get(0).getRootNode(),null,null);
        }

        this.rootOperator = scanOperatorNodesList.get(0).getRootNode().getOperator();
        }

    /**
     * Get the root operator.
     * @return root operator.
     */
    public Operator getRootOpertaor(){
        return rootOperator;
    }
}
