package bank;

import bank.exceptions.TransactionAttributeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TransferTest {

    private Transfer transfer;
    private IncomingTransfer incoming;
    private OutgoingTransfer outgoing;

    @BeforeEach
    void setUp() throws TransactionAttributeException {
        transfer = new Transfer("01.01.2024", 100.0, "Standard", "Sender", "Receiver");
        incoming = new IncomingTransfer("01.01.2024", 100.0, "Incoming", "Sender", "Me");
        outgoing = new OutgoingTransfer("01.01.2024", 100.0, "Outgoing", "Me", "Receiver");
    }

    @Test
    void testConstructor() {
        assertEquals("Sender", transfer.getSender());
        assertEquals("Receiver", transfer.getRecipient());
        assertEquals(100.0, transfer.getAmount());
    }

    @Test
    void testCopyConstructor() throws TransactionAttributeException {
        Transfer copy = new Transfer(transfer);
        assertEquals(transfer, copy);
        assertNotSame(transfer, copy);
    }

    // PDF Requirement: Test calculate for Incoming/Outgoing
    @Test
    void testCalculate() {
        // Standard Transfer: returns amount unchanged
        assertEquals(100.0, transfer.calculate());

        // IncomingTransfer: returns positive amount
        assertEquals(100.0, incoming.calculate());

        // OutgoingTransfer: returns NEGATIVE amount
        assertEquals(-100.0, outgoing.calculate());
    }

    @Test
    void testEquals() throws TransactionAttributeException {
        Transfer same = new Transfer("01.01.2024", 100.0, "Standard", "Sender", "Receiver");
        assertEquals(transfer, same);

        Transfer diff = new Transfer("01.01.2024", 50.0, "Diff", "A", "B");
        assertNotEquals(transfer, diff);
    }

    @Test
    void testToString() {
        String str = transfer.toString();
        assertTrue(str.contains("100.0"));
        assertTrue(str.contains("Sender"));
        assertTrue(str.contains("Receiver"));
    }

    @Test
    void testNegativeAmountThrowsException() {
        assertThrows(TransactionAttributeException.class, () -> {
            new Transfer("Date", -10.0, "Desc", "A", "B");
        });
    }
}