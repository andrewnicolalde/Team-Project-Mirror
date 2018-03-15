/**
 * Credit for much code to Matt Gaunt from Google. https://developers.google.com/web/fundamentals/push-notifications/
 * Individual methods have the attribution. Some are modified.
 */

document.addEventListener('DOMContentLoaded', function (event) {
  if (browserSupportsPush()) {
    // add a button users can click to get push notifications.
    if (!havePermissions()) {
      const button = "<button id='notify' class='btn' onclick='getPermissionAndSubscribe()'>Notifications</button>";
      $('.nav').append(button);
    } else {
      setUpPush();
    }
  }
  navigator.serviceWorker.addEventListener('message', (event) => {
    displayOrders(JSON.stringify(event.data));
  });
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
  return registration.pushManager.getSubscription();
}

/**
 * Takes a url of a js file that contains the worker code and registers it as a ServiceWorker.
 * Console logs the result.
 * @param worker The path of the js file which contains the worker.
 * @return {Promise<ServiceWorkerRegistration>} the registration with which the service worker is accessed.
 */
function registerServiceWorker(worker) {
  return navigator.serviceWorker.register(worker)
  .then((registration) => {
    console.log("Registration successful");
    console.log(registration.scope);
    return registration;
  })
  .catch((reason) => {
    console.error("Unable to register: ", reason);
  });
}

/**
 * Wrapper method that asks for permission, subscribes the user to push,
 * then sends the subscription to the backend.
 */
function getPermissionAndSubscribe() {
  // Async checking of permission. wait for result.
  Notification.requestPermission().then((result) => {
    // if it is a success then we subscribe the user to push.
    if (result === 'granted') {
      $('#notify').remove();
      // the main part of registering, subscribing and pushing. All async meaning thens are needed.
      registerServiceWorker('/js/kitchen-notification-worker.js').then(
          (registration) => {
            console.log("Service worker registered.");
            subscribeUserToPush(registration)
          });
    } else {
      console.log("We were rejected.. :(");
    }
  }).catch((reason) => {
    console.error(reason);
  });
}

/**
 * Wrapper method to initialise the push notifications.
 */
function setUpPush() {
  navigator.serviceWorker.getRegistration('/js/')
  .then((registration) => {
    if (registration !== undefined) {
      console.log("Have a service worker");
      // we have a service worker already.
      getCurrentSubscription(registration)
      .then((subscription) => {
        console.log('Already subscribed');
        // do nothing, we have already registered and sent to the back end.
      }).catch((reason) => {
        // log the reason for debugging
        console.error(reason);
        // should be because we don't have a subscription so we subscribe and send it to the server.
        subscribeUserToPush(registration);
      });

    } else {
      console.log("No service worker.");
      registerServiceWorker('/js/kitchen-notification-worker.js')
      .then((registration) => {
        subscribeUserToPush(registration);
      })
    }
  });
}

/**
 * Subscribes the client to a push service. Uses our VAPID public key.
 * Credit Matt Gaunt, Google. https://developers.google.com/web/fundamentals/push-notifications/
 * Modified.
 */
function subscribeUserToPush(registration) {

  const serverKey = urlB64ToUint8Array(
      'BIz9luhpKgx76RcIhqU4fmdIC1ve7fT5gm2Y632w_lsd_od2B87XschASGbi7EfgTIWpBAPKh2IWTOMt1Gux7tA');
  const subscribeOptions = {
    userVisibleOnly: true,
    applicationServerKey: serverKey
  };
    registration.pushManager.subscribe(subscribeOptions)
    .then((pushSubscription) => {
      sendSubscriptionToBackEnd(pushSubscription);
    });
}

/**
 * Send the subscription object to the server so we can issue push notifications.
 * Credit Matt Gaunt, Google. https://developers.google.com/web/fundamentals/push-notifications/
 * @param subscription The PushSubscription object.
 */
function sendSubscriptionToBackEnd(subscription) {
  const pubKey = subscription.getKey('p256dh');
  const auth = subscription.getKey('auth');
  console.log('sending to backend');
  post('/api/saveSubscription', JSON.stringify({
    endpoint: subscription.endpoint,
    expirationTime: subscription.expirationTime,
    publicKey: btoa(String.fromCharCode.apply(null, new Uint8Array(pubKey))),
    auth: btoa(String.fromCharCode.apply(null, new Uint8Array(auth)))
  }), (data) => {
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

  for (let i = 0; i < rawData.length; ++i) {
    outputArray[i] = rawData.charCodeAt(i);
  }
  return outputArray;
}