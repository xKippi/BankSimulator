package at.htlinn.kippi;

import at.htlinn.kippi.bank.Bank;

import java.util.Locale;
import java.util.Scanner;

public class Main {

    public static void main(String[] args)
    {
        Locale.setDefault(Locale.getDefault(Locale.Category.FORMAT));
        Bank bank = new Bank();
        Scanner sc = new Scanner(System.in);

        System.out.println("Banking Simulator v1.0");
        System.out.println("(C) Daniel Kipp, 2017");
        System.out.println("\nType \"HELP\" for help\n");

        String[] input;
        while(true)
        {
            System.out.print("root@bank:~# ");
            input = sc.nextLine().toUpperCase().split(" ");
            switch (input[0])
            {
                case "HELP": Bank.printHelp(); break;
                case "SELECT": bank.selectAccount(input.length > 1? input[1] : ""); break;
                case "PRINT": bank.printValues(input.length > 1? input[1]:"CURRENT"); break;
                case "ADD": addAccount(input, bank); break;
                case "REMOVE": bank.removeAccount(input.length > 1? input[1]:"CURRENT"); break;
                case "DEPOSIT":
                    try {
                        if(input.length > 2)
                            bank.deposit(input[1], Double.parseDouble(input[2]));
                        else
                            bank.deposit("CURRENT", Double.parseDouble(input[1]));
                    }
                    catch(Exception e) { System.out.println("Invalid arguments. Type \"HELP\" for help"); } break;
                case "WITHDRAW":
                    try {
                        if(input.length > 2)
                            bank.withdraw(input[1], Double.parseDouble(input[2]));
                        else
                            bank.withdraw("CURRENT", Double.parseDouble(input[1]));
                    }
                    catch(Exception e) { System.out.println("Invalid arguments. Type \"HELP\" for help"); } break;
                case "TRANSFER":
                    try {
                        if (input.length > 3)
                            bank.transfer(input[1], input[2], Double.parseDouble(input[3]));
                        else
                            bank.transfer("CURRENT", input[1], Double.parseDouble(input[2]));
                    }
                    catch(Exception e) { System.out.println("Invalid arguments. Type \"HELP\" for help"); } break;
                case "BANKROBBERY": bank.bankRobbery(); break;
                case "EXAMPLE": break;
                case "EXIT": System.out.println("Bye\n"); return;
                case "":  break;
                default: System.out.println("Command not found! Type \"HELP\" for help");
            }
            System.out.println();
        }
    }

    private static void addAccount(String[] input, Bank bank)
    {
        String a="";
        int b=0, l=0;
        try
        {
            for (int i=2; i<input.length; i++)
            {
                String[] splitInput = input[i].split(":");
                if(splitInput[0].equals("A")) { a = splitInput[1]; continue; }
                if(splitInput[0].equals("B")) { b = Integer.parseInt(splitInput[1]); continue; }
                if(splitInput[0].equals("L")) { l = Integer.parseInt(splitInput[1]); continue; }

                System.out.println("Invalid argument: \""+input[i]+"\". Type \"HELP\" for help");
                return;
            }
            switch(input[1])
            {
                case "SA":
                case "SAVINGSACCOUNT":
                    try {
                        bank.addSavingsAccount(a, b);
                    } catch (UnsupportedOperationException e) {
                        Scanner sc = new Scanner(System.in);
                        System.out.print(e.getMessage() + " Overwrite? (y/N): ");
                        if (sc.nextLine().toUpperCase().equals("Y"))
                            bank.addSavingsAccount(a, b, true);
                    } break;

                case "CA":
                case "CHECKINGACCOUNT":
                    try {
                        bank.addCheckingAccount(a, b, l);
                    } catch (UnsupportedOperationException e) {
                        Scanner sc = new Scanner(System.in);
                        System.out.print(e.getMessage() + " Overwrite? (y/N): ");
                        if (sc.nextLine().toUpperCase().equals("Y"))
                            bank.addCheckingAccount(a, b, l, true);
                    } break;
                default: System.out.println("Invalid argument: \""+input[1]+"\". Type \"HELP\" for help"); break;
            }
        }
        catch(Exception e) { System.out.println("Invalid arguments. Type \"HELP\" for help"); }
    }
}