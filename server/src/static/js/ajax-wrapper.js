/**
 * @module AJAX_Wrapper
 */

/**
 * This function wraps a get request and allows unauthorized requests to be
 * redirected.
 *
 * @param url The url that the request originates from
 * @param callback If the function is successful or not
 */
function get(url, callback) {
  $.ajax({
    type: "GET",
    url: url,
    success: callback,
    error: (jqXHR, textStatus, errorThrown) => {
      if (textStatus === "error") {
        if (errorThrown === "Unauthorized") {
          // 401 error, redirect to 401 page.
          window.location.replace("/401.html");
        }
      }
    }
  });
}

/**
 * This function wraps a post request and allows unauthorized requests to be
 * redirected.
 *
 * @param url The url that the request is being sent from.
 * @param data The data that is in the post request.
 * @param callback If the request is successful or not.
 */
function post(url, data, callback) {
  $.ajax({
    type: "POST",
    url: url,
    data: data,
    success: callback,
    error: (jqXHR, textStatus, errorThrown) => {
      if (textStatus === "error") {
        if (errorThrown === "Unauthorized") {
          // 401 error, redirect to 401 page.
          window.location.replace("/401.html");
        }
      }
    }
  });
}