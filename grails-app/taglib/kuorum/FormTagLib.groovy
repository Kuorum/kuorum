package kuorum

import grails.plugin.springsecurity.SpringSecurityService
import kuorum.core.FileGroup
import kuorum.core.exception.KuorumException
import kuorum.register.KuorumUserSession
import kuorum.web.constants.WebConstants
import org.bson.types.ObjectId
import org.codehaus.groovy.grails.validation.*
import org.kuorum.rest.model.communication.CampaignRSDTO
import org.kuorum.rest.model.communication.CampaignTypeRSDTO
import org.kuorum.rest.model.communication.event.EventRSDTO
import org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetRSDTO
import org.kuorum.rest.model.communication.survey.QuestionOptionRSDTO
import org.kuorum.rest.model.communication.survey.QuestionOptionTypeRDTO
import org.kuorum.rest.model.communication.survey.QuestionRSDTO
import org.kuorum.rest.model.communication.survey.SurveyRSDTO
import org.kuorum.rest.model.communication.survey.answer.QuestionAnswerFilesRDTO
import org.kuorum.rest.model.contact.ContactRSDTO
import org.kuorum.rest.model.geolocation.RegionRSDTO
import org.kuorum.rest.model.notification.campaign.NewsletterRSDTO
import org.springframework.context.i18n.LocaleContextHolder
import payment.campaign.CampaignService
import payment.campaign.NewsletterService
import payment.campaign.ParticipatoryBudgetService
import payment.campaign.event.EventService
import payment.contact.ContactService

class FormTagLib {
    static defaultEncodeAs = 'raw'
    //static encodeAsForTags = [tagName: 'html']

    def grailsApplication
    SpringSecurityService springSecurityService
    RegionService regionService
    EventService eventService
    ParticipatoryBudgetService participatoryBudgetService
    CampaignService campaignService
    NewsletterService newsletterService
    ContactService contactService

    static namespace = "formUtil"

    def uploadCampaignImages = {attrs, body->
        String field = attrs.field
        CampaignRSDTO campaign = attrs.campaign
        def model = [
            fileGroup:FileGroup.MASS_MAIL_IMAGE,
            campaign:campaign,
            requestEndPoint:g.createLink(mapping:'politicianCampaignsUploadImages', params: [campaignId:campaign.id]),
            sessionEndPoint:g.createLink(mapping:'politicianCampaignsListImages', params: [campaignId:campaign.id]),
            body:body,
            field:field
        ]
        out << g.render(template:'/layouts/form/uploadMultiImage', model:model)
    }

