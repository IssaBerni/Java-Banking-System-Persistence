package bank;

import bank.exceptions.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws TransactionAttributeException, TransactionAlreadyExistException, AccountDoesNotExistException, AccountAlreadyExistsException, IOException {

        //Payment Objekten

        Payment EIN = new Payment("11.10.2004", -1000, "Handy");
        Payment ZWEI= new Payment("10.10.2005", 10000, "Haus", 0.1 , 0.1);
        System.out.println("--------Payment Objekten------");
        EIN.printObject();
        EIN.setIncomingInterest(0.1); EIN.setOutgoingInterest(0.1);
        EIN.printObject();
        ZWEI.printObject();

        Payment DREI= new Payment(EIN);

        //Falsche Eingabe bei der Initialisation von Objekt
        System.out.println("Falsche Eingabe:");
        Transfer Tein = new Transfer("09.01.2004", 25 , "Essen");

        //Konstruktor 2
        Transfer Tzwei = new Transfer("01.01.2001", 923, "Januar", "Vater", "Daniel");

        //Copy-Konstruktor
        Transfer Tdrei = new Transfer(Tein);

        //print Objekten
        System.out.println("--------Transfer Objekten------");
        Tein.printObject();
        Tzwei.printObject();
        Tdrei.printObject();

        // Prak 2 Calculations
        System.out.println("----Calculate---");
        System.out.println("Amount von diese Payment ist: " + EIN.getAmount());
        System.out.println("Nach OutgoingIneterest: " + EIN.calculate());
        System.out.println("----ToString---");
        System.out.println(EIN);

        System.out.println("----Equals---");
        System.out.println( "Ist Payment \"EIN\" gleich Payment \"DREI\"?: " + EIN.equals(DREI));
        System.out.println( "Ist Transfer \"Tein\" gleich Transfer \"DREI\"?: " + Tein.equals(Tdrei));


        // PRAKTIKUM 4

        System.out.println("\n\n----PrivateBank---\n");
        PrivateBank erstesBank = new PrivateBank("BCP", 0.1,0.1, "BankData");
        erstesBank.createAccount("Daniel"); // Saves to "BankData/Konto Daniel.json"
        IncomingTransfer T1BCP = new IncomingTransfer("01.01.2025", 1000, "hallo", "bob", "Daniel");
        OutgoingTransfer T2BCP = new OutgoingTransfer("01.01.2024", 250, "bye", "Daniel", "bob");
        erstesBank.addTransaction("Daniel", T1BCP);
        erstesBank.addTransaction("Daniel", T2BCP);
        System.out.println("Balance: " + erstesBank.getAccountBalance("Daniel"));
        System.out.println("Transactions: " + erstesBank.getTransactions("Daniel"));
        System.out.println("Sorted: " + erstesBank.getTransactionsSorted("Daniel", false));
        System.out.println("Positive Only: " + erstesBank.getTransactionsByType("Daniel", true));
        PrivateBank Kopie = new PrivateBank(erstesBank);
        System.out.println("Equals Copy: " + Kopie.equals(erstesBank));
        try{
            erstesBank.createAccount("Daniel");
        } catch (AccountAlreadyExistsException e){
            System.out.println("Expected Error: " + e.getMessage());
        }
    }
}