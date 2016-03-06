package e214.skeleton;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import fr.lri.swingstates.canvas.CExtensionalTag;
import fr.lri.swingstates.canvas.CRectangle;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.canvas.transitions.DragOnTag;
import fr.lri.swingstates.canvas.transitions.PressOnTag;
import fr.lri.swingstates.canvas.transitions.ReleaseOnTag;
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
	private List<MagneticGuide> allGuides;

	public MagneticGuides(String title, int width, int height) {
		super(title);
		canvas = new Canvas(width, height);
		canvas.setAntialiased(true);
		getContentPane().add(canvas);
		
		allGuides = new ArrayList<MagneticGuide>();

		oTag = new CExtensionalTag(canvas) {
		};

		glTagVertical = new MagneticGuide(canvas) {
		};

		glTagHorizontal = new MagneticGuide(canvas) {
		};
		

		CStateMachine sm = new CStateMachine() {

			private Point2D p;
			private CShape draggedShape;
			private CExtensionalTag draggedOnTag;

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
						System.out.println("addV");
						Point2D origin = getPoint();
						MagneticGuide mg = new MagneticGuideVertical(origin, canvas);
						mg.addTag(glTagVertical);
						allGuides.add(mg);
					}
				};

				Transition pressOnBackgroundButton1 = new Press(BUTTON1) {
					public void action() {
						System.out.println("addH");
						Point2D origin = getPoint();
						MagneticGuide mg = new MagneticGuideHorizontal(origin, canvas);//,glTagHorizontal);
						mg.addTag(glTagHorizontal);
						allGuides.add(mg);
					}
				};

			};

			public State oDrag = new State() {
				
				Transition drag = new Drag(BUTTON1) {
					public void action() {
						Point2D q = getPoint();
						draggedShape.translateBy(q.getX() - p.getX(), q.getY()
								- p.getY());
						p = q;
						draggedOnTag = null;
					}
				};
				
				//TODO marche plus ou moins
				Transition dragOnHorizontalMagneticGuide = new DragOnTag(glTagHorizontal, BUTTON1) {
					public void action() {
						System.out.println("onLine");
						Point2D q = getPoint();
						draggedShape.translateBy(q.getX() - p.getX(), q.getY()
								- p.getY());
						p = q;
						CShape s = getShape();
						s.setOutlinePaint(Color.BLACK);
						List<CExtensionalTag> l = s.getTags();
						System.out.println("tags "+l.size());
						for (CExtensionalTag t : l){
							System.out.println("un tag "+t);
							if (t.toString().equals("Horizontal")){
								System.out.println("adding tag "+t);
								draggedOnTag = t;
								//draggedShape.addTag(t);
							}
						}
					}
				};
				Transition releaseOnTag = new ReleaseOnTag(glTagHorizontal, BUTTON1, ">> start") {
					public void action(){
						if (draggedOnTag != null){
							System.out.println("release!"+draggedOnTag);
							draggedShape.addTag(draggedOnTag);
							draggedShape.removeTag(oTag);
							for (CExtensionalTag t : draggedShape.getTags()){
								System.out.println("le tag "+t);
							}
						}
					}
				};
				Transition release = new Release(BUTTON1, ">> start") {
				};
			};

			public State glDragHorizontal = new State() {
				Transition drag = new Drag(BUTTON1) {
					@SuppressWarnings("unchecked")
					public void action() {
//						glTagHorizontal.forEachRemaining((e)->{
//							
//							Point2D q = getPoint();
//							draggedShape.translateBy(0, q.getY() - p.getY());
//							p = q;
//						});
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
		System.out.println("toto");
		MagneticGuides guides = new MagneticGuides("Magnetic guides", 600, 600);
		for (int i = 0; i < 20; ++i)
			guides.populate();
		guides.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
