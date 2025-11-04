package neuronx.utils;

import java.io.*;
import java.util.*;

/**
 * FileUtils - Single-class utility (like pandas + sklearn)
 * Supports CSV & JSON for ML preprocessing
 * All static methods — no object creation
 */
public class FileUtils {

    // === 1️⃣ Read CSV ===
    public static List<Map<String, String>> read_csv(String filePath) {
        List<Map<String, String>> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String headerLine = br.readLine();
            if (headerLine == null || headerLine.trim().isEmpty()) return data;

            String[] headers = headerLine.split(",");
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] values = line.split(",");
                Map<String, String> row = new LinkedHashMap<>();

                for (int i = 0; i < headers.length; i++) {
                    String key = headers[i].trim();
                    String val = i < values.length ? values[i].trim() : "";
                    row.put(key, val);
                }

                data.add(row);
            }

        } catch (IOException e) {
            System.out.println("❌ Error reading CSV: " + e.getMessage());
        }

        return data;
    }

    // === 2️⃣ Read JSON (very simple JSON array of objects) ===
    public static List<Map<String, String>> read_json(String filePath) {
        List<Map<String, String>> data = new ArrayList<>();
        StringBuilder json = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) json.append(line.trim());
        } catch (IOException e) {
            System.out.println("❌ Error reading JSON: " + e.getMessage());
        }

        String raw = json.toString().replaceAll("\\[|\\]", "");
        String[] objs = raw.split("\\},\\{");

        for (String obj : objs) {
            obj = obj.replace("{", "").replace("}", "");
            if (obj.trim().isEmpty()) continue;

            String[] pairs = obj.split(",");
            Map<String, String> map = new LinkedHashMap<>();

            for (String pair : pairs) {
                String[] kv = pair.split(":");
                if (kv.length == 2) {
                    String key = kv[0].replaceAll("\"", "").trim();
                    String val = kv[1].replaceAll("\"", "").trim();
                    map.put(key, val);
                }
            }
            data.add(map);
        }

        return data;
    }

    // === 3️⃣ Extract X and Y (for supervised or unsupervised) ===
    public static Map<String, List<double[]>> extract_X_y(List<Map<String, String>> data, boolean supervised) {
        Map<String, List<double[]>> result = new HashMap<>();
        List<double[]> X = new ArrayList<>();
        List<double[]> Y = new ArrayList<>();

        if (data == null || data.isEmpty()) {
            result.put("X", X);
            result.put("Y", Y);
            return result;
        }

        List<String> keys = new ArrayList<>(data.get(0).keySet());
        int n = keys.size();

        for (Map<String, String> row : data) {
            // Extract features
            double[] features = new double[supervised ? n - 1 : n];
            for (int i = 0; i < features.length; i++) {
                features[i] = parseDouble(row.get(keys.get(i)));
            }
            X.add(features);

            // Extract label (if supervised)
            if (supervised) {
                double[] label = new double[1];
                label[0] = parseDouble(row.get(keys.get(n - 1)));
                Y.add(label);
            }
        }

        result.put("X", X);
        result.put("Y", Y);
        return result;
    }

    private static double parseDouble(String s) {
        if (s == null || s.isEmpty()) return 0.0;
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    // === 4️⃣ Train-Test Split ===
    public static Map<String, List<double[]>> train_test_split(
            List<double[]> X, List<double[]> Y, double test_size) {

        Map<String, List<double[]>> split = new HashMap<>();

        if (X == null || X.isEmpty()) {
            split.put("X_train", new ArrayList<>());
            split.put("X_test", new ArrayList<>());
            split.put("Y_train", new ArrayList<>());
            split.put("Y_test", new ArrayList<>());
            return split;
        }

        int total = X.size();
        int testCount = (int) Math.max(1, total * test_size);

        List<Integer> idx = new ArrayList<>();
        for (int i = 0; i < total; i++) idx.add(i);
        Collections.shuffle(idx, new Random());

        List<double[]> X_train = new ArrayList<>(), X_test = new ArrayList<>();
        List<double[]> Y_train = new ArrayList<>(), Y_test = new ArrayList<>();

        for (int i = 0; i < total; i++) {
            int id = idx.get(i);
            if (i < testCount) {
                X_test.add(X.get(id));
                if (Y != null && !Y.isEmpty()) Y_test.add(Y.get(id));
            } else {
                X_train.add(X.get(id));
                if (Y != null && !Y.isEmpty()) Y_train.add(Y.get(id));
            }
        }

        split.put("X_train", X_train);
        split.put("X_test", X_test);
        split.put("Y_train", Y_train);
        split.put("Y_test", Y_test);
        return split;
    }

    // === 5️⃣ Save CSV ===
    public static void to_csv(List<Map<String, String>> data, String filePath) {
        if (data == null || data.isEmpty()) return;

        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            Set<String> keys = data.get(0).keySet();
            pw.println(String.join(",", keys));

            for (Map<String, String> row : data) {
                List<String> vals = new ArrayList<>();
                for (String k : keys) vals.add(row.getOrDefault(k, ""));
                pw.println(String.join(",", vals));
            }
        } catch (IOException e) {
            System.out.println("❌ Error writing CSV: " + e.getMessage());
        }
    }
}
