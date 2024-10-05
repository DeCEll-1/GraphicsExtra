package DeCell.GraphicsExtra.Helpers;

import org.lwjgl.util.vector.Vector2f;

import java.util.Random;

public class PerlinNoise {//https://gist.github.com/nbadal/5538346
    final static int TABLE_SIZE = 64;

    private final static double WEIGHT(double T) {
        return ((2.0 * Math.abs(T) - 3.0) * (T) * (T) + 1.0);
    }

    private final int CLAMP(int val, int min, int max) {
        return ((val < min ? min : val) > max ? max : val);
    }

    private final double CLAMP(double val, double min, double max) {
        return ((val < min ? min : val) > max ? max : val);
    }

    final int SCALE_WIDTH = 128;
    final double MIN_SIZE = 0.1;
    final double MAX_SIZE = 16.0;

    private boolean tilable = false;
    private boolean turbulent = false;
    private long seed = 0;
    private int detail = 1;
    private double size = 8.0;

    private int clip;
    private double offset, factor;
    int[] perm_tab = new int[TABLE_SIZE];
    Vector2f[] grad_tab = new Vector2f[TABLE_SIZE];

    public PerlinNoise(long seed) {
        this.seed = seed;
        init();
    }

    public double noise2(double x, double y) {
        x /= 100;
        y /= 100;
        return noise(x, y);
    }

    void init() {
        int i, j, k, t;
        double m;
        Random r;

        r = new Random(seed);

        /* Force sane parameters */
        detail = CLAMP(detail, 0, 15);
        size = CLAMP(size, MIN_SIZE, MAX_SIZE);

        /* Set scaling factors */
        if (tilable) {
            this.size = Math.ceil(size);
            clip = (int) size;
        }

        /* Set totally empiric normalization values */
        if (turbulent) {
            offset = 0.0;
            factor = 1.0;
        } else {
            offset = 0.94;
            factor = 0.526;
        }

        /* Initialize the permutation table */
        for (i = 0; i < TABLE_SIZE; i++)
            perm_tab[i] = i;

        for (i = 0; i < (TABLE_SIZE >> 1); i++) {
            j = r.nextInt(TABLE_SIZE);
            k = r.nextInt(TABLE_SIZE);
            t = perm_tab[j];
            perm_tab[j] = perm_tab[k];
            perm_tab[k] = t;
        }

        /* Initialize the gradient table */
        for (i = 0; i < TABLE_SIZE; i++) {
            grad_tab[i] = new Vector2f();
            do {
                grad_tab[i].setX((float) ((r.nextDouble() * 2) - 1));
                grad_tab[i].setY((float) ((r.nextDouble() * 2) - 1));
                m = grad_tab[i].getX() * grad_tab[i].getX() + grad_tab[i].getY() * grad_tab[i].getY();
            } while (m == 0.0 || m > 1.0);

            m = 1.0 / Math.sqrt(m);
            grad_tab[i].setX((float) (grad_tab[i].getX() * m));
            grad_tab[i].setY((float) (grad_tab[i].getY() * m));
        }

        r = null;
    }

    double plain_noise(double x, double y, int s) {
        Vector2f v = new Vector2f();
        int a, b, i, j, n;
        double sum;

        sum = 0.0;
        x *= s;
        y *= s;
        a = (int) Math.floor(x);
        b = (int) Math.floor(y);

        for (i = 0; i < 2; i++)
            for (j = 0; j < 2; j++) {
                if (tilable)
                    n = perm_tab[betterMod((betterMod((a + i), (clip * s)) + perm_tab[betterMod(betterMod((b + j), (clip * s)), TABLE_SIZE)]), TABLE_SIZE)];
                else
                    n = perm_tab[betterMod(a + i + perm_tab[betterMod(b + j, TABLE_SIZE)], TABLE_SIZE)];
                v.setX((float) (x - a - i));
                v.setY((float) (y - b - j));
                sum += WEIGHT(v.getX()) * WEIGHT(v.getY()) * (grad_tab[n].getX() * v.getX() + grad_tab[n].getY() * v.getY());
            }

        return sum / s;
    }

    /**
     * Modified modulus, so that negative numbers wrap correctly!
     */
    private int betterMod(int val, int range) {
        return (val % range + range) % range;
    }

    double noise(double x, double y) {
        int i;
        int s;
        double sum;

        s = 1;
        sum = 0.0;
        x *= size;
        y *= size;

        for (i = 0; i <= detail; i++) {
            if (turbulent)
                sum += Math.abs(plain_noise(x, y, s));
            else
                sum += plain_noise(x, y, s);
            s <<= 1;
        }

        return (sum + offset) * factor;
    }
}
