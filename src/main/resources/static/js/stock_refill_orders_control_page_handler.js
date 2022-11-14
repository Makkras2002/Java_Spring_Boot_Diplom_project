let current_page = 1;
let records_per_page = 5;
let ordersData = document.getElementById("ordersData");
let context = document.getElementById("context").innerText;
let orders =  JSON.parse(ordersData.innerHTML);
let color;
let status;

function prevPage()
{
    if (current_page > 1) {
        current_page--;
        changePage(current_page);
    }
}

function nextPage()
{
    if (current_page < numPages()) {
        current_page++;
        changePage(current_page);
    }
}

function changePage(page)
{
    let btn_next = document.getElementById("next");
    let btn_prev = document.getElementById("prev");
    let listing_table = document.querySelector("#ordersTable");
    listing_table.innerHTML = "";
    let page_span = document.getElementById("page");

    if (page < 1) page = 1;
    if (page > numPages()) page = numPages();

    listing_table.innerHTML = "";

    for (let i = (page-1) * records_per_page; i < (page * records_per_page) && i < orders.length; i++) {
        let tr = "";
        if (i == (page-1) * records_per_page) {
            tr +=
                "<tr><td style='color: green; font-weight: bolder'>" +
                "N" +
                "</td>" +
                "<td style='color: green; font-weight: bolder'>" +
                "Имя работника, совершившего заказ" +
                "</td>" +
                "<td style='color: green; font-weight: bolder'>" +
                "Компоненты" +
                "</td>" +
                "<td style='color: green; font-weight: bolder'>" +
                "Дата совершения" +
                "</td>" +
                "<td style='color: green; font-weight: bolder'>" +
                "Названние компании-поставщика" +
                "</td>" +
                "<td style='color: green; font-weight: bolder'>" +
                "Статус" +
                "</td>" +
                "<td>" +
                "</td>" +
                "</tr>";
        }
        if(orders[i].isCompleted){
            status = "Заказ подтвержден";
            color = "green";
        } else {
            status = "Заказ не подтвержден";
            color = "red";
        }
        let components = "<table class='table'><tr><td style='color: green; font-weight: bolder'>Название</td>"+
            "<td style='color: green; font-weight: bolder'>Количество</td>"+
            "<td style='color: green; font-weight: bolder'>Цена</td></tr><tbody>";
        for(let j =0; j < orders[i].stockRefillComponentOrders.length; j++) {
            components+="<tr><td>"+orders[i].stockRefillComponentOrders[j].product.productName +
                "</td><td>"+ orders[i].stockRefillComponentOrders[j].orderedProductAmount +" ед.</td><td>"+
                orders[i].stockRefillComponentOrders[j].orderedProductFullPrice + " BYN</td></tr>";
        }
        components+="</tbody></table>";
        tr +=
            "<form id='"+i+"' method='post' action='"+context+"/completeSROrder'><input form='"+i+"' type='hidden' name='order_id' id='order_id"+i+"' required='required' readonly='readonly' value='"+
            orders[i].completeStockRefillOrderId +
            "'/>" + "<input form='"+i+"' type='hidden' name='_csrf' value='"+document.getElementById("csrf").innerText+"'/>" +
            "<input form='"+i+"' type='hidden' name='isCompleted' id='isCompleted"+i+"' value='"+orders[i].isCompleted+"'/>" +
            "</form>" +
            "<tr><td>"+i+"</td>" +
            "<td>" +
            orders[i].user.login+
            "</td>" +
            "<td>" +
            components+
            "</td>" +
            "<td>" +
            orders[i].completeStockRefillOrderDate.day+"."+orders[i].completeStockRefillOrderDate.month+"."+orders[i].completeStockRefillOrderDate.year+
            "</td>"+
            "<td>" +
            orders[i].supplierCompany.supplierCompanyName+
            "</td>"+
            "<td><div style='color: "+color+"' >" +
            status+
            "</div></td>"+
            "<td>"+
            "<button form='"+i+"' onclick='checkIfOrderIsCompleted(event,"+i+")' id='confirmOrderButton' type='submit' class='btn btn-primary btn-sm'>"+"Подтвердить заказ"+
            "</button></td>"
            +"</tr>";
        listing_table.innerHTML += tr;
    }
    page_span.innerHTML = page;

    if (page == 1) {
        btn_prev.style.visibility = "hidden";
    } else {
        btn_prev.style.visibility = "visible";
    }

    if (page == numPages()) {
        btn_next.style.visibility = "hidden";
    } else {
        btn_next.style.visibility = "visible";
    }
}

function numPages()
{
    return Math.ceil(orders.length / records_per_page);
}

window.onload = function() {
    let error = document.getElementById("error").innerText;
    if(error != "") {
        alert(error);
    }
    changePage(1);
};

function checkIfOrderIsCompleted(event,formNumber) {
    if(document.getElementById("isCompleted"+formNumber).value === 'true') {
        event.preventDefault();
        alert("Этот заказ уже подтверждён!");
    }
}