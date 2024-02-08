package main;

import accounthandler.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import metadata.ClassInfo;

/**
 * Essa classe contem o metodo principal e implementa a interface de autenticacao de login
 * Contem metodos de login, registro e um metodo para limpar o buffer do objeto Scanner
 * 
 * @author deiv
 */
@ClassInfo (
        author = "Deiv",
        date = "22/01/2024",
        
        version = "1.2-SNAPSHOT",
        revision = 4,
        
        lastModified = "08-02-2024"
)

public class LoginAPI implements Authenticator {
    static Scanner scanner = new Scanner(System.in);
    
    
    /**
     * Metodo principal
     * 
     * Define o menu e entrada de usuario
     * Trata os seguintes erros que podem ser lancados pelos metodos:
     * 
     *      - NullPointerException;
     *      - InputMismatchException;
     * 
     * @param args 
     * @author Deiv
     */
    public static void main(String... args) {
        int userInput = 0;

        try {
            System.out.print("""
                               Login Manager v1.2-SNAPSHOT
                               
                               Select an Option:
                               
                               1 - Login
                               2 - Register
                               3 - Exit
                               
                               >> """);
            
            userInput = scanner.nextInt();
            
            if(userInput < 1 || userInput > 3) {
                System.out.println("Invalid option. Try again...");
                main();
            }
            
        } catch (InputMismatchException ime) {
            System.err.println("\nCaught InputMismatchException: " + ime.getMessage());
            System.err.print("Enter only numbers\n");
            scanner = cleanScannerBuffer(scanner);
            main();
        }
        
        try {
            switch(userInput) {
                case 1 -> {
                    Login login = new Login();
                    System.out.print(login.loginAuthentication());  
                    break;
                }
                case 2 -> {
                    Register register = new Register();
                    System.out.print(register.register());
                    main();
                    break;
                }
                case 3 -> {
                    System.out.println("\nExiting...\n");
                    scanner.close();
                    break;
                }
                default -> scanner = cleanScannerBuffer(scanner);
            }
            
        } catch(NullPointerException | InputMismatchException e) {
            System.err.println("Error: " + e.getMessage());
            
            if(e.getCause() != null) {
                System.err.println(e.getCause());
            }
            
            //garantindo a limpeza do scanner apos uma excessao
            scanner = cleanScannerBuffer(scanner);
            main();
        }
    }
    
    
    /**
     * Usado para resetar a instancia scanner quando necessario.
     * Alem de resetar a instancia, reinicia ela para evitar erros.
     * Esse metodo e necessario para que o buffer do scanner seja limpo
     * 
     * @author Deiv
     * @param scannerInstance recebe a instancia de um Scanner para resetar as informacoes armazenadas
     * @return uma nova instancia de Scanner para a variavel de referencia
     * @since 1.1-SNAPSHOT
     */
    public static Scanner cleanScannerBuffer(Scanner scannerInstance) {
        scannerInstance.reset();
        return new Scanner(System.in);
    }
}
