package kuorum.core.annotations

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * Classes annotated with this annotation will have the method "mongoUpdate()"
 *
 * This method updates only the fields annotated whit "Updatable" annotation using the mongo api.
 *
 * IMPORTANT => Is not transactional
 *
 * Is used for not overwrite the possible user votes saved while the updating operation
 */
@Target(ElementType.TYPE )
@Retention(RetentionPolicy.RUNTIME)
public @interface MongoUpdatable {

}
