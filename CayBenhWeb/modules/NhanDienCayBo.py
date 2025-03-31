import numpy as np
from ultralytics import YOLO
import os

names = {0: "Bị bệnh", 1: "Khoẻ mạnh"}
model = YOLO("models/avocado.pt")
def predictBo(image: np.ndarray):
    result = model.predict(image, verbose=False)[0]
    probs = result.probs.data.cpu().detach().numpy()
    idx = np.argmax(probs)
    return idx,probs[idx],names[idx]