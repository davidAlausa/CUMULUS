<script>
export default {
  name: 'ProcessWorkstream',
  data() {

    const formdata = this.$route.query.data ? JSON.parse(decodeURIComponent(this.$route.query.data)) : {};

    return {
      processState: "Assessing Workstream",
      workstreamData: {},
      inputData: formdata || {},
      buffering: true,
      workstreamID: "",


  };
  },
  mounted() {
    fetch('http://localhost:8080/api/assess-workstream', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ ...this.inputData }),
      credentials: 'include',
    })
        .then(response => {
          console.log("This was what was sent to the back end to assess:", this.inputData);

          if (!response.ok) {
            throw new Error('Failed to assess workstream');
          }
          return response.json();
        })
        .then(data => {
          try {

            console.log("Data from backend:", data);

            this.migrationPlanner = JSON.parse(data.migrationPlanner).migrationPlanner;
            this.assessmentModule = JSON.parse(data.assessmentModule).assessmentModule;
            this.justification = JSON.parse(data.justification).justification;
            this.costEstimator = JSON.parse(data.costEstimator).costEstimator;

            console.log("Assessment Module:", this.assessmentModule);
            console.log("Cost Estimator:", this.costEstimator);
            console.log("Migration Planner:", this.migrationPlanner);
            console.log("Justification:", this.justification);

            this.workstreamData = {
              assessmentModule: this.assessmentModule,
              costEstimator: this.costEstimator,
              migrationPlanner: this.migrationPlanner,
              justification: this.justification,
            };

            this.processWorkstream();
          } catch (error) {
            console.error("Error parsing JSON:", error);
            throw new Error("Failed to parse backend response.");
          }
        })
        .catch(error => {
          console.error('Error submitting form:', error);
          this.$router.push({
            name: 'FailedProcess',
            query: {
              data: encodeURIComponent(
                  JSON.stringify({ message: "Failed To Assess Workstream. Please Try Again." })
              ),
            },
          });
        });
  },

  methods:{
    processWorkstream() {
      this.processState = "Saving Workstream";

      fetch('http://localhost:8080/api/process-workstream', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
        },
        body: JSON.stringify(this.workstreamData),
      })
          .then( response => {
            if (!response.ok) {
              throw new Error('Failed to process workstream')
            }
            return response.json();
          })
          .then(data => {
            console.log("This was the workstream data that was sent to the backend to be saved", JSON.stringify(this.workstreamData));
            console.log("Workstream processed:", data)
            this.workstreamID = data.workstreamID ? data.workstreamID : "";
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
      this.processState = "Saving Inputs";

      const inputContent = this.inputData.messages[0]?.content || "{}";
      const parsedInputData = JSON.parse(inputContent);


      console.log("Saving inputs:", this.inputData);

      fetch('http://localhost:8080/api/save-inputs', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
        },
        body: JSON.stringify({
          workstreamID: this.workstreamID,
          ...parsedInputData
        })
      })
          .then(response => response.json())
          .then(data => {
            console.log("Inputs saved:", data);
            this.redirectAM();
          })
          .catch(error => {
            console.error('Error saving inputs:', error);
            this.$router.push({
              name: 'FailedProcess',
              query: { data: encodeURIComponent(JSON.stringify({ message: "Failed To Save Inputs. Please Try Again." })) }
            });
          });
    },

    redirectAM(){
      console.log("This is the workstreamID that is now being pushed to the next page AM: ", this.workstreamID);
      this.$router.push({
        name: 'AssessmentModule',
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
  background-color: rgba(234, 208, 206, 0.98);
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
  color: #e43d40;
  text-align: center;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>