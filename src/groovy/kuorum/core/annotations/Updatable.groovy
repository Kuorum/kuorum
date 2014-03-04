package kuorum.core.annotations

import com.mongodb.BasicDBObject
import kuorum.core.exception.KuorumExceptionUtil

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * Domain fields marked with @Updatable are the fields that are going to be updated using
 * mongo api instead .save() gorm function
 */
@Target(ElementType.FIELD )
@Retention(RetentionPolicy.RUNTIME)
public @interface Updatable {

}
