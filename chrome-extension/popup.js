function generateUUID() {
  return crypto.randomUUID();
}

const toggleBtn = document.getElementById('toggleDarkMode');
const generateBtn = document.getElementById("generateBtn");

if (toggleBtn) {
  toggleBtn.addEventListener('click', function () {
    document.body.classList.toggle('dark');
    const isDark = document.body.classList.contains('dark');
    toggleBtn.textContent = isDark ? '☀️' : '🌙';
    localStorage.setItem('theme', isDark ? 'dark' : 'light');
  });
}

window.addEventListener('DOMContentLoaded', () => {
  if (localStorage.getItem('theme') === 'dark') {
    document.body.classList.add('dark');
    if (toggleBtn) {
      toggleBtn.textContent = '☀️';
    }
  }

  chrome.runtime.sendMessage({ action: "getSelectedText" }, (response) => {
    if (response && response.selectedText) {
      const promptInput = document.getElementById("userPrompt");
      if (promptInput) {
        promptInput.value = response.selectedText.trim();
      }
    }
  });
});

chrome.storage.local.get("userId", (result) => {
  if (!result.userId) {
    const userId = generateUUID();
    chrome.storage.local.set({ userId }, () => {
      console.log("Generated and saved new userId:", userId);
    });
  } else {
    console.log("Existing userId found:", result.userId);
  }
});

if (generateBtn) {
  generateBtn.addEventListener("click", async () => {
    const userPrompt = document.getElementById("userPrompt").value;
    const resumeUpload = document.getElementById("resumeUpload").files[0];

    chrome.storage.local.get("userId", async (data) => {
      const userId = data.userId;
      if (!userId) {
        alert("User ID not found. Please refresh and try again.");
        return;
      }

      try {
        const formData = new FormData();
        formData.append("userId", userId);
        formData.append("metaData", userPrompt);
        if (resumeUpload) {
          formData.append("resumeFile", resumeUpload);
        }

        generateBtn.disabled = true;
        generateBtn.innerText = "Generating...";

        const response = await fetch("https://prowrite-backend.onrender.com/api/text/generate", {
          method: "POST",
          body: formData,
        });

        generateBtn.disabled = false;
        generateBtn.innerText = "Generate";

        if (response.ok) {
          const resultText = await response.text();
          document.getElementById("responseArea").innerText = resultText;
        } else {
          console.error("Request failed:", response.status);
          document.getElementById("responseArea").innerText = "An error occurred while generating the reply.";
        }
      } catch (error) {
        console.error("Error:", error);
        document.getElementById("responseArea").innerText = "An error occurred while generating the reply.";
      }
    });
  });
}
