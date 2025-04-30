<script>
export default {
  name: 'AssessWorkstreamOld',
  data() {
    return {
      AWView: 1,
      workstreamType: '',
      createBy: '',
      isWhole: 0,
      reason: '',
      accessAmount: '',
      sensitivityLevel: 1,
      isDirect: 0,
      isLocal: 0,
      importantAspect: '',
      isMultiPlatform: 0,
      payPlan: '',
      priority: '',
      isConstant: 0,
      isAutomated: 0,

      progress: 100/12,
    };
  },
  methods: {
    selectWorkstream(workstreamType) {
      this.workstreamType = workstreamType;
      this.nextForm();
    },
    selectCreateBy(createBy) {
      this.createBy = createBy;
      this.nextForm();
    },
    selectIsWhole(isWhole) {
      this.isWhole = isWhole;
      this.nextForm();
    },
    selectReason() {
      this.reason = document.getElementById('reason').value;
      this.nextForm();
    },
    selectAccessAmount() {
      this.accessAmount = document.getElementById('accessAmount').value;
      this.nextForm();
    },
    selectSensitivityLevel() {
      this.sensitivityLevel = document.getElementById('sensitivityLevel').value;
      this.nextForm();
    },
    selectIsDirect(isDirect) {
      this.isDirect = isDirect;
      this.nextForm();
    },
    selectIsLocal(isLocal) {
      this.isLocal = isLocal;
      this.nextForm();
    },
    selectImportantAspect() {
      this.importantAspect = document.getElementById('importantAspect').value;
      this.nextForm();
    },
    selectIsMultiPlatform(multiPlatform) {
      this.isMultiPlatform = multiPlatform;
      this.nextForm();
    },
    selectPayPlan(payPlan) {
      this.payPlan = payPlan;
      this.nextForm();
    },
    selectPriority(priority) {
      this.priority = priority;
      this.nextForm();
    },
    selectIsConstant(constant) {
      this.isConstant = constant;
      this.nextForm();
    },
    selectIsAutomatic(automatic) {
      this.isAutomated = automatic;
      this.sendInputs();
    },
    nextForm() {
      if (this.AWView > 2) {
        this.progress = this.progress + 100/12;
      }

      this.AWView = this.AWView + 1;
      console.log(this.AWView + ' and ' + this.workstreamType + ' and ' + this.createBy + ' and ' + this.isWhole + ' and ' + this.reason + ' and ' + this.accessAmount + ' and ' + this.sensitivityLevel + ' and ' + this.isDirect + ' and ' + this.isLocal + ' and ' + this.importantAspect + ' and ' + this.isMultiPlatform + ' and ' + this.payPlan + ' and ' + this.priority + ' and ' + this.isConstant + ' and ' + this.isAutomated);
    },
    previousForm() {
      this.progress = this.progress - 100/12;
      this.AWView = this.AWView - 1;
    },
    sendInputs() {

      if(this.accessAmount>100){
        this.accessAmount = 100;
      }
      else if(this.accessAmount<1){
        this.accessAmount = 1;
      }

      const formdata = {
        model: "gpt-3.5-turbo",
        messages: [{ role: "user", content: JSON.stringify({
            workstreamType: this.workstreamType,
            createBy: this.createBy,
            isWhole: this.isWhole,
            reason: this.reason,
            accessAmount: this.accessAmount,
            sensitivityLevel: this.sensitivityLevel,
            isDirect: this.isDirect,
            isLocal: this.isLocal,
            importantAspect: this.importantAspect,
            isMultiPlatform: this.isMultiPlatform,
            payPlan: this.payPlan,
            priority: this.priority,
            isConstant: this.isConstant,
            isAutomated: this.isAutomated,
          }) }]
      };

      this.$router.push({
        name: 'ProcessWorkstream',
        query: { data: encodeURIComponent(JSON.stringify(formdata)) }
      });
    },
  },
}

</script>

