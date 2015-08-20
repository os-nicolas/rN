package cube.d.n.rn;

import android.util.Log;

import java.util.ArrayList;



/**
 * Created by Colin on 5/1/2015.
 */
public class FaceIndex extends ArrayList<FaceIndex.FaceValue> {

    public enum FaceValue {
        FORWARD,
        BACK,
        EVEN,
        NONE
    }

    //TODO
    //TODO
    //TODO
    //TODO
    //TODO
    //TODO
    //TODO
    //TODO
    //TODO
    //TODO
    //TODO
    //TODO

//    public ArrayList<Vector> getCompnetVectors(HasVectorDims tess) {
//        ArrayList<Vector> result = new ArrayList<>();
//
//        for (int i=0;i< size();i++){
//            Vector vec=tess.vectorForDimension(i);
//            vec.scale(get(i),false);
//            if (vec.noneZero()) {
//                result.add(vec);
//            }
//        }
//        return result;
//    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        for (int i = 0; i < size(); i++) {
            hashCode += (int)Math.pow(FaceValue.values().length,i) * (toInt(get(i)));
        }
        return hashCode;
    }

    private int toInt(FaceValue fv) {
        if (fv== FaceValue.BACK){
            return 0;
        }else if (fv== FaceValue.FORWARD){
            return 1;
        }else if (fv == FaceValue.EVEN){
            return 2;
        }else if (fv == FaceValue.NONE){
            return 3;
        }
        Log.e("toInt","unexpected value "+ fv );
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

//    public int noneZeroComps() {
//        int count = 0;
//        for (FaceValue i:this){
//            count += (i!= FaceValue.NONE?1:0);
//        }
//        return count;
//    }
//
//    public Index rotate(int dim1, int dim2, boolean direction,Tess owner) {
//        Index newIndex = new Index(this);
//        if (direction){
//            newIndex.set(dim1,owner.size.get()-1-this.get(dim2));
//            newIndex.set(dim2,this.get(dim1));
//        }else {
//            newIndex.set(dim1,this.get(dim2));
//            newIndex.set(dim2,owner.size.get()-1-this.get(dim1));
//        }
//        return newIndex;
//    }

}
