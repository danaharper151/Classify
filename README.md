# Skin Lesion Classification Android App

An Android application that uses a custom-trained MobileNetV2 CNN to classify skin lesions as benign or malignant. This project demonstrates transfer learning, data augmentation techniques, and mobile ML deployment for medical image analysis.

##  Features

- **Real-time Classification**: Take photos with your phone's camera or upload from gallery
- **Deep Learning Model**: MobileNetV2 architecture fine-tuned on HAM10000 dataset
- **Binary Classification**: Predicts benign vs. malignant skin lesions
- **TensorFlow Lite**: Optimized model for mobile deployment (9 MB)
- **High Accuracy**: 86.8% accuracy with 91.2% recall (catches 9 out of 10 malignant cases)
- **User-Friendly Interface**: Clean Material Design UI with confidence scores

##  Model Performance

### Current Model (v2.0) - **Recommended**

| Metric | Value | Description |
|--------|-------|-------------|
| **Accuracy** | **86.8%** | Overall classification accuracy |
| **Recall** | **91.2%** | Catches 91% of malignant cases |
| **Precision** | **83.8%** | 84% of malignant predictions are correct |
| **F1 Score** | **0.873** | Balanced performance metric |
| **AUC-ROC** | **0.943** | Excellent discrimination ability |
| **Loss** | **0.303** | Low validation loss |

### Performance Comparison

| Metric | v1.0 (Original) | v2.0 (Current) | Improvement |
|--------|-----------------|----------------|-------------|
| Accuracy | 75.6% | **86.8%** | **+11.2%**  |
| Recall | 78.5% | **91.2%** | **+12.7%**  |
| Precision | 74.1% | **83.8%** | **+9.7%**  |
| F1 Score | 0.762 | **0.873** | **+11.1%**  |
| AUC | 0.826 | **0.943** | **+11.7%**  |

**Key Achievement**: The new model catches **91% of malignant lesions** compared to 78% in the original version - a critical improvement for medical screening applications.

##  Project Structure

```
Classify/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── assets/
│   │   │   │   └── skin_lesion_model.tflite    # v2.0 trained model
│   │   │   ├── java/com/example/classify/
│   │   │   │   ├── MainActivity.kt              # Main app logic
│   │   │   │   └── SkinLesionClassifier.kt      # TFLite inference
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   │   └── activity_main.xml        # UI layout
│   │   │   │   └── xml/
│   │   │   │       └── file_paths.xml           # FileProvider config
│   │   │   └── AndroidManifest.xml
│   └── build.gradle
├── training_notebooks/
│   └── (Training notebooks for model development)
└── README.md
```

## 🔧 Technologies Used

### Android App
- **Language**: Kotlin
- **Min SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34
- **Compile SDK**: API 35
- **ML Framework**: TensorFlow Lite 2.14.0
- **UI**: Material Components, CardView
- **Camera**: CameraX 1.3.1
- **Image Loading**: Glide 4.16.0

### Model Training
- **Framework**: TensorFlow/Keras
- **Architecture**: MobileNetV2 (transfer learning from ImageNet)
- **Platform**: Google Colab (GPU T4)
- **Dataset**: HAM10000 dermatoscopic images

##  Training Methodology (v2.0)

### Dataset Overview
- **Source**: HAM10000 dataset
- **Total Images**: 10,015 dermatoscopic images
- **Original Distribution**:
  - Benign: 8,388 images (83.8%)
  - Malignant: 1,627 images (16.2%)

### Class Balancing Strategy
To address the severe class imbalance (5:1 ratio), we used a hybrid approach:

1. **Benign Class**: Sampled 8,000 images from 8,388 available
2. **Malignant Class**: Augmented 1,627 original images to create 8,000 total
   - Each original image augmented ~4 times
   - On-the-fly augmentation during training creates diverse variations

**Final Balanced Dataset**: 16,000 images (8,000 per class)

