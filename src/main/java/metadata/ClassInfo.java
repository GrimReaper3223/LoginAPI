package metadata;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Contem as anotacoes informativasa pertinentes a classe que ela e implementada
 * 
 * @author Deiv
 */
@Documented                                 //permite que essas anotacoes aparecam no javadoc
@Target(ElementType.TYPE)                   //define que estas anotacoes so poderao ser utilizadas em classes, interfaces, enums, etc.
@Inherited                                  //permite que essas anotacoes sejam herdadas pelas subclasses da superclasse que a implementa
@Repeatable(ClassInfoContainer.class)       //permite que essas anotacoes sejam repetidas. ClassInfoContainer.class guarda as anotacoes marcadas como repetiveis
@Retention(RetentionPolicy.CLASS)           //politica de retencao de anotacoes. .CLASS retem essas anotacoes em binarios e no source. nao ha necessidade de carregar as anotacoes no runtime system
public @interface ClassInfo {
    /**
     * autor que criou a classe 
     * 
     * @return o autor que confeccionou a classe
     */
    String author() default "Deiv";

    /**
     * define a data de criacao da classe
     * 
     * @return a data de criacao da classe
     */
    String date() default "N/A";
    

    /**
     * indica a versao atual da classe
     * 
     * @return a versao atual da classe 
     */
    String version();

    /**
     * indica desde qual versao a classe foi implementada no projeto
     * 
     * @return a versao que esta classe foi implementada
     */
    String since() default "v1.0-SNAPSHOT";

    /**
     * indica quantas revisoes essa classe ja passou. cada alteracao se torna uma revisao
     * 
     * @return quantas revisoes foram feitas na classe
     */
    int revision();
    

    /**
     * indica quando foi a ultima vez que a classe foi modificada
     * 
     * @return a ultima data de modificacao dessa classe
     */
    String lastModified() default "N/A";

    /**
     * indica quem modificou esta classe na ultima vez
     * 
     * @return o mantenedor que modificou a classe pela ultima vez
     */
    String lastModifiedBy() default "N/A";
}
