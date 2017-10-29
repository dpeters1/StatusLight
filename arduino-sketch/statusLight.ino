#include <ESP8266WiFi.h>            
#include <ESP8266WebServer.h>

#define ledPin 5

ESP8266WebServer server(80); 

const char* ssid = "DOM";
const char* password = "allstream11";

int brightness = 0;
int brightnessStep = 20;
int maxBrightness = 1023;
int ledMode = 2;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200);
  
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  Serial.print("Waiting to connect...");

  while(WiFi.status() != WL_CONNECTED){
    delay(500);
    Serial.print(".");
  }
  Serial.print("\nIP address: ");
  Serial.println(WiFi.localIP());

  server.on("/", handleArgs); 
  server.begin();        

  pinMode(ledPin, OUTPUT);
}

void loop() {
  // put your main code here, to run repeatedly:
  server.handleClient();


    yield();
  }
}

void handleArgs(){
  ledMode = server.arg("ledMode").toInt();
  maxBrightness = map(server.arg("maxBrightness").toInt(), 0, 100, 0, 1023);
  brightnessStep = int(maxBrightness/50);
  brightness = 0;
  server.send(200, "text/plain", "");
}



void flash() {
  delay(1000);
  analogWrite(ledPin, maxBrightness);
  delay(1000);
  analogWrite(ledPin, 0);
}

