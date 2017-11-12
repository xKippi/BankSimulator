package at.htlinn.kippi;

import at.htlinn.kippi.bank.Bank;
import java.util.Scanner;

public class Main {

    public static void main(String[] args)
    {
        Bank bank = new Bank();
        Scanner sc = new Scanner(System.in);

        System.out.println("Banking Simulator v1.0");
        System.out.println("(C) Daniel Kipp, 2017");
        System.out.println("\nType \"HELP\" for help\n");

        //String input="";
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
                case "DEPOSIT": try { bank.deposit(input.length > 2? input[2]:"CURRENT", Double.parseDouble(input[1])); }
                                catch(Exception e) { System.out.println("Invalid input. Type \"HELP\" for help"); } break;
                case "WITHDRAW": try { bank.withdraw(input.length > 2? input[2]:"CURRENT", Double.parseDouble(input[1])); }
                                 catch(Exception e) { System.out.println("Invalid input. Type \"HELP\" for help"); } break;
                case "TRANSFER": break;
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

                System.out.println("Invalid argument: "+input[i]+". Type \"HELP\" for help");
                return;
            }
            if(input[1].equals("SAVINGSACCOUNT") || input[1].equals("SA"))
            {
                try {
                    bank.addSavingsAccount(a, b);
                } catch (IllegalArgumentException e) {
                    Scanner sc = new Scanner(System.in);
                    System.out.print(e.getMessage() + " Overwrite? (y/N): ");
                    if (sc.nextLine().toUpperCase().equals("Y"))
                        bank.addSavingsAccount(a, b, true);
                }
            }
            else if(input[1].equals("CHECKINGACCOUNT") || input[1].equals("CA"))
            {
                try {
                    bank.addCheckingAccount(a, b, l);
                } catch (IllegalArgumentException e) {
                    Scanner sc = new Scanner(System.in);
                    System.out.print(e.getMessage() + " Overwrite? (y/N): ");
                    if (sc.nextLine().toUpperCase().equals("Y"))
                        bank.addCheckingAccount(a, b, l, true);
                }
            }
            else
                System.out.println("Invalid argument: "+input[1]+". Type \"HELP\" for help");
        }
        catch(Exception e) { System.out.println("Invalid input. Type \"HELP\" for help"); }
    }
}