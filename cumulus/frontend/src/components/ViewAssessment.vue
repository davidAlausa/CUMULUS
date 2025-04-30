<script>
import {fetchWithAuth} from "@/utils/api";

export default {
  name: 'ViewAssessment',
  data() {
    const formdata = this.$route.query.data ? JSON.parse(decodeURIComponent(this.$route.query.data)) : {};
    return {
      assessmentID: formdata.workstreamID || "",
      assessmentObject: {},
      providerObject: {},
      recommendedProviderObject: {},
      resourcesObject: {},
      view: 1,
    };
  },
  async mounted(){
    try {
      await fetchWithAuth('http://localhost:8080/api/loggedin/user');
    } catch (error) {
      console.error('Error validating your token. Please log in again', error);
    }
    fetch('http://localhost:8080/api/getAssessment',{
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
      },
      credentials: 'include',
      body: JSON.stringify({ assessmentId: this.assessmentID })
    })
        .then(response => {
          if (!response.ok) {
            throw new Error('Failed to fetch assessment');
          }
          return response.json();
        })
        .then(data=> {
          try {
            this.assessmentObject = data;
            this.getCloudproviders();
          } catch (error) {
            this.$router.push({
              name: 'FailedProcess',
              query: { data: encodeURIComponent(JSON.stringify({ message: "Failed To Fetch Assessment. Please Try Again." })) }
            });
          }
        })
  },
  methods: {
    getCloudproviders() {
      fetch('http://localhost:8080/api/getCloudProviders', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
        },
        credentials: 'include',
      })
          .then(response => {
            if (!response.ok) {
              throw new Error('Failed to fetch cloud providers');
            }
            return response.json();
          })
          .then(data => {
            try {
              console.log("Fetched cloudProviders:", data);

              this.providerObject = data;
              this.findRecommendedProvider();
            } catch (error) {
              this.$router.push({
                name: 'FailedProcess',
                query: { data: encodeURIComponent(JSON.stringify({ message: "Failed To Fetch Cloud Providers. Please Try Again." })) }
              });
            }
          })
    },
    findRecommendedProvider() {
      console.log(this.assessmentObject)
      console.log(this.providerObject)
      if (!this.assessmentObject || !this.assessmentObject.providerScore || !this.providerObject) {
        console.error("Missing data for recommendation");
        return;
      }

      const providerScores = this.assessmentObject.providerScore;
      let maxScore = -Infinity;
      let bestProviderId = null;

      for (const [providerId, score] of Object.entries(providerScores)) {
        if (score > maxScore) {
          maxScore = score;
          bestProviderId = providerId;
        }
      }

      const recommended = this.providerObject.find(p => p.id === bestProviderId);

      if (recommended) {
        this.recommendedProviderObject = recommended;
        console.log("Recommended Provider:", recommended);
        this.getResources();
      } else {
        console.error("Recommended provider not found in provider list");
      }
    },
    getResources(){
      fetch('http://localhost:8080/api/getResources', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
        },
        credentials: 'include',
        body: JSON.stringify(this.assessmentObject)
      })
          .then(response => {
            if (!response.ok) {
              throw new Error('Failed to fetch resources');
            }
            return response.json();
          })
          .then(data => {
            try {
              console.log("Fetched resources:", data);

              this.resourcesObject = data;
            } catch (error) {
              this.$router.push({
                name: 'FailedProcess',
                query: { data: encodeURIComponent(JSON.stringify({ message: "Failed To Fetch Resources. Please Try Again." })) }
              });
            }
          })    },
    changeView() {
      this.view = 2;
    },
    AssessmentModule(providerId) {
      this.$router.push({
        name: 'AssessmentModule',
        query: { data: encodeURIComponent(JSON.stringify({ providerId: providerId, assessmentOBJ: this.assessmentObject, providerOBJ: this.providerObject, resourceOBJ: this.resourcesObject })) }
      });
    },
  }
}

</script>

<template>
  <div class="div-viewassessment">
    <div class="container-fluid">

      <div class="row top-header">
        <div class="col-3">
          <router-link to="/dashboard"><abbr title="Go Back To Dashboard"><span class="hvricn bi bi-arrow-left-short"></span></abbr></router-link>
        </div>
        <div class="col-9"></div>
      </div>
      <div class="body-content">

        <div class="recommendedDIV center row" v-if="view===1">
          <div class="col-1"></div>
          <h1 class="col-10">We recommend that <strong> {{recommendedProviderObject.name}} </strong> is the best cloud provider for the job</h1>
          <div class="col-1"></div>
          <div class="col-2"></div>
          <p class="col-8 center">Why? {{recommendedProviderObject.description}}</p>
          <div class="col-2"></div>
          <div class="col-5"></div>
          <button class="btn btn-success col-2" @click="changeView()">See More</button>
          <div class="col-5"></div>
        </div>

        <div class="allProviderDIV row" v-if="view===2">
          <div class="col-lg-2"></div>
          <div class="card recommended col-lg-8" @click="this.AssessmentModule(recommendedProviderObject.id)">
            <div class="card-body row">
            <h4 class="col-3">{{recommendedProviderObject.name}}</h4>
            <div class="col-9 row">
              <div class="col-12"
                  v-for="resource in resourcesObject.filter(r => r.providerId === recommendedProviderObject.id)"
                  :key="resource.id"
              >
                <h5>{{ resource.resourceName }}</h5>
                <p><strong>Service:</strong> {{ resource.service }}</p>
                <p><strong>Category:</strong> {{ resource.category }}</p>
                <hr>
              </div>
              <h5 class="col-12">Click For More</h5>
            </div>
            </div>
          </div>
          <div class="col-lg-2 space"></div>

          <div class="center col-12 row" v-for="provider in providerObject.filter(p => p.id !== recommendedProviderObject.id)"
               :key="provider.id">
            <div class="col-lg-3"></div>
            <div class="card col-lg-6" @click="this.AssessmentModule(provider.id)">


              <div class="card-body row">
                <h4 class="col-3">{{ provider.name }}</h4>
                <div class="col-9 row">
                  <div class="col-12"
                       v-for="resource in resourcesObject.filter(r => r.providerId === provider.id)"
                       :key="resource.id">
                    <h5>{{ resource.resourceName }}</h5>
                    <p><strong>Service:</strong> {{ resource.service }}</p>
                    <p><strong>Category:</strong> {{ resource.category }}</p>
                    <hr>
                  </div>
                  <h5 class="col-12">Click For More</h5>
                </div>
              </div>

            </div>
            <div class="col-lg-3 space"></div>
          </div>


        </div>
      </div>

    </div>
  </div>
</template>

<style scoped>
.div-viewassessment {
  background-color: #DBEBED;
  padding: 20px;
  height: 100vh;
  overflow-y: auto;
}

.center {
  justify-content: center;
  align-items: center;
  text-align: center;
  margin-top: 2em;
}

.space {
  margin-bottom: 2em;
}

.extra-info {
  display: none;
  margin-top: 10px;
  color: white;
}

.card:hover, .extra-info{
  background-color: #047076;
  color: white;
  transform: scale(1.05);
  transition: background-color 0.3s ease, transform 0.3s ease;
  display: block;

}

/*-----------------------------------------------------------------*/
h1 {
  font-size: 3em;
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
  font-size: .9em;
}
</style>