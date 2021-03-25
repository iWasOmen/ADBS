package ed.inf.adbs.lightdb;

import net.sf.jsqlparser.statement.select.SelectItem;

import java.util.ArrayList;
import java.util.List;

/**
 * ProjectOperator Class to realize a project function.
 */
public class ProjectOperator extends Operator {

    Operator child;
    List<String> selectTables = new ArrayList<>();
    List<String> selectColumns = new ArrayList<>();

    /**
     * Initialize a ProjectOperator Class based on selectItems and child operator.
     * @param selectItems selectItems
     * @param child child operator
     */
    public ProjectOperator(List<SelectItem> selectItems, Operator child){
        this.child = child;
        for(int i=0; i < selectItems.size(); i++){
            String[] temp = selectItems.get(i).toString().split("\\.");
            selectTables.add(temp[0]);
            //selectColumns.add(temp[1]);
            selectColumns.add(selectItems.get(i).toString());
        }
    }

    /**
     * Get next tuple.
     * @return tuple
     */
    @Override
    public Tuple getNextTuple() {
        Tuple tuple;
        while ((tuple = child.getNextTuple()) != null){
            tuple.projectTuple(selectTables, selectColumns);
            return tuple;
        }
        return null;
    }

    /**
     * reset operator.
     */
    @Override
    public void reset() {
        child.reset();
    }
}
