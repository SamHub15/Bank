package ATM;

import java.util.Date;

public class Transaction {

    /**
     * Сумма транзакции
     */
    private double amount;

    /**
     * Время и дата транзакции
     */
    private Date timestamp;

    /**
     * Памятка для транзакции
     */
    private String memo;

    /**
     * Счет, на котором была совершена транзакция
     */
    private Account account;

    /**
     * Создание новой транзакции
     * @param amount    Сумма транзакции
     * @param account   Аккаунт к которому принадлежит транзакция
     */
    public Transaction(double amount, Account account) {

        this.amount = amount;
        this.account = account;
        this.timestamp = new Date();
        this.memo = "";

    }

    /**
     * Создание новой транзакции
     * @param amount    Сумма транзакции
     * @param memo      Памятка для транзакции
     * @param account   Аккаунт к которому принадлежит транзакция
     */
    public Transaction(double amount, String memo, Account account) {

        // Вызов конструктора
        this(amount, account);

        this.memo = memo;
    }

    /**
     * Получение суммы транзакции
     * @return  Сумма
     */
    public double getAmount() {
        return this.amount;
    }

    /**
     * Получить строку, суммирующую транзакцию
     * @return  Сумма строки
     */
    public String getSummaryLine() {

        if (this.amount >= 0) {
            return String.format("%s : $%.02f : %s", this.timestamp.toString(),
                        this.amount, this.memo);
        }
        else {
            return String.format("%s : $(%.02f) : %s", this.timestamp.toString(),
                    -this.amount, this.memo);
        }
    }
}
