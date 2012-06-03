<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

// Execute this when the page's finished loading
<script type="text/javascript">var f = Flotr.draw(
	$('container'), [
	{ // => first series
	    data: [ [0, 0], [1, 2], [2, 4], [3, 6], [4, 8] ],
	    label: "y = 2x",
	    lines: {show: true, fill: true},
	    points: {show: true}
	},
	{ // => second series
	    data: [ [0, 2.5], [1, 5.5], [2, 8.5], [3, 11.5], [4, 14.5] ],
	    label: "y = 2.5 + 3x"
	}]
);
</script>

<div id="container" style="width:600px;height:300px;"></div>
</body>
</html>