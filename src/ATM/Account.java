package ATM;

import java.util.ArrayList;

public class Account {

    /**
     * Имя  аккаунта
     */
    private String name;

    /**
     * Уникальный номер аккаунт
     */
    private String id;

    /**
     * Держатель этого аккаунта
     */
    private User holder;

    /**
     * Список транзакций для этого аккаунта
     */
    private ArrayList<Transaction> transactions;


    /**
     * Создание новой учетной записи
     * @param name      Имя учетной записи
     * @param holder    Владелец учетной записи
     * @param theBank   Банк который выдает счет
     */
    public Account(String name, User holder, Bank theBank) {

        // Установим имя учетной записи и держателя
        this.name = name;
        this.holder = holder;

        // Получение нового уникального номера учетной записи
        this.id = theBank.getNewAccountId();

        // Инициализация транзакций
        this.transactions = new ArrayList<>();
    }

    public String getId() {
        return this.id;
    }

    /**
     * Получить сводную строку для учетной записи
     * @return  Сводка строк
     */
    public String getSummaryLine() {

        // Получение баланса учетной записи
        double balance = this.getBalance();

        // Форматирование сводной строки в зависимости от того,
        // является ли баланс отрицательным
        if (balance >= 0) {
            return String.format("%s : $%.02f : %s", this.id, balance, this.name);
        }
        else {
            return String.format("%s : $(%.02f) : %s", this.id, balance, this.name);
        }
    }

    /**
     * Получить баланс этого счета, добавив сумму транзакции
     * @return  Баланс
     */
    public double getBalance() {

        double balance = 0;
        for (Transaction t : this.transactions) {
            balance += t.getAmount();
        }
        return balance;
    }

    /**
     * Печать историю транзакции для учетной записи
     */
    public void printTransHistory() {

        System.out.printf("\nATM.Transaction history for account %s\n", this.id);
        for (int i = this.transactions.size()-1; i >= 0; i--) {
            System.out.println(this.transactions.get(i).getSummaryLine());
        }
        System.out.println();
    }

    /**
     * Добавить новую транзакцию в этот аккаунт
     * @param amount    Сумма транзакции
     * @param memo      Памятка о транзакции
     */
    public void addTransaction(double amount, String memo) {

        // Создание новой транзакции и добавление его в список
        Transaction newTrans = new Transaction(amount, memo, this);
        this.transactions.add(newTrans);
    }
}