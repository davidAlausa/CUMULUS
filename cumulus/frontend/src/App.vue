<template>
  <!DOCTYPE html>
  <html lang="en" xmlns:th="http://www.thymeleaf.org">
  <head>
    <meta charset="UTF-8">
    <title>Welcome!</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css">
  </head>

  <transition name="fade" mode="out-in">
  <router-view/>
  </transition>

  </html>
</template>

<script>
  import HomePage from "@/components/HomePage.vue";
  import TEST from "@/components/TEST.vue";
  export default {
    name: "App",
    components: {
      HomePage,
      TEST
    },
  };
</script>

<style>

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}


  h1 {
    font-family: "Roboto Light", Helvetica, sans-serif;
  }
  small {
    font-family: "Open Sans", Helvetica, sans-serif;
    letter-spacing: .75em;
  }
  input.btn {
    border-radius: 10px;
  }

  .hvrbtn:hover {
    background-color: #E8EEF1;
    color: #1E3D58;
  }
  .hvricn:hover {
    color: #E8EEF1;
  }
</style>
