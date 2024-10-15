# Crypto Wallet Management System
This repository contains the code base for the Crypto wallet management system which
allows users to create account and then maintain the wallets for themselves.

# Approach & Methodology
 

The approach followed in this application is as follows:
- Each user needs to create an account and then authenticate themselves in order to be able to use the application.
    - The application exposes to endpoints for the same:
        - [POST] `/users`: To create account
        - `/users/authenticate`
    - This endpoint validates the username and password of user and returns a JWT containing the role as well.
- Each user has one single dedicated wallet and each wallet consists of wallet items for specific currencies.
- The transaction management system performs transactions on the wallet items, identifying the source and target wallet items.
- Apart from that the 

# Available Features
- Wallet Management:
  - Each user has one single dedicated `wallet` associated with their account.
  - Each user's `wallet` has `wallet items` inside it that correspond to specific currencies [like bitcoin, ethereum etc.].
  - Each wallet is encrypted using a pin
  - APIs:
      - **[POST]** Create Wallet
      - **[GET]** Get Wallet By ID [User Authentication is required]
      - List Wallet
      - **[POST]** Add Wallet Item for another currency
      - **[DELETE]** Delete Wallet By ID
- Transaction management:
  - The application allows user send units of currency from one wallet to another
  - The payload requires below things to process the request:
      - Source Wallet ID
      - User Pin
      - Target Wallet ID
      - Units to transfer
      - Public key of sender
  - The application verifies the wallet for balances by first verifying against the pin
  - The application verifies the sender by verifying the private and public keys
  - Then the transaction is signed using the private key of sender
  - The same is then put into the block chain
  - APIs:
      - **[POST]** Transactions 
- User Management:
  - User can create account by sign up / `create user` endpoint.
    - **Note:** The user's wallet is not created as of now at account creation time. A separate api is exposed to do that that initiates the wallet creation with one currency.
  - APIs:
    - **[POST]** Create User
    - **[GET]** List User
    - **[GET]** GET User by ID
    - **[POST]** Authenticate user and return JWT
    - **[PUT]** Assign role to a user
    - **[PATCH]** Enable user
    - **[PATCH]** Disable user

# Pending Features
- Complete blockchain validation simulation

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
**Note:**
- Due to some outstanding issues the maven install is not working.
- Please use the intelli to run the main app directly and the application will run.

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

