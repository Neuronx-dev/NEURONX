package neuronx.unsupervised.clustering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * DBSCAN - Density-Based Spatial Clustering of Applications with Noise
 * Works for unsupervised learning problems.
 *
 * Similar to sklearn's DBSCAN:
 * - eps: neighborhood radius
 * - minPts: minimum points required to form a dense region
 */
public class DBSCAN {

    private double eps;
    private int minPts;
    private List<double[]> data;
    private int[] labels; // -1 = noise, otherwise cluster id

    private static final int UNVISITED = 0;
    private static final int NOISE = -1;

    // === Constructor ===
    public DBSCAN(double eps, int minPts) {
        if (eps <= 0 || minPts <= 0)
            throw new IllegalArgumentException("eps and minPts must be positive!");
        this.eps = eps;
        this.minPts = minPts;
    }

    // === Fit method ===
    public void fit(List<double[]> X) {
        data = X;
        labels = new int[X.size()];
        Arrays.fill(labels, UNVISITED);

        int clusterId = 0;

        for (int i = 0; i < X.size(); i++) {
            if (labels[i] != UNVISITED) continue;

            List<Integer> neighbors = regionQuery(i);
            if (neighbors.size() < minPts) {
                labels[i] = NOISE;
            } else {
                clusterId++;
                expandCluster(i, neighbors, clusterId);
            }
        }

        System.out.println("🏁 DBSCAN finished. Total clusters found: " + clusterId);
    }

    // === Expand cluster ===
    private void expandCluster(int pointIndex, List<Integer> neighbors, int clusterId) {
        labels[pointIndex] = clusterId;

        Queue<Integer> queue = new LinkedList<>(neighbors);

        while (!queue.isEmpty()) {
            int current = queue.poll();

            if (labels[current] == NOISE) {
                labels[current] = clusterId; // change noise to border point
            }

            if (labels[current] != UNVISITED) continue;

            labels[current] = clusterId;

            List<Integer> currentNeighbors = regionQuery(current);
            if (currentNeighbors.size() >= minPts) {
                queue.addAll(currentNeighbors);
            }
        }
    }

    // === Find all neighbors within eps radius ===
    private List<Integer> regionQuery(int index) {
        List<Integer> neighbors = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            if (euclideanDistance(data.get(index), data.get(i)) <= eps) {
                neighbors.add(i);
            }
        }
        return neighbors;
    }

    // === Euclidean distance ===
    private double euclideanDistance(double[] a, double[] b) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += Math.pow(a[i] - b[i], 2);
        }
        return Math.sqrt(sum);
    }

    // === Get cluster labels ===
    public int[] getLabels() {
        return labels;
    }

    // === Predict single point’s cluster (optional helper) ===
    public int predict(double[] sample) {
        double minDist = Double.MAX_VALUE;
        int assignedCluster = NOISE;

        for (int i = 0; i < data.size(); i++) {
            double dist = euclideanDistance(sample, data.get(i));
            if (dist <= eps && labels[i] > 0 && dist < minDist) {
                minDist = dist;
                assignedCluster = labels[i];
            }
        }
        return assignedCluster;
    }

    // === Example usage ===
    public static void main(String[] args) {
        // Sample dataset
        List<double[]> X = Arrays.asList(
                new double[]{1.0, 2.0},
                new double[]{1.2, 2.1},
                new double[]{0.8, 1.9},
                new double[]{8.0, 8.0},
                new double[]{8.1, 8.2},
                new double[]{25.0, 80.0} // noise
        );

        DBSCAN dbscan = new DBSCAN(0.5, 2);
        dbscan.fit(X);

        int[] labels = dbscan.getLabels();
        System.out.println("\nCluster labels:");
        for (int i = 0; i < labels.length; i++) {
            System.out.printf("Point %d: %s -> Cluster %d%n", i + 1, Arrays.toString(X.get(i)), labels[i]);
        }

        // Predict new point
        double[] newPoint = {8.05, 8.1};
        int cluster = dbscan.predict(newPoint);
        System.out.println("\nPredicted cluster for " + Arrays.toString(newPoint) + ": " + cluster);
    }
}
