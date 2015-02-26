<g:if test="${(!errors) && (orderedNotice != null)}">
    <aside class="box-ppal condition clearfix">
    <g:if test="${orderedNotice != message(code:'dashboard.warningsUserProfile.FOLLOWPEOPLE')}">
        <button class="close" type="button"><span class="fa fa-times-circle-o fa"></span><span class="sr-only">eliminar de la lista</span></button>
    </g:if>

        <g:if test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOPROVINCE')}">
            <h1>${message(code:'dashboard.userProfile.incompleteDate.fillTheProvince.mainTitle')}</h1>
        </g:if>
        <g:elseif test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOPOLITICIANPHONE')}">
            <h1>${message(code:'dashboard.userProfile.incompleteDate.fillThePhone.mainTitle')}</h1>
        </g:elseif>
        <g:elseif test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOPOLITICIANINYOURCOUNTRY')}">
            <h1>${message(code:'dashboard.userProfile.incompleteDate.fillAPoliticianInYourCountry.mainTitle')}</h1>
        </g:elseif>
        <g:elseif test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOAGEANDGENDER')}">
            <h1>${message(code:'dashboard.userProfile.incompleteDate.fillYourAgeAndGender.mainTitle')}</h1>
        </g:elseif>
        <g:elseif test="${orderedNotice == message(code:'dashboard.warningsUserProfile.FOLLOWPEOPLE')}">
            <h1>${message(code:'dashboard.userProfile.incompleteDate.notFollowingPoliticians.mainTitle')}</h1>
        </g:elseif>

        <g:if test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOPOLITICIANINYOURCOUNTRY') || orderedNotice == message(code:'dashboard.warningsUserProfile.FOLLOWPEOPLE') }">
            <g:if test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOPOLITICIANINYOURCOUNTRY')}">
                <h2>${message(code:'dashboard.userProfile.incompleteDate.fillPAPoliticianInYourCountry.leftTitle')}</h2>
            </g:if>
            <g:if test="${orderedNotice == message(code:'dashboard.warningsUserProfile.FOLLOWPEOPLE')}">
                <h2>${message(code:'dashboard.userProfile.incompleteDate.notFollowingPoliticians.leftTitle')}</h2>
                <ul class="user-list-followers">
                    <g:each in="${recommendedUsers}" var="user">
                        <li itemtype="http://schema.org/Person" itemscope class="user">
                            <userUtil:showUser user="${user}" showName="true" showRole="true" showActions="true"/>
                        </li>
                    </g:each>
                </ul>
            </g:if>
        </g:if>
        <g:elseif test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOPROVINCE') || orderedNotice == message(code:'dashboard.warningsUserProfile.NOPOLITICIANPHONE') || orderedNotice == message(code:'dashboard.warningsUserProfile.NOAGEANDGENDER')}">
            <div class="row">
                <g:if test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOPROVINCE')}">
                    <div class="col-md-6 col-lg-7">
                        <h2>${message(code:'dashboard.userProfile.incompleteDate.fillTheProvince.leftTitle')}</h2>
                    </div>
                    <div class="col-md-6 col-lg-5">
                </g:if>
                <g:elseif test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOPOLITICIANPHONE')}">
                    <div class="col-md-6">
                        <h2>${message(code:'dashboard.userProfile.incompleteDate.fillThePhone.leftTitle')}</h2>
                    </div>
                    <div class="col-md-6">
                </g:elseif>
                <g:elseif test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOAGEANDGENDER')}">
                    <div class="col-md-6 col-lg-7">
                        <h2>${message(code:'dashboard.userProfile.incompleteDate.fillYourAgeAndGender.leftTitle')}</h2>
                    </div>
                    <div class="col-md-6 col-lg-5">
                </g:elseif>
                    <g:if test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOPROVINCE')}">
                        <p>${message(code:'dashboard.userProfile.incompleteDate.fillTheProvince.rightTitle')}</p>
                        <g:form method="POST" name="sign" role="form" class="no-postalcode" mapping="customRegisterCountryAndPostalCode">
                            <div class="form-group pull-left">
                                <formUtil:selectNation command="${personalDataCommand}" field="country" cssClass="sr-only"/>
                            </div>
                            <div class="form-group pull-left">
                                <label for="codigo-postal" class="sr-only"><g:message code="dashboard.userProfile.incompleteDate.postalCode.label"/></label>
                                <input type="number" name="postalCode" class="form-control input-lg" id="codpostal" required placeholder="${message(code:'dashboard.userProfile.incompleteDate.postalCode.label')}" aria-required="true" value="${personalDataCommand?.postalCode}">
                            </div>
                            <div class="form-group">
                                <input type="submit" value="${message(code:'dashboard.userProfile.incompleteDate.button.save')}" class="btn btn-grey btn-lg">
                            </div>
                        </g:form>
                    </g:if>
                    <g:elseif test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOPOLITICIANPHONE')}">
                        <p>${message(code:'dashboard.userProfile.incompleteDate.fillThePhone.rightTitle')}</p>
                        <g:form method="POST" name="sign" role="form" class="no-phone" mapping="customRegisterTelephone">
                            <div class="form-group">
                                <formUtil:selectNation command="${personalDataCommand}" field="country" cssClass="sr-only"/>
                            </div>
                            <div class="form-group pull-left">
                                %{--//TODO: Completar el TagLib para mostrar los prefijos--}%
                                <formUtil:telephoneWithPrefix />
                            </div>
                            <div class="form-group pull-left">
                                <label for="phone" class="sr-only"><g:message code="dashboard.userProfile.incompleteDate.phone.label"/></label>
                                <input type="number" name="telephone" class="form-control input-lg" id="telephone" required placeholder="${message(code:'dashboard.userProfile.incompleteDate.phone.label')}" aria-required="true" value="${personalDataCommand?.telephone?personalDataCommand.telephone.split().last():''}">
                            </div>
                            <div class="form-group">
                                <input type="submit" value="${message(code:'dashboard.userProfile.incompleteDate.button.save')}" class="btn btn-grey btn-lg">
                            </div>
                        </g:form>
                    </g:elseif>
                    <g:elseif test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOAGEANDGENDER')}">
                        <g:form method="POST" name="sign" role="form" class="no-age" mapping="customRegisterAgeAndGender">
                            <formUtil:radioEnum command="${personalDataCommand}" field="gender"/>
                            <div class="form-group pull-left">
                                <formUtil:selectBirdthYear command="${personalDataCommand}" field="year" cssClass="sr-only"/>
                            </div>
                            <div class="form-group pull-left">
                                <input type="submit" value="${message(code:'dashboard.userProfile.incompleteDate.button.save')}" class="btn btn-grey btn-lg">
                            </div>
                        </g:form>
                    </g:elseif>
                </div>
            </div>
        </g:elseif>

        <div class="photo">
            <g:if test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOPROVINCE')}"> <img alt="" src="images/img-nopostalcode.jpg"> </g:if>
            <g:elseif test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOPOLITICIANPHONE')}"> <img alt="" src="images/img-nophone.jpg"></g:elseif>
            <g:elseif test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOPOLITICIANINYOURCOUNTRY')}"> <img alt="" src="images/img-nopolitician.jpg"></g:elseif>
            <g:elseif test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOAGEANDGENDER')}"> <img alt="" src="images/img-noage.jpg"></g:elseif>
        </div>
    </aside>
</g:if>

<g:elseif test="${errors}">
    <script>
        $(function(){
            display.error('${orderedNotice}')
        })
    </script>
</g:elseif>

