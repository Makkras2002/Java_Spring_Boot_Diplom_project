let checkbox = document.getElementById("needDelivery");
let btn = document.getElementById("deliveryAddress");

checkbox.addEventListener( 'change', function() {
    if(this.checked) {
        btn.disabled = false;
    } else {
        btn.disabled = true;
    }
});