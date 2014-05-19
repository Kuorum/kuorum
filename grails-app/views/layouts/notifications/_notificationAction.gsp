<g:if test="${answerLink}">
    <span class="actions clearfix">
        <span class="pull-right">
            <g:if test="${modalVictory}">
                <script>
                    $(function(){
                        modalData.notification_${notification.id} = {
                            user: {
                                name:"Cambiado",
                                imageUrl:"/kuorum/static/images/user-default.jpg"
                            },
                            politician:{
                                name:"Politico",
                                imageUrl:"/kuorum/static/images/user-default.jpg"
                            },
                            post:{
                                title:"Titulo del post"
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
