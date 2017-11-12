package at.htlinn.kippi.bank;

import at.htlinn.kippi.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Bank
{
    private Map<String ,Account> accounts = new HashMap<>();
    private String currentAccountNr = "";
    private int SACounter = 0, CACounter = 0;

    public void selectAccount(String accountNr)
    {
        if(!accounts.containsKey(accountNr))
        {
            System.out.println("Account \""+accountNr+"\" doesn't exists");
            return;
        }

        currentAccountNr = accountNr;
        System.out.println("Selected "+accountNr);
    }

    public void printValues(String query)
    {
        parseQuery(query, new Method<String>() {
            @Override
            public String call(String... arguments)
            {
                return accounts.containsKey(arguments[0])? accounts.get(arguments[0]).toString() : "Account \""+arguments[0]+"\" doesn't exist\"";
            }
        });
    }

    public void addSavingsAccount(String accountNr, double startingBalance) {
        addSavingsAccount(accountNr, startingBalance, false);
    }
    public void addSavingsAccount(String accountNr, double startingBalance, boolean overwrite)
    {
        if(startingBalance < 0)
        {
            System.out.println("The starting balance must not be smaller than 0");
            return;
        }

        if(accounts.containsKey(accountNr) && !overwrite)
            throw new IllegalArgumentException("The account \""+accountNr+"\" already exists");

        if(accountNr.equals(""))
            do accountNr = "SA" + String.format("%03d", ++SACounter);
            while(accounts.containsKey(accountNr));

        accounts.put(accountNr, new SavingsAccount(accountNr, startingBalance));
        System.out.println("Added savings account with account number \""+accountNr+"\"");
    }
    public void addCheckingAccount(String accountNr, double startingBalance, double overdraftLimit) {
        addCheckingAccount(accountNr, startingBalance, overdraftLimit, false);
    }
    public void addCheckingAccount(String accountNr, double startingBalance, double overdraftLimit, boolean overwrite)
    {
        if(startingBalance < 0 || overdraftLimit < 0)
        {
            System.out.println("The starting balance and the overdraft limit must not be smaller than 0");
            return;
        }

        if(accounts.containsKey(accountNr) && !overwrite)
            throw new IllegalArgumentException("The account \""+accountNr+"\" already exists");

        if(accountNr.equals(""))
            do accountNr = "CA" + String.format("%03d", ++CACounter);
            while(accounts.containsKey(accountNr));

        accounts.put(accountNr, new CheckingAccount(accountNr, startingBalance, overdraftLimit));
        System.out.println("Added checking account with account number \""+accountNr+"\"");
    }

    public void removeAccount(String query)
    {
        parseQuery(query, new Method<String>() {
            @Override
            public String call(String... arguments) {
                if(accounts.containsKey(arguments[0]))
                {
                    accounts.remove(arguments[0]);
                    return "Removed account \""+arguments[0]+"\"";
                }
                return "Account \""+arguments[0]+"\" doesn't exist";
            }
        });
    }

    public void deposit(String query, double amount)
    {
        parseQuery(query, new Method<String>() {
            @Override
            public String call(String... arguments) {
                if(accounts.containsKey(arguments[0]))
                {
                    accounts.get(arguments[0]).deposit(amount);
                    return String.format(Locale.GERMAN, "Deposited %.2f€ into account \"" + arguments[0] + "\"", amount);
                }
                return "Account \""+arguments[0]+"\" doesn't exist";
            }
        });
    }

    public void withdraw(String query, double amount)
    {
        parseQuery(query, new Method<String>() {
            @Override
            public String call(String... arguments) {
                if(accounts.containsKey(arguments[0]))
                {
                    try { accounts.get(arguments[0]).withdraw(amount); }
                    catch (UnsupportedOperationException e) { return e.getMessage(); }
                    return String.format(Locale.GERMAN, "Withdrew %.2f€ from account \"" + arguments[0] + "\"", amount);
                }
                return "Account \""+arguments[0]+"\" doesn't exist";
            }
        });
    }



    public void bankRobbery()
    {
        for (Map.Entry<String, Account> account : accounts.entrySet())
            if(account.getValue().getBalance() > 0)
                account.getValue().withdraw(account.getValue().getBalance());

        System.out.println("Oh no... The bank has been robbed :o");
    }

    public static void printHelp()
    {
        System.out.println("Simulate a simple bank in an interactive shell");
        System.out.println("\nCommands:");
        System.out.println("HELP                                       \tShows this help");
        System.out.println("SELECT accNr                               \tSelects an account with an specific account number.");
        System.out.println("PRINT [query]                              \tQueries and prints account data");
        System.out.println("ADD accType [a:accNr] [b:balance] [l:limit]\tAdds either a account of type SavingAccount (SA) or CheckingAccount (CA)");
        System.out.println("                                           \tIf no account number is submitted, a new one is generated (SA001 or CA001)");
        System.out.println("                                           \tThe limit is only specified for checking accounts");
        System.out.println("REMOVE [query]                             \tRemoved the account(s) specified in the query");
        System.out.println("DEPOSIT amount [query]                     \tDeposits a certain amount of money into the specified accounts");
        System.out.println("WITHDRAW amount [query]                    \tWithdraws a certain amount of money from the specified accounts");
        System.out.println("TRANSFER [s:accNr] accNr amount            \tTransfers a certain amount of money from the first to the second account");
        System.out.println("                                           \tIf no sender is submitted, the currently selected account will be used");
        System.out.println("BANKROBBERY                                \tRobs the bank :)");
        System.out.println("EXAMPLE                                    \tPrints an example");
        System.out.println("EXIT                                       \tExits the program");
        System.out.println("\nPossible queries are: ALL, accNr1-accNr6, accNr1,accNr3,accNr2, accNr5, CURRENT");
        System.out.println("If no query is submitted, the CURRENTly selected account will be queried");
    }

    private void parseQuery(String query, Method<String> func)
    {
        if(query.toUpperCase().equals("ALL"))
            for (Map.Entry<String, Account> account : accounts.entrySet())
                System.out.println(func.call(account.getKey()));
        else if(query.toUpperCase().equals("CURRENT"))
            System.out.println(accounts.containsKey(currentAccountNr)? func.call(currentAccountNr):"There is currently no account selected");
        else if (query.contains(","))
            for (String accountNr : query.split (","))
                System.out.println(func.call(accountNr));
        else if(query.contains("-"))
        {
            String[] num = { "", "" };
            String[] splitIndex = query.split("-");
            String pattern = "";

            try
            {
                if(splitIndex.length != 2)
                {
                    System.out.println("The query may only contain one \"-\"");
                    return;
                }

                for(int i=0; i<splitIndex[0].length();i++)
                {
                    if(splitIndex[0].charAt(i) == splitIndex[1].charAt(i))
                        pattern += splitIndex[0].charAt(i);
                    else
                    {
                        pattern += "\0";
                        for (int j = 0; j < 2; j++)
                            num[j] += splitIndex[j].charAt(i);
                    }
                }

                while(pattern.indexOf('\0') != pattern.lastIndexOf('\0'))
                    pattern = pattern.replaceFirst("\0", "");

                for (int i = Integer.parseInt(num[0]); i <= Integer.parseInt(num[1]); i++)
                    System.out.println( func.call( pattern.replace("\0", String.format("%0" + num[0].length() + "d", i))));
            }
            catch(Exception e) { System.out.println("Invalid query"); }
        }
        else
            System.out.println(func.call(query));
    }
}