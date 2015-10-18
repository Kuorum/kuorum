<section class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">
            <g:message code="politician.quickNotes.title"/>
        </h3>
    </div>
    <div class="panel-body text-center">
        <table class="table table-condensed">
            <thead>
            <tr>
                <th colspan="2"><g:message code="politician.quickNotes.data.background.title"/></th>
            </tr>
            </thead>
            <tbody>
            <g:if test="${politician?.politicianExtraInfo?.completeName}">
                <tr>
                    <th scope="row"><g:message code="politician.quickNotes.data.background.completeName"/></th>
                    <td>${politician?.politicianExtraInfo?.completeName?:"N/A"}</td>
                </tr>
            </g:if>
            <g:if test="${politician?.politicianExtraInfo?.birthDate}">
                <tr>
                    <th scope="row"><g:message code="politician.quickNotes.data.background.age"/></th>
                    <td>${new Date()[Calendar.YEAR]-politician.politicianExtraInfo.birthDate[Calendar.YEAR]}</td>
                </tr>
            </g:if>
            <g:if test="${politician?.politicianExtraInfo?.birthDate}">
                <tr>
                    <th scope="row"><g:message code="politician.quickNotes.data.background.dateOfBirth"/></th>
                    <td>
                        <g:formatDate date="${politician.politicianExtraInfo.birthDate}" format="yyyy"/>
                    </td>
                </tr>
            </g:if>
            <g:if test="${politician?.politicianExtraInfo?.birthPlace}">
                <tr>
                    <th scope="row"><g:message code="politician.quickNotes.data.background.placeOfBirth"/></th>
                    <td>${politician?.politicianExtraInfo?.birthPlace}</td>
                </tr>
            </g:if>
            </tbody>
            <tfoot>
            <tr>
                <td colspan="2"class="text-right"><a href="#"><g:message code="politician.quickNotes.readMore"/></a></td>
            </tr>
            </tfoot>
        </table>
        <hr/>
        <table class="table table-condensed">
            <thead>
            <tr>
                <th colspan="2"><g:message code="politician.quickNotes.data.education.title"/></th>
            </tr>
            </thead>
            <tbody>
            <g:if test="${politician?.politicianExtraInfo?.university}">
                <tr>
                    <th scope="row"><g:message code="politician.quickNotes.data.education.university"/></th>
                    <td>${politician?.politicianExtraInfo?.university}</td>
                </tr>
            </g:if>
            <g:if test="${politician?.politicianExtraInfo?.university}">
                <tr>
                    <th scope="row"><g:message code="politician.quickNotes.data.education.school"/></th>
                    <td>${politician?.politicianExtraInfo?.school}</td>
                </tr>
            </g:if>
            </tbody>
            <tfoot>
            <tr>
                <td colspan="2"class="text-right"><a href="#"><g:message code="politician.quickNotes.readMore"/></a></td>
            </tr>
            </tfoot>
        </table>
        <hr/>
        <table class="table table-condensed">
            <thead>
            <tr>
                <th colspan="2"><g:message code="politician.quickNotes.data.moreInfo.title"/></th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <th scope="row"><g:message code="politician.quickNotes.data.moreInfo.webSite"/></th>
                <td>
                    <g:if test="${politician?.politicianExtraInfo?.webSite}">
                        <a href="${politician?.politicianExtraInfo?.webSite}" target="_blank"> ${politician?.politicianExtraInfo?.webSite} </a>
                    </g:if>
                </td>
            </tr>
            </tbody>
        </table>
        <hr/>
        <ul class="panel-share-buttons">

            <g:if test="${politician.socialLinks?.twitter}">
                <li><a href="https://twitter.com/${politician.socialLinks.twitter - '@'}" target="_blank" title="${message(code:"kuorum.web.commands.profile.SocialNetworkCommand.twitter.label")}"><i class="fa fa-twitter-square fa-2x"></i></a></li>
            </g:if>
            <g:if test="${politician.socialLinks?.facebook}">
                <li><a href="${politician.socialLinks.facebook}" target="_blank" title="${message(code:"kuorum.web.commands.profile.SocialNetworkCommand.facebook.label")}"><i class="fa fa-facebook-square fa-2x"></i></a></li>
            </g:if>
            <g:if test="${politician.socialLinks?.googlePlus}">
                <li><a href="${politician.socialLinks?.googlePlus}" target="_blank" title="${message(code:"kuorum.web.commands.profile.SocialNetworkCommand.googlePlus.label")}"><i class="fa fa-google-plus fa-2x"></i></a></li>
            </g:if>
            <g:if test="${politician.socialLinks?.linkedIn}">
                <li><a href="${politician.socialLinks?.linkedIn}" target="_blank" title="${message(code:"kuorum.web.commands.profile.SocialNetworkCommand.linkedIn.label")}"><i class="fa fa-linkedin-square fa-2x"></i></a></li>
            </g:if>
            <g:if test="${politician.socialLinks?.blog}">
                <li><a href="${politician.socialLinks?.blog}" target="_blank" title="${message(code:"kuorum.web.commands.profile.SocialNetworkCommand.blog.label")}"><i class="fa fa-rss-square fa-2x"></i></a></li>
            </g:if>
            %{--<li><a href="http://www.reddit.com/submit?url=&title=" target="_blank" title="Submit to Reddit"><i class="fa fa-youtube-square fa-2x"></i></a></li>--}%
        </ul>
    </div>
</section>