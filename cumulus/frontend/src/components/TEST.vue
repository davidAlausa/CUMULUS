<script>
import HomePage  from "@/components/HomePage.vue";
export default {
  name: "TEST",
  components: {
    HomePage
  },
  data() {
    return {
      monkeyImage: null, // The image URL will be stored here
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
</body>
</template>

<style scoped>

</style>