<template>
  <div class="container-fluid">
    <div class="row top-header">
      <div class="col-3">
        <router-link to="/dashboard"><abbr title="Go Back To Dashboard"><span class="hvricn bi bi-arrow-left-short"></span></abbr></router-link>
      </div>
      <div class="col-9"></div>
    </div>

    <transition name="fade" mode="out-in">
    <div class="centered" v-if="AWView===1">
      <h1>What would you like to assess today?</h1>
      <form @submit.prevent="nextForm">
        <div class="row pick-workstream-type ">
            <div class="col-sm-5">
              <abbr title="Create a business workstream to assess business operations."><button class="btn" type="button" @click="selectWorkstream('Business')"><strong> A Business Component</strong></button></abbr>
            </div>
            <div class="col-sm-2">
              <p class="center-text"><strong>OR</strong></p>
            </div>
            <div class="col-sm-5">
              <abbr title="Create a project workstream to assess more personal project-based tasks."><button class="btn" type="button" @click="selectWorkstream('Project')"><strong> A Documented Project</strong></button></abbr>
            </div>
        </div>
      </form>
      <abbr title="Choose the type of workstream that best fits the description of the task you want to assess."><span class="bi bi-question-circle-fill"></span></abbr>
    </div>
    </transition>
    <transition name="fade" mode="out-in">
    <div class="centered" v-if="AWView===2">
      <h1>Are You Building This From Scratch?</h1>
      <form @submit.prevent="nextForm">
        <div class="row pick-workstream-type ">
          <div class="col-sm-5">
            <abbr title="Create a custom workstream based on your inputs."><button class="btn" type="button" @click="selectCreateBy('self')"><strong>Assessing Workstream from scratch</strong></button></abbr>
          </div>
          <div class="col-sm-2">
            <p class="center-text"><strong>OR</strong></p>
          </div>
          <div class="col-sm-5">
            <abbr title="Create a custom workstream based on an existing one."><button class="btn" type="button" @click="selectCreateBy('template')"><strong>Assess based on a pre-made template</strong></button></abbr>
          </div>
        </div>
      </form>
      <button class="btn btn-link" @click="previousForm">Go back</button>
      <abbr title="Choose whether to assess your task by answering a series of questions or by modifying a pre-existing workstream."><span class="bi bi-question-circle-fill"></span></abbr>
    </div>
    </transition>

    <transition name="fade" mode="out-in">
    <div class="centered" v-if="AWView===3">
      <h1>Is the workstream a whole business or a partial workload?</h1>
      <small><strong>Question 1 - Operational Excellence</strong></small>
      <form @submit.prevent="nextForm">
        <div class="row pick-workstream-type ">
          <div class="col-sm-4">
            <abbr title="Assess the entire business as a single entity"><button class="btn" type="button" @click="selectIsWhole(0)"><strong>Whole</strong></button></abbr>
          </div>
          <div class="col-sm-4">
            <p class="center-text"><strong>OR</strong></p>
          </div>
          <div class="col-sm-4">
            <abbr title="Assess a specific part or division of the business"><button class="btn" type="button" @click="selectIsWhole(1)"><strong>Partial</strong></button></abbr>
          </div>
        </div>
      </form>
      <button class="btn btn-link" @click="previousForm">Go back</button>

      <abbr title="Choose whether to do an assessment for a business as a whole or just a specific aspect of a business."><span class="bi bi-question-circle-fill"></span></abbr>

      <div class="progress">
            <div :style="{ width: Math.round(progress)   + '%' }">
              <div class="progress-bar">{{Math.round(progress) }}%</div>
            </div>
      </div>

    </div>
    </transition>

    <transition name="fade" mode="out-in">
    <div class="centered" v-if="AWView===4">
      <h1>What do you want the cloud to do for your workstream?</h1>
      <small><strong>Question 2 - Operational Excellence</strong></small>
      <form @submit.prevent="nextForm" class="aw-form-textarea-div">
        <div class="row pick-workstream-type aw-textarea-div">
          <textarea class="form-control" placeholder="Enter Reason Here." name="reason" id="reason"  required></textarea>
        </div>
      </form>
      <button class="btn rsn-btn" type="button" @click="selectReason">Submit</button>
      <button class="btn btn-link" @click="previousForm">Go back</button>
      <abbr title="Why are you assessing your task?"><span class="bi bi-question-circle-fill"></span></abbr>

      <div class="progress">
        <div :style="{ width: Math.round(progress)   + '%' }">
          <div class="progress-bar">{{Math.round(progress) }}%</div>
        </div>
      </div>

    </div>
    </transition>

    <transition name="fade" mode="out-in">
    <div class="centered" v-if="AWView===5">
      <h1>How many people will have access to your workstream?</h1>
      <small><strong>Question 3 - Security</strong></small>
      <form @submit.prevent="nextForm">
        <div class="row pick-workstream-type ">
          <input class="form-control" type="number" min="1" max="100" id="accessAmount" required>
        </div>
      </form>
      <button class="btn rsn-btn" type="button" @click="selectAccessAmount">Submit</button>
      <button class="btn btn-link" @click="previousForm">Go back</button>
      <abbr title="How many people would roughly have access to what you are assessing today?"><span class="bi bi-question-circle-fill"></span></abbr>

      <div class="progress">
        <div :style="{ width: Math.round(progress)   + '%' }">
          <div class="progress-bar">{{Math.round(progress) }}%</div>
        </div>
      </div>

    </div>
    </transition>

    <transition name="fade" mode="out-in">
    <div class="centered" v-if="AWView===6">
      <h1>How sensitive is the data involved in your workstream?</h1>
      <small><strong>Question 4 - Security</strong></small>
      <form @submit.prevent="nextForm" class="sL-form">
        <div class="row pick-workstream-type ">
          <input type="range" v-model="sensitivityLevel" min="1" max="100" step="1" class="sensitivityLevel" id="sensitivityLevel">
          <div class="d-flex justify-content-between">
            <p class="p-left">Least sensitive</p>
            <p class="p-right">Most sensitive</p>
          </div>
        </div>
      </form>
      <button class="btn rsn-btn" type="button" @click="selectSensitivityLevel">Submit</button>
      <button class="btn btn-link" @click="previousForm">Go back</button>
      <abbr title="Least sensitive to the most sensitive (Left -> Right)"><span class="bi bi-question-circle-fill"></span></abbr>

      <div class="progress">
        <div :style="{ width: Math.round(progress)   + '%' }">
          <div class="progress-bar">{{Math.round(progress) }}%</div>
        </div>
      </div>

    </div>
    </transition>

    <transition name="fade" mode="out-in">
    <div class="centered" v-if="AWView===7">
      <h1>Will customers interact with the workstream directly?</h1>
      <small><strong>Question 5 - Reliablity</strong></small>
      <form @submit.prevent="nextForm">
        <div class="row pick-workstream-type">
          <div class="col-sm-5">
            <abbr title="Customers will interact directly with this workstream (e.g., a website or app interface)."><button class="btn" type="button" @click="selectIsDirect(0)">Yes</button></abbr>
          </div>
          <div class="col-sm-2">
            <p class="center-text">OR</p>
          </div>
          <div class="col-sm-5">
            <abbr title="Customers will not interact directly with this workstream (e.g., a background service or internal process)."><button class="btn" type="button" @click="selectIsDirect(1)">No</button></abbr>
          </div>
        </div>
      </form>
      <button class="btn btn-link" @click="previousForm">Go back</button>
      <abbr title="Does this workstream have a customer-facing component (like a website), or does it operate in the background (like a database)?"><span class="bi bi-question-circle-fill"></span></abbr>

      <div class="progress">
        <div :style="{ width: Math.round(progress)   + '%' }">
          <div class="progress-bar">{{Math.round(progress) }}%</div>
        </div>
      </div>

    </div>
    </transition>

    <transition name="fade" mode="out-in">
    <div class="centered" v-if="AWView===8">
      <h1>Will your workstream serve local or international users?</h1>
      <small><strong>Question 6 - Reliablity</strong></small>
      <form @submit.prevent="nextForm">
        <div class="row pick-workstream-type ">
          <div class="col-sm-5">
            <abbr title="Customers will be based in Ireland"><button class="btn" type="button" @click="selectIsLocal(0)">Local</button></abbr>
          </div>
          <div class="col-sm-2">
            <p class="center-text"><strong>OR</strong></p>
          </div>
          <div class="col-sm-5">
            <abbr title="Customers will be based worldwide"><button class="btn" type="button" @click="selectIsLocal(1)">International</button></abbr>
          </div>
        </div>
      </form>
      <button class="btn btn-link" @click="previousForm">Go back</button>
      <abbr title="Where will the majority of the customer traffic come from?"><span class="bi bi-question-circle-fill"></span></abbr>

      <div class="progress">
        <div :style="{ width: Math.round(progress)   + '%' }">
          <div class="progress-bar">{{Math.round(progress) }}%</div>
        </div>
      </div>

    </div>
    </transition>

    <transition name="fade" mode="out-in">
    <div class="centered" v-if="AWView===9">
      <h1>What would you say is the most important aspect of your workstream operations?</h1>
      <small><strong>Question 7 - Performance Efficiency</strong></small>
      <form @submit.prevent="nextForm" class="aw-form-textarea-div">
        <div class="row pick-workstream-type">
          <textarea class="form-control" placeholder="Enter Reason Here." name="importantAspect" id="importantAspect" required></textarea>
        </div>
      </form>
      <button class="btn rsn-btn" type="button" @click="selectImportantAspect">Submit</button>
      <button class="btn btn-link" @click="previousForm">Go back</button>
      <abbr title="What is the most important factor in regards to you workstream? (e.g saving cost, being automated)"><span class="bi bi-question-circle-fill"></span></abbr>

      <div class="progress">
        <div :style="{ width: Math.round(progress)   + '%' }">
          <div class="progress-bar">{{Math.round(progress) }}%</div>
        </div>
      </div>

    </div>
    </transition>

    <transition name="fade" mode="out-in">
    <div class="centered" v-if="AWView===10">
      <h1>Does your workstream interact with other systems?</h1>
      <small><strong>Question 8 - Performance Efficiency</strong></small>
      <form @submit.prevent="nextForm">
        <div class="row pick-workstream-type ">
          <div class="col-sm-5">
            <abbr title="Yes, this workstream will talk to other apps and software"><button class="btn" type="button" @click="selectIsMultiPlatform(0)">Yes</button></abbr>
          </div>
          <div class="col-sm-2">
            <p class="center-text">OR</p>
          </div>
          <div class="col-sm-5">
            <abbr title="No, this workstream acts alone with my software only"><button class="btn" type="button" @click="selectIsMultiPlatform(1)">No</button></abbr>
          </div>
        </div>
      </form>
      <button class="btn btn-link" @click="previousForm">Go back</button>
      <abbr title="What does your workstream connect to?"><span class="bi bi-question-circle-fill"></span></abbr>

      <div class="progress">
        <div :style="{ width: Math.round(progress)   + '%' }">
          <div class="progress-bar">{{Math.round(progress) }}%</div>
        </div>
      </div>

    </div>
    </transition>

    <transition name="fade" mode="out-in">
    <div class="centered" v-if="AWView===11">
      <h1>What pay plan would suit you?</h1>
      <small><strong>Question 9 - Cost Optimisation</strong></small>
      <form @submit.prevent="nextForm">
        <div class="row pick-workstream-type ">
          <div class="col-sm-3">
            <abbr title="Paying as you use the software. Ideal for Short-term projects"><button class="btn" type="button" @click="selectPayPlan('PAYG')">Pay As You Go</button></abbr>
          </div>
          <div class="col-sm-1">
            <p class="center-text">OR</p>
          </div>
          <div class="col-sm-3">
            <abbr title="Commit to paying a discounted amount for 1-3 years usage"><button class="btn" type="button" @click="selectPayPlan('MDTM')">Over A Period Of Time</button></abbr>
          </div>
          <div class="col-sm-1">
            <p class="center-text">OR</p>
          </div>
          <div class="col-sm-4">
            <abbr title="Use Resources If Available. Though cheaper, performance may be impacted"><button class="btn" type="button" @click="selectPayPlan('SPRS')">Use Resources If Available</button></abbr>
          </div>
        </div>
      </form>
      <button class="btn btn-link" @click="previousForm">Go back</button>
      <abbr title="What pay plan best suits you and your workstream?"><span class="bi bi-question-circle-fill"></span></abbr>

      <div class="progress">
        <div :style="{ width: Math.round(progress)   + '%' }">
          <div class="progress-bar">{{Math.round(progress) }}%</div>
        </div>
      </div>

    </div>
    </transition>

    <transition name="fade" mode="out-in">
    <div class="centered" v-if="AWView===12">
      <h1>What do you prioritise more?</h1>
      <small><strong>Question 10 - Cost Optimisation</strong></small>
      <form @submit.prevent="nextForm">
        <div class="row pick-workstream-type ">
          <div class="col-sm-5">
            <abbr title="My workstream needs to be as cheap as possible"><button class="btn" type="button" @click="selectPriority('Cost')">Cost</button></abbr>
          </div>
          <div class="col-sm-2">
            <p class="center-text">OR</p>
          </div>
          <div class="col-sm-5">
            <abbr title="My workstream needs to work to the best of its ability"><button class="btn" type="button" @click="selectPriority('Operational Excellence')">Operational Excellence</button></abbr>
          </div>
        </div>
      </form>
      <button class="btn btn-link" @click="previousForm">Go back</button>
      <abbr title="What would you prioritise more when assessing your workstream?"><span class="bi bi-question-circle-fill"></span></abbr>

      <div class="progress">
        <div :style="{ width: Math.round(progress)   + '%' }">
          <div class="progress-bar">{{Math.round(progress) }}%</div>
        </div>
      </div>

    </div>
    </transition>

    <transition name="fade" mode="out-in">
    <div class="centered" v-if="AWView===13">
      <h1>How often is you workstream supposed to be operational?</h1>
      <small><strong>Question 11 - Sustainability</strong></small>
      <form @submit.prevent="nextForm">
        <div class="row pick-workstream-type ">
          <div class="col-sm-5">
            <abbr title="It needs to be online all the time"><button class="btn" type="button" @click="selectIsConstant(0)">24/7</button></abbr>
          </div>
          <div class="col-sm-2">
            <p class="center-text">OR</p>
          </div>
          <div class="col-sm-5">
            <abbr title="Only when needed (e.g work hours)"><button class="btn" type="button" @click="selectIsConstant(1)">A certain amount of hours</button></abbr>
          </div>
        </div>
      </form>
      <button class="btn btn-link" @click="previousForm">Go back</button>
      <abbr title="How accessible do you need your workstream to be?"><span class="bi bi-question-circle-fill"></span></abbr>

      <div class="progress">
        <div :style="{ width: Math.round(progress)   + '%' }">
          <div class="progress-bar">{{Math.round(progress) }}%</div>
        </div>
      </div>

    </div>
    </transition>

    <transition name="fade" mode="out-in">
    <div class="centered" v-if="AWView===14">
      <h1>Do you prioritise automation or manual oversight?</h1>
      <small><strong>Question 12 - Cost Optimisation</strong></small>
      <form @submit.prevent="sendInputs">
        <div class="row pick-workstream-type ">
          <div class="col-sm-5">
            <abbr title="My workstream needs to be able to work and think on its own"><button class="btn" type="button" @click="selectIsAutomatic(0)">Automation</button></abbr>
          </div>
          <div class="col-sm-2">
            <p class="center-text">OR</p>
          </div>
          <div class="col-sm-5">
            <abbr title="I want to have manual control over my workstream"><button class="btn" type="button" @click="selectIsAutomatic(1)">Manual Oversight</button></abbr>
          </div>
        </div>
      </form>
      <button class="btn btn-link" @click="previousForm">Go back</button>
      <abbr title="How in dependent do you need your workstream to be?"><span class="bi bi-question-circle-fill"></span></abbr>

      <div class="progress">
        <div :style="{ width: Math.round(progress)   + '%' }">
          <div class="progress-bar">{{Math.round(progress) }}%</div>
        </div>
      </div>

    </div>
    </transition>
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

