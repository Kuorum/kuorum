package kuorum

import kuorum.core.customDomain.CustomDomainResolver
import kuorum.files.AmazonFileService
import kuorum.files.LessCompilerService
import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import org.kuorum.rest.model.domain.DomainRSDTO

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
        String emptyLogo= g.resource(dir: "images", file: "logo@2x.png", absolute: true)
        out <<"""
        <img 
            id="domain-logo"
            src="${logoUrl}" 
            onerror="this.onerror=null;this.src='$emptyLogo';"
            alt="${g.message(code:'head.logo.alt', args:[kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name])}" 
            title="${g.message(code:'head.logo.title', args:[kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name])}">
        <span class="hidden">${kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name}</span>
        """
    }

    def footerLis={attrs ->
        DomainRSDTO domain = CustomDomainResolver.domainRSDTO
        if (domain.footerLinks){
            domain.footerLinks.each{
                out << "<li><a href='${it.value}' target='_blank'>${it.key}</a></li>"
            }
        }
    }

    def favicon={
        Long version = CustomDomainResolver.domainRSDTO?.version?:0
        out << """
        <link rel="apple-touch-icon" sizes="180x180" href="${_domainResourcesPath}/favicon/apple-touch-icon.png?v=${version}">
        <link rel="icon" type="image/png" sizes="32x32" href="${_domainResourcesPath}/favicon/favicon-32x32.png?v=${version}">
        <link rel="icon" type="image/png" sizes="16x16" href="${_domainResourcesPath}/favicon/favicon-16x16.png?v=${version}">
        <link rel="manifest" href="${_domainResourcesPath}/favicon/site.webmanifest?v=${version}">
        <link rel="mask-icon" href="${_domainResourcesPath}/favicon/safari-pinned-tab.svg?v=${version}" color="#ff9431">
        <link rel="shortcut icon" href="${_domainResourcesPath}/favicon/favicon.ico?v=${version}">
        <meta name="msapplication-TileColor" content="#20a2ea">
        <meta name="msapplication-config" content="${_domainResourcesPath}/favicon/browserconfig.xml?v=${version}">
        <meta name="theme-color" content="#ffffff">
        """
    }
}
