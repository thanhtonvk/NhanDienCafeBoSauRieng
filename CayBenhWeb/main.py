from modules.NhanDienCayCafe import predictCafe
from modules.NhanDienSauRieng import predictSauRieng
from modules.NhanDienCayBo import predictBo
import cv2
import os
from flask import Flask, render_template, request, redirect, jsonify
from unidecode import unidecode

app = Flask(__name__)
f = open("chua_benh/cafe.txt", "r", encoding="utf-8")
lines = f.read()
chua_benh_cafe = lines.split("#")
f.close()

f = open("chua_benh/saurieng.txt", "r", encoding="utf-8")
lines = f.read()
chua_benh_sau_rieng = lines.split("#")
f.close()


f = open("chua_benh/bo.txt", "r", encoding="utf-8")
lines = f.read()
chua_benh_bo= lines.split("#")
f.close()
import base64


def encode_image(image_path):
    with open(image_path, "rb") as image_file:
        return base64.b64encode(image_file.read()).decode("utf-8")


@app.route("/la-cafe", methods=["GET", "POST"])
def la_cafe():
    if request.method == "GET":
        return render_template("index.html", data=None)
    f = request.files["fileCafe"]
    save_path = f"static/image.png"
    f.save(save_path)
    image = cv2.imread(save_path)

    rectangle_color = (0, 255, 0)  # Green color
    rectangle_thickness = 2

    font = cv2.FONT_HERSHEY_SIMPLEX
    font_scale = 1
    text_color = (0, 0, 255)  # Red color
    text_thickness = 2
    boxes, classes, scores, nhan_label = predictCafe(image)
    result = ""
    for box, cls, score in zip(boxes, classes, scores):
        xmin, ymin, xmax, ymax = box
        top_left = (xmin, ymin)
        bottom_right = (xmax, ymax)
        cv2.rectangle(
            image, top_left, bottom_right, rectangle_color, rectangle_thickness
        )
        text_position = (xmin, ymin - 10)
        cv2.putText(
            image,
            unidecode(cls),
            text_position,
            font,
            font_scale,
            text_color,
            text_thickness,
        )
        result += f"{cls} - {int(score*100)}% \n"
    if len(classes) > 0:
        chua_benh = chua_benh_cafe[nhan_label[0]]
    else:
        chua_benh = "Khoẻ mạnh"
    cv2.imwrite(save_path, image)
    image_base64 = encode_image(save_path)
    response = {
        "image_path": image_base64,
        "result": result,
        "type": 5,
        "chua_benh": chua_benh,
    }
    return render_template("index.html", data=response)


@app.route("/la-sau-rieng", methods=["GET", "POST"])
def la_sau_rieng():
    if request.method == "GET":
        return render_template("index.html", data=None)
    f = request.files["fileSauRieng"]
    save_path = f"static/image.png"
    f.save(save_path)
    image = cv2.imread(save_path)

    rectangle_color = (0, 255, 0)  # Green color
    rectangle_thickness = 2

    font = cv2.FONT_HERSHEY_SIMPLEX
    font_scale = 1
    text_color = (0, 0, 255)  # Red color
    text_thickness = 2
    boxes, classes, scores, nhan_label = predictSauRieng(image)
    result = ""
    for box, cls, score in zip(boxes, classes, scores):
        xmin, ymin, xmax, ymax = box
        top_left = (xmin, ymin)
        bottom_right = (xmax, ymax)
        cv2.rectangle(
            image, top_left, bottom_right, rectangle_color, rectangle_thickness
        )
        text_position = (xmin, ymin - 10)
        cv2.putText(
            image,
            unidecode(cls),
            text_position,
            font,
            font_scale,
            text_color,
            text_thickness,
        )
        result += f"{cls} - {int(score*100)}% \n"
    if len(classes) > 0:
        chua_benh = chua_benh_sau_rieng[nhan_label[0]]
    else:
        chua_benh = "Khoẻ mạnh"
    cv2.imwrite(save_path, image)
    image_base64 = encode_image(save_path)
    response = {
        "image_path": image_base64,
        "result": result,
        "type": 0,
        "chua_benh": chua_benh,
    }
    return render_template("index.html", data=response)


@app.route("/la-bo", methods=["GET", "POST"])
def la_bo():
    if request.method == "GET":
        return render_template("index.html", data=None)
    f = request.files["fileBo"]
    save_path = f"static/image.png"
    f.save(save_path)
    image = cv2.imread(save_path)
    idx,score,nhan_label = predictBo(image)
    result = ""

    result += f"{nhan_label} - {int(score*100)}% \n"

    chua_benh = chua_benh_bo[idx]

    cv2.imwrite(save_path, image)
    image_base64 = encode_image(save_path)
    response = {
        "image_path": image_base64,
        "result": result,
        "type": 1,
        "chua_benh": chua_benh,
    }
    return render_template("index.html", data=response)

@app.route("/", methods=["GET"])
def index():
    return render_template("index.html", data=None)


import socket


def get_local_ipv4():
    try:
        # Tạo kết nối giả để tìm địa chỉ IP thực
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.connect(("8.8.8.8", 80))
        ipv4 = s.getsockname()[0]
        s.close()
        return ipv4
    except Exception as e:
        return f"Error: {e}"


if __name__ == "__main__":
    ipv4 = get_local_ipv4()
    app.run(host=ipv4, port=5000, debug=True)
