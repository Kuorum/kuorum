<!-- ^fade-carousel !-->
<div id="carousel-landing-main" class="carousel slide fade-carousel ${msgPrefix}" data-ride="carousel" data-interval="5000" data-pause="null">
    <!-- Wrapper for slides -->
    <div class="content container">
        <hgroup>
            <h1><g:message code="${msgPrefix}.carousel.slogan"/> </h1>
            <h3 class="hidden-xs"><g:message code="${msgPrefix}.carousel.subslogan"/></h3>
        </hgroup>
    </br class="hidden-xs">
        <div id="request-demo-btn" class="form-inline dark col-lg-offset-4 col-lg-4">
            <a href="#" class="btn btn-lg btn-orange" data-effect="scroll"><g:message code="${msgPrefix}.requestDemo"/> </a>
        </div>
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