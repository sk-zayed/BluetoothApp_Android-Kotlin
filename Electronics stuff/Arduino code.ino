float voltage, tempC;
int temp_reading, intensity, input;
String light = "ON", fan = "ON", socket1 = "ON", socket2="ON";

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  pinMode(2, OUTPUT);
  pinMode(3, OUTPUT);
  pinMode(4, OUTPUT);
  pinMode(5, OUTPUT);
}

void loop() {
  temp_reading = analogRead(A4);
  voltage = temp_reading * (5.0 / 1024.0);
  tempC = voltage * 100;
  Serial.print("Temperature: ");
  Serial.println(tempC);

  intensity = analogRead(A5);
  Serial.print("Intensity: ");
  Serial.println(intensity);

  input = Serial.parseInt();

  switch(input){
    case 2:
      if(light == "OFF"){
        Serial.print("2 if called !!");
        digitalWrite(2, LOW); 
        light = "ON";
      }else{
        Serial.print("2 else called !!");
        digitalWrite(2, HIGH);
        light = "OFF";
      }
      break;

    case 3:
      if(fan == "OFF"){
        Serial.print("3 if called !!");
        digitalWrite(3, LOW); 
        fan = "ON";
      }else{
        Serial.print("3 else called !!");
        digitalWrite(3, HIGH);
        fan = "OFF";
      }
      break;

     case 4:
      if(socket1 == "OFF"){
        Serial.print("4 if called !!");
        digitalWrite(4, LOW); 
        socket1 = "ON";
      }else{
        Serial.print("4 else called !!");
        digitalWrite(4, HIGH);
        socket1 = "OFF";
      }
      break;

      case 5:
      if(socket2 == "OFF"){
        Serial.print("5 if called !!");
        digitalWrite(5, LOW); 
        socket2 = "ON";
      }else{
        Serial.print("5 else called !!");
        digitalWrite(5, HIGH);
        socket2 = "OFF";
      }
      break;
  }
  
  if(light == "ON"){
    if(intensity > 30){
      digitalWrite(2, HIGH);
      Serial.println("CONDITION L OFF INT > 30");
    }else{
      digitalWrite(2, LOW);
      Serial.println("CONDITION L ON INT < 30");
    }
  }

  if(fan == "ON"){
    if(tempC > 25){
      digitalWrite(3, HIGH);
      Serial.println("CONDITION F OFF TEM > 25");
    }else{
      digitalWrite(3, LOW);
      Serial.println("CONDITION F ON TEM < 25");
    }
  }
}
