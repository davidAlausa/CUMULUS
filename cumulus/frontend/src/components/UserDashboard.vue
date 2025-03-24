<script>
import {fetchWithAuth} from '../utils/api';

export default {
  name: 'UserDashboard',
  data() {
    return {
      userFirstName: '',
    };
  },
  async mounted() {
    try {
      const response = await fetchWithAuth('http://localhost:8080/api/loggedin/user');
      const data = await response.json();
      this.userFirstName = data.firstName;
    } catch (error) {
      console.error('Error fetching user:', error);
    }
  }
};

</script>

<template>
  <div class="div-userdashboard">

    <router-link to="assess-workstream">
      <div class="side-div left-div">
        <span class="arrow bi bi-caret-left-fill"></span>
        <h2>Assess</h2>
        <p>Evaluate a new workstream based on your preferences and visualise a plan on how to make your project come to life!</p>
      </div>
    </router-link>

    <div class="side-div right-div">
      <span class="arrow bi bi-caret-right-fill"></span>
      <h2>Compare</h2>
      <p>Compare between two and three workstreams of the same type to gain insight and identify the most suitable option for your project!</p>
    </div>

    <div class="dashboard-content">
      <h1 class="ud-welcomebackmsg">
        <strong>{{ 'Welcome Back ' + userFirstName }}</strong>
      </h1>
    </div>
  </div>
</template>

<style scoped>
.div-userdashboard {
  background-color: #DBEBED;
  padding: 20px;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
}

.side-div {
  width: 5em;
  height: 100vh;
  position: absolute;
  top: 0;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: #10564F;
  transition: width 0.5s ease-in-out, background-color 0.5s ease-in-out;
  padding: 10px;
  box-sizing: border-box;
  overflow: hidden;
}


.left-div {
  left: 0;
  border-right: 5px solid #ccc;
}

.right-div {
  right: 0;
  border-left: 5px solid #ccc;
}


.side-div:hover {
  width: 20em;
  color: #F6D4D2;
}
.left-div:hover {
  background-color: #E43D40;
}
.right-div:hover {
  background-color: #4120A9;
}

.side-div p , .side-div h2  {
  display: none;
  text-align: center;
}

.side-div:hover p , .side-div:hover h2 {
  display: block;
  animation: fadeIn 0.5s ease-in-out;
}

.dashboard-content {
  text-align: center;
  z-index: 2;
  width: 60%;
}

.ud-welcomebackmsg {
  font-size: 3em;
  color: #047076;
}

.arrow {
  font-size: 1.5em;
  color: white;
  transition: transform 0.5s ease-in-out;
  position: absolute;
  top: 50%;
}

.left-div .arrow {
  left: 10px;
}

.right-div .arrow {
  right: 10px;
}

.left-div:hover .arrow {
  transform: translateY(5em) rotate(180deg);
  transition: transform 0.5s ease-in-out;
  color: #F6D4D2;
}

.right-div:hover .arrow {
  transform: translateY(5em) rotate(180deg);
  transition: transform 0.5s ease-in-out;
  color: #F6D4D2;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}


</style>