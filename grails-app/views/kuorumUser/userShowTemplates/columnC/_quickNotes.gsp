<g:if test="${
//    ['completeName','birthDate', 'birthPlace','family'].find{politician?.politicianExtraInfo?."${it}"} ||
//    ['address','telephone', 'mobile','fax', 'assistants'].find{politician?.institutionalOffice?."${it}"} ||
//    ['address','telephone', 'mobile','fax', 'assistants'].find{politician?.politicalOffice?."${it}"} ||
    ['officialWebSite'].find{politician?.socialLinks?."${it}"} ||
    ['twitter', 'facebook', 'googlePlus', 'linkedIn', 'blog', 'instagram', 'youtube'].find{politician?.socialLinks?."${it}"}}">

    <section class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">
                <g:message code="politician.quickNotes.title"/>
            </h3>
        </div>
        <div class="panel-body text-center">
            %{--<g:if test="${['completeName','birthDate', 'birthPlace','family'].find{politician?.politicianExtraInfo?."${it}"}}">--}%
                %{--<div class="table table-condensed limit-height" data-collapsedHeight="95">--}%
                    %{--<div class="thead"><g:message code="politician.quickNotes.data.background.title"/></div>--}%
                    %{--<g:render template="/kuorumUser/userShowTemplates/columnC/rowPoliticianColumnC" model="[--}%
                            %{--message:g.message(code:'politician.quickNotes.data.background.completeName'),--}%
                            %{--data:politician?.politicianExtraInfo?.completeName?:''--}%
                    %{--]"/>--}%
                    %{--<g:render template="/kuorumUser/userShowTemplates/columnC/rowPoliticianColumnC" model="[--}%
                            %{--message:g.message(code:'politician.quickNotes.data.background.age'),--}%
                            %{--data:(politician?.politicianExtraInfo?.birthDate?new Date()[Calendar.YEAR]-politician.politicianExtraInfo.birthDate[Calendar.YEAR]:''),--}%
                    %{--]"/>--}%
                    %{--<g:render template="/kuorumUser/userShowTemplates/columnC/rowPoliticianColumnC" model="[--}%
                            %{--message:g.message(code:'politician.quickNotes.data.background.placeOfBirth'),--}%
                            %{--data:politician?.politicianExtraInfo?.birthPlace?:'',--}%
                            %{--itemprop: 'address'--}%
                    %{--]"/>--}%
                    %{--<g:render template="/kuorumUser/userShowTemplates/columnC/rowPoliticianColumnC" model="[--}%
                            %{--message:g.message(code:'politician.quickNotes.data.background.dateOfBirth'),--}%
                            %{--data:(politician?.politicianExtraInfo?.birthDate?g.formatDate(format: 'yyyy', date: politician.politicianExtraInfo.birthDate):''),--}%
                            %{--itemprop: 'birthDate',--}%
                            %{--content: politician?.politicianExtraInfo?.birthDate?g.formatDate(date: politician?.politicianExtraInfo?.birthDate, format: 'yyyy-MM-dd'):''--}%
                    %{--]"/>--}%
                    %{--<g:render template="/kuorumUser/userShowTemplates/columnC/rowPoliticianColumnC" model="[--}%
                            %{--message:g.message(code:'kuorum.web.commands.profile.EditUserProfileCommand.gender.label'),--}%
                            %{--data:(politician?.personalData?.gender?g.message(code:'kuorum.core.model.Gender.'+politician?.personalData?.gender):''),--}%
                            %{--itemprop:'gender'--}%
                    %{--]"/>--}%
                    %{--<g:render template="/kuorumUser/userShowTemplates/columnC/rowPoliticianColumnC" model="[--}%
                            %{--message:g.message(code:'politician.quickNotes.data.background.family'),--}%
                            %{--data:politician?.politicianExtraInfo?.family?:''--}%
                    %{--]"/>--}%
                %{--</div>--}%
            %{--</g:if>--}%
            %{--<g:if test="${['address','telephone', 'mobile','fax', 'assistants'].find{politician?.institutionalOffice?."${it}"}}">--}%
                %{--<div class="table table-condensed limit-height" data-collapsedHeight="110">--}%
                    %{--<div class="thead"><g:message code="politician.institutionalOffice.title"/> </div>--}%
                    %{--<g:render template="/kuorumUser/userShowTemplates/columnC/rowPoliticianColumnC" model="[--}%
                            %{--message:g.message(code:'kuorum.users.extendedPoliticianData.OfficeDetails.assistants.label'),--}%
                            %{--data:politician?.institutionalOffice?.assistants?:''--}%
                    %{--]"/>--}%
                    %{--<g:render template="/kuorumUser/userShowTemplates/columnC/rowPoliticianColumnC" model="[--}%
                            %{--message:g.message(code:'kuorum.users.extendedPoliticianData.OfficeDetails.address.label'),--}%
                            %{--data:politician?.institutionalOffice?.address?:'',--}%
                            %{--itemprop:'address'--}%
                    %{--]"/>--}%
                    %{--<g:render template="/kuorumUser/userShowTemplates/columnC/rowPoliticianColumnC" model="[--}%
                            %{--message:g.message(code:'kuorum.users.extendedPoliticianData.OfficeDetails.telephone.label'),--}%
                            %{--data:politician?.institutionalOffice?.telephone?:'',--}%
                            %{--itemprop:'telephone'--}%
                    %{--]"/>--}%
                    %{--<g:render template="/kuorumUser/userShowTemplates/columnC/rowPoliticianColumnC" model="[--}%
                            %{--message:g.message(code:'kuorum.users.extendedPoliticianData.OfficeDetails.mobile.label'),--}%
                            %{--data:politician?.institutionalOffice?.mobile?:'',--}%
                            %{--itemprop:'telephone'--}%
                    %{--]"/>--}%
                    %{--<g:render template="/kuorumUser/userShowTemplates/columnC/rowPoliticianColumnC" model="[--}%
                            %{--message:g.message(code:'kuorum.users.extendedPoliticianData.OfficeDetails.fax.label'),--}%
                            %{--data:politician?.institutionalOffice?.fax?:''--}%
                    %{--]"/>--}%
                %{--</div>--}%
            %{--</g:if>--}%
            %{--<g:if test="${['address','telephone', 'mobile','fax', 'assistants'].find{politician?.politicalOffice?."${it}"}}">--}%
                %{--<div class="table table-condensed limit-height" data-collapsedHeight="110">--}%
                    %{--<div class="thead"><g:message code="politician.politicalOffice.title"/> </div>--}%
                    %{--<g:render template="/kuorumUser/userShowTemplates/columnC/rowPoliticianColumnC" model="[--}%
                            %{--message:g.message(code:'kuorum.users.extendedPoliticianData.OfficeDetails.assistants.label'),--}%
                            %{--data:politician?.politicalOffice?.assistants?:''--}%
                    %{--]"/>--}%
                    %{--<g:render template="/kuorumUser/userShowTemplates/columnC/rowPoliticianColumnC" model="[--}%
                            %{--message:g.message(code:'kuorum.users.extendedPoliticianData.OfficeDetails.address.label'),--}%
                            %{--data:politician?.politicalOffice?.address?:'',--}%
                            %{--itemprop:'address'--}%
                    %{--]"/>--}%
                    %{--<g:render template="/kuorumUser/userShowTemplates/columnC/rowPoliticianColumnC" model="[--}%
                            %{--message:g.message(code:'kuorum.users.extendedPoliticianData.OfficeDetails.telephone.label'),--}%
                            %{--data:politician?.politicalOffice?.telephone?:'',--}%
                            %{--itemprop:'telephone'--}%
                    %{--]"/>--}%
                    %{--<g:render template="/kuorumUser/userShowTemplates/columnC/rowPoliticianColumnC" model="[--}%
                            %{--message:g.message(code:'kuorum.users.extendedPoliticianData.OfficeDetails.mobile.label'),--}%
                            %{--data:politician?.politicalOffice?.mobile?:'',--}%
                            %{--itemprop:'telephone'--}%
                    %{--]"/>--}%
                    %{--<g:render template="/kuorumUser/userShowTemplates/columnC/rowPoliticianColumnC" model="[--}%
                            %{--message:g.message(code:'kuorum.users.extendedPoliticianData.OfficeDetails.fax.label'),--}%
                            %{--data:politician?.politicalOffice?.fax?:''--}%
                    %{--]"/>--}%
                %{--</div>--}%
            %{--</g:if>--}%
            <g:if test="${['officialWebSite','institutionalWebSite'].find{politician?.socialLinks?."${it}"}}">
                <div class="table table-condensed limit-height" data-collapsedHeight="60">
                    %{--<div class="thead"><g:message code="politician.quickNotes.data.moreInfo.title"/></div>--}%
                    <g:render template="/kuorumUser/userShowTemplates/columnC/rowPoliticianColumnC" model="[
                            message:g.message(code:'politician.quickNotes.data.moreInfo.webSite'),
                            link:politician?.socialLinks?.officialWebSite?:''
                    ]"/>
                    %{--<g:render template="/kuorumUser/userShowTemplates/columnC/rowPoliticianColumnC" model="[--}%
                            %{--message:g.message(code:'politician.quickNotes.readMore'),--}%
                            %{--link:politician?.socialLinks?.institutionalWebSite?:''--}%
                    %{--]"/>--}%
                </div>
            </g:if>
            <g:if test="${['twitter', 'facebook', 'googlePlus', 'linkedIn', 'blog', 'instagram', 'youtube'].find{politician?.socialLinks?."${it}"}}">
                <div class="table table-condensed no-margins">
                    <ul class="panel-share-buttons">

                        <g:if test="${politician.socialLinks?.twitter}">
                            <li><a href="${politician.socialLinks.twitter}" target="_blank" title="${message(code:"kuorum.web.commands.profile.SocialNetworkCommand.twitter.label")}"><i class="fa fa-twitter-square fa-2x"></i></a></li>
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
                </div>
            </g:if>
        </div>
    </section>
</g:if>