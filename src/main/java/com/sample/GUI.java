package com.sample;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class GUI extends JFrame implements ActionListener {

  private static final long serialVersionUID = 3378774311250822914L;

  private JButton button;
  private String[] possibleAnswers;
  
  public boolean ready = false;
  public int n = -1;
	
  public ArrayList<JRadioButton> options = new ArrayList<>();
   
  public GUI(String question) {
	  super("Can We Date? - Result");
	  
      /// QUESTION ///
      
      JLabel label = new JLabel(question);
      Font font = new Font("Courier", Font.BOLD,20);
      label.setFont(font);
      
      JPanel contentPanel = new JPanel();
      Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
      contentPanel.setBorder(padding);
      contentPanel.add(label);
      this.setContentPane(contentPanel);
      
      /// if IMAGE ///
      
      if (question.startsWith("<")) {
    	  question = question.substring(1, question.length()-1);
    	  String path = "src/main/resources/imgs/" + question + ".png";
    	  this.addImage(path);
      }
      
      pack();
      
      this.setDefaultCloseOperation(EXIT_ON_CLOSE);  
      this.centerWindow();
      this.setVisible(true);
  }
  
  public GUI(String question, String[] possibleAnswers) {
	  super("Can We Date? - Question");
	  
      this.setLayout(null);
      this.possibleAnswers = possibleAnswers;
   
      /// QUESTION ///
      
      JPanel panel_Q = new JPanel();
      JLabel label = new JLabel(question);
      panel_Q.add(label);
      
      /// LIST ///
      
      JPanel panel_L = new JPanel(new GridLayout(0, 1));
         
      for (String str_option : possibleAnswers) { 
    	options.add(new JRadioButton(str_option));
      }
      
      ButtonGroup group = new ButtonGroup();
      
      for (JRadioButton option : options) { 	
    	  group.add(option);
      }

      setLayout(new FlowLayout());

      for (JRadioButton option : options) { 	
    	 panel_L.add(option);
      }

      /// CONTINUE ///
      
      JPanel panel_B = new JPanel();
      button = new JButton("Continue");
      button.setBounds(10, 10, 70, 40);
      button.addActionListener(this);
      panel_B.add(button);
      
      /// REGISTER ///
      
      this.add(panel_Q, BorderLayout.PAGE_START);
      this.add(panel_L, BorderLayout.PAGE_START);
      this.add(panel_B, BorderLayout.PAGE_START);
      
      pack();
      
      this.setDefaultCloseOperation(EXIT_ON_CLOSE);  
      this.centerWindow();
      this.setVisible(true);
  }
  
  public void addImage(String path) {
	  try {
    	  BufferedImage wPic = ImageIO.read(new File(path));
    	  JLabel wIcon = new JLabel(new ImageIcon(wPic));
    	  this.add(wIcon);
      } catch (IOException e) {
    	  // error
      }
  }
  
  public void centerWindow() {
      Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
      int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
      int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
      this.setLocation(x, y);
  }

  public void waitForAnswer() {
	  try {
		  while (!this.ready) { TimeUnit.MILLISECONDS.sleep(25); }
	  } catch (InterruptedException e) {
		  // error
	  }
  }
  
  public String getAnswer() {
	if(this.n >= 0) {
		System.out.println(this.possibleAnswers[this.n]);
		return this.possibleAnswers[this.n];
	}
	return "";
  }
  
  public void actionPerformed(ActionEvent e) {
	  System.out.println(e.getSource());
      if (e.getSource() == button) {
          System.out.println("[GUI] clicked!");
          for (int counter = 0; counter < options.size(); counter++) { 		      
              JRadioButton current = options.get(counter);
              if (current.isSelected()) {
            	  this.n = counter;
            	  break;
              }
          }   
          this.ready = true;
          this.dispose();
      }
  }
}