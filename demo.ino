#include <Arduino.h>
#include <TimeLib.h>
#if defined(ESP32)
#include <WiFi.h>
#elif defined(ESP8266)
#include <ESP8266WiFi.h>
#endif
#include <Firebase_ESP_Client.h>

//Provide the token generation process info.
#include "addons/TokenHelper.h"
//Provide the RTDB payload printing info and other helper functions.
#include "addons/RTDBHelper.h"

// Insert your network credentials
#define WIFI_SSID "red"
#define WIFI_PASSWORD "11111111"

// Insert Firebase project API Key
#define API_KEY "AIzaSyAM3CWr-Ach1f1xGL4Re79tODG6TkaKXrY"

// Insert RTDB URLefine the RTDB URL */
#define DATABASE_URL "https://dht11-mq2-default-rtdb.firebaseio.com/"

#define LED 2
#define MQ2A 34
#define BUZZER 5
#define GAS_SENSOR A0  // Chân analog dùng để đọc cảm biến khí gas
#include <time.h>
const int DHT_PIN = 15;
int sensorValue = 0;  // Biến để lưu giá trị từ cảm biến
#include "DHTesp.h"
DHTesp dhtSensor;
bool fireDetected = false;

//Define Firebase Data object
FirebaseData fbdo;

FirebaseAuth auth;
FirebaseConfig config;

unsigned long sendDataPrevMillis = 0;
int count = 0;
bool signupOK = false;
struct SensorData {
  String temperature;
  String humidity;
  float CO_concentration;
  float sensorValue;
  bool fireDetected;
  String timestamp;
};
FirebaseData firebaseData;
void setup() {
  pinMode(LED, OUTPUT);
  pinMode(BUZZER, OUTPUT);
  pinMode(MQ2A, INPUT);
  Serial.println("Sensor start");
  dhtSensor.setup(DHT_PIN, DHTesp::DHT11);
  Serial.begin(115200);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  /* Assign the api key (required) */
  config.api_key = API_KEY;

  /* Assign the RTDB URL (required) */
  config.database_url = DATABASE_URL;

  /* Sign up */
  if (Firebase.signUp(&config, &auth, "", "")) {
    Serial.println("ok");
    signupOK = true;
  } else {
    Serial.printf("%s\n", config.signer.signupError.message.c_str());
  }

  /* Assign the callback function for the long running token generation task */
  config.token_status_callback = tokenStatusCallback;  //see addons/TokenHelper.h

  Firebase.begin(&config, &auth);
  Firebase.reconnectWiFi(true);
  configTime(0, 0, "pool.ntp.org");
}

