package org.teaminfty.math_dragon;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/** This class draws binary operations that are written linear.
 * That is, operations which are written like '&lt;left operand&gt; &lt;operator&gt; &lt;right operand&gt;'.
 * For example: the add or subtract operation.
 */
public class MathOperationDivide extends MathBinaryOperation
{

    /** The paint that is used for drawing the operator */
    protected Paint operatorPaint = new Paint();
    
    /** Constructor
     * @param defWidth The default maximum width
     * @param defHeight The default maximum height
     */
    public MathOperationDivide(int defWidth, int defHeight)
    {
        super(defWidth, defHeight);
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
    protected Rect[] getSizes(int maxWidth, int maxHeight)
    {
        // Get the size both operands want to take
        Rect topSize = getChild(0).getBoundingBox(maxWidth, NO_MAXIMUM);
        Rect bottomSize = getChild(1).getBoundingBox(maxWidth, NO_MAXIMUM);
        
        // Calculate the height the operator wants to take
        int operatorHeight = Math.max((topSize.height() + bottomSize.height()) / 15 , 5);

        // If we have no maximum height, we're done
        if(maxHeight == NO_MAXIMUM)
            return new Rect[] {new Rect(0, 0, Math.max(topSize.width(), bottomSize.width()), operatorHeight), topSize, bottomSize};

        // If we would get higher than the maximum height, shrink so we fit in
        if(topSize.height() + operatorHeight + bottomSize.height() > maxHeight)
        {
            // Determine the maximum width for each operand
            final int totalHeight = topSize.height() + operatorHeight + bottomSize.height();
            final int topMax = maxHeight * topSize.height() / totalHeight;
            final int bottomMax = maxHeight * topSize.height() / totalHeight;
            
            // Set the new sizes
            topSize.set(0, 0, topMax * topSize.width() / topSize.height(), topMax);
            bottomSize.set(0, 0, bottomMax * bottomSize.width() / bottomSize.height(), bottomMax);
            
            // Calculate the new operator size
            operatorHeight = Math.max((topSize.height() + bottomSize.height()) / 15 , 5);
        }

        // Return the sizes
        return new Rect[] {new Rect(0, 0, Math.max(topSize.width(), bottomSize.width()), operatorHeight), topSize, bottomSize};
    }

    @Override
    public Rect[] getOperatorBoundingBoxes(int maxWidth, int maxHeight)
    {
        // Get the sizes
        Rect[] sizes = getSizes(maxWidth, maxHeight);

        // Position the bounding box and return it
        sizes[0].offsetTo(0, sizes[1].height());
        return new Rect[] {sizes[0]};
    }

    @Override
    public Rect getChildBoundingBox(int index, int maxWidth, int maxHeight) throws IndexOutOfBoundsException
    {
        // Make sure the child index is valid
        checkChildIndex(index);

        // Get the sizes and the total height
        Rect[] sizes = getSizes(maxWidth, maxHeight);
        
        // Translate the operand's bounding box
        if(index == 0)
            sizes[1].offsetTo((sizes[0].width() - sizes[1].width()) / 2, 0);
        else
            sizes[2].offsetTo((sizes[0].width() - sizes[2].width()) / 2, sizes[0].height() + sizes[1].height());

        // Return the requested bounding box
        return sizes[index + 1];
    }
    
    @Override
    public IExpr eval() throws EmptyChildException
    {
        // Check if the children are not empty
        this.checkChildren();
        
        // Return the result
        return F.Divide(getChild(0).eval(), getChild(1).eval());
    }
    
    @Override
    public double approximate() throws NotConstantException, EmptyChildException
    {
        // Check if the children are not empty
        this.checkChildren();
        
        // Return the result
        return getChild(0).approximate() / getChild(1).approximate();
    }

    @Override
    public void draw(Canvas canvas, int maxWidth, int maxHeight)
    {
        // Get the bounding boxes
        final Rect operator = getOperatorBoundingBoxes(maxWidth, maxHeight)[0];
        
        // Draw the operator
        canvas.save();
        operatorPaint.setColor(getColor());
        canvas.drawRect(operator.left, operator.top + operator.height() / 3, operator.right, operator.bottom - operator.height() / 3, operatorPaint);
        canvas.restore();

        // Draw the children
        drawChildren(canvas, maxWidth, maxHeight);
    }
    

}
