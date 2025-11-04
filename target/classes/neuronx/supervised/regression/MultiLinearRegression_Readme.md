## 📘 MultiLinearRegression – Java Implementation

### 📖 Overview
This project implements a **Multiple Linear Regression (MLR)** model from scratch in **Java**, using the **Ordinary Least Squares (OLS)** method.  
It loads a dataset from a CSV file, trains the regression model, makes predictions, and evaluates model performance using **Mean Squared Error (MSE)** and **R² Score**.

---

### 🧠 What is Multiple Linear Regression?
**Multiple Linear Regression (MLR)** is a statistical technique used to predict the value of a dependent variable (Y) based on multiple independent variables (X₁, X₂, X₃, ...).

The equation is:  
\[
Y = β₀ + β₁X₁ + β₂X₂ + ... + βₙXₙ
\]
where:  
- **β₀** = Intercept (bias)  
- **β₁...βₙ** = Coefficients for each feature  

---

### 📂 File Structure
```
NEURONX/
 ├── dataset_multiLinerRegression.csv
 ├── src/
 │    └── main/
 │         └── java/
 │              └── neuronx/
 │                   └── supervised/
 │                        └── regression/
 │                             └── MultiLinearRegression.java
 └── MultiLinearRegression_Readme.md
```

---

### 📄 CSV File Format

Each row should contain your **input features** and the **target output** (last column).

**Example (`dataset_multiLinerRegression.csv`):**
```csv
feature1,feature2,feature3,target
1.2,3.4,2.1,45.3
2.0,4.1,3.2,50.1
3.1,5.6,4.0,61.4
4.5,6.3,5.2,72.8
```

➡️ The **last column** is always the target/output value.

---

### ⚙️ How the Code Works

1. **Load CSV File**  
   The program reads the dataset using built-in Java I/O.  
   - Features (X) are extracted from all columns except the last.  
   - Labels (y) are extracted from the last column.

2. **Split Dataset**  
   The data is split into **80% training** and **20% testing** subsets.

3. **Train the Model**  
   Model parameters are computed using the **Normal Equation**:  
   \[
   β = (XᵀX)^{-1} Xᵀy
   \]

4. **Predict**  
   Predictions are generated for the test dataset.

5. **Evaluate**  
   The model prints:  
   - ✅ Trained Weights  
   - 🔢 Predictions (Predicted vs Actual)  
   - 📉 Mean Squared Error (MSE)  
   - 📈 R² Score  

---

### 🧪 Sample Output
```
✅ Model trained with 3 features
Weights: [2.15, 3.56, 4.02, 1.23]

--- Predictions ---
Predicted: 72.45 | Actual: 70.20
Predicted: 81.33 | Actual: 82.10
Predicted: 90.12 | Actual: 89.75

MSE: 2.4135
R² Score: 0.9724
```

---

### ▶️ How to Run the Code

1. **Open VS Code / Terminal**
2. Navigate to your Java source folder:
   ```bash
   cd C:\Users\DELL\Desktop\NEURONX\src\main\java
   ```
3. **Compile the Java file:**
   ```bash
   javac neuronx\supervised\regression\MultiLinearRegression.java
   ```
4. **Run the program:**
   ```bash
   java neuronx.supervised.regression.MultiLinearRegression
   ```

---

### 📊 Evaluation Metrics

| Metric | Formula | Description |
|---------|----------|-------------|
| **MSE** | (1/n)Σ(y - ŷ)² | Measures average squared difference between predicted and actual values |
| **R² Score** | 1 - (SS_res / SS_tot) | Indicates how well the model fits the data (closer to 1 = better) |

---

### 🧩 Future Enhancements
- Implement **Gradient Descent** optimization.  
- Add **data normalization** for better scaling.  
- Include **feature selection** and **cross-validation**.

---

### 👩‍💻 Author
**Kantineni Gayathri**  
📍 Project: NEURONX – Supervised Learning Models  
📧 Contact: [your email here if you want]
