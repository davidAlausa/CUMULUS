<script>
export default {
  name: "AssessWorkstream",
  data(){
    return{
      AWView: 1,
      QuestionNumber: 1,
      slideDirection: 'forward',
      progress1: 1,
      progress2: 1,
      progressSplit: 49,
      progressSplit2: 0,
      loading: 0,
      QuestionType: "Core Questions",
      help: "This is a hint to help you understand the question better.",
      questionsObject:{}
    };
  },
  methods:{
    startAssessment(){
      this.AWView=2;
      this.getQuestions();
    },

    async getQuestions(){
      try {
        const response = await fetch("http://localhost:8080/api/getquestions", {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
          },
        });
        if (!response.ok) {
          throw new Error("Failed to fetch questions");
        }
        const data = await response.json();
        this.questionsObject = data.questions;
        console.log(this.questionsObject);
      } catch (error) {
        console.error("Error fetching questions:", error);
      }
    },

    async sendQuestions(questionsObject){
      try {
        const response = await fetch("http://localhost:8080/api/sendfirstsix", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
          },
          body: JSON.stringify({ questions: questionsObject }),
        });
        if (!response.ok) {
          throw new Error("Failed to submit questions");
        }
        const data = await response.json();
        this.questionsObject = [...this.questionsObject, ...data.questions];
        this.progressSplit2 = 50/(this.questionsObject.length-6);
        console.log("Currently doing 50 divided by" + this.questionsObject.length + " - 6");
        return true;
        } catch (error) {
        console.error("Error submitting questions:", error);
      }
    },

    async iterateQuestions(iterator) {
      console.log("----------------------------------------START METHOD " + this.QuestionNumber + " ----------------------------------------");
      iterator > 0 ? console.log("") : console.log("NOTE: BACK BUTTON WAS PRESSED");

      this.QuestionNumber += iterator;


      this.slideDirection = iterator > 0 ? 'forward' : 'backward';

      if (this.QuestionNumber < 1) {
        this.AWView = 1;
        this.QuestionNumber = 1;
        this.progress1 = 1;
        this.progress2 = 1;
        this.progressSplit = 49;
        return;
      }

      if (this.QuestionType === "Core Questions") {
        this.progress1 = iterator > 0 ? this.progress1 + 8.3 : this.progress1 - 8.3;
        this.progressSplit = iterator > 0 ? this.progressSplit - 8.3 : this.progressSplit + 8.3;

        if (this.progressSplit < 0) {
          this.progressSplit = 0;
        }


        if (this.QuestionNumber === 7 && iterator > 0) {

          this.loading = 1;

          const result = await this.sendQuestions(this.questionsObject);

          if (result) {
            this.QuestionType = "Follow Up Questions";
            this.loading = 0;
            this.QuestionNumber = 7;
            this.AWView = 2;
            this.progress1 = 50;
            this.progress2 = 1;
          } else {
            console.error("Error sending questions");
          }
        }
      } else {
        console.log("I choose follow up questions");
        this.progress2 = iterator > 0 ? this.progress2 + this.progressSplit2 : this.progress2 - this.progressSplit2;

        if (this.QuestionNumber > this.questionsObject.length) {
          this.$router.push({
            name: 'ProcessWorkstream',
            query: { data: encodeURIComponent(JSON.stringify(this.questionsObject)) }
          });
        }
      }
    },

    populateQuestions(answer){
      if (this.QuestionNumber - 1 < this.questionsObject.length) {
        this.questionsObject[this.QuestionNumber - 1].answerText = answer;
        if (answer === "Yes") {
          this.questionsObject[this.QuestionNumber - 1].question.score+= 3;
        }
        else if(answer === "No"){
          this.questionsObject[this.QuestionNumber - 1].question.score+= -3;
        }
        else if (answer === "Most Likely") {
          this.questionsObject[this.QuestionNumber - 1].question.score+= 1;
        }
        else if (answer === "Most Likely Not") {
          this.questionsObject[this.QuestionNumber - 1].question.score+= -1;
        }
        else if (answer === "I Don't Know") {
          this.questionsObject[this.QuestionNumber - 1].question.score+= 0;
        }
        this.iterateQuestions(1);
      }
      else {
        console.error("Question number out of bounds");
      }
    }
  }
}

</script>

