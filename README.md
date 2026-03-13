# Skin Lesion Classifier

## Android application that uses a MobileNetV2 model trained on the HAM10000 dataset for binary classification of skin lesions

Take a photo of a skin lesion with your cell phone and the CNN will classify the lesion as benign or malignant with a confidence score.

### ***This skin lesion classifier is for software development purposes only and is not for medical use.***

### ***Consult a medical professional for accurate diagnosis of any medical condition.***

## Features

* Take a photo of a skin lesion with your cell phone or upload an image from your gallery
* Pre-trained MobileNetV2 model classifies skin lesions as malignant or benign
* Results display with confidence scores
* **Current version (v2.0) achieves 86.8% accuracy with 91.2% recall**

## Model Performance

### Version 2.0 (Current) - Improved Model

**Training Configuration:**
- Image Size: 224x224
- Batch Size: 64
- Epochs: 50
- Learning Rate: 0.0005

**Dataset:**
- Original HAM10000: 10,015 total images (8,388 benign, 1,627 malignant)
- **Balanced via augmentation:** 8,000 benign + 8,000 augmented malignant = 16,000 total
- Training samples: 12,800 (6,400 benign + 6,400 malignant)
- Validation samples: 3,200 (1,600 benign + 1,600 malignant)

**Validation Results:**
| Metric | Score |
|--------|-------|
| **Accuracy** | **86.78%** |
| **Recall** | **91.19%** |
| **Precision** | 83.80% |
| **AUC** | 0.9426 |
| **F1 Score** | 0.8734 |
| Loss | 0.3030 |

**Key Improvements:**
-  +11.2% accuracy improvement (75.6% → 86.8%)
-  +13.4% recall improvement (78.5% → 91.2%)
-  Catches 91 out of 100 malignant cases
-  Trained on 5x more data (3,254 → 16,000 images)

---

### Version 1.0 - Initial Model

**Dataset:**
- Benign cases: 1,627 (50.0%)
- Malignant cases: 1,627 (50.0%)
- Total balanced dataset: 3,254

**Data Split:**
- Training samples: 2,603
- Validation samples: 651

**Validation Results:**
| Metric | Score |
|--------|-------|
| Accuracy | 75.58% |
| Recall | 78.46% |
| Precision | 74.13% |
| AUC | 0.8260 |
| F1 Score | 0.7623 |
| Loss | 0.5127 |

---

## Model Comparison

| Metric | v1.0 | v2.0 | Improvement |
|--------|------|------|-------------|
| **Accuracy** | 75.6% | **86.8%** | **+11.2%** |
| **Recall** | 78.5% | **91.2%** | **+13.4%** |
| **Precision** | 74.1% | **83.8%** | **+9.7%** |
| **F1 Score** | 0.762 | **0.873** | **+14.6%** |
| **Training Data** | 3,254 | **16,000** | **+391%** |

**Clinical Significance:** Version 2.0 successfully identifies 91 out of 100 malignant lesions compared to 78 out of 100 in v1.0, significantly reducing false negatives in a screening context.

## Screenshots

![App Interface](https://private-user-images.githubusercontent.com/143127911/550609815-72a00b51-df3a-4b66-8b8b-5522f74b2bf1.png?jwt=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NzI1ODEyODYsIm5iZiI6MTc3MjU4MDk4NiwicGF0aCI6Ii8xNDMxMjc5MTEvNTUwNjA5ODE1LTcyYTAwYjUxLWRmM2EtNGI2Ni04YjhiLTU1MjJmNzRiMmJmMS5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjYwMzAzJTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI2MDMwM1QyMzM2MjZaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT0zOWNhOGM2ODcyZTk5YzUzZTE1MTQxOTAzMWU2NzEzNmVhMTAwZTlhMmU4NDUwMDMwMDRiNGEwMzI4YzZiODFlJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.3QeLd2PLxoVR7ZvHnYiYyKl_4_qSDPLim6yrXFTZBKI)

![Classification Result](https://private-user-images.githubusercontent.com/143127911/550610924-ef015c25-b088-4947-aff7-f27aa5b1253d.jpg?jwt=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NzI1ODEyODYsIm5iZiI6MTc3MjU4MDk4NiwicGF0aCI6Ii8xNDMxMjc5MTEvNTUwNjEwOTI0LWVmMDE1YzI1LWIwODgtNDk0Ny1hZmY3LWYyN2FhNWIxMjUzZC5qcGc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjYwMzAzJTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI2MDMwM1QyMzM2MjZaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT05MWFhZDA1NzQwYjU2NzE5MTI2N2UzM2MzN2I5YTk5YmJkMjA0OTcwMGFmOTljNjQwNTFmMGYwYWIxM2JjNGZlJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.7z-y-u3F2nl_Gx9GpY3cC6cGUj6D080dI7AC5hSNu5Q)

## Technologies Used

* **Language:** Kotlin
* **IDE:** Android Studio
* **Platform:** Android (API 24+)
* **ML Framework:** TensorFlow Lite
* **Model Architecture:** MobileNetV2 (pre-trained on ImageNet, fine-tuned on HAM10000)
* **Training Environment:** Google Colab with GPU acceleration
* **Dataset:** HAM10000 (Harvard Dataverse)

## Technical Highlights

* **Transfer Learning:** Leverages MobileNetV2 pre-trained weights from ImageNet
* **Data Augmentation:** On-the-fly rotation, shifting, flipping, and zooming to increase model robustness
* **Balanced Training:** Addressed class imbalance through augmentation (83.8% benign → 50/50 balanced)
* **Mobile Optimization:** Quantized TFLite model (~9 MB) for efficient on-device inference
* **Real-time Classification:** Processes images in 100-300ms on modern Android devices

## Installation & Usage

1. Clone this repository
2. Open in Android Studio
3. Build and run on Android device (API 24+) or emulator
4. Grant camera permissions when prompted
5. Take a photo or select from gallery
6. View classification results with confidence score

## Model Training

The model was trained using:
- **Base Model:** MobileNetV2 (frozen during initial training)
- **Custom Layers:** Global Average Pooling → Dropout (0.3) → Dense (128) → Dropout (0.2) → Dense (1, sigmoid)
- **Optimizer:** Adam (learning rate: 0.0005)
- **Loss Function:** Binary Cross-Entropy
- **Batch Size:** 64
- **Epochs:** 50 (with early stopping)
- **Regularization:** Dropout layers and data augmentation

## Disclaimer

This application is a **proof-of-concept educational project** demonstrating:
- CNN training on medical imaging data
- Transfer learning with MobileNetV2
- Android app development with TensorFlow Lite
- Handling class imbalance in medical datasets

**This is NOT a medical device and should NOT be used for clinical diagnosis.** Always consult qualified medical professionals for health concerns.

## Future Improvements

- Multi-class classification (7 HAM10000 classes)
- Gradient-weighted Class Activation Mapping (Grad-CAM) for visual explanations
- Integration with SLICE-3D dataset for 3D lesion analysis
- Confidence calibration for uncertainty quantification

## About

This project demonstrates practical implementation of deep learning in dermatological image classification and mobile AI deployment. Created as a learning project to explore CNN architectures, transfer learning, and edge AI deployment on Android.

## License

Educational use only. HAM10000 dataset subject to original licensing terms.
