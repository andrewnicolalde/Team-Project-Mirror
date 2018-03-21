waiterPageUrl = "http://localhost:4567/staff/waiter.html";

self.addEventListener('push', (event) => {
  const notify = self.registration.showNotification(event.data.text());
  const tellPage = clients.matchAll(
      {type: 'window', includeUncontrolled: true}).then((windowClients) => {

    windowClients.forEach((windowClient) => {
      if (windowClient.url === waiterPageUrl) {
        windowClient.postMessage("update");
      }
    });
  });
  const promiseChain = Promise.all([
    notify,
    tellPage
  ]);
  event.waitUntil(promiseChain);
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