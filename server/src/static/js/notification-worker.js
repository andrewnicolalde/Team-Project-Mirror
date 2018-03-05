/**
 * Adds an EventListener waiting for push notifications.
 */
self.addEventListener('push', function (event) {
  if (event.data) {
    console.log(event.data.text());
  }
  const notify = self.registration.showNotification(event.data.text());
  event.waitUntil(notify);
});