h1 {
  font-size: 4em;
  color: #e43d40;
  text-align: center;
}
small, p{
  font-size: 1.2em;
  color: #e43d40;
}
textarea, .form-control {
  color: #e43d40;
}
.aw-form-textarea-div {
  width: 50%;
  text-align: center;
  justify-content: center;
  align-items: center;
}
.center-text {
text-align: center;
}
.rsn-btn {
  margin-top: 1em;
}
.bi {
  color: #1e1f22;
}
.sL-form {
  width: 50%;
}
.sensitivityLevel {
  background: #E43D40;
}
.hvricn {
  color: #E43D40;
}

.hvricn:hover {
  color: #FABEC0;
}
.top-header{
  height: 0;
}

.pick-workstream-type {
  margin-top: 6em;
}

.btn {
  background-color: #E43D40;
  color: #FABEC0;
  border: none;
  margin-bottom: .5em;
}

.btn-link {
  background-color: #F37970;
}

.btn:hover {
  background-color: #FABEC0;
  color: #E43D40;
  border: none;
}

.bi-question-circle-fill {
  font-size: 3em;
  color: #E43D40;
}

.bi-question-circle-fill:hover {
  color: #FABEC0;

}

.progress {
  width: 70%;
  margin-top: 4em;
  transition: width 0.5s ease;
}
.progress-bar {
  background-color: #E43D40;
}
.p-left {
  float: left;
  justify-content: left;
  text-align:  left;
}
.p-right {
  float: right;
  justify-content: right;
  text-align:  right;
}
</style>