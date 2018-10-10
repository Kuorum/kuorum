package kuorum.core.exception

import org.codehaus.groovy.grails.exceptions.GrailsException

/**
 * Created with IntelliJ IDEA.
 * User: iduetxe
 * Date: 5/04/13
 * Time: 19:15
 * To change this template use File | Settings | File Templates.
 */
class KuorumException extends GrailsException{

    ArrayList<KuorumExceptionData> errors

    public KuorumException(String message, KuorumExceptionData error){
        super(message)
        this.errors = new ArrayList<KuorumExceptionData>();
        this.errors.add(error)
    }
    public KuorumException(String message, ArrayList<KuorumExceptionData> errors){
        super(message)
        this.errors = errors
    }
    public KuorumException(String message){
        super(message)
        this.errors = new ArrayList<KuorumExceptionData>()
    }

    public KuorumException(String message, String code){
        super(message)
        KuorumExceptionData kuorumExceptionData = new KuorumExceptionData(code:code)
        this.errors = new ArrayList<KuorumExceptionData>()
        this.errors.add(kuorumExceptionData)
    }
    public KuorumException(String message, String code, List<Object> args){
        super(message)
        KuorumExceptionData kuorumExceptionData = new KuorumExceptionData(code:code, args: args as String[])
        this.errors = new ArrayList<KuorumExceptionData>()
        this.errors.add(kuorumExceptionData)
    }

    public KuorumException(KuorumExceptionData error){
        super(error.code)
        this.errors = new ArrayList<KuorumExceptionData>();
        this.errors.add(error)
    }


    public void addError(KuorumExceptionData error){
        this.errors.add(error)
    }
}
