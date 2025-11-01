package nuronx.supervised.classification;

import java.util.*;

/**
 * KNeighborsClassifier - Simple KNN model (similar to sklearn)
 * Works for supervised classification problems.
 */
public class KNNClassifier {

    // === Internal class for dataset points ===
    private static class DataPoint {
        double[] features;
        String label;

        DataPoint(double[] features, String label) {
            this.features = features;
            this.label = label;
        }
    }

    private List<DataPoint> trainingData = new ArrayList<>();
    private int k = 3;  // Default to 3 (odd number)

    // === Constructor with auto-adjust for odd k ===
    public KNNClassifier(int k) {
        this.k = (k % 2 == 0) ? k + 1 : k; // ensure k is odd
    }

    public KNNClassifier() {
        this.k = 3; // default
    }

    // === Fit method (like sklearn's fit) ===
    public void fit(List<double[]> X_train, List<String> y_train) {
        if (X_train.size() != y_train.size()) {
            throw new IllegalArgumentException("X and Y size must match!");
        }
        for (int i = 0; i < X_train.size(); i++) {
            trainingData.add(new DataPoint(X_train.get(i), y_train.get(i)));
        }
        System.out.println("✅ Model trained with " + trainingData.size() + " samples. (k=" + k + ")");
    }

    // === Predict single sample ===
    public String predict(double[] X_test) {
        if (trainingData.isEmpty()) {
            throw new IllegalStateException("Model not trained. Call fit() first.");
        }

        List<Map.Entry<Double, String>> distances = new ArrayList<>();

        // Compute distances
        for (DataPoint dp : trainingData) {
            double dist = euclideanDistance(dp.features, X_test);
            distances.add(new AbstractMap.SimpleEntry<>(dist, dp.label));
        }

        // Sort by distance
        distances.sort(Comparator.comparing(Map.Entry::getKey));

        // Count labels in top k
        Map<String, Integer> labelCount = new HashMap<>();
        for (int i = 0; i < Math.min(k, distances.size()); i++) {
            String label = distances.get(i).getValue();
            labelCount.put(label, labelCount.getOrDefault(label, 0) + 1);
        }

        // Return majority label
        return Collections.max(labelCount.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    // === Predict multiple samples ===
    public List<String> predict(List<double[]> X_test) {
        List<String> predictions = new ArrayList<>();
        for (double[] sample : X_test) {
            predictions.add(predict(sample));
        }
        return predictions;
    }

    // === Euclidean distance ===
    private double euclideanDistance(double[] a, double[] b) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += Math.pow(a[i] - b[i], 2);
        }
        return Math.sqrt(sum);
    }

    // === Example usage ===
    public static void main(String[] args) {
        // Example dataset
        List<double[]> X_train = Arrays.asList(
                new double[]{1.0, 2.0},
                new double[]{2.0, 3.0},
                new double[]{3.0, 3.0},
                new double[]{6.0, 5.0}
        );

        List<String> y_train = Arrays.asList("A", "A", "B", "B");

        double[] testPoint = {2.5, 2.7};

        // Initialize model
        KNNClassifier knn = new KNNClassifier(4); // even number → auto converts to 5
        knn.fit(X_train, y_train);

        // Predict
        String result = knn.predict(testPoint);
        System.out.println("Predicted class: " + result);

        // Predict multiple points
        List<double[]> X_test = Arrays.asList(
                new double[]{2.5, 2.7},
                new double[]{5.5, 4.8}
        );
        List<String> preds = knn.predict(X_test);
        System.out.println("Batch predictions: " + preds);
    }
}
