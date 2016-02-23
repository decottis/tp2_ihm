package e214.skeleton;

import java.awt.Color;
import java.awt.geom.Point2D;

import javax.swing.JFrame;

import fr.lri.swingstates.canvas.CExtensionalTag;
import fr.lri.swingstates.canvas.CRectangle;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.canvas.transitions.DragOnTag;
import fr.lri.swingstates.canvas.transitions.PressOnTag;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.Drag;
import fr.lri.swingstates.sm.transitions.Press;
import fr.lri.swingstates.sm.transitions.Release;

/**
 * @author Nicolas Roussel (roussel@lri.fr)
 * 
 */
public class MagneticGuides extends JFrame {

	private Canvas canvas;
	private CExtensionalTag oTag;
	private CExtensionalTag glTagVertical;
	private CExtensionalTag glTagHorizontal;

	public MagneticGuides(String title, int width, int height) {
		super(title);
		canvas = new Canvas(width, height);
		canvas.setAntialiased(true);
		getContentPane().add(canvas);

		oTag = new CExtensionalTag(canvas) {
		};

		glTagVertical = new CExtensionalTag(canvas) {
		};

		glTagHorizontal = new CExtensionalTag(canvas) {
		};

		CStateMachine sm = new CStateMachine() {

			private Point2D p;
			private CShape draggedShape;

			public State start = new State() {
				Transition pressOnObject = new PressOnTag(oTag, BUTTON1,
						">> oDrag") {
					public void action() {
						p = getPoint();
						draggedShape = getShape();
					}
				};

				Transition pressOnHorizontalGuideLine = new PressOnTag(
						glTagHorizontal, BUTTON1, ">> glDragHorizontal") {
					public void action() {
						System.out.println("pressH");
						p = getPoint();
						draggedShape = getShape();
					}
				};

				Transition pressOnVerticalGuideLine = new PressOnTag(
						glTagVertical, BUTTON1, ">> glDragVertical") {
					public void action() {
						System.out.println("pressV");
						p = getPoint();
						draggedShape = getShape();
					}
				};

				Transition pressOnBackgroundButton3 = new Press(BUTTON3) {
					public void action() {
						Point2D origin = getPoint();
						MagneticGuide mg;

						mg = new MagneticGuideVertical(origin, canvas,
								glTagVertical);
					}
				};

				Transition pressOnBackgroundButton1 = new Press(BUTTON1) {
					public void action() {
						Point2D origin = getPoint();
						MagneticGuide mg;

						mg = new MagneticGuideHorizontal(origin, canvas,
								glTagHorizontal);
					}
				};

			};

			public State oDrag = new State() {
				
				//TODO marche pas
				Transition dragOnHorizontalMagneticGuide = new DragOnTag(glTagHorizontal, BUTTON1) {
					public void action() {
						System.out.println("onLine");
						Point2D q = getPoint();
						draggedShape.translateBy(q.getX() - p.getX(), q.getY()
								- p.getY());
						p = q;
						getShape().setOutlinePaint(Color.BLACK);
					}
				};
				
				Transition drag = new Drag(BUTTON1) {
					public void action() {
						Point2D q = getPoint();
						draggedShape.translateBy(q.getX() - p.getX(), q.getY()
								- p.getY());
						p = q;
					}
				};
				Transition release = new Release(BUTTON1, ">> start") {
				};
			};

			public State glDragHorizontal = new State() {
				Transition drag = new Drag(BUTTON1) {
					public void action() {
						Point2D q = getPoint();
						draggedShape.translateBy(0, q.getY() - p.getY());
						p = q;
					}
				};
				Transition release = new Release(BUTTON1, ">> start") {
				};
			};

			public State glDragVertical = new State() {
				Transition drag = new Drag(BUTTON1) {
					public void action() {
						Point2D q = getPoint();
						draggedShape.translateBy(q.getX() - p.getX(), 0);
						p = q;
					}
				};
				Transition release = new Release(BUTTON1, ">> start") {
				};
			};

		};
		sm.attachTo(canvas);

		pack();
		setVisible(true);
		canvas.requestFocusInWindow();
	}

	public void populate() {
		int width = canvas.getWidth();
		int height = canvas.getHeight();

		double s = (Math.random() / 2.0 + 0.5) * 30.0;
		double x = s + Math.random() * (width - 2 * s);
		double y = s + Math.random() * (height - 2 * s);

		int red = (int) ((0.8 + Math.random() * 0.2) * 255);
		int green = (int) ((0.8 + Math.random() * 0.2) * 255);
		int blue = (int) ((0.8 + Math.random() * 0.2) * 255);

		CRectangle r = canvas.newRectangle(x, y, s, s);
		r.setFillPaint(new Color(red, green, blue));
		r.addTag(oTag);
	}

	public static void main(String[] args) {
		MagneticGuides guides = new MagneticGuides("Magnetic guides", 600, 600);
		for (int i = 0; i < 20; ++i)
			guides.populate();
		guides.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
