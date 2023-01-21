let productsNames = JSON.parse(document.getElementById("soldProductsNames").innerHTML);
let productsStats = JSON.parse(document.getElementById("soldProductsStats").innerHTML);
let productsBoughtStats = JSON.parse(document.getElementById("boughtProductsStats").innerHTML);
let xValues = productsNames;
let yValues = productsStats;

new Chart("myChart", {
    // type: "pie",
    type: "doughnut",
    data: {
        labels: xValues,
        datasets: [{
            backgroundColor: fillAndReturnColorsList(),
            data: yValues
        }]
    },
    options: {
        title: {
            display: true,
            text: "Статистика продаж продуктов"
        },
        plugins: {
            labels: {
                render: 'percentage',
                fontStyle: 'bold',
                fontColor: 'white',
                precision: 2
            }
        },
    }
});
let barsData = {
    labels: productsNames,
    datasets: [
        {
            label: "Продано",
            backgroundColor: "green",
            data: productsStats
        },
        {
            label: "Закуплено",
            backgroundColor: "red",
            data: productsBoughtStats
        }
    ]
};
new Chart("myBarsChart", {
    type: "bar",
    data: barsData,
    options: {
        barValueSpacing: 20,
        scales: {
            yAxes: [{
                ticks: {
                    min: 0,
                }
            }]
        },
        title: {
            display: true,
            text: "Диаграмма соотношения закупленного и проданного количества товаров"
        },
        plugins: {
            labels: {
                render: 'percentage',
                fontWeight: 0,
                fontColor: 'white',
                precision: 0
            }
        },
    }
});
function getRandomColor() {
    let letters = '0123456789ABCDEF'.split('');
    let color = '#';
    for (let i = 0; i < 6; i++ ) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}
function fillAndReturnColorsList() {
    let barColors = [];
    for (let i = 0; i< productsNames.length;i++){
        barColors.push(getRandomColor());
    }
    return barColors;
}


let xValues2 = JSON.parse(document.getElementById("dates").innerHTML);
let yValues2 = JSON.parse(document.getElementById("earnedMoneyAmount").innerText);

new Chart("myChartLine", {
    type: "line",
    data: {
        labels: xValues2,
        datasets: [{
            //fill: false,
            backgroundColor: 'green',
            borderColor: 'darkgreen',
            data: yValues2
        }]
    },
    options: {
        legend: {display: false},
        title: {
            display: true,
            text: "Статистика заработка от продаж по дням (в BYN)"
        },
        scales: {
            x: {
                type: 'time',
                time: {
                    displayFormats: {
                        quarter: 'YYYY-MM-DD'
                    }
                }
            }
        }
    }
});

let xValues3 = JSON.parse(document.getElementById("dates2").innerHTML);
let yValues3 = JSON.parse(document.getElementById("spentMoneyAmount").innerText);

new Chart("myChartLine2", {
    type: "line",
    data: {
        labels: xValues3,
        datasets: [{
            backgroundColor: '#FFCCCB',
            borderColor: 'red',
            data: yValues3
        }]
    },
    options: {
        legend: {display: false},
        title: {
            display: true,
            text: "Статистика затрат на пополнение склада по дням (в BYN)"
        },
        scales: {
            x: {
                type: 'time',
                time: {
                    displayFormats: {
                        quarter: 'YYYY-MM-DD'
                    }
                }
            }
        }
    }
});

