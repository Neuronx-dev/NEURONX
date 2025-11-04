package test;

import java.util.*;
import neuronx.unsupervised.clustering.DBSCAN;
import neuronx.utils.FileUtils;

public class TestDBSCAN {

    public static void main(String[] args) {
        System.out.println("\n📒 Clustering (Unsupervised) Dataset:");

        String path = "dataset_unsupervised.csv";
        List<Map<String, String>> data = FileUtils.read_csv(path);

        if (data == null || data.isEmpty()) {
            System.out.println("No data found! Check dataset path.");
            return;
        }

        Map<String, List<double[]>> xy = FileUtils.extract_X_y(data, false);
        Map<String, List<double[]>> split = FileUtils.train_test_split(xy.get("X"), xy.get("Y"), 0.3);

        printSummary(split);

        // Run DBSCAN
        DBSCAN dbscan = new DBSCAN(1.5, 3);
        dbscan.fit(split.get("X_train"));

        int[] labels = dbscan.getLabels();
        System.out.println("\n📊 Cluster Labels:");
        for (int i = 0; i < labels.length; i++) {
            System.out.printf("Point %d: %s → Cluster %d%n", i + 1, Arrays.toString(split.get("X_train").get(i)), labels[i]);
        }
        System.err.println();
        System.out.println("\n🔍 Predicting New Samples:");
        // 🧪 Predict multiple new samples
double[][] samples = {
    {8.0, 8.2},   // near Cluster B
    {2.1, 2.0},   // near Cluster A
    {9.9, 2.2},   // near Cluster C
    {7.2, 7.0},   // near Cluster B
    {10.1, 2.3},  // near Cluster C
    {1.9, 2.3}    // near Cluster A
};

// 🔍 Loop through all samples and predict their cluster

    for (double[] sample : samples) {
       int cluster = dbscan.predict(sample);
       System.out.println("🔹 Predicted cluster for " + Arrays.toString(sample) + " → " + cluster);
}

    }

    private static void printSummary(Map<String, List<double[]>> split) {
        System.out.println("Training size: " + split.get("X_train").size());
        System.out.println("Testing size: " + split.get("X_test").size());
        System.out.println("Feature dimensions: " + split.get("X_train").get(0).length);
    }
}
