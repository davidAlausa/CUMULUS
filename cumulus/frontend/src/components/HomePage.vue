<script>
  export default {
    name: 'HomePage',
  };
</script>

<template>
  <body>
    <div class="centered">

      <h1>CUMULUS</h1><br>

      <small>Dedicated to making moving easy.</small>

      <div class="pt-5">
        <router-link to="test"><input class="hvrbtn hp-login-btn btn" type="button" value="Login"></router-link>
          <router-link to="test"><input class="hvrbtn hp-signup-btn btn" type="button" value="Sign Up"></router-link>
      </div>

      <div class="hp-greenuc-btns pb-5 fixed-bottom d-flex justify-content-center">
        <router-link to="test"><span class="hvricn bi bi-question-circle-fill"></span></router-link>
        <router-link to="test"><span class="hvricn bi bi-envelope-fill"></span></router-link>>
      </div>

    </div>
  </body>
</template>

<style scoped>
  body {
    background-color: #057DCD;
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