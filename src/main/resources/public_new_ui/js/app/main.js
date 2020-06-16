
$( document ).ready(function() {
    $( "#accordion" ).accordion();
});

function fetchTrackingData() {
	if (!$('.search-text').val())
        return;
    //search text have some value https://jsonkeeper.com/b/
    var template = '',
        //requestURL = "https://jsonkeeper.com/b/" + $('.search-text').val(); 
        getOrderUrl = "http://localhost:8085/order/11"; 
		trackOrderUrl = "http://localhost:8085/trackOrder/" + $('.search-text').val()+"?type="+$("input[name='user-type']:checked").val();  
		

	$.ajax({
     type : "GET",
     url : trackOrderUrl,
	 dataType : "json"
     }).done(function(response) {
             
			  $("#accordion").html('');
        var responseData = response.data;
        if (responseData.id) {
            var index=0;

			template = template + 
			`<h2>Order #${responseData.id}</h2>
			<p>Date ordered ${responseData.createdDate}</p>
			<p>Order status ${responseData.orderStatus}</p>`;
				
				responseData.attributes.map(attribute => {
				
				template	= template + `<h2><b>${attribute.fulfillmentType}</b></h2>`;
				
				if(attribute.deliveryAddress){
				
				template	= template + `<p>DELIVERY ADDRESS</p>
				<p>${attribute.deliveryAddress.houseName},${attribute.deliveryAddress.addressLine1}</p>
				<p>${attribute.deliveryAddress.addressLine2},${attribute.deliveryAddress.city}</p>
				<p>${attribute.deliveryAddress.county},${attribute.deliveryAddress.postalCode}</p>`;
				
				}
					
				
            attribute.deliveryGroups.map(group => {
                var currentStatus = group.currentStatus,
                trackingUrl = group.trackingUrl,
                statusList = group.lifeCycles,
                currentStatus = '',
                currentRefType='',
                currentRefNumber = '';
                index = index+1;
				
				 template = template + 
                    `<div><p class="red">Estimated delivery date ${group.deliveryDate}</p>`;
					
					
				group.lines.map(lineItem => {
					
					template = template + `
					
					<p><img src="images/pic01.jpg" alt="" width="50" height="50" class="alignleft">${lineItem.productName}<br/>Product code : ${lineItem.ean}</p>			
					`
				
					
				})
				
				template = template + `<div class="container">
                            <ul class="progressbar">`;
				
				group.lifeCycles.map(lifeCycle => {
					
					if(lifeCycle.completed) {
						
					template = template +	`<li class="complete" style="width :25%">${lifeCycle.status}<span class="date">${lifeCycle.date}</span></li>`;
					
					     currentRefType = lifeCycle.refernceType;
                        currentRefNumber = lifeCycle.refernceNumber;
						
					} else {
						
						template = template + `<li style="width :25%">${lifeCycle.status}</li>`;
					}
					
					
								
					
				})
			
		template = template + 	`</ul>`;		
				
			template = template + `<p class="info"><span></span><strong>Order Reference : <em>${currentRefType}</em></strong></p>
                            <p class="info"><span></span><strong>Order Reference number: <em>${currentRefNumber}</em></strong></p>`
							
							
				 if (trackingUrl) {
                    template = template + 
                    `<p class="info-track"><span></span><strong>Track your Order from:</strong> <a href="${trackingUrl}">${trackingUrl}</a></p>`;
					
                }			
							
                    template = template +  `</div></div>`;
				
			
     
            })
			
		})

            $("#content_right").html(template);

        }
        else {
            $("#accordion").html( `<p class="error"><span></span>No order details found</p>`);
        }
             
          }).fail(function(error) {
           // it failed.
		    $("#accordion").html(`<p class="error"><span></span>Error in fetching data</p>`);
          });
	
}

$( '.search-button' ).click(function(e) {
    e.preventDefault();
	fetchTrackingData();
    
});

$("input[name='user-type']" ).change(function(e) {
	fetchTrackingData();
    
});