package main;

import accesslevel.AccessLevel;
import accounthandler.*;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;
import metadata.ClassInfo;
import registerfiles.*;

/**
 * Essa classe contem o metodo principal e implementa a interface de autenticacao de login
 * Contem metodos de login, registro e um metodo para limpar o buffer do objeto Scanner
 * 
 * @author deiv
 */
@ClassInfo (
        author = "Deiv",
        date = "22/01/2024",
        
        version = "1.1-SNAPSHOT",
        revision = 2,
        
        lastModified = "26-01-2024"
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
     *      - ArrayIndexOutOfBoundsException ;
     *      - InputMismatchException;
     * 
     * @param args 
     * @author Deiv
     */
    @SafeVarargs
    public static void main(String... args) {
        int userInput = 0;

        try {
            System.out.println("\nLogin Manager v1.0-SNAPSHOT\n");
            System.out.println("Select an Option: \n");
            System.out.print("1 - Login \n2 - Register \n3 - Exit\n\n>> ");
            userInput = scanner.nextInt();
            
            // verifica se a opcao digitada esta incluida no menu
            if(userInput < 1 || userInput > 3) {
                System.out.println("Invalid option. Try again...");
            }
            
        } catch (InputMismatchException ime) {
            System.err.println("\nCaught InputMismatchException: " + ime.getMessage());
            System.err.print("Enter only numbers\n");
            scanner = cleanScannerBuffer(scanner);
            main();
        }
        
        // o valor de entrada entao e checado e cai num case correspondente
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
     * Metodo de login
     * 
     * Define 2 variaveis locais do tipo String que armazena o email e senha digitados pelo usuario.
     * Gera um hashCode desse email e senha e compara com os hashs de admins e usuarios registrados.
     * A conta localizada retorna seu nivel de acesso a variavel de referencia accountType.
     * Dependendo do nivel de acesso da conta, uma condicional performa sobre a funcao de login apropriada (user ou admin).
     * Caso o tipo de conta seja um admin, tambem e requerido um codigo de acesso que e fornecido ao admin (dono da conta) no momento da criacao da conta
     * Fornecendo o codigo correto, e criado um novo hash contendo o hash anterior + o codigo de acesso.
     * Esse novo hash esta vinculado a conta de admin. Quando a conta for encontrada, como teste de acesso, sera retornado todos os dados associados a conta
     * O mesmo vale para o usuario (que nao precisa de um codigo de acesso, podendo fazer login assim que a conta for encontrada).
     * 
     * @throws NullPointerException - Se nenhuma instancia de nivel de acesso for retornado (null)
     * @throws InputMismatchException  - Se os dados inseridos no objeto Scanner nao corresponder ao tipo de variavel
     * @since 1.0
     * @deprecated Existe uma nova classe de login mais otimizada e eficiente em outro pacote
     * @see accounthandler.Login#loginAuthentication() - novo metodo de login que substitui este
     */
    @Deprecated(forRemoval = true, since = "1.1-SNAPSHOT")
    public static void login() throws NullPointerException, InputMismatchException {
        
        String emailInput, passwordInput;
        
        System.out.print("\nE-mail: ");
        emailInput = scanner.next();
        System.out.print("Password: ");
        passwordInput = scanner.next();
        
        //gera o hash da entrada de email e senha
        int hashGenerated = Objects.hash(emailInput, passwordInput);
        
        AccessLevel accountType = Authentication.verifyAccountType(hashGenerated);
        
        if(accountType.equals(AccessLevel.ADMIN)) {
            System.out.println("\nAdmin account detected!");
            System.out.print("Enter access code: ");
            int accessCodeInput = scanner.nextInt();
            
            int fullHashGenerated = Objects.hash(hashGenerated, accessCodeInput); 
            
            for(var adminHash : DataStructure.registeredAdmins) {
                if(adminHash.getMasterHash() == fullHashGenerated) {
                    System.out.println("\nName: " + adminHash.getName());
                    System.out.println("E-mail: " + adminHash.getEmail());
                    System.out.println("Phone: " + adminHash.getPhoneNumber());
                    System.out.println("ID: " + adminHash.getId());
                    System.out.println("Master Hash: " + adminHash.getMasterHash());
                    System.out.println("Account Level: " + adminHash.getAccountLevel().name());
                    System.out.println("Hash Code: "+ adminHash.getHash() + '\n');
                    break;
                }
            }
            
        } else {
            for(var userHash : DataStructure.registeredUsers) {
                if(userHash.getHash() == hashGenerated) {
                    System.out.println("\nName: " + userHash.getName());
                    System.out.println("E-mail: " + userHash.getEmail());
                    System.out.println("Phone: " + userHash.getPhoneNumber());
                    System.out.println("ID: " + userHash.getId());
                    System.out.println("Account Level: " + userHash.getAccountLevel().name());
                    System.out.println("Hash Code: "+ userHash.getHash() + '\n');
                    break;
                }
            }
        }
    }
    
    
    /**
     * Metodo de registro
     * 
     * Define 4 variaveis (3 Strings e 1 long) fundamentais para registro + 1 de verificacao (String).
     * A variavel de verificacao pergunta ao usuario durante o registro se ha um codigo.
     * Se sim, esse codigo deve ser fornecido e um admin sera registrado. Se nao, um usuario sera registrado.
     * Esse codigo se encontra na classe AdminRegister. Nao e um meio eficiente para verificacao no sistema. Posteriormente sera trocado.
     * Uma variavel 'isValidPubKey', do tipo booleano, armazena 'true' se a chave fornecida combina com a chave do sistema. O padrao e 'false'.
     * Apos fornecidas as informacoes, dependendo de qual seja o registro no sistema, ambos chamarao as estruturas de dados disponiveis na classe 'DataStructure'.
     * 
     * 
     * @throws ArrayIndexOutOfBoundsException - Se o sistema tentar registrar um objeto com a estrutura de dados cheia
     * @throws InputMismatchException - Se algum dado inserido nao corresponder ao tipo da variavel
     * @author Deiv
     * @since 1.0
     * @deprecated existe uma nova classe de registro mais eficiente e otimizada em outro pacote
     * @see accounthandler.Register#register() - novo metodo que substitui este
     */
    @Deprecated(forRemoval = true, since = "1.1-SNAPSHOT")
    public static void register() throws ArrayIndexOutOfBoundsException, InputMismatchException {
        
        //variaveis temporarias que armazenarao dados de registro para insercao na estrutura de dados
        String nameInput, emailInput, passwordInput;
        Long phoneInput;
        
        //pergunta se o usuario que esta sendo registrado possui uma chave publica. A chave publica serve para registrar um admin
        //caso nao haja, sera registrado um usuario
        System.out.print("\nIs there a public key? (Y/n): ");
        String hasAKey = scanner.next().toLowerCase();

        
        //contem o resultado de paridade entre a pubKey do sistema e a pubKey registrada
        boolean isValidPubKey = false;
        
        switch (hasAKey) {
            case "y", "yes" -> {
                int pubKey;
                
                //verifica a paridade da chave publica fornecida com a do sistema. se for igual, isValidPubKey = true. se for 0 (para sair do loop), isValidPubKey = false (default);
                do {
                    System.out.print("Public Key: ");
                    pubKey = scanner.nextInt();
                } while(pubKey != AdminRegisterComponent.getPubKey() || pubKey == 0);
                
                isValidPubKey = pubKey == AdminRegisterComponent.getPubKey();
                
                scanner = cleanScannerBuffer(scanner);
                break;
            }
            case "n", "no" -> scanner = cleanScannerBuffer(scanner);
            
            default -> {
                System.err.println("\nInvalid Option. Input (y)es or (n)o");
                register();
            }
        }
        
        System.out.print("Name: ");
        nameInput = scanner.nextLine();

        System.out.print("Phone: ");
        phoneInput = scanner.nextLong();

        System.out.print("E-Mail: ");
        emailInput = scanner.next();

        System.out.print("Password: ");
        passwordInput = scanner.next();
            
        if(isValidPubKey) {
            DataStructure.registeredAdmins[UserRegisterComponent.getIdIterator()] = new AdminRegisterComponent(nameInput, phoneInput.toString(), emailInput, passwordInput);
            DataStructure.hashVerification[UserRegisterComponent.getIdIterator()][0] = Objects.hash(emailInput, passwordInput);
            DataStructure.hashVerification[UserRegisterComponent.getIdIterator()][1] = DataStructure.registeredAdmins[UserRegisterComponent.getIdIterator()].getAccountLevel().ordinal();
            
            System.out.printf("%nAdmin %s successful registered. Your access code is %d.%n", nameInput, DataStructure.registeredAdmins[UserRegisterComponent.getIdIterator()].getAccessCode());
            UserRegisterComponent.increaseIdIterator();
            
        } else {
            DataStructure.registeredUsers[UserRegisterComponent.getIdIterator()] = new UserRegisterComponent(nameInput, phoneInput.toString(), emailInput, passwordInput);
            DataStructure.hashVerification[UserRegisterComponent.getIdIterator()][0] = Objects.hash(emailInput, passwordInput);
            DataStructure.hashVerification[UserRegisterComponent.getIdIterator()][1] = DataStructure.registeredUsers[UserRegisterComponent.getIdIterator()].getAccountLevel().ordinal();
            
            System.out.printf("%nUser %s successful registered. Log into your account.%n", nameInput);
            UserRegisterComponent.increaseIdIterator();
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
