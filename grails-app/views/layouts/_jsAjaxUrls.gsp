
<script>
    var urls = {
        home:'<g:createLink mapping="home" absolute="true"/>',
        searchSuggest:'<g:createLink mapping="searcherSuggests" absolute="true"/>',
        search:'<g:createLink mapping="searcherSearch" absolute="true"/>',
        suggestRegion:'<g:createLink mapping="suggestRegions" absolute="true"/>',
        tour:{
            tour_dashboard:'<g:createLink mapping="tour_dashboard"/>'
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
        form:{
            warn:{
                leavingEditedForm: '<g:message code="form.warn.leavingEditedForm"/>'
            },
            image:{
                dragImage: '<g:message code="form.image.dragImage"/>',
                openBrowser: '<g:message code="form.image.openBrowser"/>',
            }
        },
        cookies:{
            message: '<g:message code="cookies.message" args="[createLink(mapping: 'footerPrivacyPolicy', absolute: true)]" encodeAs="raw"/>',
            accept:'<g:message code="cookies.accept"/>'
        },
        read:{
            more:'<g:message code="read.more"/>',
            less:'<g:message code="read.less"/>'
        },
        tour:{
            next:'<g:message code="tour.dashboard.next"/>',
            skip:'<g:message code="tour.dashboard.skip"/>',
            repeat:'<g:message code="tour.dashboard.repeat"/>',
            gotIt:'<g:message code="tour.dashboard.gotIt"/>',
            step1:{
                title:'<g:message code="tour.dashboard.step1.title"/>',
                content:'<g:message code="tour.dashboard.step1.content"/>'
            },
            step2:{
                title:'<g:message code="tour.dashboard.step2.title"/>',
                content:'<g:message code="tour.dashboard.step2.content"/>'
            },
            step3:{
                title:'<g:message code="tour.dashboard.step3.title"/>',
                content:'<g:message code="tour.dashboard.step3.content"/>'
            },
            step4:{
                title:'<g:message code="tour.dashboard.step4.title"/>',
                content:'<g:message code="tour.dashboard.step4.content"/>'
            }
        }
    }


</script>