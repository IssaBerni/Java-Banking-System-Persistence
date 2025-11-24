package bank;

import java.util.*;
import java.io.*;
import java.nio.file.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import bank.exceptions.*;

/**
 * A concrete implementation of the {@link Bank} interface.
 * <p>
 * This class manages accounts and transactions in memory and persists them
 * to the local file system as JSON files using the Gson library.
 * Each account is stored in a separate file named "Konto [Name].json".
 */
public class PrivateBank implements Bank {

    /**
     * The name of the bank.
     */
    private String name;

    /**
     * The interest rate applied to incoming transactions (0.0 to 1.0).
     */
    private double incomingInterest;

    /**
     * The interest rate applied to outgoing transactions (0.0 to 1.0).
     */
    private double outgoingInterest;

    /**
     * The file system directory where account data is stored.
     */
    private String directoryName;

    /**
     * Internal storage mapping account names to lists of transactions.
     */
    private Map<String, List<Transaction>> accountsToTransactions = new HashMap<>();

    /**
     * Constructs a new PrivateBank and loads existing data from the specified directory.
     *
     * @param name             the name of the bank
     * @param incomingInterest the interest on deposits
     * @param outgoingInterest the interest on withdrawals
     * @param directoryName    the directory path for storing persistent data
     * @throws IOException if an error occurs while reading existing account files
     */
    public PrivateBank(String name, double incomingInterest, double outgoingInterest, String directoryName) throws IOException {
        this.name = name;
        this.incomingInterest = incomingInterest;
        this.outgoingInterest = outgoingInterest;
        this.directoryName = directoryName;
        readAccounts();
    }
    /**
     * Copy constructor.
     *
     * @param other the PrivateBank object to copy
     * @throws IOException if an error occurs while initializing storage
     */
    public PrivateBank(PrivateBank other) throws IOException {
        this(other.name, other.incomingInterest, other.outgoingInterest, other.directoryName);
    }
    /**
     * Persists a specific account to the file system.
     * <p>
     * Uses {@link TransactionSerializer} to handle polymorphic types.
     *
     * @param account the name of the account to write
     * @throws IOException if writing to the file fails
     */
    private void writeAccount(String account) throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Transaction.class, new TransactionSerializer())
                .setPrettyPrinting()
                .create();
        List<Transaction> transactions = accountsToTransactions.get(account);
        File dir = new File(directoryName);
        if (!dir.exists()) dir.mkdirs();
        String fileName = "Konto " + account + ".json";
        Path path = Paths.get(directoryName, fileName);
        java.lang.reflect.Type listType = new TypeToken<List<Transaction>>(){}.getType();
        String jsonString = gson.toJson(transactions, listType);
        Files.writeString(path, jsonString);
    }
    /**
     * Reads all account files from the configured directory and populates the bank.
     * Only files matching the pattern "Konto [Name].json" are processed.
     *
     * @throws IOException if reading the files fails
     */
    private void readAccounts() throws IOException {
        File dir = new File(directoryName);
        if (!dir.exists()) return;
        File[] files = dir.listFiles();
        if (files == null) return;
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Transaction.class, new TransactionSerializer())
                .create();
        for (File file : files) {
            String fileName = file.getName();
            if (fileName.startsWith("Konto ") && fileName.endsWith(".json")) {
                String account = fileName.substring(6, fileName.length() - 5);
                String content = Files.readString(file.toPath());
                java.lang.reflect.Type listType = new TypeToken<List<Transaction>>(){}.getType();
                List<Transaction> transactionList = gson.fromJson(content, listType);
                accountsToTransactions.put(account, transactionList);
            }
        }
    }

    @Override
    public void createAccount(String account) throws AccountAlreadyExistsException, IOException {
        if (accountsToTransactions.containsKey(account)) {
            throw new AccountAlreadyExistsException("Account already exists: " + account);
        }
        accountsToTransactions.put(account, new ArrayList<>());
        writeAccount(account);
    }

    @Override
    public void createAccount(String account, List<Transaction> transactions)
            throws AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException, IOException {
        if (accountsToTransactions.containsKey(account)) {
            throw new AccountAlreadyExistsException("Account already exists: " + account);
        }
        List<Transaction> transactionList = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getAmount() < 0) throw new TransactionAttributeException("Invalid negative amount in creation");
            if (transactionList.contains(t)) throw new TransactionAlreadyExistException("Duplicate transaction detected");
            transactionList.add(t);
        }
        accountsToTransactions.put(account, transactionList);
        writeAccount(account);
    }

    @Override
    public void addTransaction(String account, Transaction transaction)
            throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException, IOException {
        if (!accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException("Account not found: " + account);
        }
        List<Transaction> transactionList = accountsToTransactions.get(account);

        if (transactionList.contains(transaction)) {
            throw new TransactionAlreadyExistException("Transaction already exists");
        }

        if (transaction instanceof Payment) {
            ((Payment) transaction).setIncomingInterest(this.incomingInterest);
            ((Payment) transaction).setOutgoingInterest(this.outgoingInterest);
        }

        transactionList.add(transaction);
        writeAccount(account);
    }

    @Override
    public void removeTransaction(String account, Transaction transaction)
            throws AccountDoesNotExistException, TransactionDoesNotExistException, IOException {
        if (!accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException("Account not found: " + account);
        }
        List<Transaction> transactionList = accountsToTransactions.get(account);

        if (!transactionList.contains(transaction)) {
            throw new TransactionDoesNotExistException("Transaction not found");
        }

        transactionList.remove(transaction);
        writeAccount(account);
    }

    @Override
    public boolean containsTransaction(String account, Transaction transaction) {
        if (!accountsToTransactions.containsKey(account)) return false;
        return accountsToTransactions.get(account).contains(transaction);
    }

    @Override
    public double getAccountBalance(String account) {
        double balance = 0.0;
        List<Transaction> list = accountsToTransactions.get(account);
        if (list != null) {
            for (Transaction t : list) balance += t.calculate();
        }
        return balance;
    }

    @Override
    public List<Transaction> getTransactions(String account) {
        List<Transaction> list = accountsToTransactions.get(account);
        return list != null ? new ArrayList<>(list) : new ArrayList<>();
    }

    @Override
    public List<Transaction> getTransactionsSorted(String account, boolean asc) {
        List<Transaction> list = getTransactions(account);
        list.sort((t1, t2) -> asc ? Double.compare(t1.calculate(), t2.calculate())
                : Double.compare(t2.calculate(), t1.calculate()));
        return list;
    }

    @Override
    public List<Transaction> getTransactionsByType(String account, boolean positive) {
        List<Transaction> list = getTransactions(account);
        List<Transaction> filtered = new ArrayList<>();
        for (Transaction t : list) {
            if ((positive && t.calculate() > 0) || (!positive && t.calculate() < 0)) {
                filtered.add(t);
            }
        }
        return filtered;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getIncomingInterest() { return incomingInterest; }
    public void setIncomingInterest(double i) { this.incomingInterest = i; }
    public double getOutgoingInterest() { return outgoingInterest; }
    public void setOutgoingInterest(double i) { this.outgoingInterest = i; }
    public String getDirectoryName() { return directoryName; }
    public void setDirectoryName(String d) { this.directoryName = d; }

    @Override
    public String toString() {
        return "PrivateBank{name='" + name + "', directory='" + directoryName + "', accounts=" + accountsToTransactions + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrivateBank that = (PrivateBank) o;
        return Double.compare(that.incomingInterest, incomingInterest) == 0 &&
                Double.compare(that.outgoingInterest, outgoingInterest) == 0 &&
                Objects.equals(name, that.name) &&
                Objects.equals(directoryName, that.directoryName) &&
                Objects.equals(accountsToTransactions, that.accountsToTransactions);
    }
}