package cube.d.n.rn;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.util.Log;

import cube.d.n.rn.filter.CubeFilter;
import cube.d.n.rn.filter.cornorFilter;

/**
 * Created by Colin_000 on 5/22/2015.
 */
public class EdgeFilterButton extends OnFilterButton {
    public EdgeFilterButton(Tess that, int dim, int value) {
        super(that,new CubeFilter(that,dim,value), getVector(that,dim,value));
    }

    private static Vector getVector(Tess that, int dim, int value) {
        Index yo0 = new Index(new int[that.dim.get()]);
        Index yo1 = new Index(new int[that.dim.get()]);

        int end = that.size.get()-1;

        yo0.set(dim,value);
        yo1.set(dim,value);
        if (dim-1>=0){
            yo0.set(dim-1,value);
            yo1.set(dim-1,value);
            if (dim-2>=0){
                yo0.set(dim-2,0);
                yo1.set(dim-2,end);
                if (dim-3>=0){
                    yo0.set(dim,(value==end?0:end));
                    yo1.set(dim,(value==end?0:end));
                }
            }
        }
        if (dim+1<that.dim.get()){
            yo0.set(dim+1,value);
            yo1.set(dim+1,value);
            if (dim+2<that.dim.get()){
                yo0.set(dim+2,0);
                yo1.set(dim+2,end);
                if (dim+3<that.dim.get()){
                    yo0.set(dim+3,(value==end?0:end));
                    yo1.set(dim+3,(value==end?0:end));
                }
            }
        }

        Vector v0 = CornoreFilterButton.getVector(that,yo0);
        Vector v1 = CornoreFilterButton.getVector(that,yo1);

        Vector result = v1.add(v0,true);
        result.scale(.5f,false);

        return result;
    }

    @Override
    protected Bitmap updateBitmap(Bitmap old) {
        Picture picture = new Picture();
        Canvas canvas = picture.beginRecording((int)(2*radius), (int)(2*radius));

        drawBkg(canvas);

        picture.endRecording();

        return Util.pictureDrawable2Bitmap(picture,old);
    }
}
