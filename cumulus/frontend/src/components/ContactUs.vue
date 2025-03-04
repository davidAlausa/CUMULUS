<script>
export default {
  name: 'ContactUs',
  data() {
    return {
      topic: '',
      subject: '',
      email: '',
      message: '',
      topics: ['General Inquiry', 'Technical Support', 'Business Inquiry', 'Test (Admin Only)', 'Other'],
      contactUsView: 'contact-us-form',
    };
  },
  methods: {
    submitForm() {
      const formdata = {
        topic: this.topic,
        subject: this.subject,
        email: this.email,
        message: this.message,
      };

      fetch('http://localhost:8080/api/contactus/sendemail', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formdata),
      })
          .then(response => response.json())
          .then(data => {
            console.log(data.message);
            this.contactUsView = 'contact-us-success';
          })
          .catch(error => {
            this.contactUsView = 'contact-us-failure';
            console.error('Error submitting form:', error);
          });
    },
  },
}
</script>

<template>
  <div class="div-contactus">
    <div class="go-back-div row">
      <div class="col-3">
        <router-link to="/"><abbr title="Go Back"><span class="hvricn bi bi-arrow-left-short"></span></abbr></router-link>
      </div>
      <div class="col-9"></div>
    </div>

    <div v-if="contactUsView==='contact-us-form'" class="centered row">
      <h1 class="cu-title">Contact Us</h1>
      <div class="form-container col-lg-6">
        <form @submit.prevent="submitForm">
          <label for="topic" class="form-label">Topic</label>
          <select id="topic" v-model="topic" class="form-select" required>
            <option v-for="topicOption in topics" :key="topicOption" :value="topicOption">{{ topicOption }}</option>
          </select>

          <label for="subject" class="form-label">Subject</label>
          <input type="text" id="subject" v-model="subject" class="form-control" required />

          <label for="email" class="form-label">Your Email</label>
          <input type="email" id="email" v-model="email" class="form-control" required />

          <label for="message" class="form-label">Message</label>
          <textarea id="message" v-model="message" class="form-control" rows="5" required></textarea>

          <button type="submit" class="btn btn-outline-primary col-sm-12"><strong>Submit</strong></button>
        </form>
      </div>
      <div class="article-container card col-lg-6">
        <h2>Social Media</h2>
        <p>Coming Soon...</p>
      </div>
    </div>
    <div v-if="contactUsView==='contact-us-success'" class="centered row">
      <span class="hvricn bi bi-envelope-check-fill"></span>
      <h1 class="cu-success-h1">The email was sent successfully.</h1>
      <small><strong>One of our administrators should contact you soon.</strong></small>
    </div>
    <div v-if="contactUsView==='contact-us-failure'" class="centered row">
      <span class="hvricn bi bi-envelope-exclamation-fill"></span>
      <h1 class="cu-failure-h1">Message failed to send...</h1>
      <small><strong>Something went wrong. Please try again later.</strong></small>
    </div>
  </div>
</template>

<style scoped>
.div-contactus {
  background-color: #354A18;
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
.cu-title {
  text-align: center;
}
.go-back-div {
  height: 0;
}
.bi-arrow-left-short, .bi-envelope-check-fill, small, .bi-envelope-exclamation-fill{
  color: #4EC33D;
}
.form-container, .article-container {
  width: 50%;
}
.article-container {
  margin-top: 2.5%;
  padding-bottom: 23.2%;
}
.form-label {
  display: block;
  margin-top: 10px;
  color: #4EC33D;
}
.form-control, .form-select {
  width: 100%;
  padding: 10px;
  margin-top: 10px;
  box-sizing: border-box;
}
input, select, textarea, .card {
  background-color: #F9EBF3;
  color: #4EC33D;
}
button {
  margin-top: 20px;
  padding: 10px 20px;
  color: #4EC33D;
  border-color: #2C7721;
}
.article-container h2, .article-container p, h1{
  color: #4EC33D;
  text-align: center;
}
button:hover {
  background-color: #F9EBF3;
  color: #354A18;
  border-color: #2C7721;
}
.bi-envelope-check-fill, small, .bi-envelope-exclamation-fill{
  text-align: center;
}
</style>