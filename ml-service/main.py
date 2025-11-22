from fastapi import FastAPI
from pydantic import BaseModel 
from transformers import ViTImageProcessor, ViTForImageClassification 
from PIL import Image
import torch
from uuid import UUID

app = FastAPI()

MODEL_NAME = "google/vit-base-patch16-224-in21k"
try:
    print("--- Loading model (this may take a minute)... ---")
    processor = ViTImageProcessor.from_pretrained(MODEL_NAME)
    model = ViTForImageClassification.from_pretrained(MODEL_NAME)
    print("--- Model loaded successfully! ---")
except Exception as e:
    print(f"--- Error loading model: {e} ---")
    processor = None
    model = None 


class ScanRequest(BaseModel):
    mri_scan_id: UUID


@app.get("/")
def read_root():
    return {"message": "Hello! The ML Service is running."}


@app.post("/predict")
async def predict_mri(request: ScanRequest):

    try:
        image = Image.open("test_image.jpg")
    except FileNotFoundError:
        return {"error": "test_image.jpg not found! Please add it."}

    inputs = processor(images=image, return_tensors="pt")

    with torch.no_grad():
        outputs = model(**inputs)
    
    logits = outputs.logits
    predicted_class_idx = logits.argmax(-1).item()

    fake_prediction = "ASD" if predicted_class_idx % 2 == 0 else "VSD"
    confidence = torch.softmax(logits, dim=-1)[0, predicted_class_idx].item()

    return {
        "mri_scan_id": str(request.mri_scan_id),  # <-- IMPORTANT: return UUID as string
        "prediction": fake_prediction,
        "confidence_score": round(confidence, 4),
        "status": "COMPLETED"
    }
