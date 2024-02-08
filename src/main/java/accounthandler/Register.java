
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
        
        version = "1.2-SNAPSHOT",
        since = "1.1-SNAPSHOT",
        revision = 2,
        
        lastModified = "07-02-2024"
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
    
    // construtor...
    public Register() {}

    
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
        String firstName, lastName, name, formattedPhoneNumber, formattedEmailInput, passwordInput;
        
        //verifica se existe espaco na estrutura de dados para registrar mais um usuario
        boolean isFull = UserRegisterComponent.getIdIterator() < DataStructure.MAX_USERS;
        
        if(!isFull && DataStructure.registeredUsers[UserRegisterComponent.getIdIterator()] != null) {
            return "Error: the number of registered users has reached the maximum.";
        }
        
        try {
            System.out.println("All fields marked with * are mandatory\n");
            System.out.print("First Name*: ");
            firstName = sc.next();
            
            System.out.print("Last Name*: ");
            lastName = sc.next();
            name = nameFormatter(firstName, lastName);
            
            System.out.print("Phone (enter 0 if don't have): ");
            Long phoneNumberInput = sc.nextLong();
            formattedPhoneNumber = phoneFormatter(phoneNumberInput);
            
            System.out.print("E-mail*: ");
            String emailInput = sc.next();
            formattedEmailInput = emailFormatter(emailInput);
            
            System.out.print("Password (min. 6 characters)*: ");
            passwordInput = sc.next();
            
            if(passwordInput.length() < PASSWORD_MIN_LEN) {
                return "Error: Password must be >= 6 characters. Try again";
            }
            
            System.out.println("\nIs the data entered correct?(Y/n):\n");
            System.out.printf(" Name: %s;%n Phone: %s;%n E-mail: %s;%n Password: %s;%n%n>> ", name, formattedPhoneNumber, formattedEmailInput, passwordInput);
            String choose;
            
            //interruptor para o loop do-while
            boolean trueCondition = false;

            do { 
                sc.reset();
                sc = new Scanner(System.in);
                choose = sc.next().toLowerCase();
                
                switch(choose) {
                    case "y", "yes":
                        trueCondition = true;
                        break;
                    case "n", "no":
                        System.out.println("Restarting registration process...\n");
                        sc.reset();
                        register();
                        break;
                    default: System.err.println("Invalid option. Try again");
                }
                
            } while(!trueCondition);
            
        } catch (NullPointerException e) {
            throw (InputMismatchException) new InputMismatchException("Cause of this throwing: ").initCause(e);
        }
        
        
        DataStructure.registeredUsers[UserRegisterComponent.getIdIterator()] = new UserRegisterComponent(name, formattedPhoneNumber, formattedEmailInput, passwordInput);
        DataStructure.hashVerification[UserRegisterComponent.getIdIterator()][0] = DataStructure.registeredUsers[UserRegisterComponent.getIdIterator()].getHash();
        DataStructure.hashVerification[UserRegisterComponent.getIdIterator()][1] = DataStructure.registeredUsers[UserRegisterComponent.getIdIterator()].getAccountLevel().ordinal();
        DataStructure.hashVerification[UserRegisterComponent.getIdIterator()][2] = DataStructure.registeredUsers[UserRegisterComponent.getIdIterator()].getId();
        
        UserRegisterComponent.increaseIdIterator();
        
        return String.format("User %s registered. Please, log in to your account", name.substring(0, name.indexOf(" ")));
    }
    
    /**
     * Verifica inconsistencias digitadas no nome de um usuario ao efetuar o registro.
     * Se o nome for valido, os caracteres iniciais do nome e do sobrenome passarao a ser maiusculos
     * caso contrario, uma excessao sera lancada
     * 
     * @param firstName o primeiro nome de usuario
     * @param lastName o sobrenome ou ultimo nome de usuario
     * @return uma String caso tudo esteja correto
     * @throws InputMismatchException se o nome inserido nao for valido ou os parametros estiverem em branco
     */
    private String nameFormatter(String firstName, String lastName) {
        //caracteres especiais e numericos para verificacao
        String invalidChar = "~`!@#$%^&*()_-+={[}]|\\:;\"\'<,>.?/0123456789";
        
        //cria uma string unica de nome + sobrenome
        String nameCheck = firstName.concat(lastName);
        
        //cria um array do tamanho da string de nome unica
        String[] iteratedNameArray = new String[nameCheck.length()];
        
        //Obtem o valor em formato string do caractere no indice i de nameCheck(nome concatenado) e insere na array iteratedNameArray
        for(var i = 0; i < iteratedNameArray.length; i++) {
            iteratedNameArray[i] = String.valueOf(nameCheck.charAt(i));
        }
        
        //loop que recebe cada item anteriormente iterado e passa em uma variavel
        for(var valueCheck : iteratedNameArray) {
            if(invalidChar.contains(valueCheck)) {
                throw new InputMismatchException("Invalid name. Try again");
            }
        }
        
        //reatribui os parametros para as arrays para evitar embaralhamento do nome
        char[] charArrFirstName = firstName.toCharArray();
        char[] charArrLastName = lastName.toCharArray();

        //converte a primeira letra de cada nome
        charArrFirstName[0] = Character.toUpperCase(charArrFirstName[0]);
        charArrLastName[0] = Character.toUpperCase(charArrLastName[0]);

        //reconstroi a string com as iniciais convertidas
        String modifiedFirstName = new String(charArrFirstName);
        String modifiedLastName = new String(charArrLastName);

        //retorna uma concatenacao do primeiro nome + espaco + segundo nome modificados
        return modifiedFirstName + " " + modifiedLastName;
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
        
            if(phoneNumber == 0) {
                return null;
            } else if(phoneNumber.toString().length() < PHONE_NUMBER_LENGTH || phoneNumber.toString().length() > PHONE_NUMBER_LENGTH) {
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
