let nome = document.getElementById("nome");
let senha = document.getElementById("senha");
let botao = document.getElementById("botao");

const URL = "http://localhost:8080/user"

function sendUser() {

    // Create a data object with user information
    const userData = {
        "username": nome.value,
        "password": senha.value
    };

    // Make a POST request to the specified URL with user data
    fetch(URL , {
        method: "POST",
        headers: {
            "Content-Type": "application/json"  // Specify the content type as JSON
            // Add any other headers if needed
        },
        body: JSON.stringify(userData)
    })
    .then(response => {
        // Handle the response, e.g., check for success or handle errors
        if (response.ok) {
            console.log("User data sent successfully");
        } else {
            console.error("Failed to send user data");
        }
    })
    .catch(error => {
        console.error("Error:", error);
    });
}

// Attach the sendUser function to the button click event
botao.addEventListener("click", sendUser);
