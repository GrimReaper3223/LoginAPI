
package registerfiles;

import accesslevel.AccessLevel;
import java.util.Objects;
import metadata.ClassInfo;

/**
 * Essa classe contem a estrutura de registro simples de usuario, com nome, email, numero de telefone, id e nivel de acesso.
 * Compartilha seus campos e metodos para adminRegister, estendendo-os
 * As anotacoes desta classse tambem serao compartilhadas com adminRegister
 * 
 * @see registerfiles.AdminRegisterComponent para mais informacoes.
 * @author deiv
 */
@ClassInfo (
        author = "Deiv",
        date = "22/01/2024",
    
        version = "1.1-SNAPSHOT",
        revision = 2,
        
        lastModified = "26-01-2024"
)

public class UserRegisterComponent {
    
    //informacoes do usuario
    protected String name, phoneNumber, email, password;

    
    // hash de email e senha de usuario. caso email e/ou senha sejam alterados, o hash tambem sera
    private final int userHash;
    
    
    // itera + 1 (no metodo de registro na classe LoginAPI) sobre cada usuario criado e atribui esse numero ao id do proximo usuario
    static int idIterator = 0;
    private final int userId;
    
    
    // Define o nivel da conta
    protected AccessLevel accountLevel;
    

    public UserRegisterComponent(String name, String phoneNumber, String email, String password) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        
        //define que todos os registros vindos desse construtor terao privilegios de usuario
        accountLevel = AccessLevel.USER;
        
        userHash = Objects.hash(email, password);   //gera o hash de email e senha
        userId = idIterator;                                      //armazena o id do objeto atual
    }
    
    
    //metodos get
    public final String getName() {
        return name;
    }
    
    public final String getPhoneNumber() {
        return phoneNumber;
    }
    
    public final String getEmail() {
        return email;
    }
    
    public final String getPassword() {
        return password;
    }
    
    public final AccessLevel getAccountLevel() {
        return accountLevel;
    }

    /**
     * sobrescrito na classe AdminRegister
     * 
     * @return o ID associado a esta conta
     */
    public int getId() {
        return userId;
    }
    
    public static final int getIdIterator() {
        return idIterator;
    }
    
    /**
     * sobrescrito na classe AdminRegister
     * 
     * @return o hash de email e senha associado a esta conta
     */
    public int getHash() {
        return userHash;
    }
    
    
    // metodos set
    public final void setName(String name) {
        this.name = name;
    }

    
    public final void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    /**
     * altera somente o email, sem alterar a senha
     * posteriormente, sera adicionado metodos para alterar email ou senha vinculados a conta
     * para caso o usuario tenha esquecido seu acesso.
     * Esses metodos serao desenvolvidos para que novos hashs sejam criados
     * 
     * @param email o novo email para substituir o antigo
     */
    public final void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * altera somente a senha, sem alterar o email
     * 
     * posteriormente, sera adicionado metodos para alterar email ou senha vinculados a conta
     * para caso o usuario tenha esquecido seu acesso.
     * Esses metodos serao desenvolvidos para que novos hashs sejam criados
     * 
     * @param password a nova senha para substituir a antiga
     */
    public final void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * incrementa o iterador para gerar o proximo indice para qualquer proximo objeto (admin ou user) que se registre no sistema
     * este metodo e chamado ao fim do metodo register(), na classe LoginAPI
     * 
     * @see accounthandler.Register#register() para mais informacoes.
     */
    public static final void increaseIdIterator() {
        idIterator++;
    }
}
