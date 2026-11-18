package kuorum

import kuorum.core.customDomain.CustomDomainResolver

class ContestApplicationTagLib {

    static defaultEncodeAs = 'raw'
    static namespace = "contestApplication"

    /**
     * Resolves whether the collaborator (pharmacy/association) variant applies for this render.
     * Callers editing an existing ContestApplication should pass the persisted type explicitly via
     * attrs.hasCollaborator (it must win over the domain's live flag - see
     * ContestApplicationController#resolveContestApplicationType). Callers with no specific entity
     * in mind (e.g. the profile funnel) fall back to the domain flag via CustomDomainResolver.
     */
    private boolean resolveHasCollaborator(Map attrs) {
        if (attrs.containsKey('hasCollaborator') && attrs.hasCollaborator != null) {
            return Boolean.parseBoolean(attrs.hasCollaborator.toString())
        }
        return CustomDomainResolver.isContestApplicationWithCollaboratorEnabled()
    }

    /**
     * Copy of attrs where the label (and, unless withPlaceholder is false, the placeholder) are replaced by the
     * "<command class>.<field>.label|placeHolder.withCollaborator" messages when the collaborator variant applies and
     * those messages exist. Copying avoids GroovyPageAttributes being read-only.
     */
    private Map collaboratorOverrides(Map attrs, boolean withPlaceholder = true) {
        Map newAttrs = new HashMap(attrs)
        if (resolveHasCollaborator(attrs)) {
            String baseCode = "${newAttrs.command.getClass().name}.${newAttrs.field}"
            String label = message(code: "${baseCode}.label.withCollaborator", default: '')
            if (label) newAttrs.label = label
            if (withPlaceholder) {
                String placeholder = message(code: "${baseCode}.placeHolder.withCollaborator", default: '')
                if (placeholder) newAttrs.placeholder = placeholder
            }
        }
        return newAttrs
    }

    /**
     * Renders the collaborating association's avatar + name next to the contest application
     * owner's own entry, when the domain is configured for the collaborator variant AND this
     * specific application actually has collaborator data - the domain flag alone doesn't mean
     * every application has an associationName (e.g. non-collaborator applications, or ones from
     * before this field existed), and rendering unconditionally on the flag alone was printing a
     * literal "null" (Groovy's GString rendering of a null value) plus a broken image icon.
     *
     * attrs.tag picks the wrapper element - "li" (default) for the card footer's <ul>, "div" for
     * standalone contexts like the contest application show page's header (next to userUtil:showUser).
     * attrs.showImage (default true) lets callers with no room/need for the avatar image, like the
     * ranking list, render the name only.
     */
    def collaboratorAvatar = { attrs ->
        def contestApplication = attrs.contestApplication
        String name = contestApplication?.associationName?.encodeAsHTML()
        String wrapperTag = attrs.tag ?: 'li'
        boolean showImage = attrs.containsKey('showImage') ? Boolean.parseBoolean(attrs.showImage.toString()) : true
        if (resolveHasCollaborator(attrs) && name) {
            // Only touch associationImage when actually needed: callers like the ranking list pass a
            // slimmer DTO (ContestApplicationRankingRSDTO) that has no associationImage property at all,
            // and Groovy's ?. only guards a null receiver, not a missing property on a non-null one.
            String imgSrc = showImage ? contestApplication?.associationImage?.encodeAsHTML() : null
            out << "<${wrapperTag} class=\"association\">"
            out << "<span class=\"association-inline\" title=\"${name}\">"
            if (showImage && imgSrc) {
                out << "<img src=\"${imgSrc}\" alt=\"${name}\" class=\"user-img\"/>"
            }
            out << "<span>${name}</span>"
            out << "</span>"
            out << "</${wrapperTag}>"
        }
    }

    /**
     * Writes whether the collaborator variant applies into a page-scope variable (attrs.var,
     * defaults to "hasCollaborator"). Called via the <contestApplication:hasCollaborator/> tag
     * syntax rather than as a property/method on a "contestApplication" variable - most callers
     * (card templates, ranking rows) already bind their own model variable named
     * "contestApplication", which would shadow the taglib namespace and silently resolve the call
     * against that model object instead.
     */
    def hasCollaborator = { attrs ->
        pageScope."${attrs.var ?: 'hasCollaborator'}" = resolveHasCollaborator(attrs)
    }

