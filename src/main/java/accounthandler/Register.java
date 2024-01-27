
package accounthandler;

import java.util.Arrays;
import java.util.Formatter;
import java.util.InputMismatchException;
import java.util.Scanner;
import metadata.ClassInfo;
import registerfiles.*;

/**
 * Contem a reformulacao de registro de usuarios no sistema
 * 
 * @author deiv
 */
@ClassInfo(
        author = "Deiv",
        date = "26/01/2024",
        
        version = "1.1-SNAPSHOT",
        since = "1.1-SNAPSHOT",
        revision = 1,
        
        lastModified = "26-01-2024"
)
public class Register {
    
    Scanner sc;
    
    //ajustes de numero de telefone no padrao brasileiro: (DDD) 9xxxx-xxxx
    public final String PHONE_NUMBER_PATTERN = "(%2s) %5s-%4s";
    public final int PHONE_NUMBER_LENGTH = 11;
    
    //formato de endereco de email para formatacao. tambem ha uma lista com alguns provedores de email mais usados
    public final String[] EMAIL_PROVIDERS = {"gmail", "outlook", "yahoo"};
    public final String EMAIL_SINTAX = "%s@%s.com";
    
    //tamanho minimo do registro de senha
    public final int PASSWORD_MIN_LEN = 6;
    
    
    public Register() {}

    
    //para depuracao desta classe
//    public static void main(String... args) {
//        
//        try {
//            register();
//            
//        } catch (InputMismatchException e) {
//            
//            System.err.println("Error: " + e.getMessage());
//            
//            if(e.getCause() != null) {
//                System.err.println(e.getCause());
//            }
//            
//        } finally {
//            sc.remove();
//            main();
//        }
//    }
    
    
    /**
     * Registra um usuario
     * O registro de administradores sera de total responsabilidade do mantenedor do sistema
     * Durante as manutencoes que ocorrerem no sistema, o mantenedor podera adicionar ou remover
     * manualmente um administrador.
     * 
     * Mais a frente, sera criada uma funcao que apenas o mantenedor podera acessar, que registra um admin.
     * 
     * @throws InputMismatchException - caso algum dado invalido seja fornecido
     * @return Uma String na qual informa que ocorreu um erro ao registrar um usuario ou que um usuario foi registrado com sucesso.
     */
    public String register() throws InputMismatchException {
        sc = new Scanner(System.in);
        String name, formattedPhoneNumber, formattedEmailInput, passwordInput;
        
        boolean isFull = UserRegisterComponent.getIdIterator() < DataStructure.MAX_USERS;
        
        if(!isFull && DataStructure.registeredUsers[UserRegisterComponent.getIdIterator()] != null) {
            return "Error: the number of registered users has reached the maximum.";
        }
        
        try {
            System.out.print("Name: ");
            name = sc.nextLine();
            
            System.out.print("Phone: ");
            Long phoneNumberInput = sc.nextLong();
            formattedPhoneNumber = phoneFormatter(phoneNumberInput);
            
            System.out.print("E-mail: ");
            String emailInput = sc.next();
            formattedEmailInput = emailFormatter(emailInput);
            
            System.out.print("Password (min. 6 characters): ");
            passwordInput = sc.next();
            
            if(passwordInput.length() < PASSWORD_MIN_LEN) {
                return "Error: Password must be >= 6 characters. Try again";
            }
            
            System.out.println("\nIs the data entered correct?(Y/n):\n");
            System.out.printf(" Name: %s;%n Phone: %s;%n E-mail: %s;%n Password: %s;%n%n>> ", name, formattedPhoneNumber, formattedEmailInput, passwordInput);
            String choose = sc.next().toLowerCase();
            
            do {
                if(choose.equals("n")) {
                    System.out.println("Restarting registration process...\n");
                    choose = "";
                    sc.reset();
                    register();
                    
                } else if(choose.equals("y")) {
                    break;
                }
                
            } while(!choose.equals("y") || !choose.equals("n"));
            
        } catch (NullPointerException e) {
            throw (InputMismatchException) new InputMismatchException("Cause of this throwing: ").initCause(e);
        }
        
        
        DataStructure.registeredUsers[UserRegisterComponent.getIdIterator()] = new UserRegisterComponent(name, formattedPhoneNumber, formattedEmailInput, passwordInput);
        DataStructure.hashVerification[UserRegisterComponent.getIdIterator()][0] = DataStructure.registeredUsers[UserRegisterComponent.getIdIterator()].getHash();
        DataStructure.hashVerification[UserRegisterComponent.getIdIterator()][1] = DataStructure.registeredUsers[UserRegisterComponent.getIdIterator()].getAccountLevel().ordinal();
        DataStructure.hashVerification[UserRegisterComponent.getIdIterator()][2] = DataStructure.registeredUsers[UserRegisterComponent.getIdIterator()].getId();
        
        UserRegisterComponent.increaseIdIterator();
        
        return String.format("User %s registered. Please, log in to your account", name);
    }
    
    
    /**
     * Recebe um numero de telefone como argumento. O numero, entao, e submetido a 
     * formatacao para o padrao brasileiro.
     * 
     * @param phoneNumber um numero de telefone
     * @return a String do numero de telefone formatado no padrao brasileiro
     */
    private String phoneFormatter(Long phoneNumber) {
        
        String phoneNumberFormatted;
        
        //instrucao try-with-resources que fecha o recurso quando o processamento terminar
        try(Formatter formatter = new Formatter()) {
        
            if(phoneNumber.toString().length() < PHONE_NUMBER_LENGTH || phoneNumber.toString().length() > PHONE_NUMBER_LENGTH) {
                throw new InputMismatchException("Invalid phone number. Try again");
            }

            String phone = phoneNumber.toString();

            String ddd = phone.substring(0, 2);
            String n1 = phone.substring(2, 7);
            String n2 = phone.substring(7, phone.length());

            phoneNumberFormatted = formatter.format(PHONE_NUMBER_PATTERN, ddd, n1, n2).toString();
        }
        
        return phoneNumberFormatted;
    }
    
    
    /**
     * Recebe um email como argumento e o destrincha, analisando cada parte digitada,
     * impedindo que um email invalido seja registrado.
     * 
     * @param email um email para analisar
     * @return a String do email analisado, se este seguir todos os padroes do metodo
     */
    private String emailFormatter(String email) throws InputMismatchException {
        String emailFormatted;
        
        //instrucao try-with-resources que fecha o recurso quando o processamento terminar
        try(Formatter formatter = new Formatter()) {
        
            //variaveis de processamento de email
            final char symbolReference = '@';
            final char dotReference = '.';
            final String deniedCharAtEmailName = "@!#%^&$*()+={}[]:;\'\"<>,/?\\|`~ ";
            char[] charArray = deniedCharAtEmailName.toCharArray();

            //obtem os indices das referencias de '.' e '@'
            int searchPosOfSymbolReference = email.indexOf(symbolReference) + 1;
            int searchPosOfDotReference = email.indexOf(dotReference, searchPosOfSymbolReference);

            //obtem a substring entre o arroba e o ponto de referencia e pesquisa na array se existe um provedor compativel com o que o usuario foneceu
            String mailProviderInput = email.substring(searchPosOfSymbolReference, searchPosOfDotReference);
            int bsResult = Arrays.binarySearch(EMAIL_PROVIDERS, mailProviderInput);

            //se o resultado da pesquisa binaria nao for encontrado
            if(bsResult == -1) {
                throw new InputMismatchException("Invalid email provider. Try again");
            }

            //usa esse indice para extrair o nome do email do usuario e atribuir a essa nova formatacao
            String extractUserMailName = email.substring(0, searchPosOfSymbolReference - 1);

            //verifica se ha inconsistencias no nome do email
            for(var verifyInconsistence : charArray) {
                if(extractUserMailName.indexOf(verifyInconsistence) != -1) {
                    throw new InputMismatchException("Invalid email name. Try again");
                }
            }

            //retorna o email caso ele tenha passado nas verificacoes
            emailFormatted = formatter.format(EMAIL_SINTAX, extractUserMailName, EMAIL_PROVIDERS[bsResult]).toString();
            
        } catch(StringIndexOutOfBoundsException exp) {
            throw (InputMismatchException) new InputMismatchException("Cause of this throwing: ").initCause(exp);
        }
        
        return emailFormatted;
    }
}
