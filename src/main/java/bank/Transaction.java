package bank;

import bank.exceptions.TransactionAttributeException;

/**
 * Abstrakte Klasse, die eine generische Banktransaktion darstellt.
 * Enthält die gemeinsamen Attribute aller Transaktionen wie Datum, Beschreibung und Amount
 * Diese Attribute werden als „protected“ deklariert, damit sie direkt von den Unterklassen,
 * die von Transaction erben, aufgerufen werden können..
 */
abstract class Transaction implements CalculateBill {

    /**
     * Datum mit DD.MM.YYYY Format
     */
    protected String date;

    /**
     * Geldmenge einer Ein- oder Auszahlung (nur positiv Wert)
     */
    protected double amount;

    /**
     * //beschreibung von Payment
     */
    protected String description;



    //Getter und Settermethoden

    //DATE
    /**
     * bekommen wir die Datum
     *
     * @return Datum
     */
    public String getDate() {
        return date;
    }

    /**
     * setzen wir die Datum
     *
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }


    //AMOUNT
    /**
     * bekommen wir die Anzahl von Payment
     *
     * @return Amount
     */
    public double getAmount() {
        return amount;
    }


    /**
     * setzen wir die Anzahl von Payment
     *
     * @param amount
     */
    public void setAmount(double amount) throws TransactionAttributeException {
        this.amount = amount;
    }

    //DESCRIPTION

    /**
     * bekommen wir die Description
     *
     * @return Description
     */
    public String getDescription() {
        return description;
    }

    /**
     * setzen wir die Description
     *
     * @param description
     */
    public void setDescription(String description) {

        this.description = description;
    }

    //KONSTRUKTOR
    /**
     * Konstruktor für die Attribute date, amount und description.
     *
     * @param date
     * @param amount
     * @param description
     */
    public Transaction(String date, double amount, String description){

        this.date = date;
        this.amount = amount;
        this.description = description;

    }


    /**
     * @return  String mit Werten von Date, Amount und Description
     */
    @Override
    public String toString(){
        return (    "Date: " + this.date +
                    ", Amount: " + this.calculate() +
                    " , Description: " + this.description
                );
    }
    /**
     * Checks if two transactions are equal based on their values.
     * Note: This method compares content (Strings), not object references.
     *
     * @param obj the reference object with which to compare
     * @return true if this object is the same as the obj argument; false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Transaction other = (Transaction) obj;

        boolean dateMatch = (this.date == null) ? (other.date == null) : this.date.equals(other.date);
        boolean descMatch = (this.description == null) ? (other.description == null) : this.description.equals(other.description);

        return (Double.compare(this.amount, other.amount) == 0 &&
                dateMatch &&
                descMatch &&
                Double.compare(this.calculate(), other.calculate()) == 0
        );
    }
}
