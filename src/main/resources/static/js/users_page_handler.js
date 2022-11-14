let current_page = 1;
let records_per_page = 5;
let usersData = document.getElementById("usersData");
let context = document.getElementById("context").innerText;
let users =  JSON.parse(usersData.innerHTML);
let color;
let status;
let buttonText;
let roleColor;
let roleButtonText;

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
    let listing_table = document.querySelector("#usersTable");
    listing_table.innerHTML = "";
    let page_span = document.getElementById("page");

    if (page < 1) page = 1;
    if (page > numPages()) page = numPages();

    listing_table.innerHTML = "";

    for (let i = (page-1) * records_per_page; i < (page * records_per_page) && i < users.length; i++) {
        let tr = "";
        if (i == (page-1) * records_per_page) {
            tr +=
                "<tr><td style='color: green; font-weight: bolder'>" +
                "N" +
                "</td>" +
                "<td style='color: green; font-weight: bolder'>" +
                "Имя пользователя" +
                "</td>" +
                "<td style='color: green; font-weight: bolder'>" +
                "Email пользователя" +
                "</td>" +
                "<td style='color: green; font-weight: bolder'>" +
                "Роль пользователя" +
                "</td>" +
                "<td style='color: green; font-weight: bolder'>" +
                "Статус" +
                "</td>" +
                "<td>" +
                "</td>" +
                "<td>" +
                "</td>" +
                "</tr>";
        }
        if(users[i].isActive){
            status = "Пользователь активен";
            color = "green";
            buttonText = "Деактивировать пользователя";
        } else {
            status = "Пользователь не активен";
            color = "red";
            buttonText = "Активировать пользователя";
        }
        if(users[i].role.roleType === "CLIENT"){
            roleColor = "orange";
            roleButtonText = "Предоставить права сотрудника";
        } else {
            roleColor = "purple";
            roleButtonText = "Отозвать права сотрудника";
        }
        tr +=
            "<form id='"+i+"' method='post' action='"+context+"/changeActivityStatus'><input form='"+i+"' type='hidden' name='user_id' id='user_id"+i+"' required='required' readonly='readonly' value='"+
            users[i].userId +
            "'/>" + "<input form='"+i+"' type='hidden' name='_csrf' value='"+document.getElementById("csrf").innerText+"'/>" +
            "</form>" +
            "<tr><td>"+i+"</td>" +
            "<td>" +
            users[i].login+
            "</td>" +
            "<td>" +
            users[i].email+
            "</td>" +
            "<td style='color: "+roleColor+"'>" +
            users[i].role.roleType+
            "</td>"+
            "<td><div style='color: "+color+"' >" +
            status+
            "</div></td>"+
            "<td><button form='"+i+"' id='changeActivityStatusButton"+i+"' type='submit' class='btn btn-primary btn-sm' style='background-color: "+color+"; border: "+color+"'><i class='fa fa-user'></i>| "+buttonText+
            "</button></td>"+
            "<form id='role"+i+"' method='post' action='"+context+"/changeUserAuthority'><input form='role"+i+"' type='hidden' name='user_id' id='user_id"+i+"' required='required' readonly='readonly' value='"+
            users[i].userId +
            "'/>" + "<input form='role"+i+"' type='hidden' name='_csrf' value='"+document.getElementById("csrf").innerText+"'/>" +
            "</form><td><button form='role"+i+"' id='changeEmployeeAuthorityButton"+i+"' type='submit' class='btn btn-primary btn-sm' style='background-color: "+roleColor+"; border: "+roleColor+"'><i class='fa fa-key'></i>| "+roleButtonText+
            "</button></td></tr>";
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
    return Math.ceil(users.length / records_per_page);
}

window.onload = function() {
    let error = document.getElementById("error").innerText;
    if(error != "") {
        alert(error);
    }
    changePage(1);
};