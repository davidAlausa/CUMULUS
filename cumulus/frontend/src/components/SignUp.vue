<script>
export default {
  name: 'SignUp',
  data() {
    return {
      firstName: '',
      surname: '',
      email: '',
      phoneNumber: '',
      password: '',
      retypePassword: '',
      country: '',
      usage: '',
      countries: [],
      usages: ['Business Cloud Migrations', 'A Project', 'I Don\'t Know Yet...'],
      signUpView: 'signup',
    };
  },
  mounted() {
    fetch('https://restcountries.com/v3.1/all')
        .then(response => response.json())
        .then(data => {
          this.countries = data.map(country => country.name.common).sort();
        })
        .catch(error => {
          console.error('Error fetching countries:', error);
        });
  },
  methods: {
    submitForm() {
      const formdata ={
        firstName: this.firstName,
        surname: this.surname,
        email: this.email,
        phoneNumber: this.phoneNumber,
        password: this.password,
        retypePassword: this.retypePassword,
        country: this.country,
        usage: this.usage,
      };


      fetch('http://localhost:8080/api/signup', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formdata),
        credentials: 'include',
      })
          .then(response => response.json())
          .then(data => {
            console.log(data.message);
            this.signUpView='signup-success';
          })
          .catch(error => {
            console.error('Error submitting form:', error);
            this.signUpView='signup-failure';
          });
    },
  },
};
</script>

<template>
  <div class="container-fluid" v-if="signUpView==='signup'">
    <div class=" row">
      <div class="col-3">
        <router-link to="/"><abbr title="Go Back"><span class="hvricn bi bi-arrow-left-short"></span></abbr></router-link>
      </div>
      <div class="col-9"></div>
    </div>

    <div class="centered">
      <h1>Sign Up</h1>
      <form @submit.prevent="submitForm">
        <div class="form-row">
          <div class="form-column">
            <label for="firstName" class="form-label">First Name</label>
            <input class="form-control" type="text" id="firstName" placeholder="John" v-model="firstName" required />

            <label for="email" class="form-label">Email</label>
            <input class="form-control" type="email" id="email" placeholder="example@email.com" v-model="email" required />

            <label for="password" class="form-label">Password</label>
            <input class="form-control" type="password" id="password" placeholder="Enter Password Here" v-model="password" required />

            <label for="country" class="form-label">Country of Residence</label>
            <select class="form-select" id="country" v-model="country" required>
              <option v-for="country in countries" :key="country" :value="country">{{ country }}</option>
            </select>
          </div>
          <div class="form-column">
            <label for="surname" class="form-label">Surname</label>
            <input class="form-control" type="text" id="surname" placeholder="Doe" v-model="surname" required />

            <label for="phoneNumber" class="form-label">Phone Number</label>
            <input  class="form-control" type="number" id="phoneNumber" placeholder="Enter Number Here" v-model="phoneNumber" max="9999999999" required />

            <label for="retypePassword" class="form-label">Retype Password</label>
            <input  class="form-control" type="password" id="retypePassword" placeholder="Retype Password Here" v-model="retypePassword" required />

            <label for="usage" class="form-label">What Do You Plan To Use This Website For?</label>
            <select class="form-select" id="usage" v-model="usage" required>
              <option v-for="usage in usages" :key="usage" :value="usage">{{ usage }}</option>
            </select>
          </div>
        </div>
        <div class="su-submit-btn">
          <button class="btn btn-outline-primary" type="submit"><strong>Submit</strong></button>
        </div>
      </form>
    </div>
  </div>

  <div class="container-fluid" v-if="signUpView==='signup-success'">
    <div class=" row">
      <div class="col-3">
        <router-link to="/"><abbr title="Go Back"><span class="hvricn bi bi-arrow-left-short"></span></abbr></router-link>
      </div>
      <div class="col-9"></div>
    </div>
    <div class="centered">
      <span class="hvricn bi bi-person-fill-check"></span>
      <h1 class="su-success-h1">User was successfully created. Please log in.</h1>
    </div>
  </div>

  <div class="container-fluid" v-if="signUpView==='signup-failure'">
    <div class=" row">
      <div class="col-3">
        <router-link to="/"><abbr title="Go Back"><span class="hvricn bi bi-arrow-left-short"></span></abbr></router-link>
      </div>
      <div class="col-9"></div>
    </div>
    <div class="centered">
      <span class="hvricn bi bi-person-fill-x"></span>
      <h1 class="su-success-h1">There was an error creating this account</h1>
      <small>Please try again later or contact us for help.</small>
    </div>
  </div>
</template>

<style scoped>
.container-fluid {
  background-color: #43B0F1;
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
h1 {
  font-size: 7em;
  color: #e8eef1;
}
.bi {
  color: #1e1f22;
}
.hvricn:hover {
  color: #E8EEF1;
}
form {
  width: 1500px;
  max-width: 100%;
  padding: 20px;
}
.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 40px;
  width: 100%;
}
.form-column {
  flex: 1;
  margin-right: 20px;
}
label {
  display: block;
  margin-top: 10px;
  color: #1E3D58;
}
input, select {
  width: 100%;
  padding: 10px;
  margin-top: 10px;
  box-sizing: border-box;
}
button {
  margin-top: 50px;
  padding: 10px 20px;
}
.su-submit-btn{
  display: flex;
  justify-content: center;
}
.bi-person-fill-check {
  color: seagreen;
}

.su-success-h1 {
  font-size: 3em;
  font-weight: bolder;
  color: #1e3d58;
}

small {
  letter-spacing: 0;
}
</style>