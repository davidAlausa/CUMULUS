<script>
import {fetchWithAuth} from "@/utils/api";

export default {
  name: 'ProcessWorkstream',
  data() {

    const formdata = this.$route.query.data ? JSON.parse(decodeURIComponent(this.$route.query.data)) : {};

    return {
      processState: "Choosing Cloud Provider...",
      workstreamData: {},
      inputData: formdata || {},
      attributeIds: {},
      providerScore: {},
      buffering: true,
      workstreamID: "",

  };
  },
  async mounted() {

    try {
      await fetchWithAuth('http://localhost:8080/api/loggedin/user');
    } catch (error) {
      console.error('Error validating your token. Please log in again', error);
    }

    fetch('http://localhost:8080/api/process-workstream', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
      },
      body: JSON.stringify({ questions: this.inputData }),
      credentials: 'include',
    })
        .then(response => {
          if (!response.ok) {
            throw new Error('Failed to assess workstream');
          }
          return response.json();
        })
        .then(data => {
          try {
            this.providerScore = data['providerScores'];
            this.attributeIds = data['attributeIds'];
            this.saveWorkstream();

          } catch (error) {
            this.$router.push({
              name: 'FailedProcess',
              query: { data: encodeURIComponent(JSON.stringify({ message: "Failed To Assess Workstream. Please Try Again." })) }
            });
          }
        })
        .catch(error => {
          this.$router.push({
            name: 'FailedProcess',
            query: {
              data: encodeURIComponent(
                  JSON.stringify({ message: "Failed To Assess Workstream. Please Try Again." + error.message })
              ),
            },
          });
        });
  },

  methods:{
    saveWorkstream() {
      this.processState = "Saving Workstream";

      fetch('http://localhost:8080/api/save-inputs', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
        },
        body: JSON.stringify( {providerScore: this.providerScore, attributeIds: this.attributeIds, questions: this.inputData} ),
      })
          .then( response => {
            if (!response.ok) {
              throw new Error('Failed to process workstream')
            }
            return response.json();
          })
          .then(data => {
            this.workstreamID = data.id ? data.id : "";
            this.saveInputs()
          })
          .catch(error => {
            console.error('Error processing workstream:', error);
            this.$router.push({
              name: 'FailedProcess',
              query: { data: encodeURIComponent(JSON.stringify({ message: "Failed To Save Workstream. Please Try Again." })) }
            });
          });
    },

    saveInputs() {
      this.processState = "Learning From Inputs";

      fetch('http://localhost:8080/api/evolve-question-list', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
        }
      })
          .then(response => response.json())
          .then(data => {
            if (!data.success) {
              throw new Error('Failed to learn from inputs' + data.message);
            }
            this.redirectAM();
          })
          .catch(error => {
            console.error('Error Learning From Inputs:', error);
            this.redirectAM();
          });
    },

    redirectAM(){
      this.$router.push({
        name: 'ViewAssessment',
        query: { data: encodeURIComponent(JSON.stringify({ workstreamID: this.workstreamID })) }
      });
    },
  }
}
</script>

<template>
  <div class="container-fluid">
    <div class="centered">
      <h1>{{processState}}</h1>
      <div v-if="buffering" class="buffering-animation"></div>
    </div>
  </div>

</template>

<style scoped>
.container-fluid {
  background-color: #DBEBED;
}

.centered {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  transition: opacity 0.25s ease-in-out;
}

.buffering-animation {
  width: 50px;
  height: 50px;
  border: 5px solid #f3f3f3;
  border-top: 5px solid #3498db;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 20px auto;
}
h1 {
  font-size: 4em;
  color: #047076;
  text-align: center;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>