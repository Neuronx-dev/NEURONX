
## 🧠 KNN Classifier in Java

**A simple implementation of K-Nearest Neighbors (KNN)** for supervised classification using Java.  
This example mimics `scikit-learn`’s structure and works with any numeric dataset (like Iris).

---

## 📁 Folder Structure

```

nuronx/
├── supervised/
│   └── classification/
│       └── KNNClassifier.java
└── utils/
└── FileUtils.java
test/
└── TestKNNWithCSV.java
dataset_supervised.csv

````

---

## ⚙️ 1️⃣ Class — `KNNClassifier.java`

A minimal and reusable **KNN model class**.

### 🔍 Key Features

* Trains using `fit(X_train, Y_train)`
* Predicts for new samples using `predict(X_test)`
* Automatically ensures `k` is an odd number
* Supports both single and batch predictions

---

### 📄 Example (Inside `KNNClassifier.java`)

```java
List<double[]> X_train = Arrays.asList(
    new double[]{1.0, 2.0},
    new double[]{2.0, 3.0},
    new double[]{3.0, 3.0},
    new double[]{6.0, 5.0}
);
List<String> y_train = Arrays.asList("A", "A", "B", "B");

KNNClassifier knn = new KNNClassifier(3);
knn.fit(X_train, y_train);

double[] testPoint = {2.5, 2.7};
String result = knn.predict(testPoint);

System.out.println("Predicted class: " + result);
````

🟢 **Output Example**

```
✅ Model trained with 4 samples. (k=3)
Predicted class: A
```

---

## 📊 2️⃣ Test Class — `TestKNNWithCSV.java`

This example demonstrates **how to train and predict** using data from a CSV file.

```java
package test;

import nuronx.utils.FileUtils;
import nuronx.supervised.classification.KNNClassifier;
import java.util.*;

public class TestKNNWithCSV {
    public static void main(String[] args) {

        // 1️⃣ Load CSV dataset
        String path = "dataset_supervised.csv";
        List<String[]> data = FileUtils.loadCSV(path);
        Map<String, List<double[]>> xy = FileUtils.extractXY(data, true);

        List<double[]> X = xy.get("X");
        List<double[]> Y_array = xy.get("Y");

        // Convert Y (double[]) → String labels
        List<String> Y = new ArrayList<>();
        for (double[] arr : Y_array) Y.add(String.valueOf((int) arr[0]));

        // 2️⃣ Train simple KNN model
        KNNClassifier knn = new KNNClassifier(3);
        knn.fit(X, Y);

        // 3️⃣ Predict new unseen samples
        double[][] newSamples = {
            {5.1, 3.5, 1.4, 0.2},
            {6.3, 2.9, 5.6, 1.8}
        };

        for (double[] sample : newSamples) {
            String pred = knn.predict(sample);
            System.out.println("➡ Features " + Arrays.toString(sample) + " → Predicted: " + pred);
        }
    }
}
```

🧩 **Output Example:**

```
✅ Model trained with 150 samples. (k=3)
➡ Features [5.1, 3.5, 1.4, 0.2] → Predicted: 0
➡ Features [6.3, 2.9, 5.6, 1.8] → Predicted: 1
```

---

## 📂 3️⃣ CSV File — `dataset_supervised.csv`

Example (similar to Iris dataset):

```
5.1,3.5,1.4,0.2,0
6.3,2.9,5.6,1.8,1
5.8,2.7,4.1,1.0,1
4.7,3.2,1.3,0.2,0
```

➡ Last column = **label (target)**
➡ First columns = **features**

---

## 🪶 4️⃣ Utility Class — `FileUtils.java`

*(Already implemented — just import and use)*
It handles:

* CSV loading (`loadCSV(path)`)
* Extracting X and Y (`extractXY(data, supervised)`)

---

## 🚀 Run (Using Maven)

### 🧩 1️⃣ Compile

```bash
mvn clean compile
```

> Cleans old builds and compiles all `.java` files inside `src/main/java`.

---

### 🧪 2️⃣ Run Test File

Your `TestKNNWithCSV.java` is under `src/test/java/test/`.
Run it using Maven’s Exec plugin:

```bash
mvn exec:java -Dexec.mainClass="test.TestKNNWithCSV"
```

> Runs the main class `test.TestKNNWithCSV` through Maven.

---

### ⚙️ 3️⃣ (Optional) Configure Exec Plugin in `pom.xml`

If not configured, add this inside `<build>`:

```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.codehaus.mojo</groupId>
      <artifactId>exec-maven-plugin</artifactId>
      <version>3.1.0</version>
      <configuration>
        <mainClass>test.TestKNNWithCSV</mainClass>
      </configuration>
    </plugin>
  </plugins>
</build>
```

Then simply run:

```bash
mvn exec:java
```

---

## 💬 Output Summary

| Step | Description                          |
| ---- | ------------------------------------ |
| 1️⃣  | Load dataset from CSV                |
| 2️⃣  | Train KNN model with `k=3`           |
| 3️⃣  | Predict for new unseen samples       |
| ✅    | Output predicted labels (0,1,2 etc.) |

---

## 🧠 Optional Label Meaning (if Iris dataset)

| Label | Class Name      |
| ----- | --------------- |
| 0     | Iris-setosa     |
| 1     | Iris-versicolor |
| 2     | Iris-virginica  |


