package kuorum

import kuorum.core.FileGroup
import kuorum.project.Project
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

        def labelCssClass = attrs.labelCssClass?:''
        KuorumFile kuorumFile = null
        FileGroup fileGroup = attrs.fileGroup
        def label = message(code: "${command.class.name}.${field}.label")
        def value = ""
        def imageUrl = ""
        if (kuorumImageId)
            kuorumFile = KuorumFile.get(new ObjectId(kuorumImageId))

        if (!kuorumFile){
            kuorumImageId = "_${field}_NEW_"
        }else{
            value = kuorumImageId
            imageUrl = kuorumFile.url
        }

        def error = hasErrors(bean: command, field: field,'error')
        def errorMessage = ''
        if(error){
            errorMessage = g.fieldError(bean: command, field: field)
        }
        def model = [
                imageId: kuorumImageId,
                value:value,
                fileGroup:fileGroup,
                imageUrl:imageUrl,
                name:field,
                labelCssClass:labelCssClass,
                label:label,
                errorMessage:errorMessage
        ]
        out << g.render(template:'/layouts/form/uploadImage', model:model)
    }

    def editPdf ={attrs ->
        def command = attrs.command
        def field = attrs.field
        def kuorumPdfId = command."$field"

        KuorumFile kuorumFile = null
        FileGroup fileGroup = attrs.fileGroup
        def label = message(code: "${command.class.name}.${field}.label")
        def value = ""
        def pdfUrl = ""
        String fileName = ""
        if (kuorumPdfId)
            kuorumFile = KuorumFile.get(new ObjectId(kuorumPdfId))

        if (!kuorumFile){
            kuorumPdfId = "_${field}_NEW_"
        }else{
            value = kuorumPdfId
            pdfUrl = kuorumFile.url
            fileName = kuorumFile.originalName
        }

        def error = hasErrors(bean: command, field: field,'error')
        def errorMessage = ''
        if(error){
            errorMessage = g.fieldError(bean: command, field: field)
        }
        def model = [
                pdfId: kuorumPdfId,
                value:value,
                fileGroup:fileGroup,
                pdfUrl:pdfUrl,
                name:field,
                fileName:fileName,
                label:label,
                errorMessage:errorMessage
        ]
        out << g.render(template:'/layouts/form/uploadPdf', model:model)
    }

