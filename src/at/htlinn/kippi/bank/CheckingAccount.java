package at.htlinn.kippi.bank;

import java.util.Locale;

public class CheckingAccount extends Account
{
    private double overdraftLimit;

    public CheckingAccount(String accountNumber, double startingBalance, double overdraftLimit)
    {
        super(accountNumber, startingBalance);
        setOverdraftLimit(overdraftLimit);
    }

    public double getOverdraftLimit()
    {
        return overdraftLimit;
    }

    @Override
    public void withdraw(double amount)
    {
        if(balance - amount < -overdraftLimit)
            throw new UnsupportedOperationException("Overdraft limit reached");

        super.withdraw(amount);
    }

    @Override
    public String toString()
    {
        return "Type: CheckingAccounts\t" + super.toString() + String.format(Locale.GERMAN, "\tOverdraft limit: %.2f€",overdraftLimit);
    }

    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof CheckingAccount))
            throw new IllegalArgumentException("Object must be of type CheckingAccount");

        return balance == ((CheckingAccount)o).balance && accountNumber.equals(((CheckingAccount)o).accountNumber) && ((CheckingAccount)o).overdraftLimit == overdraftLimit;
    }

    @Override
    public int hashCode()
    {
        return accountNumber.hashCode() * 17 + Double.hashCode(balance) + Double.hashCode(overdraftLimit);
    }

    private void setOverdraftLimit(double limit)
    {
        if(limit < 0)
            throw new IllegalArgumentException("Limit must not be smaller than 0");

        overdraftLimit = limit;
    }
}