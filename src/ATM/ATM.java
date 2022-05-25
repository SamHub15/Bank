package ATM;

import java.util.Scanner;

public class ATM {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        Bank bank = new Bank("Bank of John");

        // Добавление пользователя, который создает сберегательный счет
        User user = bank.addUser("Max", "Doe", "12345");

        // Добавление учетной записи для текущего пользователя
        Account newAccount = new Account("Checking", user, bank);
        user.addAccount(newAccount);
        bank.addAccount(newAccount);

        User curUser;
        while (true) {

            // Оставаться в приглашении для входа в систему до успешного входа в систему
            curUser = ATM.mainMenuPrompt(bank, scanner);

            // Оставаться в главном меню, пока пользователь не завершит работу
            ATM.printUserMenu(curUser, scanner);

        }
    }

    /**
     * Печать меню входа в банкомат
     * @param bank      Банковский объект, чей счет использовать
     * @param scanner   Объект сканера, используемый для ввода пользователем
     * @return          Объект аутентифицированного пользователя
     */
    public static User mainMenuPrompt(Bank bank, Scanner scanner) {

        String userID;
        String pin;
        User authUser;

        // Запрашивать комбинацию у пользователя (ID и пин-код) до тех пор,
        // пока не будет достигнута правильная комбинация
        do {
            System.out.printf("\n\nWelcome to %s\n\n", bank.getName());
            System.out.print("Enter User ID: ");
            userID = scanner.nextLine();
            System.out.print("Enter pin: ");
            pin = scanner.nextLine();

            // Получаем пользователя который будет соответствовать комбинации ID и пин-код
            authUser = bank.userLogin(userID, pin);
            if (authUser == null) {
                System.out.println("Incorrect user ID/pin combination. " +
                        "Please try again");
            }


            // Продолжать цикл до успешного входа в систему
        } while (authUser == null);

        return authUser;
    }

    public static void printUserMenu(User user, Scanner scanner) {

        // Печать сводки учетной записи пользователя
        user.printAccountSummary();

        int choice;

        do {

            System.out.printf("Welcome %s, what would you like to do?\n",
                    user.getFirstName());
            System.out.println(" 1) Show account transaction history");
            System.out.println(" 2) Withdrawal");
            System.out.println(" 3) Deposit");
            System.out.println(" 4) Transfer");
            System.out.println(" 5) Quit");
            System.out.println();
            System.out.println("Enter choice: ");
            choice = scanner.nextInt();

            if (choice < 1 || choice > 5) {
                System.out.println("Invalid choice. Please choice 1-5: ");
            }

        } while (choice < 1 || choice > 5);

        // Процесс выбора
        switch (choice) {

            case 1:
                ATM.showTransHistory(user, scanner);
                break;
            case 2:
                ATM.withdrawFunds(user, scanner);
                break;
            case 3:
                ATM.depositFunds(user, scanner);
                break;
            case 4:
                ATM.transferFunds(user, scanner);
                break;
            case 5:
                scanner.nextLine();
                break;
        }

        // Повторное отображение этого меню, если пользователь не захочет выйти
        if (choice != 5)
            ATM.printUserMenu(user, scanner);

    }

    /**
     * Показать историю транзакции для учетной записи
     * @param user      Зарегистрированный пользователь
     * @param scanner   Объект сканера для ввода пользователя
     */
    public static void showTransHistory(User user, Scanner scanner) {

        int account;

        // Получить учетную запись, историю транзакций которой нужно просмотреть
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "whose transaction you want to see: ", user.numAccount());
            account = scanner.nextInt() - 1;
            if (account < 0 || account >= user.numAccount()) {
                System.out.println("Invalid account. Please try again. ");
            }
        } while (account < 0 || account >= user.numAccount());

        // Печать истории транзакции
        user.printAcctTransHistory(account);
    }

    /**
     * Процесс перевода средств с одного счета на другой
     * @param user      Зарегестрированный пользователь
     * @param scanner   Объект сканера для ввода пользователя
     */
    public static void transferFunds(User user, Scanner scanner) {

        int fromAccount;
        int toAccount;
        double amount;
        double accountBal;

        // Получить учетную запись для перевода из
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to transfer from: ", user.numAccount());
            fromAccount = scanner.nextInt() - 1;
            if (fromAccount < 0 || fromAccount >= user.numAccount()) {
                System.out.println("Invalid account. Please try again. ");
            }
        } while (fromAccount < 0 || fromAccount >= user.numAccount());
        accountBal = user.getAcctBalance(fromAccount);


        // Получить учетную запись для перевода в
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to transfer to: ", user.numAccount());
            toAccount = scanner.nextInt() - 1;
            if (toAccount < 0 || toAccount >= user.numAccount()) {
                System.out.println("Invalid account. Please try again. ");
            }
        } while (toAccount < 0 || toAccount >= user.numAccount());

        // Получить сумму перевода
        do {
            System.out.printf("Enter the amount to transfer (max $%.02f): $",
                        accountBal);
            amount = scanner.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than zero.");
            }
            else if (amount > accountBal) {
                System.out.printf("Amount must not be greater than\n" +
                            "balance of $%.02f.\n", accountBal);
            }
        } while (amount < 0 || amount > accountBal);

        // Выполнить транзакцию
        user.addAcctTransaction(fromAccount, -1*amount, String.format(
                        "Transfer to account %s", user.getAcctId(toAccount)));
        user.addAcctTransaction(toAccount, -1*amount, String.format(
                "Transfer to account %s", user.getAcctId(fromAccount)));
    }

    /**
     * Процесс вывода средств со счета
     * @param user
     * @param scanner
     */
    public static void withdrawFunds(User user, Scanner scanner) {

        int fromAccount;
        double amount;
        double accountBal;
        String memo;

        // Получить учетную запись для перевода из
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to withdraw from: ", user.numAccount());
            fromAccount = scanner.nextInt() -1;
            if (fromAccount < 0 || fromAccount >= user.numAccount()) {
                System.out.println("Invalid account. Please try again. ");
            }
        } while (fromAccount < 0 || fromAccount >= user.numAccount());
        accountBal = user.getAcctBalance(fromAccount);

        // Получить сумму перевода
        do {
            System.out.printf("Enter the amount to withdraw (max $%.02f): $",
                    accountBal);
            amount = scanner.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than zero.");
            }
            else if (amount > accountBal) {
                System.out.printf("Amount must not be greater than\n" +
                        "balance of $%.02f.\n", accountBal);
            }
        } while (amount < 0 || amount > accountBal);

        // Взять остаток предыдущего ввода
        scanner.nextLine();

        // Получить памятку
        System.out.print("Enter a memo: ");
        memo = scanner.nextLine();

        // Сделать вывод средств
        user.addAcctTransaction(fromAccount, -1*amount, memo);
    }

    /**
     * Процесс внесения средств на счет учетной записи
     * @param user      Зарегистрированный пользователь
     * @param scanner   Объекс сканер для ввода пользователя
     */
    public static void depositFunds(User user, Scanner scanner) {

        int toAccount;
        double amount;
        double accountBal;
        String memo;

        // Получить учетную запись для перевода из
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to deposit in: ", user.numAccount());
            toAccount = scanner.nextInt() -1;
            if (toAccount < 0 || toAccount >= user.numAccount()) {
                System.out.println("Invalid account. Please try again. ");
            }
        } while (toAccount < 0 || toAccount >= user.numAccount());
        accountBal = user.getAcctBalance(toAccount);

        // Получить сумму перевода
        do {
            System.out.printf("Enter the amount to transfer (max $%.02f): $",
                    accountBal);
            amount = scanner.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than zero.");
            }
        } while (amount < 0);

        // Взять остаток предыдущего ввода
        scanner.nextLine();

        // Получить памятку
        System.out.print("Enter a memo: ");
        memo = scanner.nextLine();

        // Сделать вывод средств
        user.addAcctTransaction(toAccount, amount, memo);
    }

}























