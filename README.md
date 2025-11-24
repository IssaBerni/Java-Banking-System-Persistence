Java Banking System with Persistence 🏦💾

A robust, object-oriented banking simulation developed in Java. This project demonstrates advanced software engineering concepts including Data Persistence, Polymorphism, and Unit Testing.

It simulates a private banking system where accounts and transactions (Payments, Transfers) are persisted to the local file system using JSON serialization.

(Figure 1: UML Class Diagram illustrating the architecture and inheritance hierarchy)

🚀 Key Features

Data Persistence (JSON):

Utilizes Google Gson library to save and load account data.

Implements a Custom Serializer (TransactionSerializer) to handle polymorphic types (Payment vs. Transfer) by injecting a metadata tag (CLASSNAME) into the JSON files.

Ensures synchronization between runtime memory (RAM) and the file system.

Object-Oriented Design:

Polymorphism: Distinct behaviors for IncomingTransfer (positive balance) and OutgoingTransfer (negative balance).

Encapsulation: Strict access control (private helpers) prevents external interference with the file system.

Interfaces: Adheres to a strict Bank interface contract, updated to handle IOExceptions.

Comprehensive Testing (JUnit 5):

Unit Tests: verifies logic for interest calculations and exception handling.

Integration Tests: verifies the full persistence lifecycle (Save -> Restart -> Load).

Test Isolation: Uses @AfterEach to clean up test data, ensuring a pristine environment for every test run.

🛠️ Technologies Used

Java 17+

Maven (Dependency Management)

Google Gson (JSON Serialization/Deserialization)

JUnit 5 (Unit & Integration Testing)

Git (Version Control)

📂 Project Structure

src/
├── main/java/bank/
│   ├── Bank.java                 # Interface defining banking operations
│   ├── PrivateBank.java          # Core logic implementation (File I/O)
│   ├── Transaction.java          # Abstract base class
│   ├── Payment.java              # Handles deposits/withdrawals with interest
│   ├── Transfer.java             # Handles money transfers
│   ├── TransactionSerializer.java # Custom Gson adapter for polymorphism
│   └── Main.java                 # Demonstration entry point
│
└── test/java/bank/
    ├── PaymentTest.java          # Logic verification for Payments
    ├── TransferTest.java         # Polymorphism verification
    └── PrivateBankTest.java      # Persistence and file system verification


⚙️ How It Works

1. Serialization Logic

The challenge was saving a list of generic Transaction objects (List<Transaction>). Standard JSON parsers lose the specific subclass type (Payment vs Transfer).
Solution: I implemented a custom TransactionSerializer that adds a property "CLASSNAME": "Payment" during serialization. During deserialization, Reflection is used to read this tag and instantiate the correct class.

2. Persistence Lifecycle

Startup: The PrivateBank constructor calls readAccounts() to scan the directory and load all .json files into memory.

Runtime: Every time addTransaction() or createAccount() is called, the writeAccount() method immediately updates the specific JSON file on the disk.

🧪 How to Run

Prerequisites

Java JDK 17 or higher.

Maven installed.

Running the Application

Clone the repository:

git clone [https://github.com/IssaBerni/Java-Banking-System-Persistence.git](https://github.com/IssaBerni/Java-Banking-System-Persistence.git)


Navigate to the project folder:

cd Java-Banking-System-Persistence


Build the project:

mvn clean install


Run the Main class via your IDE (IntelliJ IDEA recommended) or via command line.

Running Tests

To execute the JUnit test suite:

mvn test


👤 Author

Issa Berni

GitHub: IssaBerni

This project was developed as part of the Object-Oriented Software Development (OOS) practical course.
