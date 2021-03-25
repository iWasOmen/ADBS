package ed.inf.adbs.lightdb;

import net.sf.jsqlparser.statement.select.OrderByElement;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * SortOperator Class to realize sort fuction.
 */
public class SortOperator extends Operator{
    Operator child;
    List<OrderByElement> orderByElements;
    List<Tuple> allTupleList;
    int count;

    /**
     * Initialize a SortOperator based on orderByElements and child operator.
     * Dump() to get all the input tuples.
     * @param orderByElements orderByElements
     * @param child child operator
     */
    public SortOperator(List<OrderByElement> orderByElements,Operator child){
        this.child = child;
        this.orderByElements = orderByElements;
        allTupleList = child.dump();
        sortTuple();
        count = 0;
    }

    /**
     * If there is no orderByElements(distinct), initialize a SortOperator just based on child operator.
     * Dump() to get all the input tuples.
     * @param child child operator
     */
    public SortOperator(Operator child){
        this.child = child;
        allTupleList = child.dump();
        sortTuple();
        count = 0;
    }

    /**
     * Get the next tuple
     * @return
     */
    @Override
    public Tuple getNextTuple() {
        Tuple tuple;
        if(count < allTupleList.size() && (tuple = allTupleList.get(count)) != null){
            count++;
            return tuple;
        }
        return null;
    }


    /**
     * Sort the tuple list
     * Override compare() to compare tuple based on orderByElements.
     * If it is same on one orderByElement(zero result), continue to compare on the next orderByElement
     * until running out of it or get a none zero result.
     * If there is no rderByElements, compare from the first column.
     */
    private void sortTuple(){
        if(orderByElements != null) {
            int maxCount = orderByElements.size();
            //OrderByElement eachOrder = orderByElements.get(count);
            Collections.sort(allTupleList, new Comparator<Tuple>() {
                //int sortCount = 0;
                @Override
                public int compare(Tuple tuple1, Tuple tuple2) {
                    int sortCount = 0;
                    OrderByElement eachOrder;// = orderByElements.get(sortCount);
                    //System.out.println("tuple1:"+tuple1.getTupleNumber(eachOrder.toString()));
                    //System.out.println("tuple2:"+tuple2.getTupleNumber(eachOrder.toString()));
                    int result = 0;
                    while (result == 0 & sortCount < maxCount) {
                        eachOrder = orderByElements.get(sortCount);
                        //System.out.println("result:"+result);
                        //System.out.println("sortCount:"+sortCount);
                        //System.out.println("eachOrder:"+eachOrder);
                        result = (int) (tuple1.getTupleNumber(eachOrder.toString()) - tuple2.getTupleNumber(eachOrder.toString()));
                        sortCount++;
                    }
                    return result;
                }
            });
        }
        else {
            Collections.sort(allTupleList, new Comparator<Tuple>() {
                @Override
                public int compare(Tuple tuple1, Tuple tuple2) {
                    int sortCount = 0;
                    int maxCount = tuple1.getTupleArray().length;
                    //System.out.println("tuple1:"+tuple1.getTupleArray()[sortCount]);
                    //System.out.println("tuple2:"+tuple2.getTupleArray()[sortCount]);
                    int result = 0;
                    while (result == 0 & sortCount < maxCount) {
                        result = (int) (tuple1.getTupleArray()[sortCount] - tuple2.getTupleArray()[sortCount]);
                        sortCount++;
                    }
                    return result;
                }
            });
        }
        System.out.println("--------------sort finish-------------");
    }

    /**
     * reset operator.
     */
    @Override
    public void reset() {
        child.reset();
    }
}
