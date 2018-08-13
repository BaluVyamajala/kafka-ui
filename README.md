# kafka-ui
UI for Kafka and KafkaConfluent

# Environment variables
```
KAFKA_BOOTSTRAP_SERVERS
KAFKA_LOGIN_PASSWORD	
KAFKA_LOGIN_USER_NAME
KAFKA_SASL_MECHANISM
KAFKA_SECURITY_PROTOCOL
```
**Note:** Bootstrap Servers is mandatory, remaining 4 are needed for confluent Kafka. Sample values are
Sasl-Mechanism "PLAIN" 
Security-Protocal "SASL_SSL"



# Build from source for deployable Jar
```
git clone https://github.com/BaluVyamajala/kafka-ui.git
cd kafka-ui
mvn clean install 
java -jar kafka-ui-server/target/kafka-ui-server-1.0-SNAPSHOT.jar
localhost:5000
```

# Development 
```
Server:
git clone https://github.com/BaluVyamajala/kafka-ui.git
import kafka-ui-server as maven project in to your editor(eclipse) and run KafkaUiStarter class as Java application.
import kafka-ui-client into your editor(visual studio code)
npm install
npm run-script start
```