void loop() {
  // SensorData data1;
  TempAndHumidity data = dhtSensor.getTempAndHumidity();
  Serial.println("Nhiệt độ: " + String(data.temperature, 2) + "°C");
  Serial.println("Độ ẩm: " + String(data.humidity, 1) + "%");


  // Đọc giá trị từ cảm biến khí gas (MQ2)
  sensorValue = analogRead(MQ2A);
  Serial.print("Giá trị cảm biến khí gas: ");
  Serial.println(sensorValue);

  // Giả định chuyển đổi giá trị analog thành nồng độ CO
  int CO_concentration = map(sensorValue, 0, 1023, 0, 100);  // Giả định chuyển đổi

  Serial.print("Nồng độ khí CO: ");
  Serial.println(CO_concentration);

  if (CO_concentration > 10) {
    fireDetected = true;  // Đánh dấu rằng có khói hoặc có khả năng cháy
  } else {
    fireDetected = false;  // Không có khói hoặc cháy
  }

  if (data.temperature > 1) {  // Giả định nhiệt độ cao là có cháy
    fireDetected = true;
  }

  if (fireDetected) {    // Bật đèn LED nếu phát hiện có khói hoặc nhiệt độ cao (nghi ngờ có cháy)
    tone(BUZZER, 1000);  // Bật còi
  } else {               // Tắt đèn LED nếu không có khói hoặc nhiệt độ bình thường
    noTone(BUZZER);      // Tắt còi
  }

  if (Firebase.ready() && signupOK && (millis() - sendDataPrevMillis > 1000 || sendDataPrevMillis == 0)) {
    sendDataPrevMillis = millis();
    if (Firebase.RTDB.getBool(&fbdo, "sensor/led")) {
      if (fbdo.dataType() == "boolean") {
        bool ledStatus = fbdo.boolData();
        if (!ledStatus) digitalWrite(LED, LOW);
        else {
          if (fireDetected) {
            digitalWrite(LED, HIGH);  // Bật đèn LED nếu phát hiện có khói hoặc nhiệt độ cao (nghi ngờ có cháy)
            tone(BUZZER, 1000);       // Bật còi
          } else {
            digitalWrite(LED, LOW);  // Bật đèn LED nếu phát hiện có khói hoặc nhiệt độ cao (nghi ngờ có cháy)
            tone(BUZZER, 0);         // Bật còi
          }
        }
      }
    }
    time_t now;
    struct tm tm;
    time(&now);
    localtime_r(&now, &tm);

    // Lấy timestamp từ thời gian hiện tại
    time_t timestamp = mktime(&tm);
    String timestampStr = String(timestamp);

    // data1.temperature = String(data.temperature, 2);
    // data1.humidity = String(data.humidity, 1);
    // data1.CO_concentration = CO_concentration;
    // data1.sensorValue = sensorValue;
    // data1.fireDetected = fireDetected;
    // data1.timestamp = timestampStr;

    Firebase.RTDB.setString(&fbdo, "demo/" + timestampStr + "/temperature", String(data.temperature, 2));
    Firebase.RTDB.setString(&fbdo, "demo/" + timestampStr + "/humidity", String(data.humidity, 1));
    Firebase.RTDB.setFloat(&fbdo, "demo/" + timestampStr + "/coConcentration", CO_concentration);
    Firebase.RTDB.setFloat(&fbdo, "demo/" + timestampStr + "/gas", sensorValue);
    Firebase.RTDB.setBool(&fbdo, "demo/" + timestampStr + "/fireDetected", fireDetected);

    // Write an Int number on the database path test/int
    //   if (Firebase.RTDB.setString(&fbdo, "demo/" + timestampStr + "/temperature", String(data.temperature, 2))) {
    //     Serial.println("PASSED");
    //     Serial.println("PATH: " + fbdo.dataPath());
    //     Serial.println("TYPE: " + fbdo.dataType());
    //   } else {
    //     Serial.println("FAILED");
    //     Serial.println("REASON: " + fbdo.errorReason());
    //   }
    //   count++;

    //   // Write an Float number on the database path test/float
    //   if (Firebase.RTDB.setString(&fbdo, "demo/" + timestampStr + "/humidity", String(data.humidity, 1))) {
    //     Serial.println("PASSED");
    //     Serial.println("PATH: " + fbdo.dataPath());
    //     Serial.println("TYPE: " + fbdo.dataType());
    //   } else {
    //     Serial.println("FAILED");
    //     Serial.println("REASON: " + fbdo.errorReason());
    //   }

    //   if (Firebase.RTDB.setFloat(&fbdo, "demo/" + timestampStr + "/coConcentration", CO_concentration)) {
    //     Serial.println("PASSED");
    //     Serial.println("PATH: " + fbdo.dataPath());
    //     Serial.println("TYPE: " + fbdo.dataType());
    //   } else {
    //     Serial.println("FAILED");
    //     Serial.println("REASON: " + fbdo.errorReason());
    //   }

    //   if (Firebase.RTDB.setFloat(&fbdo, "demo/" + timestampStr + "/gas", sensorValue)) {
    //     Serial.println("PASSED");
    //     Serial.println("PATH: " + fbdo.dataPath());
    //     Serial.println("TYPE: " + fbdo.dataType());
    //   } else {
    //     Serial.println("FAILED");
    //     Serial.println("REASON: " + fbdo.errorReason());
    //   }

    //   if (Firebase.RTDB.setBool(&fbdo, "demo/" + timestampStr + "/fireDetected", fireDetected)) {
    //     Serial.println("PASSED");
    //     Serial.println("PATH: " + fbdo.dataPath());
    //     Serial.println("TYPE: " + fbdo.dataType());
    //   } else {
    //     Serial.println("FAILED");
    //     Serial.println("REASON: " + fbdo.errorReason());
    //   }

    //   // Chuyển timestamp thành String và gửi lên Firebase

    //   if (Firebase.RTDB.setString(&fbdo, "demo/" + timestampStr + "/time", timestampStr)) {
    //     Serial.println("PASSED");
    //     Serial.println("PATH: " + fbdo.dataPath());
    //     Serial.println("TYPE: " + fbdo.dataType());
    //   } else {
    //     Serial.println("FAILED");
    //     Serial.println("REASON: " + fbdo.errorReason());
    //   }
    // }
    // if (sendDataToFirebase(data1)) {
    //   Serial.println("Data sent to Firebase successfully!");
    // } else {
    //   Serial.println("Failed to send data to Firebase");
    // }
    Serial.println("---");
    delay(1000);
  }
}
// bool sendDataToFirebase(const SensorData& data) {
//   // Tạo một đối tượng JsonObject để xây dựng cấu trúc dữ liệu
//   FirebaseJson json;
//   json.set("temperature", data.temperature);
//   json.set("humidity", data.humidity);
//   json.set("coConcentration", data.CO_concentration);
//   json.set("gas", data.sensorValue);
//   json.set("fireDetected", data.fireDetected);
//   json.set("time", String(data.timestamp));

//   // Gửi đối tượng JsonObject lên Firebase
//   if (Firebase.RTDB.setJson(&firebaseData, "demo/" + String(data.timestamp), json)) {
//     Serial.println("Data sent to Firebase successfully!");
//     return true;
//   } else {
//     Serial.println("Failed to send data to Firebase");
//     Serial.println("REASON: " + firebaseData.errorReason());
//     return false;
//   }
// }
