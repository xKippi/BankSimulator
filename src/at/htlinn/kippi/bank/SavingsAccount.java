package at.htlinn.kippi.bank;

public class SavingsAccount extends Account
{
    public SavingsAccount(String accountNumber, double startingBalance)
    {
        super(accountNumber, startingBalance);
    }

    @Override
    public void withdraw(double amount)
    {
        if(balance - amount < 0)
            throw new UnsupportedOperationException("Overdraft is not supported");

        super.withdraw(amount);
    }

    @Override
    public String toString()
    {
        return "Type: SavingsAccounts\t" + super.toString();
    }
}