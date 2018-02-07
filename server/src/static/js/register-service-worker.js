/**
 * Credit for this code to Matt Gaunt from Google
 */

/**
 * Verify if the browser supports service workers and Push.
 * @return {boolean} true if the browser supports it, false otherwise.
 */
function verifyBrowserSupport() {
  if (!('serviceWorker' in navigator)) {
    // not supported, we can't do anything so..
    return Boolean(false);
  }
  if (!('PushManager' in window)) {
    // again not supported, we can't do anything so..
    return Boolean(false);
  }
  if (!('Notification' in window)) {
    // can't show notifications
  }
  return Boolean(true);
}

/**
 * Takes a url of a js file that contains the worker code and registers it as a ServiceWorker.
 * Console logs the result.
 * @param worker
 * @return {Promise<ServiceWorkerRegistration>} the registration with which the service worker is accessed.
 */
function registerServiceWorker(worker) {
  return navigator.serviceWorker.register(worker)
  .then(function (registration) {
    console.log("Registration successful");
    return registration;})
  .catch(function (reason) {
    console.error("Unable to register: ", reason);
  });
}

/**
 * Asks for permission to display notifications to the user.
 * credit Matt Gaunt, Google.
 * @return {Promise<any>}
 */
function askPermission() {
  return new Promise(function(resolve, reject) {
    const permissionResult = Notification.requestPermission(function(result) {
      resolve(result);
    });

    if (permissionResult) {
      permissionResult.then(resolve, reject);
    }
  })
  .then(function(permissionResult) {
    if (permissionResult !== 'granted') {
      throw new Error('We weren\'t granted permission.');
    }
  });
}

function doSomething() {
  if (Notification.permission === "granted") {
    var notification = new Notification("Hello World!");
  } else {
    var result = askPermission();
    if (result === "granted") {
      subscribeUserToPush();
    } else {
      // Do nothing right now permission was denied.
    }
  }
}

