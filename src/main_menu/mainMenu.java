package main_menu;


import java.util.Scanner;

public class mainMenu {
    public static void main(String[] args) {
        Scanner scanner  = new Scanner(System.in);
        int option = scanner.nextInt();
        switch (option) {
            case 1:
                System.out.println("Case 1 BTW!");
                break;
            case 2:
                System.out.println("Case 2 BTW!");
                break;
            default:
                System.out.println("Invalid option");
                break;
        }
    }
}
