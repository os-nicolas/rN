package cube.d.n.rn;

/**
 * Created by Colin on 4/24/2015.
 */
public class GS<T> {
    protected T value;
    public GS(){}
    public GS(T inValue){
        set(inValue);
    }
    public T get(){
        return value;
    }
    public void set(T newValue){
        value = newValue;
    }
}
