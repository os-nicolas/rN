package cube.d.n.rn;

import android.app.Application;
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
    private static RN instance;// = new RN();
    ArrayList<Problem> problems;
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

    ArrayList<Problem> initProblems() {

        ArrayList<Problem> problemRows = new ArrayList<Problem>();
        try {
            InputStream is = getApplicationContext().getAssets().open("Problems.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;// = reader.readLine(); // the first line is the table header so we skip it
            //line = reader.readLine(); // the first line is the table header so we skip it
            while ((line = reader.readLine()) != null){
                String[] data = line.split("\t");
                problemRows.add(Problem.make(data[0],Integer.parseInt(data[1]),Integer.parseInt(data[2])));
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
}
