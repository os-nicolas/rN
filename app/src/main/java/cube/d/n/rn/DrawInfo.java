package cube.d.n.rn;


/**
 * Created by Colin_000 on 4/2/2015.
 */
public class DrawInfo {
    public ToDraw toDraw;

    // what part of the canvas are we drawing on?
//    public final float top;
//
//    public final float bot;
//    public final float left;
//    public final float right;
    public MyPoint topl;
    public MyPoint topr;
    public MyPoint botr;
    public MyPoint botl;
    public int alpha = 0xff;

    public DrawInfo(ToDraw toDraw) {//, float top, float bot, float left, float right
        this.toDraw = toDraw;
//        this.top = top;
//        this.bot = bot;
//        this.left = left;
//        this.right = right;
    }

    public DrawInfo(DrawInfo base) {
        this(base.toDraw);//,base.top,base.bot,base.left,base.right
    }

//    public float toX(float num) {
//        return left + (num*(right-left));
//    }
//
//    public float toY(float num) {
//        return top + (num*(bot-top));
//    }
//
//    public void upScalePoints() {
//        topl.x = toX(topl.x);
//        topl.y = toY(topl.y);
//        topr.x = toX(topr.x);
//        topr.y = toY(topr.y);
//        botr.x = toX(botr.x);
//        botr.y = toY(botr.y);
//        botl.x = toX(botl.x);
//        botl.y = toY(botl.y);
//    }

//    public float width() {
//        return right - left;
//    }
//
//    public float height() {
//        return bot-top;
//    }


}
