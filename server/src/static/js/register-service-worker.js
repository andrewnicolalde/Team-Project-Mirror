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

/**
 * Check permissions and show a test notification.
 */
function doSomething() {
  if (Notification.permission === "granted") {
    var notification = new Notification("Hello World!");
  } else {
    var result = askPermission();
    if (result === "granted") {
      // nothing right now.
    } else {
      // Do nothing right now permission was denied.
    }
  }
}

/**
 * Subscribes the client to a push service. Uses our VAPID public key.
 * Credit Matt Gaunt, Google.
 * @return {Promise<ServiceWorkerRegistration>}
 */
function subscribeUserToPush() {
  return navigator.serviceWorker.register('/js/notification-worker.js')
  .then(function(registration) {
    var enc = new TextEncoder("utf-8");
    const subscribeOptions = {
      userVisibleOnly: true,
      applicationServerKey: enc.encode(
          'BE4fhya6e_JedY-5R7cRMXj973kghfY3YTk0Bzi8AWXGc8f1JHD3GBmWsg1J8DvMW3uLG5nf6ycDjbFO0n5u7n4'
      )
    };

    return registration.pushManager.subscribe(subscribeOptions);
  })
  .then(function(pushSubscription) {
    console.log('Received PushSubscription: ', JSON.stringify(pushSubscription));
    return pushSubscription;
  });
}

/**
 * Send the subscription object to the server so we can issue push notifications.
 * Credit Matt Gaunt, Google.
 * @param subscription
 * @return {Promise<Response>}
 */
function sendSubscriptionToBackEnd(subscription) {
  return fetch('/api/save-subscription/', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(subscription)
  })
  .then(function(response) {
    if (!response.ok) {
      throw new Error('Bad status code from server.');
    }

    return response.json();
  })
  .then(function(responseData) {
    if (!(responseData.data && responseData.data.success)) {
      throw new Error('Bad response from server.');
    }
  });
}