    def editImage ={attrs ->
        def command = attrs.command
        def field = attrs.field
        def kuorumImageId = command."$field"

        def labelCssClass = attrs.labelCssClass?:''
        def cssClass = attrs.cssClass?:''
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
                cssClass:cssClass,
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
        def prefixFieldName=attrs.prefixFieldName?:""

        FileGroup fileGroup = attrs.fileGroup
        def label = message(code: "${command.class.name}.${field}.label")
        def placeHolder = message(code: "${command.class.name}.${field}.placeHolder", default: message(code:'form.uploadFile.placeHolder', default: 'Upload file'))
        def value = ""
        def pdfUrl = ""
        String fileName = ""

        KuorumFile kuorumFile = null

        if (kuorumPdfId  && kuorumPdfId instanceof KuorumFile){
            kuorumFile = kuorumPdfId
            kuorumPdfId = kuorumFile.id.toString()
        }else if (kuorumPdfId ){
            kuorumFile = KuorumFile.get(new ObjectId(kuorumPdfId))
        }
        String fieldName = prefixFieldName+field
        Class fieldType = command.class.metaClass.properties.find { it.name == "$field" }.type
        if (fieldType == KuorumFile){
            fieldName += ".id"
        }

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
                name:fieldName,
                fileName:fileName,
                label:label,
                placeHolder:placeHolder,
                errorMessage:errorMessage
        ]
        out << g.render(template:'/layouts/form/uploadPdf', model:model)
    }

    def uploadCampaignFiles = {attrs ->
        CampaignRSDTO campaignRSDTO = attrs.campaign
        String label = attrs.label
        def actionUpload
        def actionDelete
        List<String> alreadyUploadedFiles
        if (campaignRSDTO.campaignType != CampaignTypeRSDTO.BULLETIN){
            alreadyUploadedFiles = campaignService.getFiles(campaignRSDTO)
            actionUpload = g.createLink(mapping:'ajaxUploadCampaignFile', params: campaignRSDTO.encodeAsLinkProperties())
            actionDelete = g.createLink(mapping:'ajaxDeleteCampaignFile', params: campaignRSDTO.encodeAsLinkProperties())

        }else{
            KuorumUserSession user =  springSecurityService.principal
            alreadyUploadedFiles = newsletterService.getFiles(user, campaignRSDTO)
            alreadyUploadedFiles = alreadyUploadedFiles.collect{it.split('\\?').first()}
            actionUpload = g.createLink(mapping:'ajaxUploadMassMailingAttachFile', params: [campaignId: campaignRSDTO.id])
            actionDelete = g.createLink(mapping:'ajaxDeleteMassMailingAttachFile', params: [campaignId: campaignRSDTO.id])
        }
        alreadyUploadedFiles = alreadyUploadedFiles.collect{it.split('\\?').first()}
        def model = [
                alreadyUploadedFiles:alreadyUploadedFiles,
                elementId: campaignRSDTO.id,
                disabled: false,
                confirmRemoveFile: true,
                actionUpload: actionUpload,
                actionDelete: actionDelete,
                label:label
        ]
        out << g.render(template:'/layouts/form/uploadMultipleFiles', model:model)

    }

    def uploadContactFiles = {attrs ->
        ContactRSDTO contact = attrs.contact
        String label = attrs.label
        KuorumUserSession userSession= springSecurityService.principal
        List<String> alreadyUploadedFiles = contactService.getFiles(userSession,contact)
        alreadyUploadedFiles = alreadyUploadedFiles.collect{it.split('\\?').first()}
        def model = [
                alreadyUploadedFiles:alreadyUploadedFiles,
                elementId: contact.id,
                disabled: false,
                confirmRemoveFile: true,
                actionUpload: g.createLink(mapping:'ajaxUploadContactFile', params: [contactId: contact.id, userAlias:userSession.id.toString()]),
                actionDelete: g.createLink(mapping:'ajaxDeleteContactFile', params: [contactId: contact.id, userAlias:userSession.id.toString()]),
                label:label
        ]
        out << g.render(template:'/layouts/form/uploadMultipleFiles', model:model)
    }
    def uploadQuestionOptionFiles = {attrs ->
        SurveyRSDTO survey = attrs.survey
        QuestionRSDTO question = attrs.question
        QuestionOptionRSDTO questionOption= attrs.questionOption
        String label = attrs.label
        KuorumUserSession userSession= springSecurityService.principal
        if (questionOption.questionOptionType != QuestionOptionTypeRDTO.ANSWER_FILES){
            throw new Exception("This option is not an ANSWER_FILES option")
        }
        List<String> alreadyUploadedFiles = []
        if (questionOption.answer){
            if (questionOption.answer instanceof QuestionAnswerFilesRDTO){
                alreadyUploadedFiles = ((QuestionAnswerFilesRDTO)questionOption.answer).getFiles();
            }else{
                throw new Exception("The answer is not the correct type -> QuestionAnswerFilesRDTO")
            }
        }
        if (!alreadyUploadedFiles){
//            alreadyUploadedFiles = contactService.getFiles(userSession,contact)
        }
        alreadyUploadedFiles = alreadyUploadedFiles.collect{it.split('\\?').first()}
        def model = [
                alreadyUploadedFiles:alreadyUploadedFiles,
                elementId: "questionOption_${questionOption.id}",
                disabled: question.answered,
                confirmRemoveFile: false,
                actionUpload: g.createLink(mapping:'ajaxUploadQuestionOptionFile', params: [userAlias:survey.user.alias, surveyId: survey.id, questionId: question.id, questionOptionId:questionOption.id]),
                actionDelete: g.createLink(mapping:'ajaxDeleteQuestionOptionFile', params: [userAlias:survey.user.alias, surveyId: survey.id, questionId: question.id, questionOptionId:questionOption.id]),
                label:label
        ]
        out << g.render(template:'/layouts/form/uploadMultipleFiles', model:model)
    }

