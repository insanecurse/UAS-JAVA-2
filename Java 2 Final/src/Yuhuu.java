import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.mysql.jdbc.ResultSet;

import java.awt.BorderLayout;
import java.awt.Color;


public class Yuhuu extends JFrame{
	private static Client ChatClient;
	public static String username ="No-Name";
	public static String ip;
	
	//interface buat login
	public static JFrame login = new JFrame();
	public static JTextField txtlogin = new JTextField(20);
	private static JLabel lbllogin = new JLabel("Type The Username You Want");
	private static JButton btnlogin = new JButton("Go");
	private static JPanel login2 = new JPanel();
	private static JLabel lbl1 = new JLabel("WELCOME TO OMAIGLE !");
	private static JButton btncek = new JButton("Check");
	
	
	//interface buat main nya
	public static JFrame mainyuhuu = new JFrame();
	public static JTextField chatuser = new JTextField(20);
	public static JTextArea chat = new JTextArea(470, 280);
	private static JLabel lblmsg = new JLabel("Message : ");
	private static JLabel lbllistuser = new JLabel("List User Online :");
	private static JButton clsc = new JButton("Clear Screen");
	private static JButton send = new JButton("Send");
	public static JList listuser = new JList();
	private static JButton whisp = new JButton("Whisper");
	private static JLabel lblOmaigle = new JLabel("OMAIGLE");
	private static JScrollPane scrollchat = new JScrollPane(chat);
	private static JButton btndc = new JButton("Disconnect");
	
	
	public static void main(String args[]){
//		GUImain();
		GUIlogin();
}
	
	public static void FillListbox(){
		DefaultListModel m = new DefaultListModel();
		try{
		DatabaseYuhuu df = new DatabaseYuhuu();
		java.sql.ResultSet rs = df.selectuser();
		while(rs.next()){
			String username2=rs.getString("usernameyuhuu");
			m.addElement(username2);
		}
		listuser.setModel(m);
		}catch(Exception e){
			
		}
	}
	
	public static void Connect(){
		try
		{
				final int PORT =1070;
				final String HOST ="192.168.1.104";
				Socket SOCK = new Socket(HOST, PORT);
				System.out.println("You connected to : " + HOST);
				ip = SOCK.getRemoteSocketAddress().toString();
				
				ChatClient = new Client(SOCK);
				
				//buat liat orang yg online
				PrintWriter OUT = new PrintWriter(SOCK.getOutputStream());
				OUT.println(username);
				OUT.flush();
				
				Thread X = new Thread(ChatClient);
				X.start();
			}
			catch(Exception X){
				System.out.print(X);
				JOptionPane.showMessageDialog(null, "Server not Responding");
				System.exit(0);
			}
	}
	
