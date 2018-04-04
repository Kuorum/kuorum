
<li class="col-sm-12 col-md-6 search-article">
    <article role="article" class="box-ppal clearfix">
        <div class="link-wrapper" id="survey-${survey.id}" data-datepublished="${survey.datePublished.time}">
            <g:link mapping="surveyShow" params="${survey.encodeAsLinkProperties()}" class="hidden"></g:link>
            <g:if test="${survey.photoUrl || survey.videoUrl}">
            <div class="card-header-photo">
                <g:set var="surveyMultimedia" value="${false}"/>
                <g:if test="${survey.photoUrl}">
                    <g:set var="surveyMultimedia" value="${true}"/>
                    <img src="${survey.photoUrl}" alt="${survey.title}">
                </g:if>
                <g:elseif test="${survey.videoUrl}">
                    <g:set var="surveyMultimedia" value="${true}"/>
                    <image:showYoutube youtube="${survey.videoUrl}"/>
                </g:elseif>
                    </div>
                </g:if>
                <div class="card-body">
                    <h1>
                        <g:link mapping="surveyShow" class="link-wrapper-clickable" params="${survey.encodeAsLinkProperties()}">
                            ${survey.title}
                        </g:link>
                    </h1>
                <g:if test="${!surveyMultimedia}">
                    <div class="card-text"><modulesUtil:shortText text="${survey.body}"/></div>
                </g:if>
            </div>
            <div class="card-footer">
                <ul>
                    <g:if test="${showAuthor}">
                        <li class="owner">
                            <userUtil:showUser
                                    user="${survey.user}"
                                    showName="true"
                                    showActions="false"
                                    showDeleteRecommendation="false"
                                    htmlWrapper="div"
                            />
                        </li>
                    </g:if>

                    <li>
                        <g:link mapping="surveyShow" params="${survey.encodeAsLinkProperties()}" fragment="survey-progress" role="button" class="${survey.completed?'active':''}">
                            <span class="fa fa fa-pie-chart fa-lg"></span>
                            <span class="number">${survey.amountAnswers}</span>
                        </g:link>
                    </li>
                </ul>
            </div>
        </div>
    </article>
</li>