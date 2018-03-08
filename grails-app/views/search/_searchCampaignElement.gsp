<%@ page import="kuorum.users.KuorumUser; org.bson.types.ObjectId" %>

<article itemtype="http://schema.org/Person" itemscope role="article" class="box-ppal clearfix">
    <div class="link-wrapper">
        <g:link mapping="campaignShow" params="${solrCampaign.encodeAsLinkProperties()}" class="hidden"></g:link>
        <g:set var="campaignMultimedia" value="${false}"/>
        <g:if test="${solrCampaign.urlImage}">
            <div class="card-header-photo">
                <g:if test="${solrCampaign.urlImage}">
                    <g:set var="campaignMultimedia" value="${true}"/>
                    <img src="${solrCampaign.urlImage}" alt="${solrCampaign.name}">
                </g:if>
                %{--<g:elseif test="${campaign.videoUrl}">--}%
                    %{--<g:set var="campaignMultimedia" value="${true}"/>--}%
                    %{--<image:showYoutube youtube="${campaign.videoUrl}"/>--}%
                %{--</g:elseif>--}%
            </div>
        </g:if>
        <div class="card-body">
            <h1>
                <g:link mapping="campaignShow" params="${solrCampaign.encodeAsLinkProperties()}" class="link-wrapper-clickable">
                    <${solrCampaign.name}/>
                </g:link>
            </h1>
            <g:if test="${!campaignMultimedia}">
                <div class="card-text">
                    <p><kuorumDate:showShortedText text="${solrCampaign.text}" numChars="500"/>
                </div>
            </g:if>
        </div>
        <div class="card-footer">
            <ul>
                <li class="owner">
                    <userUtil:showUser
                            user="${solrCampaign}"
                            showName="true"
                            showActions="false"
                            showDeleteRecommendation="true"
                    />
                </li>
                <li>
                    <g:set var="campaignIcon" value="fa-lightbulb-o"/>
                    <g:set var="linkFragment" value="openProposal"/>
                    <g:if test="${solrCampaign.type == kuorum.core.model.solr.SolrType.EVENT}">
                        <g:set var="campaignIcon" value="fa-ticket"/>
                        <g:set var="linkFragment" value=""/>
                    </g:if>
                    <g:elseif test="${solrCampaign.type == kuorum.core.model.solr.SolrType.POST}">
                        <g:set var="campaignIcon" value="fa-heart"/>
                        <g:set var="linkFragment" value=""/>
                    </g:elseif>
                    <g:elseif test="${solrCampaign.type == kuorum.core.model.solr.SolrType.SURVEY}">
                        <g:set var="campaignIcon" value="fa-pie-chart"/>
                        <g:set var="linkFragment" value="survey-progress"/>
                    </g:elseif>
                    <g:link mapping="campaignShow" params="${solrCampaign.encodeAsLinkProperties()}" fragment="${linkFragment}" role="button">

                        <span class="fa ${campaignIcon} fa-lg"></span>
                        %{--<span class="label">${campaign.numProposals}</span>--}%
                    </g:link>
                </li>
            </ul>
        </div>
    </div>
</article>


%{--<article itemtype="http://schema.org/Article" itemscope role="article" class="kakareo post">--}%
    %{--<div class="link-wrapper">--}%
        %{--<g:link mapping="userShow" class="hidden" params="${solrUser.encodeAsLinkProperties()}"><g:message code="search.list.user.goTo" args="[solrUser.name]"/> </g:link>--}%
        %{--<div class="popover-box">--}%
            %{--<div class="user" itemscope itemtype="http://schema.org/Person">--}%
                %{--<a href="#" itemprop="url">--}%
                    %{--<img src="${image.solrUserImgSrc(user:solrUser)}" alt="${solrUser.name}" class="user-img" itemprop="image"><span itemprop="name"><searchUtil:highlightedField solrElement="${solrUser}" field="name"/></span>--}%
                %{--</a>--}%
                %{--<span class="user-type">--}%
                    %{--<small>${userUtil.roleName(user:user)}</small>--}%
                %{--</span>--}%
            %{--</div><!-- /user -->--}%
            %{--<userUtil:followButton user="${user}" cssSize="btn-xs"/>--}%
            %{--<userUtil:isFollower user="${user}"/>--}%
            %{--<g:render template="/kuorumUser/userActivity" model="[user:user]"/>--}%
        %{--</div>--}%
    %{--</div>--}%
%{--</article>--}%