	public static void GUIlogin(){
		login.setTitle("OMAIGLE");
		login.setResizable(false);
		login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		login.setBounds(100,100,450,300);
		login.setSize(470,200);
		login.setLayout(null);
		
		lbl1.setFont(new Font("Segoe Print", Font.PLAIN, 16));
		lbl1.setBounds(120, 11, 219, 42);
		login.getContentPane().add(lbl1);
		
		lbllogin.setBounds(144, 50, 184, 14);
		login.getContentPane().add(lbllogin);
		
		txtlogin.setBounds(104, 67, 241, 20);
		txtlogin.setColumns(10);
		login.getContentPane().add(txtlogin);
		
		btncek.setBounds(114, 98, 89, 23);
		btncek.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			try{
				String a = txtlogin.getText();
				DatabaseYuhuu db = new DatabaseYuhuu();
				if(db.cek(a) == false){
					JOptionPane.showMessageDialog(null, "Username dapat dipakai !");
					btnlogin.setEnabled(true);
				}
				else{
					JOptionPane.showMessageDialog(null, "Username telah dipakai !!!");
					
				}
				
				
			}catch (Exception er){
				er.printStackTrace();
			}
			}
		});
		login.getContentPane().add(btncek);
		
		btnlogin.setBounds(244, 98, 89, 23);
		btnlogin.setEnabled(false);
		btnlogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(!txtlogin.getText().equals("")){
					username = txtlogin.getText().trim();
					Server.CurrentUsers.add(username);
					login.setVisible(false);
					Connect();
					GUImain();
					try{
						//final int PORT =1000;
						//final String HOST ="192.168.1.104";
						
						DatabaseYuhuu dc = new DatabaseYuhuu();
						dc.insertData(txtlogin.getText(), ip);
						
						
					}catch (Exception er){
						er.printStackTrace();
					}
				}else{
					JOptionPane.showMessageDialog(null, "Please Enter Your Username !");
				}
			}
		});
		login.getContentPane().add(btnlogin);
		login.setVisible(true);
	}
	
	public static void GUImain(){
		mainyuhuu.setTitle("Anda login sebagai : " + username);
		mainyuhuu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainyuhuu.setBackground(Color.GRAY);
		mainyuhuu.setSize(680, 440);
		mainyuhuu.setBounds(100,100,648,412);
		mainyuhuu.setLayout(null);
		//mainyuhuu.setBackground("Dark Blue");
		
		Timer timer = new Timer(1000, new ActionListener() {
			  @Override
			  public void actionPerformed(ActionEvent arg0) {
			   FillListbox();
			  }
			});
			timer.setRepeats(true); // Only execute once
			timer.start();
			
		lblOmaigle.setForeground(Color.blue);
		lblOmaigle.setFont(new Font("Yu Gothic Light", Font.PLAIN,23));
		lblOmaigle.setBounds(10, 0, 115, 37);
		mainyuhuu.getContentPane().add(lblOmaigle);
		
		listuser.setVisibleRowCount(20);
		listuser.setToolTipText("");
		listuser.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listuser.setBounds(510, 60, 89, 250);
		mainyuhuu.getContentPane().add(listuser);
		
		chatuser.setBounds(10, 343, 382, 20);
		mainyuhuu.getContentPane().add(chatuser);
		
		
		chat.setEditable(false);
		chat.setColumns(20);
		chat.setLineWrap(true);
		chat.setRows(5);
		
		//chat.setBounds(10, 35, 430, 280);
		//mainyuhuu.getContentPane().add(chat);
		
		scrollchat.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollchat.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollchat.setViewportView(chat);
		scrollchat.setBounds(10,35,430,280);
		mainyuhuu.getContentPane().add(scrollchat);
		
		
		lblmsg.setBackground(Color.BLACK);
		lblmsg.setBounds(10, 328, 62, 14);
		mainyuhuu.getContentPane().add(lblmsg);
		
		lbllistuser.setForeground(Color.blue);
		lbllistuser.setBounds(510, 35, 100, 14);
		mainyuhuu.getContentPane().add(lbllistuser);

		btndc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			try {
				try {
					DatabaseYuhuu dd = new DatabaseYuhuu();
					dd.deleteuser(username);
				} catch (Exception e) {
					e.printStackTrace();
				}
				ChatClient.DC();
			} catch (IOException e) {
				e.printStackTrace();
			}
			}
		});
		btndc.setBounds(567, 11, 74, 20);
		mainyuhuu.getContentPane().add(btndc);
	
		
		send.setForeground(Color.blue);
		send.setBackground(Color.gray);
		send.setFont(new Font("Segoe Print", Font.PLAIN, 10));
		send.setBounds(397, 343, 70, 20);
		send.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(!chatuser.getText().equals("")){
					ChatClient.SEND(chatuser.getText());
					chatuser.requestFocus();
					//FillListbox();
			}}
		});
		mainyuhuu.getContentPane().add(send);
		
		clsc.setForeground(Color.blue);
		clsc.setBackground(Color.gray);
		clsc.setFont(new Font("Segoe Script", Font.PLAIN, 10));
		clsc.setBounds(474, 343, 74, 20);
		clsc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chat.setText(null);
			}
		});
		mainyuhuu.getContentPane().add(clsc);
		
		
		whisp.setForeground(Color.blue);
		whisp.setBackground(Color.gray);
		whisp.setFont(new Font("Segoe Print", Font.PLAIN, 10));
		whisp.setBounds(555, 343, 74, 20);
		mainyuhuu.getContentPane().add(whisp);
		
		mainyuhuu.setVisible(true);
	}	
	public static void disp(){
	mainyuhuu.dispose();
	}
	
	
}