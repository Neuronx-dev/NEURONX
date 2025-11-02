package neuronx.supervised.regression;

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

    // === Main Function ===
    public static void main(String[] args) {
        // Sample dataset (X, y)
        double[][] data = {
            {1.5, 52}, {2.3, 54}, {2.9, 62}, {3.8, 70}, {4.2, 71},
            {4.5, 75}, {5.1, 78}, {5.5, 82}, {6.0, 89}, {6.8, 91},
            {7.1, 94}, {8.0, 96}, {8.5, 98}, {9.2, 99}, {10.0, 100},
            {10.5, 102}, {11.0, 104}, {11.5, 107}, {12.0, 110}, {12.5, 112}
        };

        // Shuffle data (optional for randomness)
        List<double[]> list = Arrays.asList(data);
        Collections.shuffle(list, new Random(42)); // fixed seed for reproducibility
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

        // Train the model
        SimpleLinearRegression slr = new SimpleLinearRegression();
        slr.fit(X_train, y_train);

        // Predict on test data
        double[] predictions = slr.predict(X_test);

        // Print predictions
        System.out.println("\n--- Test Predictions ---");
        for (int i = 0; i < X_test.length; i++) {
            System.out.printf("X=%.2f → Predicted=%.2f | Actual=%.2f%n", X_test[i], predictions[i], y_test[i]);
        }

        // Evaluate model
        double mse = slr.meanSquaredError(y_test, predictions);
        double r2 = slr.score(y_test, predictions);
        System.out.printf("%nMSE: %.4f%n", mse);
        System.out.printf("R² Score: %.4f%n", r2);
    }
}