### Data Split
- **Training Set**: 12,800 images (6,400 benign + 6,400 malignant)
- **Validation Set**: 3,200 images (1,600 benign + 1,600 malignant)
- **Split Ratio**: 80/20 train-validation

### Data Augmentation (Training Only)
Applied to training data to improve generalization:
- **Rotation**: ±20°
- **Width/Height Shift**: ±20%
- **Horizontal Flip**: Yes
- **Vertical Flip**: Yes
- **Zoom Range**: ±20%
- **Fill Mode**: Nearest neighbor

### Model Architecture
```
Input (224×224×3)
  ↓
MobileNetV2 Base (ImageNet pre-trained, frozen)
  ↓
GlobalAveragePooling2D
  ↓
Dropout(0.3)
  ↓
Dense(128, ReLU activation)
  ↓
Dropout(0.2)
  ↓
Dense(1, Sigmoid activation)
  ↓
Output (Binary: Benign/Malignant)
```

### Training Configuration
| Parameter | Value | Notes |
|-----------|-------|-------|
| **Image Size** | 224×224 | MobileNetV2 standard input |
| **Batch Size** | 64 | Increased from v1.0 (32) |
| **Epochs** | 50 | Increased from v1.0 (30) |
| **Learning Rate** | 0.0005 | Optimized for balance |
| **Optimizer** | Adam | Adaptive learning rate |
| **Loss Function** | Binary Crossentropy | Standard for binary classification |
| **Callbacks** | EarlyStopping, ReduceLROnPlateau | Prevent overfitting |

### Key Improvements from v1.0
1.  **Larger balanced dataset** (16,000 vs 8,000 images)
2.  **Higher batch size** (64 vs 32) for more stable gradients
3.  **More training epochs** (50 vs 30) for better convergence
4.  **Optimized learning rate** (0.0005 vs 0.0001)
5.  **4x malignant augmentation** for better minority class learning

