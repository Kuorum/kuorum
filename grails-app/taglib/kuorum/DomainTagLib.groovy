package kuorum

import kuorum.core.customDomain.CustomDomainResolver
import kuorum.files.AmazonFileService
import kuorum.files.LessCompilerService
import org.codehaus.groovy.grails.web.mapping.LinkGenerator

class DomainTagLib {
    static defaultEncodeAs = 'raw'
    //static encodeAsForTags = [tagName: 'raw']

    static namespace = "domain"

    def springSecurityService
    LinkGenerator grailsLinkGenerator
    LessCompilerService lessCompilerService
    AmazonFileService amazonFileService

    def customCss={attrs ->
        String domain = CustomDomainResolver.domain
        String urlCustomDomainCss = lessCompilerService.getUrlDomainCss(domain)
        Long version = CustomDomainResolver.domainRSDTO?.version?:0
        out <<"""
            <link rel="stylesheet" href="${urlCustomDomainCss}?v=${version}" type="text/css"/>
        """
    }

    def brandAndLogo={attrs ->
        String domain = CustomDomainResolver.domain
        String logoUrl = amazonFileService.getDomainLogoUrl(domain)
        out <<"""
        <img 
            src="${logoUrl}" 
            alt="${g.message(code:'head.logo.alt', args:[kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name])}" 
            title="${g.message(code:'head.logo.title', args:[kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name])}">
        <span class="hidden">${kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name}</span>
        """
    }
}
