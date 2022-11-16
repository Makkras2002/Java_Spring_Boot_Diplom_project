let current_page = 1;
let records_per_page = 5;
let expensesData = document.getElementById("expensesData");
let context = document.getElementById("context").innerText;
let expenses =  JSON.parse(expensesData.innerHTML);

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
    let listing_table = document.querySelector("#expensesTable");
    listing_table.innerHTML = "";
    let page_span = document.getElementById("page");

    if (page < 1) page = 1;
    if (page > numPages()) page = numPages();

    listing_table.innerHTML = "";

    for (let i = (page-1) * records_per_page; i < (page * records_per_page) && i < expenses.length; i++) {
        let tr = "";
        if (i == (page-1) * records_per_page) {
            tr +=
                "<tr><td style='color: green; font-weight: bolder'>" +
                "N" +
                "</td>" +
                "<td style='color: green; font-weight: bolder'>" +
                "Название" +
                "</td>" +
                "<td style='color: green; font-weight: bolder'>" +
                "Сумма" +
                "</td>" +
                "<td style='color: green; font-weight: bolder'>" +
                "Период" +
                "</td>" +
                "<td>" +
                "" +
                "</td>" +
                "<td>" +
                "" +
                "</td>" +
                "</tr>";
        }

        tr +=
            "<form id='"+i+"' method='post' action='"+context+"/updateExpensesData'><input form='"+i+"' type='hidden' name='expenses_id' id='expenses_id"+i+"' required='required' readonly='readonly' value='"+
            expenses[i].expensesId +
            "'/><input form='"+i+"' type='hidden' name='_csrf' value='"+document.getElementById("csrf").innerText+"'/>" + "</form>" +
            "<tr><td>"+i+"</td>" +
            "<td>" + "<input onchange = \"changeColorOnFieldUpdate("+i+",'name')\" form='"+i+"' type='text' name='name' id='name"+i+"' minlength='3' maxlength='100' value='"+
            expenses[i].expensesName +
            "'/>" +
            "</td>" +
            "<td>" + "<input onchange = \"changeColorOnFieldUpdate("+i+",'expensesAmount')\" form='"+i+"' type='number' name='expensesAmount' required='required' id='expensesAmount"+i+"' min='0' step='0.01'  value='"+
            expenses[i].expensesAmount +
            "'/>" + " BYN"+
            "</td>" +
            "<td>" + "<input onchange = \"changeColorOnFieldUpdate("+i+",'period')\" form='"+i+"' type='text' title='Должен быть \"DAILY\", \"WEEKLY\",\"MONTHLY\", or \"YEARLY\"!' name='period' id='period"+i+"' list='periodTypesList' pattern='DAILY|WEEKLY|MONTHLY|YEARLY'  required='required' autocomplete='off' value='"+
            expenses[i].period +
            "'/>" +
            "</td>" +
            "<td>"+
            "<button form='"+i+"' type='submit' onclick='checkIfDataWasUpdated(event,"+i+")' class='btn btn-success'>"+"Обновить данные"+
            "</button></td>"+
            "<td>"+
            "<form id='delete"+i+"' method='post' action='"+context+"/deleteExpensesData'><input form='delete"+i+"' type='hidden' name='_csrf' value='"+document.getElementById("csrf").innerText+"'/><input type='hidden' name='expenses_id_for_delete' id='expenses_id_for_delete"+i+"' value='"+expenses[i].expensesId+"' form='delete"+i+"'/><button form='delete"+i+"' type='submit' class='btn btn-outline-danger'>"+"Удалить данные"+
            "</button></form></td>"
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
    return Math.ceil(expenses.length / records_per_page);
}

window.onload = function() {
    let error = document.getElementById("error").innerText;
    if(error != "") {
        alert(error);
    }
    changePage(1);
};

function changeColorOnFieldUpdate(formNumber,paramName) {
    let element = document.getElementById(paramName+formNumber);
    let field;
    switch (paramName) {
        case 'name' : {
            field = expenses[formNumber].expensesName;
            break;
        }
        case 'expensesAmount' : {
            field = expenses[formNumber].expensesAmount.toString();
            break;
        }
        case 'period' : {
            field = expenses[formNumber].period;
            break;
        }
        default : {
            break;
        }
    }
    if(element.value !== field) {
        element.style.color = "orange";
        element.style.fontStyle = "italic";
        document.getElementById(formNumber).classList.add("updated_"+paramName);
    }  else {
        element.style.color = "black";
        element.style.fontStyle = "normal";
        document.getElementById(formNumber).classList.remove("updated_"+paramName);
    }

}
function checkIfDataWasUpdated(event,formNumber) {
    if(document.getElementById(formNumber).classList.length===0) {
        event.preventDefault();
        alert("Никакие данные о расходах не были изменены. Обновление не требуется.");
    }
}