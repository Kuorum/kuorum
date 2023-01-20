package kuorum.core.annotations


import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * Handle "fake security" for the validation funnel. Mainly for QR process
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FunnelLoginSessionValid {

}
