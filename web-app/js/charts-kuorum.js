
// valuation chart
$(function () {
    printCharts();
});
function printCharts(){
    $(".polValChart").each(function(idx){
        var uuid = guid();
        $(this).attr("id",uuid);
        printChart("#"+uuid);
    })
}
function printChart(divId){
    if ($(divId).length >0){
        $(divId).html("");
        $(divId).parent().show();
        Highcharts.setOptions({
            colors: ['#ff9431', '#999999', '#ED561B', '#DDDF00', '#24CBE5', '#64E572', '#FF9655', '#FFF263', '#6AF9C4'],
            global: {
                useUTC: false
            }
        });
        var urlHighchart=$(divId).attr("data-urljs");
        if (urlHighchart == undefined){
            urlHighchart = 'mock//valpol.json'
        }

        $.getJSON(urlHighchart, function (activity) {
            var seriesData = [];
            $.each(activity.datasets, function (i, dataset) {

                seriesData[i]={
                    data: dataset.data,
                    name: dataset.name,
                    type: dataset.type,
                    pointInterval: 1 * 3600 * 1000, // cada 1h
                    color: Highcharts.getOptions().colors[i],
                    fillOpacity: 0.3,
                    tooltip: {
                        valueSuffix: ' ' + dataset.unit
                    }
                }
            });
            var divHeight = $(divId).height();
            $('<div class="chart">')
                .appendTo(divId)
                .highcharts('StockChart', {
                    chart: {
                        spacingBottom: 10,
                        spacingTop: 0,
                        zoomType: 'x',
                        height: divHeight,
                        events : {
                            //load : function () {
                            //    // set up the updating of the chart each second
                            //    var series = this.series;
                            //    setInterval(function () {
                            //        $.getJSON(urlHighchart, function (activity) {
                            //            $.each(activity.datasets, function (i, dataset) {
                            //                series[i].setData(dataset.data);
                            //            });
                            //        });
                            //
                            //    }, 1000);
                            //}
                        }
                    },
                    title: {
                        text: null,
                        //text: activity.title,
                        align: 'left',
                        margin: 5,
                        color: '#666666',
                        x: 0
                    },
                    credits: {
                        enabled: false
                    },
                    legend: {
                        enabled: true,
                        layout:"horizontal",
                        verticalAlign:"top",
                        align:"left",
                        floating:true,
                        itemStyle:
                            {
                                fontSize:'14px',
                                fontWeight:'normal'
                            },
                        y:-5,
                        x:0
                    },
                    xAxis: {
                        type: 'datetime',
                        //minTickInterval: 24 * 1000 * 3600, // intervalo cada 1h
                        //minorTickInterval: 24 * 1000 * 3600, // intervalo cada 1h
                        range: 13 * 24 * 3600 * 1000, // mostramos 1 semana
                        dateTimeLabelFormats:{
                            millisecond: '%H:%M:%S.%L',
                            second: '%H:%M:%S',
                            minute: '%H:%M',
                            hour: '%H:%M',
                            day: '%e/%m',
                            week: '%e/%m',
                            month: '%b \'%y',
                            year: '%Y'
                        }
                    },
                    yAxis: {
                        title: {
                            text: null
                        },
                        allowDecimals: false,
                        crosshair: true,
                        //minTickInterval: 1,
                        minorGridLineColor: '#F0F0F0',
                        //minorTickInterval:null,
                        tickPositions: [1, 2, 3, 4, 5, 6],
                        plotLines: [{
                            value: activity.average,
                            width: 1,
                            color: '#666666',
                            dashStyle: 'dash',
                            label: {
                                text: activity.averageLabel,
                                align: 'left',
                                y: 0,
                                x: -2,
                                rotation:270,
                                style:{
                                    color:'#666666'
                                }
                            }
                        }],
                        offset: 20
                    },
                    tooltip: {
                        formatter: function () {
                            var date = new Date(this.x);
                            var s = '<b>'+formatTooltipDate(date)+'</b>';
                            s += '<br/> -----------'; //CHAPU BR

                            $.each(this.points, function () {
                                s += '<br/><span style="color:'+this.series.color+'">' + this.series.name + '</span><span style="float:right">: ' +
                                    Math.floor(this.y*100)/100 + '</span>';
                            });
                            return s;
                        },
                        backgroundColor: 'rgba(240, 240, 240, 0.8)',
                        borderWidth: 1,
                        borderRadius:15,
                        borderColor:'#666',
                        headerFormat: '',
                        shadow: false,
                        style: {
                            fontSize: '15px'
                        },
                        valueDecimals: 2,
                    },
                    rangeSelector: {
                        enabled: false
                    },
                    scrollbar: {
                        enabled: false
                    },
                    navigator: {
                        height: 20,
                        margin: 10,
                        maskFill: 'rgba(0, 0, 0, 0.05)',
                        maskInside: false
                    },
                    series: seriesData
                });
        });
    }
}