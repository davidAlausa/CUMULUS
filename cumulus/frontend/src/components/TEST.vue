<script>

export default {
  name: "TEST",
  components: {
  },
  data() {
    return {
      monkeyImage: null, // The image URL will be stored here
      gptResponse: "", // The GPT response will be stored here
    };
  },
  methods: {
    async fetchMonkeyImage() {
      try {
        const response = await fetch("http://localhost:8080/api/test/getrequest");
        if (!response.ok) {
          throw new Error("Failed to fetch image");
        }
        const text = await response.text();
        this.monkeyImage = text;
      } catch (error) {
        console.error("Error fetching monkey image:", error);
      }
    },
    async chatGPTPrompt() {
      const input = document.getElementById("input").value;

      try {
        const response = await fetch("http://localhost:8080/api/test/getgptresponse", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            model: "gpt-3.5-turbo",  // Match what Spring Boot expects
            messages: [
              { role: "user", content: input }
            ]
          }),
        });

        if (!response.ok) {
          throw new Error("Failed to fetch GPT response");
        }

        const data = await response.json(); // Read response as JSON
        this.gptResponse = data.choices[0].message.content; // Extract GPT response
      } catch (error) {
        console.error("Error sending chatGPT prompt:", error);
      }
    }
  },
};
</script>

<template>
<body>
  <h1> THIS IS A TEST PAGE.</h1>
  <small> Nothing to see here</small>
  <router-link to="/">Go to Home Page</router-link>

  <br><hr><br>

  <h2>TestingGetRequestFromBackend</h2>
  <button @click="fetchMonkeyImage">Fetch Monkey Image</button>

  <!-- Display the image if fetched -->
  <div v-if="monkeyImage">
    <h2>Here's your monkey!</h2>
    <img :src="monkeyImage" alt="Monkey Image" style="width: 10%"/>
  </div>

  <br><hr><br>

  <h2>TestingChatGPTResponseFromBackend</h2>
  <form @submit.prevent="chatGPTPrompt">
    <label for="input">Enter Input Here: </label>
    <input type="text" id="input" name="input" required>
    <button type="submit">Send</button>
  </form>
  <textarea v-model="gptResponse"></textarea>
</body>
</template>

<style scoped>

</style>