/**
 * Adds an EventListener waiting for push notifications.
 */
self.addEventListener('push', function (event) {
  if (event.data) {
    console.log(event.data.json());
  }
  const notify = self.registration.showNotification(event);
  event.waitUntil(notify);
});
