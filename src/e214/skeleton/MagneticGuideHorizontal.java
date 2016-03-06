package e214.skeleton;

import java.awt.geom.Point2D;

import fr.lri.swingstates.canvas.CExtensionalTag;
import fr.lri.swingstates.canvas.Canvas;

public class MagneticGuideHorizontal extends MagneticGuide {

	public MagneticGuideHorizontal(Point2D origin, Canvas canvas){
		super(0, origin.getY(), 3000, origin.getY(), canvas);
	}
	
	@Override
	public String toString() {
		return "Horizontal";
	}

}
