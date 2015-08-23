package cube.d.n.rn;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Colin on 8/20/2015.
 */
public class Problem {

    final private static HashMap<Integer,Problem> probs = new HashMap<>();
    private static final String PREFS_NAME = "PROBLEMS";
    final String startState;
    final int dim;
    final int size;
    private static  int count=0;
    final int myId = count++;

    public static Problem make(String startState, int dim,int size){
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

    public boolean getSolved(){
        SharedPreferences settings = RN.rn().getSharedPreferences(PREFS_NAME, 0);
        return settings.getBoolean(myId + "", false);
    }

    public void setSolved(boolean solved){
        if (!getSolved() && solved){
            for(Runnable r: runOnSolved){
                r.run();
            }

        }

        SharedPreferences settings = RN.rn().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(myId+"", solved);
        editor.commit();
    }

    ArrayList<Runnable> runOnSolved = new ArrayList<>();
    public void onSolved(Runnable runnable) {
        runOnSolved.add(runnable);
    }

    public boolean unlocked() {
        if (myId == 0){
            return true;
        }else{
            Problem before = getProblem(myId-1);
            return before.getSolved();
        }

    }
}
