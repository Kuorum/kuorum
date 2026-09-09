package kuorum

import kuorum.core.customDomain.CustomDomainResolver
import kuorum.register.KuorumUserSession
import org.kuorum.rest.model.contact.ContactRSDTO
import kuorum.web.constants.WebConstants

class ContestApplicationTagLib {

    static defaultEncodeAs = 'raw'
    static namespace = "contestApplication"

    // --- INYECCIÓN DE SERVICIOS (Necesarios para la subida de archivos) ---
    def springSecurityService
    def contactService
    // -----------------------------------------------------------------------

    /**
     * Resolves whether the pharmacy variant applies for this render. Callers editing an existing
     * ContestApplication should pass the persisted type explicitly via attrs.isPharma (it must win
     * over the domain's live flag - see ContestApplicationController#resolveContestApplicationType).
     * Callers with no specific entity in mind (e.g. the profile funnel) fall back to the domain flag.
     */
    private boolean resolveIsPharma(Map attrs) {
        if (attrs.containsKey('isPharma') && attrs.isPharma != null) {
            return Boolean.parseBoolean(attrs.isPharma.toString())
        }
        return CustomDomainResolver.domainRSDTO?.contestApplicationWithCollaborator ?: false
    }

    def domainInput = { attrs ->
        // Clonamos para evitar el bloqueo de GroovyPageAttributes
        def newAttrs = new HashMap(attrs)
        boolean isPharma = resolveIsPharma(attrs)

        if (isPharma) {
            String baseCode = "${newAttrs.command.getClass().name}.${newAttrs.field}"
            // Usamos message() nativo del taglib
            String pharmaLabel = message(code: "${baseCode}.label.pharmacy", default: '')
            String pharmaPlaceholder = message(code: "${baseCode}.placeHolder.pharmacy", default: '')

            if (pharmaLabel) newAttrs.label = pharmaLabel
            if (pharmaPlaceholder) newAttrs.placeholder = pharmaPlaceholder
        }

        out << formUtil.input(newAttrs)
    }

    def domainTextArea = { attrs ->
        def newAttrs = new HashMap(attrs)
        boolean isPharma = resolveIsPharma(attrs)

        if (isPharma) {
            String baseCode = "${newAttrs.command.getClass().name}.${newAttrs.field}"

            String pharmaLabel = message(code: "${baseCode}.label.pharmacy", default: '')
            String pharmaPlaceholder = message(code: "${baseCode}.placeHolder.pharmacy", default: '')

            if (pharmaLabel) newAttrs.label = pharmaLabel
            if (pharmaPlaceholder) newAttrs.placeholder = pharmaPlaceholder
        }

        out << formUtil.textArea(newAttrs)
    }

    def domainEditImage = { attrs ->
        def newAttrs = new HashMap(attrs)
        boolean isPharma = resolveIsPharma(attrs)

        if (isPharma) {
            String baseCode = "${newAttrs.command.getClass().name}.${newAttrs.field}"
            String pharmaLabel = message(code: "${baseCode}.label.pharmacy", default: '')
            if (pharmaLabel) newAttrs.label = pharmaLabel
        }

        def showLabel = newAttrs.showLabel ? Boolean.parseBoolean(newAttrs.showLabel.toString()) : false
        if (showLabel) {
            def command = newAttrs.command
            def field = newAttrs.field
            def label = newAttrs.label ?: message(code: "${command.getClass().name}.${field}.label", default: field)
            def labelCssClass = newAttrs.labelCssClass ?: ''
            out << "<label class='${labelCssClass}'>${label}</label>"
        }

        out << formUtil.editImage(newAttrs)
    }

    /**
     * Reimplements FormTagLib#socialInput's HTML (rather than delegating to it) because that tag
     * always resolves its own label/placeholder from the generic message code, with no way to pass
     * an override via attrs - same reasoning as domainUploadContactFiles above.
     */
    def domainSocialInput = { attrs ->
        def command = attrs.command
        def field = attrs.field
        def cssIcon = attrs.cssIcon
        boolean isPharma = resolveIsPharma(attrs)

        String baseCode = "${command.getClass().name}.${field}"
        String label = isPharma ? message(code: "${baseCode}.label.pharmacy", default: '') : ''
        if (!label) {
            label = message(code: "${baseCode}.label")
        }
        String placeHolder = isPharma ? message(code: "${baseCode}.placeHolder.pharmacy", default: '') : ''
        if (!placeHolder) {
            placeHolder = message(code: "${baseCode}.placeHolder", default: '')
        }
        def value = command."${field}" ?: ''

        def error = hasErrors(bean: command, field: field, 'error')
        out << """
            <label for="${field}">${label}</label>
            <div class="input-group">
                <span class="input-group-addon"><span class="${cssIcon} fa-fw"></span></span>
                <input class="form-control ${error ? 'error' : ''}" value="${value}" id="${field}" name="${field}" type="text" placeholder="${placeHolder}" >
            </div>
        """
        if (error) {
            out << "<span class='error'>${g.fieldError(bean: command, field: field)}</span>"
        }
    }

