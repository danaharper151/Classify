# Skin Lesion Classifier
## Android application that uses a MobilNetV2 model trained on the HAM10000 dataset for binary classification of skin lesions 

Take a photo of a skin lesion with your cell phone and the CNN will classify the lesion as benign or malignant with a confidence score.

### ***This skin lesion classifier is for software develpoment purposes only and is not for medical use.***

### ***Consult a medical professional for accurate diagnosis of any medical condition.*** 

## Features


- Take a photo of a skin lesion with your cell phone or upload an image from your google drive.
- The pretrained MobilNetV2 model will classify the skin lesion as malignant or benign.
- Results display with confidence scores.
- Accuracy of the current version peaked at 75% and training is ongoing.

## Training dataset specs
- Benign cases: 1627 (50.0%)
- Malignant cases: 1627 (50.0%)
- Total balanced dataset: 3254

### Balanced Data Split:
- Training samples: 2603
- Validation samples: 651
- Training - Benign: 1301, Malignant: 1302
- Validation - Benign: 326, Malignant: 325
  
### Current Validation results:
- Loss: 0.5127
- Accuracy: 0.7558
- AUC: 0.8260
- Precision: 0.7413
- Recall: 0.7846
- F1 Score: 0.7623

<img width="820" height="293" alt="Screenshot 2026-01-24 154003" src="https://github.com/user-attachments/assets/72a00b51-df3a-4b66-8b8b-5522f74b2bf1" />

## Technologies Used
- **Language**: Kotlin
- **IDE**: Android Studio
- **Platform**: Android
- **ML Framework**: MobileNetV2 model trained on the HAM10000 dataset in Colab
  

## About
This project demonstrates Convolutional Neural Network training in dermascopic image classificaion and Android app development.

## Screenshots
<img src="https://github.com/user-attachments/assets/ef015c25-b088-4947-aff7-f27aa5b1253d" width="300" alt="Screenshot_20260216-111917" />
