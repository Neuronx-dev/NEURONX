package test;

import java.util.*;
import neuronx.supervised.regression.LinearRegression;
import neuronx.utils.FileUtils;

public class TestSLR {

    public static void printSummary(Map<String, List<double[]>> split) {
        System.out.println("Train X: " + split.get("X_train").size());
        System.out.println("Test X: " + split.get("X_test").size());
        System.out.println("Train Y: " + split.get("Y_train").size());
        System.out.println("Test Y: " + split.get("Y_test").size());
    }

    public static void main(String[] args) {
        try {
            String path = "dataset_SLR.csv";
            List<Map<String, String>> data = FileUtils.read_csv(path);
            Map<String, List<double[]>> xy = FileUtils.extract_X_y(data, true);
            Map<String, List<double[]>> split = FileUtils.train_test_split(xy.get("X"), xy.get("Y"), 0.3);

            printSummary(split);

            // Prepare arrays
            double[][] X_train = split.get("X_train").toArray(new double[0][]);
            double[] y_train = split.get("Y_train").stream().mapToDouble(a -> a[0]).toArray();
            double[][] X_test = split.get("X_test").toArray(new double[0][]);
            double[] y_test = split.get("Y_test").stream().mapToDouble(a -> a[0]).toArray();

            LinearRegression model = new LinearRegression();
            model.fit(X_train, y_train);
            double[] preds = model.predict(X_test);

            System.out.println("\n=== Predictions ===");
            for (int i = 0; i < preds.length; i++)
                System.out.printf("Actual: %.2f | Predicted: %.2f%n", y_test[i], preds[i]);

            System.out.printf("\nMSE: %.4f%n", model.mean_squared_error(y_test, preds));
            System.out.printf("R² Score: %.4f%n", model.r2_score(y_test, preds));

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
