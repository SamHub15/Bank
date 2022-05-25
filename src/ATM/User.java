package ATM;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class User {

    /**
     * Имя пользователя
     */
    private String firstName;

    /**
     * Фамилия пользователя
     */
    private String lastName;

    /**
     * Уникальный номер пользователя
     */
    private String id;

    /**
     * MD5 хэширует пин код пользователя
     */
    private byte pinHash[];

    /**
     * Лист аккаунтов для этого пользователя
     */
    private ArrayList<Account> accounts;

    /**
     * Создание нового пользователя
     * @param firstName     Имя
     * @param lastName      Фамилия
     * @param pin           pin-код учетной записи пользователя
     * @param theBank       Подтверждение банка, что пользователь является клиентом
     */
    public User(String firstName, String lastName, String pin, Bank theBank){

        this.firstName = firstName;
        this.lastName = lastName;

        // Хранение хэш pin-кода, а не исходное значение

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            this.pinHash = md.digest(pin.getBytes());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error, caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }

        // Получение уникального идентификатора  пользователя
        this.id = theBank.getNewUserId();

        // Создание списка учетных записей
        this.accounts = new ArrayList<>();

        // Печать сообщений журнала
        System.out.printf("New user %s, %s with ID %s created.\n", lastName, firstName, this.id);
    }

    /**
     * Добавление учетной записи для пользователя
     * @param account   Учетная запись для добавления
     */
    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    public String getId() {
        return this.id;
    }

    /**
     * Проверка на соответствие данного пин-кода истинному пин-коду пользователя
     * @param pin   Проверка данного пин-кода
     * @return      Является ли пин-код истинным или нет
     */
    public boolean validatePin(String pin) {

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(md.digest(pin.getBytes()), this.pinHash);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("error, caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }
        return false;
    }

    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Печать сводки для учетной записи этого пользователя
     */
    public void printAccountSummary() {

        System.out.printf("\n\n%s's accounts summary\n", this.firstName);
        for (int i=0; i<this.accounts.size(); i++) {
            System.out.printf("  %d) %s\n", i+1,
                        this.accounts.get(i).getSummaryLine());
        }
        System.out.println( );
    }

    /**
     * Получить номер учетной записи пользователя
     * @return  Номер учетной записи
     */
    public int numAccount() {
        return this.accounts.size();
    }

    /**
     * Печчать истории транзакции для определенной учетной записи
     * @param accountIdx    Индекс учетной записи пользователя
     */
    public void printAcctTransHistory(int accountIdx) {
        this.accounts.get(accountIdx).printTransHistory();

    }

    /**
     * Получить баланс для определенной учетной записи
     * @param accountIdx    Индекс четной записи
     * @return              Баланс учетной записи
     */
    public double getAcctBalance(int accountIdx) {
        return this.accounts.get(accountIdx).getBalance();
    }

    /**
     * Получить id определенной учетной записи
     * @param accountId     Индекс учетной записи
     * @return              Id учетной записи
     */
    public String getAcctId(int accountId) {
        return this.accounts.get(accountId).getId();
    }

    /**
     * Добавить транзакцию в определенную учетную запись
     * @param accountId         Индекс учетной записи
     * @param amount            Сумма транзакции
     * @param memo              Памятка о транзакции
     */
    public void addAcctTransaction(int accountId, double amount, String memo) {
        this.accounts.get(accountId).addTransaction(amount, memo);
    }

}
























