<script>
import {fetchWithAuth} from '../utils/api';
export default {
  name: 'AssessmentModule',
  data() {

    const querydata = this.$route.query.data ? JSON.parse(decodeURIComponent(this.$route.query.data)) : {};

    return {
      providerID: querydata.providerId || "",
      assessmentObject: querydata.assessmentOBJ || {},
      providerObject: querydata.providerOBJ || {},
      resourcesObject: querydata.resourceOBJ || {},

      cloudProvider: "",
      justification: "",
      costPerMonth: 0,
      migrationTime:0,
      confidenceReason:0,

      costBreakdown:[],
      priceSavings:null,

      migrationDificulty:"",
      keyResources:[[]],
      steps:[],

      view: "Loading",
      boxView:-1,
    };
  },

  async mounted() {
    try {
      await fetchWithAuth('http://localhost:8080/api/loggedin/user');
    } catch (error) {
      console.error('Error validating your token. Please log in again', error);
    }

    this.fetchWorkstreamData();
  },
  methods: {
    async fetchWorkstreamData() {
      try {
        const response = await fetch('http://localhost:8080/api/initialiseAssessmentModule', {
          method: 'POST',
          headers: {  'Content-Type': 'application/json',
                      'Authorization': 'Bearer ' + localStorage.getItem('accessToken')},
          body: JSON.stringify({providerId: this.providerID, assessment: this.assessmentObject, providersJ: this.providerObject, resources: this.resourcesObject}),
        });

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();

        console.log("Data received from server:", data);

        this.cloudProvider = data.cloudProvider;
        this.justification = data.justification;
        this.costPerMonth = data.costEstimator.costPerMonth;
        this.migrationTime = data.migrationPlanner.migrationTime;
        this.confidenceReason = data.justification;

        this.costBreakdown = data.costEstimator.costBreakdown;
        this.priceSavings = data.costEstimator.priceSavings;

        this.migrationDificulty = data.migrationPlanner.migrationDifficulty;
        this.keyResources = data.migrationPlanner.keyResources;

        this.view = "AssessmentModule";

      } catch (error) {
        console.error("Error fetching workstream data:", error);
        this.$router.push({
          name: 'FailedProcess',
          query: { data: encodeURIComponent(JSON.stringify({ message: "Failed To Retrieve Workstream and Input data. Please Try Again." })) }
        });
      }
    },
    convertToText(boolvalue){
      return boolvalue ? "Yes" : "No";
    },
  },

}

</script>

