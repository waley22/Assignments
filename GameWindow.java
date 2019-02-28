package windows;

import java.awt.*;
import javax.swing.*;
import main.*;
import panels.*;

public class GameWindow extends JFrame {
	public GameWindow() {
		super("Plants vs. Zombie (Java)");
		setVisible(true);
		setSize(1024, 768);
		setResizable(false);
		
		FieldPanel fieldPanel = new FieldPanel();
		setContentPane(fieldPanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
