package ed.inf.adbs.lightdb;

public class Tuple {

    private int[] tuple;

    public Tuple(String tupleStr){
        String[] temp = tupleStr.split(",");
        tuple = new int[temp.length];
        for(int i=0;i<temp.length;i++){
            tuple[i] = Integer.parseInt(temp[i]);
            //System.out.println(tuple[i]);
        }
    }

    public int[] getTupleArray(){
        return tuple;
    }
}
