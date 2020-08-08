
$( document ).ready(function() {
    $( "#accordion" ).accordion();
});

function fetchTrackingData() {
	if (!$('.search-text').val())
        return;
    //search text have some value https://jsonkeeper.com/b/
    var template = '',
        //requestURL = "https://jsonkeeper.com/b/" + $('.search-text').val(); 
        requestURL = "/trackOrder/" + $('.search-text').val()+"?type="+$("input[name='user-type']:checked").val(); 

	$.ajax({
     type : "GET",
     url : requestURL,
	 dataType : "json"
     }).done(function(response) {
             
			  $("#accordion").html('');
        var responseData = response.data;
        if (responseData.id) {
            var index=0;
            responseData.attributes.map(lineItem => {
                var currentStatus = lineItem.currentStatus,
                trackingUrl = lineItem.trackingUrl,
                statusList = lineItem.lifeCycles,
                currentStatus = '',
                currentRefType='',
                currentRefNumber = '';
                index = index+1;

                template = template + 
                    `<h3 class="accordion-header">Shipment ${index} of ${responseData.attributes.length}</h3>
                    <div class="inner-content">
                        <div class="container">
                            <ul class="progressbar">`;
                statusList.map(statusItem => {
                    var statusWidth = 100/statusList.length;
                    var date = statusItem.date? statusItem.date : '';
                    if (statusItem.completed) {
                        template = template + `<li class="complete" style = "width :${statusWidth}%">${statusItem.status}<span class="date">${date}</span></li>`;
                        currentRefType = statusItem.refernceType;
                        currentRefNumber = statusItem.refernceNumber;
                    }
                    else if(currentStatus) {
                        template = template + `<li style = "width :${statusWidth}%">${statusItem.status}<span class="date">${date}</span></li>`;
                    }
                    else {
                        template = template + `<li style = "width :${statusWidth}%">${statusItem.status}</li>`;
                    }
                    currentStatus = statusItem.completed;          
                })
                template = template + 
                            `</ul></div>
                            <p class="info"><span></span><strong>Order Reference : <em>${currentRefType}</em></strong></p>
                            <p class="info"><span></span><strong>Order Reference number: <em>${currentRefNumber}</em></strong></p>
                             `
                if (trackingUrl) {
                    template = template + 
                    `<p class="info-track"><span></span><strong>Track your Order from:</strong> <a href="${trackingUrl}">${trackingUrl}</a></p>`
                }
                template = template + `</div>` // inner content div
            })
            $('#accordion').accordion('destroy');
            $("#accordion").html(template);
            $( "#accordion").accordion({
                heightStyle: "content",
                collapsible: true,
                animate: 200
            });
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