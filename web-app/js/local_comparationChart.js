


// valuation chart
$(function () {
    printComparative();


    //The click before adding event click. If not 2 charts will be displayed the first time
    $("#real-time ul.nav li a")[0].click();
    $("#real-time .nav a").on("click", function(e){
        var tabId = $(this).attr("href")
        var graphDiv = $(tabId +" .polValChart").attr("id")
        printChart("#"+graphDiv)
    });
});


function printComparative(){
    var divId = "#comparativeValChart"
    if ($(divId).length >0){
        $(divId).html("")
        Highcharts.setOptions({
            colors: ['#ff9431', '#999999', '#ED561B', '#DDDF00', '#24CBE5', '#64E572', '#FF9655', '#FFF263', '#6AF9C4'],
            global: {
                useUTC: false
            }
        });
        var urlHighchart=$(divId).attr("data-urlJson")

        $.getJSON(urlHighchart, function (activity) {
            var seriesData = []
            var alias = {}
            $.each(activity.datasets, function (i, dataset) {
                alias[dataset.name]=dataset.alias
                seriesData[i]= {
                    data: dataset.data,
                    name: dataset.name,
                    type: dataset.type,
                    pointInterval: 1 * 3600 * 1000, // cada 1h
                    color: Highcharts.getOptions().colors[i],
                    fillOpacity: 0.3,
                    tooltip: {
                        valueSuffix: ' '
                    },
                    cursor: 'pointer',
                    events: {
                        click: function (event) {
                            $("#tab-link-"+alias[this.name]).click()
                        }
                    }
                }
            });
            $('<div class="chart">')
                .appendTo(divId)
                .highcharts('StockChart', {
                    chart: {
                        spacingBottom: 10,
                        spacingTop: -23,
                        zoomType: 'x',
                        height: 300,
                        //marginLeft:240
                        //events : {
                        //    load : function () {
                        //        // set up the updating of the chart each second
                        //        var series = this.series;
                        //        setInterval(function () {
                        //            $.getJSON(urlHighchart, function (activity) {
                        //                $.each(activity.datasets, function (i, dataset) {
                        //                    series[i].setData(dataset.data);
                        //                });
                        //            });
                        //
                        //        }, 1000);
                        //    }
                        //}
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
                        layout:"vertical",
                        verticalAlign:"top",
                        align:"left",
                        padding:30,
                        itemMarginTop:10,
                        floating:false,
                        itemStyle:
                        {
                            fontSize:'14px',
                            fontWeight:'normal'
                        },
                        y:40,
                        //x:0
                    },
                    xAxis: {
                        type: 'datetime',
                        //minTickInterval: 24 * 1000 * 3600, // intervalo cada 1h
                        //minorTickInterval: 24 * 1000 * 3600, // intervalo cada 1h
                        range: 3 * 24 * 3600 * 1000, // mostramos 1 semana
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
                        offset: 20
                    },
                    tooltip: {
                        positioner: function () {
                            return {
                                x: 5,
                                y: 0
                            };
                        },
                        backgroundColor: 'rgba(255, 255, 255, 1)',
                        borderWidth: 0,
                        pointFormat: '<span style="color:{point.color}"> {series.name}: <b>{point.y}</b></span>',
                        //pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b> ({point.change}%)<br/>',
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
