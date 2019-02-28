package windows;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.net.*;
import main.*;
import network.*;
import sounds.SoundBuffer;

public class WelcomeWindow extends JFrame implements ActionListener {
	static final ImageIcon TITLESCREEN_BACKGROUND = new ImageIcon(WelcomeWindow.class.getResource("/resources/interface/titlescreen.jpg"));
	static final ImageIcon PVZ_LOGO = new ImageIcon(WelcomeWindow.class.getResource("/resources/interface/PvZ_Logo.png"));
	static final ImageIcon ZOMBIE_HEAD = new ImageIcon(WelcomeWindow.class.getResource("/resources/interface/ZombieHead.png"));
	static final String[] DIFFICULTY_CHOICE = { "Easy", "Normal", "Hard", "Lunatic" };
	static final String[] NETMODE_CHOICE = { "服务端", "客户端" };
	JPanel blankPanel = new JPanel(), centerPanel = new JPanel();
	JButton startButton = new JButton(), endButton = new JButton(), netButton = new JButton();
	
	public WelcomeWindow() {
		super("Plants vs. Zombies (Java)");
		setSize(1024, 768);
		setResizable(false);
		try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
		SoundBuffer.INTRO.start();
		
		JLayeredPane lp = (JLayeredPane)getLayeredPane();
		JLabel backgroundLabel = new JLabel(new ImageIcon(TITLESCREEN_BACKGROUND.getImage().getScaledInstance(1024, 768, Image.SCALE_DEFAULT)));
		backgroundLabel.setBounds(0, 0, 1024, 768);
		lp.add(backgroundLabel, new Integer(-30001));
		
		JPanel cp = (JPanel)getContentPane();
		cp.setOpaque(false);
		cp.setLayout(null);
		JLabel logoLabel = new JLabel(PVZ_LOGO);
		logoLabel.setBounds(162, 50, 700, 116);
		cp.add(logoLabel);
		
		startButton.setBorderPainted(false);
		startButton.setContentAreaFilled(false);
		startButton.setFocusPainted(false);
		startButton.setIcon(new ImageIcon(SystemBuffer.BUTTON_ICON.getImage().getScaledInstance(226, 82, Image.SCALE_DEFAULT)));
		startButton.setBounds(399, 260, 226, 82);
		startButton.setFont(new Font("微软雅黑", Font.BOLD, 24));
		startButton.setForeground(Color.GREEN);
		startButton.setText("开始游戏");
		startButton.setHorizontalTextPosition(SwingConstants.CENTER);
		startButton.addActionListener(this);
		cp.add(startButton);
		
		endButton.setBorderPainted(false);
		endButton.setContentAreaFilled(false);
		endButton.setFocusPainted(false);
		endButton.setIcon(new ImageIcon(SystemBuffer.BUTTON_ICON.getImage().getScaledInstance(226, 82, Image.SCALE_DEFAULT)));
		endButton.setBounds(399, 480, 226, 82);
		endButton.setFont(new Font("微软雅黑", Font.BOLD, 24));
		endButton.setForeground(Color.GREEN);
		endButton.setText("退出");
		endButton.setHorizontalTextPosition(SwingConstants.CENTER);
		endButton.addActionListener(this);
		cp.add(endButton);
		
		netButton.setBorderPainted(false);
		netButton.setContentAreaFilled(false);
		netButton.setFocusPainted(false);
		netButton.setIcon(new ImageIcon(SystemBuffer.BUTTON_ICON.getImage().getScaledInstance(226, 82, Image.SCALE_DEFAULT)));
		netButton.setBounds(399, 370, 226, 82);
		netButton.setFont(new Font("微软雅黑", Font.BOLD, 24));
		netButton.setForeground(Color.GREEN);
		netButton.setText("网络游戏");
		netButton.setHorizontalTextPosition(SwingConstants.CENTER);
		netButton.addActionListener(this);
		cp.add(netButton);
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public void actionPerformed(ActionEvent ae) {
		Object source = ae.getSource();
		if (source == endButton) System.exit(0);
		else if (source == startButton) {
			String selectedDifficulty = (String)JOptionPane.showInputDialog(null, "请选择游戏难度", "选择难度", 
					JOptionPane.INFORMATION_MESSAGE, ZOMBIE_HEAD, DIFFICULTY_CHOICE, DIFFICULTY_CHOICE[1]);
			try {
				if (selectedDifficulty.equals("Easy")) SystemBuffer.difficulty = 0;
				else if (selectedDifficulty.equals("Normal")) SystemBuffer.difficulty = 1;
				else if (selectedDifficulty.equals("Hard")) SystemBuffer.difficulty = 2;
				else if (selectedDifficulty.equals("Lunatic")) SystemBuffer.difficulty = 3;
				SystemBuffer.totalZombieNum = SystemBuffer.difficulty * 10 + 25;
				this.dispose();
				SoundBuffer.INTRO.playerStop();
				GameWindow game = new GameWindow();
			} catch (NullPointerException e) {}
		}
		else if (source == netButton) {
			int mode;
			try {
				String netMode = (String)JOptionPane.showInputDialog(null, "请选择网络连接方式（服务端/客户端）：", "选择连接方式",
						JOptionPane.INFORMATION_MESSAGE, null, NETMODE_CHOICE, NETMODE_CHOICE[1]);
				if (netMode.equals("服务端")) {
					String portStr = (String)JOptionPane.showInputDialog(null, "请输入端口号 (1025-65535)：", "服务端连接", JOptionPane.PLAIN_MESSAGE);
					int portNum = Integer.parseInt(portStr);
					if (portNum <= 1024 && portNum >= 65535) {
						JOptionPane.showMessageDialog(null, "端口号无效！", "错误", JOptionPane.ERROR_MESSAGE);
						throw new Exception();
					}
					SystemBuffer.netGame = new NetGame(portNum);
					SystemBuffer.networkOn = true;
					
					this.dispose();
					SoundBuffer.INTRO.playerStop();
					GameWindow game = new GameWindow();
				}
				else {
					InetAddress serverAddress;
					String srvAddrStr = (String)JOptionPane.showInputDialog(null, "请输入服务器IP地址：", "客户端连接", JOptionPane.PLAIN_MESSAGE);
					try { serverAddress = InetAddress.getByName(srvAddrStr); } catch (Exception e) { 
						JOptionPane.showMessageDialog(null, "IP地址无效！", "错误", JOptionPane.ERROR_MESSAGE);
						throw new Exception();
					}
					String portStr = (String)JOptionPane.showInputDialog(null, "请输入端口号 (1025-65535)：", "服务端连接", JOptionPane.PLAIN_MESSAGE);
					int portNum = Integer.parseInt(portStr);
					if (portNum <= 1024 && portNum >= 65535) {
						JOptionPane.showMessageDialog(null, "端口号无效！", "错误", JOptionPane.ERROR_MESSAGE);
						throw new Exception();
					}
					SystemBuffer.netGame = new NetGame(serverAddress, portNum);
					SystemBuffer.networkOn = true;
					
					this.dispose();
					SoundBuffer.INTRO.playerStop();
					GameWindow game = new GameWindow();
				}
			} catch (Exception e) { System.out.println(e.getMessage()); }
		}
	}
}