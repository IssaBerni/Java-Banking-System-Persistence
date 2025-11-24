package bank;

import bank.exceptions.TransactionAttributeException;

/**
 * SubKlasse von Transfer
 * mit Konstruktor und
 * eigenes calculate Methode um die Balance zu berechnen
 */
public class IncomingTransfer extends Transfer{


    /**
     * Kontruktor
     *
     * @param date
     * @param amount
     * @param description
     * @param sender
     * @param recipient
     * @throws TransactionAttributeException
     */
    public IncomingTransfer(String date, double amount, String description, String sender, String recipient)
            throws TransactionAttributeException {

        super(date, amount, description, sender, recipient);
    }

}