//    private static final Integer NUM_CHARS_SHORTEN_URL = 19 //OWLY
    private static final Integer NUM_CHARS_TWITTER_URL = 22 // Twitter change all urls to t.co and its size will be 22 (before february of 2014 was 20)
    private static final Integer NUM_EXTRA_SPACE = 2 // Between text and hastag, and between hastag and shortUrl

    def postTitleLimitChars = {attrs->
        Project project = attrs.project
        out << grailsApplication.config.kuorum.post.titleSize - project.hashtag.size() - NUM_CHARS_TWITTER_URL -NUM_EXTRA_SPACE

    }

    def input={attrs->
        def command = attrs.command
        def field = attrs.field

        def id = attrs.id?:field
        def helpBlock = attrs.helpBlock?:''
        def type = attrs.type?:'text'
        def required = attrs.required?'required':''
        def cssClass = attrs.cssClass?:'form-control input-lg'
        def labelCssClass = attrs.labelCssClass?:''
        def maxlength = attrs.maxlength?"maxlength='${attrs.maxlength}'":''
        def clazz = command.metaClass.properties.find{it.name == field}.type
        def label = message(code: "${command.class.name}.${field}.label")
        def placeHolder = attrs.placeHolder?:message(code: "${command.class.name}.${field}.placeHolder", default: '')

        def value = command."${field}"?:''
        def error = hasErrors(bean: command, field: field,'error')

        ConstrainedProperty constraints = command.constraints.find{it.key.toString() == field}.value
        MaxSizeConstraint maxSizeConstraint = constraints.appliedConstraints.find{it instanceof MaxSizeConstraint}
        def maxSize = maxSizeConstraint?.maxSize?:0


        out <<"""
            <input type="${type}" name="${field}" class="${cssClass} ${error?'error':''}" id="${id}" ${required} ${maxlength} placeholder="${placeHolder}" value="${value}">
        """
        if(error){
            out << "<span for='${id}' class='error'>${g.fieldError(bean: command, field: field)}</span>"
        }

        if (helpBlock){
            out << "<p class='help-block'>${helpBlock}</p>"
        }

        if (maxSize){
            out << """
            <div id="charInit" class="hidden">${message(code:'form.textarea.limitChar')}<span>${maxSize}</span></div>
            <div id="charNum" class="help-block">${message(code:'form.textarea.limitChar.left')} <span></span> ${message(code:'form.textarea.limitChar.characters')}</div>
            """
        }
    }

    def date={attrs ->
        def command = attrs.command
        def field = attrs.field

        def id = attrs.id?:field
        def required = attrs.required?'required':''
        def cssClass = attrs.cssClass?:'form-control input-lg'
        def placeHolder = attrs.placeHolder?:message(code: "${command.class.name}.${field}.placeHolder", default: '')

        def error = hasErrors(bean: command, field: field,'error')
        //TODO: ¿Internacionalizar el formato a mostrar de la fecha?
        def value = command."${field}"?command."${field}".format('dd/MM/yyyy'):''
        out <<"""
            <div class="input-group date">
            <input type="text" name="${field}" class="${cssClass} ${error?'error':''}" placeholder="${placeHolder}" id="${id}" required aria-required="${required}" value="${value}">
            <span class="input-group-addon"><a href="#" class="datepicker"><span class="fa fa-calendar fa-lg"></span></a></span>
        """
        if(error){
            out << "<span for='${id}' class='error'>${g.fieldError(bean: command, field: field)}</span>"
        }
    }

    def url={attrs->
        def command = attrs.command
        def field = attrs.field

        def id = attrs.id?:field
        def cssClass = attrs.cssClass?:'form-control input-lg'
        def labelCssClass = attrs.labelCssClass?:''
        def label = message(code: "${command.class.name}.${field}.label")
        def placeHolder = attrs.placeHolder?:message(code: "${command.class.name}.${field}.placeHolder", default: '')
        def value = command."${field}"?:''

        def error = hasErrors(bean: command, field: field,'error')
        out <<"""
            <label for="${id}" class="${labelCssClass}">${label}</label>
            <input name="videoPost" type="url" value="${value}" class="${cssClass}" id="${id}" placeholder="${placeHolder}">
        """
        if(error){
            out << "<span for='${id}' class='error'>${g.fieldError(bean: command, field: field)}</span>"
        }
    }

    def dynamicListInput={attrs->
        def command = attrs.command
        def field = attrs.field

        def id = attrs.id?:field
        def helpBlock = attrs.helpBlock?:''
        def type = attrs.type?:'text'
        def required = attrs.required?'required':''
        def cssClass = attrs.cssClass?:'form-control input-lg'
        def labelCssClass = attrs.labelCssClass?:''
        def maxlength = attrs.maxlength?"maxlength='${attrs.maxlength}'":''

        def clazz = command.metaClass.properties.find{it.name == field}.type
        def label = message(code: "${command.class.name}.${field}.label")
        def labelAdd = message(code: "${command.class.name}.${field}.add")
        def labelRemove = message(code: "${command.class.name}.${field}.remove")
        def placeHolder = attrs.placeHolder?:message(code: "${command.class.name}.${field}.placeHolder", default: '')

        def listVals = command."${field}"?:[]
        def error = hasErrors(bean: command, field: field,'error')
        out << "<div class='dynamicList'>"
        out << """<label for="${id}" class="${labelCssClass}">${label}</label>"""
        def count = 0;
        listVals?:listVals << ""
        listVals.each{value ->
            def fieldList = "${field}[$count]"
            out <<"<div class='list-item'>"
            out <<"""
                <input type="${type}" name="${fieldList}" class="${cssClass} ${error?'error':''}" id="${id}" ${required} ${maxlength} placeholder="${placeHolder}" value="${value}">
            """
            if(error){
                out << "<span for='${id}' class='error'>${g.fieldError(bean: command, field: fieldList)}</span>"
            }

            if (helpBlock){
                out << "<p class='help-block'>${helpBlock}</p>"
            }
            out <<"<a href='#' class='list-remove'>${labelRemove}</a></div>"
            count++
        }
        out << """
            <a href="#" class="list-add">${labelAdd}</a>
             </div>
                """
    }

    def socialInput={attrs ->
        def command = attrs.command
        def field = attrs.field
        def cssIcon = attrs.cssIcon

        def label = message(code: "${command.class.name}.${field}.label")
        def placeHolder = attrs.placeHolder?:message(code: "${command.class.name}.${field}.placeHolder", default: '')
        def value = command."${field}"?:''

        def error = hasErrors(bean: command, field: field,'error')
        out <<"""
            <label for="${field}">${label}</label>
            <div class="input-group">
                <span class="input-group-addon"><span class="fa ${cssIcon} fa-fw"></span></span>
                <input class="form-control ${error?'error':''}" value="${value}" id="${field}" name="${field}" type="text" placeholder="${placeHolder}">
            </div>
        """
        if (error){
            out << "<span for='${field}' class='error'>${g.fieldError(bean: command, field: field)}</span>"
        }
    }

    def selectMultipleCommissions={attrs->
        def command = attrs.command
        def field = attrs.field

        def id = attrs.id?:field
        def label = message(code: "${command.class.name}.${field}.label")
        def subLabel = message(code: "${command.class.name}.${field}.subLabel")
        def helpBlock = message(code: "${command.class.name}.${field}.helpBlock")
        def checkedCommissions = command."${field}"
        def errorMessage =''
        if (hasErrors(bean: command, field: field,'error')){
            errorMessage = g.fieldError(bean: command, field: id)
        }
        def model = [
                field:field,
                label:label,
                errorMessage:errorMessage,
                checkedCommissions:checkedCommissions,
                subLabel:subLabel,
                helpBlock:helpBlock
        ]
        out << render(template:'/layouts/form/commissions', model:model)
    }

    def selectEnum = {attrs->
        def command = attrs.command
        def field = attrs.field

        def id = attrs.id?:field
        def cssClass = attrs.cssClass
        def clazz = command.metaClass.properties.find{it.name == field}.type
        def label = message(code: "${clazz.name}.label")
        def error = hasErrors(bean: command, field: field,'error')
        out <<"""
            <label for="${id}">${label}</label>
            <select name="${field}" class="form-control ${error}" id="${id}">
            """
        out << "<option value=''> ${message(code:"${clazz.name}.empty")}</option>"
        clazz.values().each{
            String codeMessage = "${clazz.name}.$it"
            out << "<option value='${it}' ${it==command."$field"?'selected':''}> ${message(code:codeMessage)}</option>"
        }
        out << "</select>"
        if(error){
            out << "<span for='${id}' class='error'>${g.fieldError(bean: command, field: id)}</span>"
        }
    }

    def checkBox = {attrs ->
        def command = attrs.command
        def field = attrs.field

        def checked = command."$field"?'checked':''
        def label = message(code: "${command.class.name}.${field}.label")
        def error = hasErrors(bean: command, field: field,'error')
        out <<"""
            <label class="checkbox-inline">
                <input class="${error}" type="checkbox" name='${field}' id="${field}" ${checked} value='true' >
                ${label}
            </label>
            """
        if(error){
            out << "<span for='${field}' class='error'>${g.fieldError(bean: command, field: field)}</span>"
        }
    }

    def selectDomainObject = {attrs->
        def command = attrs.command
        def field = attrs.field
        def values = attrs.values
        def valueName=attrs.valueName?:'name'

        def required = attrs.required?'required':''
        def id = attrs.id?:"${field}.id"
        def cssClass = attrs.cssClass
        def clazz = command.metaClass.properties.find{it.name == field}.type
        def label = message(code: "${clazz.name}.label")
        def error = hasErrors(bean: command, field: field,'error')
        out <<"""
        <label for="${id}">${label}</label>
        <select name="${field}.id" class="form-control ${error}" id="${id}" ${required?'required':''}>
        """
        out << "<option value=''> ${message(code:"${clazz.name}.empty")}</option>"
        values.each{
            def valId = it.id
            def commandValId = command."$field"?.id
            def valName = it."${valueName}"
            out << "<option value='${valId}' ${valId==commandValId?'selected':''}> ${valName}</option>"
        }
        out << "</select>"
        if(error){
            out << "<span for='${id}' class='error'>${g.fieldError(bean: command, field: field)}</span>"
        }
    }

    def radioEnum = {attrs ->
        def command = attrs.command
        def field = attrs.field

        def clazz = command.metaClass.properties.find{it.name == field}.type
        def label = message(code: "${command.class.name}.${field}.label")
        def error = hasErrors(bean: command, field: field,'error')
        out << "<span class='span-label'>${label} </span>"
        clazz.values().each{
            out << "<label class='radio-inline'>"
            out << "<input type='radio' name='${field}' value='${it}' ${command."${field}"==it?'checked':''}>"
            String codeMessage = "${clazz.name}.$it"
            out << "${message(code:codeMessage)}"
            if(error){
                out << "<span for='${field}' class='error'>${g.fieldError(bean: command, field: field)}</span>"
                error = "" //Only first radio button
            }
            out << "</label>"
        }
    }

    def textArea = {attrs ->
        def command = attrs.command
        def field = attrs.field
        def rows = attrs.rows?:5

        def id = attrs.id?:field
        def value = command."$field"?:''
        def placeHolder = message(code: "${command.class.name}.${field}.placeHolder")
        def error = hasErrors(bean: command, field: field,'error')
        ConstrainedProperty constraints = command.constraints.find{it.key.toString() == field}.value
        MaxSizeConstraint maxSizeConstraint = constraints.appliedConstraints.find{it instanceof MaxSizeConstraint}
        def maxSize = maxSizeConstraint?.maxSize?:0
        def texteditor = attrs.texteditor?:''

        out << """
            <textarea name='${field}' class="form-control counted ${texteditor} ${error}" rows="${rows}" id="${id}" placeholder="${placeHolder}">${value}</textarea>
        """
        if (error){
            out << "<span for='${id}' class='error'>${g.fieldError(bean: command, field: field)}</span>"
        }

        if (maxSize){
        out << """
            <div id="charInit" class="hidden">${message(code:'form.textarea.limitChar')}<span>${maxSize}</span></div>
            <div id="charNum" class="help-block">${message(code:'form.textarea.limitChar.left')} <span></span> ${message(code:'form.textarea.limitChar.characters')}</div>
            """
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
//        }else if (constraint instanceof MatchesConstraint){
//            restrictions.append("regex: /${constraint.regex}/,")
//            String code = prefixMessage + ".${filedName}.matches"
//            String text = g.message(code:code)
//            messages.append("regex: '${text}',")
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
                out << "display.error('', '${msg}');"
            }
            out <<"""
                });
            </script>
                """

        }
    }

    private void printFieldErrors(def errors, def bean){
        if (errors){
            out << """
            <script>
                \$(document).ready(function (){
                 """
            errors.each{error ->
                String msg = tranlateErrorCode(error.codes)
                out << "display.error('${error.field}','${msg}');"
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
//            printFieldErrors(obj.errors.fieldErrors, obj)
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
