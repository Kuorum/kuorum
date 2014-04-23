package kuorum

import kuorum.core.FileGroup
import kuorum.law.Law
import org.bson.types.ObjectId
import org.codehaus.groovy.grails.validation.*

class FormTagLib {
    static defaultEncodeAs = 'raw'
    //static encodeAsForTags = [tagName: 'html']

    def grailsApplication

    static namespace = "formUtil"

    def editImage ={attrs ->
        def command = attrs.command
        def field = attrs.field
        def kuorumImageId = command."$field"
        KuorumFile kuorumFile = null
        FileGroup fileGroup = null
        def value = ""
        def imageUrl = ""
        if (kuorumImageId)
            kuorumFile = KuorumFile.get(new ObjectId(kuorumImageId))

        if (!kuorumFile){
            kuorumImageId = "__NEW__"
            fileGroup = attrs.fileGroup
        }else{
            value = kuorumImageId
            fileGroup =kuorumFile.fileGroup
            imageUrl = kuorumFile.url
        }
        def model = [
                imageId: kuorumImageId,
                value:value,
                fileGroup:fileGroup,
                imageUrl:imageUrl,
                name:field
        ]
        out << g.render(template:'/layouts/form/uploadImage', model:model)
    }

    private static final Integer NUM_CHARS_SHORTEN_URL = 18 //OWLY
    def postTitleLimitChars = {attrs->
        Law law = attrs.law
        out << grailsApplication.config.kuorum.post.titleSize - law.hashtag.size() - NUM_CHARS_SHORTEN_URL

    }

    def input={attrs->
        def command = attrs.command
        def name = attrs.field
        def label = attrs.label
        def placeholder = attrs.placeholder?:''
        def id = attrs.id?:name
        def helpBlock = attrs.helpBlock?:''
        def type = attrs.type?:'text'
        def required = attrs.required?'required':''
        def cssClass = attrs.cssClass?:''

        def value = command."${name}"?:''
        def error = hasErrors(bean: command, field: name,'error')
        out <<"""
            <label for="${id}">${label}</label>
            <input type="${type}" name="${name}" class="${cssClass} ${error}" id="${id}" ${required} placeholder="${placeholder}" value="${value}">
        """
        if(error){
            out << "<span for='${id}' class='error'>${g.fieldError(bean: command, field: name)}</span>"
        }

        if (helpBlock){
            out << "<p class='help-block'>${helpBlock}</p>"
        }
    }


    /* VALIDATION */

    private def getValue(def command, String field){
        def res = command
        field.split("\\.").each {res = res."$it"}
        res
    }

    private void printValidationType (restrictions, messages,constraint,filedName){
        String prefixMessage = constraint.constraintOwningClass.name
        def clazz = constraint.constraintOwningClass

        if (clazz.getDeclaredFields().find{it.name==filedName}?.type ==Integer.class){
            restrictions.append("number: true ,")
            String code = prefixMessage + ".${filedName}.notNumber"
            String text = g.message(code:code)
            messages.append("number: '${text}',")
        }
    }
    private void printValidation (restrictions, messages,constraint,filedName){
        String prefixMessage = constraint.constraintOwningClass.name

        if (constraint instanceof NullableConstraint){
            restrictions.append("required: ${!constraint.nullable} ,")
//            restrictions.append("blank: ${constraint.nullable},")
            String code = prefixMessage + ".${filedName}.nullable"
            String text = g.message(code:code)
            messages.append("required: '${text}',")
//            messages.append("blank: '${text}',")
//        }else if (constraint instanceof BlankConstraint){
//            restrictions.append("blank: ${constraint.blank},")
//            String code = prefixMessage + ".${filedName}.blank"
//            String text = g.message(code:code)
//            messages.append("blank: '${text}',")
        }else if (constraint instanceof MinConstraint && !(constraint.minValue instanceof Date)){
            restrictions.append("min: ${constraint.minValue},")
            String code = prefixMessage + ".${filedName}.min"
            String text = g.message(code:code,args:[constraint.minValue])
            messages.append("min: '${text}',")
        }else if (constraint instanceof MinSizeConstraint){
            restrictions.append("minlength: ${constraint.minSize},")
            String code = prefixMessage + ".${filedName}.min.size"
            String text = g.message(code:code,args:[constraint.minSize])
            messages.append("minlength: '${text}',")
        }else if (constraint instanceof MatchesConstraint){
            restrictions.append("regex: /${constraint.regex}/,")
            String code = prefixMessage + ".${filedName}.matches"
            String text = g.message(code:code)
            messages.append("regex: '${text}',")
        }else if (constraint instanceof MaxSizeConstraint){
            restrictions.append("maxlength: ${constraint.maxSize},")
            String code = prefixMessage + ".${filedName}.max.size"
            String text = g.message(code:code,args:[constraint.maxSize])
            messages.append("maxlength: '${text}',")
        }else if (constraint instanceof EmailConstraint){
            restrictions.append("email: true,")
            String code = prefixMessage + ".${filedName}.wrongFormat"
            String text = g.message(code:code)
            messages.append("email: '${text}',")
        }else if (constraint instanceof UrlConstraint && constraint.url){
            restrictions.append("url: true,")
            String code = prefixMessage + ".${filedName}.wrongFormat"
            String text = g.message(code:code)
            messages.append("url: '${text}',")
        }
    }


