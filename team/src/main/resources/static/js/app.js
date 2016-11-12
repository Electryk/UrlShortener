$(document).ready(
    function() {
        $("#shortener").submit(
            function(event) {
                event.preventDefault();
                $.ajax({
                    type : "POST",
                    url : "/link",
                    data : $(this).serialize(),
                    success : function(msg) {
                        $("#result").html(
                            "<div class='alert alert-success lead'><a target='_blank' href='"
                            + msg.uri
                            + "'>"
                            + msg.uri
                            + "</a></div>");
                    },
                    error : function() {
                        $("#result").html(
                                "<div class='alert alert-danger lead'>ERROR</div>");
                    }
                });                
            });
        
    });

function getPublicIp() {
	$.getJSON("https://api.ipify.org?format=jsonp&callback=?",
      function(json) {
  		console.log(json)
        var data = {
  			ip: json.ip
  		};
  		$.ajax({
            type : "POST",
            url : "/publicIp",
            data : data,
            dataType: "json",
            success: function(msg) {
            	console.log(msg);
            	$("#ip").html(
                "<div class='alert alert-success lead'>" + msg.ip + " - " + msg.city + "</div>");
            },
            error : function() {
                $("#ip").html(
                        "<div class='alert alert-danger lead'>ERROR</div>");
            }
        });

     });
};