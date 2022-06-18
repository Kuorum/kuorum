<%@ page import="org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO" %>

<article role="article" class="box-ppal clearfix ${highlighted ? 'highlighted' : ''}">
    <div class="link-wrapper" id="districtProposal-${districtProposal.id}"
         data-datepublished="${districtProposal.datePublished.time}">
        <g:set var="campaignLink"
               value="${g.createLink(mapping: 'districtProposalShow', params: districtProposal.encodeAsLinkProperties())}"/>
        <a href="${campaignLink}" class="hidden"></a>
        <g:render template="/campaigns/cards/campaignMultimediaCard" model="[campaign: districtProposal]"/>
        <g:render template="/campaigns/cards/campaignBodyCard"
                  model="[campaign: districtProposal, campaignLink: campaignLink]"/>
        <g:render template="/campaigns/cards/districtProposalCardFooter" model="[districtProposal: districtProposal]"/>
    </div>
</article>
