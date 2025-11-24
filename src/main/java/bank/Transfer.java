package bank;

import bank.exceptions.*;


/**
 * Es handelt sich um eine Banküberweisung ohne Zinsen.
 * Enthält Sender und Empfänger der Überweisung
 * Erbt von Transaction und implementiert CalculateBill.
 */
public class Transfer extends Transaction {

    //private String date; //  Datum mit DD.MM.YYYY Format
    //private double amount; // Geldmenge einer Ein- oder Auszahlung (nur positiv Wert)
    //private String description; //beschreibung von Payment

    /**
     * Wer der Transfer gemacht hat
     */
    private String sender;

    /**
     * Wer bekommt das Geld
     */
    private String recipient;


    //Getter und Settermethoden

    //DATE
    /*bekommen wir die Datum
    public String getDate() {
        return date; //bek
    }
    //setzen wir die Datum
    public void setDate(String date) {
        this.date = date;
    }*/

    //AMOUNT
    /*bekommen wir die Anzahl
    public double getAmount() {
        return amount;
    }*/
    //setzen wir die Anzahl mit nur positiven Werten


    /**
     * Setzen wir den Wert von Amount
     * Falls amount negativ ist, dann ist mit 0 initializiert
     *
     * @param amount
     */
    @Override
    public void setAmount(double amount) throws TransactionAttributeException{
        if (amount < 0){
            //System.out.println("nur positiven Werten");
            //this.amount = 0;
            throw new TransactionAttributeException("falsche");
        }
        else
        {this.amount = amount;}
    }

    //DESCRIPTION
    /*bekommen wir die Description
    public String getDescription() {
        return description;
    }
    //setzen wir die Description
    public void setDescription(String description) {
        this.description = description;
    }*/

    //SENDER
    /**
     * bekommen wir die SENDER
     *
     * @return String von Sender
     */
    public String getSender(){
        return sender;
    }

    /**
     * setzen wir die SENDER
     *
     * @param sender
     */
    public void setSender(String sender) {
        this.sender = sender;
    }


    //recipient
    /**
     * bekommen wir die recipient
     *
     * @return String von recipient
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * setzen wir die recipient
     *
     * @param recipient
     */
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }



    //KONSTRUKTOREN


    /**
     * Konstruktor für die Attribute date, amount und description.
     * benutzt super, dh Konstruktor von Class Transaction
     * Falls amount negativ ist, dann ist mit 0 initializiert
     *
     * @param date
     * @param amount
     * @param description
     */
    public Transfer(String date, double amount, String description) throws TransactionAttributeException{

        super(date, amount, description);
        this.setAmount(amount);

    }

    /**
     * Konstruktor für alle Attribute.
     * benutzen wir Teil der andere Konstruktor plus andere Attributen
     *
     * @param date
     * @param amount
     * @param description
     * @param sender
     * @param recipient
     */
    public Transfer (String date, double amount, String description, String sender, String recipient) throws TransactionAttributeException{

        this(date, amount, description);

        this.sender = sender;
        this.recipient = recipient;

    }

    //Copy-Konstruktor
    /**
     * Dieser Konstruktor kopiert die Attribute eines bereits vorhandenen Objekts und verwendet sie für das neue Objekt.
     *
     * @param andere Objekt
     */
    public Transfer (Transfer andere) throws TransactionAttributeException{

        this(andere.date, andere.amount, andere.description, andere.sender, andere.recipient);

    }

    /**
     * Methode zum Drucken den Inhalt aller Klassenattribute des Objekts
     */
    public void printObject(){

        System.out.println("Date: " + getDate());
        System.out.println("Amount: " + getAmount());
        System.out.println("Description: " + getDescription());
        System.out.println("Sender: " + getSender());
        System.out.println("Recipient: " + getRecipient());
        System.out.println();

    }

    /**
     * @return Wert ohne Änderung von Amount
     */
    @Override
    public double calculate() {
        return amount;
    }

    /**
     * @return einen String mit allen Werten der Attribute der Klasse
     */
    @Override
    public String toString() {
        return ( "Transfer...  " + super.toString() +
                 " , Sender: " + this.sender +
                 " , Recipient: " + this.recipient


                );
    }

    /**
     * Checks if two Transfer objects are equal.
     * Compares superclass fields (Transaction) as well as Sender and Recipient.
     *
     * @param obj the reference object with which to compare
     * @return true if attributes match, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        Transfer other = (Transfer) obj;
        boolean senderMatch = (this.sender == null) ? (other.sender == null) : this.sender.equals(other.sender);
        boolean recipientMatch = (this.recipient == null) ? (other.recipient == null) : this.recipient.equals(other.recipient);
        return senderMatch && recipientMatch;
    }
    @Override
    public String error(){

        return "Error in Transfer";
    }
}




