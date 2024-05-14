<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
   <h1>please check your credentials once again</h1>
   <button onclick="redirectToLogin()">Back</button>
   <script>
       function redirectToLogin() {
           // Redirect to the login page
           window.location.href = "Login.jsp";
       }
   </script>
</body>
</html>