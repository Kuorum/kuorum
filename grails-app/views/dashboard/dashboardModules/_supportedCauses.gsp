<g:if test="${supportedCauses}">
    <r:require modules="causes"/>
    <g:set var="ownerCauses" value=""/>
    <userUtil:ifUserIsTheLoggedOne user="${user}">
        <g:set var="ownerCauses" value="owner-causes"/>
    </userUtil:ifUserIsTheLoggedOne>
    <section class="panel panel-default causes ${ownerCauses}">
        <div class="panel-heading">
            <h3 class="panel-title">
                <g:message code="modules.causes.topCauses.title"/>
            </h3>
            <g:link mapping="profileCauses"> <span class="fa fa-edit"></span></g:link>
        </div>
        <div class="panel-body">
            <ul class="causes-tags hide10">
                <g:each in="${supportedCauses}" var="cause">
                    <cause:show cause="${cause}"/>
                </g:each>
            </ul>
        </div>
        <div class="panel-footer">
            <div class="see-more-content open-causes">
                <button class="angle-down">See more</button>
            </div>
        </div>
    </section>
</g:if>