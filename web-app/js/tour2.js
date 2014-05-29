var tour2
$(document).ready(function() {

    // tour 2
    $(function() {
        tour2 = new Tour({
            name: 'tour2',
            storage: false,
            debug: true,
            backdrop: true,
            template: '<div class="popover tour"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div><div class="popover-navigation"><button class="btn btn-grey" data-role="next">Continuar</button></div></div>',
            onEnd : function(tour2){
                window.location.href = "tour3.htm";
            }
        });

        tour2.addSteps([
            {
                element: '#tour2-8',
                placement: 'bottom',
                title: '8/11 Aquí tienes voto',
                content: 'Además de escribir publicaciones para cambiar un texto legislativo, también puedes votar la ley en su conjunto. Da tu opinión de manera totalmente anónima. Si estás de acuerdo vota a favor, si no lo estás vota en contra y si no lo tienes claro... ya sabes. Pero no pierdas la oportunidad de votar esas leyes que hasta ahora sólo se votaban en el Parlamentpo. En Kuorum tienes la oportunidad de que tu voto cuente, ya que si alcanzamos los 1000 votos - si hay quórum - nos comprometemos a hacer llegar los resultados de la consulta a los parlamentarios.'
            },
            {
                element: '#tour2-9',
                placement: 'bottom',
                template: '<div class="popover tour"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div><div class="popover-navigation"><button class="btn btn-grey" data-role="end">Continuar</button></div></div>',
                title: '9/11 También tienes voz',
                content: 'Frenar la aprobación de una ley o promover que sea aprobada no es una tarea sencilla. Pero introducir pequeños cambios en el texto es algo que los grandes lobbies llevan mucho tiempo haciendo. ¡Ahora tú también puedes hacerlo! Kuorum es una plataforma para el lobby ciudadano. Encuentra la parte de la ley que quieres cambiar y haz una propuesta, si tienes dudas publica una pregunta y si crees que tu experiencia vital puede inspirar a los legisladores cuenta tu historia. Los parlamentarios presentes en Kuorum podrían apadrinar tu publicación para hacer llegar tu voz a las instituciones.'
            }
        ]);

        // Initialize the tour
        tour2.init();

        // Start the tour
//        $('tour2.htm').ready(function() {
//            tour2.start();
//        });

    });


});