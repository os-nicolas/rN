package cube.d.n.rn;

import android.graphics.Color;
import java.lang.Math;

/**
 * Created by Colin_000 on 4/10/2015.
 */
public class Quads {
    MyPoint tl = new MyPoint();
    MyPoint tr = new MyPoint();
    MyPoint br = new MyPoint();
    MyPoint bl = new MyPoint();
    int color;
    //TODO scale by screen size
    float target = .004f;

    public Quads(MyPoint tl, MyPoint tr, MyPoint bl, MyPoint br, int color) {
        float c = .01f;

        this.tl.x = (tl.x) * 2f - 1f;
        this.tl.y = -(tl.y) * 2f + 1f;

        this.tr.x = (tr.x) * 2f - 1f;
        this.tr.y = -(tr.y) * 2f + 1f;

        this.bl.x = (bl.x) * 2f - 1f;
        this.bl.y = -(bl.y) * 2f + 1f;

        this.br.x = (br.x) * 2f - 1f;
        this.br.y = -(br.y) * 2f + 1f;
        this.color = color;

        float c2 = 0f;

//        float centerX = (this.tl.x + this.tr.x + this.bl.x + this.br.x)/4f;

//        this.tl.x += (this.tl.x<centerX?c:-c);
//        this.tr.x += (this.tr.x<centerX?c:-c);
//        this.bl.x += (this.bl.x<centerX?c:-c);
//        this.br.x += (this.br.x<centerX?c:-c);
        this.tl.x += normalize((this.tl.x * c2 + (this.tr.x + this.bl.x) / 2f) / (c2 + 1) - this.tl.x);
        this.tr.x += normalize((this.tr.x * c2 + (this.tl.x + this.br.x) / 2f) / (c2 + 1) - this.tr.x);
        this.bl.x += normalize((this.bl.x * c2 + (this.tl.x + this.br.x) / 2f) / (c2 + 1) - this.bl.x);
        this.br.x += normalize((this.br.x * c2 + (this.tr.x + this.bl.x) / 2f) / (c2 + 1) - this.br.x);


//        float centerY = (this.tl.y + this.tr.y + this.bl.y + this.br.y)/4f;

//        this.tl.y += (this.tl.y<centerY?c:-c);
//        this.tr.y += (this.tr.y<centerY?c:-c);
//        this.bl.y += (this.bl.y<centerY?c:-c);
//        this.br.y += (this.br.y<centerY?c:-c);
        this.tl.y += normalize((this.tl.y * c2 + (this.tr.y + this.bl.y) / 2f) / (c2 + 1) - this.tl.y);
        this.tr.y += normalize((this.tr.y * c2 + (this.tl.y + this.br.y) / 2f) / (c2 + 1) - this.tr.y);
        this.bl.y += normalize((this.bl.y * c2 + (this.tl.y + this.br.y) / 2f) / (c2 + 1) - this.bl.y);
        this.br.y += normalize((this.br.y * c2 + (this.tr.y + this.bl.y) / 2f) / (c2 + 1) - this.br.y);
    }

    private float normalize(float v) {
        if (Math.abs(v) < target) {
            return v;
        }
        return target * v / Math.abs(v);
    }

    public float getRed() {
        return Color.red(color);
    }

    public float getGreen() {
        return Color.green(color);
    }

    public float getBlue() {
        return Color.blue(color);
    }

    public float getAlpha() {
        return Color.alpha(color);
    }
}
