package cube.d.n.rn;

import android.util.Log;

import java.util.ArrayList;



/**
 * Created by Colin on 5/1/2015.
 */
public class FaceIndex extends ArrayList<FaceIndex.FaceValue> {

    public static int enumSize() {
        return FaceValue.values().length;
    }



    public enum FaceValue {
        FORWARD,
        BACK,
        EVEN,
        NONE
    }

    public FaceIndex(FaceIndex index){
        super();
        for (FaceValue fv:index){
            add(fv);
        }
    }

    public FaceIndex(){
        super();
    }

    public ArrayList<Vector> getCompnetVectors(HasVectorDims tess) {
        ArrayList<Vector> result = new ArrayList<>();

        for (int i=0;i< size();i++){
            Vector vec=tess.vectorForDimension(i);
            if (get(i)!= FaceValue.NONE){
                vec.scale((get(i)==FaceValue.FORWARD?1:-1),false);
                result.add(vec);
            }
        }
        return result;
    }

    public ArrayList<FaceValue> getCompnetValues() {
        ArrayList<FaceValue> result = new ArrayList<>();

        for (int i=0;i< size();i++){
            if (get(i)!= FaceValue.NONE){
                result.add(get(i));
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        for (int i = 0; i < size(); i++) {
            hashCode += (int)Math.pow(4,i) * toInt(get(i));
        }
        return hashCode;
    }

    private int toInt(FaceValue faceValue) {
        if (faceValue!= FaceValue.NONE){
            return 0;
        }
        if (faceValue!= FaceValue.FORWARD){
            return 1;
        }
        if (faceValue!= FaceValue.BACK){
            return 2;
        }
        if (faceValue!= FaceValue.EVEN){
            return 3;
        }

        Log.e("toInt","enum not in the enum");
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Index && ((Index) o).size() == size()){
            for (int i=0;i<size();i++){
                if (!get(i).equals(((Index) o).get(i))){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        String result = "(";
        for (FaceValue i: this){
            result += i + (indexOf(i)!=size()-1?",":"");
        }
        return result+")";
    }

    public int noneZeroComps() {
        int count = 0;
        for (FaceValue i:this){
            count += (i!=FaceValue.NONE?1:0);
        }
        return count;
    }

    public FaceIndex rotate(int dim1, int dim2, boolean direction) {
        FaceIndex newIndex = new FaceIndex(this);
        if (direction){
            newIndex.set(dim1,flip(this.get(dim2)));
            newIndex.set(dim2,this.get(dim1));
        }else {
            newIndex.set(dim1,this.get(dim2));
            newIndex.set(dim2,flip(this.get(dim1)));
        }
        if(newIndex.getCompnetValues().size()<2){
            Log.e("FaceIndex-rotate","rotated in to a mess");
        }

        return newIndex;
    }

    public static FaceValue flip(FaceValue faceValue) {
        if (faceValue== FaceValue.FORWARD){
            return FaceValue.BACK;
        }
        if (faceValue== FaceValue.BACK){
            return FaceValue.FORWARD;
        }

        //Log.e("FaceValue-flip","err! you probably changed FaceValue");

        return faceValue;
    }

}
