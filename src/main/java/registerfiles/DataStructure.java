package registerfiles;

import metadata.ClassInfo;

/** 
 * Classe que armazena uma estrutura de dados simples para simular o registro e login de usuarios com informacoes persistentes em tempo de execucao.
 * Por se tratar de uma API, esta classe pode ser substituida ou modificada para comportar um banco de dados ou uma estrutura que grava dados em arquivos.
 * 
 * Especificacoes da classe:
 *      2 variaveis de classe constantes do tipo inteiro, que define o maximo de admins e usuarios no sistema.
 *      2 arrays de classe com os tamanhos correspondentes as variaveis: registeredUser[MAX_USERS], registeredAdmins[MAX_ADMINS]
 *      1 array de classe bidimensional inteira, onde o tamanho da primeira dimensao e a soma dos espacos de usuarios e admins, e a segunda dimensao tem tamanho 2 [0, 1]
 *              Essa array armazena o hashCode de email e senha de admins e usuarios no primeiro indice da segunda array
 *              No segundo indice da segunda array, armazena qual o nivel de acesso esta vinculado aquele hash (0 - admin, 1 - user)
 *              Essa segunda array serve para identificar onde procurar pelo objeto durante o login usando sua identificacao.
 * 
 * Isso deve ser aprimorado mais a frente
 * 
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

public class DataStructure {
    

    /**
     * maximo de usuarios que podem ser registrados no sistema
     */
    public static final int MAX_USERS = 100;

    /**
     * maximo de admins que podem ser registrados no sistema
     */
    public static final int MAX_ADMINS = 3;
    
    
    /**
     * estrutura de dados simples que armazena dados de usuarios registrados. 
     * Contem o tamanho maximo de usuarios que podem ser registrados.
     * 
     * O tipo desta estrutura corresponde com o tipo de dado de usuario (Register)
     */
    public static Register[] registeredUsers = new Register[MAX_USERS];

    /**
     * estrutura de dados simples que armazena dados de administradores registrados.
     * Contem o tamanho maximo de administradores que podem ser registrados.
     * 
     * O tipo desta estrutura corresponde com o tipo de dado de administrador (AdminRegister)
     */
    public static AdminRegister[] registeredAdmins = new AdminRegister[MAX_ADMINS];
    
    
    /**
     * estrutura de dados bidimensional que armazena dados de hash de usuarios e administradores registrados
     * a primeira dimensao tem o tamanho maximo de MAX_USERS + MAX_ADMINS
     * a segunda dimensao tem o tamanho maximo de 2
     * 
     * o primeiro indice da segunda dimensao armazena o hash de email e senha de contas criadas no sistema, seja de admins ou usuarios.
     * o segundo indice da segunda dimensao armazena o indice do nivel de acesso enum da conta (0 = admin, 1 = user)
     * 
     * durante o login, o hash e comparado com o primeiro indice da segunda dimensao da matriz
     * se o hash corresponder ao indice, sera acessado o segundo indice para saber qual tipo de conta esta sendo acessado.
     * Isso serve para manipular o login. Caso o tipo seja um USER, o sistema ira entrar. Caso seja um ADMIN, um accessCode sera requerido
     */
    public static int[][] hashVerification = new int[MAX_USERS + MAX_ADMINS][2];
}