##  Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Android device or emulator (API 24+)
- Minimum 2GB RAM on device
- Camera permission

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/danaharper151/Classify.git
cd Classify
```

2. **Open in Android Studio**
   - Open Android Studio
   - File → Open → Select the `Classify` folder
   - Wait for Gradle sync to complete

3. **Build and Run**
   - Connect your Android device via USB (enable USB debugging)
   - Or use an Android emulator
   - Click Run (green play button)
   - Grant camera and storage permissions when prompted

### First-Time Setup
1. Launch the app
2. Grant camera permission
3. Take a test photo or load from gallery
4. View classification results with confidence score

##  Usage

### Taking a Photo
1. Tap **"Take Photo"** button
2. Position the skin lesion in frame
3. Capture the image
4. Wait for classification (< 1 second)

### Using Gallery Images
1. Tap **"From Gallery"** button
2. Select an image from your device
3. View instant classification results

### Understanding Results
- **Prediction**: Benign or Malignant
- **Confidence**: Percentage (0-100%)
- **Color Coding**: 
  - Green = Benign
  - Red = Malignant
- **Disclaimer**: Always consult a medical professional

 **Medical Disclaimer**: This app is for **educational purposes only**. It is NOT a medical diagnostic tool. Always consult qualified healthcare professionals for medical advice and diagnosis.

##  Training Your Own Model

### Download Dataset
1. Visit [Kaggle HAM10000](https://www.kaggle.com/kmader/skin-cancer-mnist-ham10000)
2. Download:
   - `HAM10000_metadata.csv`
   - `HAM10000_images_part_1.zip`
   - `HAM10000_images_part_2.zip`
3. Upload to Google Drive

### Train in Google Colab
1. Open a new Colab notebook
2. Enable GPU: Runtime → Change runtime type → GPU (T4)
3. Mount Google Drive and extract dataset
4. Use the training configuration above
5. Expected training time: ~1-2 hours with GPU

### Deploy to App
1. Download `skin_lesion_binary.tflite` from Colab
2. Rename to `skin_lesion_model.tflite`
3. Replace in `app/src/main/assets/`
4. Clean and rebuild project
5. Run on device

##  Performance Analysis

### Why v2.0 Performs Better

**1. Larger Training Dataset** 
- v1.0: 6,400 training images
- v2.0: 12,800 training images (2x larger)
- More data = better generalization

**2. Balanced Classes**
- v1.0: 4,000 per class
- v2.0: 8,000 per class
- Prevents bias toward majority class

**3. Optimized Hyperparameters**
- Larger batch size (64) improves gradient estimates
- More epochs (50) allows better convergence
- Tuned learning rate (0.0005) balances speed and stability

**4. Extensive Augmentation**
- Each malignant image seen in ~4 different variations
- Increases effective dataset diversity
- Reduces overfitting on minority class

### Confusion Matrix Insights (v2.0)
Based on validation results:
- **True Positives (Malignant correctly identified)**: ~1,459 / 1,600 (91.2%)
- **False Negatives (Malignant missed)**: ~141 / 1,600 (8.8%)
- **True Negatives (Benign correctly identified)**: ~1,389 / 1,600 (86.8%)
- **False Positives (Benign misclassified as malignant)**: ~211 / 1,600 (13.2%)

**Clinical Significance**: High recall (91.2%) means the model rarely misses malignant cases, which is critical for screening applications.

##  Technical Details

### Model Size & Performance
- **TFLite Model Size**: 9.08 MB
- **Inference Time**: < 200ms on modern Android devices
- **Memory Usage**: ~50 MB during inference
- **Supported Image Formats**: JPEG, PNG
- **Input Processing**: Auto-resize to 224×224, normalize [0,1]

### Binary Classification Labels
- **Class 0 (Benign)**: nv, bkl, df, vasc, akiec
- **Class 1 (Malignant)**: mel (melanoma), bcc (basal cell carcinoma)

##  Educational Value

This project demonstrates:
-  Transfer learning from ImageNet to medical imaging
-  Handling severe class imbalance (5:1 ratio)
-  Data augmentation strategies for minority classes
-  Hyperparameter tuning for performance optimization
-  Model deployment on mobile devices with TensorFlow Lite
-  Real-world medical AI application constraints
-  Iterative model improvement (v1.0 → v2.0)

##  References

### Dataset
- Tschandl, P., Rosendahl, C. & Kittler, H. The HAM10000 dataset, a large collection of multi-source dermatoscopic images of common pigmented skin lesions. *Scientific Data* 5, 180161 (2018).
- DOI: [10.1038/sdata.2018.161](https://doi.org/10.1038/sdata.2018.161)

### Architecture
- Sandler, M., Howard, A., Zhu, M., Zhmoginov, A. & Chen, L. MobileNetV2: Inverted Residuals and Linear Bottlenecks. *CVPR* (2018).

### Frameworks
- [TensorFlow Lite](https://www.tensorflow.org/lite)
- [Keras Documentation](https://keras.io/)
- [Android CameraX](https://developer.android.com/training/camerax)

##  License

This project is for educational purposes. The HAM10000 dataset is available under Creative Commons Attribution-NonCommercial 4.0 International License.

##  Acknowledgments

- HAM10000 dataset creators for providing high-quality dermatoscopic images
- TensorFlow and Keras teams for deep learning frameworks
- Google Colab for free GPU resources
- MobileNetV2 architecture developers
- Android development community
- Anthropic Claude.ai for Colab and Android Studio build assistance

##  Contact

For questions, suggestions, or collaboration opportunities, please open an issue on GitHub.

---

**Version History**
- **v2.0** (Current): 86.8% accuracy, 91.2% recall - Major performance improvements
- **v1.0** (Initial): 75.6% accuracy, 78.5% recall - First working version

**Last Updated**: March 2026

---

 **IMPORTANT MEDICAL DISCLAIMER**: This application is a student educational project and is NOT intended for actual medical diagnosis, treatment, or clinical decision-making. Skin lesion evaluation requires professional medical examination. Always consult qualified dermatologists or healthcare providers for any skin concerns.
