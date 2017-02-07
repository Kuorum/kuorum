<div class="row">
    <h1><g:message code="${msgPrefix}.trustUs.title"/></h1>
    <g:each in="${users}" var="userTestimony">
        <div class="col-md-6 testimonial">
            <div class="header">
                <img src="${r.resource(dir:'images/landing', file:'fotos.png')}" alt="image border" class="img-border">
                <img class="img img-circle" src="${r.resource(dir:'images/landing', file:userTestimony.img)}" alt="${userTestimony.name}">
                <img class="img-brand" src="${r.resource(dir:'images/landing', file:userTestimony.logo)}" alt="${userTestimony.logoAlt}">
            </div>
            <div class="body">
                <p class="quote">"${userTestimony.quote}"</p>
                <p><span class="name">${userTestimony.name}</span>-<span> ${userTestimony.role}</span>
            </div>
        </div>
    </g:each>
</div>