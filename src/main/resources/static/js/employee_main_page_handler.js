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
            "<form class='productForm' id='"+i+"' method='post' action='"+context+"/saveChanges'><input form='"+i+"' type='hidden' name='product_id' id='product_id' required='required' readonly='readonly' value='"+
            products[i].productId +
            "'/>" + "</form>" +
            "<tr><td>"+i+"</td>" +
            "<td>" + "<input form='"+i+"' type='text' name='name' id='name' minlength='3' value='"+
            products[i].productName +
            "'/>" +
            "</td>" +
            "<td>" + "<input form='"+i+"' list='categoriesList' autocomplete='off' name='category' id='category' value='"+
            products[i].category.category +
            "'/>" +
            "</td>" +
            "<td>" + "<input form='"+i+"' type='number' name='price' id='price' min='0' value='"+
            products[i].productPrice +
            "'/>"
             +" BYN"+
            "</td>" +
            "<td><img src='" +context+"/"+
            products[i].picturePath+
            "' alt='"+products[i].productName+"' width='60px' height='60px'/>"+"<br/><input form='"+i+"' type='file' name='picture' id='picture'/>"+
            +"</td>" +
            "<td>" + "<textarea form='"+i+"' name='comment' id='comment' minlength='3'>"+
            products[i].productComment +
            "</textarea>" +
            "</td>"+
            "<td><input form='"+i+"' type='number' name='amount'  id='amount' step='1' value='0'"+
            "/></td>" +
            "<td>" +
            products[i].amountInStock+ " ед."+
            "</td>"+
            "<td>" + "<input form='"+i+"' type='checkbox' name='isAvailable' id='isAvailable' min='0' checked='"+
            products[i].isAvailable +
            "'/>" +
            "</td>"+
            "<td>"+
            "<button form='"+i+"' type='submit' class='btn btn-success'>"+"Сохранить изменения"+
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

