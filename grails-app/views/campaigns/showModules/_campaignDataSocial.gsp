<g:if test="${_showSocialButtons && campaign.published}">
    <r:require modules="social"/>
    %{--TODO Add language param to campaignLink--}%
    <g:set var="currentLang"
           value="${org.springframework.web.servlet.support.RequestContextUtils.getLocale(request).getLanguage()}"/>
    <g:set var="campaignLink"><g:createLink
            mapping="campaignShowShortener"
            params="[campaignId: campaign.id]"
            absolute="true"/></g:set>

    <ul class="social pull-left">
        <li>
            <g:set var="twitterShareText">${campaign.title}</g:set>
            <g:set var="twitterLink">https://twitter.com/share?url=${campaignLink}&text=${twitterShareText}</g:set>
            <a href="${twitterLink}" target="_blank" title="${g.message(code: 'project.social.twitter')}" rel="noopener noreferrer" tabindex="-1">
                <span class="fa-stack fa-lg" aria-hidden="true">
                    <span class="fas fa-circle dark fa-stack-2x"></span>
                    <span class="fab fa-twitter fa-stack-1x fa-inverse"></span>
                </span>
            </a>
        </li>
        <li>
            <g:set var="facebookLink">https://www.facebook.com/sharer/sharer.php?u=${campaignLink}</g:set>
            <a href="${facebookLink}" target="_blank" title="${g.message(code: 'project.social.facebook')}" rel="noopener noreferrer" tabindex="-1">
                <span class="fa-stack fa-lg" aria-hidden="true">
                    <span class="fas fa-circle dark fa-stack-2x"></span>
                    <span class="fab fa-facebook-f fa-stack-1x fa-inverse"></span>
                </span>
            </a>
        </li>
        <li>
            <a href="http://www.linkedin.com/shareArticle?mini=true&url=${campaignLink}&title=${campaign.title}&summary=${campaign.body}&source=${kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.domain}"
               target="_blank" rel="noopener noreferrer" title="${g.message(code: 'project.social.linkedin')}"
               tabindex="-1">
                <span class="fa-stack fa-lg" aria-hidden="true">
                    <span class="fas fa-circle dark fa-stack-2x"></span>
                    <span class="fab fa-linkedin-in fa-stack-1x fa-inverse"></span>
                </span>
            </a>
        </li>
        <li>
            <a href="https://api.whatsapp.com/send?text=${campaignLink}"
               target="_blank" rel="noopener noreferrer" title="${g.message(code: 'project.social.whatsapp')}"
               tabindex="-1">
                <span class="fa-stack fa-lg" aria-hidden="true">
                    <span class="fas fa-circle dark fa-stack-2x"></span>
                    <span class="fab fa-whatsapp fa-stack-1x fa-inverse"></span>
                </span>
            </a>
        </li>
    </ul>
</g:if>