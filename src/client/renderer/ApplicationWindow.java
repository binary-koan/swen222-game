import javax.swing.*;


public class ApplicationWindow extends JFrame {

	private final GameCanvas canvas;
	
	public ApplicaionWindow(String title, GameCanvas canvas) {
		super(title);
		this.canvas = canvas;
		
		final JMenuBar menuBar = setupMenuBar();
		JPanel lowerBar = setupLowerBar();
		setLayout(new BorderLayout());
		
		//Add items to the frame 
		add(menuBar, BorderLayout.NORTH);
		add(canvas, BorderLayout.CENTER);
		add(lowerBar, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		pack();
		setResizeable(false);
		setVisible(true);
			
	}
	
	
	private JMenuBar setupMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		JMenu editMenu = new JMenu("Game");
		
		JMenuItem newMenuItem = new JMenuItem("New");
		newMenuItem.setMnemonic(KeyEvent.VK_N);
		newMenuItem.setActionCommand("New");

	     JMenuItem openMenuItem = new JMenuItem("Open");
	     openMenuItem.setActionCommand("Open");

	     JMenuItem saveMenuItem = new JMenuItem("Save");
	     saveMenuItem.setActionCommand("Save");

	     JMenuItem exitMenuItem = new JMenuItem("Exit");
	     exitMenuItem.setActionCommand("Exit");
	     
	     fileMenu.add(newMenuItem);
	     fileMenu.add(openMenuItem);
	     fileMenu.add(saveMenuItem);
	     fileMenu.addSeparator();
	     fileMenu.add(exitMenuItem);    
	     
	     menuBar.add(fileMenu);
	     menuBar.add(editMenu);
		
	     return menuBar;
	
	}
	
	private JPanel setupLowerBar() {
		JPanel area = new JPanel();
		area.setLayout(new FlowLayout());
		return area;
	}