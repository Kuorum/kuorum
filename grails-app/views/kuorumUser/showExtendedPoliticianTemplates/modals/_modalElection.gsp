<!-- Launchs the header to clickable to open the modal -->
<script>
    var campaign;
    $(function(){
        campaign = new Campaign("${campaign.id}", "${campaign.name}", "${g.message(code:'modal.election.main.header')}", 5*1000)
        campaign.preparePageForCampaign()
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
                <h1 class="hidden-xs"><g:message code="modal.election.header.title"/> </h1>
                <h3><g:message code="modal.election.header.subtitle"/></h3>
                <formUtil:validateForm form="causes-modal-form" command="kuorum.web.commands.campaign.CampaignPollCommand"/>
                <g:form mapping="campaignPoll" id="causes-modal-form" role="form" method="post" name="causes-modal-form">
                    <input type="hidden" name="politician" value="${politician.id}"/>
                    <input type="hidden" name="campaign" value="${campaign.id}"/>
                    <div class="interestContainer all clearfix">
                        <g:set var="causes" value="${[
                                '#Recuperacion Justa'       :[icon:'fa fa-suitcase',      value: 'recovery'], 
                                '#Educación & Innovación'   :[icon:'icon3-lightbulb',     value: 'education'],
                                '#Democracia'               :[icon:'icon3-ballotbox',     value: 'democracy'],
                                '#Igualdad'                 :[icon:'icon-JUSTICE',        value: 'equalty'],
                                '#Reforma Constitucional'   :[icon:'icon-CONSTITUTIONAL', value: 'constitution'],
                                '#Politica Exterior'        :[icon:'icon-FOREIGN_AFFAIRS',value: 'foreign']
                        ]}"/>
                        <g:each in="${causes}" var="commissionType">
                            <input type="checkbox" name="causes" id="pretty-check-${commissionType.key}" value="${commissionType.value.value}" class="check" />
                            <label for="${commissionType.key}">
                                <span class="${commissionType.value.icon} hidden-xs"></span>
                                ${commissionType.key}
                            </label>
                        </g:each>
                    </div><!-- /.all -->
                <!-- email subscription form -->
                    <sec:ifNotLoggedIn>
                    <h3><g:message code="modal.election.hook"/></h3>
                    <div class="form-group">
                        <input type="email" name="email" class="form-control input-lg center-block" id="email" required="" placeholder="name@example.com" value="" aria-required="true">
                    </div>
                    </sec:ifNotLoggedIn>
                    <sec:ifLoggedIn>
                        <h3><g:message code="modal.election.hook.logged"/></h3>
                        <input type="email" name="email" class="hide" id="email" placeholder="name@example.com" value="${sec.username()}">
                    </sec:ifLoggedIn>
                    <div class="form-group">
                        <input type="submit" class="btn btn-maroon" value="Submit my choice!">
                    </div>
                    <sec:ifNotLoggedIn>
                        <div class="form-group">
                            <g:message code="register.conditions" args="[g.createLink(mapping: 'footerPrivacyPolicy')]" encodeAs="raw"/>
                        </div>
                    </sec:ifNotLoggedIn>
                </g:form>
                <script>
                    $(function(){
                        $( "#causes-modal-form" ).on( "submit", function( e ) {
                            e.preventDefault();
                            if ($(this).valid() && !$(this).hasClass("disabled")){
                                var url = $(this).attr("action")
                                var data = $( this ).serialize();
                                $.ajax({
                                    type: "POST",
                                    url: url,
                                    data: data,
                                    beforeSend: (function(){
                                        $( "#causes-modal-form input[type=submit]")
                                                .addClass("spinner")
                                                .addClass("disabled")
                                    })
                                })
                                .done(function( data ) {
                                    var msgs = JSON.parse(data);
                                    if (msgs.message != ""){
                                        display.success( msgs.message);
                                    }
                                    if (msgs.error != ""){
                                        display.error( msgs.error );
                                    }
                                    campaign.hideCampaign();
                                    campaign.notShowCampaignAgain();
                                    $( "#causes-modal-form input[type=submit]")
                                            .removeClass("spinner")
                                            .removeClass("disabled");
                                }).fail(function() {
                                    display.error( "Error" );
                                });
                            }
                        });
                    })
                </script>
            </div>
            <!-- BODY FOR MOBILE DEVICES-->

        </div>
    </div>
</div>

<!-- fin modal -->