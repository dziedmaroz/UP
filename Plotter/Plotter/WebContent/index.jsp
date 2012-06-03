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
	<jsp:include page="plotspace.jsp"/>		 
</div>
	 
</body>
</html>