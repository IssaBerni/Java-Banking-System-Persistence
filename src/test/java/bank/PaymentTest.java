package bank;

import bank.exceptions.TransactionAttributeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {

    private Payment payment;

    @BeforeEach
    void setUp() throws TransactionAttributeException {
        // Amount: 100, IncomingInterest: 10% (0.1), OutgoingInterest: 20% (0.2)
        payment = new Payment("12.03.2024", 100.0, "Test Payment", 0.1, 0.2);
    }

    @Test
    void testConstructor() {
        assertEquals("12.03.2024", payment.getDate());//(we have, expected)
        assertEquals(100.0, payment.getAmount());
        assertEquals("Test Payment", payment.getDescription());
        assertEquals(0.1, payment.getIncomingInterest());
        assertEquals(0.2, payment.getOutgoingInterest());
    }

    @Test
    void testCopyConstructor() throws TransactionAttributeException {
        Payment copy = new Payment(payment);
        assertEquals(payment, copy); // we use .equal() if they have the same name and money
        assertNotSame(payment, copy); // Different objects in memory
    }

    @Test
    void testCalculateIncoming() {
        // Formula: amount - (amount * incomingInterest)
        // 100 - (100 * 0.1) = 90.0
        assertEquals(90.0, payment.calculate(), 0.001);
    }

    @Test
    void testCalculateOutgoing() throws TransactionAttributeException {
        // Formula: amount + (amount * outgoingInterest)
        // -100 + (-100 * 0.2) = -120.0
        Payment outgoing = new Payment("12.03.2024", -100.0, "Outgoing", 0.1, 0.2);
        assertEquals(-120.0, outgoing.calculate(), 0.001);
    }

    @Test
    void testEquals() throws TransactionAttributeException {
        Payment same = new Payment("12.03.2024", 100.0, "Test Payment", 0.1, 0.2);
        Payment different = new Payment("12.03.2024", 50.0, "Different", 0.1, 0.2);

        assertEquals(payment, same);
        assertNotEquals(payment, different);
    }

    @Test
    void testToString() {
        String result = payment.toString();
        // Transaction.toString() calls calculate(), so we expect 90.0, NOT 100.0
        assertTrue(result.contains("90.0"));
        assertTrue(result.contains("Test Payment"));
        assertTrue(result.contains("0.1"));
    }

    @ParameterizedTest
    @ValueSource(doubles = {1.1, -0.1, 2.0, -5.0})
    void testInvalidInterestThrowsException(double badInterest) {
        assertThrows(TransactionAttributeException.class, () -> {
            new Payment("Date", 100, "Desc", badInterest, 0.1);
        });
    }
}