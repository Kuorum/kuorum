<!-- No time zone -->
<userUtil:ifLoggedUserHasNotTimeZone>
    <div class="modal fade in" id="enterTimeZone" tabindex="-1" role="dialog" aria-labelledby="contactDeleteTitle" aria-hidden="true">
        <div class="modal-dialog ">
            <div class="modal-content">
                <g:set var="timeZoneCommand" value="${new kuorum.web.commands.profile.TimeZoneCommand()}"/>
                <formUtil:validateForm bean="${timeZoneCommand}" form="timeZoneForm" dirtyControl="false"/>
                <g:form method="POST" mapping="politicianMassMailingSaveTimeZone" name="timeZoneForm" role="form" class="submitOrangeButton" autocomplete="off">
                    <input type="hidden" name="timeZoneRedirect" value="">
                    <div class="modal-header"><h4><g:message code="modal.timeZone.header"/></h4></div>
                    <div class="modal-body">
                        <p>
                            <g:message code="modal.timeZone.explain"/>
                        </p>
                        <fieldset class="time-zone">
                            <div class="row form-group">
                                <formUtil:selectTimeZone command="${timeZoneCommand}" field="timeZoneId" required="true" labelCssClass="hide" cssClass="col-xs-12 col-sm-4"/>
                            </div>
                        </fieldset>
                    </div>
                    <div class="modal-footer">
                        <button class="btn" type="submit"><g:message code="default.save"/></button>
                    </div>
                </g:form>
            </div>
        </div>
    </div>
    <r:script>
        $(function() {
            $("#enterTimeZone").modal({
                backdrop: 'static',
                keyboard: false
            }).modal('show');
            $("input[name=timeZoneRedirect]").val(window.location)
        });
    </r:script>
</userUtil:ifLoggedUserHasNotTimeZone>