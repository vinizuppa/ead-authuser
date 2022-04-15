package com.ead.authuser.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameConstraintImpl.class)
@Target({ElementType.METHOD, ElementType.FIELD})//Indica os locais onde podemos utilizar a anotação. Nesse caso, podemos utilizar em um campo e um metodo.
@Retention(RetentionPolicy.RUNTIME)//validação será executada em tempo de execução
public @interface UsernameConstraint {//Validação customizada
    String message() default "Invalid username";//Mensagem de retorno
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};//Nível em que ira ocorrer tal erro
}
