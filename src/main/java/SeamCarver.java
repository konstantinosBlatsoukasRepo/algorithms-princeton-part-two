import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

/**
 * Created by yiyuanliu on 2017/2/2.
 */
public class SeamCarver {
    private int[][] colors;

    public SeamCarver(Picture picture) {
        if (picture == null) throw new NullPointerException();
        colors = new int[picture.width()][picture.height()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                colors[i][j] = picture.get(i, j).getRGB();
            }
        }
    }

    public Picture picture() {
        Picture picture = new Picture(colors.length, colors[0].length);
        for (int i = 0; i < colors.length; i++) {
            for (int j = 0; j < colors[0].length; j++) {
                Color color = new Color(this.colors[i][j]);
                picture.set(i, j, color);
            }
        }
        return picture;
    }

    public int width() {
        return this.colors.length;
    }

    public int height() {
        return this.colors[0].length;
    }

    public double energy(int x, int y) {
        if (x < 0 || x > this.width() - 1 || y < 0 || y > this.height() - 1) {
            throw new IndexOutOfBoundsException();
        }
        if (x == 0 || x == this.width() - 1 || y == 0 || y == this.height() - 1) {
            return 1000.0;
        } else {
            int deltaXRed = red(colors[x - 1][y]) -
                    red(colors[x + 1][y]);
            int deltaXGreen = green(colors[x - 1][y]) -
                    green(colors[x + 1][y]);
            int deltaXBlue = blue(colors[x - 1][y]) -
                    blue(colors[x + 1][y]);

            int deltaYRed = red(colors[x][y - 1]) - red(colors[x][y + 1]);
            int deltaYGreen = green(colors[x][y - 1]) - green(colors[x][y + 1]);
            int deltaYBlue = blue(colors[x][y - 1]) - blue(colors[x][y + 1]);

            return Math.sqrt(Math.pow(deltaXRed, 2) + Math.pow(deltaXBlue, 2) + Math.pow(deltaXGreen, 2) + Math.pow(deltaYRed, 2) + Math.pow(deltaYBlue, 2) + Math.pow(deltaYGreen, 2));
        }

    }

    public int[] findHorizontalSeam() {
        this.colors = transpose(this.colors);
        int[] seam = findVerticalSeam();
        this.colors = transpose(this.colors);
        return seam;
    }


    public int[] findVerticalSeam() {
        int n = this.width() * this.height();
        int[] seam = new int[this.height()];
        int[] nodeTo = new int[n];
        double[] distTo = new double[n];
        for (int i = 0; i < n; i++) {
            if (i < width())
                distTo[i] = 0;
            else
                distTo[i] = Double.POSITIVE_INFINITY;
        }
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                for (int k = -1; k <= 1; k++) {
                    if (j + k < 0 || j + k > this.width() - 1 || i + 1 < 0 || i + 1 > this.height() - 1) {
                        continue;
                    } else {
                        if (distTo[index(j + k, i + 1)] > distTo[index(j, i)] + energy(j, i)) {
                            distTo[index(j + k, i + 1)] = distTo[index(j, i)] + energy(j, i);
                            nodeTo[index(j + k, i + 1)] = index(j, i);
                        }
                    }
                }
            }
        }

        // find min dist in the last row
        double min = Double.POSITIVE_INFINITY;
        int index = -1;
        for (int j = 0; j < width(); j++) {
            if (distTo[j + width() * (height() - 1)] < min) {
                index = j + width() * (height() - 1);
                min = distTo[j + width() * (height() - 1)];
            }
        }

        // find seam one by one
        for (int j = 0; j < height(); j++) {
            int y = height() - j - 1;
            int x = index - y * width();
            seam[height() - 1 - j] = x;
            index = nodeTo[index];
        }

        return seam;
    }

    public void removeHorizontalSeam(int[] seam) {
        if (height() <= 1) throw new IllegalArgumentException();
        if (seam == null) throw new NullPointerException();
        if (seam.length != width()) throw new IllegalArgumentException();

        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] > height() - 1)
                throw new IllegalArgumentException();
            if (i < width() - 1 && Math.pow(seam[i] - seam[i + 1], 2) > 1)
                throw new IllegalArgumentException();
        }

        int[][] updatedColor = new int[width()][height() - 1];
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] == 0) {
                System.arraycopy(this.colors[i], seam[i] + 1, updatedColor[i], 0, height() - 1);
            } else if (seam[i] == height() - 1) {
                System.arraycopy(this.colors[i], 0, updatedColor[i], 0, height() - 1);
            } else {
                System.arraycopy(this.colors[i], 0, updatedColor[i], 0, seam[i]);
                System.arraycopy(this.colors[i], seam[i] + 1, updatedColor[i], seam[i], height() - seam[i] - 1);
            }

        }
        this.colors = updatedColor;
    }

    public void removeVerticalSeam(int[] seam) {
        this.colors = transpose(this.colors);
        removeHorizontalSeam(seam);
        this.colors = transpose(this.colors);
    }


    private int red(int rgb) {
        return (rgb >> 16) & 0xFF;
    }

    private int green(int rgb) {
        return (rgb >> 8) & 0xFF;
    }

    private int blue(int rgb) {
        return (rgb >> 0) & 0xFF;
    }

    private int index(int x, int y) {
        return width() * y + x;
    }

    private int[][] transpose(int[][] origin) {
        if (origin == null) throw new NullPointerException();
        if (origin.length < 1) throw new IllegalArgumentException();
        int[][] result = new int[origin[0].length][origin.length];
        for (int i = 0; i < origin[0].length; i++) {
            for (int j = 0; j < origin.length; j++) {
                result[i][j] = origin[j][i];
            }
        }
        return result;
    }

//    public static void main(String[] args) {
//        String path = "/Users/yiyuanliu/IdeaProjects/Seam Carving/seamCarving/10x10.png";
//        Picture picture = new Picture(path);
//        StdOut.println("row:" + picture.height() + " column:" + picture.width());
//        SeamCarver sc = new SeamCarver(picture);
//        int[] verticalSeam = sc.findVerticalSeam();
//        for (int x : verticalSeam)
//            StdOut.print(x + " ");
//    }

}