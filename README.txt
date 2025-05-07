Project Name: CUMULUS – Cloud Migration Assessment Tool
Technologies: Spring Boot (Backend), Vue.js (Frontend)


-----------------------------------------
PROJECT STRUCTURE
-----------------------------------------

/cumulus
│
├── backend       → Spring Boot API
└── frontend      → Vue.js frontend


-----------------------------------------
HOW TO RUN
-----------------------------------------


\\\\\\\\\\\\\\\\\ BACKEND \\\\\\\\\\\\\\\\\

Requirements:

	- Java 17+

	- Maven

Terminal Code:

	cd backend
	mvn clean install
	mvn spring-boot:run

The backend will start on: http://localhost:8080

\\\\\\\\\\\\\\\\\ FRONTEND \\\\\\\\\\\\\\\\\

Requirements:

	- Node.js 18+
	
	- npm

Terminal Code:

	cd frontend
	npm install
	npm run serve

http://localhost:3000

-----------------------------------------
ENVIRONMENT VARIABLES
-----------------------------------------

This project requires an OpenAI API key to function correctly.

In your backend, create a .env file or set the environment variable in your system:

	OPENAI_API_KEY=your_openai_key_here

Make sure your application.properties file contains:

	openai.api.key=${OPENAI_API_KEY}

This allows the application to securely access the OpenAI API without hardcoding the key.


-----------------------------------------
ADDED NOTES
-----------------------------------------

Not all files in this application are functional. Due to limited time, I could not clean project file structure. Please keep this in mind.

This connects to a collection in MongoDB. Network access is global but message me in case of any issues connecting.

