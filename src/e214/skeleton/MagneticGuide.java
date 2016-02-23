package e214.skeleton;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.function.Consumer;

import fr.lri.swingstates.canvas.CExtensionalTag;
import fr.lri.swingstates.canvas.CSegment;
import fr.lri.swingstates.canvas.Canvas;

public abstract class MagneticGuide extends CExtensionalTag {

	protected CSegment guideLine;
	
	public MagneticGuide(double minX, double minY, double maxX, double maxY, Canvas canvas, CExtensionalTag glTag) {
		super(canvas);
		guideLine = canvas.newSegment(minX, minY, maxX, maxY);
        guideLine.belowAll();
        float[] dash = {6f, 0f, 2f};
        BasicStroke bs4 = new BasicStroke(2, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_ROUND, 1.0f, dash, 2f);
        guideLine.setOutlinePaint(Color.GRAY);
        guideLine.setStroke(bs4);
        
        guideLine.addTag(this);
        guideLine.addTag(glTag);
	}
	
	@Override
	public void forEachRemaining(Consumer action) {
		// TODO vérifier
		for(Object o : this.collection)
			action.accept(o);
	}
	
	

}