    def domainUploadContactFiles = { attrs ->
        // 1. Comprobamos si es farmacia para cambiar la etiqueta
        boolean isPharma = resolveIsPharma(attrs)
        String labelCode = isPharma ? 'customRegister.fillProfile.files.uploadContactFiles.label.pharmacy' : 'customRegister.fillProfile.files.uploadContactFiles.label'

        ContactRSDTO contact = attrs.contact
        String label = message(code: labelCode)

        // 2. Forzamos adminContact o leemos el de attrs
        Boolean adminContact = attrs.adminContact ? Boolean.parseBoolean(attrs.adminContact.toString()) : true

        KuorumUserSession userSession = springSecurityService.principal
        String contactOwnerId = adminContact ? WebConstants.FAKE_LANDING_ALIAS_USER : userSession.id.toString()

        List<String> alreadyUploadedFiles = contactService.getFiles(contactOwnerId, contact)
        alreadyUploadedFiles = alreadyUploadedFiles.collect { it.split('\\?').first() }

        def model = [
                alreadyUploadedFiles: alreadyUploadedFiles,
                elementId           : contact?.id,
                disabled            : false,
                confirmRemoveFile   : true,
                actionUpload        : g.createLink(mapping: 'ajaxUploadContactFile', params: [contactId: contact?.id, userAlias: contactOwnerId, adminContact: adminContact]),
                actionDelete        : g.createLink(mapping: 'ajaxDeleteContactFile', params: [contactId: contact?.id, userAlias: contactOwnerId, adminContact: adminContact]),
                label               : label,
                hideLinkIcon        : true
        ]

        // 3. Renderizamos directamente la plantilla esquivando a formUtil
        out << g.render(template: '/layouts/form/uploadMultipleFiles', model: model)
    }

    def domainSocialHeader = { attrs ->
        boolean isPharma = resolveIsPharma(attrs)
        if (isPharma) {
            String text = message(code: 'customRegister.fillProfile.social.pharmacy.label')
            out << "<label>${text}</label>"
        }
    }

    def domainMessage = { attrs ->
        boolean isPharma = resolveIsPharma(attrs)
        String baseCode = attrs.code

        if (isPharma && baseCode) {
            String pharmaMsg = message(code: baseCode + ".pharmacy", default: '')
            if (pharmaMsg) {
                out << pharmaMsg
                return
            }
        }
        out << message(attrs)
    }

    def domainSelectEnum = { attrs ->
        def newAttrs = new HashMap(attrs)
        boolean isPharma = resolveIsPharma(attrs)

        if (isPharma) {
            def command = newAttrs.command
            def field = newAttrs.field
            def clazz = newAttrs.enumClass ?: command.metaClass.properties.find{it.name == field}.type

            String enumPharmaLabel = message(code: "${clazz.name}.label.pharmacy", default: '')
            if (enumPharmaLabel) {
                newAttrs.label = enumPharmaLabel
            }
        }
        out << formUtil.selectEnum(newAttrs)
    }

    def handlePharmaFields = { attrs ->
        def command = attrs.command
        boolean isPharma = resolveIsPharma(attrs)

        if (isPharma) {
            // ---- LÓGICA FARMACIAS (Campo unificado) ----
            out << "<div class='hidden'>"
            out << "<input type='hidden' name='numBenefitedCaregivers' value='1' />"
            out << "<input type='hidden' name='numBenefitedPacients' value='1' />"
            out << "</div>"

            // Fila 1: Campos (offset-1 y 4 columnas cada uno)
            out << """<div class="col-sm-offset-1 col-sm-4 col-xs-12">"""
            out << domainInput([type: 'number', command: command, field: 'numBeneficiaries', showLabel: 'true', minValue: '1', isPharma: isPharma])
            out << "</div>"

            out << """<div class="col-sm-4 col-xs-12">"""
            out << domainInput([type: 'text', command: command, field: 'associationName', showLabel: 'true', isPharma: isPharma])
            out << "</div>"

            out << """<div class="clearfix"></div>"""

            out << """<div class="col-sm-offset-3 col-sm-4 col-xs-12" style="margin-top: 15px;">"""
            out << domainEditImage([
                    command: command,
                    field: 'associationImage',
                    fileGroup: kuorum.core.FileGroup.ASSOCIATION_IMAGE_PROFILE,
                    showLabel: 'true',
                    labelCssClass: 'associationImage-label',
                    isPharma: isPharma
            ])
            out << "</div>"

        } else {
            // ---- LÓGICA ASOCIACIONES (Clásica) ----
            // associationName/associationImage/numBeneficiaries only apply to PHARMACY applications:
            // they are nullable and not required here, so no placeholder values need to be submitted.
            out << """<div class="col-sm-offset-1 col-sm-4 col-xs-12">"""
            out << formUtil.input([type: 'number', command: command, field: 'numBenefitedPacients', showLabel: 'true', minValue: '1'])
            out << "</div>"

            out << """<div class="col-sm-4 col-xs-12">"""
            out << formUtil.input([type: 'number', command: command, field: 'numBenefitedCaregivers', showLabel: 'true', minValue: '1'])
            out << "</div>"
        }
    }
}