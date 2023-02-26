$('#sendCodeToEmailForm').submit(function(event) {
    event.preventDefault();
    $.ajax({
        url: $(this).attr('action'),
        type: 'get',
        dataType: "text",
        cache: "false",
        async: true,
        data:$('#sendCodeToEmailForm').serialize(),
        success: function() {
            alert("Сообщение с кодом было доставлено на введённую почту");
            $('#restorePasswordForm').find('#code').prop('disabled',false);
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert("Нет пользователя с такой почтой!");
        },
    });

});