package ATM;

import java.util.ArrayList;
import java.util.Random;

public class Bank {

    /**
     * Имя банка
     */
    private String name;

    /**
     * Список пользователей
     */
    private ArrayList<User> users;

    /**
     * Список учетных записей
     */
    private ArrayList<Account> accounts;

    public Bank(String name) {

        this.name = name;
        this.users = new ArrayList<>();
        this.accounts = new ArrayList<>();
    }

    /**
     * Генерация нового уникального номера для пользователя
     * @return  Уникальный номер
     */
    public String getNewUserId() {

        String id;
        Random random = new Random();
        int len = 6;
        boolean nonUnique;

        /// Генерация уникального номера для пользователя
        do {
            // Генерация номера
            id = "";
            for (int i=0; i<len; i++) {
                id += ((Integer) random.nextInt(10)).toString();
            }

            nonUnique = false;
            for (User u: this.users) {
                if (id.compareTo(u.getId()) == 0) {
                    nonUnique = true;
                    break;
                }
            }


        } while (nonUnique);

        return id;

    }

    public String getNewAccountId() {

        String id;
        Random random = new Random();
        int len = 10;
        boolean nonUnique;

        // Генерация уникального номера для учетной записи
        do {

            // Генерация номера
            id = "";
            for (int i=0; i<len; i++) {
                id += ((Integer) random.nextInt(10)).toString();
            }

            nonUnique = false;
            for (Account a: this.accounts) {
                if (id.compareTo(a.getId()) == 0) {
                    nonUnique = true;
                    break;
                }
            }

        } while (nonUnique);

        return id;
    }


    /**
     * Создание нового пользователя из банка
     * @param firstName     Фамилия пользователя
     * @param lastName      Имя пользователя
     * @param pin           Пин-код пользователя
     * @return              Пользователь
     */
    public User addUser(String firstName, String lastName, String pin) {

        // Создание нового пользователя и добавление его в список
        User newUser = new User(firstName, lastName, pin, this);
        this.users.add(newUser);

        // Создание учетной записи для пользователя
        Account newAccount = new Account("Savings", newUser, this);
        newUser.addAccount(newAccount);
        this.accounts.add(newAccount);

        return newUser;
    }

    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    /**
     * Получение пользователя который связан с уникальным номером и пин-кодом
     * @param userID    Уникальный номер пользователя
     * @param pin       Пин-код пользователя
     * @return          Рузультат работы метода
     */
    public User userLogin(String userID, String pin) {

        // Поиск пользователя в списке
        for(User u: users) {

            // Проверка уникального номера и пин-кода на правильность
            if (u.getId().compareTo(userID) == 0 && u.validatePin(pin))
                return u;
        }
        return null;
    }

    public String getName() {
        return name;
    }
}

























