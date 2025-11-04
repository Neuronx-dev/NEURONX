
---

# 🧠 NEURONX — DBSCAN Clustering (Unsupervised Learning)

## 📘 Overview

This module demonstrates **DBSCAN (Density-Based Spatial Clustering of Applications with Noise)** — a **core unsupervised learning algorithm** that groups data points based on **density** rather than distance or centroids.

Unlike **K-Means**, which needs the number of clusters (`k`) in advance, **DBSCAN**:

* Automatically detects the number of clusters.
* Works with **arbitrary-shaped** clusters.
* Identifies **outliers (noise)** points.
* Is robust to small data fluctuations.

---

## 🧩 Algorithm Explanation

| Parameter      | Description                                                    |
| -------------- | -------------------------------------------------------------- |
| `eps`          | Maximum distance between two points to be considered neighbors |
| `minPts`       | Minimum points required to form a dense region (cluster)       |
| `core point`   | Has ≥ `minPts` neighbors                                       |
| `border point` | Lies within a cluster but has < `minPts` neighbors             |
| `noise`        | Doesn’t belong to any cluster                                  |

### Step-by-Step Flow

1. **Load dataset** using `FileUtils.read_csv(path)`
2. **Extract numeric features** from data
3. **Split** into training and testing sets
4. **Initialize** DBSCAN with `(eps, minPts)`
5. **Fit model** → assign cluster labels
6. **Predict unseen samples** using distance rules
7. **Output clusters and noise points**

---

## 🧮 Code Explanation

### 🔹 `DBSCAN.java`

Implements:

* Euclidean distance calculation
* Core point detection
* Neighborhood search (`eps` radius)
* Recursive cluster expansion
* Label assignment (`clusterID`, `-1` for noise)

### 🔹 `FileUtils.java`

Handles:

* Reading CSV datasets
* Extracting feature arrays
* Splitting data (train/test)
* Lightweight utility similar to `pandas + sklearn`

### 🔹 `TestDBSCAN.java`

Performs:

* Data loading
* Model training
* Cluster visualization
* Sample prediction (multiple test points)

---

## 🧠 Full Runnable Code (Test File)

```java
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

        DBSCAN dbscan = new DBSCAN(1.5, 3);
        dbscan.fit(split.get("X_train"));

        int[] labels = dbscan.getLabels();
        System.out.println("\n📊 Cluster Labels:");
        for (int i = 0; i < labels.length; i++) {
            System.out.printf("Point %d: %s → Cluster %d%n",
                    i + 1,
                    Arrays.toString(split.get("X_train").get(i)),
                    labels[i]);
        }

        System.out.println("\n🔍 Predicting New Samples:");
        double[][] samples = {
            {8.0, 8.2},
            {2.1, 2.0},
            {9.9, 2.2},
            {7.2, 7.0},
            {10.1, 2.3},
            {1.9, 2.3}
        };

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
```

---


## 📁 Dataset Info

**File:** `dataset_unsupervised.csv`
**Size:** 150 samples
**Format:** 2D numerical (x₁, x₂)

| x1   | x2   |
| ---- | ---- |
| 2.1  | 2.3  |
| 8.2  | 8.1  |
| 10.0 | 2.1  |
| 25.0 | 80.0 |

**Tip:** You can generate your own dataset using Python or manually edit this CSV for testing different cluster structures.

---

## 🧩 Advantages of DBSCAN

* ✅ No need for predefined cluster count (`k`)
* ✅ Detects **arbitrary shape** clusters (not just circular)
* ✅ Identifies **outliers/noise**
* ✅ Works well for **spatial/geographical** data
* ✅ Stable against minor data changes

---

## 🧭 Real-World Use Cases

| Domain                 | Application                                      |
| ---------------------- | ------------------------------------------------ |
| 🚗 Autonomous Vehicles | Grouping nearby objects or LiDAR points          |
| 📍 GIS / Maps          | Detecting regions of high activity or hotspots   |
| 💬 NLP                 | Topic clustering of text embeddings              |
| 🧬 Bioinformatics      | Grouping similar gene expression patterns        |
| 📊 Retail              | Customer segmentation based on spending behavior |

---

## 🧾 Summary

| Component        | Description                       |
| ---------------- | --------------------------------- |
| Algorithm        | DBSCAN (Density-Based Clustering) |
| Type             | Unsupervised Learning             |
| Dataset          | Synthetic 2D Data                 |
| Output           | Cluster Labels + Noise            |
| Language         | Java                              |
| Library          | Custom-built (no ML dependencies) |
| Predict Function | Supported                         |

---

## 💡 Developer Notes

* You can **tune** `eps` and `minPts` to control clustering sensitivity.
* Noise points are labeled as **`-1`**.
* You can **extend** this to higher-dimensional datasets.
* Works seamlessly with other **NEURONX modules** (Regression, Classification, etc.).

---


