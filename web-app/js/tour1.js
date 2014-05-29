var tour1
$(document).ready(function() {

    // tour 1
    $(function() {
        tour1 = new Tour({
            name: 'tour1',
            storage: false,
            debug: true,
            backdrop: true,
            template: '<div class="popover tour"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div><div class="popover-navigation"><button class="btn btn-grey" data-role="next">Continuar</button></div></div>',
            onEnd : function(tour1){
                window.location.href = urls.tour.tour2;
            }
        });

        tour1.addSteps([
            {
                element: '#brand',
                placement: 'bottom',
                title: '1/11 ¿Qué hago aquí?',
                content: 'Kuorum funciona como un blog en el que tus publicaciones pueden cambiar las leyes. Cada vez que accedas a Kuorum verás toda la actividad de los usuarios a los que sigues y conocerás la actualidad de las leyes que te afectan. Esta es una iniciativa de gente que, como tú, cree que es posible cambiar las cosas. Puedes hacer 3 tipos de publicaciones sobre las leyes que te interesan: historias, preguntas o propuestas. También puedes comentar las publicaciones de otros, compartirlas e impulsarlas para que lleguen a oídos de los parlamentarios.'
            },
            {
                element: '#list-kakareos-id li:first div.user:first span.action',
                placement: 'bottom',
                title: '2/11 ¿Qué es un kakareo?',
                content: 'Los títulos de las publicaciones en Kuorum se llaman kakareos. En tu tablero puedes ver los kakareos de la gente a la que sigues.'
            },
            {
                element: '#list-kakareos-id li:first footer ul.actions-kak li.kakareo-number a.action.cluck:first',
                placement: 'bottom',
                title: '3/11 ¿Cómo kakarear?',
                content: 'Si te gusta tanto una publicación como para compartirla con otros usuarios, sólo tienes que kakarearla pulsando este botón. Y así todos tus seguidores podrán verla aunque no sigan al autor original.'
            },
            {
                element: '#list-kakareos-id li:first footer ul.actions-kak li.more-actions',
                placement: 'bottom',
                title: '4/11 ¿Cómo compartir?',
                content: 'También puedes compartirla en otras redes sociales usando el menú desplegable "+".'
            },
            {
                element: '#list-kakareos-id li:first div.link-wrapper h1 a',
                placement: 'bottom',
                title: '5/11 Acceder a la ley',
                content: 'Si estás muy interesado en una ley determinada y no quieres perderte nada, puedes clicar en su #hashtag para acceder a ella. Allí podrás votarla, ver todas las publicaciones relacionadas y hasta el texto completo y los detalles técnicos de la norma.'
            },
            {
                element: '#list-kakareos-id li:first footer ul.actions-kak li.read-later',
                placement: 'bottom',
                title: '6/11 Leer más tarde',
                content: 'Sabemos que muchas veces no vas a encontrar tiempo para leer todas las publicaciones que te interesan. Por eso te damos la posibilidad de marcar los kakareos para leer la publicación completa más tarde. Así siempre podrás consultarlas desde tu tablero o desde tus páginas de configuración.'
            },
            {
                element: 'section.boxes.guay:first > h1',
                placement: 'bottom',
                template: '<div class="popover tour"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div><div class="popover-navigation"><button class="btn btn-grey" data-role="end">Continuar</button></div></div>',
                title: '7/11 No te pierdas nada',
                content: 'En esta columna te iremos mostrando leyes, publicaciones, personas y todo lo que no has visto pero te puede interesar. Para que no te pierdas nada, para que descubras más gente y más leyes interesantes; pero sobre todo para que puedas participar más.',
            }
        ]);

        // Initialize the tour
        tour1.init();

        // Start the tour

        if ($(location).attr('pathname') == urls.tour.tour1){
            tour1.start();
        }

    });


});