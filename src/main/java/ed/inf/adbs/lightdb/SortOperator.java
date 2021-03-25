package ed.inf.adbs.lightdb;

import net.sf.jsqlparser.statement.select.OrderByElement;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortOperator extends Operator{
    Operator child;
    List<OrderByElement> orderByElements;
    List<Tuple> allTupleList;
    int count;

    public SortOperator(List<OrderByElement> orderByElements,Operator child){
        this.child = child;
        this.orderByElements = orderByElements;
        allTupleList = child.dump();
        sortTuple2();
        count = 0;
    }

    public SortOperator(Operator child){
        this.child = child;
        allTupleList = child.dump();
        //this.orderByElements = child.getNextTuple();
        sortTuple2();
        count = 0;
    }
    @Override
    public Tuple getNextTuple() {
        Tuple tuple;
        if(count < allTupleList.size() && (tuple = allTupleList.get(count)) != null){
            count++;
            return tuple;
        }
        return null;
    }


    private void sortTuple2(){
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
//                if(result == 0 & sortCount < maxCount){
//                    sortCount++;
//                    System.out.println("result:"+result);
//                    System.out.println("sortCount:"+sortCount);
//                    System.out.println("eachOrder:"+eachOrder);
//                    eachOrder = orderByElements.get(sortCount);
//                    result = (int) (tuple1.getTupleNumber(eachOrder.toString()) - tuple2.getTupleNumber(eachOrder.toString()));
//                }
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
                    System.out.println("tuple1:"+tuple1.getTupleArray()[sortCount]);
                    System.out.println("tuple2:"+tuple2.getTupleArray()[sortCount]);
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


    @Override
    public void reset() {
        child.reset();
    }


}
