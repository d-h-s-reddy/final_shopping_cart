<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="models.Products" %>
<%@ page import="models.Discount" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        /* Additional custom styling */
        body {
            padding: 20px;
        }
        img{
        width:100px;
        heiht:80px;
        }
        table,tr,th{
        text-align:center;
        }
        .data-table {
  width: 100%;
  border-collapse: collapse;
  border: 1px solid #ddd; /* Border color */
}

.data-table th, .data-table td {
  padding: 8px;
  text-align: left;
  border-bottom: 1px solid #ddd; /* Border color */
}

.data-table th {
  background-color: #f2f2f2; /* Header background color */
  font-weight: bold;
}

.data-table tbody tr:nth-child(even) {
  background-color: #f2f2f2; /* Even row background color */
}

.data-table tbody tr:hover {
  background-color: #ddd; /* Hover row background color */
}
#valuesdata {
  float: right; /* Or use margin-left: auto; */
  width:300px;
}
    </style>
</head>
<body>
    <% 
       ArrayList<Products> cart=(ArrayList<Products>)session.getAttribute("cart");
       request.getRequestDispatcher("/DataCalculation").include(request,response);
       Map<String,Double> hm=(Map<String,Double>)session.getAttribute("priceDetails");
      %>
 
<div class="container">
    <h2>Product Details</h2>

    <table class="table table-striped">
        <thead class="thead-dark">
            <tr>
                <th>Product Details</th>
                <th>Price</th>
                <th>Quantity</th>
                <th>GST</th>
                <th>Shipping Charge</th>
                
            </tr>
        </thead>
        <tbody>
            <% for (Products p : cart) { %>
            <tr>
                <td><img src="<%= p.getImage() %>"></td>
                <td><%= p.getProd_price() %></td>
                <td><%= p.getQuantity() %></td>
                <td><%= p.getGst() %></td>
                <td><%= p.getShipping_charge() %></td>
            </tr>
            <% } %>
        </tbody>
    </table>
    <br><br>
</div>
<div>
   <select id="discountvalues">
       <option value="">Select and discount coupon</option>
       <% request.getRequestDispatcher("/DiscountServlet").include(request,response);
   ArrayList<Discount> d = (ArrayList<Discount>)request.getAttribute("discount"); 
       if(d != null) {
                for (Discount dis: d) { %>
                    <option value="<%= dis.getDcpn_value() %>"><%= dis.getDcpn_title() %></option>
                <% }
            } %>
   </select>

</div>

<div id="discountdetails">
  
</div>
<div id="valuesdata">
</div>


<br>
 <script>
    $(document).ready(function() {
	    $("#discountvalues").change(function() {
	        var discount_value = $("#discountvalues").val();
	        $.ajax({
	            url: 'ApplyingDiscountServlet',
	            type: 'POST',
	            data: { "discount": discount_value },
	            success: function(data) {
	            	console.log(data[1]);
	                datadisplaying(data[0]);
	                pricegstdisplay(data[1]);
	                calshipping();
	            },
	            error: function(xhr, status, error) {
	                console.error(status, error);
	            }
	        });
	    });
	    function datadisplaying(data){
	    	$("#discountdetails").empty();
	    	var $table = $('<table>').addClass('data-table');
	    	  var $thead = $('<thead>');
	    	  var $tbody = $('<tbody>');

	    	  // Create table header
	    	  var $headerRow = $('<tr>');
	    	  $headerRow.append($('<th>').text('Sno'));
	    	  $headerRow.append($('<th>').text('Price'));
	    	  $headerRow.append($('<th>').text('GST'));
	    	  $thead.append($headerRow);
	    	  var count=1;
	    	  // Create table body rows
	    	  $.each(data, function(key, values) {
	    		    var $row = $('<tr>');
	    		    $row.append($('<td>').text(count));
	    		    count=count+1;
	    		    $.each(values, function(index, value) {
	    		    	 var roundedValue = parseFloat(value).toFixed(2);
	    		         $row.append($('<td>').text(roundedValue));
	    		    });
	    		    $tbody.append($row);
	    		  });

	    		  $table.append($thead).append($tbody);
	    		  // Append the table to the body
	    		  $('#discountdetails').append($table);	 
	    }
	    function  pricegstdisplay(data){
	    	$("#valuesdata").empty();
	    	var $dataContainer = $('#valuesdata');
	    	  var $table = $('<table>').addClass('data-table').attr('id', 'myDataTable');;
	    	  var $tbody = $('<tbody>');

	    	  // Loop through the data object and create table rows
	    	  $.each(data, function(key, value) {
	    	    var $row = $('<tr>');
	    	    $row.append($('<td>').text(key));
	    	    $row.append($('<td>').text(value.toFixed(2))); // Round off to 2 decimal places
	    	    $tbody.append($row);
	    	  });

	    	  // Append table body to the table
	    	  $table.append($tbody);

	    	  // Append table to the data container
	    	  $dataContainer.append($table);
	    }
	    function calshipping(){
	    	$.ajax({
	    		url:'ShippingServlet',
	    		type:'POST',
	    		dataType:"json",
	    		success:function(data){
	    			console.log(data);
	    			displayingshipping(data[0]);
	    		},
	    		error: function(xhr, status, error) {
	                console.error(status, error);
	            }
	    	});
	    }
	    function displayingshipping(data){
	    	var $tableReference = $('#myDataTable');
	    	$table.find('thead tr').append($('<th>').text('Shipping charge'));
	        $table.find('thead tr').append($('<th>').text('Shipping Gst'));
	        var $tbodyRows = $table.find('tbody tr');
	        $tbodyRows.each(function(index) {
	            // Assuming data keys are sorted and match the row index
	            var key = Object.keys(data)[index];
	            var values = data[key];

	            // Append new data to each row
	            $(this).append($('<td>').text(values[0].toFixed(2))); // New Column 1
	            $(this).append($('<td>').text(values[1].toFixed(3))); // New Column 2
	        });
	    }
	});
     
 </script>
      
</body>
</html>