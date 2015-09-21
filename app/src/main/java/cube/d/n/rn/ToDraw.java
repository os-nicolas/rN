package cube.d.n.rn;

import java.util.ArrayList;

import java.util.ArrayList;

/**
 * Created by Colin_000 on 4/10/2015.
 */
public class ToDraw {
    ArrayList<Quads> quads = new ArrayList<Quads>();

    public ToDraw() {
    }

    public float[] getColors() {
        //TODO this was working when this did not have very many points

        float[] colors = new float[quads.size() * 16];
        ArrayList<Quads> bkgQuads = getBkgQuads();
        for (int i = 0; i < bkgQuads.size(); i++) {
            int at = i * 16;
            Quads quad = bkgQuads.get(i);
            colors[at] = quad.getRed() / (float) 0xff;//
            colors[(at) + 1] = quad.getGreen() / (float) 0xff;//
            colors[(at) + 2] = quad.getBlue() / (float) 0xff;//1f;//
            colors[at + 3] = quad.getAlpha() / (float) 0xff;//.4f;//

            colors[at + 4] = quad.getRed() / (float) 0xff;//1f;//
            colors[at + 5] = quad.getGreen() / (float) 0xff;//1f;//
            colors[at + 6] = quad.getBlue() / (float) 0xff;//1f;//
            colors[at + 7] = quad.getAlpha() / (float) 0xff;//1f;//.4f;//

            colors[at + 8] = quad.getRed() / (float) 0xff;//1f;//
            colors[at + 9] = quad.getGreen() / (float) 0xff;//1f;//
            colors[at + 10] = quad.getBlue() / (float) 0xff;//1f;//
            colors[at + 11] = quad.getAlpha() / (float) 0xff;//1f;//.4f;//

            colors[at + 12] = quad.getRed() / (float) 0xff;//1f;//
            colors[at + 13] = quad.getGreen() / (float) 0xff;//1f;//
            colors[at + 14] = quad.getBlue() / (float) 0xff;//1f;//
            colors[at + 15] = quad.getAlpha() / (float) 0xff;//1f;//.4f;//

        }
        ArrayList<Quads> fgQuads = getfgQuads();
        for (int i = 0; i < fgQuads.size(); i++) {
            int at = (bkgQuads.size() + i) * 16;
            Quads quad = fgQuads.get(i);
            colors[at] = quad.getRed() / (float) 0xff;//
            colors[at + 1] = quad.getGreen() / (float) 0xff;//
            colors[at + 2] = quad.getBlue() / (float) 0xff;//1f;//
            colors[at + 3] = quad.getAlpha() / (float) 0xff;//.4f;//

            colors[at + 4] = quad.getRed() / (float) 0xff;//1f;//
            colors[at + 5] = quad.getGreen() / (float) 0xff;//1f;//
            colors[at + 6] = quad.getBlue() / (float) 0xff;//1f;//
            colors[at + 7] = quad.getAlpha() / (float) 0xff;//1f;//.4f;//

            colors[at + 8] = quad.getRed() / (float) 0xff;//1f;//
            colors[at + 9] = quad.getGreen() / (float) 0xff;//1f;//
            colors[at + 10] = quad.getBlue() / (float) 0xff;//1f;//
            colors[at + 11] = quad.getAlpha() / (float) 0xff;//1f;//.4f;//

            colors[at + 12] = quad.getRed() / (float) 0xff;//1f;//
            colors[at + 13] = quad.getGreen() / (float) 0xff;//1f;//
            colors[at + 14] = quad.getBlue() / (float) 0xff;//1f;//
            colors[at + 15] = quad.getAlpha() / (float) 0xff;//1f;//.4f;//

        }
        return colors;
    }

    private ArrayList<Quads> getBkgQuads() {
        ArrayList<Quads> result = new ArrayList<Quads>();
        for (Quads q : quads) {
            if (q.getAlpha() / (float) 0xff == 1f) {
                result.add(q);
            }
        }
        return result;
    }

    private ArrayList<Quads> getfgQuads() {
        ArrayList<Quads> result = new ArrayList<Quads>();
        for (Quads q : quads) {
            if (q.getAlpha() / (float) 0xff != 1f) {
                result.add(q);
            }
        }
        return result;
    }

    public float[] getCoords() {
        float[] colors = new float[quads.size() * 12];
        ArrayList<Quads> bkgQuads = getBkgQuads();
        for (int i = 0; i < bkgQuads.size(); i++) {
            int at = i * 12;
            Quads quad = bkgQuads.get(i);
            colors[at + 0] = quad.tl.x;
            colors[at + 1] = quad.tl.y;
            colors[at + 2] = 0;

            colors[at + 3] = quad.bl.x;
            colors[at + 4] = quad.bl.y;
            colors[at + 5] = 0;

            colors[at + 6] = quad.br.x;
            colors[at + 7] = quad.br.y;
            colors[at + 8] = 0;

            colors[at + 9] = quad.tr.x;
            colors[at + 10] = quad.tr.y;
            colors[at + 11] = 0;

        }

        ArrayList<Quads> fgQuads = getfgQuads();
        for (int i = 0; i < fgQuads.size(); i++) {
            int at = (i + bkgQuads.size()) * 12;
            Quads quad = fgQuads.get(i);
            colors[at + 0] = quad.tl.x;
            colors[at + 1] = quad.tl.y;
            colors[at + 2] = 0;

            colors[at + 3] = quad.bl.x;
            colors[at + 4] = quad.bl.y;
            colors[at + 5] = 0;

            colors[at + 6] = quad.br.x;
            colors[at + 7] = quad.br.y;
            colors[at + 8] = 0;

            colors[at + 9] = quad.tr.x;
            colors[at + 10] = quad.tr.y;
            colors[at + 11] = 0;

        }
        return colors;
    }

    public short[] getOrder() {
        short[] colors = new short[quads.size() * 6];
        for (int i = 0; i < quads.size(); i++) {
            colors[(6 * i) + 0] = (short) ((4 * i) + 0);
            colors[(6 * i) + 1] = (short) ((4 * i) + 1);
            colors[(6 * i) + 2] = (short) ((4 * i) + 2);

            colors[(6 * i) + 3] = (short) ((4 * i) + 0);
            colors[(6 * i) + 4] = (short) ((4 * i) + 2);
            colors[(6 * i) + 5] = (short) ((4 * i) + 3);
        }
        return colors;
    }
}
