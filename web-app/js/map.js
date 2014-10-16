var getKeys = function(obj){
    var keys = [];
    for(var key in obj){
        keys.push(key);
    }
    return keys;
}

$(document).ready(function() {
    startPieStat(votes)
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
            onRegionClick: function(event, selectedRegion){
                var data = map.series.regions[0].elements[selectedRegion].config
				$('.kakareo.post.ley.ficha > h2 > span').text(data.name);
                loadPieStats(selectedRegion)
		    }
		   });
            //Selection subRegion of map
            regions = getKeys(map.params.series.regions[0].values)
            if ($.inArray(regionSelected, regions)!= -1){
                map.setSelectedRegions(regionSelected)
            }

		});

		$('#allMap').on('click', function (e) {
			e.preventDefault();
			var map = $('#map').vectorMap('get', 'mapObject');
			map.clearSelectedRegions();
			$('.kakareo.post.ley.ficha > h2 > span').text('España')
            loadPieStats("EU-ES")

		});

	});
});
function loadPieStats(regionIso3166){
    $.ajax( {
        url:urlPieChart,
        data:"regionIso3166="+regionIso3166,
        statusCode: {
            500: function() {
                display.warn("Disculpe las molestias, hubo algún problema interno")
            }
        }
    }).done(function(data, status, xhr) {
        startPieStat(data.totalVotes)
        $("table.segmentation .favor        .hombre span").html(data.genderVotes.MALE.yes)
        $("table.segmentation .contra       .hombre span").html(data.genderVotes.MALE.no)
        $("table.segmentation .abstencion   .hombre span").html(data.genderVotes.MALE.abs)
        $("table.segmentation .favor        .mujer span").html(data.genderVotes.FEMALE.yes)
        $("table.segmentation .contra       .mujer span").html(data.genderVotes.FEMALE.no)
        $("table.segmentation .abstencion   .mujer span").html(data.genderVotes.FEMALE.abs)
        $("table.segmentation .favor        .todos  span").html(data.genderVotes.ORGANIZATION.yes)
        $("table.segmentation .contra       .todos  span").html(data.genderVotes.ORGANIZATION.no)
        $("table.segmentation .abstencion   .todos  span").html(data.genderVotes.ORGANIZATION.abs)
    })
}

function startPieStat(votes){
    var votesdata = [
        // A favor
        {
            value: votes.yes,
            color:"#93bfdb"
        },
        // En contra
        {
            value : votes.no,
            color : "#213b47"
        },
        // Abstenciones
        {
            value : votes.abs,
            color : "#d1d1d1"
        }

    ];
    $("#votation .graphContainer .numberVotes").html(votes.total)
    $("#votation .activity li.favor span").html(votes.yes)
    $("#votation .activity li.contra span").html(votes.no)
    $("#votation .activity li.abstencion span").html(votes.abs)
    var globalGraphSettings = {
        animation : Modernizr.canvas, // si el navegador no soporta canvas deshabilito animación
        segmentShowStroke : false,
        percentageInnerCutout : 60
    };
    var ctx = $("#votesChart").get(0).getContext("2d");
    var myDoughnut = new Chart(ctx);
    new Chart(ctx).Doughnut(votesdata, globalGraphSettings);
}