<script>
export default {
  name: 'SignIn',
  data() {
    return {
      email: '',
      password: '',
      errorCount: 0,
    };
  },
  methods: {
    async submitForm() {
      const formdata = {
        email: this.email,
        password: this.password,
      };

      try {
        const response = await fetch('http://localhost:8080/api/session/authenticate', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(formdata),
        });

        if (!response.ok) {
          const errorData = await response.json();
          this.errorCount += 1;
          throw new Error(errorData.message || 'Invalid email or password');
        }

        const data = await response.json();
        localStorage.setItem('accessToken', data.accessToken);
        localStorage.setItem('refreshToken', data.refreshToken);

        this.$router.push('/dashboard');
      } catch (error) {
        console.error('Error submitting form:', error);
      }
    },
  },
};
</script>

<template>
<div class="div-signin">
  <div class="go-back-div row">
    <div class="col-3">
      <router-link to="/"><abbr title="Go Back"><span class="hvricn bi bi-arrow-left-short"></span></abbr></router-link>
    </div>
    <div class="col-9"></div>
  </div>

  <div class="centered row">
    <h1 class="si-title">Login</h1>
    <div class="form-container col-lg-6">
      <form @submit.prevent="submitForm">
        <label for="email" class="form-label">Email</label>
        <input id="email" v-model="email" type="email" class="form-control form-control-lg" required>
        <label for="password" class="form-label">Password</label>
        <input id="password" v-model="password" type="password" class="form-control form-control-lg" required>
        <div class="pt-3">
          <button class="hvrbtn btn si-submit-btn" type="submit"><strong>Login</strong></button>
        </div>
      </form>
      <div class="forgot-password" v-if="errorCount>0">
        <p>These login details are not correct. Please try again or reset password.</p>
        <router-link to="reset-password"><strong class="si-reset-password">Forgot Password?</strong></router-link>
      </div>
    </div>
  </div>

</div>
</template>

<style scoped>
.div-signin {
  background-color: #1E3D58;
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
.si-title {
  font-size: 7em;
  text-align: center;
  color: #43B0F1;
  height: 0;
}
.go-back-div {
  height: 0;
}
.bi-arrow-left-short {
  color: #43B0F1;
}
form {
  width: 1500px;
  max-width: 100%;
  padding: 20px;
}
label {
  display: block;
  margin-top: 10px;
  color: #43B0F1;
}
.si-submit-btn {
  margin-top: 20px;
  padding: 10px 20px;
  color: #43B0F1;
  border-color: #43B0F1;
  width: 50%;
}
.si-submit-btn:hover {
  background-color: #43B0F1;
  color: #1E3D58;
}
.pt-3, .forgot-password{
  padding-top: 1rem;
  text-align: center;
}

input, .si-reset-password {
  color: #43B0F1;
}
p {
  color: darkorange;
}
</style>