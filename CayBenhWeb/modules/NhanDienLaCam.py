import numpy as np
from ultralytics import YOLO

names = {0: "đốm đen", 1: "tàn nhang", 2: "xanh hóa", 3: "khỏe mạnh", 4: "hắc tố"}
model = YOLO("models/nhan_dien_benh_la_cam.pt")


def predictLaCam(image: np.ndarray):
    result = model.predict(image, verbose=False)[0]
    boxes = result.boxes.xyxy.cpu().detach().numpy().astype("int")
    cls = result.boxes.cls.cpu().detach().numpy().astype("int")
    classes = [names[i] for i in cls]
    scores = result.boxes.conf.cpu().detach().numpy().astype('float')
    return boxes, classes, scores,cls
