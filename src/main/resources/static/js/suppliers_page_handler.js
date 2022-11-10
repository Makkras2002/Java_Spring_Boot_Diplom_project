let current_page = 1;
let records_per_page = 5;
let suppliersData = document.getElementById("suppliersData");
let context = document.getElementById("context").innerText;
let suppliers =  JSON.parse(suppliersData.innerHTML);

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
    let listing_table = document.querySelector("#suppliersTable");
    listing_table.innerHTML = "";
    let page_span = document.getElementById("page");

    if (page < 1) page = 1;
    if (page > numPages()) page = numPages();

    listing_table.innerHTML = "";

    for (let i = (page-1) * records_per_page; i < (page * records_per_page) && i < suppliers.length; i++) {
        let tr = "";
        if (i == (page-1) * records_per_page) {
            tr +=
                "<tr><td style='color: green; font-weight: bolder'>" +
                "N" +
                "</td>" +
                "<td style='color: green; font-weight: bolder'>" +
                "Название компании-поставщика" +
                "</td>" +
                "<td style='color: green; font-weight: bolder'>" +
                "Email компании-поставщика" +
                "</td>" +
                "<td style='color: green; font-weight: bolder'>" +
                "Контактный телефон компании-поставщика" +
                "</td>" +
                "<td style='color: green; font-weight: bolder'>" +
                "Местоположение компании-поставщика" +
                "</td>" +
                "<td style='color: green; font-weight: bolder'>" +
                "Статус активности компании-поставщика" +
                "</td>" +
                "<td>" +
                "" +
                "</td>" +
                "</tr>";
        }

        tr +=
            "<form id='"+i+"' method='post' action='"+context+"/updateSupplierData'><input form='"+i+"' type='hidden' name='supplier_id' id='supplier_id"+i+"' required='required' readonly='readonly' value='"+
            suppliers[i].supplierCompanyId +
            "'/><input form='"+i+"' type='hidden' name='_csrf' value='"+document.getElementById("csrf").innerText+"'/>" + "</form>" +
            "<tr><td>"+i+"</td>" +
            "<td>" + "<input onchange = \"changeColorOnFieldUpdate("+i+",'name')\" form='"+i+"' type='text' name='name' id='name"+i+"' minlength='3' maxlength='100' value='"+
            suppliers[i].supplierCompanyName +
            "'/>" +
            "</td>" +
            "<td>" + "<input onchange = \"changeColorOnFieldUpdate("+i+",'email')\" form='"+i+"' type='email' name='email' id='email"+i+"' minlength='3' maxlength='100' value='"+
            suppliers[i].supplierCompanyEmail +
            "'/>" +
            "</td>" +
            "<td>" + "<input onchange = \"changeColorOnFieldUpdate("+i+",'telNumber')\" form='"+i+"' type='tel' name='telNumber' id='telNumber"+i+"' minlength='3' maxlength='20' value='"+
            suppliers[i].telNumber +
            "'/>" +
            "</td>" +
            "<td>" + "<input onchange = \"changeColorOnFieldUpdate("+i+",'location')\" form='"+i+"' type='text' name='location' id='location"+i+"' minlength='3' maxlength='100' value='"+
            suppliers[i].location +
            "'/>" +
            "</td>" +
            "<td>" + "<input onchange = \"changeColorOnFieldUpdate("+i+",'isActive')\" class='form-check' form='"+i+"' type='checkbox' name='isActive' id='isActive"+i+"' checked='"+
            suppliers[i].isActive +
            "'/>" +
            "</td>"+
            "<td>"+
            "<button form='"+i+"' type='submit' onclick='checkIfDataWasUpdated(event,"+i+")' class='btn btn-success'>"+"Обновить данные"+
            "</button></td>"
            +"</tr>";
        listing_table.innerHTML += tr;
        if(!suppliers[i].isActive) {
            document.getElementById("isActive"+i).removeAttribute('checked');
        }
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
    return Math.ceil(suppliers.length / records_per_page);
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
            field = suppliers[formNumber].supplierCompanyName;
            break;
        }
        case 'email' : {
            field = suppliers[formNumber].supplierCompanyEmail;
            break;
        }
        case 'telNumber' : {
            field = suppliers[formNumber].telNumber;
            break;
        }
        case 'location' : {
            field = suppliers[formNumber].location;
            break;
        }
        case 'isActive' : {
            field = suppliers[formNumber].isActive;
            break;
        }
        default : {
            break;
        }
    }
    if(paramName === "isActive") {
        if(element.checked && field || !element.checked && !field) {
            document.getElementById(formNumber).classList.remove("updated_"+paramName);
        } else {
            document.getElementById(formNumber).classList.add("updated_"+paramName);
        }
    } else {
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

}
function checkIfDataWasUpdated(event,formNumber) {
    if(document.getElementById(formNumber).classList.length===0) {
        event.preventDefault();
        alert("Никакие данные о контрагентах не были изменены. Обновление не требуется.");
    }
}