<section class="contact-politician panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title"><g:message code="politician.contact.title"/></h3>
    </div>
    <div class="panel-body text-center">
        <h3><g:message code="politician.contact.tellSomething" args="[politician.name]"/> </h3>
        <g:set var="politicianMail" value="${politician.enabled?'info@kuorum.org':politician.email}"/>
        <a href="mailto:${politicianMail}" class="btn btn-xl btn-blue btn-contact" role="button">
            <g:message code="politician.contact.button"/>
        </a>
    </div>
</section>