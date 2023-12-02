/**
 * 
 */
package com.sumadhura.employeeservice.service.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
/**
 * This Annotation is responsible to Print Error Mail. 
 * 
 * @author Venkat_Koniki
 * @since 08.04.2019
 * @time 07:06PM
 */
public @interface ErrorMail {

}
