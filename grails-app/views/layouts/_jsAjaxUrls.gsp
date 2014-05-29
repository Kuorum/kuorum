
<script>
    var urls = {
        searchSuggest:'<g:createLink mapping="searcherSuggests" absolute="true"/>',
        search:'<g:createLink mapping="searcherSearch" absolute="true"/>',
        tour:{
            tour1:'<g:createLink mapping="tourStart"/>',
            tour2:'<g:createLink mapping="tour2"/>',
            tour3:'<g:createLink mapping="tour3"/>'
        }
    }
    var ajaxHeadNotificationsChecked='<g:createLink mapping="ajaxHeadNotificationsChecked" absolute="true"/>'
    var ajaxHeadMessagesChecked='<g:createLink mapping="ajaxHeadMessagesChecked" absolute="true"/>'
</script>


<script>
//    TODO: Use jawr. The actual version ignore codification
    var i18n = {
        readLater : '<g:message code="cluck.footer.readLater"/>',
        customRegister : {
            step4:{
                form:{
                    submit:{
                        description0:'<g:message code="customRegister.step4.form.submit.description0"/>',
                        description1:'<g:message code="customRegister.step4.form.submit.description1"/>',
                        description2:'<g:message code="customRegister.step4.form.submit.description2"/>',
                        descriptionOk:'<g:message code="customRegister.step4.form.submit.descriptionOk"/>'
                    }
                }
            }
        },
        post:{
            show:{
                boxes:{
                    like:{
                        vote:{
                            button:'<g:message code="post.show.boxes.like.vote.button"/>',
                            buttonVoted:'<g:message code="post.show.boxes.like.vote.buttonVoted"/>'
                        }
                    }
                }
            }
        },
        profile:{
            kuorumStore:{
                skillButton:{
                    active:'<g:message code="profile.kuorumStore.skillButton.active"/>'
                },
                roleButton:{
                    active:'<g:message code="profile.kuorumStore.roleButton.active"/>'
                }
            }
        }
    }


</script>