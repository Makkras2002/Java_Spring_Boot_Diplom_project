let current_page = 1;
let records_per_page = 5;
let productData = document.getElementById("productData");
let context = document.getElementById("context").innerText;
let products=  JSON.parse(productData.innerHTML);

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
                "Необходимое количество" +
                "</td>" +
                "<td style='color: green; font-weight: bolder'>" +
                "Имеющееся количество на складе" +
                "</td>" +
                "<td>" +
                "" +
                "</td>" +
                "</tr>";
        }
        tr +=
            "<form id='"+i+"' method='post' action='"+context+"/addToBasket'><input form='"+i+"' type='hidden' name='product_id' id='product_id"+i+"' required='required' readonly='readonly' value='"+
            products[i].productId +
            "'/>" + "<input form='"+i+"' type='hidden' name='category_id' id='category_id"+i+"' required='required' readonly='readonly' value='"+
            products[i].category.categoryId +
            "'/><input form='"+i+"' type='hidden' name='_csrf' value='"+document.getElementById("csrf").innerText+"'/>" +
            "</form>" +
            "<tr><td>"+i+"</td>" +
            "<td>" +
            products[i].productName+
            "</td>" +
            "<td>" +
            products[i].category.category +
            "</td>" +
            "<td>" +
            products[i].productPrice +" BYN"+
            "</td>" +
            "<td><img src='" +context+"/"+
            products[i].picturePath+
            "' alt='"+products[i].productName+"' width='120px' height='120px'/></td>" +
            "<td>" +
            products[i].productComment+
            "</td>"+
            "<td><input form='"+i+"' type='number' name='amount'  id='amount"+i+"' min='1' max='"+products[i].amountInStock+"' step='1' required='required'"+
            "/></td>" +
            "<td>" +
            products[i].amountInStock+ " ед."+
            "</td>"+
            "<td>"+
            "<button form='"+i+"' type='submit' class='btn btn-primary'>"+"Добавить в корзину"+
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
    let error = document.getElementById("error").innerText;
    if(error != "") {
        alert(error);
    }
    changePage(1);
};