    /**
     * Same as hasCollaborator, but also requires attrs.associationName to be present - the same
     * combined condition collaboratorAvatar uses to decide whether to render, exposed here for
     * callers that only need the boolean (e.g. to toggle a CSS class).
     */
    def showsCollaboratorInfo = { attrs ->
        pageScope."${attrs.var ?: 'hasCollaborator'}" = resolveHasCollaborator(attrs) && attrs.associationName
    }

    def domainInput = { attrs ->
        out << formUtil.input(collaboratorOverrides(attrs))
    }

    def domainTextArea = { attrs ->
        out << formUtil.textArea(collaboratorOverrides(attrs))
    }

    def domainEditImage = { attrs ->
        Map newAttrs = collaboratorOverrides(attrs, false)

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
     * an override via attrs.
     */
    def domainSocialInput = { attrs ->
        def command = attrs.command
        def field = attrs.field
        def cssIcon = attrs.cssIcon
        boolean hasCollaborator = resolveHasCollaborator(attrs)

        String baseCode = "${command.getClass().name}.${field}"
        String label = hasCollaborator ? message(code: "${baseCode}.label.withCollaborator", default: '') : ''
        if (!label) {
            label = message(code: "${baseCode}.label")
        }
        String placeHolder = hasCollaborator ? message(code: "${baseCode}.placeHolder.withCollaborator", default: '') : ''
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

    def domainSocialHeader = { attrs ->
        boolean hasCollaborator = resolveHasCollaborator(attrs)
        if (hasCollaborator) {
            String text = message(code: 'customRegister.fillProfile.social.withCollaborator.label')
            out << "<label>${text}</label>"
        }
    }

    def domainMessage = { attrs ->
        boolean hasCollaborator = resolveHasCollaborator(attrs)
        String baseCode = attrs.code

        if (hasCollaborator && baseCode) {
            String collaboratorMsg = message(code: baseCode + ".withCollaborator", default: '')
            if (collaboratorMsg) {
                out << collaboratorMsg
                return
            }
        }
        out << message(attrs)
    }

    def domainSelectEnum = { attrs ->
        def newAttrs = new HashMap(attrs)
        boolean hasCollaborator = resolveHasCollaborator(attrs)

        if (hasCollaborator) {
            def command = newAttrs.command
            def field = newAttrs.field
            def clazz = newAttrs.enumClass ?: command.metaClass.properties.find{it.name == field}.type

            String enumCollaboratorLabel = message(code: "${clazz.name}.label.withCollaborator", default: '')
            if (enumCollaboratorLabel) {
                newAttrs.label = enumCollaboratorLabel
            }
        }
        out << formUtil.selectEnum(newAttrs)
    }

    /**
     * Beneficiaries fields of the Scope step. The collaborator variant asks for a single beneficiaries number (patients and
     * caregivers are sent as a fixed 1); the default variant asks for patients and caregivers.
     */
    def beneficiaryFields = { attrs ->
        def command = attrs.command

        if (resolveHasCollaborator(attrs)) {
            out << "<div class='hidden'>"
            out << "<input type='hidden' name='numBenefitedCaregivers' value='1' />"
            out << "<input type='hidden' name='numBenefitedPacients' value='1' />"
            out << "</div>"

            out << """<div class="col-sm-offset-1 col-sm-4 col-xs-12">"""
            out << domainInput([type: 'number', command: command, field: 'numBeneficiaries', showLabel: 'true', minValue: '1', hasCollaborator: true])
            out << "</div>"
        } else {
            out << """<div class="col-sm-offset-1 col-sm-4 col-xs-12">"""
            out << formUtil.input([type: 'number', command: command, field: 'numBenefitedPacients', showLabel: 'true', minValue: '1'])
            out << "</div>"

            out << """<div class="col-sm-4 col-xs-12">"""
            out << formUtil.input([type: 'number', command: command, field: 'numBenefitedCaregivers', showLabel: 'true', minValue: '1'])
            out << "</div>"
        }
    }
}
