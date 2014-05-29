var tour3
$(document).ready(function() {

    // tour 3
    $(function() {
        tour3 = new Tour({
            name: 'tour3',
            storage: false,
            debug: true,
            backdrop: true,
            template: '<div class="popover tour"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div><div class="popover-navigation"><button class="btn btn-grey" data-role="next">Continuar</button></div></div>',
            onEnd : function(tour3){
                window.location.href = "home-videomodal.htm";
            }
        });

        tour3.addSteps([
            {
                element: '#tour3-10',
                placement: 'bottom',
                title: '10/11 Impulsa la propuesta',
                content: 'Si te gusta mucho una publicación y crees que debe ser tenida en cuenta por nuestros parlamentarios, impúlsala con este botón y luego compártela con el mayor número de gente posible. Sólo last historias, preguntas y propuestas con un gran número de impulsos tienen posibilidades de llegar a las instituciones. Además puedes elegir si quieres que tu impulso sea anónimo, o si prefieres darle un apoyo incondicional al autor de la publicación mostrando tu nombre.'
            },
            {
                element: '#tour3-11',
                placement: 'bottom',
                template: '<div class="popover tour"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div><div class="popover-navigation"><button class="btn" data-role="end">Finalizar</button></div></div>',
                title: '11/11 Kakarea',
                content: 'Si te gusta tanto una publicación como para compartirla con otros usuarios, sólo tienes que kakarearla pulsando este botón. Y así todos tus seguidores podrán verla aunque no sigan al autor original. También puedes compartirla en otras redes sociales; de esta forma además nos ayudas a crecer. Cuantos más seamos, más fuerza tendremos.'
            }
        ]);

        // Initialize the tour
        tour3.init();

        // Start the tour
//        $('tour3.htm').ready(function() {
//            tour3.start();
//        });

    });


});