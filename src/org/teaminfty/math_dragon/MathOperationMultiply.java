package org.teaminfty.math_dragon;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

import android.graphics.Canvas;
import android.graphics.Rect;

public class MathOperationMultiply extends MathBinaryOperationLinear
{
	public MathOperationMultiply(int defWidth, int defHeight)
    { super(defWidth, defHeight); }
	
	public MathOperationMultiply(MathObject A, MathObject B, int defWidth, int defHeight)
    { 
		super(defWidth, defHeight);
		this.setChild(0, A);
		this.setChild(1,B);
    }
	
	 @Override
	    public IExpr eval() throws EmptyChildException
	    {
	        // Check if the children are not empty
	        this.checkChildren();
	        
	        // Return the result
	        return F.Times(getChild(0).eval(), getChild(1).eval());
	    }
	 @Override
	    public double approximate() throws NotConstantException, EmptyChildException
	    {
	        // Check if the children are not empty
	        this.checkChildren();
	        
	        // Return the result
	        return getChild(0).approximate() * getChild(1).approximate();
	    }

	    @Override
	    public void draw(Canvas canvas, int maxWidth, int maxHeight)
	    {
	        // Get the bounding box
	        final Rect operator = getOperatorBoundingBoxes(maxWidth, maxHeight)[0];
	        
	        // Draw the operator
	        canvas.save();
	        canvas.translate(operator.left, operator.top);
	        operatorPaint.setColor(this.getColor());
	        operatorPaint.setAntiAlias(true);
	        canvas.drawCircle(operator.width() / 2, operator.height() / 2, operator.width() / 7, operatorPaint);
	        canvas.restore();
	        
	        drawLeft(canvas, getChildBoundingBox(0, maxWidth, maxHeight));
	        drawRight(canvas, getChildBoundingBox(1, maxWidth, maxHeight));
	    }
}
