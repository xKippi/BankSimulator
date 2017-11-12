package at.htlinn.kippi.bank;

import java.util.Locale;

public abstract class Account
{
    protected String accountNumber;
    protected double balance;

    public Account(String accountNumber, double startingBalance)
    {
        this.accountNumber = accountNumber;
        deposit(startingBalance);
    }

    public void deposit(double amount)
    {
        if(amount < 0)
            throw new IllegalArgumentException("Amount must not be smaller than 0");

        balance += amount;
    }

    public void withdraw(double amount)
    {
        if(amount < 0)
            throw new IllegalArgumentException("Amount must not be smaller than 0");

        balance -= amount;
    }

    public double getBalance()
    {
        return balance;
    }

    @Override
    public String toString()
    {
        return "Account number: "+accountNumber+String.format(Locale.GERMAN,"\tBalance: %.2f€",balance);
    }

    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof Account))
            throw new IllegalArgumentException("Object must be of type Account");

        return balance == ((Account)o).balance && accountNumber.equals(((Account)o).accountNumber);
    }

    @Override
    public int hashCode()
    {
        return accountNumber.hashCode() * 17 + Double.hashCode(balance);
    }
}