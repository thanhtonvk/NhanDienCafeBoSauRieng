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
names = {0: "Đốm lá tảo", 1: "Bốc lá", 2: "Đốm lá", 3: "Không bệnh"}
model = YOLO("models/durian.pt")
def predictSauRieng(image: np.ndarray):
    result = model.predict(image, verbose=False)[0]
    boxes = result.boxes.xyxy.cpu().detach().numpy().astype("int")
    cls = result.boxes.cls.cpu().detach().numpy().astype("int")
    classes = [names[i] for i in cls]
    scores = result.boxes.conf.cpu().detach().numpy().astype('float')
    return boxes, classes, scores,cls
