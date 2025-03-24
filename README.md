#  **CUMULUS - Workstream Assessment Module**  
A web application designed to help small to medium-sized businesses transition to cloud environments using the Well-Architected Framework. This module allows users to assess workstreams, estimate costs, plan migrations, and provide justifications for cloud provider selection.

---

## **Project Structure**  

### **Frontend:** Vue.js (Bootstrap 5)  
- **Components:**  
  - `AssessmentModule.vue` — Displays a general summary of the results from assessing a users input.  
  - `AssessWorkstream.vue` — Allows user to input customisations in order to create a workstream - a guided plan on creating an ideal infrastructure in the cloud.  
  - `ContactUs.vue` — Display a form for the user to contact an Admin via email.  
  - `FaileedProcess.vue` — Displays errors to the user.  
  - `Homepage.vue` — Landing page for all logged out users.  
  - `ProcessWorkstream.vue` — Displays the loading process of assessing, saving and loading the workstream and inputs for the user.  
  - `ResetPassword.vue` — Handles user reset password process (non-functional).  
  - `SignIn.vue` — Form that allows user to log in to the application.  
  - `SignUp.vue` — Form that allows user to register to the application.  
  - `TEST.vue` — Initial test page for application.  
  - `UserDashboard.vue` — Landing page for all logged in users.  
  - `ProcessWorkstream.vue` — Handles user input and displays assessment results.  

- **Styling:**  
  - **Bootstrap 5** for layout and responsiveness.  
  - In Built Vue Transitions for smooth UI interactions.

### **Backend:** Spring Boot  
- **API Endpoint:** `/api/assess-workstream` -- Receives JSON input, processes it via **OpenAI**, and returns structured assessment data.
- **API Endpoint:** `/api/assessment-module` -- Retrieves the data needed to load the assessment module via workstreamID.
- **API Endpoint:** `/api/contactus/sendemail` -- Sends form input from the user to system mail box using the GMAIL API.
- **API Endpoint:** `/api/loggedin/user` -- Retrieves the details of the user who has logged in via token.
- **API Endpoint:** `/api/process-workstream` -- Saves the workstream to the database.
- **API Endpoint:** `/api/save-inputs` -- Saves the user inputs to the database.
- **API Endpoint:** `/api/signin` -- Signs in the registered user and assigns a JWT token 
- **API Endpoint:** `/api/signup` -- Saves the inputted user details to the database.
 


