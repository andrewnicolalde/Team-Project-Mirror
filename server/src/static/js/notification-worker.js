/**
 * Adds an EventListener waiting for push notifications.
 */
self.addEventListener('push', function (event) {
  if (event.data) {
    console.log(event.data.text());
  }
  const notify = self.registration.showNotification("Something");
  event.waitUntil(notify);
});
