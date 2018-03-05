/**
 * Credit for much code to Matt Gaunt from Google. https://developers.google.com/web/fundamentals/push-notifications/
 * Individual methods have the attribution. Some are modified.
 */

$(document).ready(function () {
  if (browserSupportsPush()) {
    // add a button users can click to get push notifications.
    if (!havePermissions()) {
      var button = "<button id='notify' class='btn' onclick='getPermissionAndSubscribe()'>Notifications</button>";
      $('.nav').append(button);
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
 * credit Matt Gaunt, Google. https://developers.google.com/web/fundamentals/push-notifications/
 * @return {Promise<any>} A Promise that resolves to indicate the users response to asking permission.
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
 * Wrapper method that checks for existing permissions. If there are none it asks for permission
 * and then subscribes the user to push and sends the subscription to the backend.
 */
function getPermissionAndSubscribe() {
  if (!(Notification.permission === "granted")) {
    // Async checking of permission. wait for result.
    askPermission().then(function (result) {
      // if it is a success then we subscribe the user to push.
      $('#notify').remove();
      subscribeUserToPush().then(function (subscription) { return sendSubscriptionToBackEnd(subscription)});
    }, function (err) {
      // if it fails we log the error.
      console.error(err);
    });
  }
}

/**
 * Subscribes the client to a push service. Uses our VAPID public key.
 * Credit Matt Gaunt, Google. https://developers.google.com/web/fundamentals/push-notifications/
 * @return {Promise<ServiceWorkerRegistration>}
 */
function subscribeUserToPush() {
  return navigator.serviceWorker.register('/js/notification-worker.js')
  .then(function(registration) {
    var serverKey = urlB64ToUint8Array('BIz9luhpKgx76RcIhqU4fmdIC1ve7fT5gm2Y632w_lsd_od2B87XschASGbi7EfgTIWpBAPKh2IWTOMt1Gux7tA');
    const subscribeOptions = {
      userVisibleOnly: true,
      applicationServerKey: serverKey
    };

    return registration.pushManager.subscribe(subscribeOptions);
  }).then(function(pushSubscription) {
    console.log('success');
    return pushSubscription;
  });
}

/**
 * Send the subscription object to the server so we can issue push notifications.
 * Credit Matt Gaunt, Google. https://developers.google.com/web/fundamentals/push-notifications/
 * @param subscription The PushSubscription object.
 */
function sendSubscriptionToBackEnd(subscription) {
  var pubKey = subscription.getKey('p256dh');
  var auth = subscription.getKey('auth');
  post('/api/saveSubscription', JSON.stringify({
      endpoint: subscription.endpoint,
      expirationTime: subscription.expirationTime,
      publicKey: btoa(String.fromCharCode.apply(null, new Uint8Array(pubKey))),
      auth: btoa(String.fromCharCode.apply(null, new Uint8Array(auth)))
    }), function (data) {
      console.log(data);
  });
}

/*
*  Function: urlB64ToUint8Array.
*  Credit: Matt Gaunt https://github.com/GoogleChromeLabs/web-push-codelab/blob/master/app/scripts/main.js
*  Push Notifications codelab
*  Copyright 2015 Google Inc. All rights reserved.
*
*  Being used under the Apache License, Version 2.0
*
*      https://www.apache.org/licenses/LICENSE-2.0
*/
/**
 * Utility function to send the VAPID key.
 * Modified to change let to var in for loop due to intellij complaining. Roger Milroy.
 * @param base64String
 * @return {Uint8Array}
 */
function urlB64ToUint8Array(base64String) {
  const padding = '='.repeat((4 - base64String.length % 4) % 4);
  const base64 = (base64String + padding)
  .replace(/\-/g, '+')
  .replace(/_/g, '/');

  const rawData = window.atob(base64);
  const outputArray = new Uint8Array(rawData.length);

  for (var i = 0; i < rawData.length; ++i) {
    outputArray[i] = rawData.charCodeAt(i);
  }
  return outputArray;
}