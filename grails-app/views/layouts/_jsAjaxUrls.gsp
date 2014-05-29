
<script>
    var urls = {
        home:'<g:createLink mapping="home" absolute="true"/>',
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
        },
        tour:{
            tour1:{
                step1_title:    '<g:message code="tour.tour1.step1_title"/>'    ,
                step1_content:  '<g:message code="tour.tour1.step1_content"/>'  ,
                step2_title:    '<g:message code="tour.tour1.step2_title"/>'    ,
                step2_content:  '<g:message code="tour.tour1.step2_content"/>'  ,
                step3_title:    '<g:message code="tour.tour1.step3_title"/>'    ,
                step3_content:  '<g:message code="tour.tour1.step3_content"/>'  ,
                step4_title:    '<g:message code="tour.tour1.step4_title"/>'    ,
                step4_content:  '<g:message code="tour.tour1.step4_content"/>'  ,
                step5_title:    '<g:message code="tour.tour1.step5_title"/>'    ,
                step5_content:  '<g:message code="tour.tour1.step5_content"/>'  ,
                step6_title:    '<g:message code="tour.tour1.step6_title"/>'    ,
                step6_content:  '<g:message code="tour.tour1.step6_content"/>'  ,
                step7_title:    '<g:message code="tour.tour1.step7_title"/>'    ,
                step7_content:  '<g:message code="tour.tour1.step7_content"/>'
            },
            tour2:{
                step1_title:    '<g:message code="tour.tour2.step1_title"/>'    ,
                step1_content:  '<g:message code="tour.tour2.step1_content"/>'  ,
                step2_title:    '<g:message code="tour.tour2.step2_title"/>'    ,
                step2_content:  '<g:message code="tour.tour2.step2_content"/>'
            }
        }
    }


</script>