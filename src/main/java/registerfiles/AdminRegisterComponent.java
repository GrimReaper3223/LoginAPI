
package registerfiles;

import accesslevel.AccessLevel;
import java.util.Objects;
import java.util.Random;

/**
 * Essa classe estende de Register. Register contem a estrutura de registro de um objeto de usuario simples
 * AdminRegister implementa caracteristicas de registro de administrador (nao tantas, mas isso sera resolvido mais a frente),
 * estendendo toda a estrutura de registro de usuario para evitar reescrita de codigo
 * 
 * Register tambem implementa as informacoes de anotacoes ClassInfo. 
 * Essas anotacoes serao herdadas por essa classe tambem
 * 
 * @see registerfiles.UserRegisterComponent para mais informacoes.
 */
public class AdminRegisterComponent extends UserRegisterComponent {
    
    Random rng = new Random();
    
    
    // chave publica que atribui o registro a conta de admin quanto usada (codigo simbolico. futuramente usarei outros meios de verificacao)
    @Deprecated(forRemoval = true, since = "1.0-SNAPSHOT")
    private static final int ADMIN_REG_KEY = 197256;
    
    
    // o codigo de acesso sera um numero de 4 digitos gerado aleatoriamente pela variavel de referencia rng
    private final int accessCode;
    
    
    // hash de admin. adminHashEmailPass - identifica a conta pelo hash de usuario e senha. adminHashEmailPassAccessCode - contem o valor do hash anterior + accessCode
    private final int adminHashEmailPass;
    private final int adminHashEmailPassAccessCode;
    
    
    // mesma logica de atribuicao de id para um administrador
    private final int adminId;
    
    
    public AdminRegisterComponent(String name, String phoneNumber, String email, String password) {
        super(name, phoneNumber, email, password);
        
        accountLevel = AccessLevel.ADMIN;
        accessCode = rng.nextInt(1000, 10000);
        
        adminHashEmailPass = Objects.hash(email, password);
        adminHashEmailPassAccessCode = Objects.hash(adminHashEmailPass, accessCode);
        
        adminId = idIterator;
    }
    
    
    //metodos get
    public final int getAccessCode() {
        return accessCode;
    }
    
    public final int getMasterHash() {
        return adminHashEmailPassAccessCode;
    }

    /**
     * @return chave publica de registro de admins.
     * @deprecated Este metodo esta obsoleto desde esta versao inicial.
     * foi incluido neste projeto para fins de teste de seguranca. Futuramente sera substituido por outra funcao mais eficiente
     */
    @Deprecated(forRemoval = true, since = "1.0-SNAPSHOT")
    public static final int getPubKey() {
        return ADMIN_REG_KEY;
    }
    
    @Override
    public int getId() {
        return adminId;
    }
    
    @Override
    public int getHash() {
        return adminHashEmailPass;
    }
}
