package org.teaminfty.math_dragon.view.math;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

/** This class draws binary operations that are written linear.
 * That is, operations which are written like '&lt;left operand&gt; &lt;operator&gt; &lt;right operand&gt;'.
 * For example: the add or subtract operation.
 */
public class MathOperationDivide extends MathBinaryOperation
{
	public static final String TYPE = "divide";
	
    /** The paint that is used for drawing the operator */
    protected Paint operatorPaint = new Paint();

    public MathOperationDivide()
    {}
    
    public MathOperationDivide(MathObject left, MathObject right)
    {
    	super(left, right);
    }
    
    public String toString()
    {
        return "(" + getLeft().toString() + "/" + getRight().toString() + ")";
    }

    @Override
    public int getPrecedence()
    { return MathObjectPrecedence.MULTIPLY; }
    
    /**
     * Returns the sizes of the bounding boxes.
     * The first rectangle is the size of the operator, the second and third rectangle are the sizes of the children.
     * 
     * @param maxWidth
     *        The maximum width the {@link MathObject} can have (can be {@link MathObject#NO_MAXIMUM})
     * @param maxHeight
     *        The maximum height the {@link MathObject} can have (can be {@link MathObject#NO_MAXIMUM})
     * @return The size of the child bounding boxes
     */
    protected Rect[] getSizes()
    {
        // Get the size both operands want to take
        Rect topSize = getChild(0).getBoundingBox();
        Rect bottomSize = getChild(1).getBoundingBox();
        
        // Calculate the height the operator wants to take
        int operatorHeight = Math.max((topSize.height() + bottomSize.height()) / 15 , 5);

        // If we have no maximum height or it isn't breached, we're done
       // if(maxHeight == NO_MAXIMUM )//topSize.height()+ operatorHeight + bottomSize.height() < maxHeight )
            return new Rect[] 
            		{
        			new Rect(0, 0, Math.max(topSize.width(), bottomSize.width()), operatorHeight),
        			topSize, 
        			bottomSize
        			};
    }

    @Override
    public Rect[] getOperatorBoundingBoxes()
    {
        // Get the sizes
        Rect[] sizes = getSizes();

        // Position the bounding box and return it
        sizes[0].offsetTo(0, sizes[1].height());
        return new Rect[] {sizes[0]};
    }

    @Override
    public Rect getChildBoundingBox(int index) throws IndexOutOfBoundsException
    {
        // Make sure the child index is valid
        checkChildIndex(index);

        // Get the sizes and the total height
        Rect[] sizes = getSizes();
        Point center_one = getChild(0).getCenter();
        Point center_two = getChild(1).getCenter();
        Point center_this = this.getCenter();
        
        // Translate the operand's bounding box
        if(index == 0)
            sizes[1].offsetTo(center_this.x - center_one.x, 0);
        else
            sizes[2].offsetTo(center_this.x - center_two.x, sizes[0].height() + sizes[1].height());

        // Return the requested bounding box
        return sizes[index + 1];
    }
    
    @Override
    public Rect getBoundingBox()
    {
        // Get the sizes
        Rect[] sizes = getSizes();
        
        // Return a bounding box, containing the bounding boxes of the children
       
        int width = Math.max(sizes[1].width(), sizes[2].width());
        int height = sizes[0].height() + sizes[1].height() + sizes[2].height();

        return new Rect(0, 0, width, height);
    }
    
    @Override
    public Point getCenter()
    {
        // Get the operator bounding box
        Rect operatorBounding = getOperatorBoundingBoxes()[0];
        
        // Return the center, which is the center of the operator
        return new Point(operatorBounding.centerX(), operatorBounding.centerY());
    }
    
    @Override
  	public void setLevel(int l)
  	{
  		level = l;
  		getChild(0).setLevel(level + 1);
  		getChild(1).setLevel(level + 1);
  	}

    @Override
    public void draw(Canvas canvas)
    {
        // Draw the bounding boxes
        drawBoundingBoxes(canvas);
        
        // Get the bounding boxes
        final Rect operator = getOperatorBoundingBoxes()[0];
        
        // Draw the operator
        canvas.save();
        operatorPaint.setColor(getColor());
        operatorPaint.setStrokeWidth(lineWidth);
        canvas.drawLine(operator.left, operator.centerY(), operator.right, operator.centerY(), operatorPaint);
        //canvas.drawRect(operator.left, operator.top + operator.height() / 6, operator.right, operator.bottom - operator.height() / 3, operatorPaint);
        canvas.restore();

        // Draw the children
        drawChildren(canvas);
    }

    @Override
    protected String getType()
    {
        return TYPE;
    }
}
