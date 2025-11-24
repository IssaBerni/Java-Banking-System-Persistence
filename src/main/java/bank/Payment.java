package bank;

import bank.exceptions.*;

/**
 * Stellt eine Zahlung im Bankensystem dar, sei es eine Einzahlung oder eine Auszahlung.
 * Erbt von Transaction und implementiert CalculateBill, um den angepassten Betrag zu berechnen.
 */
public class Payment extends Transaction {

    //private String date; // Datum mit DD.MM.YYYY Format
    //private double amount; // Geldmenge einer Ein- oder Auszahlung (positiv oder negativ Werten)
    //private String description; //beschreibung von Payment

    /**
     * Anzahl von Interest zwischen 0 un 1
     */
    private double incomingInterest;

    /**
     * Anzahl von Interest zwischen 0 un 1
     */
    private double outgoingInterest;



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

    /*bekommen wir die Anzahl von Payment
    public double getAmount() {
        return amount;
    }

    //setzen wir die Anzahl von Payment
    public void setAmount(double amount) {
        this.amount = amount;
    }*/


    //DESCRIPTION

    /*bekommen wir die Description
    public String getDescription() {
        return description;
    }
    //setzen wir die Description
    public void setDescription(String description) {

        this.description = description;
    }*/



    //incomingInterest

    /**
     * bekommen wir die IncomingInterest
     *
     * @return wert von incomingInterest von 0 bis 1
     */
    public double getIncomingInterest() {

        return incomingInterest;
    }

    /**
     * setzen wir die IncomingInterest
     *
     * @param incomingInterest
     */
    public void setIncomingInterest(double incomingInterest) throws TransactionAttributeException{
        if(incomingInterest<0 || incomingInterest>1){
            System.out.println("Interest nur zwischen 0 und 1");
            System.out.println();
            this.incomingInterest = 0;
            throw new TransactionAttributeException("falsche");

        }
        else
            this.incomingInterest = incomingInterest;
    }



    //outgoingInterest

    /**
     * bekommen wir die outgoingInterest
     *
     * @return wert von outgoingInterest von 0 bis 1
     */
    public double getOutgoingInterest() {
        return outgoingInterest;
    }


    /**
     * setzen wir die outgoingInterest
     *
     * @param outgoingInterest
     */
    public void setOutgoingInterest(double outgoingInterest) throws  TransactionAttributeException{
        if(outgoingInterest<0 || outgoingInterest>1){
            System.out.println("Interest nur zwischen 0 und 1");
            System.out.println();
            this.outgoingInterest = 0;
            throw new TransactionAttributeException("falsche");
        }
        else
            this.outgoingInterest = outgoingInterest;
    }


    //KONSTRUKTOREN


    /**
     * Konstruktor für die Attribute date, amount und description.
     * benutzt super, dh Konstruktor von Class Transaction
     *
     * @param date
     * @param amount
     * @param description
     */
    public Payment(String date, double amount, String description){

        super(date, amount, description);

    }

    /**
     * Konstruktor für alle Attribute.
     * benutzen wir Teil der andere Konstruktor plus andere Attributen
     *
     * @param date
     * @param amount
     * @param description
     * @param incomingInterest
     * @param outgoingInterest
     */
    public Payment (String date, double amount, String description, double incomingInterest, double outgoingInterest) throws TransactionAttributeException{

        this(date, amount, description);
        this.setIncomingInterest(incomingInterest);
        this.setOutgoingInterest(outgoingInterest);

    }


    //Copy-Konstruktor
    /**
     *
     * Dieser Konstruktor kopiert die Attribute eines bereits vorhandenen Objekts und verwendet sie für das neue Objekt.
     *
     * @param andere Objekt
     */
    public Payment (Payment andere) throws TransactionAttributeException{

        this(andere.date, andere.amount, andere.description, andere.incomingInterest, andere.outgoingInterest);


    }


    /**
     * Methode zum Drucken den Inhalt aller Klassenattribute des Objekts
     */
    public void printObject(){

        System.out.println("Date: " + getDate());
        System.out.println("Amount: " + getAmount());
        System.out.println("Description: " + getDescription());
        System.out.println("IncomingInterest: " + getIncomingInterest());
        System.out.println("outgoingInterest: " + getOutgoingInterest());
        System.out.println();
    }


    /**
     * Berechnet der Amount der Payment nach der Incoming oder Outgoing Interest
     *
     * @return Wert von Amount nach Interest
     * @return 0 wenn Amount 0 ist
     */
    @Override
    public double calculate() {
        double geldmitincominginterest, geldmitoutgoinginterest;
        if (amount > 0){
            geldmitincominginterest = amount- amount*incomingInterest;
            //System.out.println("Nach IncomingInterest: "+ geldmitincominginterest);
            return geldmitincominginterest;
        }
        else if (amount < 0){
            geldmitoutgoinginterest = amount + amount*outgoingInterest;
            //System.out.println("Nach OutgoingIneterest "+ geldmitoutgoinginterest);
            return geldmitoutgoinginterest;
        }
        else
            return 0;

    }

    /**
     * @return  einen String mit allen Werten der Attribute der Klasse .
     */
    @Override
    public String toString() {
        return ("Payment...  " + super.toString() +
                " , IncomingInterest: " + this.incomingInterest +
                " , OutgoingInterest: " + this.outgoingInterest

                );
    }

    /**
     * Methode zum Vergleichen, ob zwei Klassen gleich sind
     *
     * @param obj the reference object with which to compare.
     * @return True wenn beide Klassen gleich sind
     * @return False wenn nicht gleich sind
     */
    @Override
    public boolean equals(Object obj) {
        Payment other = (Payment) obj;
        return (super.equals(other) &&
                this.incomingInterest == other.incomingInterest &&
                this.outgoingInterest == other.outgoingInterest
        );
    }

    @Override
    public String error(){

        return "Error in Payment";
    }
}

