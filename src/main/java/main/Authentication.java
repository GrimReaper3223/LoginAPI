
package main;

import accesslevel.AccessLevel;
import metadata.ClassInfo;
import registerfiles.DataStructure;

/**
 * Interface que faz a autenticacao de usuarios e admins no sistema.
 * 
 * @author deiv
 * @deprecated Uma nova interface melhorada foi criada no pacote accounthandler#Authenticator
 * @see accounthandler.Authenticator para mais informacoes
 */
@ClassInfo (
        author = "Deiv",
        date = "22/01/2024",
        
        version = "1.0-SNAPSHOT",
        revision = 2,
        
        lastModified = "26-01-2024"
)

@Deprecated(forRemoval = true, since = "1.1-SNAPSHOT")
public interface Authentication {
        
    /**
     * Metodo que e chamado pelo sistema de login e recebe o hash de um email e senha fornecidos pelo usuario
     * Esse hash e comparado na estrutura de dados hashVerification.
     * Se for encontrado algum valor correspondente ao hash fornecido como argumento ao metodo, sera retornado o nivel de acesso daquele hash processado pelo metodo convertAcessLevel()
     * Se nada for encontrado, retorna null (o metodo que recebe o retorno null notifica que lanca um NullPointerException na call stack)
     * 
     * @param hash hash de um email e senha fornecido para login
     * @return retorna um enum nivel de acesso (ADMIN ou USER)
     */
    static AccessLevel verifyAccountType(int hash) {
            
        for(int i = 0; i < DataStructure.hashVerification.length; i++) {
            if(DataStructure.hashVerification[i][0] == hash) {
                return convertAccessLevel(DataStructure.hashVerification[i][1]);
                
            } else if(DataStructure.hashVerification[i] == null) {
                return null;
            }
        }
        return null;
    }
    
    /**
     * Recebe um argumento do metodo verifyAccountType se, e somente se, um hash de conta for encontrado.
     * 
     * @param accessLevelIntegerFormat recebe um enumerado inteiro que identifica o tipo de acesso da conta (0 - ADMIN, 1 - USER)
     * @return retorna a conversao correspondente ao nivel de acesso recebido como argumento
     */
    static AccessLevel convertAccessLevel(int accessLevelIntegerFormat) {
        
        return accessLevelIntegerFormat == 0 ? AccessLevel.ADMIN : AccessLevel.USER;
    }
}
