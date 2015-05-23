package cube.d.n.rn;

import cube.d.n.rn.filter.Filter;
import cube.d.n.rn.filter.cornorFilter;

/**
 * Created by Colin_000 on 5/22/2015.
 */
public abstract class OnFilterButton extends OnScreenButton {


    public OnFilterButton(Tess t, Filter f,Vector myVector){
        super(t,getActionForFilter(t,f),myVector);

    }

    private static Action getActionForFilter(final Tess tess,final Filter f) {
        return new Action() {
            @Override
            public void act() {
                tess.filter.set(f);
            }
        };
    }
}
