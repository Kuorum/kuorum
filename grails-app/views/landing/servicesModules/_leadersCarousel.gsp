<!-- ^fade-carousel !-->
<div id="carousel-landing-main" class="carousel slide fade-carousel ${msgPrefix}" data-ride="carousel" data-interval="5000" data-pause="null">
    <!-- Indicators -->
    <div class="container indicators">
        <ol class="carousel-indicators">
            <li data-target="#carousel-landing-main" data-slide-to="0" class="active"></li>
            <li data-target="#carousel-landing-main" data-slide-to="1"></li>
            <li data-target="#carousel-landing-main" data-slide-to="2"></li>
            <li class="text"><span><g:message code="${msgPrefix}.carousel.slide1.text"/></span></li>
        </ol>
    </div>
    <!-- Wrapper for slides -->
    <div class="content container">
        <hgroup>
            <h1>${slogan}</h1>
            <h3 class="hidden-xs">${subtitle}</h3>
        </hgroup>
    <a href="#how-it-works" class="btn btn-lg btn-grey-transparent hidden-xs" data-effect="scroll"><g:message code="landingServices.howItWorks.title"/> </a>
    <sec:ifNotLoggedIn>
        <formUtil:validateForm bean="${command}" form="sign"/>
        <g:form mapping="register" autocomplete="off" method="post" name="sign" class="form-inline dark" role="form" novalidate="novalidate">
            <fieldset>
                <div class="form-group col-sm-3">
                    <formUtil:input
                            command="${command}"
                            field="name"
                            labelCssClass="sr-only"
                            showLabel="true"
                            showCharCounter="false"
                            required="true"/>
                </div>
                <div class="form-group col-sm-3">
                    <formUtil:input
                            command="${command}"
                            field="email"
                            type="email"
                            showLabel="true"
                            labelCssClass="sr-only"
                            required="true"/>
                </div>
                <div class="form-group col-sm-3">
                    <formUtil:input
                            command="${command}"
                            field="password"
                            type="password"
                            showLabel="true"
                            labelCssClass="sr-only"
                            required="true"/>
                </div>
                <div class="form-group col-sm-3 submit-button">
                    <button type="submit"
                            id="register-submit"
                            data-sitekey="${siteKey}"
                            data-size="invisible"
                            data-callback='registerCallback'
                            class="btn btn-lg g-recaptcha"><g:message code="landingPage.register.form.submit"/>
                    </button>
                </div>
            </fieldset>
            <fieldset>
                <div class="form-group col-xs-12 conditions">
                    <formUtil:checkBox
                            command="${command}"
                            field="conditions"
                            label="${g.message(code:'register.conditions', args:[g.createLink(mapping: 'footerPrivacyPolicy')], encodeAs: 'raw')}"/>

                </div>
                <r:require modules="recaptcha_register"/>
            </fieldset>
        </g:form>
    </sec:ifNotLoggedIn>
    </div>
    <div class="carousel-inner" role="listbox">
        <!-- leaders !-->
        <div class="item slides active">
            <div class="slide-1 ${msgPrefix}">
                <div class="overlay"></div>
            </div>
        </div> <!-- slide 1 leaders !-->

        <div class="item slides">
            <div class="slide-2 ${msgPrefix}">
                <div class="overlay"></div>
            </div>
        </div> <!-- slide 2 leaders !-->

        <div class="item slides">
            <div class="slide-3 ${msgPrefix}">
                <div class="overlay"></div>
            </div>
        </div> <!-- slide 3 leaders !-->
    </div>
</div>

<r:script>
    $(function(){
        //scroll effect
        $("[data-effect='scroll']").on('click', function(e) {
            e.preventDefault();
            var hash = this.hash;
            $('html, body').animate({
                scrollTop: $(hash).offset().top
            }, 1000, function(){
                window.location.hash = hash;
            });
        });
        //carousel
        $('#carousel-landing-main').on('slid.bs.carousel', function (event) {
            var $carouselContainer = $(event.target);
            var $carouselText = $($carouselContainer.find('li.text')[0]);
            var messages = {
                'slide-0': '<g:message code="${msgPrefix}.carousel.slide1.text"/>',
                'slide-1': '<g:message code="${msgPrefix}.carousel.slide2.text"/>',
                'slide-2': '<g:message code="${msgPrefix}.carousel.slide3.text"/>'
            };
            $carouselText.slideToggle('slow', function () {
                var activeSlide = $carouselContainer.find('.carousel-indicators li.active').attr('data-slide-to');
                $text = $("<span>"+messages['slide-' + activeSlide]+"</span>")
                $(this).html($text);
                $(this).slideToggle();
            });
        });

    });
</r:script>