
package registerfiles;

import accesslevel.AccessLevel;
import java.util.Objects;
import metadata.ClassInfo;

/**
 * Essa classe contem a estrutura de registro simples de usuario, com nome, email, numero de telefone, id e nivel de acesso.
 * Compartilha seus campos e metodos para adminRegister, estendendo-os
 * As anotacoes desta classse tambem serao compartilhadas com adminRegister
 * 
 * @see registerfiles.AdminRegister para mais informacoes.
 * @author deiv
 */
@ClassInfo (
        author = "Deiv",
        date = "22/01/2024",
    
        version = "v1.0-SNAPSHOT",
        revision = 1,
        
        lastModified = "22-01-2024",
        lastModifiedBy = "Deiv"
)

public class Register {
    
    // variaveis que armazenam informacoes de usuario
    protected String name, email;
    protected long phoneNumber;
    
    
    // armazena o hash do email e senha fornecidos ao construtor
    // ao mudar email e senha, o hash tambem sera mudado.
    private final int userHash;
    
    
    // itera + 1 (no metodo de registro na classe LoginAPI) sobre cada usuario criado e atribui esse numero ao id do proximo usuario
    static int idIterator = 0;
    private final int userId;
    
    
    // Define o nivel da conta (user ou admin. nao pode ser final)
    protected AccessLevel accountLevel;
    
    

    /**
     * Construtor base para usuarios.
     * 
     * @param name
     * @param phoneNumber
     * @param email
     * @param password
     */
    public Register(String name, long phoneNumber, String email, String password) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        
        //define que todos os registros vindos desse construtor terao privilegios de usuario
        accountLevel = AccessLevel.USER;
        
        userHash = Objects.hash(email, password);   //gera o hash de email e senha
        userId = idIterator;                                      //armazena o id do objeto atual
    }
    
    

    /**
     * @return o nome associado a esta conta
     */
    public final String getName() {
        return this.name;
    }

    /**
     * @return o numero de telefone associado a esta conta
     */
    public final long getPhoneNumber() {
        return this.phoneNumber;
    }

    /**
     *
     * @return o email associado a esta conta
     */
    public final String getEmail() {
        return this.email;
    }
    
    /**
     *
     * @return o nivel de acesso associado a esta conta
     */
    public final AccessLevel getAccountLevel() {
        return this.accountLevel;
    }
    

    /**
     * sobrescrito na classe AdminRegister
     * 
     * @return o ID associado a esta conta
     */
    public int getId() {
        return userId;
    }
    
    /**
     * @return o iterador que define o ID
     */
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

    /**
     * define um novo nome para esta conta
     * 
     * @param name recebe o novo nome para esta conta
     */
     public final void setName(String name) {
        this.name = name;
    }

    /**
     * define um novo numero de telefone para esta conta
     * 
     * @param phoneNumber recebe o novo numero de telefone para esta conta
     */
    public final void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    /**
     * altera somente o email, sem alterar a senha
     * posteriormente sera adicionado um metodo que visa alterar a senha tambem.
     * 
     * para emular taticas de seguranca, a senha nao foi projetada para ser armazenada
     * em versoes futuras desta api, a senha tambem sera armazenada para que possa ser mudada, de forma individual ao email
     * 
     * @param email email a ser alterado
     */
    public final void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * incrementa o iterador para gerar o proximo indice para qualquer proximo objeto (admin ou user) que se registre no sistema
     * este metodo e chamado ao fim do metodo register(), na classe LoginAPI
     * 
     * @see main.LoginAPI#register() para mais informacoes.
     */
    public static final void increaseIdIterator() {
        idIterator++;
    }
}
