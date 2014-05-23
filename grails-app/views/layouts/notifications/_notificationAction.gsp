<%@ page import="kuorum.core.model.PostType" %>
<g:if test="${answerLink}">
    <span class="actions clearfix">
        <span class="pull-right">
            <g:if test="${modalVictory}">
                <a href="#" class="btn btn-xs openModalVictory" data-toggle="modal" data-target="#modalVictory" data-notification-id="${notification.id}">Dar victoria</a>
                <g:set var="action" value="${message(code:"modalVictory.action.${notification.post.commitmentType}")}"/>
                <g:set var="postType" value="${message(code:"${PostType.class.name}.${notification.post.postType}")}"/>
                <g:set var="lawLink" value="${createLink(mapping:"lawShow", params:notification.post.law.encodeAsLinkProperties())}"/>
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
                                description:'${message(code:'modalVictory.description', args:[postType, notification.post.defender.name,action,notification.post.numVotes])}',
                                lawLink:"${notification.post.title.trim()} <a href='${lawLink}'>${notification.post.law.hashtag}</a>",
                                victoryLink:"${g.createLink(mapping: 'postAddVictory', params:notification.post.encodeAsLinkProperties())}"
                            }
                        }
                    });
                </script>
            </g:if>
            <g:else>
                <a href="${answerLink}" class="btn btn-sm btn-custom-primary">Responder</a>
            </g:else>
        </span>
    </span>
</g:if>
