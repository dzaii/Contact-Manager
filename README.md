 <a name="readme-top"></a>
<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->








<div align="center">
<h1 align="center">Contact Manager</h1>

  <p align="center">
    Simple app for managing your contacts
    <br />
    <a href="https://github.com/dzaii/Contact-Manager"><strong>Explore the docs »</strong></a>
    <br />
    <a href="https://github.com/dzaii/Contact-Manager/issues">Report Bug</a>
    ·
    <a href="https://github.com/dzaii/Contact-Manager/issues">Request Feature</a>
  </p>
</div>


<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
        <li><a href="#project-description">Project Description</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#running-the-application">Running The Application</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project
### Built With

[![Java][Java.img]][Java-url]
[![Spring][Spring.img]][Spring-boot-url]
[![Postgres][PostgreSql.img]][PostgreSql-url]
<a href='https://flywaydb.org' target="_blank"><img alt='Flyway' src='https://img.shields.io/badge/Flyway-100000?style=for-the-badge&logo=Flyway&logoColor=FF0000&labelColor=FFFFFF&color=FFFEFE'/></a>
[![Docker][Docker.img]][Docker-url]
[![Swagger][Swagger.img]][Swagger-url]
<a href='https://www.twilio.com' target="_blank"><img alt='twilio' src='https://img.shields.io/badge/twilio-100000?style=for-the-badge&logo=twilio&logoColor=FF0000&labelColor=FFFFFF&color=FFFEFE'/></a>

### Project Description
This one-developer project was part of a 5-week internship program in <a href=https://www.ingsoftware.com>IngSoftware.</a> The main task  was to create an application for managing user's contacts.
#### Base functional requirements:
* User can perform CRUD operations on his contacts
* Admin can perform CRUD operations on his contacts, all users and types of contacts.
#### Additional functional requirements:
* Implement search contact by first name, last name, email, or phone number
* Add unit tests
* Use Docker Compose to run Spring Boot and PostgreSQL
* Set Up Swagger
* Implement API call for adding bulk imports via CSV file
* Create Registration call
* Send greetings email to new users
* Implement 2 factor verification using Twilio

#### Non-functional requirements:
* Use Spring via Spring Boot
* Use PostgreSQL as the RDMBS
* Use migrations (Flyway)
* Use Spring Security
* Use Maven
* Use Java 17
<p align="right">(<a href="#readme-top">back to top</a>)</p>










<!-- GETTING STARTED -->
## Getting Started

To get a local copy up and running follow these simple example steps.

### Prerequisites

* Docker and Docker Compose. Follow <a href=https://docs.docker.com> installation instructions</a> for your OS.
* <a href=https://www.google.com/gmail/about/ >Gmail</a> account to send verification emails.
 You will have to generate <a href=https://support.google.com/accounts/answer/185833> App Password</a>. 
* <a href=https://www.twilio.com> Twilio</a> account and <a href=https://support.twilio.com/hc/en-us/articles/360033309133-Getting-Started-with-Twilio-Verify-V2>Twillio verification service.</a> 


### Running The Application


1. Clone the repo
   ```sh
   $ git clone https://github.com/dzaii/Contact-Manager.git
   ```
2. Open docker-compose.yml file in project root at, provide your credentials and save the file.
   ```
      - TWILIO_ACCOUNT_SID=
      - TWILIO_AUTH_TOKEN=
      - TWILIO_SERVICE_SID=
      - MY_EMAIL=
      - MY_PASSWORD=
   ```
3. Open your terminal, navigate to project folder and type:
   ```sh
   $ docker compose up
   ```

4. If everything was set up correctly, last line in your terminal should end with something like:
   ```
   Started ContactManagerApplication in 9.038 seconds (JVM running for 10.109)
   ```
   

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- USAGE EXAMPLES -->
## Usage

After successfully running the application.
Access your browser and navigate to http://localhost:8080/swagger-ui/ .
When asked for credentials provide:
* Username: admin@gmail.com 
* Password: Password1!

Afterwards, you should be logged in as an administrator on
the documentation page where you can try each endpoint.
All methods should be self-explanatory.

More experienced users can always use <a href=https://www.postman.com> Postman</a> to test the API.


<p align="right">(<a href="#readme-top">back to top</a>)</p>





<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to learn,
inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a
pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<p align="right">(<a href="#readme-top">back to top</a>)</p>





<!-- CONTACT -->
## Contact

Ivan Bojinović  - bojinovic.ivan@gmail.com - 

[![LinkedIn][linkedin-shield]][linkedin-url]

Project Link: [https://github.com/dzaii/Contact-Manager](https://github.com/dzaii/Contact-Manager)

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/dzaii/Contact-Manager.svg?style=for-the-badge
[contributors-url]: https://github.com/dzaii/Contact-Manager/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/dzaii/Contact-Manager.svg?style=for-the-badge
[forks-url]: https://github.com/dzaii/Contact-Manager/network/members
[stars-shield]: https://img.shields.io/github/stars/dzaii/Contact-Manager.svg?style=for-the-badge
[stars-url]: https://github.com/dzaii/Contact-Manager/stargazers
[issues-shield]: https://img.shields.io/github/issues/dzaii/Contact-Manager.svg?style=for-the-badge
[issues-url]: https://github.com/dzaii/Contact-Manager/issues
[license-shield]: https://img.shields.io/github/license/dzaii/Contact-Manager.svg?style=for-the-badge
[license-url]: https://github.com/dzaii/Contact-Manager/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/ivan-bojinovic-b7090624b
[Java-url]: https://www.java.com
[Java.img]: https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white
[Spring-boot-url]: https://spring.io/projects/spring-boot
[Spring.img]:https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white
[PostgreSql.img]: https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white
[PostgreSql-url]: https://www.postgresql.org
[Docker.img]: https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white
[Docker-url]: https://www.docker.com
[Swagger.img]: https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white
[Swagger-url]: https://swagger.io
