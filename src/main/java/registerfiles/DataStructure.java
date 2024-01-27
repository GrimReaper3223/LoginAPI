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
        
        version = "1.1-SNAPSHOT",
        revision = 2,
        
        lastModified = "26-01-2024"
)

public class DataStructure {

    public static final int MAX_USERS = 100;
    public static final int MAX_ADMINS = 3;
    
    /**
     * estrutura de dados simples que armazena dados de usuarios registrados. 
     * Contem o tamanho maximo de usuarios que podem ser registrados.
     * 
     * O tipo desta estrutura corresponde com o tipo de dado de usuario (Register)
     */
    public static UserRegisterComponent[] registeredUsers = new UserRegisterComponent[MAX_USERS];

    /**
     * estrutura de dados simples que armazena dados de administradores registrados.
     * Contem o tamanho maximo de administradores que podem ser registrados.
     * 
     * O tipo desta estrutura corresponde com o tipo de dado de administrador (AdminRegister)
     */
    public static AdminRegisterComponent[] registeredAdmins = new AdminRegisterComponent[MAX_ADMINS];
    
    
    /**
     * estrutura de dados bidimensional que armazena dados de hash de usuarios e administradores registrados
     * a primeira dimensao tem o tamanho maximo de MAX_USERS + MAX_ADMINS
     * a segunda dimensao tem o tamanho maximo de 3
     * 
     * o primeiro indice da segunda dimensao armazena o hash de email e senha de contas criadas no sistema, seja de admins ou usuarios.
     * o segundo indice da segunda dimensao armazena o indice do nivel de acesso enum da conta (0 = admin, 1 = user)
     * o terceiro indice da segunda dimensao armazena o indice onde a conta do usuario esta localizada na estrutura de dados de registro de informacoes
     * 
     * durante o login, o hash e comparado com o primeiro indice da segunda dimensao da matriz
     * se o hash corresponder ao indice, sera acessado o segundo indice para saber qual tipo de conta esta sendo acessado.
     * Tambem o acesso sera feito ao terceiro indice, que obtem o indice de onde o usuario verificado esta armazenado na array de informacoes pessoais
     * 
     * Isso serve para manipular o login. Caso o tipo seja um USER, o sistema ira entrar. Caso seja um ADMIN, um accessCode sera requerido
     */
    public static Integer[][] hashVerification = new Integer[MAX_USERS + MAX_ADMINS][3];
}
