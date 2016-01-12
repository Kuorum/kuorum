<%@ page import="kuorum.notifications.NoticeType" %>
<g:if test="${(!errors) && (orderedNotice != null)}">
    <aside class="box-ppal condition clearfix">
    <g:if test="${noticeType != NoticeType.FOLLOWPEOPLE}">
        <button class="close" type="button"><span class="fa fa-times-circle-o fa"></span><span class="sr-only">eliminar de la lista</span></button>
    </g:if>

        <g:if test="${noticeType == NoticeType.NOPROVINCE}">
            <h1>${message(code:'dashboard.userProfile.incompleteDate.fillTheProvince.mainTitle')}</h1>
        </g:if>
        <g:elseif test="${noticeType == NoticeType.NOPOLITICIANPHONE}">
            <h1>${message(code:'dashboard.userProfile.incompleteDate.fillThePhone.mainTitle')}</h1>
        </g:elseif>
        <g:elseif test="${noticeType == NoticeType.NOPOLITICIANINYOURCOUNTRY}">
            <h1>${message(code:'dashboard.userProfile.incompleteDate.fillAPoliticianInYourCountry.mainTitle')}</h1>
        </g:elseif>
        <g:elseif test="${noticeType == NoticeType.NOAGEANDGENDER}">
            <h1>${message(code:'dashboard.userProfile.incompleteDate.fillYourAgeAndGender.mainTitle')}</h1>
        </g:elseif>
        <g:elseif test="${noticeType == NoticeType.FOLLOWPEOPLE}">
            <h1>${message(code:'dashboard.userProfile.incompleteDate.notFollowingPoliticians.mainTitle')}</h1>
        </g:elseif>

        <g:if test="${noticeType == NoticeType.NOPOLITICIANINYOURCOUNTRY || noticeType == NoticeType.FOLLOWPEOPLE }">
            <g:if test="${noticeType == NoticeType.NOPOLITICIANINYOURCOUNTRY}">
                <h2>${message(code:'dashboard.userProfile.incompleteDate.fillPAPoliticianInYourCountry.leftTitle')}</h2>
            </g:if>
            <g:if test="${noticeType == NoticeType.FOLLOWPEOPLE}">
                <h2>${message(code:'dashboard.userProfile.incompleteDate.notFollowingPoliticians.leftTitle')}</h2>
                <modulesUtil:recommendedUsersList user="${user}" numUsers="14"/>
            </g:if>
        </g:if>
        <g:elseif test="${noticeType == NoticeType.NOPROVINCE || noticeType == NoticeType.NOPOLITICIANPHONE || noticeType == NoticeType.NOAGEANDGENDER}">
            <div class="row">
                <g:if test="${noticeType == NoticeType.NOPROVINCE}">
                    <div class="col-md-6 col-lg-7">
                        <h2>${message(code:'dashboard.userProfile.incompleteDate.fillTheProvince.leftTitle')}</h2>
                    </div>
                    <div class="col-md-6 col-lg-5">
                </g:if>
                <g:elseif test="${noticeType == NoticeType.NOPOLITICIANPHONE}">
                    <div class="col-md-6">
                        <h2>${message(code:'dashboard.userProfile.incompleteDate.fillThePhone.leftTitle')}</h2>
                    </div>
                    <div class="col-md-6">
                </g:elseif>
                <g:elseif test="${noticeType == NoticeType.NOAGEANDGENDER}">
                    <div class="col-md-6 col-lg-7">
                        <h2>${message(code:'dashboard.userProfile.incompleteDate.fillYourAgeAndGender.leftTitle')}</h2>
                    </div>
                    <div class="col-md-6 col-lg-5">
                </g:elseif>
                    <g:if test="${noticeType == NoticeType.NOPROVINCE}">
                        <p>${message(code:'dashboard.userProfile.incompleteDate.fillTheProvince.rightTitle')}</p>
                        <g:form method="POST" name="sign" role="form" class="no-postalcode" mapping="customRegisterCountryAndPostalCode">
                            <div class="form-group pull-left">
                                <formUtil:regionInput command="${personalDataCommand}" field="province"/>
                            </div>
                            <div class="form-group">
                                <input type="submit" value="${message(code:'dashboard.userProfile.incompleteDate.button.save')}" class="btn btn-grey btn-lg">
                            </div>
                        </g:form>
                    </g:if>
                    <g:elseif test="${noticeType == NoticeType.NOPOLITICIANPHONE}">
                        <p>${message(code:'dashboard.userProfile.incompleteDate.fillThePhone.rightTitle')}</p>
                        <g:form method="POST" name="sign" role="form" class="no-phone" mapping="customRegisterTelephone">
                            <div class="form-group">
                                <formUtil:selectNation command="${personalDataCommand}" field="country" cssClass="sr-only"/>
                            </div>
                            <div class="form-group pull-left">
                                <formUtil:telephoneWithPrefix command="${personalDataCommand}" field="phonePrefix"/>
                            </div>
                            <div class="form-group pull-left">
                                <label for="telephone" class="sr-only"><g:message code="dashboard.userProfile.incompleteDate.phone.label"/></label>
                                <input type="number" name="telephone" class="form-control input-lg" id="telephone" required placeholder="${message(code:'dashboard.userProfile.incompleteDate.phone.label')}" aria-required="true" value="${personalDataCommand?.telephone?personalDataCommand.telephone.split().last():''}">
                            </div>
                            <div class="form-group">
                                <input type="submit" value="${message(code:'dashboard.userProfile.incompleteDate.button.save')}" class="btn btn-grey btn-lg">
                            </div>
                        </g:form>
                    </g:elseif>
                    <g:elseif test="${noticeType == NoticeType.NOAGEANDGENDER}">
                        <g:form method="POST" name="sign" role="form" class="no-age" mapping="customRegisterAgeAndGender">
                            <formUtil:radioEnum command="${personalDataCommand}" field="gender"/>
                            <div class="form-group pull-left userData">
                                <formUtil:selectBirthYear command="${personalDataCommand}" field="year" cssClass="sr-only"/>
                            </div>
                            <div class="form-group pull-left">
                                <input type="submit" value="${message(code:'dashboard.userProfile.incompleteDate.button.save')}" class="btn btn-grey btn-lg">
                            </div>
                        </g:form>
                    </g:elseif>
                </div>
            </div>
        </g:elseif>

        %{--<div class="photo">--}%
            %{--<g:if test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOPROVINCE')}"> <img alt="" src="images/img-nopostalcode.jpg"> </g:if>--}%
            %{--<g:elseif test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOPOLITICIANPHONE')}"> <img alt="" src="images/img-nophone.jpg"></g:elseif>--}%
            %{--<g:elseif test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOPOLITICIANINYOURCOUNTRY')}"> <img alt="" src="images/img-nopolitician.jpg"></g:elseif>--}%
            %{--<g:elseif test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOAGEANDGENDER')}"> <img alt="" src="images/img-noage.jpg"></g:elseif>--}%
        %{--</div>--}%
    </aside>
</g:if>

<g:elseif test="${errors}">
    <script>
        $(function(){
            display.error('${orderedNotice}')
        })
    </script>
</g:elseif>

