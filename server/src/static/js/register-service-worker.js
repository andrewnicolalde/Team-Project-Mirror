/**
 * Credit for this code to Matt Gaunt from Google
 */

/**
 * Verify if the browser supports service workers and Push.
 * @return {boolean} true if the browser supports it, false otherwise.
 */
function verifyBrowserSupport() {
  if (!('serviceWorker' in navigator)) {
    // not supported, we can't do anything so..
    return Boolean(false);
  }
  if (!('PushManager' in window)){
    // again not supported, we can't do anything so..
    return Boolean(false);
  }
  return Boolean(true);
}

