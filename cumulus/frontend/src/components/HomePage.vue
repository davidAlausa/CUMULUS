<script>
  export default {
    name: 'HomePage',
  };
</script>

<template>
  <div class="div-homepage">
    <div class="gradient-background">
      <div class="centered">

        <h1>CUMULUS</h1><br>

        <small>Dedicated To Making Moving Easy.</small>

        <div class="pt-5">
          <router-link to="signin"><input class="hvrbtn hp-login-btn btn" type="button" value="Login"></router-link>
            <router-link to="signup"><input class="hvrbtn hp-signup-btn btn" type="button" value="Sign Up"></router-link>
        </div>

        <div class="hp-greenuc-btns pb-5 fixed-bottom d-flex justify-content-center">
          <router-link to="test"><abbr title="Help Center"><span class="hvricn bi bi-question-circle-fill"></span></abbr></router-link>
          <router-link to="contactus"><abbr title="Contact Us"><span class="hvricn bi bi-envelope-fill"></span></abbr></router-link>
          <router-link to="test"><abbr title="Test Page"><span class="hvricn bi bi-gear-wide-connected"></span></abbr></router-link>
        </div>

      </div>
    </div>
  </div>
</template>

<style scoped>
  .div-homepage {
    background-color: #057DCD;
  }

  .gradient-background {
    background: linear-gradient(319deg,#0012e7,#009ee7,#753fd6,#590be7);
    background-size: 240% 240%;
    animation: gradient-animation 20s ease infinite;
  }

  @keyframes gradient-animation {
    0% {
      background-position: 0% 50%;
    }
    50% {
      background-position: 100% 50%;
    }
    100% {
      background-position: 0% 50%;
    }
  }

  .centered {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    height: 100vh;
  }

  h1 {
    font-size: 7em;
    color: #E8EEF1;
  }

  small {
    font-size: 1.5em;
    color: #43B0F1;
  }

  .hp-login-btn {
    background-color: #1E3D58;
    color: #E8EEF1;
    font-size: 1.25em;
    margin-right: 10px;

  }

  .hp-signup-btn {
    background-color: #43B0F1;
    color: #1E3D58;
    font-size: 1.25em;
    margin-left: 10px;
  }

  .bi {
    color: #1E3D58;
    font-size: 3em;
    margin: 100px;
  }

</style>