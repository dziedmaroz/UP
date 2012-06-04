package eval;

import expr.*;

public class Eval {
    private String exp;
    private Expr expr;
    private String err ="";
    private boolean allOk = false;
    private final double UP_X = 10;
    private final double DOWN_X = -10;
    private final double STEP = 0.1;

    public Eval(String exp) {
	this.exp = exp;
    }

    public boolean tryParse() {
	try {
	    expr = Parser.parse(exp);
	    allOk = true;
	} catch (SyntaxException e) {
	    allOk = false;
	    err =e.explain();
	}
	return allOk;
    }

    public String getTable() {
	String res = "";
	try {
	    expr = Parser.parse(exp);
	    allOk = true;
	    Variable x = Variable.make("x");

	    for (double xval = DOWN_X; xval <= UP_X; xval += STEP) {
		x.setValue(xval);
		res += " [" + Double.toString(xval) + ", "
			+ Double.toString(expr.value()) + "]";
		if (xval != UP_X)
		    res += ", ";
	    }
	} catch (SyntaxException e) {
	    allOk = false;
	}

	return res;
    }

    public String getErr() {
        return err;
    }
    
    
}
