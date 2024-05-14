<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.ArrayList" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Shopping Cart</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
<link rel="stylesheet" type="text/css" href="home.css">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<style>
   .product {
            border: 1px solid #ccc;
            margin: 10px;
            padding: 10px;
            width: 300px;
            height:350px;
            display: inline-block;
            text-align:center;
            
        }
        .product-image{
           width:300px;
           height:200px;
        }
        .product-image  img{
           width:100%;
           height:200px;
        }
        
        .product p{
         font-size:14px;        
        }
        
        .product button{
           background-color: #007bff;
           color: #ffffff;
           border: none;
           padding: 8px 16px;
           border-radius: 5px;
           font-size: 16px;
           cursor: pointer;
        }
</style>
</head>
<body>
  <div class="headerdiv">
  <button class="register-button" onclick="location.href='signup.jsp'">Register</button>
     <p>Shopping Cart</p>
     <div class="hdivoptions">
         <select id="CategoryList">
            <option value="All" selected>All Categories</option>
            <% 
            // Retrieve product categories from the request scope
            request.getRequestDispatcher("/ProductCategory").include(request,response);
            ArrayList<String> productCategories = (ArrayList<String>)request.getAttribute("prodcat");
            if(productCategories != null) {
                for (String category : productCategories) { %>
                    <option value="<%= category %>"><%= category %></option>
                <% }
            } %>
         </select>
         <select id="pricelist">
            <option value="All" selected>Price</option>option>
            <option value="0" >Below 500</option>
            <option value="500" >500-1000</option>
            <option value="1000" >1000-10000</option>
            <option value="10000" >Above 10000</option>
         </select>
         <%
if (session.getAttribute("LOGGEDIN") != null && session.getAttribute("LOGGEDIN").equals("yes")) {
    String username = (String) session.getAttribute("USERNAME");
%>
    <button class="login-button" id="OpenLogin" style="display: none;">Login</button>
    <span> <%= username %></span>
    <!-- Add a logout link here if needed -->
<%
} else {
%>
    <button class="login-button" id="OpenLogin">Login</button>
<%
}
%>

          
         <button class="cart-button" id="OpenCart"><i class="fas fa-shopping-cart"></i></button>
     </div>
  </div>
  <h1>Welcome to my Shopping Cart</h1>
  <div id="productcard"> 
      
      
  </div>
  <div class="pagination">
  <button id="prev">Previous</button>
  <input id="pagevalue" value="1"/>
  <button id="next">Next</button>
</div>
  <script>
     $(document).ready(function(){
    	 var pagenumber=0
    	 loaddata("All","All");
    	 var datalength;
    	 function loaddata(selectedcategory,selectedprice){  
    		 
    		 $.ajax({
   			  url:"ProductServlet",
   			  type:"POST",
   			  data:{category:selectedcategory,price:selectedprice,page:pagenumber},
   			  success:function(data){
   				  console.log(data);
   				  datalength=data.length;
   				  displayingproducts(data);
   			  },
   			  error:function(xhr, status, error) {
     	            console.error(status, error);
     	        }
   		  })
    	 }
    	 $("#next").click(function(){
    		 if(datalength<6){
    			 alert("no more elements");
    		 }else{
    			 pagenumber=pagenumber+1;
        		 newdata();
        		 $("#pagevalue").val(pagenumber+1);
    		 }
    		 
    	 })
    	 $("#prev").click(function(){
    		 if(pagenumber!=0){
    			 pagenumber=pagenumber-1;
    			 newdata();
    			 $("#pagevalue").val(pagenumber-1);
    		 }else{
    			 alert("Your are at page 1");s
    		 }
    		 
    		 
    	 })
    	 $("#CategoryList, #pricelist").change(function(){
   		    
   		    newdata();
   		});
    	 function newdata(){
    		 var selectedCategory = $("#CategoryList").val();
    		 var selectedPrice = $("#pricelist").val();
    		 loaddata(selectedCategory, selectedPrice);
    	 }
    	 function creatingcomponents(product){
   		  var productDiv = $('<div>').addClass('product');

   		// Create a div for the image
   		var imageDiv = $('<div>').addClass('product-image');

   		// Create the image element
   		var image = $('<img>').attr('src', product.image);

   		// Append the image to the image div
   		imageDiv.append(image);

   		// Create a paragraph for the product title
   		var ptitle = $('<p>').text('Product name: ' + product.prod_title);

   		// Create a paragraph for the price
   		var price = $('<p>').text('Price: $' + product.prod_price);

   		// Create the button
   		var button = $('<button>').text('Add to Cart');

   		// Add click event listener to the button
   		button.click(function(){
   			addingcart(product);
   		})

   		// Append the elements to the productDiv
   		productDiv.append(imageDiv);
   		productDiv.append(ptitle);
   		productDiv.append(price);
   		productDiv.append(button);
   	    return productDiv;
   	  }
   	  function displayingproducts(data){
   		  var $p_data = $('#productcard');
   		  $p_data.empty(); // Clear previous content

   		  data.forEach(function(product) {
   		      var productComponent = creatingcomponents(product);
   		      $p_data.append(productComponent);
   		  });
   		  
   	  }
   	  function addingcart(product){
   		  
   		  $.ajax({
   			  url:'CartAdding',
   			  type:'POST',
   			  data:{prod:product},
   			  success:function(data){
   				  alert(data);
   			  },
   			  error:function(xhr, status, error) {
       	            console.error(status, error);
       	        }
   		  })
   	  }
   	  $("#OpenCart").click(function(){
		  window.location.href = 'cart.jsp';
	  })
	   $("#OpenLogin").click(function(){
		  window.location.href = 'Login.jsp';
	  })
     })
  
  </script>
  
 
</body>
</html>