<template>
  <div class="container-fluid">
    <div v-if="view==='Loading'">
      <h1>Loading...</h1>
      <div class="buffering-animation"></div>
    </div>
    <transition name="slide-fade">
    <div class="d-flex flex-wrap centered" v-if="view==='AssessmentModule'">
      <div class="card col-2 one">
        <div class="card-body">
          <h1>Inputs</h1>
          <hr>
          <div v-for="(answer, questionId) in assessmentObject.questionAnswer" :key="questionId">
            <p> {{questionId}} - {{ questionId }}</p>
            <input type="text" class="form-control" :value="answer" readonly>
            <hr>
          </div>
        </div>
      </div>

      <div class="col-9 twotosix">
        <div class="row">
          <div class="card col-6 two">
            <div class="card-body">

              <h1><input v-model="moduleTitle" type="text" class="form-control" placeholder="Enter Workstream Title Here"></h1>
              <h1>You Should Consider Using {{cloudProvider}}</h1>
              <h2>Here's Why:</h2>
              <hr>
              <p>{{justification}}</p>

            </div>
          </div>

          <div class="card col-3 three">
            <div class="card-body three-body">
              <h1>Save Workstream</h1>
              <h1>Edit Workstream</h1>
              <router-link to="/assess-workstream"><h1>Redo Workstream</h1></router-link>
            </div>
          </div>
        </div>

        <div class="row">
          <div class="card col-3 four">
            <div class="card-body four-body">
              <h1>Cost Estimator</h1>
              <hr>
              <h2>€{{initialCost}} initial cost</h2>
              <h3>€{{costPerMonth}} per month</h3>
              <button @click="view = 'CostEstimator'" class="btn btn-link">Click Here To View More </button>
            </div>
          </div>
          <!-- Div 5 -->
          <div class="card col-3 five">
            <div class="card-body five-body">
              <h1>Migration Planner</h1>
              <hr>
              <h2>{{migrationTime}} Hour(s)</h2>
              <p>may be needed for successful migration</p>
              <button @click="view = 'MigrationPlanner'" class="btn btn-link">Click Here To View More </button>
            </div>
          </div>

          <!-- Div 6 -->
          <div class="card col-3 six">
            <div class="card-body six-body">
              <h1>Confidence Level</h1>
              <h1>{{confidenceLevel}}%</h1>
              <button @click="view = 'Justification'" class="btn btn-link">Read Justification Here</button>
            </div>
          </div>
        </div>
      </div>
    </div>
    </transition>

    <transition name="slide-fade">
    <div class="d-flex flex-wrap centered" v-if="view==='CostEstimator'">

      <div class="card col-2 costBreakdown">
        <div class="card-body">
          <h1>Cost Breakdown</h1>
          <hr>
          <div v-for="(cost, index) in costBreakdown" :key="index">
            <h3>{{cost[0]}}</h3>
            <dt>{{cost[1]}}</dt>
            <dd>{{cost[2]}}</dd>
            <hr>
          </div>
        </div>
      </div>

      <div class="col-9 costsAndSavings">
        <div class="row">
          <div class="costs">
            <h1>€{{initialCost}} Initial Cost</h1>
            <h2>€{{costPerMonth}} Monthly Costs</h2>
            <button class="btn btn-link" @click="view='AssessmentModule'">Return back to module?</button>
          </div>
        </div>
        <div class="row">
          <div class="card savings">
            <div class="card-body">
              <h2>Price Savings Compared to Other Providers</h2>

              <div v-for="(savings, provider) in priceSavings" :key="provider" class="progress" style="height:3em">
                <div class="progress-bar" :style="{ width: savings + '%' }"><h3>{{provider}}</h3></div>  <h3 class="p-title"> €{{savings}}</h3>
              </div>

            </div>
          </div>
        </div>
      </div>
    </div>
    </transition>

    <transition name="slide-fade">
      <div class="d-flex flex-wrap centered" v-if="view==='MigrationPlanner'">
        <div class="col-7 timeAndResources">

          <div class="row">
            <div class="time">
              <h1>{{migrationTime}} min(s) estimated to migrate fully</h1>
              <h2>Monthly Costs: {{migrationDificulty}}</h2>
              <button class="btn btn-link" @click="view='AssessmentModule'">Return back to module?</button>
            </div>
          </div>

          <div class="row">
            <div class="card resources">
              <div class="card-body">
                <h2>Key Resources - Click one to learn more.</h2>

                <div class="row">
                  <div v-for="(resources, index) in keyResources" :key="index" class="col-1 boxes">
                    <button class="btn btn-link w-10" @click="boxView = index; console.log(boxView)"><span class="hvricn bi bi-box"></span> {{ index[0] }}</button>
                  </div>
                </div>

                <div class="row" v-if="boxView!==-1">
                  <h3>{{ keyResources[boxView][0] }}</h3>
                  <p>{{ keyResources[boxView][1] }}</p>
                </div>

              </div>
            </div>
          </div>
        </div>

        <div class="card col-4 steps">
          <div class="card-body">
            <h1>Steps</h1>
            <hr>
            <div v-for="(step, index) in steps" :key="index">
              <dt>Step {{index + 1}}.</dt>
              <dd>{{step}}</dd>
              <hr>
            </div>
          </div>

        </div>
      </div>
    </transition>

    <transition name="slide-fade">
      <div class="d-flex flex-wrap centered justification" v-if="view==='Justification'">
        <div class="card card-justification">
          <div class="card-body card-body-justification">
            <h1 class=" j j-h1">The Confidence Level that {{cloudProvider}} is best for you is at {{confidenceLevel}}%</h1>
            <h3 class=" j j-h3">Here's Why:</h3>
            <p class=" j j-p">{{justification}}</p>
            <div class="row">
              <abbr title="The information provided in the assessment module is for guidance purposes only. Please conduct thorough research before making any financial investments related to cloud migration."><span class="bi bi-question-circle-fill"></span></abbr>
            </div>
            <div class="row">
              <button class="btn btn-link" @click="view='AssessmentModule'"><span class="bi bi-arrow-left-circle-fill"></span></button>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </div>

</template>

<style scoped>
.container-fluid{
  background-color: #FFF2F2;
}

.centered {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  margin-left: 10%;
}
.card, .costs, .time{
  margin-top: 1em;
  height: 23.70em;
  border: 0;
}
.costs, .time{
  padding: 10%;
  text-align: center;
  justify-content: center;
  align-items: center;
}
.boxes{
  margin: 1em;
}

.three-body, .six-body{
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;}

.four-body, .five-body{
  text-align: center;
}

.four-body h1, .four-body h2, .five-body h1, .five-body h2, .four-body h3, .five-body p{
  margin: 1em;
}

h2,h3, small, .btn-link{
  color: #A9B5DF;
}

.one, .costBreakdown, .steps{
  height: 85vh;
  margin-top: .25em;
  margin-bottom: 1em;
  max-height: 85vh;
  overflow-y: auto;
}

.two, .card-justification{
  overflow-y: auto;
}


.twotosix, .costsAndSavings, .timeAndResources{
  height: 90vh;
}

.two, .five, .costs, .savings, .resources{
  margin-right: 1em;
}

h1{
  color: #2D336B;
  white-space: nowrap;
  text-overflow: ellipsis;
  font-size: 1.5rem;
}
hr{
  color: #7886C7;
}
p{
  color: #7886C7;
}
.p-title{
  margin-right: 2em;
}
.progress{
  margin: 5em;
}
.progress-bar{
  background-color: #7886C7;
}

.justification {
  margin: 0;
}

.card-justification {
  height: 90vh;
  overflow-y: auto;
}

.j-h1{
  font-size: 3em;
}
.j-p{
  margin: 1em;
}
.j-btn{
  text-align: center;
  justify-content: center;
  align-items: center;
}

.slide-fade-enter-active,
.slide-fade-leave-active {
  transition: all 0.5s ease;
}

.slide-fade-enter-from {
  transform: translateX(100%);
  opacity: 0;
}

.slide-fade-leave-to {
  transform: translateX(-100%);
  opacity: 0;
}

</style>