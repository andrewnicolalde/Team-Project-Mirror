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
  event.waitUntil(notify);
});
