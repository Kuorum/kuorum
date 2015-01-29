<%@ page import="kuorum.core.model.PostType" %>
<g:if test="${modalVictory}">
    <a href="#" class="btn btn-xs openModalVictory" data-toggle="modal" data-target="#modalVictory" data-notification-id="${notification.id}">
        Dar victoria
    </a>
    <g:set var="action" value="${g.message(code:"modalVictory.action.${notification.post.commitmentType}")}"/>
    <g:set var="postType" value="${g.message(code:"${PostType.class.name}.${notification.post.postType}")}"/>
    <g:set var="projectLink" value="${g.createLink(mapping:"projectShow", params:notification.post.project.encodeAsLinkProperties())}"/>
    <script>
        //Data for modalVictory
        $(function(){
            modalVictory.data.notification_${notification.id} = {
                user: {
                    name:"${notification.kuorumUser.name}",
                    imageUrl:"${image.userImgSrc(user:notification.kuorumUser)}"
                },
                defender:{
                    name:"${notification.defender.name}",
                    imageUrl:"${image.userImgSrc(user:notification.defender)}"
                },
                post:{
                    title:"${notification.post.title}",
                    action:"${action}",
                    description:"${g.message(code:'modalVictory.description', args:[postType, notification.post.defender.name,action,notification.post.numVotes])}",
                    projectLink:"${notification.post.title.trim()} <a href='${projectLink}'>${notification.post.project.hashtag}</a>",
                    victoryLink:"${g.createLink(mapping: 'postAddVictory', params:notification.post.encodeAsLinkProperties())}"
                }
            }
        });
    </script>
</g:if>
<g:else>
    <a href="${answerLink}" class="btn btn-sm btn-custom-primary">Responder</a>
</g:else>