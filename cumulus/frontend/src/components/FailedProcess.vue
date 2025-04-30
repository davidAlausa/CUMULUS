<script>
  export default {
    name: 'FailedProcess',
    data() {
      const querydata = this.$route.query.data ? JSON.parse(decodeURIComponent(this.$route.query.data)) : {};

      return {
        message: querydata.message || "",
      };
    },
    mounted() {
      console.log("Received Data:", this.message);
    }
  };
</script>

<template>
  <div class="container-fluid">
    <div class=" row">
      <div class="col-3">
        <router-link to="/dashboard"><abbr title="Go Back"><span class="hvricn bi bi-arrow-left-short"></span></abbr></router-link>
      </div>
      <div class="col-9"></div>
    </div>
    <div class="centered">
      <span class="hvricn bi bi-database-fill-exclamation"></span>
      <h1 class="su-success-h1">{{message}}</h1>
    </div>
  </div>
</template>

<style scoped>
.container-fluid {
  background-color: #DBEBED;
}
h1{
  font-size: 5em;
  color: #047076;
  text-align: center;
  margin-top: 1em;
}
.centered {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.row{
  height: 0;
}

.bi{
  color: #047076;
}
</style>