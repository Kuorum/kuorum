<h1><g:message code="post.progressBar.title" args="[postUtil.progressRemainingPoints(post:post)]" encodeAs="raw"/></h1>
<div class="progress">
    <div class="progress-bar" role="progressbar" aria-valuetransitiongoal="${post.numVotes}" aria-valuemin="${postUtil.progressBarMinValue(post:post)}" aria-valuemax="${postUtil.progressBarMaxValue(post:post)}"></div>
</div>
<h2><g:message code="post.progressBar.subTitle" args="[post.numVotes]"/> </h2>
