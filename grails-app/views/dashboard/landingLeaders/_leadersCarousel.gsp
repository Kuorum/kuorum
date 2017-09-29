<!-- ^fade-carousel !-->
<div id="carousel-new-landing" class="carousel slide fade-carousel ${msgPrefix}" data-ride="carousel" data-interval="5000" data-pause="null">
    <!-- Indicators -->
    <div class="container indicators">
        <ol class="carousel-indicators">
            <li data-target="#carousel-new-landing" data-slide-to="0" class="active"></li>
            <li data-target="#carousel-new-landing" data-slide-to="1"></li>
            <li data-target="#carousel-new-landing" data-slide-to="2"></li>
            <li class="text"><g:message code="${msgPrefix}.carousel.slide1.text"/> </li>
        </ol>
    </div>
    <!-- Wrapper for slides -->
    <div class="content container">
        <hgroup>
            <h1><g:message code="${msgPrefix}.carousel.slogan"/> </h1>
            <h3 class="hidden-xs"><g:message code="${msgPrefix}.carousel.subslogan"/></h3>
        </hgroup>
        <a href="#how-it-works" class="btn btn-lg btn-blue-light hidden-xs" data-effect="scroll"><g:message code="${msgPrefix}.carousel.howItWorks"/> </a>
        </br class="hidden-xs">
        <sec:ifNotLoggedIn>
            <formUtil:validateForm bean="${command}" form="landing-register"/>
            <g:form mapping="register" autocomplete="off" method="post" name="landing-register" class="form-inline dark" role="form" novalidate="novalidate">
                <fieldset>
                    <div class="form-group col-lg-4">
                        <formUtil:input
                                command="${command}"
                                field="name"
                                labelCssClass="sr-only"
                                showLabel="true"
                                showCharCounter="false"
                                required="true"/>
                    </div>
                    <div class="form-group col-lg-4">
                        <formUtil:input
                                command="${command}"
                                field="email"
                                type="email"
                                showLabel="true"
                                labelCssClass="sr-only"
                                required="true"/>
                    </div>
                    <g:render template="/layouts/recaptchaForm"/>
                    <button type="submit"
                            data-sitekey="${siteKey}"
                            data-size="invisible"
                            data-callback='onSubmit'
                            class="btn btn-blue btn-lg col-lg-4 g-recaptcha"><g:message code="${msgPrefix}.carousel.login.submit"/>
                    </button>
                </fieldset>
            </g:form>
            <p class="conditions hidden-xs"><g:message code="register.conditions" args="[g.createLink(mapping: 'footerTermsUse')]"/></p>
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

<script>
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
        $('#carousel-new-landing').on('slid.bs.carousel', function (event) {
            var $carouselContainer = $(event.target);
            var $carouselText = $($carouselContainer.find('li.text')[0]);
            var messages = {
                'slide-0': '<g:message code="${msgPrefix}.carousel.slide1.text"/>',
                'slide-1': '<g:message code="${msgPrefix}.carousel.slide2.text"/>',
                'slide-2': '<g:message code="${msgPrefix}.carousel.slide3.text"/>'
            }
            $carouselText.slideToggle('slow', function () {
                var activeSlide = $carouselContainer.find('.carousel-indicators li.active').attr('data-slide-to');
                $(this).text(messages['slide-' + activeSlide]);
                $(this).slideToggle();
            });
        });

    });
</script>