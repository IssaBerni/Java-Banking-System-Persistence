package bank;

import bank.exceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrivateBankAlt implements Bank{


    private String name;
    private double incomingInterest;
    private double outgoingInterest;
    private Map<String, List<Transaction>> accountsToTransactions = new HashMap<>();


    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public double getIncomingInterest() {
        return incomingInterest;
    }

    public void setIncomingInterest(double incomingInterest) {
        this.incomingInterest = incomingInterest;
    }


    public double getOutgoingInterest() {
        return outgoingInterest;
    }

    public void setOutgoingInterest(double outgoingInterest) {
        this.outgoingInterest = outgoingInterest;
    }

    public PrivateBankAlt (String name, double incomingInterest, double outgoingInterest){
        this.name = name;
        this.incomingInterest = incomingInterest;
        this.outgoingInterest = outgoingInterest;
    }

    public PrivateBankAlt (PrivateBankAlt anderesbank){
        this(anderesbank.name, anderesbank.incomingInterest, anderesbank.outgoingInterest);
    }

    @Override
    public String toString() {
        return (    "PrivateBankAlt: \n" +
                "Name= " + name + "---" +
                "IncomingInterest= " + incomingInterest + "---" +
                "OutgoingInterest= " + outgoingInterest + "---" +
                "AccountsToTransactions= " + accountsToTransactions
        );
    }

    @Override
    public boolean equals(Object obj) {
        PrivateBankAlt other = (PrivateBankAlt) obj;
        return (this.name.equals(other.name) &&
                this.incomingInterest == other.incomingInterest &&
                this.outgoingInterest == other.outgoingInterest &&
                this.accountsToTransactions.equals(other.accountsToTransactions)
        );
    }


    @Override
    public void createAccount(String account) throws AccountAlreadyExistsException {
        if(accountsToTransactions.containsKey(account)){
            throw new AccountAlreadyExistsException("Die Konto " + account + " schon existiert.");
        }
        accountsToTransactions.put(account, new ArrayList<>());
    }

    @Override
    public void createAccount(String account, List<Transaction> transactions)
            throws AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException {

        if(accountsToTransactions.containsKey(account)){
            throw new AccountAlreadyExistsException("Die Konto " + account + " schon existiert.");
        }

        List<Transaction> transactionList = new ArrayList<>();

        for (Transaction t : transactions) {
            if (t.getAmount() < 0) { // nur positiven WErten
                throw new TransactionAttributeException("Ungültiger Betrag: " + t.getAmount());
            }
            if (transactionList.contains(t)) { // Duplikate prufen
                throw new TransactionAlreadyExistException("Transaktion existiert bereits in der neue Konto");
            }
            transactionList.add(t); //alles gultig dann in der Liste addieren
        }

        accountsToTransactions.put(account, transactionList);
    }


    @Override
    public void addTransaction(String account, Transaction transaction)
            throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException {

        if (!accountsToTransactions.containsKey(account)) { // Prufung ob der Konto existiert
            throw new AccountDoesNotExistException("Konto '" + account + "' existiert nicht.");
        }
        List<Transaction> transactionList = accountsToTransactions.get(account); //Die liste von diese Konto bekommen (keine Kopie)

        if (transaction.getAmount() < 0) { // Exception fur ungulige Werten
            throw new TransactionAttributeException("Ungültiger Betrag: " + transaction.getAmount());
        }

        if (transaction instanceof Payment) { // wir sehen ob die Transaction ein Payment ist
            Payment p = (Payment) transaction;
            p.setIncomingInterest(this.incomingInterest);
            p.setOutgoingInterest(this.outgoingInterest);
        }

        if (transactionList.contains(transaction)) { // Throw Exeption wenn die Transaction schon in der Konto existiert
            throw new TransactionAlreadyExistException("Transaktion existiert bereits in diesem Konto");
        }

        transactionList.add(transaction); //Transaction in der Liste von diese Konto hinzufugen

    }

    @Override
    public void removeTransaction(String account, Transaction transaction)
            throws AccountDoesNotExistException, TransactionDoesNotExistException {

        if (!accountsToTransactions.containsKey(account)) { // Prufung ob der Konto existiert
            throw new AccountDoesNotExistException("Konto '" + account + "' existiert nicht.");
        }
        List<Transaction> transactionList = accountsToTransactions.get(account); //Die liste von diese Konto bekommen (keine Kopie)
        if (!transactionList.contains(transaction)) {
            throw new TransactionDoesNotExistException("Transaktion existiert nicht in diesem Konto.");
        }
        transactionList.remove(transaction); // existiert die Konto und Transaction, dann wird diese geloscht

    }


    @Override
    public boolean containsTransaction(String account, Transaction transaction) {

        if (!accountsToTransactions.containsKey(account)) {
            return false;
        }

        List<Transaction> transactionList = accountsToTransactions.get(account);
        return transactionList.contains(transaction);

    }

    @Override
    public double getAccountBalance(String account) {

        double balance = 0.0;
        List<Transaction> transList = accountsToTransactions.get(account);

        if (transList != null) {
            for (Transaction t : transList) {

                if (t instanceof Transfer) {                // ist Transfer
                    Transfer tr = (Transfer) t;            // Cast nach Transfer
                    if (tr.getSender().equals(account)) {  // wenn diese account ist Sender
                        balance -= tr.getAmount();        // minus
                    } else if (tr.getRecipient().equals(account)) { // diese account Recipient
                        balance += tr.getAmount();        // plus
                    }
                } else {  // andere Transactions
                    balance += t.getAmount();             // nur Amount addieren
                }

            }
        }

        return balance;

    }

    @Override
    public List<Transaction> getTransactions(String account) {

        List<Transaction> transactionList = accountsToTransactions.get(account);

        if (transactionList != null) {
            return new ArrayList<>(transactionList); // eine Kopie geben
        } else {
            return new ArrayList<>(); // leere List
        }

    }

    @Override
    public List<Transaction> getTransactionsSorted(String account, boolean asc) {


        List<Transaction> transList = accountsToTransactions.get(account); //List bekommen

        if (transList == null) {
            return new ArrayList<>(); // leere Liste
        }

        List<Transaction> sortedList = new ArrayList<>(transList); // eine Kopie machen um die Original nicht zu wechseln

        sortedList.sort((t1, t2) -> {
            return asc  ? Double.compare(t1.calculate(), t2.calculate())  // asc wenn asc true ist
                        : Double.compare(t2.calculate(), t1.calculate()); // desc wenn falsch
            }
        );

        return sortedList;
    }

    @Override
    public List<Transaction> getTransactionsByType(String account, boolean positive) {

        List<Transaction> transList = accountsToTransactions.get(account);// List bekommen

        if (transList == null) {
            return new ArrayList<>(); // leere Liste
        }

        List<Transaction> filteredList = new ArrayList<>(); //neue List wo nur entweder positiv oder negativ Werten haben
        for (Transaction t : transList) { //jeder Transaction sehen
            if (positive && t.calculate() > 0) { //positive True ist und wert positiv ist dann hinzufugen
                filteredList.add(t);
            } else if (!positive && t.calculate() < 0) { // False und negatives Wert
                filteredList.add(t);
            }
        }

        return filteredList;
    }
}