<template>
  <div class="div-assessworkstream">
    <div class="container-fluid">

      <div class="row top-header">
        <div class="col-3">
          <router-link to="/dashboard"><abbr title="Go Back To Dashboard"><span class="hvricn bi bi-arrow-left-short"></span></abbr></router-link>
        </div>
        <div class="col-9"></div>
      </div>
    </div>

    <div class="body-content">

      <transition :name="AWView === 2 ? 'fade-forward' : 'fade-backward'" mode="in-out">
      <div class="assessmentpage-start" v-if="AWView===1">
        <h1 class="assess-title">Assess Workstream</h1>
        <p>Evaluate a new workstream based on your preferences and visualise a plan on how to make your project come to life!</p>
        <div class="btn-choose row">
          <div class="col-4"></div>
          <div class="col-2 btn-div-1">
            <button class="hvrbtn btn-1 btn si-submit-btn" @click="startAssessment"><strong>Start Assessment</strong></button>
          </div>
          <div class="col-2 btn-div-2">
            <button type="button" class="hvrbtn btn-2 btn si-submit-btn" data-bs-toggle="modal" data-bs-target="#myModal"><strong>How Does It Work?</strong></button>
          </div>

          <div class="modal fade" id="myModal">
            <div class="modal-dialog modal-xl modal-dialog-centered">
              <div class="modal-content">

                <div class="modal-header">
                  <h4 class="modal-title">How the Assessment Module Works...</h4>
                  <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <div class="modal-body">
                  <ul class="list-group list-group-flush">
                    <li class="list-group-item"><span class=" col-2 modal-icon bi bi-clipboard"></span> You will be asked 6 questions relating to the project your are assessing.</li>
                    <li class="list-group-item"><span class=" col-2 modal-icon bi bi-hand-thumbs-up"></span> The answers to these questions are very simple. <strong class="color-green">"Yes"</strong>, <strong class="color-green">"No"</strong>, <strong class="color-green">"Most Likely"</strong>, <strong class="color-green">"Most Likely Not"</strong> or simply <strong class="color-green">"I Don't Know"</strong>!</li>
                    <li class="list-group-item"><span class=" col-2 modal-icon bi bi-terminal"></span> Based on your answers, you may be asked more specific follow-up questions. This is to pinpoint exactly what you need!</li>
                    <li class="list-group-item"><span class=" col-2 modal-icon bi bi-emoji-smile"></span> That's it! Sit back and let us figure out the hard parts for you.</li>
                    <li class="list-group-item"><span class=" col-2 modal-icon bi bi-question-lg"></span> Still confused? That's alright. We have little hints to help explain questions in more detail. Just look for the <span class="modal-icon modal-icon-question bi bi-question-circle"></span> symbol.</li>
                  </ul>
                </div>

                <!-- Modal footer -->
                <div class="modal-footer">
                  <button type="button" class="btn btn-danger" data-bs-dismiss="modal">Close</button>
                </div>

              </div>
            </div>
          </div>

          <div class="col-4"></div>

        </div>
      </div>
      </transition>

      <transition :name="AWView === 2 ? 'fade-forward' : 'fade-backward'" mode="in-out">
      <div class="assessmentpage-questions" v-if="AWView===2">
        <div class="Questions-container" v-if="loading===0">
          <transition :name="slideDirection === 'forward' ? 'slide-left' : 'slide-right'" mode="out-in">
          <div class="Questions" :key="QuestionNumber">
            <p>Question {{QuestionNumber}} - {{QuestionType}}</p>
            <h1>{{questionsObject[QuestionNumber - 1]?.question.text}}</h1>
          </div>
          </transition>
        </div>

        <div class="Loading-container" v-if="loading===1">
          <div class="Loading center">
            <p class="center">Please wait a moment</p>
            <h1>Loading Up Follow Up Questions...</h1>
            <div class="spinner-border text-success" role="status">
              <span class="visually-hidden ">Loading...</span>
            </div>
          </div>
        </div>

        <div class="Answers" v-if="loading===0">
          <div class="btn-group  btn-group-lg">
            <button type="button" class="btn question-btn" @click="populateQuestions('Yes')">Yes</button>
            <button type="button" class="btn question-btn" @click="populateQuestions('No')">No</button>
            <button type="button" class="btn question-btn" @click="populateQuestions('I Don\'t Know')">I Don't Know</button>
            <button type="button" class="btn question-btn" @click="populateQuestions('Most Likely')">Most Likely</button>
            <button type="button" class="btn question-btn" @click="populateQuestions('Most Likely Not')">Most Likely Not</button>
          </div>
        </div>

        <div class="Answers" v-if="loading===1">
          <div class="btn-group  btn-group-lg">
            <button type="button" class="btn question-btn">Yes</button>
            <button type="button" class="btn question-btn">No</button>
            <button type="button" class="btn question-btn">I Don't Know</button>
            <button type="button" class="btn question-btn">Most Likely</button>
            <button type="button" class="btn question-btn">Most Likely Not</button>
          </div>
        </div>

        <div class="HelpBack row">
          <div class="col-4"></div>
          <div class="col-2 goback">
            <button type="button" class="btn btn-link" v-if="loading === 0 && QuestionNumber !== 7"><span class="bi bi-arrow-left-circle-fill" @click="iterateQuestions(-1)"></span></button>
            <button type="button" class="btn btn-link" v-if="loading === 1 || QuestionNumber === 7"><span class="bi bi-arrow-left-circle-fill"  ></span></button>
          </div>
          <div class="col-2 helphint">
            <span class="bi bi-question-circle-fill" data-bs-toggle="tooltip" title={{help}}></span>
          </div>
          <div class="col-4"></div>
        </div>


        <div class="Progress">
          <div class="progress" style="height:50px">
            <div class="progress-bar bg-success progress-bar-striped progress-bar-animated" :style="{ width: progress1 + '%' }"></div>
            <div class="progress-bar bg-light progress-bar-striped progress-bar-animated" :style="{ width: progressSplit + '%' }"></div>
            <div class="progress-bar bg-warning progress-bar-striped progress-bar-animated" :style="{ width: progress2 + '%' }"></div>
          </div>
        </div>

      </div>
      </transition>
    </div>

  </div>

