package ed.inf.adbs.lightdb;

import net.sf.jsqlparser.statement.select.SelectItem;

import java.util.ArrayList;
import java.util.List;

public class ProjectOperator extends Operator {

    Operator child;
    List<String> selectTables = new ArrayList<>();
    List<String> selectColumns = new ArrayList<>();

    public ProjectOperator(List<SelectItem> selectItems, Operator child){
        this.child = child;
        for(int i=0; i < selectItems.size(); i++){
            String[] temp = selectItems.get(i).toString().split("\\.");
            selectTables.add(temp[0]);
            selectColumns.add(temp[1]);
        }
    }

    @Override
    public Tuple getNextTuple() {
        Tuple tuple;
        while ((tuple = child.getNextTuple()) != null){
            tuple.projectTuple(selectTables, selectColumns);
            return tuple;
        }
        return null;
    }

    @Override
    public void reset() {
        child.reset();
    }
}
