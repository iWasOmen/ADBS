package ed.inf.adbs.lightdb;

public class SortOperator extends Operator{
    Operator child;
    @Override
    public Tuple getNextTuple() {
        return null;
    }

    @Override
    public void reset() {
        child.reset();
    }

    @Override
    public String getTableName() {
        return null;
    }
}
