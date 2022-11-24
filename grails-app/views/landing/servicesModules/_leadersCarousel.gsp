<!-- ^fade-carousel !-->
<div id="carousel-landing-main" class="carousel slide fade-carousel ${msgPrefix}" data-ride="carousel"
     data-interval="7000" data-pause="null">
    <!-- Indicators -->
    <div class="container indicators">
        <ol class="carousel-indicators">
            <li data-target="#carousel-landing-main" data-slide-to="0" class="active"></li>
            <li data-target="#carousel-landing-main" data-slide-to="1"></li>
            <li data-target="#carousel-landing-main" data-slide-to="2"></li>
            <li class="text"><span>${carouselFooter1}</span></li>
        </ol>
    </div>
    <!-- Wrapper for slides -->
    <div class="content container">
        <hgroup>
            <h1>${slogan}</h1>

            <h3 class="hidden-xs">${subtitle}</h3>
        </hgroup>
        <g:render template="/landing/servicesModules/${formName}"/>
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
                'slide-0': '${carouselFooter1}',
                'slide-1': '${carouselFooter2}',
                'slide-2': '${carouselFooter3}'
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