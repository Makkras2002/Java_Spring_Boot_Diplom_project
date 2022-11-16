let current_page = 1;
let records_per_page = 5;
let productData = document.getElementById("productData");
let productCategoriesData = document.getElementById("productCategories");
let context = document.getElementById("context").innerText;
let products =  JSON.parse(productData.innerHTML);
let categories = JSON.parse(productCategoriesData.innerHTML);



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
    let listing_table = document.querySelector("#productTable");
    listing_table.innerHTML = "";
    let page_span = document.getElementById("page");

    if (page < 1) page = 1;
    if (page > numPages()) page = numPages();

    listing_table.innerHTML = "";

    for (let i = (page-1) * records_per_page; i < (page * records_per_page) && i < products.length; i++) {
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
                "Категория" +
                "</td>" +
                "<td style='color: green; font-weight: bolder'>" +
                "Цена" +
                "</td>" +
                "<td style='color: green; font-weight: bolder'>" +
                "Изображение" +
                "</td>" +
                "<td style='color: green; font-weight: bolder'>" +
                "Комментарий" +
                "</td>" +
                "<td style='color: green; font-weight: bolder'>" +
                "Уменьшить или увеличить кол-во продукта" +
                "</td>" +
                "<td style='color: green; font-weight: bolder'>" +
                "Имеющееся количество на складе" +
                "</td>" +
                "<td style='color: green; font-weight: bolder'>" +
                "Товар доступен для продажи" +
                "</td>" +
                "<td>" +
                "" +
                "</td>" +
                "</tr>";
        }

        tr +=
            "<form enctype='multipart/form-data' id='"+i+"' method='post' action='"+context+"/updateProductData'><input form='"+i+"' type='hidden' name='product_id' id='product_id"+i+"' required='required' readonly='readonly' value='"+
            products[i].productId +
            "'/><input form='"+i+"' type='hidden' name='_csrf' value='"+document.getElementById("csrf").innerText+"'/>" + "</form>" +
            "<tr><td>"+i+"</td>" +
            "<td>" + "<input onchange = \"changeColorOnFieldUpdate("+i+",'name')\" form='"+i+"' type='text' name='name' id='name"+i+"' minlength='3' maxlength='100' value='"+
            products[i].productName +
            "'/>" +
            "</td>" +
            "<td>" + "<input onchange = \"changeColorOnFieldUpdate("+i+",'category')\" form='"+i+"' list='categoriesList' minlength='3' maxlength='100' autocomplete='off' name='category' id='category"+i+"' value='"+
            products[i].category.category +
            "'/>" +
            "</td>" +
            "<td>" + "<input onchange = \"changeColorOnFieldUpdate("+i+",'price')\" form='"+i+"' type='number' name='price' id='price"+i+"' min='0' step='0.01' required='required' value='"+
            products[i].productPrice +
            "'/>"
             +" BYN"+
            "</td>" +
            "<td><img src='" +context+"/prod_pics/"+
            products[i].picturePath +
            "' alt='"+products[i].productName+"' width='60px' height='60px'/>"+"<br/><br/><input onchange = \"changeColorOnFieldUpdate("+i+",'picture')\" form='"+i+"' type='file' accept='.png,.jpg' name='picture' id='picture"+i+"' style='display: none'/><input class='btn btn-sm btn-secondary' type=\"button\" value=\"Выбрать...\" onclick=\"document.getElementById('picture"+i+"').click();\"/></td>" +
            "<td>" + "<textarea onchange = \"changeColorOnFieldUpdate("+i+",'comment')\" form='"+i+"' name='comment' id='comment"+i+"' minlength='3'>"+
            products[i].productComment +
            "</textarea>" +
            "</td>"+
            "<td><input onchange = \"changeColorOnFieldUpdate("+i+",'amount')\" form='"+i+"' type='number' name='amount'  id='amount"+i+"' required='required' step='1' value='0'"+
            "/></td>" +
            "<td>" +
            products[i].amountInStock+ " ед." +
            "</td>"+
            "<td>" + "<input onchange = \"changeColorOnFieldUpdate("+i+",'isAvailable')\" class='form-check' form='"+i+"' type='checkbox' name='isAvailable' id='isAvailable"+i+"' checked='"+
            products[i].isAvailable +
            "'/>" +
            "</td>"+
            "<td>"+
            "<button form='"+i+"' type='submit' onclick='checkIfDataWasUpdated(event,"+i+")' class='btn btn-success'>"+"Обновить данные"+
            "</button></td>"
            +"</tr>";
        listing_table.innerHTML += tr;
        if(!products[i].isAvailable) {
            document.getElementById("isAvailable"+i).removeAttribute('checked');
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
    return Math.ceil(products.length / records_per_page);
}

window.onload = function() {
    let categoriesSelectList = document.getElementById("categoriesList");
    let option;
    if (categories.length > 0) {
        for (let j = 0; j < categories.length; j++) {
            option = document.createElement("option");
            option.text = categories[j].category;
            option.value = categories[j].category;
            categoriesSelectList.appendChild(option);
        }
    }
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
            field = products[formNumber].productName;
            break;
        }
        case 'category' : {
            field = products[formNumber].category.category;
            break;
        }
        case 'price' : {
            field = products[formNumber].productPrice.toString();
            break;
        }
        case 'comment' : {
            field = products[formNumber].productComment;
            break;
        }
        case 'amount' : {
            field = '0';
            break;
        }
        case 'isAvailable' : {
            field = products[formNumber].isAvailable;
            break;
        }
        case 'picture' : {
            field = products[formNumber].picturePath;
            break;
        }
        default : {
            break;
        }
    }
    if(paramName === "isAvailable") {
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
        alert("Никакие данные о продукте не были изменены. Обновление не требуется.");
    }
}

