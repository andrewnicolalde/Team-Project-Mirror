function get(url, callback) {
    $.ajax({
        type: "GET",
        url: url,
        success: callback,
        error: function (jqXHR, textStatus, errorThrown) {
            if (textStatus === "error") {
                if (errorThrown === "Unauthorized") {
                    // 401 error, redirect to 401 page.
                    window.location.replace("/401.html");
                }
            }
        }
    });
}

function post(url, data, callback) {
    $.ajax({
        type: "POST",
        url: url,
        data: data,
        success: callback,
        error: function (jqXHR, textStatus, errorThrown) {
            if (textStatus === "error") {
                if (errorThrown === "Unauthorized") {
                    // 401 error, redirect to 401 page.
                    window.location.replace("/401.html");
                }
            }
        }
    });
}