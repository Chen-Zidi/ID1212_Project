###KTH-ID1212-project Client side application

## Introduction
This is a client-server chat application with user account management functionality. Users can send either files or messages in chat rooms. In order to enter a chat room, the user need to know the password for the room. Files and messages are room based. The project frontend is based on Spring boot, MVC, web socket, and RESTful APIs. The connection between frontend and backend is encrypted (https).

## Running environment
Please do not use Chrome. 
The web app should run well on firefox and Edge. Not sure about other browsers.
In order to run on firefox and Edge, you first need to access https://localhost:10086/hello 
and trust the certificate (which should be alerted as unsafe, but it is actually
can be trustable from us). Then you can access to localhost:8088/login to login or register.


## File Download directories
The created dirs are at the user dir(eg. C:/User/your username)
\ProjectDownloadFiles: for downloaded files
\TempFiles: for the files that you upload
\ProjectFiles: for backend File storage

## reference: 
1. login.html, register.html and signin.css are from bootstrap official website. 
The authors are: Mark Otto, Jacob Thornton, and Bootstrap contributors.
There are changes from us, and the logo is from us.

2. Part of index.html and sticky-footer-navbar.css are from the official website of bootstrap. 
Authors are Mark Otto, Jacob Thornton, and Bootstrap contributors.
Part of index.html and font-awesome.css are from: https://bootsnipp.com/snippets/1ea0N.
The author is sunil8107(Sunil Rajput).
We combined these two html files and adapted to our ideas.
