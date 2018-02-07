import edu.princeton.cs.algs4.Picture;

import java.util.*;

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
            }
        }

        //TODO: remove after testing
        topologicalSort(new Point(0, 2));
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


        for (int row = 0; row < picture.height(); row++) {
        }
        return null;
    }

    private List<Point> topologicalSort(Point point) {
        List<Point> topologicalSortedPoints = new ArrayList<>();
        topologicalSortedPoints.add(point);

        List<Point> currentAdjacents = findAdjacents(topologicalSortedPoints);
        addToTopologicalSortedList(currentAdjacents, topologicalSortedPoints);

        while (!currentAdjacents.isEmpty()) {
            currentAdjacents = findAdjacents(currentAdjacents);
            addToTopologicalSortedList(currentAdjacents, topologicalSortedPoints);
        }

        return topologicalSortedPoints;
    }

    private List<Point> findAdjacents(List<Point> points) {
        List<Point> adjacentPoints = new ArrayList<>();
        List<String> duplicatePoints = new ArrayList<>();
        for (Point point : points) {
            int x = point.getX();
            int y = point.getY();

            if (!isOutOfRange(x, y)) {
                duplicatePoints.add(x + "," +y);
                int candidateX = x + 1;

                int firstCandidateY = y - 1;
                if (!duplicatePoints.contains(candidateX + "," + firstCandidateY) && !isOutOfRange(candidateX, firstCandidateY)) {
                    Point firstCandidatePoint = new Point(candidateX, firstCandidateY);
                    adjacentPoints.add(firstCandidatePoint);
                    duplicatePoints.add(candidateX + "," + firstCandidateY);
                }

                int secondCandidateY = y;
                if (!duplicatePoints.contains(candidateX + "," + secondCandidateY) && !isOutOfRange(candidateX, secondCandidateY)) {
                    Point secondCandidatePoint = new Point(candidateX, secondCandidateY);
                    adjacentPoints.add(secondCandidatePoint);
                    duplicatePoints.add(candidateX + "," + secondCandidateY);
                }

                int thirdCandidateY = y + 1;
                if (!duplicatePoints.contains(candidateX + "," + thirdCandidateY) && !isOutOfRange(candidateX, thirdCandidateY)) {
                    Point thirdCandidatePoint = new Point(candidateX, thirdCandidateY);
                    adjacentPoints.add(thirdCandidatePoint);
                    duplicatePoints.add(candidateX + "," + thirdCandidateY);
                }
            }
        }
        return adjacentPoints;
    }

    private boolean isOutOfRange(int x, int y) {
        boolean isOutOfRange = false;
        if (y < 0) { isOutOfRange = true; }
        if (x > height) { isOutOfRange = true; }
        if (y > width) { isOutOfRange = true; }
        return isOutOfRange;
    }

    private void addToTopologicalSortedList(List<Point> currentAdjacents, List<Point> topologicalSortedPoints) {
        for (Point currentAdjacent : currentAdjacents) {
            topologicalSortedPoints.add(currentAdjacent);
        }

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


        seamCarver.findVerticalSeam();

        System.out.println("energyTable = " + energyTable);
    }

    private final class Point {
        private final int x;
        private final int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

    }
}

