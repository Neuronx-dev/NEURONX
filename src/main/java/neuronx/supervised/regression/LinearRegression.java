package neuronx.supervised.regression;

import java.util.*;

public class LinearRegression {

    private double[] weights; // [β0, β1, β2, ...]
    private boolean trained = false;

    // ===== 1️⃣ Fit model (Simple & Multiple Regression) =====
    public void fit(double[][] X, double[] y) {
        int n = X.length;
        int m = X[0].length;

        // Add bias column
        double[][] Xb = new double[n][m + 1];
        for (int i = 0; i < n; i++) {
            Xb[i][0] = 1.0;
            for (int j = 0; j < m; j++) {
                Xb[i][j + 1] = X[i][j];
            }
        }

        // Compute β = (XᵀX)⁻¹ Xᵀ y
        double[][] Xt = transpose(Xb);
        double[][] XtX = multiply(Xt, Xb);
        double[][] XtX_inv = inverse(copyMatrix(XtX)); // Copy to avoid modifying
        double[][] XtY = multiply(Xt, toColumnMatrix(y));
        double[][] beta = multiply(XtX_inv, XtY);

        // Save weights
        weights = new double[beta.length];
        for (int i = 0; i < beta.length; i++)
            weights[i] = beta[i][0];

        trained = true;
    }

    // ===== 2️⃣ Predict =====
    public double[] predict(double[][] X) {
        if (!trained) throw new IllegalStateException("Model not trained. Call fit() first.");

        int n = X.length;
        double[] preds = new double[n];

        for (int i = 0; i < n; i++) {
            double y_pred = weights[0];
            for (int j = 0; j < X[i].length; j++)
                y_pred += weights[j + 1] * X[i][j];
            preds[i] = y_pred;
        }

        return preds;
    }

    // ===== 3️⃣ Metrics =====
    public double mean_squared_error(double[] y_true, double[] y_pred) {
        double sum = 0;
        for (int i = 0; i < y_true.length; i++)
            sum += Math.pow(y_true[i] - y_pred[i], 2);
        return sum / y_true.length;
    }

    public double r2_score(double[] y_true, double[] y_pred) {
        double mean = 0;
        for (double v : y_true) mean += v;
        mean /= y_true.length;

        double ss_tot = 0, ss_res = 0;
        for (int i = 0; i < y_true.length; i++) {
            ss_tot += Math.pow(y_true[i] - mean, 2);
            ss_res += Math.pow(y_true[i] - y_pred[i], 2);
        }
        return 1 - (ss_res / ss_tot);
    }

    // ===== 4️⃣ Matrix Utilities =====
    private static double[][] transpose(double[][] A) {
        int r = A.length, c = A[0].length;
        double[][] T = new double[c][r];
        for (int i = 0; i < r; i++)
            for (int j = 0; j < c; j++)
                T[j][i] = A[i][j];
        return T;
    }

    private static double[][] multiply(double[][] A, double[][] B) {
        int r1 = A.length, c1 = A[0].length, c2 = B[0].length;
        double[][] C = new double[r1][c2];
        for (int i = 0; i < r1; i++)
            for (int j = 0; j < c2; j++)
                for (int k = 0; k < c1; k++)
                    C[i][j] += A[i][k] * B[k][j];
        return C;
    }

    private static double[][] toColumnMatrix(double[] y) {
        double[][] col = new double[y.length][1];
        for (int i = 0; i < y.length; i++)
            col[i][0] = y[i];
        return col;
    }

    private static double[][] copyMatrix(double[][] A) {
        double[][] copy = new double[A.length][A[0].length];
        for (int i = 0; i < A.length; i++)
            System.arraycopy(A[i], 0, copy[i], 0, A[0].length);
        return copy;
    }

    // ===== ✅ Stable Gauss–Jordan Inverse =====
    private static double[][] inverse(double[][] A) {
        int n = A.length;
        double[][] I = new double[n][n];
        for (int i = 0; i < n; i++) I[i][i] = 1.0;

        for (int i = 0; i < n; i++) {
            double diag = A[i][i];
            if (Math.abs(diag) < 1e-12)
                throw new ArithmeticException("Matrix not invertible (zero diagonal)");

            for (int j = 0; j < n; j++) {
                A[i][j] /= diag;
                I[i][j] /= diag;
            }

            for (int k = 0; k < n; k++) {
                if (k == i) continue;
                double factor = A[k][i];
                for (int j = 0; j < n; j++) {
                    A[k][j] -= factor * A[i][j];
                    I[k][j] -= factor * I[i][j];
                }
            }
        }
        return I;
    }
}
