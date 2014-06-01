$(document).ready(function() {

    startPieStat()
	// inicializa mapa
	$(function(){
		$.getJSON(urlDataMap, function(data){
		var map = new jvm.WorldMap({
			map: 'spain',
			backgroundColor: '#ffffff',
			zoomOnScroll: false,
			regionsSelectable: true,
			regionsSelectableOne: true,
			regionStyle: {
				initial: {
					fill: '#EFEFEF',
					"fill-opacity": 1,
					stroke: 'yes',
					"stroke-width": 3,
					"stroke-opacity": 1
				},
				hover: {
					fill: '#ff9431',
					"fill-opacity": 0.7
				},
				selected: {
					fill: '#ff9431',
					"fill-opacity": 1
				},
				selectedHover: {
					fill: '#ff9431',
					"fill-opacity": 1
				}
			},
		    container: $('#map'),
		    	series: {
		    		regions: [{
                        scale: {
                            '1': '#93bfdb',
                            '2': '#213b47',
                            '3': '#d1d1d1'
                        },
                        attribute: 'fill',
                        values: data['votation'].results
                    }]
		    },
		    onRegionSelected: function(event, selectedRegion){
                var data = map.series.regions[0].elements[selectedRegion].config
				$('.kakareo.post.ley.ficha > h2 > span').text(data.name);
		    }
		   });

		});

		$('#allMap').on('click', function (e) {
			e.preventDefault();
			var map = $('#map').vectorMap('get', 'mapObject');
			map.clearSelectedRegions();
			$('.kakareo.post.ley.ficha > h2 > span').text('España');
		});

	});
});

function startPieStat(){
    var votesdata = [
        // A favor
        {
            value: 1498,
            color:"#93bfdb"
        },
        // En contra
        {
            value : 1687,
            color : "#213b47"
        },
        // Abstenciones
        {
            value : 1978,
            color : "#d1d1d1"
        }

    ];
    var globalGraphSettings = {
        animation : Modernizr.canvas, // si el navegador no soporta canvas deshabilito animación
        segmentShowStroke : false,
        percentageInnerCutout : 60
    };
    var ctx = $("#votesChart").get(0).getContext("2d");
    var myDoughnut = new Chart(ctx);
    new Chart(ctx).Doughnut(votesdata, globalGraphSettings);
}