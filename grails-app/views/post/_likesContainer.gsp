<div class="likesContainer">
    <div id="m-callback-done">
        <span class="likesCounter">${post.numVotes}</span> <g:message code="post.votes"/>
        <span class="arrowLike"></span>
    </div>
    <div class="progress">
        <div class="progress-bar" role="progressbar" aria-valuetransitiongoal="${post.numVotes}" aria-valuemin="${postUtil.progressBarMinValue(post:post)}" aria-valuemax="${postUtil.progressBarMaxValue(post:post)}"></div> <!-- hay que pasarle los valores que correspondan (número de impulsos, el mínimo y el máximo) -->
    </div>
</div>