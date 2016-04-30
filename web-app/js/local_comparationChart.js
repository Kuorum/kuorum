


// valuation chart
$(function () {
    printComparative();
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
            $.each(activity.datasets, function (i, dataset) {

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
                            console.log(event)
                            alert(this.name + ' clicked\n' +
                                'Alt: ' + event.altKey + '\n' +
                                'Control: ' + event.ctrlKey + '\n' + 'Shift: ' + event.shiftKey + '\n');

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
                        layout:"horizontal",
                        verticalAlign:"top",
                        align:"left",
                        floating:true,
                        itemStyle:
                        {
                            fontSize:'14px',
                            fontWeight:'normal'
                        },
                        y:15,
                        x:0
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
                                y: -11
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
