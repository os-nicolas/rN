package cube.d.n.rn;

/**
 * Created by Colin_000 on 4/22/2015.
 */
public class ColorKey {
    public final Index faceIndex;
    public final Index atIndex;

    public ColorKey( Index faceIndex, Index atIndex){
        this.faceIndex = faceIndex;
        this.atIndex= atIndex;
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof ColorKey){
            return hashCode() ==o.hashCode();
        }
        return false;
    }

    @Override
    public int hashCode(){
        return atIndex.hashCode()*(int)Math.pow(RN.rn().getMaxSize(),faceIndex.size()) + binaryHash(faceIndex);
    }

    private int binaryHash(Index faceIndex) {
        int hashCode = 0;
        for (int i = 0; i < faceIndex.size(); i++) {
            hashCode += (int)Math.pow(2,i) * (faceIndex.get(i)==0?0:1);
        }
        return hashCode;
    }

    @Override
    public String toString() {
        return faceIndex + "" + atIndex;
    }

}
