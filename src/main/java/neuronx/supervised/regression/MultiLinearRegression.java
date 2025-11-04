package neuronx.supervised.regression;

import java.io.*;
import java.util.*;

public class MultiLinearRegression {
    private double[] weights; // includes bias term
    private boolean trained = false;

    // === Train Model using Ordinary Least Squares ===
    public void fit(double[][] X, double[] y) {
        int n = X.length;       // samples
        int m = X[0].length;    // features

        // Add bias column (X0 = 1)
        double[][] Xb = new double[n][m + 1];
        for (int i = 0; i < n; i++) {
            Xb[i][0] = 1.0; // bias term
            System.arraycopy(X[i], 0, Xb[i], 1, m);
        }

        // Compute (Xᵀ * X)
        double[][] XtX = multiply(transpose(Xb), Xb);

        // Compute (Xᵀ * y)
        double[] Xty = multiply(transpose(Xb), y);

        // Compute weights = (Xᵀ * X)^(-1) * (Xᵀ * y)
        double[][] XtX_inv = invert(XtX);
        weights = multiply(XtX_inv, Xty);

        trained = true;
        System.out.println("✅ Model trained with " + m + " features");
        System.out.println("Weights: " + Arrays.toString(weights));
    }

    // === Predict single sample ===
    public double predict(double[] x) {
        if (!trained) throw new IllegalStateException("Model not trained yet!");
        double y_pred = weights[0]; // bias
        for (int i = 0; i < x.length; i++) y_pred += weights[i + 1] * x[i];
        return y_pred;
    }

    // === Predict multiple samples ===
    public double[] predict(double[][] X) {
        double[] preds = new double[X.length];
        for (int i = 0; i < X.length; i++) preds[i] = predict(X[i]);
        return preds;
    }

    // === Mean Squared Error ===
    public double meanSquaredError(double[] y_true, double[] y_pred) {
        double sum = 0;
        for (int i = 0; i < y_true.length; i++)
            sum += Math.pow(y_true[i] - y_pred[i], 2);
        return sum / y_true.length;
    }

    // === R² Score ===
    public double r2Score(double[] y_true, double[] y_pred) {
        double meanY = Arrays.stream(y_true).average().orElse(0);
        double ssTot = 0, ssRes = 0;
        for (int i = 0; i < y_true.length; i++) {
            ssTot += Math.pow(y_true[i] - meanY, 2);
            ssRes += Math.pow(y_true[i] - y_pred[i], 2);
        }
        return 1 - (ssRes / ssTot);
    }

    // === Utility Methods ===
    private double[][] transpose(double[][] A) {
        double[][] T = new double[A[0].length][A.length];
        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < A[0].length; j++)
                T[j][i] = A[i][j];
        return T;
    }

    private double[][] multiply(double[][] A, double[][] B) {
        double[][] C = new double[A.length][B[0].length];
        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < B[0].length; j++)
                for (int k = 0; k < B.length; k++)
                    C[i][j] += A[i][k] * B[k][j];
        return C;
    }

    private double[] multiply(double[][] A, double[] x) {
        double[] result = new double[A.length];
        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < x.length; j++)
                result[i] += A[i][j] * x[j];
        return result;
    }

    private double[][] invert(double[][] A) {
        int n = A.length;
        double[][] X = new double[n][n];
        double[][] B = new double[n][n];
        for (int i = 0; i < n; i++) {
            B[i][i] = 1;
            System.arraycopy(A[i], 0, X[i], 0, n);
        }

        for (int i = 0; i < n; i++) {
            double diag = X[i][i];
            for (int j = 0; j < n; j++) {
                X[i][j] /= diag;
                B[i][j] /= diag;
            }
            for (int k = 0; k < n; k++) {
                if (k != i) {
                    double factor = X[k][i];
                    for (int j = 0; j < n; j++) {
                        X[k][j] -= factor * X[i][j];
                        B[k][j] -= factor * B[i][j];
                    }
                }
            }
        }
        return B;
    }

    // === Load CSV ===
    public static double[][] loadFeatures(String csvPath) throws IOException {
        List<double[]> X = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                double[] row = new double[values.length - 1];
                for (int i = 0; i < row.length; i++)
                    row[i] = Double.parseDouble(values[i]);
                X.add(row);
            }
        }
        return X.toArray(new double[0][]);
    }

    public static double[] loadLabels(String csvPath) throws IOException {
        List<Double> y = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                y.add(Double.parseDouble(values[values.length - 1]));
            }
        }
        return y.stream().mapToDouble(Double::doubleValue).toArray();
    }

    // === MAIN ===
    public static void main(String[] args) throws Exception {
        String csvPath = "C:\\Users\\DELL\\Desktop\\NEURONX\\dataset_multiLinerRegression.csv";

        double[][] X = loadFeatures(csvPath);
        double[] y = loadLabels(csvPath);

        // Split 80% training / 20% testing
        int splitIndex = (int) (X.length * 0.8);
        double[][] X_train = Arrays.copyOfRange(X, 0, splitIndex);
        double[] y_train = Arrays.copyOfRange(y, 0, splitIndex);
        double[][] X_test = Arrays.copyOfRange(X, splitIndex, X.length);
        double[] y_test = Arrays.copyOfRange(y, splitIndex, y.length);

        MultiLinearRegression mlr = new MultiLinearRegression();
        mlr.fit(X_train, y_train);

        double[] predictions = mlr.predict(X_test);

        System.out.println("\n--- Predictions ---");
        for (int i = 0; i < predictions.length; i++) {
            System.out.printf("Predicted: %.2f | Actual: %.2f%n", predictions[i], y_test[i]);
        }

        double mse = mlr.meanSquaredError(y_test, predictions);
        double r2 = mlr.r2Score(y_test, predictions);

        System.out.printf("%nMSE: %.4f%n", mse);
        System.out.printf("R² Score: %.4f%n", r2);
    }
}
