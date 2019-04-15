# questionnaire
Professional athlete hosting a Question &amp; Answer session with fans

# Run application
1. install java8
2. install mysql (user: "root", password:"") and start
3. git clone https://github.com/valentinabrui/questionnaire.git
3. ./gradlew clean build
4. ./gradlew run
5. localhost:8080

# Create session:
curl -X POST -H "Content-Type: application/json" -d '{
  "start_time": "2019-04-14T18:46Z",
  "end_time": "2019-04-14T19:46Z",
  "host_name": "user1"
}' "http://localhost:8080/qa/"

# Get a session by id:
curl -X GET -H "Content-Type: application/json" "http://localhost:8080/qa/1"

# Create a question:
curl -X POST -H "Content-Type: application/json" -d '{"text" : "what is the day today?",
"asked_by_name" : "Winnie",
"qa_id" : 1}' "http://localhost:8080/question/1"

# Create an answer:
curl -X POST -H "Content-Type: application/json" -d '{"text" : "Today, my favorite day!",
"image_url" : "http://www.imageswallpaper.in/wp-content/uploads/2017/04/Saturday-Image-4.jpg",
"answered_by" : "Pyatachok",
"question_id" : 1}' "http://localhost:8080/answer/1"

# Get list of question by session id, answeredFilter could be true/false or empty :
    without filter:
    curl -X GET "http://localhost:8080/qa/1/questions"

    answered question (answeredFilter=true):
    curl -X GET "http://localhost:8080/qa/1/questions?answeredFilter=true"

    nonanswered question (answeredFilter=false):
    curl -X GET "http://localhost:8080/qa/1/questions?answeredFilter=false"

# TODO list:
1. add more validations
2. better logging
3. dockerize
