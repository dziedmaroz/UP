<%@page import="eval.Eval"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!--[if IE]>
			<script type="text/javascript" src="js/flotr/lib/excanvas.js"></script>
			<script type="text/javascript" src="js/flotr/lib/base64.js"></script>
		<![endif]-->
<script type="text/javascript" src="js/flotr/lib/canvas2image.js"></script>
<script type="text/javascript" src="js/flotr/lib/canvastext.js"></script>
<script type="text/javascript" src="js/flotr/lib/prototype-1.6.0.2.js"></script>
<script type="text/javascript" src="js/flotr/flotr-0.2.0-alpha.js"></script>
<link rel="stylesheet" type="text/css" href="css/style.css" />
<title>Insert title here</title>
</head>
<body>
<%!
   Eval eval = new Eval("x^2"); 
   String plot = eval.getTable();
%>
	<div id="promt">
		<input style="width: 95%;">
		<button style="width: 4%;">=</button>
	</div>
	<div id="plotspace">
		<div id="plot"></div>

		<form name="image-download" action="" onsubmit="return false">
			<button name="to-image"
				onclick="f.saveImage('png', null, null, true)">To Image</button>
			<button name="download" onclick="f.saveImage('png')">Download</button>
			<button name="reset" onclick="f.restoreCanvas()">Reset</button>
		</form>
	</div>
	<script type="text/javascript">
			var f;
			/**
			 * Wait till dom's finished loading.
			 */
			document.observe('dom:loaded', function(){
	/**
	 * Fill series d1 and d2.
	 */
	var d1 = [<%= plot %>];
    
	/**
	 * Draw the graph.
	 */
    f = Flotr.draw($('plot'), [ d1 ]);
 
				
				if (Prototype.Browser.IE) {
					var form = $(document.forms['image-download']);
					form.disable().insert({top: "Your browser doesn't allow you to use this feature, sorry :(<br />"});
				}
			});
		</script> 
</body>


</html>