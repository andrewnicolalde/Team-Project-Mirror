/**
 * Credit for this code to Matt Gaunt from Google
 */

$(document).ready(function () {
  if (browserSupportsPush()) {
    // add a button users can click to get push notifications.
    if (!havePermissions()) {
      var button = "<button onclick='doSomething()'>Notifications</button>";
      $('div').append(button);
    } else {
      // register service worker and check subscriptions. send to backend.
    }
  }
});

/**
 * Verify if the browser supports service workers and Push.
 * @return {boolean} true if the browser supports it, false otherwise.
 */
function browserSupportsPush() {
  if (!('serviceWorker' in navigator)) {
    // not supported, we can't do anything so..
    return Boolean(false);
  }
  if (!('PushManager' in window)) {
    // again not supported, we can't do anything so..
    return Boolean(false);
  }
  // TODO check if we need to show notifications or can we just change the page?
  if (!('Notification' in window)) {
    // check if there is another notification API.
    if (('showNotification' in ServiceWorkerRegistration.prototype)) {
      return Boolean(true);
    } else {
      // We definitely can't show Notifications.
      return Boolean(false);
    }
  }
  return Boolean(true);
}

/**
 * Checks if the page has permission to show notifications to the user. If false we cannot use push notifications.
 * @return {boolean} True if we can show notifications. False if not.
 */
function havePermissions() {
  return Boolean(Notification.permission === "granted");
}

/**
 * Gets the PushSubscription if it already exists. If not it returns null.
 * @param registration
 * @return {Promise<PushSubscription>} or null if there is no subscription.
 */
function getCurrentSubscription(registration) {
  registration.pushManager.getSubscription()
  .then(function (subscription) {
    if (!subscription) {
      return; // there isn't a subscription return to create a new one.
    }
    return subscription;
  }).catch(function (err) {
    console.error(err);
  });
}

/**
 * Takes a url of a js file that contains the worker code and registers it as a ServiceWorker.
 * Console logs the result.
 * @param worker The path of the js file which contains the worker.
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
 * TODO refactor to become permission asking wrapper leading to registering a service worker etc.
 */
function doSomething() {
  if (!(Notification.permission === "granted")) {
    var result = askPermission();
    if (result === "granted") {
      // nothing right now.
    } else {
      // return an error.
      return new Error();
    }
  } else {
    // Do nothing we want to register a worker either way.
  }
  var subscription = subscribeUserToPush();
  console.log(subscription);
  // sendSubscriptionToBackEnd(registration);
}

/**
 * Subscribes the client to a push service. Uses our VAPID public key.
 * Credit Matt Gaunt, Google.
 * @return {Promise<ServiceWorkerRegistration>}
 */
function subscribeUserToPush() {
  return navigator.serviceWorker.register('/js/notification-worker.js')
  .then(function(registration) {
    var serverKey = urlBase64ToUint8Array('BIz9luhpKgx76RcIhqU4fmdIC1ve7fT5gm2Y632w_lsd_od2B87XschASGbi7EfgTIWpBAPKh2IWTOMt1Gux7tA');
    const subscribeOptions = {
      userVisibleOnly: true,
      applicationServerKey: serverKey
    };

    return registration.pushManager.subscribe(subscribeOptions);
  }).then(function(pushSubscription) {
    console.log('Received PushSubscription: ', JSON.stringify(pushSubscription));
    return pushSubscription;
  });
}

/**
 * Send the subscription object to the server so we can issue push notifications.
 * Credit Matt Gaunt, Google.
 * @param subscription The PushSubscription object.
 * @return {Promise<Response>} The response from the server.
 */
function sendSubscriptionToBackEnd(subscription) {
  return fetch('/api/authStaff/save-subscription/', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(subscription)
  }).then(function(response) {
    if (!response.ok) {
      return Response.error();
    }

    return response.json();
  }).then(function(responseData) {
    if (!(responseData.data && responseData.data.success)) {
      return Response.error();
    }
  });
}

/**
 * Utility function to send the VAPID key.
 * Credit Malko https://gist.github.com/malko/ff77f0af005f684c44639e4061fa8019
 * @param base64String
 * @return {Uint8Array}
 */
function urlBase64ToUint8Array(base64String) {
  const padding = '='.repeat((4 - base64String.length % 4) % 4);
  const base64 = (base64String + padding)
      .replace(/\-/g, '+')
      .replace(/_/g, '/')
  ;
  const rawData = window.atob(base64);
  return Uint8Array.from([...rawData;
].
  map((char) = > char.charCodeAt(0);
))
}