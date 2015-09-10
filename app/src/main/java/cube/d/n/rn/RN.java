package cube.d.n.rn;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Colin_000 on 4/22/2015.
 */
public class RN extends Application {
    private static final String PREFS_NAME = "GLOBAL";
    private static RN instance;// = new RN();
    ArrayList<LayoutInfo> problems;
    public RN(){
        instance=this;
    }
    public static RN rn(){
        return instance;
    }
//    public int getMaxSize(){
//        return 5;
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        problems = initProblems();
    }


    /**
     * @return milliseconds
     */
    public int fadeTime() {
        return 500;
    }

    public float rate() {
        return 5;
    }

    public float scale() {
        return 1;
    }

    public int getDarkColor() {
        return 0xff888888;
    }

    public float getStrokeWidth() {
        return 3f;
    }

    ArrayList<LayoutInfo> initProblems() {

        ArrayList<LayoutInfo> problemRows = new ArrayList<LayoutInfo>();
        try {
            InputStream is = getApplicationContext().getAssets().open("Problems.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;// = reader.readLine(); // the first line is the table header so we skip it
            //line = reader.readLine(); // the first line is the table header so we skip it
            while ((line = reader.readLine()) != null){
                String[] data = line.split("\t");
                if (data[0].equals("p")) {
                    problemRows.add(Problem.make(data[1], Integer.parseInt(data[2]), Integer.parseInt(data[3])));
                }else if (data[0].equals("i")){
                    problemRows.add(ImagePageInfo.make(data[1]));
                }
            }
            is.close();
        }
        catch (IOException ex) {
            Log.e("error loading file", ex.toString());
        }

        return problemRows;
    }

    public int maxUnlocked() {
        return 0;
    }

//    public int unsolvedIndex() {
//        for (Problem p :problems){
//            if (p.getSolved() == false && p.unlocked()){
//                return p.myId;
//            }
//        }
//        // if they have solved them all just leave them on the first one
//        return 0;
//    }

    public int getLast() {
        SharedPreferences settings = RN.rn().getSharedPreferences(PREFS_NAME, 0);
        return settings.getInt("last", 0);
    }

    public void setLast(int last) {
        SharedPreferences settings = RN.rn().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("last", last);
        editor.commit();
    }
}
