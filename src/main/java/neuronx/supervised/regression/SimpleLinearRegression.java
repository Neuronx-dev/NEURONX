package neuronx.supervised.regression;

import java.io.*;
import java.util.*;

public class SimpleLinearRegression {

    private double slope;      // β1
    private double intercept;  // β0
    private boolean trained = false;

    // === Train the model ===
    public void fit(double[] X_train, double[] y_train) {
        if (X_train.length != y_train.length) {
            throw new IllegalArgumentException("X and y must have the same length!");
        }

        int n = X_train.length;
        double sumX = 0, sumY = 0, sumXY = 0, sumXX = 0;

        for (int i = 0; i < n; i++) {
            sumX += X_train[i];
            sumY += y_train[i];
            sumXY += X_train[i] * y_train[i];
            sumXX += X_train[i] * X_train[i];
        }

        double meanX = sumX / n;
        double meanY = sumY / n;

        slope = (sumXY - n * meanX * meanY) / (sumXX - n * meanX * meanX);
        intercept = meanY - slope * meanX;

        trained = true;
        System.out.println("✅ Model trained successfully (slope=" + slope + ", intercept=" + intercept + ")");
    }

    // === Predict single value ===
    public double predict(double x) {
        if (!trained) throw new IllegalStateException("Model not trained. Call fit() first.");
        return slope * x + intercept;
    }

    // === Predict multiple values ===
    public double[] predict(double[] X_test) {
        double[] predictions = new double[X_test.length];
        for (int i = 0; i < X_test.length; i++) {
            predictions[i] = predict(X_test[i]);
        }
        return predictions;
    }

    // === Mean Squared Error (MSE) ===
    public double meanSquaredError(double[] y_true, double[] y_pred) {
        double sumError = 0;
        for (int i = 0; i < y_true.length; i++) {
            sumError += Math.pow(y_true[i] - y_pred[i], 2);
        }
        return sumError / y_true.length;
    }

    // === R² Score ===
    public double score(double[] y_true, double[] y_pred) {
        double meanY = 0;
        for (double val : y_true) meanY += val;
        meanY /= y_true.length;

        double ssTot = 0, ssRes = 0;
        for (int i = 0; i < y_true.length; i++) {
            ssTot += Math.pow(y_true[i] - meanY, 2);
            ssRes += Math.pow(y_true[i] - y_pred[i], 2);
        }

        return 1 - (ssRes / ssTot);
    }

    // === Load CSV file ===
    public static double[][] loadCSV(String filePath) {
        List<double[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Skip empty or header lines
                if (line.trim().isEmpty() || line.toLowerCase().contains("x")) continue;

                String[] parts = line.split(",");
                double x = Double.parseDouble(parts[0].trim());
                double y = Double.parseDouble(parts[1].trim());
                data.add(new double[]{x, y});
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading CSV: " + e.getMessage());
        }

        return data.toArray(new double[0][]);
    }

    // === Main Function ===
    public static void main(String[] args) {
        String filePath = "C:\\Users\\DELL\\Desktop\\NEURONX\\dataset_SLR.csv"; // CSV should be in the same folder as src/main/java or provide full path
        double[][] data = loadCSV(filePath);

        if (data.length == 0) {
            System.out.println("❌ No data found in CSV file.");
            return;
        }

        // Shuffle data (optional)
        List<double[]> list = Arrays.asList(data);
        Collections.shuffle(list, new Random(42)); // reproducibility
        data = list.toArray(new double[0][]);

        // Split 80% train, 20% test
        int splitIndex = (int) (data.length * 0.8);
        double[] X_train = new double[splitIndex];
        double[] y_train = new double[splitIndex];
        double[] X_test = new double[data.length - splitIndex];
        double[] y_test = new double[data.length - splitIndex];

        for (int i = 0; i < data.length; i++) {
            if (i < splitIndex) {
                X_train[i] = data[i][0];
                y_train[i] = data[i][1];
            } else {
                X_test[i - splitIndex] = data[i][0];
                y_test[i - splitIndex] = data[i][1];
            }
        }

        // Train model
        SimpleLinearRegression slr = new SimpleLinearRegression();
        slr.fit(X_train, y_train);

        // Predict
        double[] predictions = slr.predict(X_test);

        // Print results
        System.out.println("\n--- Test Predictions ---");
        for (int i = 0; i < X_test.length; i++) {
            System.out.printf("X=%.2f → Predicted=%.2f | Actual=%.2f%n", X_test[i], predictions[i], y_test[i]);
        }

        // Evaluate
        double mse = slr.meanSquaredError(y_test, predictions);
        double r2 = slr.score(y_test, predictions);
        System.out.printf("%nMSE: %.4f%n", mse);
        System.out.printf("R² Score: %.4f%n", r2);
    }
}
