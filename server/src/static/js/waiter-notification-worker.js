waiterPageUrl = "http://localhost:4567/staff/waiter.html";

self.addEventListener('push', (event) => {
  const notify = self.registration.showNotification("Got a notification",
      event.data);
  event.waitUntil(notify);
});
