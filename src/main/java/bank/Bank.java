package bank;

import bank.exceptions.*;
import java.io.IOException;
import java.util.List;

/**
 * Interface for a generic bank.
 * Provides methods for managing accounts and transactions.
 * <p>
 * Note: Methods that modify data may throw {@link IOException} due to persistence requirements.
 */
public interface Bank {

    /**
     * Creates a new account with the given name.
     *
     * @param account the name of the account to be created
     * @throws AccountAlreadyExistsException if an account with this name already exists
     * @throws IOException                   if an error occurs while saving the account to the file system
     */
    void createAccount(String account) throws AccountAlreadyExistsException, IOException;

    /**
     * Creates a new account with the given name and a list of initial transactions.
     *
     * @param account      the name of the account to be created
     * @param transactions a list of transactions to be added to the new account
     * @throws AccountAlreadyExistsException    if an account with this name already exists
     * @throws TransactionAlreadyExistException if any transaction in the list is a duplicate
     * @throws TransactionAttributeException    if validation of transaction attributes fails
     * @throws IOException                      if an error occurs while saving the account to the file system
     */
    void createAccount(String account, List<Transaction> transactions)
            throws AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException, IOException;

    /**
     * Adds a transaction to an existing account.
     *
     * @param account     the name of the account
     * @param transaction the transaction object to add
     * @throws TransactionAlreadyExistException if the transaction already exists in the account
     * @throws AccountDoesNotExistException     if the specified account does not exist
     * @throws TransactionAttributeException    if validation of transaction attributes fails
     * @throws IOException                      if an error occurs while saving the changes to the file system
     */
    void addTransaction(String account, Transaction transaction)
            throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException, IOException;

    /**
     * Removes a transaction from an existing account.
     *
     * @param account     the name of the account
     * @param transaction the transaction object to remove
     * @throws AccountDoesNotExistException     if the specified account does not exist
     * @throws TransactionDoesNotExistException if the transaction is not found in the account
     * @throws IOException                      if an error occurs while saving the changes to the file system
     */
    void removeTransaction(String account, Transaction transaction)
            throws AccountDoesNotExistException, TransactionDoesNotExistException, IOException;

    /**
     * Checks if a transaction exists within a specific account.
     *
     * @param account     the name of the account
     * @param transaction the transaction to search for
     * @return true if the transaction exists, false otherwise
     */
    boolean containsTransaction(String account, Transaction transaction);

    /**
     * Calculates the total balance of an account.
     *
     * @param account the name of the account
     * @return the sum of all transaction values
     */
    double getAccountBalance(String account);

    /**
     * Retrieves all transactions for a specific account.
     *
     * @param account the name of the account
     * @return a list of all transactions
     */
    List<Transaction> getTransactions(String account);

    /**
     * Retrieves transactions for an account, sorted by calculated amount.
     *
     * @param account the name of the account
     * @param asc     true for ascending order, false for descending
     * @return a sorted list of transactions
     */
    List<Transaction> getTransactionsSorted(String account, boolean asc);

    /**
     * Retrieves transactions for an account, filtered by type (positive or negative).
     *
     * @param account  the name of the account
     * @param positive true to return only positive transactions (incoming), false for negative (outgoing)
     * @return a filtered list of transactions
     */
    List<Transaction> getTransactionsByType(String account, boolean positive);
}