<!-- Launchs the header to clickable to open the modal -->
<script>
    $(function(){
        display.blockAdvise("${g.message(code:'modal.election.main.header')}", function(e){
            e.preventDefault();
            $("#causes-modal").modal("show");
        })
    });
</script>

<!-- Modal registro/login -->
<div class="modal fade in" id="causes-modal" tabindex="-1" role="dialog" aria-labelledby="registroLoginUsuario" aria-hidden="false">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="${g.message(code:'default.close')}"><span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only">${g.message(code:'default.close')}</span></button>
                <h4 class="sr-only" id="registroLoginUsuario">${g.message(code:'modal.election.main.header')}</h4>
            </div>
            <!-- BODY FOR COMPUTER-->
            <div class="modal-body text-center">
                <h1><g:message code="modal.election.header.title"/> </h1>
                <h3><g:message code="modal.election.header.subtitle"/></h3>
            <g:form mapping="campaignPoll" id="causes-modal-form" role="form" method="post" name="causes-modal-form">
                <input type="hidden" name="politicianId" value="${politician.id}"/>`
                    <div class="interestContainer all clearfix">
                    <g:set var="causes" value="${[
                            '#Recuperacion Justa'       :'fa fa-suitcase', //business.png
                            '#Educación & Innovación'   :'icon3-lightbulb', //<!--lightbulb.svg -->
                            '#Democracia'               :'icon3-ballotbox', //democracy.svg
                            '#Igualdad'                 :'icon-JUSTICE',
                            '#Reforma Constitucional'   :'icon-CONSTITUTIONAL',
                            '#Politica Exterior'        :'icon-FOREIGN_AFFAIRS'
                    ]}"/>
                    <g:each in="${causes}" var="commissionType">
                        <input type="checkbox" name="causes" id="${commissionType.key}" value="${commissionType.key}" class="check" />
                        <label for="${commissionType.key}">
                            <span class="${commissionType.value.icon} hidden-xs"></span>
                            ${commissionType.key}
                        </label>
                    </g:each>
                    </div><!-- /.all -->
                <!-- email subscription form -->
                    <h3><g:message code="modal.election.hook"/></h3>
                    <div class="form-group">
                        <input type="email" name="email" class="form-control input-lg center-block" id="email" required="" placeholder="name@example.com" value="" aria-required="true">
                    </div>
                    <div class="form-group">
                        <input type="submit" class="btn" value="Submit my choice!">
                    </div>
                    <div class="form-group">
                        You are accepting the <a href="https://kuorum.org/kuorum/politica-privacidad" target="_blank">service conditions</a>
                    </div>
                </g:form>
            </div>
            <!-- BODY FOR MOBILE DEVICES-->

        </div>
    </div>
</div>

<!-- fin modal -->