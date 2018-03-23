kitchenPageUrl = "http://localhost:4567/staff/kitchen.html"; // TODO change to arms for deployment!!!

/**
 * Adds an EventListener waiting for push notifications.
 */
self.addEventListener('push', (event) => {
  const data = event.data.json();
  const pages = clients.matchAll(
      {type: 'window', includeUncontrolled: true}).then((windowClients) => {

    windowClients.forEach((windowClient) => {
      if (windowClient.url === kitchenPageUrl) {
        windowClient.postMessage(data);
      }

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
