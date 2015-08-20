package cube.d.n.rn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Colin on 8/20/2015.
 */
public class Problem {

    final private static HashMap<Integer,Problem> probs = new HashMap<>();
    final String startState;
    final int dim;
    final int size;
    private static  int count=0;
    final int myId = count++;

    public Problem make(String startState, int size,int dim){
        Problem res = new Problem(startState,size,dim);
        probs.put(res.myId,res);
        return res;
    }

    public static Problem getProblem(int id){
        return probs.get(id);
    }

    private Problem(String startState, int size,int dim){
        this.startState = startState;
        this.dim = dim;
        this.size = size;
    }


}
