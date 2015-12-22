<section class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">
            <g:message code="politician.quickNotes.title"/>
        </h3>
    </div>
    <div class="panel-body text-center">
        <div class="table table-condensed limit-height" data-collapsedHeight="95">
            <div class="thead"><g:message code="politician.quickNotes.data.background.title"/></div>
            <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                    message:g.message(code:'politician.quickNotes.data.background.completeName'),
                    text:politician?.politicianExtraInfo?.completeName
            ]"/>
            <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                    message:g.message(code:'politician.quickNotes.data.background.age'),
                    text:(politician?.politicianExtraInfo?.birthDate?new Date()[Calendar.YEAR]-politician.politicianExtraInfo.birthDate[Calendar.YEAR]:'')
            ]"/>
            <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                    message:g.message(code:'politician.quickNotes.data.background.placeOfBirth'),
                    text:politician?.politicianExtraInfo?.birthPlace
            ]"/>
            <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                    message:g.message(code:'politician.quickNotes.data.background.dateOfBirth'),
                    text:(politician?.politicianExtraInfo?.birthDate?g.formatDate(format: 'yyyy', date: politician.politicianExtraInfo.birthDate):'')
            ]"/>
            <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                    message:g.message(code:'politician.quickNotes.data.background.family'),
                    text:politician?.politicianExtraInfo?.family
            ]"/>
        </div>
        <g:if test="${['university', 'school', 'studies', 'profession', 'declarationLink', 'sourceWebsite'].find{politician?.politicianExtraInfo?."${it}"}}">
            <div class="table table-condensed limit-height" data-collapsedHeight="105">
                <div class="thead"><g:message code="politician.quickNotes.data.education.title"/></div>
                <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                        message:g.message(code:'politician.quickNotes.data.education.university'),
                        text:politician?.politicianExtraInfo?.university
                ]"/>
                <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                        message:g.message(code:'politician.quickNotes.data.education.school'),
                        text:politician?.politicianExtraInfo?.school
                ]"/>
                <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                        message:g.message(code:'politician.quickNotes.data.education.studies'),
                        text:politician?.politicianExtraInfo?.studies?:''
                ]"/>
            </div>
        </g:if>
        <g:if test="${['webSite'].find{politician?.politicianExtraInfo?."${it}"} || ['sourceWebsite'].find{politician?.professionalDetails?."${it}"}}">
            <div class="table table-condensed limit-height" data-collapsedHeight="95">
                <div class="thead"><g:message code="politician.quickNotes.data.moreInfo.title"/></div>
                <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                        message:g.message(code:'politician.quickNotes.data.moreInfo.webSite'),
                        link:politician?.politicianExtraInfo?.webSite
                ]"/>
                <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                        message:g.message(code:'politician.quickNotes.readMore'),
                        link:politician?.professionalDetails?.sourceWebsite
                ]"/>
            </div>
        </g:if>
        <g:if test="${['twitter', 'facebook', 'googlePlus', 'linkedIn', 'blog', 'instagram', 'youtube'].find{politician?.socialLinks?."${it}"}}">
            <hr/>
            <ul class="panel-share-buttons">

                <g:if test="${politician.socialLinks?.twitter}">
                    <li><a href="https://twitter.com/${politician.socialLinks.twitter - '@'}" target="_blank" title="${message(code:"kuorum.web.commands.profile.SocialNetworkCommand.twitter.label")}"><i class="fa fa-twitter-square fa-2x"></i></a></li>
                </g:if>
                <g:if test="${politician.socialLinks?.facebook}">
                    <li><a href="${politician.socialLinks.facebook}" target="_blank" title="${message(code:"kuorum.web.commands.profile.SocialNetworkCommand.facebook.label")}"><i class="fa fa-facebook-square fa-2x"></i></a></li>
                </g:if>
                <g:if test="${politician.socialLinks?.googlePlus}">
                    <li><a href="${politician.socialLinks?.googlePlus}" target="_blank" title="${message(code:"kuorum.web.commands.profile.SocialNetworkCommand.googlePlus.label")}"><i class="fa fa-google-plus-square fa-2x"></i></a></li>
                </g:if>
                <g:if test="${politician.socialLinks?.linkedIn}">
                    <li><a href="${politician.socialLinks?.linkedIn}" target="_blank" title="${message(code:"kuorum.web.commands.profile.SocialNetworkCommand.linkedIn.label")}"><i class="fa fa-linkedin-square fa-2x"></i></a></li>
                </g:if>
                <g:if test="${politician.socialLinks?.blog}">
                    <li><a href="${politician.socialLinks?.blog}" target="_blank" title="${message(code:"kuorum.web.commands.profile.SocialNetworkCommand.blog.label")}"><i class="fa fa-rss-square fa-2x"></i></a></li>
                </g:if>
                <g:if test="${politician.socialLinks?.instagram}">
                    <li><a href="${politician.socialLinks?.instagram}" target="_blank" title="${message(code:"kuorum.web.commands.profile.SocialNetworkCommand.instagram.label")}"><i class="fa fa-instagram fa-2x"></i></a></li>
                </g:if>
                <g:if test="${politician.socialLinks?.youtube}">
                    <li><a href="${politician.socialLinks?.youtube}" target="_blank" title="${message(code:"kuorum.web.commands.profile.SocialNetworkCommand.youtube.label")}"><i class="fa fa-youtube-square fa-2x"></i></a></li>
                </g:if>
                %{--<li><a href="http://www.reddit.com/submit?url=&title=" target="_blank" title="Submit to Reddit"><i class="fa fa-youtube-square fa-2x"></i></a></li>--}%
            </ul>
        </g:if>
    </div>
</section>