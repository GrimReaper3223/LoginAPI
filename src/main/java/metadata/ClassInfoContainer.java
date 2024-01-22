package metadata;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Container que armazena o tipo de anotacao repetivel.
 * 
 * @since 1.0
 * @author deiv
 */
@Documented                         //registra essa interface de anotacao no javadoc
@Target(ElementType.TYPE)           //o mesmo deve ser declarado nesse container que armazena as anotacoes de ClassInfo
@Inherited                          //se a interface de anotacao que esta sendo repetida for marcada como herdavel, esta interface container tambem deve ser marcada com herdavel
@Retention(RetentionPolicy.RUNTIME) //retem as anotacoes a nivel de classe, source e tempo de execucao. Isso precisa executar junto ao sistema para que as anotacoes correspondentes possam ser recuperadas

public @interface ClassInfoContainer {
    
    /**
     * contem todas as anotacoes repetiveis da interface ClassInfo
     * 
     * @return cada indice do container ClassInfo[] recebe um metodo value(). Este metodo retorna uma anotacao repetivel armazenada no container
     */
    ClassInfo[] value();            //o tipo da matriz deve ser do tipo da anotacao a ser repetida. a funcao value() retorna o valor correspondente de cada indice dessa matriz
}
