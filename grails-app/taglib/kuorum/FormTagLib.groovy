package kuorum

import kuorum.core.FileGroup
import kuorum.core.model.CommissionType
import kuorum.core.model.RegionType
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

        def prefixFieldName=attrs.prefixFieldName?:""
        def disabled=attrs.disabled?"disabled":""
        def id = "${prefixFieldName}${attrs.id?:field}"
        def type = attrs.type?:'text'
        def required = attrs.required?'required':''
        def cssClass = attrs.cssClass?:'form-control input-lg'
        def labelCssClass = attrs.labelCssClass?:''
        def showLabel = attrs.showLabel?Boolean.parseBoolean(attrs.showLabel):false
        def showCharCounter = attrs.showCharCounter?Boolean.parseBoolean(attrs.showCharCounter):true
        def clazz = command.metaClass.properties.find{it.name == field}.type
        def label = attrs.label?:message(code: "${command.class.name}.${field}.label")
        def placeHolder = attrs.placeHolder?:message(code: "${command.class.name}.${field}.placeHolder", default: '')
        String helpBlock = attrs.helpBlock?:message(code: "${command.class.name}.${field}.helpBlock", default: '')

        def value = command."${field}"?:''
        def error = hasErrors(bean: command, field: field,'error')

        ConstrainedProperty constraints = command.constraints.find{it.key.toString() == field}.value
        MaxSizeConstraint maxSizeConstraint = constraints.appliedConstraints.find{it instanceof MaxSizeConstraint}
        def maxSize = maxSizeConstraint?.maxSize?:0
        def maxlength = maxSize?"maxlength='${attrs.maxlength}'":''
        if (maxSize > 0){
            cssClass += " counted"
        }

        if (showLabel){
            out << "<label for='${prefixFieldName}${field}'>${label}</label>"
        }
        out <<"""
            <input type="${type}" name="${prefixFieldName}${field}" class="${cssClass} ${error?'error':''}" id="${id}" ${required} ${maxlength} placeholder="${placeHolder}" value="${value}" ${disabled}>
        """
        if(error){
            out << "<span for='${id}' class='error'>${g.fieldError(bean: command, field: field)}</span>"
        }

        if (helpBlock){
            out << "<p class='help-block'>${helpBlock}</p>"
        }

        if (maxSize && showCharCounter){
            out << """
            <div id="charInit_${field}" class="hidden">${message(code:'form.textarea.limitChar')} <span>${maxSize}</span></div>
            <div id="charNum_${field}" class="charNum">${message(code:'form.textarea.limitChar.left')} <span>${maxSize}</span> ${message(code:'form.textarea.limitChar.characters')}</div>
            """
        }
    }

    def password={attrs ->
        def command = attrs.command
        def field = attrs.field

        def id = attrs.id?:field
        def label = message(code: "${command.class.name}.${field}.label")
        def placeHolder = attrs.placeHolder?:message(code: "${command.class.name}.${field}.placeHolder", default: '')
        def prefixFieldName=attrs.prefixFieldName?:""
        def showLabel = attrs.showLabel?Boolean.parseBoolean(attrs.showLabel):false
        def value = command."${field}"?:''
        def error = hasErrors(bean: command, field: field,'error')
        if (showLabel){
            out << "<label for='${prefixFieldName}${field}'>${label}</label>"
        }else{
            out << "<label class='sr-only' for='${prefixFieldName}${field}'>${label}</label>"
        }
        out <<"""
                <div class="input-append input-group">
                    <input type="password" required aria-required="true" id="${id}" name="${field}" class="form-control input-lg" placeholder="${g.message(code:"login.email.form.password.label")}" value="" data-ays-ignore="true">
                    <span tabindex="100" class="add-on input-group-addon">
                        <label><input type="checkbox" name="show-${id}" id="show-${id}" data-ays-ignore="true">${message(code:'login.email.form.password.show')}</label>
                    </span>
                </div>
            """
        if(error){
            out << "<span for='${id}' class='error'>${g.fieldError(bean: command, field: field)}</span>"
        }
    }

    def dynamicComplexInputs={attrs, body ->
        def command = attrs.command
        def field = attrs.field
        def listClassName = attrs.listClassName
        def formId = attrs.formId
        def id = attrs.id?:field
        def customRemoveButton=attrs.customRemoveButton?Boolean.parseBoolean(attrs.customRemoveButton):false

        List listCommands = command."${field}"

        String removeButton = customRemoveButton?'':"""
            <fieldset class="row">
                <div class="col-md-12 text-right">
                    <button type="button" class="btn btn-transparent removeButton"><i class="fa fa-trash"></i></button>
                </div>
            </fieldset>
"""

        String addButton =  """
        <fieldset class="row dynamic-fieldset-addbutton">
            <div class="form-group">
                <div class="col-md-12">
                    <button type="button" class="btn btn-default addButton"><i class="fa fa-plus"></i></button>
                </div>
            </div>
        </fieldset>
        """
        out << addButton

        def obj= Class.forName(listClassName, true, Thread.currentThread().getContextClassLoader()).newInstance()
        out << "<div class='hide dynamic-fieldset' id='${id}-template'>"
        out << removeButton
        out << body([listCommand:obj, prefixField:""])
        out << "</div>"

        Integer idx = listCommands.size();
        listCommands.each{
            idx --;
            out <<"<div class='dynamic-fieldset' data-dynamic-list-index='${idx}' >"
            out << body([listCommand:it, prefixField:"${field}[${idx}].", ])
            out << removeButton
            out <<"</div>"
        }

        ConstrainedProperty constraints = command.constraints.find{it.key.toString() == field}?.value
        def maxSize = 0
        if (constraints){
            MaxSizeConstraint maxSizeConstraint = constraints.appliedConstraints.find{it instanceof MaxSizeConstraint}
            maxSize = maxSizeConstraint?.maxSize?:0
        }

        def fields = obj.properties.collect{prop,val -> if(!(prop in ["metaClass","class", "dbo"])) return prop}.findAll{it}
        def rulesAndMessages = generateRulesAndMessages(obj)
        String validationDataVarName = "validationRules_${field}"
        String validationDataVarIndex = "validationIndex_${field}"
        out << render(
                template: "/layouts/form/dynamicInputs/dynamicInputsJs",
                model:[
                        validationDataVarName:validationDataVarName,
                        validationDataVarNameValue:"{${rulesAndMessages.rules}, ${rulesAndMessages.message}}",
                        validationDataVarIndex:validationDataVarIndex,
                        validationDataVarIndexValue: listCommands.size(),
                        validationDataMaxSize:maxSize,
                        templateId : "${id}-template",
                        fields:fields,
                        parentField:field,
                        formId:formId
                ])

    }

    def date={attrs ->
        def command = attrs.command
        def field = attrs.field

        def prefixFieldName=attrs.prefixFieldName?:""
        def id = attrs.id?:field
        def required = attrs.required?'required':''
        def cssClass = attrs.cssClass?:'form-control input-lg'
        def placeHolder = attrs.placeHolder?:message(code: "${command.class.name}.${field}.placeHolder", default: '')
        def showLabel = attrs.showLabel?Boolean.parseBoolean(attrs.showLabel):false
        def label = message(code: "${command.class.name}.${field}.label")

        def error = hasErrors(bean: command, field: field,'error')
        //TODO: ¿Internacionalizar el formato a mostrar de la fecha?
        def value = command."${field}"?command."${field}".format('dd/MM/yyyy'):''
        if (showLabel){
            out << "<label for='${prefixFieldName}${field}'>${label}</label>"
        }
        out <<"""
            <div class="input-group date">
                <input type="text" name="${prefixFieldName}${field}" class="${cssClass} ${error?'error':''}" placeholder="${placeHolder}" id="${id}" aria-required="${required}" value="${value}">
                <span class="input-group-addon"><a href="#" class="datepicker"><span class="fa fa-calendar fa-lg"></span></a></span>
            </div>
        """
        if(error){
            out << "<span for='${id}' class='error'>${g.fieldError(bean: command, field: field)}</span>"
        }
    }

    def regionInput={attrs->

        def command = attrs.command
        def field = attrs.field
        def id = attrs.id?:field
        def fieldId = field +".id"//Same as EditUserProfileCommand.bindingRegion
        String extraCss = attrs.extraCss?:""
        def showLabel = attrs.showLabel?Boolean.parseBoolean(attrs.showLabel):false
        Region regionValue = command."${field}"?:null
        def value = regionValue?.iso3166_2?:''
        def showedValue = regionValue?.name?:""

        def label = attrs.label?:message(code: "${command.class.name}.${field}.label")
        def placeHolder = attrs.placeHolder?:message(code: "${command.class.name}.${field}.placeHolder", default: '')
        String helpBlock = attrs.helpBlock?:message(code: "${command.class.name}.${field}.helpBlock", default: '')

        if (showLabel){
            out << "<label for='${field}'>${label}</label>"
        }
        def error = hasErrors(bean: command, field: field, 'error')
        def cssClass ="form-control input-lg";
        out << "<input type='text' class='${extraCss} ${cssClass} input-region ${error?'error':''}' placeholder='${placeHolder}' name='${field}' value='${showedValue}' data-real-input-id='${fieldId}'>"
        out << "<input type='hidden' class='' name='${fieldId}' value='${value}' id=${fieldId}>"
        if(error){
            out << "<span for='${id}' class='error'>${g.fieldError(bean: command, field: field)}</span>"
        }
        if (helpBlock){
            out << "<p class='help-block'>${helpBlock}</p>"
        }
    }

    def url={attrs->
        def command = attrs.command
        def field = attrs.field

        def id = attrs.id?:field
        def showLabel = attrs.showLabel?Boolean.parseBoolean(attrs.showLabel):false
        def prefixFieldName=attrs.prefixFieldName?:""
        def cssClass = attrs.cssClass?:'form-control input-lg'
        def labelCssClass = attrs.labelCssClass?:''
        def label = message(code: "${command.class.name}.${field}.label")
        def placeHolder = attrs.placeHolder?:message(code: "${command.class.name}.${field}.placeHolder", default: '')
        def value = command."${field}"?:''

        if (showLabel){
            out << "<label for='${prefixFieldName}${field}'>${label}</label>"
        }
        def error = hasErrors(bean: command, field: field,'error')
        def stringError = message(code: "${command.class.name}.${field}.wrongFormat") //It is for fix a problem on dynamic list inputs
        out <<"""
            <input name="${prefixFieldName}${field}" type="url" data-msg-url="${stringError}" value="${value}" class="${cssClass} ${error?'error':''}" id="${id}" placeholder="${placeHolder}">
        """
        if(error){
            out << "<span for='${id}' class='error'>${g.fieldError(bean: command, field: field)}</span>"
        }
    }

    def dynamicListInput={attrs->
        def command = attrs.command
        def field = attrs.field

        def autocompleterUrl = attrs.autocompleteUrl?:""
        def prefixFieldName=attrs.prefixFieldName?:""
        def disabled=attrs.disabled?"disabled":""
        def id = "${prefixFieldName}${attrs.id?:field}"
        def type = attrs.type?:'text'
        def required = attrs.required?'required':''
        def cssClass = attrs.cssClass?:'form-control input-lg'
        def labelCssClass = attrs.labelCssClass?:''
        def showLabel = attrs.showLabel?Boolean.parseBoolean(attrs.showLabel):false
        def maxlength = attrs.maxlength?"maxlength='${attrs.maxlength}'":''
        def clazz = command.metaClass.properties.find{it.name == field}.type
        def label = attrs.label?:message(code: "${command.class.name}.${field}.label")
        def placeHolder = attrs.placeHolder?:message(code: "${command.class.name}.${field}.placeHolder", default: '')
        String helpBlock = attrs.helpBlock?:message(code: "${command.class.name}.${field}.helpBlock", default: '')

        def value = command."${field}"?.join(",")?:''
        def error = hasErrors(bean: command, field: field,'error')

        if (showLabel){
            out << "<label for='${prefixFieldName}${field}'>${label}</label>"
        }
        out <<"""
            <input type="${type}" name="${prefixFieldName}${field}" class="${cssClass} ${error?'error':''}" id="${id}" ${required} ${maxlength} placeholder="${placeHolder}" value="${value}" ${disabled}>
        """
        if(error){
            out << "<span for='${id}' class='error'>${g.fieldError(bean: command, field: field)}</span>"
        }

        if (helpBlock){
            out << "<p class='help-block'>${helpBlock}</p>"
        }

        out << """
            <script>
            \$(function(){
                \$('#${id}').tagsInput(
                        {
                            'autocomplete_url':"${autocompleterUrl}",
                            'autocomplete':{
                                paramName:'term',
                                width:'100%',
                                delay:'300',
                                onSelect: function( event ) {
                                    if (!\$("#${id}").tagExist(event.value)){
                                        \$('#${id}').addTag(event.value)
                                        \$('#${id}_tag').focus()
                                    }else{
                                        \$('#${id}_tag').addClass('not_valid');
                                    }
                                }},
                            'width':'auto',
                            'height':'inherit',
                            'delimiter': [',',';'],
                            'defaultText':'',
                            onChange: function(elem, elem_tags)
                            {

                            }
                        })
            })
        </script>
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
        def cssLabel=attrs.cssLabel?:""
        def clazz = command.metaClass.properties.find{it.name == field}.type
        def label = message(code: "${clazz.name}.label")
        def error = hasErrors(bean: command, field: field,'error')
        out <<"""
            <label for="${id}" class="${cssLabel}">${label}</label>
            <select name="${field}" class="form-control input-lg ${error}" id="${id}">
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

    def selectBirthYear = {attrs->
        def command = attrs.command
        def field = attrs.field

        def id = attrs.id?:field
        def cssClass = attrs.cssClass
        def clazz = command.class
        def label = message(code: "${clazz.name}.${field}.label")
        def error = hasErrors(bean: command, field: field,'error')
        out <<"""
            <label for="${id}" class="${cssClass}">${label}</label>
            <select name="${field}" class="form-control input-lg ${error}" id="${id}">
            """
        out << "<option value=''> ${message(code:"${clazz.name}.${field}.empty")}</option>"
        Integer startYear = 1900;
        Integer endYear = Calendar.getInstance().get(Calendar.YEAR) - 16
        (startYear..endYear).each{
            out << "<option value='${it}' ${it==command."$field"?'selected':''}> ${it}</option>"
        }
        out << "</select>"
        if(error){
            out << "<span for='${id}' class='error'>${g.fieldError(bean: command, field: id)}</span>"
        }
    }

    def selectNation = {attrs->
        def command = attrs.command
        def field = attrs.field

        def id = attrs.id?:field
        def cssClass = attrs.cssClass

        def clazz = command.metaClass.properties.find{it.name == field}.type
        def label = message(code: "${clazz.name}.label")
        def error = hasErrors(bean: command, field: field,'error')
        out <<"""
            <label for="${id}" class="${cssClass}">${label}</label>
            <select name="${field}" class="form-control input-lg ${error}" id="${id}">
            """
        out << "<option value=''> ${message(code:"${clazz.name}.empty")}</option>"
        List<Region> countries = Region.findAllByRegionType(RegionType.NATION)
        countries.each{
            String codeMessage = "${clazz.name}.${it.iso3166_2}"
            out << "<option value='${it.id}' ${it.id==command."$field"?.id?'selected':''}> ${message(code:codeMessage, default: it.name)}</option>"
        }
        out << "</select>"
        if(error){
            out << "<span for='${id}' class='error'>${g.fieldError(bean: command, field: id)}</span>"
        }
    }


    def checkBox = {attrs ->
        def command = attrs.command
        def field = attrs.field

        def prefixFieldName=attrs.prefixFieldName?:""
        def checked = command."$field"?'checked':''
        def label = message(code: "${command.class.name}.${field}.label")
        def error = hasErrors(bean: command, field: field,'error')
        out <<"""
            <label class="checkbox-inline">
                <input class="${error}" type="checkbox" name='${prefixFieldName}${field}' id="${field}" ${checked} value='true' >
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
        def label = message(code: "${command.class.name}.${field}.label", default: '')
        label = label?:message(code: "${clazz.name}.label")
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
        def labelCssClass = attrs.labelCssClass?:""

        def deleteOptions = attrs.deleteOptions?:[]
        def clazz = command.metaClass.properties.find{it.name == field}.type
        def label = message(code: "${command.class.name}.${field}.label")
        def error = hasErrors(bean: command, field: field,'error')
        def values = clazz.values() - deleteOptions
        out << "<div class='groupRadio'>"
        out << "<span class='span-label ${labelCssClass}'>${label} </span>"
        values.each{
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
        out << "</div>"
    }

    def textArea = {attrs ->
        def command = attrs.command
        def field = attrs.field
        def rows = attrs.rows?:5

        def id = attrs.id?:field
        def prefixFieldName=attrs.prefixFieldName?:""
        def value = command."$field"?:''
        def placeHolder = message(code: "${command.class.name}.${field}.placeHolder")
        def error = hasErrors(bean: command, field: field,'error')
        ConstrainedProperty constraints = command.constraints.find{it.key.toString() == field}.value
        MaxSizeConstraint maxSizeConstraint = constraints.appliedConstraints.find{it instanceof MaxSizeConstraint}
        def maxSize = maxSizeConstraint?.maxSize?:0
        def texteditor = attrs.texteditor?:''
        def label = message(code: "${command.class.name}.${field}.label")
        def showLabel = attrs.showLabel?Boolean.parseBoolean(attrs.showLabel):false
        if (showLabel){
            out << "<label for='${prefixFieldName}${field}'>${label}</label>"
        }
        out << """
            <textarea name='${prefixFieldName}${field}' class="form-control ${maxSize?"counted":""} ${texteditor} ${error}" rows="${rows}" id="${prefixFieldName}${id}" placeholder="${placeHolder}">${value}</textarea>
        """
        if (error){
            out << "<span for='${prefixFieldName}${id}' class='error'>${g.fieldError(bean: command, field: field)}</span>"
        }

        if (maxSize && !texteditor){
        out << """
            <div id="charInit_${prefixFieldName}${field}" class="hidden">${message(code:'form.textarea.limitChar')}<span>${maxSize}</span></div>
            <div id="charNum_${prefixFieldName}${field}" class="charNum">${message(code:'form.textarea.limitChar.left')} <span>${maxSize}</span> ${message(code:'form.textarea.limitChar.characters')}</div>
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
        def dirtyControl = Boolean.parseBoolean(attrs.dirtyControl)
        def obj

        if (!bean)
            obj = Class.forName(className, true, Thread.currentThread().getContextClassLoader()).newInstance()
        else{
            obj =  bean
            printGeneralErrors(obj.errors.allErrors - obj.errors.fieldErrors,obj);
//            printFieldErrors(obj.errors.fieldErrors, obj)
        }


        out << """
		<script type="text/javascript">
			\$(function (){
				\$("#${formId}").validate({
                errorClass:'error',
                errorPlacement: function(error, element) {
                    if(element.attr('id') == 'deadline'){
                        error.appendTo(element.parent("div").parent("div"));
                    }else if(stringStartsWith(element.attr('id'), 'pretty-check-')){
                        error.appendTo(element.parents(".interestContainer"));
                    }else{
                        error.insertAfter(element);
                    }
                },
                errorElement:'span',
        """
        def rulesAndMessages = generateRulesAndMessages(obj)
        out <<
                """ ${rulesAndMessages.rules} , ${rulesAndMessages.message}
				});
			});
			</script>
			"""
        if (dirtyControl){
            out << """
            <script>
                \$(function(){
                    formHelper.dirtyFormControl.prepare(\$("#${formId}"))
                })
            </script>
            """
        }
    }

    private Map generateRulesAndMessages(def obj){
        def rules = new StringBuffer("rules: {")
        def message = new StringBuffer(" messages: {")
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
        return [rules:rules, message:message]
    }

    def telephoneWithPrefix = {attrs, body->
        def command = attrs.command
        def field = attrs.field

        def value = command."$field"?:''
        String codeLabel = attrs.codeLabel?:'dashboard.userProfile.incompleteDate.phonePrefix.label'
        String selectId = attrs.selectId?:'phonePrefix'
        String selectCssClass = attrs.selectCssClass?:'form-control input-lg'
        List<Region> regions = Region.findAllByRegionType(RegionType.NATION)
        out << """
                <label for="${field}" class="sr-only">${message(code:codeLabel )}</label>
                <select name="${field}" class="${selectCssClass}" id="${selectId}">
                """
        regions.each {Region region ->
            String prefixPhone = region['prefixPhone'] //Dynamic attribute
            String checked = prefixPhone == value?"selected='selected'":'';
            out << "<option value='${prefixPhone}' ${checked}>${prefixPhone}</option>"
        }
        out << "</select>"
    }
}
