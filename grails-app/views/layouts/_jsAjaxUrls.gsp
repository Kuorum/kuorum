
<script>
    var kuorumUrls = {
        home:'<g:createLink mapping="home" absolute="true"/>',
        searchSuggest:'<g:createLink mapping="searcherSuggests" absolute="true"/>',
        search:'<g:createLink mapping="searcherSearch" absolute="true"/>',
        suggestRegion:'<g:createLink mapping="suggestRegions" absolute="true"/>',
        tour:{
            tour_dashboard:'<g:createLink mapping="tour_dashboard"/>'
        },
        suggestAlias: '<g:createLink mapping="suggestAlias" absolute="true"/>',
    }
    var ajaxHeadNotificationsChecked='<g:createLink mapping="ajaxHeadNotificationsChecked" absolute="true"/>'
    var ajaxHeadMessagesChecked='<g:createLink mapping="ajaxHeadMessagesChecked" absolute="true"/>'

    var kuorumKeys = {
        _googleCaptchaKey: '${_googleCaptchaKey}'
    }
</script>


<script>
//    TODO: Use jawr. The actual version ignore codification
    var i18n = {
        lang : '${currentLang?.language?:'en'}',
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
                            button:"<g:message code="post.show.boxes.like.vote.button"/>",
                            buttonVoted:"<g:message code="post.show.boxes.like.vote.buttonVoted"/>"
                        }
                    }
                }
            }
        },
        profile:{
            kuorumStore:{
                skillButton:{
                    active:"<g:message code="profile.kuorumStore.skillButton.active"/>"
                },
                roleButton:{
                    active:"<g:message code="profile.kuorumStore.roleButton.active"/>"
                }
            }
        },
        form:{
            warn:{
                leavingEditedForm: "<g:message code="form.warn.leavingEditedForm"/>"
            },
            image:{
                dragImage: "<g:message code="form.image.dragImage"/>",
                openBrowser: "<g:message code="form.image.openBrowser"/>",
            },
            textEditor:{
                textAreaPlaceHolder:"<g:message code="form.textEditor.textAreaPlaceHolder"/>"
            }
        },
        cookies:{
            message: '<g:message code="cookies.message" args="[createLink(mapping: 'footerPrivacyPolicy', absolute: true)]" encodeAs="raw"/>',
            accept:"<g:message code="cookies.accept"/>"
        },
        read:{
            more:"<g:message code="read.more"/>",
            less:"<g:message code="read.less"/>"
        },
        tour:{
            next:"<g:message code="tour.dashboard.next"/>",
            skip:"<g:message code="tour.dashboard.skip"/>",
            repeat:"<g:message code="tour.dashboard.repeat"/>",
            gotIt:"<g:message code="tour.dashboard.gotIt"/>",
            step1:{
                title:"<g:message code="tour.dashboard.step1.title"/>",
                content:"<g:message code="tour.dashboard.step1.content"/>"
            },
            step2:{
                title:"<g:message code="tour.dashboard.step2.title"/>",
                content:"<g:message code="tour.dashboard.step2.content"/>"
            },
            step3:{
                title:"<g:message code="tour.dashboard.step3.title"/>",
                content:"<g:message code="tour.dashboard.step3.content"/>"
            },
            step4:{
                title:"<g:message code="tour.dashboard.step4.title"/>",
                content:"<g:message code="tour.dashboard.step4.content"/>"
            }
        },
        tools:{
            contact:{
                filter:{
                    anonymousName:"<g:message code="tools.contact.filter.anonymousName"/>",
                    newAnonymousName:"<g:message code="tools.contact.filter.newAnonymousName"/>",
                    conditions:{
                        open:"<g:message code="tools.contact.filter.conditions.open"/>",
                        close:"<g:message code="tools.contact.filter.conditions.close"/>"
                    }
                }
            },
        },
        politician:{
            valuation:{
                rate:{
                    success:"<g:message code="politician.valuation.rate.success"/>"
                }
            }
        },
        kuorum:{
            web:{
                commands:{
                    payment:{
                        massMailing:{
                            MassMailingCommand:{
                                scheduled:{
                                    min:{
                                        error:"<g:message code="kuorum.web.commands.payment.massMailing.MassMailingCommand.scheduled.min.error"/>"
                                    }
                                },
                                headerPictureId:{
                                    nullable:"<g:message code="kuorum.web.commands.payment.massMailing.MassMailingCommand.headerPictureId.nullable"/>"
                                },
                                text:{
                                    nullable:"<g:message code="kuorum.web.commands.payment.massMailing.MassMailingCommand.text.nullable"/>"
                                },
                                subject:{
                                    nullable:"<g:message code="kuorum.web.commands.payment.massMailing.MassMailingCommand.subject.nullable"/>"
                                }
                            },
                            DebateCommand:{
                                body:{
                                    nullable:"<g:message code="kuorum.web.commands.payment.massMailing.DebateCommand.body.nullable"/>"
                                },
                                title:{
                                    nullable:"<g:message code="kuorum.web.commands.payment.massMailing.DebateCommand.title.nullable"/>"
                                }
                            }
                        }
                    }
                }
            }
        }
    }


</script>