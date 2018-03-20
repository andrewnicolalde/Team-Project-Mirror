/**
 * Adds an EventListener waiting for push notifications.
 */
self.addEventListener('push', function (event) {
  const data = event.data.json();
  const pages = clients.matchAll(
      {type: 'window', includeUncontrolled: true}).then((windowClients) => {

    windowClients.forEach((windowClient) => {
      windowClient.postMessage(data);

    });
    return self.registration.showNotification("There's a new order to cook!");

  });
  const promiseChain = Promise.all([
    pages
  ]);
  event.waitUntil(promiseChain);
});

self.addEventListener('activate', event => {
  clients.claim();
});

/**
 * Function to check if the page is in focus so we can just update it.
 * Credit: Matt Gaunt, Google https://developers.google.com/web/fundamentals/push-notifications/common-notification-patterns
 * @return {Promise<Response[]>}
 */
function isClientFocused() {
  return clients.matchAll({
    type: 'window',
    includeUncontrolled: true
  })
  .then((windowClients) => {
    let clientIsFocused = false;

    for (let i = 0; i < windowClients.length; i++) {
      const windowClient = windowClients[i];
      if (windowClient.focused) {
        clientIsFocused = true;
        break;
      }
    }
    console.log(clientIsFocused);
    return clientIsFocused;
  });
}