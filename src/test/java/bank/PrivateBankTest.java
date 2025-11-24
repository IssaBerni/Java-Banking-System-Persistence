package bank;

import bank.exceptions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for PrivateBank (Aufgabe 3).
 * Tests persistence, account management, and sorting logic.
 */
public class PrivateBankTest {

    private PrivateBank bank;
    private final String TEST_DIR = "TestBankData"; // The folder for test files

    /**
     * Helper method to delete the test directory.
     * Ensures we start and end with a clean slate.
     */
    private void cleanUpDirectory() {
        File dir = new File(TEST_DIR);
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File f : files) {
                    f.delete(); // Delete file "Konto X.json"
                }
            }
            dir.delete(); // Delete folder "TestBankData"
        }
    }

    @BeforeEach
    void setUp() throws IOException, AccountAlreadyExistsException, TransactionAttributeException, TransactionAlreadyExistException {
        // SAFETY: Delete any old/broken files before we start!
        cleanUpDirectory();

        // Initialize bank with the specific test directory
        bank = new PrivateBank("TestBank", 0.1, 0.1, TEST_DIR);

        // Create a default account for test redundancy
        bank.createAccount("TestUser");
    }

    // PDF Requirement: Use @AfterEach to delete persisted files
    @AfterEach
    void tearDown() {
        cleanUpDirectory();
    }

    @Test
    void testConstructorAndPersistence() {
        File dir = new File(TEST_DIR);
        assertTrue(dir.exists(), "Directory should exist after creating bank");
        assertTrue(dir.isDirectory());
    }

    @Test
    void testCreateAccount() {
        File accountFile = new File(TEST_DIR, "Konto TestUser.json");
        assertTrue(accountFile.exists(), "JSON file should exist for the account");
    }

    @Test
    void testCreateDuplicateAccountThrowsException() {
        assertThrows(AccountAlreadyExistsException.class, () -> {
            bank.createAccount("TestUser"); // Should fail because setUp() created it
        });
    }

    @Test
    void testAddTransaction() throws Exception {
        Payment p = new Payment("01.01.2024", 100.0, "Test", 0.1, 0.1);
        bank.addTransaction("TestUser", p);

        assertTrue(bank.containsTransaction("TestUser", p));
        // Check if file contains the data (simple check)
        File f = new File(TEST_DIR, "Konto TestUser.json");
        assertTrue(f.length() > 0);
    }

    @Test
    void testRemoveTransaction() throws Exception {
        Payment p = new Payment("01.01.2024", 100.0, "Test", 0.1, 0.1);
        bank.addTransaction("TestUser", p);

        bank.removeTransaction("TestUser", p);
        assertFalse(bank.containsTransaction("TestUser", p));
    }

    @Test
    void testGetAccountBalance() throws Exception {
        // Payment 100 with 10% incoming interest = 90.0
        Payment p = new Payment("01.01.2024", 100.0, "Test", 0.1, 0.1);
        bank.addTransaction("TestUser", p);

        assertEquals(90.0, bank.getAccountBalance("TestUser"));
    }

    /**
     * Tests if data is correctly loaded from the file system.
     * This covers the readAccounts() requirement.
     */
    @Test
    void testPersistenceReload() throws Exception {
        // 1. Add data
        Payment p = new Payment("01.01.2024", 100.0, "ReloadTest", 0.1, 0.1);
        bank.addTransaction("TestUser", p);

        // 2. Create a NEW bank instance pointing to the SAME folder
        // This forces the "readAccounts" method to run
        PrivateBank reloadedBank = new PrivateBank("TestBank", 0.1, 0.1, TEST_DIR);

        // 3. Verify data was loaded back correctly
        assertTrue(reloadedBank.containsTransaction("TestUser", p), "Reloaded bank should contain the transaction from the file");
    }

    /**
     * Testet getAccountBalance mit einem Mix aus Payment, Incoming und Outgoing.
     */
    @Test
    void testGetAccountBalanceMixed() throws Exception {
        Payment payment = new Payment("01.01.2024", 100.0, "Deposit", 0.1, 0.1);
        IncomingTransfer incoming = new IncomingTransfer("02.01.2024", 50.0, "Gift", "Oma", "TestUser");
        OutgoingTransfer outgoing = new OutgoingTransfer("03.01.2024", 20.0, "Tax", "TestUser", "Opa");
        bank.addTransaction("TestUser", payment);
        bank.addTransaction("TestUser", incoming);
        bank.addTransaction("TestUser", outgoing);
        assertEquals(120.0, bank.getAccountBalance("TestUser"), 0.001);
    }

    /**
     * Testet getTransactionsSorted mit mindestens 3 Transaktionen.
     */
    @Test
    void testGetTransactionsSorted() throws Exception {
        Payment t1 = new Payment("01.01.2024", 100.0, "Big", 0.1, 0.1);
        IncomingTransfer t2 = new IncomingTransfer("02.01.2024", 10.0, "Small", "A", "B");
        OutgoingTransfer t3 = new OutgoingTransfer("03.01.2024", 50.0, "Negative", "B", "A");
        bank.addTransaction("TestUser", t1); // 90
        bank.addTransaction("TestUser", t2); // 10
        bank.addTransaction("TestUser", t3); // -50
        List<Transaction> sortedAsc = bank.getTransactionsSorted("TestUser", true);
        assertEquals(t3, sortedAsc.get(0)); // -50
        assertEquals(t2, sortedAsc.get(1)); // 10
        assertEquals(t1, sortedAsc.get(2)); // 90
        List<Transaction> sortedDesc = bank.getTransactionsSorted("TestUser", false);
        assertEquals(t1, sortedDesc.get(0)); // 90
        assertEquals(t2, sortedDesc.get(1)); // 10
        assertEquals(t3, sortedDesc.get(2)); // -50
    }
}