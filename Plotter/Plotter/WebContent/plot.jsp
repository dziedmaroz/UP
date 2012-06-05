<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="css/style.css" />

<!--[if IE]>
			<script type="text/javascript" src="js/flotr/lib/excanvas.js"></script>
			<script type="text/javascript" src="js/flotr/lib/base64.js"></script>
		<![endif]-->
<script type="text/javascript" src="js/flotr/lib/canvas2image.js"></script>
<script type="text/javascript" src="js/flotr/lib/canvastext.js"></script>
<script type="text/javascript" src="js/flotr/lib/prototype-1.6.0.2.js"></script>
<script type="text/javascript" src="js/flotr/flotr-0.2.0-alpha.js"></script>


<title>Plotter</title>
</head>
<body>

	<div id="header">Plotter|alpha</div>


	<div id="content">
		<div id="promt">
			<form method="get" action="/Plotter/">
				<input type="text" name="expr" value="${param.expr}" size="80" /> <input
					type="submit" value="=" name="submitExpr" />
			</form>
		</div>


		<div id="plotspace">
			<div id="plot">${err}</div>
			<div id="download"></div>
		</div>


		<!-- Plotting -->
		<script type="text/javascript">
		var f;		
		document.observe('dom:loaded', function(){				
			var d1 = [${plot}];				
			if (${allOk})
			{	 
				
		    	f = Flotr.draw($('plot'), [ d1 ]);
		    	f.saveImage('png',null,null,true);
		    	document.getElementById('download').innerHTML = '<a href="'+f.canvas.toDataURL()+'"> get image </a>';
		    	
		    	if (Prototype.Browser.IE)
		    	{
		    		
					var form = $(document.forms['image-download']);
					form.disable().insert({top: "Your browser doesn't allow you to use this feature, sorry :(<br />"});
				}
	    	}		 
				 
			});
	</script>
	</div>

</body>
</html>