</template>

<style scoped>
.color-green {
  color: #047076;
}
.hvricn {
  color: #047076;
}
.hvricn:hover {
  color: #E8EEF1;
}
.div-assessworkstream {
  background-color: #DBEBED;
  padding: 20px;
  height: 100vh;
}

.btn-div-1, .btn-div-2, .goback, .helphint {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 2em;
}

.btn-1 {
  background-color: #047076;
  color: #DBEBED;
  border-radius: 5px;
  padding: 10px 20px;
}

.btn-2, .question-btn{
  background-color: #DBEBED;
  color: #047076;
  border-color: #10564F;
  border-radius: 5px;
  padding: 10px 20px;
}

.modal-icon {
  color: #047076;
  font-size: 1em;
  margin-right: 1em;
}

.modal-icon-question {
  margin-right: 0;
}

.question-btn:hover {
  background-color: #047076;
  color: #DBEBED;
}

.Questions h1 {
  font-size: 2.5em;
  margin-top: 3em;
  margin-bottom: 3em;
}

.Answers {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 2em;
}

.Progress {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 5em;
}

.progress {
  width: 75%;
}

.progress-bar {
  transition: width 0.6s ease-in-out;
}


.bi-question-circle-fill, .bi-arrow-left-circle-fill {
  color: #047076;
  font-size: 2em;
}

.bi-arrow-left-circle-fill:hover{
  color: #E8EEF1;
}

.fade-forward-enter-active {
  transition: opacity 0.5s ease 0.5s;
}
.fade-forward-leave-active {
  transition: opacity 0.5s ease;
}
.fade-forward-enter-from,
.fade-forward-leave-to {
  opacity: 0;
}
.fade-forward-enter-to,
.fade-forward-leave-from {
  opacity: 1;
}

.fade-backward-enter-active {
  transition: opacity 0.5s ease;
}
.fade-backward-leave-active {
  transition: opacity 0;
}
.fade-backward-enter-from,
.fade-backward-leave-to {
  opacity: 0;
}
.fade-backward-enter-to,
.fade-backward-leave-from {
  opacity: 1;
}


.Questions-container {
  position: relative;
  height: 200px; /* adjust as needed */
  overflow: hidden;
}

.slide-left-enter-active,
.slide-left-leave-active,
.slide-right-enter-active,
.slide-right-leave-active {
  transition: all 0.4s ease;
  position: absolute;
  width: 100%;
}

.slide-left-enter-from {
  transform: translateX(100%);
  opacity: 0;
}
.slide-left-leave-to {
  transform: translateX(-100%);
  opacity: 0;
}

.slide-right-enter-from {
  transform: translateX(-100%);
  opacity: 0;
}
.slide-right-leave-to {
  transform: translateX(100%);
  opacity: 0;
}

.center {
  justify-content: center;
  align-items: center;
  text-align: center;
  margin-top: 2em;
}

/*-----------------------------------------------------------------*/
h1 {
  font-size: 5em;
  color: #047076;
  text-align: center;
  margin-top: 1em;
}
h4 {
  font-size: 2em;
  color: #047076;
}

p{
  color: #047076;
  font-weight: bolder;
  text-align: center;
}
</style>