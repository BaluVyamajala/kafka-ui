# kafka-ui
UI for Kafka and KafkaConfluent
View all topics
Create and Delete Topics
View topic content from Begenning or from offset
Search Topic content by Key

# Apis for Creating and Deleting Topics
create and delete Topic options are not provided on UI, you can call apis directly by passing topicName as queryParm.
```
POST http://localhost:5000/api/topic?topicName=My_Topic_Name
DELETE http://localhost:5000/api/topic?topicName=My_Topic_Name
```


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

# Docker image
```
docker pull baluvv/kafka-ui
docker run -e  KAFKA_BOOTSTRAP_SERVERS='test1:9092,test2:9093,test3:9094' -e KAFKA_LOGIN_PASSWORD='mypassword' -e KAFKA_LOGIN_USER_NAME='myusername' -e KAFKA_SASL_MECHANISM='PLAIN' -e KAFKA_SECURITY_PROTOCOL='SASL_SSL' -p 5000:5000 kafka-ui:latest 
```
**Note:** make sure to pass necessary environment variables for docker run (not all 5 variables are mandatory)

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
Clone the code:
git clone https://github.com/BaluVyamajala/kafka-ui.git
Setup Server:
import kafka-ui-server as maven project in to your editor(eclipse) and run KafkaUiStarter class as Java application.
Setup Client: 
import kafka-ui-client into your editor(visual studio code)
npm install
npm run-script start
```
