package e214.skeleton;

import java.awt.geom.Point2D;

import fr.lri.swingstates.canvas.CExtensionalTag;
import fr.lri.swingstates.canvas.Canvas;

public class MagneticGuideVertical extends MagneticGuide {

	public MagneticGuideVertical(Point2D origin, Canvas canvas,
			CExtensionalTag glTag) {
		super(origin.getX(), 0, origin.getX(), 3000, canvas, glTag);
	}
}
