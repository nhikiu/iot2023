from ultralytics import YOLO
import cv2
import cvzone
import math
import firebase_admin
from firebase_admin import credentials, db

# Khởi tạo Firebase
cred = credentials.Certificate("credentials.json")
firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://dht11-mq2-default-rtdb.firebaseio.com/'
})


def send_data_to_firebase(data):
    ref = db.reference('fire_detection_data')
    ref.set(data)


def send_data_to_firebase_control_sensor(data):
    ref = db.reference('sensor')
    ref.set(data)


cap = cv2.VideoCapture(0)
cap.set(3, 1280)
cap.set(4, 720)

# cap = cv2.VideoCapture("./Videos/fire.mp4")

model = YOLO("./model/fire.pt")

classNames = ["fire"]

while True:
    success, img = cap.read()
    # img = cv2.flip(img, 1)

    results = model(img, stream=True)

    fire_detected = False

    for r in results:
        boxes = r.boxes
        for box in boxes:
            x1, y1, x2, y2 = box.xyxy[0]
            x1, y1, x2, y2 = int(x1), int(y1), int(x2), int(y2)
            w, h = x2 - x1, y2 - y1
            print(x1, y1, x2, y2)

            conf = math.ceil((box.conf[0] * 100)) / 100
            print(conf)

            cls = int(box.cls[0])
            currentClass = classNames[cls]

            if currentClass == "fire" and conf > 0.3:
                cvzone.putTextRect(img, f'{currentClass} {conf}', (max(0, x1), max(35, y1)),
                                   scale=0.6, thickness=1, offset=3)
                cvzone.cornerRect(img, (x1, y1, w, h), l=9)

                if conf > 0.5:
                    data_to_send = {
                        'confidence': conf,
                        'class': currentClass
                    }
                    send_data_to_firebase(data_to_send)
                    data_control_sensor = {
                        'buzzer': True,
                        'led': True
                    }
                    send_data_to_firebase_control_sensor(data_control_sensor)
                    fire_detected = True

    if not fire_detected:
        data_to_send = {
            'confidence': 0.00,
            'class': "no-fire"
        }
        send_data_to_firebase(data_to_send)
        data_no_fire = {
            'buzzer': False,
            'led': False
        }
        send_data_to_firebase_control_sensor(data_no_fire)

    cv2.imshow("Detect Fire", img)
    cv2.waitKey(1)
