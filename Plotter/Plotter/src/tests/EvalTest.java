/**
 * 
 */
package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import eval.Eval;

/**
 * @author lucian
 *
 */
public class EvalTest {

    /**
     * Test method for {@link eval.Eval#Eval(java.lang.String)}.
     */
    @Test
    public void testEval() {
	assertNotNull(new Eval(""));
    }

    /**
     * Test method for {@link eval.Eval#tryParse()}.
     */
    @Test
    public void testTryParse() {
	Eval eval = new Eval ("2*x+3");
	assertEquals(true, eval.tryParse());
    }

    /**
     * Test method for {@link eval.Eval#getTable()}.
     */
    @Test
    public void testGetTable() {
	Eval eval = new Eval ("2*x+3");
	if (eval.tryParse())
	{
	    assertNotSame("", eval.getTable());
	}
	else
	{
	    fail("cant parse");
	}	
    }

}
