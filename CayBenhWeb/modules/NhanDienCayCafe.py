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
names = {0: "đốm rong", 1: "nấm rỉ sắt", 2: "phấn trắng", 3: "sâu vẽ bùa"}
model = YOLO("models/coffee.pt")


def predictCafe(image: np.ndarray):

    result = model.predict(image, verbose=False)[0]
    boxes = result.boxes.xyxy.cpu().detach().numpy().astype("int")
    cls = result.boxes.cls.cpu().detach().numpy().astype("int")
    classes = [names[i] for i in cls]
    scores = result.boxes.conf.cpu().detach().numpy().astype('float')
    return boxes, classes, scores,cls
