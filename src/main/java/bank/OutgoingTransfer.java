package bank;

import bank.exceptions.TransactionAttributeException;

/**
 * SubKlasse von Transfer
 * mit Konstruktor und
 * eigenes calculate Methode um die Balance zu berechnen
 */
public class OutgoingTransfer extends Transfer{


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
    public OutgoingTransfer(String date, double amount, String description, String sender, String recipient)
    throws TransactionAttributeException {

        super(date, amount, description, sender, recipient);
    }


    /**
     * @return Amount von Transfer, wegen Outoging dann negativ
     */
    @Override
    public double calculate() {

        return -super.calculate();
    }
}