//    private static final Integer NUM_CHARS_SHORTEN_URL = 19 //OWLY
    private static final Integer NUM_CHARS_TWITTER_URL = 22 // Twitter change all urls to t.co and its size will be 22 (before february of 2014 was 20)
    private static final Integer NUM_EXTRA_SPACE = 2 // Between text and hastag, and between hastag and shortUrl

    def input={attrs->
        def command = attrs.command
        def field = attrs.field

        def prefixFieldName=attrs.prefixFieldName?:""
        def disabled=attrs.disabled?"disabled":""
        def id = "${prefixFieldName}${attrs.id?:field}"
        def required = attrs.required?'required':''
        def cssClass = attrs.cssClass?:'form-control input-lg'
        def extraClass = attrs.extraClass?:''
        def readonly = attrs.readonly?'readonly':''
        def labelCssClass = attrs.labelCssClass?:''
        def showLabel = attrs.showLabel?Boolean.parseBoolean(attrs.showLabel):false
        def showCharCounter = attrs.showCharCounter?Boolean.parseBoolean(attrs.showCharCounter):true
        def clazz
        def type
        def numberStep
        try{
            if (attrs.type){
                type = attrs.type
            }else{
                clazz = command.metaClass.properties.find{it.name == field}.type
                if (Number.class.isAssignableFrom(clazz)){
                    type = "number"
                    if (attrs.numberStep){
                        numberStep = attrs.numberStep
                    }else if (Double.class.isAssignableFrom(clazz)){
                        numberStep = "0.1"
                    }
                }else{
                    type = "text"
                }
            }
        }catch (Exception e){
            // Handle exception for development log showing wich field is wrong
            log.error("Preparing input ${field} for command ${command.class}", e)
            throw e
        }
        def label = buildLabel(command, field, attrs.label)
        def placeHolder = attrs.placeHolder?:message(code: "${command.class.name}.${field}.placeHolder", default: '')
        String helpBlock = attrs.helpBlock?:message(code: "${command.class.name}.${field}.helpBlock", default: '')
        String extraInfo = message(code: "${command.class.name}.${field}.extraInfo", default: '')

        // command."${field}" == 0 is false when using elvis operator
        def value = (command."${field}"!=null?command."${field}":'').encodeAsHTML()
        def error = hasErrors(bean: command, field: field,'error')

        ConstrainedProperty constraints = command.constraints.find{it.key.toString() == field}.value
        MaxSizeConstraint maxSizeConstraint = constraints.appliedConstraints.find{it instanceof MaxSizeConstraint}
        def maxSize = maxSizeConstraint?.maxSize?:0
        def maxlength = maxSize?"maxlength='${maxSize}'":''
        if (maxSize > 0){
            cssClass += " counted"
        }

        if (showLabel){
            out << "<label for='${prefixFieldName}${field}' class='${labelCssClass}'>${label}</label>"
        }
        if (extraInfo){
            out << """
                <span class="info-disabled">
                    <span role="button" rel="popover" class="fas fa-info-circle" data-toggle="tooltip" data-placement="top" title="${extraInfo}"></span>
                </span>
            """
        }
        out <<"""
            <input type="${type}" ${numberStep?"step='${numberStep}'":''} name="${prefixFieldName}${field}" class="${cssClass} ${extraClass} ${error?'error':''}" id="${id}" ${required} ${maxlength} placeholder="${placeHolder}" value="${value}" ${disabled} ${readonly} />
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
        def printValue = attrs.printValue?Boolean.parseBoolean(attrs.printValue):false

        def id = attrs.id?:field
        def label = buildLabel(command,field,attrs.label)

        def placeHolder = attrs.placeHolder?:message(code: "${command.class.name}.${field}.placeHolder", default: '')
        def prefixFieldName=attrs.prefixFieldName?:""
        def showLabel = attrs.showLabel?Boolean.parseBoolean(attrs.showLabel):false
        def value = printValue?command."${field}"?:'':''
        def error = hasErrors(bean: command, field: field,'error')
        if (showLabel){
            out << "<label for='${prefixFieldName}${field}'>${label}</label>"
        }else{
            out << "<label class='sr-only' for='${prefixFieldName}${field}'>${label}</label>"
        }
        out <<"""
                <div class="input-append input-group">
                    <input type="password" required aria-required="true" id="${id}" name="${field}" class="form-control input-lg" placeholder="${g.message(code:"login.email.form.password.label")}" value="${value}" data-ays-ignore="true">
                    <span tabindex="100" class="add-on input-group-addon">
                        <label><input type="checkbox" name="show-${id}" class="show-hide-pass" id="show-${id}" data-ays-ignore="true">${message(code:'login.email.form.password.show')}</label>
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
        def cssParentContainer = attrs.cssParentContainer?:''
        def formId = attrs.formId
        def id = attrs.id?:field
        def customRemoveButton=attrs.customRemoveButton?Boolean.parseBoolean(attrs.customRemoveButton):false
        def customAddButton=attrs.customAddButton?Boolean.parseBoolean(attrs.customRemoveButton):false
        def appendLast=Boolean.parseBoolean(attrs.appendLast?:'false')

        String templateId = "${formId}-template"
        List listCommands = command."${field}"

        String removeButton = customRemoveButton?'':"""
            <fieldset class="row">
                <div class="col-md-12 text-right">
                    <button type="button" class="btn btn-transparent removeButton"><i class="fal fa-trash"></i></button>
                </div>
            </fieldset>
"""

        String addButton =  customAddButton?'':"""
        <fieldset class="row dynamic-fieldset-addbutton">
            <div class="form-group">
                <div class="col-md-12">
                    <button type="button" class="btn btn-grey inverted addButton"><i class="fal fa-plus"></i></button>
                </div>
            </div>
        </fieldset>
        """
        if (!appendLast){
            out << addButton
        }

        def obj= Class.forName(listClassName, true, Thread.currentThread().getContextClassLoader()).newInstance()
        out << "<div class='hide dynamic-fieldset ${cssParentContainer}' id='${templateId}'>"
        out << removeButton
        out << body([listCommand:obj, prefixField:""])
        out << "</div>"

        Integer idx = listCommands.size()
        def operator = {i -> i -1}
        if (appendLast){
            idx = -1
            operator = {i -> i +1}
        }
        listCommands.each{
            if (it){
                idx = operator(idx)
                out <<"<div class='dynamic-fieldset ${cssParentContainer}' data-dynamic-list-index='${idx}' >"
                out << body([listCommand:it, prefixField:"${field}[${idx}].", ])
                out << removeButton
                out <<"</div>"
            }
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
                        templateId : templateId,
                        fields:fields,
                        parentField:field,
                        appendLast:appendLast,
                        formId:formId
                ])

        if (appendLast){
            out << addButton
        }

    }

    def date={attrs ->
        def command = attrs.command
        def field = attrs.field

        def time = attrs.time?:false
        def prefixFieldName=attrs.prefixFieldName?:""
        def id = attrs.id?:field
        def required = attrs.required?'required':''
        def cssClass = attrs.cssClass?:'form-control input-lg'
        def placeHolder = attrs.placeHolder?:message(code: "${command.class.name}.${field}.placeHolder", default: '')
        def showLabel = attrs.showLabel?Boolean.parseBoolean(attrs.showLabel):false
        def label = buildLabel(command,field, attrs.label)

        def error = hasErrors(bean: command, field: field,'error')
        //TODO: ¿Internacionalizar el formato a mostrar de la fecha?
        def value = command."${field}"?command."${field}".format('dd/MM/yyyy'):''
        if (showLabel){
            out << "<label for='${prefixFieldName}${field}'>${label}</label>"
        }

        String typePicker = "date"
        String timeZoneId = ""
        String timeZoneLabel = ""
        String timeZoneChangeLink=""
        String datePickerType = "days"
        if (attrs.datePickerType == "birthDate"){
            datePickerType = "birthDate"
        }

        if (time){
            KuorumUserSession user =  springSecurityService.principal
            typePicker = "datetime"
            TimeZone userTimeZone = user.timeZone?:TimeZone.getTimeZone("UTC")
            timeZoneId=utcOffset(userTimeZone)
            timeZoneLabel=timeZoneToString(userTimeZone)
            timeZoneChangeLink = g.createLink(mapping:'profileEditAccountDetails')
            value = command."${field}"?command."${field}".format(WebConstants.WEB_FORMAT_DATE, user.timeZone):''
        }

        out <<"""
            <div class="input-group ${typePicker}" data-timeZone="${timeZoneId}" data-timeZoneLabel="${timeZoneLabel}" data-timeZoneChangeLink="${timeZoneChangeLink}" data-datePicker-type="${datePickerType}">
                <input type="text" name="${prefixFieldName}${field}" class="${cssClass} ${error?'error':''}" placeholder="${placeHolder}" id="${id}" aria-required="${required}" value="${value}">
                <span class="input-group-addon"><span class="fal fa-calendar fa-lg"></span></span>
            </div>
        """
        if(error){
            out << "<span for='${id}' class='error'>${g.fieldError(bean: command, field: field)}</span>"
        }
    }

    private String timeZoneToString(TimeZone timeZone, String customName = ""){
        if (customName){
            return customName
        }else{
            return "${timeZone.ID}"
        }
    }

    private String utcOffset(TimeZone timeZone){
        "UTC  ${timeZone.rawOffset>=0?'+':''}${timeZone.rawOffset / (1000*3600)}"
    }

    def tags = { attrs ->
        def command = attrs.command
        def field = attrs.field
        Boolean editable = attrs.editable !=null?
                attrs.editable instanceof Boolean?
                        attrs.editable : Boolean.parseBoolean(attrs.editable)
                :true;
        List<String> tags = command[field]?:[]
        String prefixFieldName =attrs.prefixFieldName?:""
//        List<String> tags =[]

        out << "<label for='${prefixFieldName}.${field}' class='sr-only'>${g.message(code: 'tools.contact.list.contact.saveTags')}</label> "
        if (editable){
            out << """
            <input id="${prefixFieldName}.${field}" name="${prefixFieldName}.${field}" class="tagsField" type="text" data-urlTags="${g.createLink(mapping:'politicianContactTagsAjax')}" value="${tags.join(",")}">
            <span class="hint">${g.message(code: 'admin.createDebate.hint.setNewTag')}</span>
            """
        }else if (tags){
            out << """
            <div class="bootstrap-tagsinput disabled">
                ${tags.collect{"<span class='tag label label-info'> $it </span>"}.join(" ")}            
            </div>
            <span class="hint">${g.message(code: 'admin.createDebate.hint.noTriggerTags')}</span>
            """
        }else{
            out << """
            <input id="${prefixFieldName}.${field}" name="${prefixFieldName}.${field}" type="text" class="form-control input-lg" value="${tags.join(",")}" disabled>            
            <span class="hint">${g.message(code: 'admin.createDebate.hint.noTriggerTags')}</span>
            """
        }
    }


    def regionInput={attrs->

        def command = attrs.command
        def field = attrs.field
        def id = attrs.id?:field
        def fieldId = field +".id"//Same as EditUserProfileCommand.bindingRegion
        String extraCss = attrs.extraCss?:""
        def showLabel = attrs.showLabel?Boolean.parseBoolean(attrs.showLabel):false
        RegionRSDTO regionValue = command."${field}"?:null
        def value = regionValue?.iso3166?:''
        def showedValue = ""
        if (value){
            Locale locale = LocaleContextHolder.getLocale()
            RegionRSDTO regionRSDTO = regionService.findRegionById(value, locale)
            showedValue = regionRSDTO.name
        }

        Boolean isRequired = isRequired(command,field)
        def label = "${attrs.label?:message(code: "${command.class.name}.${field}.label")}${isRequired?'*':''}"
        def placeHolder = attrs.placeHolder?:message(code: "${command.class.name}.${field}.placeHolder", default: '')
        String helpBlock = attrs.helpBlock?:message(code: "${command.class.name}.${field}.helpBlock", default: '')
        if (showLabel){
            out << "<label for='${field}'>${label}</label>"
        }
        def error = hasErrors(bean: command, field: field, 'error')
        def cssClass ="form-control input-lg"
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
        def label = buildLabel(command,field, attrs.label)
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

        def tagInputScript = """
            
            \$(function(){
                \$('#${id}').tagsInput(
                        {
                            'autocomplete_url':"${autocompleterUrl}",
                            'autocomplete':{
                                paramName:'term',
                                width:'auto',
                                delay:'300',
                                onSelect: function( event ) {
                                    if (!\$("#${id}").tagExist(event.value)){
                                        \$('#${id}').addTag(event.value)
                                        \$('#${id}_tag').focus()
                                    }else{
                                        \$('#${id}_tag').addClass('not_valid');
                                    }
                                }},
                            'width':'100%',
                            'height':'inherit',
                            'delimiter': [',',';'],
                            'defaultText':'',
                            onChange: function(elem, elem_tags)
                            {

                            }
                        })
            })
            """
        r.script( [:],tagInputScript)
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
                <span class="input-group-addon"><span class="${cssIcon} fa-fw"></span></span>
                <input class="form-control ${error?'error':''}" value="${value}" id="${field}" name="${field}" type="text" placeholder="${placeHolder}">
            </div>
        """
        if (error){
            out << "<span for='${field}' class='error'>${g.fieldError(bean: command, field: field)}</span>"
        }
    }

    def selectPhonePrefix = {attrs->
        def command = attrs.command
        def field = attrs.field
        def disabled = attrs.disabled
        def showLabel = attrs.showLabel?Boolean.parseBoolean(attrs.showLabel):true
        def id = attrs.id?:field
        def prefixFieldName=attrs.prefixFieldName?:""
        def cssClass = attrs.cssClass
        def labelCssClass=attrs.labelCssClass?:""
        def clazz = attrs.enumClass?:command.metaClass.properties.find{it.name == field}.type
        Boolean defaultEmpty = attrs.defaultEmpty?Boolean.parseBoolean(attrs.defaultEmpty):false
        Boolean isRequired = isRequired(command,field) || (attrs.required?Boolean.parseBoolean(attrs.required):false)
        def label = buildLabel(command, field, attrs.label)
        def placeHolder = attrs.placeHolder?:message(code: "${command.class.name}.${field}.placeHolder", default: '')
        def error = hasErrors(bean: command, field: field,'error')
        if (showLabel){
            out <<"""<label for="${id}" class="${labelCssClass}">${label}</label>"""
        }
        out << """
            <select name="${prefixFieldName}${field}" class="form-control input-lg ${error}" id="${id}" ${disabled?'disabled':''}>
            """
        if (!isRequired || defaultEmpty){
            out << "<option value=''> ${message(code:"${command.class.name}.${field}.empty", default: '')}</option>"
        }
        def values = ['+34','+44','+1', '+49','+57','+20','+211','+212','+213','+216','+218','+220','+221','+222','+223','+224','+225','+226','+227','+228','+229','+230','+231','+232','+233','+234','+235','+236','+237','+238','+239','+240','+241','+242','+243','+244','+245','+246','+247','+248','+249','+250','+251','+252','+253','+254','+255','+256','+257','+258','+260','+261','+262','+263','+264','+265','+266','+267','+268','+269','+27','+290','+291','+297','+298','+299','+30','+31','+32','+33','+34','+350','+351','+352','+353','+354','+355','+356','+357','+358','+359','+36','+370','+371','+372','+373','+374','+375','+376','+377','+378','+380','+381','+382','+385','+386','+387','+389','+39','+40','+41','+420','+421','+423','+43','+44','+45','+46','+47','+48','+500','+501','+502','+503','+504','+505','+506','+507','+508','+509','+51','+52','+53','+54','+55','+56','+57','+58','+590','+591','+592','+593','+594','+595','+596','+597','+598','+599','+60','+61','+62','+63','+64','+65','+66','+670','+6723','+673','+674','+675','+676','+677','+678','+679','+680','+681','+682','+683','+685','+686','+687','+688','+689','+690','+691','+692','+7','+81','+82','+84','+850','+852','+853','+855','+856','+86','+880','+886','+90','+91','+92','+93','+94','+95','+960','+961','+962','+963','+964','+965','+966','+967','+968','+970','+971','+972','+973','+974','+975','+976','+977','+98','+992','+993','+994','+995','+996','+998']
        
        values.each{
            String normalizedNumber = it.replace('+','00')
            String codeMessage = "${command.class.name}.${field}.$normalizedNumber";
            String codeNormalized = it.replace("+","00");
            Boolean selected = (codeNormalized==command."$field")
            if (command."$field" instanceof String){
                selected = (codeNormalized==command."$field")
            }
            out << "<option value='${codeNormalized}' ${selected?'selected':''}> ${message(code:codeMessage, default:it)}</option>"
        }
        out << "</select>"
        if(error){
            out << "<span for='${id}' class='error'>${g.fieldError(bean: command, field: id)}</span>"
        }
    }

    def selectEnum = {attrs->
        def command = attrs.command
        def field = attrs.field
        def showLabel = attrs.showLabel?Boolean.parseBoolean(attrs.showLabel):true
        def id = attrs.id?:field
        def prefixFieldName=attrs.prefixFieldName?:""
        def disabled = attrs.disabled
        def cssClass = attrs.cssClass
        def labelCssClass=attrs.labelCssClass?:""
        def clazz = attrs.enumClass?:command.metaClass.properties.find{it.name == field}.type
        Boolean defaultEmpty = attrs.defaultEmpty?Boolean.parseBoolean(attrs.defaultEmpty):false
        Boolean isRequired = isRequired(command,field) || (attrs.required?Boolean.parseBoolean(attrs.required):false)
        def label ="${attrs.label?:message(code: "${clazz.name}.label")}${isRequired?'*':''}"
        def error = hasErrors(bean: command, field: field,'error')
        if (showLabel){

            out <<"""<label for="${id}" class="${labelCssClass}">${label}</label>"""
        }
        out << """
            <select name="${prefixFieldName}${field}" class="form-control input-lg ${error}" id="${id}" ${disabled?'disabled':''}>
            """
        if (!isRequired || defaultEmpty){
            out << "<option value=''> ${message(code:"${clazz.name}.empty", default: '')}</option>"
        }
        def values = attrs.values?:clazz.values()
        values.each{
            String codeMessage = "${clazz.name}.$it"
            Boolean selected = (it==command."$field")
            if (command."$field" instanceof String){
                selected = (it.toString()==command."$field")
            }
            out << "<option value='${it}' ${selected?'selected':''}> ${message(code:codeMessage)}</option>"
        }
        out << "</select>"
        if(error){
            out << "<span for='${id}' class='error'>${g.fieldError(bean: command, field: id)}</span>"
        }
    }

    def selectQuestion = { attrs, body ->
        SurveyRSDTO survey = attrs.survey
        def questionSelect =  survey.questions.inject([:]) {map, q -> map <<[(q.id):q.text]}
        attrs.values = [:]
        attrs.values[0] = g.message(code:'kuorum.web.commands.payment.survey.QuestionOptionCommand.nextQuestionId.endSurvey')
        attrs.values << questionSelect
        out << selectMap(attrs, body)
    }
    def selectMap = {attrs->
        def command = attrs.command
        def field = attrs.field
        def showLabel = attrs.showLabel?Boolean.parseBoolean(attrs.showLabel):true
        def id = attrs.id?:field
        def prefixFieldName=attrs.prefixFieldName?:""
        def cssClass = attrs.cssClass
        def labelCssClass=attrs.labelCssClass?:""
        Boolean defaultEmpty = attrs.defaultEmpty?Boolean.parseBoolean(attrs.defaultEmpty):false
        Boolean isRequired = isRequired(command,field) || (attrs.required?Boolean.parseBoolean(attrs.required):false)
        def label = buildLabel(command, field, attrs.label)
        def error = hasErrors(bean: command, field: field,'error')
        if (showLabel){
            out <<"""<label for="${id}" class="${labelCssClass}">${label}</label>"""
        }
        out << """
            <select name="${prefixFieldName}${field}" class="form-control input-lg ${error}" id="${id}">
            """
        if (!isRequired || defaultEmpty){
            out << "<option value=''> ${message(code:"${command.class.name}.${field}.empty", default: '')}</option>"
        }
        def valuesMap = attrs.values
        valuesMap.each{ key, val ->
            Boolean selected = (key==command."$field")
            if (command."$field" instanceof String){
                selected = (key.toString()==command."$field")
            }
            out << "<option value='${key}' ${selected?'selected':''}> ${val}</option>"
        }
        out << "</select>"
        if(error){
            out << "<span for='${id}' class='error'>${g.fieldError(bean: command, field: id)}</span>"
        }
    }

    def selectCampaign = { attrs->
        def command = attrs.command
        def field = attrs.field
        def campaignType = attrs.campaignType

        def id = attrs.id?:field
        def prefixFieldName=attrs.prefixFieldName?:""
        def cssClass = attrs.cssClass
        def labelCssClass=attrs.labelCssClass?:""
        def clazz = command.metaClass.properties.find{it.name == field}.type
        Boolean defaultEmpty = attrs.defaultEmpty?Boolean.parseBoolean(attrs.defaultEmpty):false
        Boolean isRequired = isRequired(command,field) || (attrs.required?Boolean.parseBoolean(attrs.required):false)
        def label ="${attrs.label?:message(code: "${clazz.name}.label")}${isRequired?'*':''}"
        def error = hasErrors(bean: command, field: field,'error')
        out <<"""
            <label for="${id}" class="${labelCssClass}">${label}</label>
            <select name="${prefixFieldName}${field}" class="form-control input-lg ${error}" id="${id}">
            """
        if (!isRequired || defaultEmpty){
            out << "<option value=''> ${message(code:"${clazz.name}.empty")}</option>"
        }
        def selectValues;
        switch (campaignType){
            case "EVENT": selectValues = eventSelectKeyValue(); break;
            case "PARTICIPATORY_BUDGET": selectValues = participatoryBudgetSelectKeyValue();break;
            default: throw new KuorumException("Campaign selector ('${campaignType}') not suported")
        }
        selectValues.each{selectValue ->
            Boolean selected = (selectValue.id==command."$field")
            if (command."$field" instanceof String){
                selected = (selectValue.id.toString()==command."$field")
            }
            out << "<option value='${selectValue.id}' ${selected?'selected':''}> ${selectValue.value} </option>"
        }
        out << "</select>"
        if(error){
            out << "<span for='${id}' class='error'>${g.fieldError(bean: command, field: id)}</span>"
        }
    }

    private def eventSelectKeyValue(){
        KuorumUserSession user = springSecurityService.principal
        List<EventRSDTO> events = eventService.findEvents(user)
        return events.collect{[id:it.id, value:it.title]}
    }


    private def participatoryBudgetSelectKeyValue(){
        KuorumUserSession user = springSecurityService.principal
        List<ParticipatoryBudgetRSDTO> participatoryBudgets = participatoryBudgetService.findAll(user)
        return participatoryBudgets.findAll{it.published}.collect{[id:it.id, value:it.name]}
    }

    def selectTimeZone = {attrs->
        def command = attrs.command
        def field = attrs.field

        def id = attrs.id?:field
        def prefixFieldName=attrs.prefixFieldName?:""
        def cssClass = attrs.cssClass
        def labelCssClass=attrs.labelCssClass?:""
        Boolean isRequired = isRequired(command,field)
        def label ="${message(code: "${command.class.name}.${field}.label")}${isRequired?'*':''}"
        def error = hasErrors(bean: command, field: field,'error')
        out <<"""
            <label for="${id}" class="${labelCssClass}">${label}</label>
            <select name="${prefixFieldName}${field}" class="form-control input-lg ${error}" id="${id}">
            """
//        if (!isRequired){
//            out << "<option value=''> ${message(code:"${command.class.name}.${field}.empty")}</option>"
//        }
        List<String> timeZoneIds = TimeZone.availableIDs.findAll{it.contains("/")}.sort{it}
        out << "<option value=''> ${message(code: 'kuorum.web.commands.profile.AccountDetailsCommand.timeZoneId.empty')} </option>"
        out << buildTimeZoneOption(timeZoneIds.find{it.contains("Madrid")},false, "Europe/Madrid-Berlin-Rome")
        out << buildTimeZoneOption(timeZoneIds.find{it.contains("London")},false, "Europe/London-Lisbon")
        out << "<option value=''> ------------------ </option>"
        timeZoneIds.each{timeZoneId ->
            out << buildTimeZoneOption(timeZoneId, timeZoneId==command."$field")
        }
        out << "</select>"
        if(error){
            out << "<span for='${id}' class='error'>${g.fieldError(bean: command, field: id)}</span>"
        }
    }

    private String buildTimeZoneOption(String timeZoneId, Boolean selected = false, String customName = ""){
        TimeZone timeZone = TimeZone.getTimeZone(timeZoneId)
        return "<option value='${timeZone.ID}' ${selected?'selected':''}> ${timeZoneToString(timeZone,customName)}</option>"
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
        Integer startYear = 1900
        Integer endYear = Calendar.getInstance().get(Calendar.YEAR) - 16
        (startYear..endYear).each{
            out << "<option value='${it}' ${it==command."$field"?'selected':''}> ${it}</option>"
        }
        out << "</select>"
        if(error){
            out << "<span for='${id}' class='error'>${g.fieldError(bean: command, field: id)}</span>"
        }
    }

    def checkBox = {attrs ->
        def command = attrs.command
        def field = attrs.field

        def disabled = attrs.disabled
        def prefixFieldName=attrs.prefixFieldName?:""
        def checked = ''
        def value = attrs.value?:"true"
        def elementId = attrs.elementId?:field
        if (command."$field" instanceof Collection) {
            checked = ((Collection)command."$field").contains(value)?'checked':''
        } else {
            checked = command."$field"?'checked':''
        }
        def label = attrs.label?:message(code: "${command.class.name}.${field}.label")
        def extraClass = attrs.extraClass?:""
        def error = hasErrors(bean: command, field: field,'error')
        out <<"""
            <label class="checkbox-inline ${extraClass} ${disabled?'disabled':''}">
                <input id="${elementId}" class="${error}" type="checkbox" name='${prefixFieldName}${field}' ${checked} value='${value}' ${disabled?'disabled':''}/>
                <span class="check-box-icon"></span>
                <span class="label-checkbox">${label}</span>
            </label>
            """
        if (error) {
            out << "<span for='${field}' class='error'>${g.fieldError(bean: command, field: field)}</span>"
        }
    }

    def checkBoxDomainList = {attrs->
        def command = attrs.command
        def field = attrs.field
        def values = attrs.values
        def valueName=attrs.valueName?:'name'

        def cssClass = attrs.cssClass
        def clazz = values.first().class
        def label = message(code: "${command.class.name}.${field}.label", default: '')
        label = label?:message(code: "${clazz.name}.label")
        def error = hasErrors(bean: command, field: field,'error')
        out << "<label>${label}</label>"
        out << "<ul class=clearfix>"
        values.eachWithIndex{ it, idx ->
            def valName = it."${valueName}"
            def checked = command."$field"?.contains(it)?'checked':''
            //TODO: LA CLASE DEL LI ES DE OTRA COSA. HACER UNA CUSTOM
            out <<"""
                <li>
                <label class="checkbox-inline">
                    <input class="${error}" type="checkbox" name='${field}[${idx}].id' id="${field}" ${checked} value='${it.id}' >
                    ${message(code: "${clazz.name}.${valName}.label", default: valName)}
                </label>
                </li>
                """
        }
        out << "</ul>"
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
        def label = buildLabel(command, field, attrs.label)
        def showLabel = attrs.showLabel?Boolean.parseBoolean(attrs.showLabel):false
        def multiLine = attrs.showLabel?Boolean.parseBoolean(attrs.multiLine):false
        def labelRadioClass = multiLine?"radio":"radio-inline"
        def error = hasErrors(bean: command, field: field,'error')
	    def values = attrs.values?:clazz.values()
        out << "<div class='groupRadio'>"
        if (showLabel){
            out << "<label for='${prefixFieldName}${field}'>${label}</label>"
        }
        values.each{
            out << "<label class='${labelRadioClass}'>"
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
        def rows = attrs.rows?:3

        def id = attrs.id?:field
        def prefixFieldName=attrs.prefixFieldName?:""
        def value = command."$field"?:''
        def placeHolder = attrs.placeholder==null?message(code: "${command.class.name}.${field}.placeHolder"):attrs.placeholder
        def error = hasErrors(bean: command, field: field,'error')
        ConstrainedProperty constraints = command.constraints.find{it.key.toString() == field}.value
        MaxSizeConstraint maxSizeConstraint = constraints.appliedConstraints.find{it instanceof MaxSizeConstraint}
        def maxSize = maxSizeConstraint?.maxSize?:0
        def texteditor = attrs.texteditor?:''
        def label = buildLabel(command, field, attrs.label)
        def showLabel = attrs.showLabel?Boolean.parseBoolean(attrs.showLabel):false
        if (showLabel){
            out << "<label for='${prefixFieldName}${field}'>${label}</label>"
        }
        out << """
            <textarea name='${prefixFieldName}${field}' class="${maxSize?"counted":""} ${texteditor} ${error}" rows="${rows}" id="${prefixFieldName}${id}" placeholder="${placeHolder}">${value}</textarea>
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
            messages.append("required: \"${text}\",")
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
            messages.append("min: \"${text}\",")
        }else if (constraint instanceof MinSizeConstraint){
            restrictions.append("minlength: ${constraint.minSize},")
            String code = prefixMessage + ".${filedName}.min.size"
            String text = g.message(code:code,args:[constraint.minSize])
            messages.append("minlength: \"${text}\",")
//        }else if (constraint instanceof MatchesConstraint){
//            restrictions.append("regex: /${constraint.regex}/,")
//            String code = prefixMessage + ".${filedName}.matches"
//            String text = g.message(code:code)
//            messages.append("regex: '${text}',")
        }else if (constraint instanceof MaxSizeConstraint){
            restrictions.append("maxlength: ${constraint.maxSize},")
            String code = prefixMessage + ".${filedName}.max.size"
            String text = g.message(code:code,args:[constraint.maxSize])
            messages.append("maxlength: \"${text}\",")
        }else if (constraint instanceof EmailConstraint){
            restrictions.append("email: true,")
            String code = prefixMessage + ".${filedName}.wrongFormat"
            String text = g.message(code:code)
            messages.append("email: \"${text}\",")
        }else if (constraint instanceof UrlConstraint && constraint.url){
            restrictions.append("url: true,")
            String code = prefixMessage + ".${filedName}.wrongFormat"
            String text = g.message(code:code)
            messages.append("url: \"${text}\",")
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
            StringBuffer showErrors =  new StringBuffer('$(document).ready(function (){')

            errors.each{error ->
                String msg = tranlateErrorCode(error.codes)
                showErrors.append("display.error('', \"${msg}\");")
            }
            showErrors.append("});")
            r.script( [:],showErrors.toString())
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
            printGeneralErrors(obj.errors.allErrors - obj.errors.fieldErrors,obj)
//            printFieldErrors(obj.errors.fieldErrors, obj)
        }


        def scriptValidation = """
			\$(function (){
				\$("#${formId}").validate({
                errorClass:'error',
                errorPlacement: function(error, element) {
                    if(element.attr('id') == 'deadline'){
                        error.appendTo(element.parent("div").parent("div"));
                    }else {
                        error.insertAfter(element);
                    }
                },
                errorElement:'span',
        """
        def rulesAndMessages = generateRulesAndMessages(obj)
        scriptValidation = scriptValidation +
            """
                 ${rulesAndMessages.rules} , ${rulesAndMessages.message}
				});
			});
			"""
        r.script( [:],scriptValidation)
        if (dirtyControl){
            r.script( [:],"""
                \$(function(){
                    formHelper.dirtyFormControl.prepare(\$("#${formId}"))
                })
            """)
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
                rules.append("\"${fieldName}\":{")
                message.append("\"${fieldName}\":{")
                constraint.appliedConstraints.each{c ->
                    printValidation(rules, message,c,fieldName)
                    printValidationType(rules, message,c, fieldName)
                }
                rules.deleteCharAt(rules.length() - 1)
                message.deleteCharAt(message.length() - 1)
                rules.append("},")
                message.append("},")
            }
        }
        rules.deleteCharAt(rules.length() - 1)
        message.deleteCharAt(message.length() - 1)
        rules.append("}")
        message.append("}")
        return [rules:rules, message:message]
    }

    private String buildLabel(def command, String field, String customLabel){
        def label = customLabel?:message(code: "${command.class.name}.${field}.label")
        "${label}${isRequired(command,field)?'*':''}"
    }

    private boolean isRequired(def command, String field){
        ConstrainedProperty constraints = command.constraints.find{it.key.toString() == field}?.value?:null
        if (constraints){
            NullableConstraint nullableConstraint = constraints.appliedConstraints.find{it instanceof NullableConstraint}
            return !(nullableConstraint?.nullable?:false)
        }else{
            return false
        }
    }
}
