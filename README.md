# Transaction Management Microservice (TransactionMS)

This microservice is responsible for managing all transactions within the system, including deposits, withdrawals, and transfers. Transactions are recorded with relevant information such as the operation type, amount, and the accounts involved.
**TransactionMS** is built using **Reactive Programming** principles, utilizing **Spring WebFlux**, **Mono**, and **Flux**. These technologies enable the microservice to handle requests and responses asynchronously and reactively, ensuring efficient and scalable communication, especially under high load conditions.

## Endpoints

- **POST /transacciones/deposito**: Registers a deposit in a bank account.
- **POST /transacciones/retiro**: Registers a withdrawal from a bank account.
- **POST /transacciones/transferencia**: Registers a transfer between bank accounts.
- **GET /transacciones/historial**: Retrieves the history of all transactions made.

## Business Rules

- Deposits and withdrawals are applied to specific accounts.
- Transfers require specifying both the source and destination account, as well as the amount to be transferred.
- Withdrawals and transfers cannot be made if the account balance is insufficient.

## AccountMS Integration

The **TransactionMS** microservice interacts with the **AccountMS** to perform the required operations, such as:

- **Deposits**: When a deposit is made, **TransactionMS** communicates with **AccountMS** to update the balance of the target account.
- **Withdrawals**: Similarly, withdrawals are processed by **TransactionMS** in coordination with **AccountMS** to update the balance of the corresponding account.
- **Transfers**: For transfers between accounts, **TransactionMS** interacts with **AccountMS** to ensure the correct deduction from the source account and addition to the destination account.

**TransactionMS** uses **WebClient** to communicate with **AccountMS** for these operations. **WebClient** provides a non-blocking, reactive way to send HTTP requests and handle responses, ensuring smooth and efficient communication between the microservices.

This interaction ensures the integrity of financial operations and up-to-date account balances across the system.

## Technologies used

- **Programming Language**: Java 17
- **Framework**: Spring Boot
- **Database**: MongoDB Cloud
- **ORM (Object-Relational Mapping)**: JPA (Java Persistence API)
- **Helper Libraries**: Lombok
- **Reactive Programming**: Spring WebFlux, Mono
- **Testing**:
  - **JUnit** (Unit Testing)
  - **Mockito** (Framework for mocking in unit tests)
- **Code Coverage**: JaCoCo
- **Code Style**: Checkstyle
- **API Documentation**: OpenAPI (Swagger)

## Postman Collection

You can find the Postman collection for testing the **TransactionMS** API at the following link:

[CustomerMS Postman Collection](https://www.postman.com/yulyschr/test-api-transactionms/overview)

This collection includes all the endpoints and examples for testing the API.

## DIAGRAMs

### Sequence diagram

![DS-transactions](https://github.com/user-attachments/assets/a951e2bf-1df7-41b8-a5af-1cded254ebf0)

## Documentation

You can find additional documentation for the project in the following documents:

- [Analysis of SOLID Principles and Design Patterns](https://github.com/user-attachments/files/18005354/Analis.de.principios.solid.y.patrones.de.diseno.docx)
- [Unit Testing and CheckStyle](https://github.com/user-attachments/files/18005355/Pruebas.unitarias.y.checkStyle.docx)

Please refer to these documents for a detailed analysis of the SOLID principles, design patterns used, unit testing practices, and code style checks.
