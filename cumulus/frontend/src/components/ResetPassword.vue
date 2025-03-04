<script>
export default {
  name: 'ResetPassword',
  data() {
    return {
      email: '',
      view: 'reset-password-form',
    };
  },
  methods: {
    submitForm() {
      const formdata = {
        email: this.email,
      };

      fetch('http://localhost:8080/api/resetpassword/sendcode', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formdata),
      })
          .then(response => response.json())
          .then(data => {
            console.log(data.message);
            this.view='reset-password-codesent';
          })
          .catch(error => {
            console.error('Error submitting form:', error);
          });
    },
  }
};
</script>

<template>
  <div class="div-resetpassword">
    <div class="go-back-div row">
      <div class="col-3">
        <router-link to="/signin"><abbr title="Go Back"><span class="hvricn bi bi-arrow-left-short"></span></abbr></router-link>
      </div>
      <div class="col-9"></div>
    </div>

    <div class="centered row">
      <h1 class="si-title">Reset Password</h1>
      <div v-if="view==='reset-password-form'" class="form-container col-lg-6">
        <form>
          <label for="email" class="form-label">Email</label>
          <input id="email" type="email" class="form-control form-control-lg" required>
          <div class="pt-3">
            <button class="hvrbtn btn si-submit-btn" type="submit"><strong>Send Code</strong></button>
          </div>
        </form>
      </div>
      <div v-if="view==='reset-password-sentcode'">
        <form>
          <label for="code" class="form-label">Enter Code Sent To Email</label>
          <input id="code" type="number" class="form-control form-control-lg" required>
          <div class="pt-3">
            <button class="hvrbtn btn si-submit-btn" type="submit"><strong>Enter Code</strong></button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<style scoped>
.div-resetpassword {
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
</style>