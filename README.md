# Sms command handler service

### What was used
- Java 8
- Lombok
- Slf4j
- Mockito
- Swagger-ui

### Installation

This app requires maven to run. Download it and follow the steps:

```sh
$ cd sms-service
$ mvn install
$ mvn spring-boot:run
```

### How execute a command

In order to provide a functional app, the back-end user and transfer services were implemented with an 'in memory' approach. 
There are three users which are created in memory when the app runs: 
```json
[
  {
    "username": "GIORNE",
    "deviceId": "12345",
    "balance": 1000,
    "transactions": []
  },
  {
    "username": "FLITZ",
    "deviceId": "54321",
    "balance": 500,
    "transactions": []
  },
  {
    "username": "PAUL",
    "deviceId": "159753",
    "balance": 200,
    "transactions": []
  }
]
```
#### Using swagger

You can see the api docs here: http://localhost:8080/swagger-ui.html

#### Using curl

```sh
curl -d '{"deviceId": "deviceId", "smsContent": "smsContent"}' -H "Content-Type: application/json" -X POST http://localhost:8080/api/sms/commands
```
