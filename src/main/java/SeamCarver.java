import edu.princeton.cs.algs4.Picture;

import java.util.Arrays;

/**
 * Created by kon on 27/1/2018.
 */
public class SeamCarver {

    private final Picture picture;
    private static final double BORDER_ENERGY = 1000.0;
    final double[][] energy;
    private final int width;
    private final int height;

    public SeamCarver(Picture picture) {
        this.picture = picture;
        width = picture.width() - 1;
        height = picture.height() - 1;
        energy = new double[picture.height()][picture.width()];
        calculatePictureEnergies();
    }

    private void calculatePictureEnergies() {
        for (int row = 0; row < picture.height(); row++) {
            for (int column = 0; column < picture.width(); column++) {
                energy(column, row);
                //System.out.println("(" + column + " ," + row + ")" + isAPointBorder(column, row));
            }
        }
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
            energy[y][x] = BORDER_ENERGY;
        } else if (energy[y][x] == 0.0) {
            energy[y][x] = calculateGradientSquare(x, y);
            pointEnergy = energy[y][x];
        }
        return pointEnergy;
    }

    private boolean isAPointBorder(int x, int y) {
        if (x == 0 || y == 0 || y == height || x == width) {
            return true;
        }
        return false;
    }

    private double calculateGradientSquare(int x, int y) {
        double squaredGradientX = calculateGradientSquare(x, y, true);
        double squaredGradientY = calculateGradientSquare(x, y, false);
        double gradient = Math.sqrt(squaredGradientX + squaredGradientY);
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

    private int[] findBestPathForm(int x, int y) {
        return null;
    }

    public void removeHorizontalSeam(int[] seam) {}

    public void removeVerticalSeam(int[] seam) {}

    public static void main(String[] args) {
        Picture picture = new Picture("6x5.png");
        SeamCarver seamCarver = new SeamCarver(picture);
        String energyTable = Arrays.deepToString(seamCarver.energy);
        System.out.println("energyTable = " + energyTable);
    }
}

