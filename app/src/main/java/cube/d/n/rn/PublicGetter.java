package cube.d.n.rn;

/**
 * Created by Colin_000 on 5/7/2015.
 */
public class PublicGetter<T> implements Getter<T> {
    private GS<T> target;

    public void pointsTo(GS<T> target) {
        this.target = target;
    }

    public T get(){
        return target.get();
    }
}
