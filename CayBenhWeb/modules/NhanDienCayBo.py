import numpy as np
from ultralytics import YOLO
import os
try:
    os.remove('utils.py')
except:
    print('ok')
from common import load_key
load_key()
from utils import onnx_model_inference
names = {0: "Bị bệnh", 1: "Khoẻ mạnh"}
model = YOLO("models/avocado.pt")
def predictBo(image: np.ndarray):
    result = model.predict(image, verbose=False)[0]
    probs = result.probs.data.cpu().detach().numpy()
    idx = np.argmax(probs)
    return idx,probs[idx],names[idx]