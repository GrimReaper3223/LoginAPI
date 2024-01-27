
package accounthandler;

import accesslevel.AccessLevel;
import java.util.Formatter;
import java.util.Objects;
import java.util.Scanner;
import metadata.ClassInfo;
import registerfiles.DataStructure;

/**
 * Contem a reformulacao do login de usuario no sistema
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
public class Login implements Authenticator {
    
    Scanner sc;
    
    String emailInput;
    String passwordInput;
    
    /**
     * Construtor basico da classe Login. 
     * Esta classe precisa instanciar um objeto toda vez que o login for requisitado.
     * A instancia tera uma nova instancia de Scanner para insercao de dados de usuario
     */
    public Login() {
        sc = new Scanner(System.in);
    }
    
    
    /**
     * Autentica o usuario no sistema
     * 
     * @return uma String contendo um erro que indica que o indice de usuario nao existe (null);
     * Uma String contendo todos os dados da conta registrada (seja de administrador, seja de usuarios);
     */
    public String loginAuthentication() {
        System.out.print("E-mail: ");
        emailInput = sc.next();
        System.out.print("Password: ");
        passwordInput = sc.next();
        
        // metodo default na interface de autenticacao
        Integer userIndex = authentication(Objects.hash(emailInput, passwordInput));
        
        if(userIndex == null) {
            return "Error: Invalid data or account not registered. Try again";
            
        } else {
            Formatter formatter = new Formatter();
            
            AccessLevel accountType = accessLevel(userIndex);
            
            return switch(accountType) {
                case ADMIN -> {
                    yield formatter.format("%nADMIN ACCOUNT: %n%n Name: %s;%n Phone: %s;%n E-mail: %s;%n Password: %s;%n Account type: %s;%n ID: %d;%n Hash: %d;%n Master hash: %d;%n%n", 
                            DataStructure.registeredAdmins[userIndex].getName(), DataStructure.registeredAdmins[userIndex].getPhoneNumber(),
                            DataStructure.registeredAdmins[userIndex].getEmail(), DataStructure.registeredAdmins[userIndex].getPassword(), 
                            DataStructure.registeredAdmins[userIndex].getAccountLevel().name(), DataStructure.registeredAdmins[userIndex].getId(),
                            DataStructure.registeredAdmins[userIndex].getHash(), DataStructure.registeredAdmins[userIndex].getMasterHash()).toString();
                }
                case USER -> {
                    yield formatter.format("%nUSER ACCOUNT: %n%n Name: %s;%n Phone: %s;%n E-mail: %s;%n Password: %s;%n Account type: %s;%n ID: %d;%n Hash: %d;%n%n",
                            DataStructure.registeredUsers[userIndex].getName(), DataStructure.registeredUsers[userIndex].getPhoneNumber(),
                            DataStructure.registeredUsers[userIndex].getEmail(), DataStructure.registeredUsers[userIndex].getPassword(), 
                            DataStructure.registeredUsers[userIndex].getAccountLevel().name(), DataStructure.registeredUsers[userIndex].getId(),
                            DataStructure.registeredUsers[userIndex].getHash()).toString();
                }
            };
        }
    }
}