<script>
export default {
  name: 'UserDashboard',
  data() {
    return {
      userFirstName: '',
    };
  },
  mounted() {
    fetch('http://localhost:8080/loggedin/api/user', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
    })
        .then(response => response.json())
        .then(data => {
          console.log(data);
          this.userFirstName = data.firstName;
        })
        .catch(error => {
          console.error('Error fetching user:', error);
        });
  }
};

</script>

<template>
  <div class="div-userdashboard">
    <div class="centered row">
      <h1 class="ud-welcomebackmsg">{{ 'Welcome Back ' + userFirstName }}</h1></div>
  </div>
</template>

<style scoped>
.div-userdashboard {
  background-color: #DBEBED;
  padding: 20px;
}
.centered {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: flex-start;
  height: 96vh;
  padding: 10%;
}

.ud-welcomebackmsg {
  font-size: 4em;
  text-align: center;
  color: #047076;
  height: 0;
}
</style>