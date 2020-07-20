/*$( document ).ready(function() {
    $( "#accordion-cc" ).accordion();
    $( "#accordion-hd" ).accordion();
});*/
function createGroupItemTemplate(groupItem) {
    var index=0;
        innerTemplate = '';
		outerTemplate = '';
		deliveryGroupStatus = '';
        groupItem.deliveryGroups.map (lineItem => {
            var currentStatus = lineItem.currentStatus,
            deliveryGroupStatus=lineItem.currentStatus,
            trackingUrl = lineItem.trackingUrl,
            statusList = lineItem.lifeCycles,
            currentStatus = '',
            currentRefType='',
            currentRefNumber = '';
            index = index+1,           
            accordion_selector = "";
			
            /*if (groupItem.fulfillmentType.toLowerCase() == "home delivery") {
                accordion_selector = "accordion-hd";
                outerTemplate = `<h2 class="hd"><span></span>Home Delivery</h2>
                                 <div id="${accordion_selector}" class="accordion-content">`;				
            } else {
                accordion_selector = "accordion-cc";
                outerTemplate = `<h2 class="cc"><span></span>Click &amp; Collect</h2>
                                 <div id="${accordion_selector}" class="accordion-content">`;
            }*/
            outerTemplate = `<h2><span></span>${groupItem.fulfillmentType}</h2>
                                 <div class="accordion-content">`
            
            innerTemplate = innerTemplate + 
                `<h3 class="accordion-header">${lineItem.deliveryGroupMsg}</h3>
                <div class="inner-content">
                    <div class="container">
                        <ul class="progressbar">`;
            statusList.map(statusItem => {
                var statusWidth = 100/statusList.length;
                var date = statusItem.date? statusItem.date : '';
                if (statusItem.completed) {
                    innerTemplate = innerTemplate + `<li class="complete" style = "width :${statusWidth}%">${statusItem.status}<span class="date">${date}</span></li>`;
                    currentRefType = statusItem.refernceType;
                    currentRefNumber = statusItem.refernceNumber;
                }
                else if(currentStatus) {
                    innerTemplate = innerTemplate + `<li style = "width :${statusWidth}%">${statusItem.status}<span class="date">${date}</span></li>`;
                }
                else {
                    innerTemplate = innerTemplate + `<li style = "width :${statusWidth}%">${statusItem.status}</li>`;
                }
                currentStatus = statusItem.completed;          
            })
            innerTemplate = innerTemplate +`</ul></div>`
			
			
             	
             	var prodInfolist = '';
			innerTemplate = innerTemplate + 
							`<table class="prod-table"><thead><tr><th>Product Name</th><th>Quantity</th></tr></thead><tbody>`;
            lineItem.lines.map(product => {
				innerTemplate = innerTemplate + 
								`<tr>
									<td>${product.productName}</td>
									<td>${product.quantity}</td>      
								</tr>`
                if(product.info) {
                	prodInfolist = prodInfolist + `<p class="info"><span></span><strong><em>${product.info}</em></strong></p>`;
                }
                 
            })
            innerTemplate = innerTemplate + `</tbody></table>` + prodInfolist;
			var selectedUserType=$("input[name='user-type']:checked").val();
            
          
            if(selectedUserType=='internal' && Object.keys(lineItem.references).length!=0){         	
           	
	              Object.keys(lineItem.references).forEach(function(key) {
		    		var value = lineItem.references[key];
		    		innerTemplate = innerTemplate + 
	                       `<p class="info"><span></span><strong>${key} : <em>${value}</em></strong></p>`
				});
                }
             
            
            if (trackingUrl) {
                innerTemplate = innerTemplate + 
                `<p class="info-track"><span></span><strong>Track `;
                 if(selectedUserType=='external'){
                 	innerTemplate = innerTemplate + `your `;
                 }
				innerTemplate = innerTemplate + `Order from:</strong> <a href="${trackingUrl}">${trackingUrl}</a></p>`;
            }
            innerTemplate = innerTemplate + `</div>` // inner content div
    })
    return  (outerTemplate + innerTemplate + `</div>`);
    //$(accordion_selector).accordion('destroy');
}

function fetchTrackingData() {
    if (!$('.search-text').val())
        return;
    //search text have some value https://jsonkeeper.com/b/
    
        //requestURL = "https://jsonkeeper.com/b/" + $('.search-text').val(); 
        requestURL = "http://localhost:8085/trackOrder/" + $('.search-text').val()+"?type="+$("input[name='user-type']:checked").val(); 

 
    $.ajax({
     type : "GET",
     url : requestURL,
     dataType : "json"
     }).done(function(response) {
             
        $("#data-content").html('');
        //$("#accordion-cc").html('');
        $("#error-msg").html('');
        var responseData = response.data;
            template = "";
        if (responseData) {
            responseData.attributes.map(groupItem => {
                template = template + createGroupItemTemplate(groupItem);
        });
            $("#data-content").html(template);
            $(".accordion-content").each(function(){
                $(this).accordion({
                    heightStyle: "content",
                    collapsible: true,
                    animate: 200
                })
            })
        }
        else {
            $("#data-content").html('');
            //$("#accordion-hd").html('');
            //$("#accordion-cc").html('');
            //$(".hd").addClass('hidden');
            //$(".cc").addClass('hidden');
            $("#error-msg").html( `<p class="error"><span></span>No order details found</p>`);
        }             
          }).fail(function(error) {
           // it failed.
           $("#data-content").html('');
            //$("#accordion-hd").html('');
            //$("#accordion-cc").html('');
            //$(".hd").addClass('hidden');
            //$(".cc").addClass('hidden');
            $("#error-msg").html(`<p class="error"><span></span>Error in fetching data</p>`);
          });
    
}

$( '.search-button' ).click(function(e) {
    e.preventDefault();
    fetchTrackingData();
    
}); 

$("input[name='user-type']" ).change(function(e) {
    fetchTrackingData();    
});