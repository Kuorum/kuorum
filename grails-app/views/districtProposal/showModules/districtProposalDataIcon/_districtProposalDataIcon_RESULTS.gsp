
<g:if test="${districtProposal.implemented}">
        <div class="comment-counter pull-right important" id="comment-counter-${districtProposal.id}">

                <span
                   class="fa-stack fa-lg kuorum-tooltip"
                   aria-hidden="true"
                   rel="tooltip"
                   data-toggle="tooltip"
                   data-placement="bottom"
                   title=""
                   data-original-title="${g.message(code:'districtProposal.callToAction.RESULTS.implementedFlag.tooltip', args: [districtProposal.name])}">
                        <span class="fas fa-circle dark fa-stack-2x"></span>
                        <span class="fal fa-flag fa-stack-1x fa-inverse"></span>
                </span>
        </div>
</g:if>