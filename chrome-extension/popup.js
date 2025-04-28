function generateUUID() {
    return crypto.randomUUID();
}

const toggleBtn = document.getElementById('toggleDarkMode');
const generateBtn = document.getElementById("generateBtn");

toggleBtn.addEventListener('click', function () {
  document.body.classList.toggle('dark');

  if (document.body.classList.contains('dark')) {
    toggleBtn.textContent = '☀️';
    localStorage.setItem('theme', 'dark');
  } else {
    toggleBtn.textContent = '🌙';
    localStorage.setItem('theme', 'light');
  }
});

window.addEventListener('DOMContentLoaded', () => {
  if (localStorage.getItem('theme') === 'dark') {
    document.body.classList.add('dark');
    toggleBtn.textContent = '☀️';
  } else {
    toggleBtn.textContent = '🌙';
  }
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

generateBtn.addEventListener("click", async () => {
    const userPrompt = document.getElementById("userPrompt").value;
    const resumeUpload = document.getElementById("resumeUpload").files[0];

    if (!userPrompt.trim()) {
        alert("Please enter some text.");
        return;
    }


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

            const response = await fetch("http://localhost:8085/api/text/generate", {
                method: "POST",
                body: formData,
            });

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
