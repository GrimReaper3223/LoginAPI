package accesslevel;

import metadata.ClassInfo;

/**
 * Identifica os tipos de usuarios no sistema. Por enquanto, temos somente ADMIN e USER.
 * Futuramente, esses objetos enum serao aprimorados e cada um tera sua exclusividade
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

public enum AccessLevel {

    /**
     * Nivel de acesso para administradores. tambem pode ser utilizado como identificador
     */
    ADMIN,

    /**
     * Nivel de acesso para usuarios. tambem pode ser utilizado como identificador
     */
    USER;
}
