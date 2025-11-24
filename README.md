<div align="center">

  <h1>🏦 Java Banking System with Persistence</h1>
  
  <p>
    A robust, object-oriented banking simulation developed in Java.<br>
    Features <b>Data Persistence</b> (JSON), <b>Polymorphism</b>, and <b>Unit Testing</b>.
  </p>

  <!-- Badges -->
  <p>
    <img src="https://img.shields.io/badge/Java-17%2B-orange?style=for-the-badge&logo=java" alt="Java" />
    <img src="https://img.shields.io/badge/Build-Maven-blue?style=for-the-badge&logo=apachemaven" alt="Maven" />
    <img src="https://img.shields.io/badge/Format-JSON_(Gson)-yellow?style=for-the-badge&logo=json" alt="Gson" />
    <img src="https://img.shields.io/badge/Testing-JUnit_5-25A162?style=for-the-badge&logo=junit5" alt="JUnit" />
  </p>

  <br />

  <!-- UML Diagram -->
  <img src="UML_diagram.png" alt="UML Architecture Diagram" width="800px" />
  <br>
  <i>Figure 1: System Architecture & Class Hierarchy</i>

</div>

<br />

---

## 🚀 Key Features

### 💾 **Data Persistence (JSON)**
* **Custom Serialization:** Solved the challenge of saving polymorphic lists (`List<Transaction>`) by implementing a custom `TransactionSerializer`.
* **Metadata Tagging:** Automatically injects a `"CLASSNAME"` tag into JSON files to distinguish between `Payment` and `Transfer` objects upon reloading.
* **Auto-Sync:** Ensures real-time synchronization between Runtime Memory (RAM) and the File System.

### 🧬 **Object-Oriented Architecture**
* **Polymorphism:** Distinct calculation logic for `IncomingTransfer` (positive flow) vs. `OutgoingTransfer` (negative flow).
* **Encapsulation:** Critical file operations (`writeAccount`) are strictly `private` to protect data integrity from external interference.
* **Robust Interfaces:** Adheres to a strict `Bank` contract, updated to safely handle `IOExceptions`.

### 🛡️ **Comprehensive Testing (JUnit 5)**
* **Integration Tests:** Validates the full persistence lifecycle (Save → Restart App → Load Data).
* **Unit Tests:** Verifies interest calculation logic and exception handling.
* **Test Isolation:** Uses `@AfterEach` to auto-clean test data (`TestBankData`), preventing "zombie files" from breaking future tests.

---

## 📂 Project Structure

```text
src/
├── main/java/bank/
│   ├── Bank.java                 # Interface contract defining banking operations
│   ├── PrivateBank.java          # Core Engine (Handles File I/O & Logic)
│   ├── Transaction.java          # Abstract base class
│   ├── Payment.java              # Handles deposits/withdrawals with interest
│   ├── Transfer.java             # Handles money transfers between users
│   ├── TransactionSerializer.java # Custom Gson adapter (The "Translator")
│   └── Main.java                 # Application Entry Point
│
└── test/java/bank/
    ├── PaymentTest.java          # Math & Logic verification
    ├── TransferTest.java         # Polymorphism verification
    └── PrivateBankTest.java      # System & Persistence verification

