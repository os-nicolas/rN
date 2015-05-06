package cube.d.n.rn;

import java.util.ArrayList;

import javax.xml.transform.Result;

/**
 * Created by Colin_000 on 4/17/2015.
 */
public class Index extends ArrayList<Integer> {
    public Index(Index index){
        super();
        for (Integer i:index){
            add(new Integer(i));
        }
    }

    public Index(){
        super();
    }

    public Index(int[] ins){
        super();
        for (Integer i:ins){
            add(new Integer(i));
        }
    }

    public Vector getVector(HasVectorDims tess) {
            Vector at = new Vector(tess.getStartAt());

            for (int i=0;i< size();i++){
                Vector vec=tess.vectorForDimension(i);
                vec.scale(get(i),false);
                at.add(vec,false);
            }
            return at;
    }

    public ArrayList<Vector> getCompnetVectors(HasVectorDims tess) {
        ArrayList<Vector> result = new ArrayList<>();

        for (int i=0;i< size();i++){
            Vector vec=tess.vectorForDimension(i);
            vec.scale(get(i),false);
            if (vec.noneZero()) {
                result.add(vec);
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        for (int i = 0; i < size(); i++) {
            hashCode += (int)Math.pow(RN.rn().getMaxSize(),i) * get(i);
        }
        return hashCode;
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
        for (Integer i: this){
            result += i + (indexOf(i)!=size()-1?",":"");
        }
        return result+")";
    }

    public int noneZeroComps() {
        int count = 0;
        for (int i:this){
            count += (i!=0?1:0);
        }
        return count;
    }

    public Index rotate(int dim1, int dim2, boolean direction,Tess owner) {
        Index newIndex = new Index(this);
        if (direction){
            newIndex.set(dim1,owner.size.get()-1-this.get(dim2));
            newIndex.set(dim2,this.get(dim1));
        }else {
            newIndex.set(dim1,this.get(dim2));
            newIndex.set(dim2,owner.size.get()-1-this.get(dim1));
        }
        return newIndex;
    }


}
