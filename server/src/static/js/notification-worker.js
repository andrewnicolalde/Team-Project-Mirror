/**
 * Adds an EventListener waiting for push notifications.
 */
self.addEventListener('push', function (event) {
  const notify = self.registration.showNotification("Something");
  event.waitUntil(notify);
});
