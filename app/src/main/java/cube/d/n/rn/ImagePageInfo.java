package cube.d.n.rn;

/**
 * Created by Colin on 8/24/2015.
 */
public class ImagePageInfo extends LayoutInfo {

    String src;

    public ImagePageInfo(String src) {
        this.src = src;
    }
    public static ImagePageInfo make(String src){
        ImagePageInfo res = new ImagePageInfo(src);
        probs.put(res.myId,res);
        return res;
    }
}
