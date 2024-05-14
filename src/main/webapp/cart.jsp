<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Cart Page</title>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
   <link rel="stylesheet" type="text/css" href="home.css">
   <style>
   #itemlist{
  
      padding:30px;
   }
     .product{
        border: 1px solid #ccc;
        margin-bottom: 10px;
        margin-left:40px;
        margin-right: 40px;
        padding:10px;
        display:flex;
        flex-direction:row;
       justify-content: space-between;
        
     }
     .imagediv{
        height:200px;
        width:550px;
        display:flex;
        flex-direction:row;
     }
     .imagediv img{
         height:100%;
         width:200px;
         padding-right:10px;
     }
     .product p{
         font-size:20px;        
        }
     .qpdiv{
     
     display:flex;
    
     }
     .pdiv{ 
     
      padding-top:70px;
     }
    .quantity {
    
    display: flex;
    align-items: center;
    }
    .quantity input{
      width:20px;
    }
    .quantity button {
       margin: 0 5px;
       padding: 10px;
       border-radius:100px;
       border:none;
       background-color:#007bff;
       font-size: 16px;
       cursor: pointer;
       
    }
    
}
   </style>
</head>

<body>
   <div class="cartdiv">
      <h1>Shopping Cart</h1>
   </div>
   <div id="itemlist" ></div>
   <div class="cartdiv">
       <button class="cart-button" id="checkout" >Checkout</button>
   </div>
   <script>
   $(document).ready(function() {
	   var pincode=window.prompt("Enter the pincode");
	   checkingservice(); 
	   function checkingservice(){
		   $.ajax({
			   url:"checkingservice",
			   type:"GET",
			   data: {
			        'pin_code': pincode // Pass the pincode as a parameter to the server
			    },
			   success:function(data){
				   if(data==null){
					   window.alert("product are delivered to that service");
					   cartdisplaying();
					   $("#checkout").prop("disabled", false);
				   }else{
					  window.alert("Some products are not able to transported to that service they are:"+data);
					  cartdisplaying();
					  $("#checkout").prop("disabled", true).css({
						    "opacity": "0.6",
						    "cursor": "not-allowed"
						});
				   }
				   
			   },
			   error:function(xhr,status,error){
				   console.error("Error cann't invoke the data",error);
				   alert("Error feteching data ")
			   }
		   })
	   }
	   
	   function cartdisplaying(){
		   $.ajax({
		 		  url:"CartAdding",
		 		  type:"GET",
		 		  dataType:"json",
		 		  success:function(data){
		 			  console.log(data);
		 			  addingdatalist(data);
		 		  },
		 		  error: function(xhr, status, error) {
		               console.error("Error fetching data:", error);
		               alert("Error fetching data. Please try again.");
		           }
		 	  });
			   
	   }
	  
	   function itemcomponent(product){
		   var productDiv=$('<div>').addClass('product');
		   
		   //adding image
		   var imagediv=$('<div>').addClass('imagediv');
		   var image=$('<img>').attr('src', product.image);
		   imagediv.append(image);
		   
		   var title=$('<p>').text('Product name: ' + product.prod_title);
		   imagediv.append(title);
		   
		   var qpdiv=$('<div>').addClass('qpdiv');
		   var pdiv=$('<div>').addClass('pdiv');
		   var price=$('<p>').addClass('price').text(product.prod_price);
		   pdiv.append(price);
		   qpdiv.append(pdiv);
		   
		   var quantityDiv = $('<div>').addClass('quantity');
		   var minusButton = $('<button>').text('-');
		   var quantityInput = $('<input>').attr('type', 'text').val(product.quantity);
		   var plusButton = $('<button>').text('+');
		   var removebutton=$('<button>').css({"background-color":"red"});
		   var span = $('<span>').html("&#128465;");
		   
		   span.css({
			    "font-size": "32px",   // Increase font size to 24 pixels
			    "background-color": "red"  // Add red background color
			});
		   removebutton.append(span);
		   quantityDiv.append(minusButton, quantityInput, plusButton,removebutton);
		   qpdiv.append(quantityDiv);
		  
		   	
		   minusButton.click(function(){
			   var currentval=parseInt(quantityInput.val());
			   quantityInput.val(currentval-1);
			   updatingsession(product.product_id,currentval-1);
			   if(parseInt(quantityInput.val())==0){
				   updatingsession(product.product_id,0);
				   productDiv.remove();
				   checkingservice();
			   }
		   })
		   plusButton.click(function(){
			   var currentval=parseInt(quantityInput.val());
			   quantityInput.val(currentval+1);
			   updatingsession(product.product_id,currentval+1);
		   }) 
		   removebutton.click(function(){
			   updatingsession(product.product_id,0);
			   productDiv.remove();
			   checkingservice();
		   })
		   productDiv.append(imagediv);
		   productDiv.append(qpdiv);
		   return productDiv
		   
		   
	   }
	   
	   function addingdatalist(data){
		   var listdiv=$("#itemlist");
		   listdiv.empty();
		   
		   data.forEach(function(product){
			   var productcomponent=itemcomponent(product);
			   listdiv.append(productcomponent);
		   })
	   }
	   
	   function updatingsession(pid,pmode){
		   $.ajax({
			   url:'UpdateCart',
			   type:'POST',
			   data:{'p_id':pid,'p_mode':pmode},
			   success:function(data){
				   console.log(data);
			   },
			   error:function(xhr,status,error){
				   console.log("error");
			   }
		   });
	   }
	   $("#checkout").click(function(){
		   
		   window.location.href="checkout.jsp";
	   })
      })
   
   </script>
</body>
</html>