<%@ page import="kuorum.core.model.UserType; kuorum.users.KuorumUser" %>
<section role="complementary" class="landing ipdb clearfix" id="ipdb-description-tag">
    <div class="full-video">
        <div class="container-fluid">
            <div class="row">
                <div class="col-sm-12">
                    <h3><g:message code="landingEditors.ipdb.title"/> </h3>
                    <p><g:message code="landingEditors.ipdb.p1"/></p>
                    <p><g:message code="landingEditors.ipdb.p2"/></p>
                    <p><g:message code="landingEditors.ipdb.p3" args="[Math.floor(KuorumUser.countByUserType(UserType.POLITICIAN)/100)*100]"/></p>
                    <g:link mapping="register" class="btn btn-lg"><g:message code="landingEditors.videoAndRegister.form.submit"/> </g:link>
                </div>
            </div>
        </div>
    </div>
</section>