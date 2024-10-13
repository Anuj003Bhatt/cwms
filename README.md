# Crypto Wallet Management System
This repository contains the code base for the Crypto wallet management system which
allows users to create account and then maintain the wallets for themselves.

# Methodology
Each user needs to create an account and then authenticate themselves in order to be able to use the application. 
The application exposes to endpoints for the same:
- [POST] `/users`: To create account
- `/users/authenticate`
    - This endpoint validates the username and password of user and returns a JWT containing the role as well.

# Code Architecture
The repository tries to create a microservice based architecture with 2 services
and one shared library as below:
- `cwms-common`: Shared Library for other modules
- `cwms-user`: The users module where one can sign-up/ login and authenticate.
- `cwms-wallet`: The wallet management system.

# Pre-requisites
In order to run the application one needs below pre-requisites installed:
- Java (17/19)
- Maven
- Docker

# Run the app locally
To setup project dependencies first start the docker dependencies.
## Step #1
Run the below command at the root of the project 
```shell
docker-compose up
```

## Step #2
Open the project in IntelliJ or other IDE and navigate to the main 
application of `cwms-user` & `cwms-wallet` and run the app directly from there.

## Step #2 Alternative
One can manually run the maven commands from the projects as well
- Navigate to either of the application `cwms-user` or `cwms-wallet`
- Run the command `mvn clean install`
- Run the jar file directly.

# Tech Stack
- Java
- Spring Boot
- Spring Security
- Docker
- PostgreSQL
- JWt
- Spring Doc Open API
- Retrofit API Client

# Testing the app
To test the api endpoints please refer to below swaggers after starting both the applications:
- `cwms-user` : http://localhost:8001/swagger-ui/index.html
- `cwms-wallet` : http://localhost:8002/swagger-ui/index.html

# Outstanding Points
- Complete User authorization was 