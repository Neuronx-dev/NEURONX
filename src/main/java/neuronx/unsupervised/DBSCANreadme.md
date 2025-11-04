

## 🧠 DBSCAN Clustering in Java

A simple implementation of DBSCAN (Density-Based Spatial Clustering of Applications with Noise) for unsupervised clustering using Java.
This example mimics scikit-learn’s structure and works with any numeric dataset.

---

## 📁 Folder Structure
---
neuronx/
├── unsupervised/
│   └── clustering/
│       └── DBSCAN.java
└── utils/
└── FileUtils.java
test/
└── TestDBSCANWithCSV.java
dataset_unsupervised.csv
---

---

## ⚙️ 1️⃣ Class — DBSCAN.java

---

A minimal and reusable unsupervised clustering algorithm based on data density.

🔍 Key Features

Works for any numeric dataset

Automatically detects dense clusters

Labels isolated points as noise (-1)

Does not require number of clusters in advance

Includes a helper predict() for unseen samples

---


## 📄 Example (Inside DBSCAN.java)

```java

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
````
---

**🟢 Output Example**
---

---

## 🏁 DBSCAN finished. Total clusters found: 2

Cluster labels:
Point 1: [1.0, 2.0] -> Cluster 1
Point 2: [1.2, 2.1] -> Cluster 1
Point 3: [0.8, 1.9] -> Cluster 1
Point 4: [8.0, 8.0] -> Cluster 2
Point 5: [8.1, 8.2] -> Cluster 2
Point 6: [25.0, 80.0] -> Cluster -1

Predicted cluster for [8.05, 8.1]: 2

### 📊 2️⃣ Test Class — TestDBSCANWithCSV.java

This example demonstrates how to cluster data from a CSV file.
```java

package test;

import neuronx.utils.FileUtils;
import neuronx.unsupervised.clustering.DBSCAN;
import java.util.*;

public class TestDBSCANWithCSV {
    public static void main(String[] args) {

        // 1️⃣ Load CSV dataset
        String path = "dataset_unsupervised.csv";
        List<String[]> data = FileUtils.loadCSV(path);

        // Extract only feature columns (no labels needed)
        Map<String, List<double[]>> xy = FileUtils.extractXY(data, false);
        List<double[]> X = xy.get("X");

        // 2️⃣ Create and train DBSCAN model
        DBSCAN dbscan = new DBSCAN(0.5, 3);
        dbscan.fit(X);

        // 3️⃣ Display cluster labels
        int[] labels = dbscan.getLabels();
        for (int i = 0; i < labels.length; i++) {
            System.out.printf("Point %d: %s -> Cluster %d%n", i + 1, Arrays.toString(X.get(i)), labels[i]);
        }

        // 4️⃣ Predict cluster for new unseen point
        double[] newPoint = {8.05, 8.1};
        int cluster = dbscan.predict(newPoint);
        System.out.println("\nPredicted cluster for " + Arrays.toString(newPoint) + ": " + cluster);
    }
}
````

---


## 🧩 Output Example:
---

🏁 DBSCAN finished. Total clusters found: 2
Point 1: [1.0, 2.0] -> Cluster 1
Point 2: [1.2, 2.1] -> Cluster 1
Point 3: [0.8, 1.9] -> Cluster 1
Point 4: [8.0, 8.0] -> Cluster 2
Point 5: [8.1, 8.2] -> Cluster 2
Point 6: [25.0, 80.0] -> Cluster -1

Predicted cluster for [8.05, 8.1]: 2
---

---

## 📂 3️⃣ CSV File — dataset_unsupervised.csv

Example dataset (no labels, only features):

1.0,2.0
1.2,2.1
0.8,1.9
8.0,8.0
8.1,8.2
25.0,80.0


➡ Each row = data point (features)
➡ No label column is required for DBSCAN.
---

---

## 🪶 4️⃣ Utility Class — FileUtils.java

(Already implemented — just import and use)
It handles:

CSV loading (loadCSV(path))

Extracting feature arrays (extractXY(data, false))
---

---

### 🚀 Run (Using Maven)
🧩 1️⃣ Compile
mvn clean compile

Cleans previous builds and compiles all .java files.

---

---

### 🧪 2️⃣ Run Test File

Your test file is under src/test/java/test/.
Run it using the Maven Exec plugin:

mvn exec:java -Dexec.mainClass="test.TestDBSCANWithCSV"

---

---

### ⚙️ 3️⃣ (Optional) Configure Exec Plugin in pom.xml

If not already configured, add this block inside <build>:

<build>
  <plugins>
    <plugin>
      <groupId>org.codehaus.mojo</groupId>
      <artifactId>exec-maven-plugin</artifactId>
      <version>3.1.0</version>
      <configuration>
        <mainClass>test.TestDBSCANWithCSV</mainClass>
      </configuration>
    </plugin>
  </plugins>
</build>
---

---

## Then simply run:

mvn exec:java
---

---

## 💬 Output Summary
Step	Description
1️⃣	Load dataset from CSV
2️⃣	Train DBSCAN model with eps, minPts
3️⃣	Identify clusters and noise points
✅	Predict cluster for new unseen data
---

---
### 🧮 Parameter Meaning

Parameter	Description
eps	Maximum distance between two points to be considered neighbors
minPts	Minimum number of points to form a dense region
Cluster = -1	Noise (outlier) points
Cluster = 1, 2, ...	Detected dense clusters
---

---

### 🧠 Optional Visualization Idea

To visualize clusters, export results (points + labels) to a CSV and plot them in Python using Matplotlib or Seaborn.
---