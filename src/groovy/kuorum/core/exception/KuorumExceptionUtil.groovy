package kuorum.core.exception

/**
 * Created with IntelliJ IDEA.
 * User: iduetxe
 * Date: 5/04/13
 * Time: 19:23
 * To change this template use File | Settings | File Templates.
 */
class KuorumExceptionUtil {
    /**
     * A partir de una excepcion rellena un validatable (command o core) con los errores de la excepcion
     * @param ape
     * @param cmd Deve de ser grails.validation.Validateable
     */
    public static void fillCommandWithException(KuorumException ape, def cmd){
        ape.errors.each {error ->
            if (error.field && cmd.class.getFields().collect{it.name}.contains(error.field)){
                cmd.errors.rejectValue(error.field,error.code)
            }else{
                cmd.errors.reject(error.code)
            }
        }
    }

    /**
     * Recorre el objeto validatable (command o core), obtiene los errores y los introduce en una excepcion para que se
     * pueda lanzar desde el servicio. El message es el mensaje  de log
     * @param cmd deve de ser grails.validation.Validateable
     * @return
     */
    public static KuorumException createExceptionFromValidatable(def cmd, String message){
        KuorumException ape = new KuorumException(message)
        cmd.errors.allErrors.each { error ->
            KuorumExceptionData aped = new KuorumExceptionData();
            if (error.field) aped.field = error.field
            String errorType = error.codes[error.codes.size() -1].split("\\.")[0]
            //aped.code = error.codes[11]    //core.Project.field.errorType
            aped.code = "${cmd.class.name}.${error.field}.${errorType}"
            ape.addError(aped)
        }
        return ape
    }
}
