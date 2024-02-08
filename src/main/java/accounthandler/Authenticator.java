package accounthandler;

import accesslevel.AccessLevel;
import java.util.NoSuchElementException;
import metadata.ClassInfo;
import registerfiles.DataStructure;

/**
 * Contem a autenticacao para login no sistema
 * 
 * @author deiv
 */

@ClassInfo(
        author = "Deiv",
        date = "26/01/2024",
        
        version = "1.2-SNAPSHOT",
        since = "1.1-SNAPSHOT",
        revision = 2,
        
        lastModified = "08-02-2024"
)
public interface Authenticator {
    
    /**
     * Inicia a primeira fase da autenticacao do usuario que esta fazendo login no sistema.
     * 
     * Funcionamento:
     *      --- Recebe o hash de email e senha de um usuario que esta tentando fazer login
     *        |
     *        |--- verifica na estrutura de dados hashVerification os indices validos (nao-null);
     *           |--- retorna null se nao forem encontrados objetos registrados
     *           |--- Se existir registros (objetos validos), entao e retornado o ultimo indice da segunda matriz
     *          
     * @param userHash o hash de email e senha de usuario criado na interface de login
     * @return o indice onde se encontra este login de usuario ou null caso nao exista usuarios registrados com o hash fornecido
     */
    default Integer authentication(int userHash) {
        
        for (Integer[] hashVerification : DataStructure.hashVerification) {
            if (hashVerification[0] != userHash && hashVerification == null) {
                throw new NoSuchElementException();
                
            } else if (hashVerification[0] == userHash) {
                return hashVerification[2];
            }
        }
        
        return null;
    }
    
    /**
     * Inicia a segunda fase da autenticacao do usuario caso a primeira tenha retornado um indice valido
     * 
     * Funcionamento:
     *      --- Recebe o indice retornado anteriormente pela primeira autenticacao
     *        |
     *        |--- Retorna o tipo da conta baseado na verificacao do segundo elemento [1] da estrutura de dados hashVerification
     *             Se for 0 (Admin.ordinal() = 0), retornar AccessLevel.ADMIN. Se nao (USER.ordinal() = 1), retornar AccessLevel.USER;
     * 
     * @param userIndex o indice retornado pelo metodo anterior de autenticacao.
     * @return O tipo de acesso da conta (ADMIN ou USER)
     */
    default AccessLevel accessLevel(int userIndex) {
        return DataStructure.hashVerification[userIndex][1] == 0? AccessLevel.ADMIN : AccessLevel.USER;
    }
}
