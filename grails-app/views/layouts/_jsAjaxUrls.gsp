
<script>
    var kuorumUrls = {
        home:'<g:createLink mapping="home" absolute="true"/>',
        searchSuggest:'<g:createLink mapping="suggestSearcher" absolute="true"/>',
        search:'<g:createLink mapping="searcherSearch" absolute="true"/>',
        suggestRegion:'<g:createLink mapping="suggestRegions" absolute="true"/>',
        suggestAlias: '<g:createLink mapping="suggestAlias" absolute="true"/>',
        ajaxLoginRRSS: '<g:createLink mapping="ajaxRegisterRRSSOAuth" absolute="true"/>',
        profileDomainValidationChecker: '<g:createLink mapping="profileDomainValidationChecker" absolute="true"/>',
        politicianContactFilterData: '<g:createLink mapping="politicianContactFilterData" absolute="true"/>'
    };
    var ajaxHeadNotificationsChecked='<g:createLink mapping="ajaxHeadNotificationsChecked" absolute="true"/>';

    var kuorumKeys = {
        _googleCaptchaKey: '${_googleCaptchaKey}',
        _googleJsAPIKey: '${_googleJsAPIKey}',
    }
</script>


<script>
//    TODO: Use jawr. The actual version ignore codification
    var i18n = {
        uploader:{
            error:{
                sizeError: "<g:message code="uploader.error.sizeError"/>",
                emptyError: "<g:message code="uploader.error.emptyError"/>"
            }
        },
        seeMore: "<g:message code="read.more"/>",
        lang : '${currentLang?.language?:'en'}',
        register:{
            errors:'<g:message code="register.errors"/>',
            head:{
                login:'<g:message code="register.head.login"/>'
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
                content:"<g:message code="tour.dashboard.step3.content" args="[_domainName]"/>"
            },
            step4:{
                title:"<g:message code="tour.dashboard.step4.title"/>",
                content:"<g:message code="tour.dashboard.step4.content" args="[_domainName]"/>"
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
                        },
                        survey:{
                            QuestionOptionCommand:{
                                text:{
                                    nullable:"<g:message code="kuorum.web.commands.payment.survey.QuestionOptionCommand.text.nullable"/>",
                                    overflow:"<g:message code="kuorum.web.commands.payment.survey.QuestionOptionCommand.text.overflow"/>"
                                },
                                number:{
                                    negative:"<g:message code="kuorum.web.commands.payment.survey.QuestionOptionCommand.number.negative"/>",
                                    points:"<g:message code="kuorum.web.commands.payment.survey.QuestionOptionCommand.number.points"/>"
                                }
                            }
                        }
                    },
                    profile:{
                        DomainValidationCommand:{
                            closeWithoutValidation :"<g:message code="kuorum.web.commands.profile.DomainValidationCommand.closeWithoutValidation"/>"
                        }
                    }
                }
            },
            session:{
                validation:{
                    error : "<g:message code="kuorum.session.validation.error"/>",
                    groupError : "<g:message code="kuorum.session.validation.groupError"/>",
                }
            }
        },
        debate:{
            proposal:{
                action:{
                    campaignClosed:"<g:message code="debate.proposal.action.campaignClosed"/>"
                }
            }
        },
        survey:{questions:{header:{extrainfo:{multi:{points:{noLoggedError:"<g:message code="survey.questions.header.extrainfo.multi.points.noLoggedError"/>"}}}}}},
        org:{
            kuorum:{
                rest:{
                    model:{
                        communication:{
                            survey:{
                                QuestionTypeRSDTO:{
                                    MULTIPLE_OPTION_WEIGHTED:   {info: "${g.message(code:'org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.MULTIPLE_OPTION_WEIGHTED.info')}"},
                                    MULTIPLE_OPTION_POINTS:     {info: "${g.message(code:'org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.MULTIPLE_OPTION_POINTS.info')}"},
                                    MULTIPLE_OPTION:            {info: "${g.message(code:'org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.MULTIPLE_OPTION.info')}"},
                                    ONE_OPTION:                 {info: "${g.message(code:'org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.ONE_OPTION.info')}"},
                                    ONE_OPTION_WEIGHTED:        {info: "${g.message(code:'org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.ONE_OPTION_WEIGHTED.info')}"},
                                    TEXT_OPTION:                {info: "${g.message(code:'org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.TEXT_OPTION.info')}"},
                                    RATING_OPTION:              {info: "${g.message(code:'org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.RATING_OPTION.info')}"},
                                    CONTACT_GENDER:             {info: "${g.message(code:'org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.CONTACT_GENDER.info')}"},
                                    CONTACT_PHONE:              {info: "${g.message(code:'org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.CONTACT_PHONE.info')}"},
                                    CONTACT_WEIGHT:             {info: "${g.message(code:'org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.CONTACT_WEIGHT.info')}"},
                                    CONTACT_EXTERNAL_ID:        {info: "${g.message(code:'org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.CONTACT_EXTERNAL_ID.info')}"},
                                    CONTACT_BIRTHDATE:          {info: "${g.message(code:'org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.CONTACT_BIRTHDATE.info')}"},
                                    CONTACT_UPLOAD_FILES:       {info: "${g.message(code:'org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.CONTACT_UPLOAD_FILES.info')}"},
                                }
                            }
                        }
                    }
                }
            }
        }
    }


</script>