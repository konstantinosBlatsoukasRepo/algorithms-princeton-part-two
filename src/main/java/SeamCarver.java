import edu.princeton.cs.algs4.Picture;
/**
 * Created by kon on 27/1/2018.
 */
public class SeamCarver {

    private Picture picture;
    private static final double BORDER_ENERGY = 1000.0;
    private double[][] energy;
    private int width;
    private int height;

    public SeamCarver(Picture picture) {
        this.picture = picture;
        width = picture.width() - 1;
        height = picture.height() - 1;
        energy = new double[width][height];
    }

    public Picture picture() {
        return picture;
    }

    public int width() {
        return picture.width();
    }

    public int height() {
        return picture.height();
    }

    public double energy(int x, int y) {
        double pointEnergy = BORDER_ENERGY;
        if (isAPointBorder(x, y)) {
            energy[x][y] = BORDER_ENERGY;
        } else if (energy[x][y] == 0.0) {
            energy[x][y] = calculateGradientSquare(x, y);
            pointEnergy = energy[x][y];
        }
        return pointEnergy;
    }

    private boolean isAPointBorder(int x, int y) {
        if ( x == 0 || y == 0 || y == height ||x == width) {
            return true;
        }
        return false;
    }

    private double calculateGradientSquare(int x, int y) {
        double squaredGradientX = calculateGradientSquare(x, y, true);
        double squaredGgradientY = calculateGradientSquare(x, y, false);
        double gradient = Math.sqrt(squaredGradientX + squaredGgradientY);
        return gradient;
    }

    private double calculateGradientSquare(int x, int y, boolean isXGradient) {
        int firstPointX = x;
        int firstPointY = y;
        if (isXGradient) {
            firstPointX = x + 1;
        } else {
            firstPointY = y + 1;
        }

        int rgbFirstPoint = picture.getRGB(firstPointX, firstPointY);
        int rFirstPoint = (rgbFirstPoint >> 16) & 0xFF;
        int gFirstPoint = (rgbFirstPoint >>  8) & 0xFF;
        int bFirstPoint = (rgbFirstPoint >>  0) & 0xFF;

        int secondPointX = x;
        int secondPointY = y;
        if (isXGradient) {
            secondPointX = x - 1;
        } else {
            secondPointY = y - 1;
        }
        int rgbSecondPoint = picture.getRGB(secondPointX, secondPointY);
        int rSecondPoint = (rgbSecondPoint >> 16) & 0xFF;
        int gSecondPoint = (rgbSecondPoint >>  8) & 0xFF;
        int bSecondPoint = (rgbSecondPoint >>  0) & 0xFF;

        int redDiff = rFirstPoint - rSecondPoint;
        int greenDiff = bFirstPoint - bSecondPoint;
        int blueDiff = gFirstPoint - gSecondPoint;

        double gradientSquared = Math.pow(redDiff, 2.0) + Math.pow(greenDiff, 2.0) + Math.pow(blueDiff, 2.0);
        return gradientSquared;
    }

    public int[] findHorizontalSeam() {
        return null;
    }

    public int[] findVerticalSeam() {
        return null;
    }

    public void removeHorizontalSeam(int[] seam) {

    }

    public void removeVerticalSeam(int[] seam) {

    }

    public static void main(String[] args) {
        Picture picture = new Picture("3x4.png");
        SeamCarver seamCarver = new SeamCarver(picture);

        System.out.println(seamCarver.energy(1,1));
        System.out.println(seamCarver.isAPointBorder(1,3));
        System.out.println(seamCarver.energy(1,2));
    }
}

