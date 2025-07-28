chrome.runtime.onMessage.addListener((request, sender, sendResponse) => {
  if (request.action === "getSelectedText") {
    chrome.tabs.query({ active: true, currentWindow: true }, (tabs) => {
      chrome.tabs.executeScript(
        tabs[0].id,
        {
          code: "window.getSelection().toString();"
        },
        (results) => {
          if (chrome.runtime.lastError || !results || results.length === 0) {
            sendResponse({ selectedText: "" });
          } else {
            sendResponse({ selectedText: results[0] });
          }
        }
      );
    });
    return true; 
  }
});