    private String tranlateErrorCode(def codes){
        String msg = ""
        codes.each {code ->
            if (!msg) msg = g.message(code: code, default: "")
        }
        if (!msg) msg = g.message(code: "${codes[codes.size()-1]}")
        msg
    }
    private void printGeneralErrors(def errors, def bean){
        if (errors){
            out << """
            <script>
                \$(document).ready(function (){
                    """
            errors.each{error ->
                String msg = tranlateErrorCode(error.codes)
                out << "displayError('', '${msg}');"
            }
            out <<"""
                });
            </script>
                """

        }
    }

    private void printFieldErrors(def errors, def bean){
        String title = g.message(code: bean.class.name +".title.error")
        if (errors){
            out << """
            <script>
                \$(document).ready(function (){
                    displayError("", "${title}");
                 """
            errors.each{error ->
                String msg = tranlateErrorCode(error.codes)
                out << "appendErrorToField('${error.field}','${msg}');"
            }

            out <<"""
                });
            </script>
                """
        }

    }

    def validateForm = {attrs, body->
        def formId = attrs.form
        def className = attrs.command
        def bean = attrs.bean
        def obj

        if (!bean)
            obj = Class.forName(className, true, Thread.currentThread().getContextClassLoader()).newInstance()
        else{
            obj =  bean
            printGeneralErrors(obj.errors.allErrors - obj.errors.fieldErrors,obj);
            printFieldErrors(obj.errors.fieldErrors, obj)
        }



        def rules = new StringBuffer("rules: {")
        def message = new StringBuffer(" messages: {")
        out << """
		<script type="text/javascript">
			\$(function (){
				\$("#${formId}").validate({
                errorClass:'error',
                errorElement:'span',
"""

        obj.constraints.each{fields ->
            String fieldName = fields.key.toString()
            ConstrainedProperty constraint = fields.value
            if (constraint.appliedConstraints){

                if (grailsApplication.isDomainClass(constraint.propertyType)){
                    fieldName = "${fieldName}.id"
                }
                rules.append("'${fieldName}':{")
                message.append("'${fieldName}':{")
                constraint.appliedConstraints.each{c ->
                    printValidation(rules, message,c,fieldName)
                    printValidationType(rules, message,c, fieldName)
                }
                rules.deleteCharAt(rules.length() - 1);
                message.deleteCharAt(message.length() - 1);
                rules.append("},")
                message.append("},")
            }
        }
        rules.deleteCharAt(rules.length() - 1);
        message.deleteCharAt(message.length() - 1);
        rules.append("}")
        message.append("}")
        out <<
                """ ${rules} , ${message}
				});
			});
			</script>
			"""
    }
}
