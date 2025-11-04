## 📈 Simple Linear Regression in Java  

**A clean and reusable implementation of Simple Linear Regression (SLR)** for supervised regression tasks using Java.  
This project mimics the workflow of `scikit-learn` with `fit`, `predict`, and `score` methods.  

---

## 📁 Folder Structure  

```
neuronx/
├── supervised/
│   └── regression/
│       └── SimpleLinearRegression.java
dataset_SLR.csv
```

---

## ⚙️ 1️⃣ Class — `SimpleLinearRegression.java`  

A self-contained **Linear Regression model class** that fits a straight line through data points.  

### 🔍 Key Features  

* Train using `fit(X_train, y_train)`  
* Predict using `predict(X_test)` or single `predict(x)`  
* Evaluate using:
  * **Mean Squared Error (MSE)**
  * **R² Score**
* Supports CSV loading  
* Automatic **train-test split (default 80–20)**  
* Optional shuffling for reproducibility  

---

### 📄 Example (Inside `SimpleLinearRegression.java`)  

```java
String filePath = "dataset_SLR.csv";
double[][] data = loadCSV(filePath);

// Shuffle data for randomness
List<double[]> list = Arrays.asList(data);
Collections.shuffle(list, new Random(42));
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

SimpleLinearRegression slr = new SimpleLinearRegression();
slr.fit(X_train, y_train);
double[] predictions = slr.predict(X_test);

double mse = slr.meanSquaredError(y_test, predictions);
double r2 = slr.score(y_test, predictions);

System.out.printf("MSE: %.4f%n", mse);
System.out.printf("R² Score: %.4f%n", r2);
```

---

🟢 **Output Example**

```
✅ Model trained successfully (slope=1.50, intercept=2.30)

--- Test Predictions ---
X=4.00 → Predicted=8.35 | Actual=8.50
X=7.00 → Predicted=12.80 | Actual=12.70
X=9.00 → Predicted=15.60 | Actual=15.50

MSE: 0.0125
R² Score: 0.9923
```

---

## 📊 2️⃣ Dataset — `dataset_SLR.csv`  

Example (two columns — X, Y):  

```
X,Y
1,2.3
2,3.1
3,5.0
4,7.2
5,8.4
6,9.8
7,11.5
8,13.2
9,15.1
10,16.4
```

➡ First column = **Independent variable (X)**  
➡ Second column = **Dependent variable (Y)**  

---

## 🚀 Run (Using Maven or Terminal)  

### 🧩 1️⃣ Compile  

```bash
javac neuronx/supervised/regression/SimpleLinearRegression.java
```

### 🧪 2️⃣ Run  

```bash
java neuronx.supervised.regression.SimpleLinearRegression
```

---

## 🧮 3️⃣ Evaluation Metrics  

| Metric | Description | Formula |
|--------|--------------|----------|
| **MSE** | Mean Squared Error |  (1/n) Σ(y - ŷ)² |
| **R² Score** | Coefficient of Determination |  1 - (SS_res / SS_tot) |

---

## ⚙️ 4️⃣ Customization  

| Parameter | Description | Default |
|------------|--------------|----------|
| `splitIndex` | Train–test ratio | 80–20 |
| `shuffle` | Randomize data order | ✅ Enabled |
| `Random(42)` | Random seed | Reproducible |

Change ratio easily:  
```java
int splitIndex = (int) (data.length * 0.9);  // 90–10 split
```

---

## 💬 Output Summary  

| Step | Description |
|------|-------------|
| 1️⃣  | Load CSV dataset |
| 2️⃣  | Split into train & test |
| 3️⃣  | Train Linear Regression model |
| 4️⃣  | Predict unseen test data |
| ✅    | Display MSE and R² metrics |
