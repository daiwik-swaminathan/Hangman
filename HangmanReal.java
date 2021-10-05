import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import java.util.*;
import java.io.*;
public class HangmanReal extends JPanel implements ActionListener, KeyListener{
	JFrame frame;
	JLabel label, name1, name2, enterWord, again;
	JButton onePlayer, twoPlayer, playAgain, homeScreen, next, next2;
	JPanel panel;
	JTextField player1Name, player2Name, twoPlayerWord;
	int screenHeight, screenWidth;
	int youScore, compScore;
	File file;
	Scanner scanner;
	String[] letters = new String[26];
	boolean canRun, canWhite, notAllowed;
	String word;
	String[] wordArray;
	int randNum;
	String input;
	boolean[] isGuessed;
	int[] redLetters = new int[26];
	boolean keyTried;
	int timeToSet;
	String[] triedKeys = new String[6];
	int wrongCount, winCount, triedKeyCount;
	int bruhCount;
	boolean isTwo;
	int whichPlayer = 0;
	String pName1, pName2;
	public static void main(String[] args) {
		new HangmanReal();
	}
	public void runTwo() {
		isTwo = true;
		canRun = true;
	}
	public void runOne() {canRun = true;}
	public HangmanReal() {
		frame = new JFrame("Hangman");
		int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		screenWidth = width;
		screenHeight = height;
		frame.setBounds(0, 0, width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		frame.setResizable(true);
		panel = new JPanel();
		panel.setBounds(0, 0, width, height);
		panel.setBackground(Color.white);
		panel.setLayout(null);
		label = new JLabel("Hangman");
		label.setBounds(475, 0, 500, 100);
		label.setFont(new Font("Courier", Font.BOLD, 100));
		panel.add(label);
		onePlayer = new JButton("One Player");
		onePlayer.setBounds(480, 200, 410, 100);
		panel.add(onePlayer);
		onePlayer.addActionListener(this);
		twoPlayer = new JButton("Two Player");
		twoPlayer.setBounds(480, 300, 410, 100);
		panel.add(twoPlayer);
		twoPlayer.addActionListener(this);
		frame.add(panel);
		frame.getContentPane().add(this);
		frame.addKeyListener(this);
		frame.setFocusable(true);
		frame.setVisible(true); 
	}
	public void setWord(){
		int count = 65;
		for(int i=0; i<letters.length && count<91; i++){
			letters[i] = ""+(char)(count);
			count++;
		}
		tryCatch();
		count = 0;
		String temp = "";
		while(scanner.hasNext()){
			temp = scanner.nextLine();
			count++;
		}
		String[] words = new String[count];
		tryCatch();
		count = 0;
		while(scanner.hasNext()){
			words[count] = scanner.nextLine();
			words[count] = words[count].toUpperCase();
			count++;
		}
		randNum = (int)(Math.random()*count+1);
		if(isTwo==false)word = words[randNum];
		wordArray = new String[word.length()];
		isGuessed = new boolean[word.length()];
		for(int i=0; i<word.length(); i++) wordArray[i] = ""+word.charAt(i);
		canWhite = true;
		for(int i=0; i<redLetters.length; i++) redLetters[i] = 0;
		for(int i=0; i<triedKeys.length; i++) triedKeys[i] = null;
		for(int i=0; i<isGuessed.length; i++) isGuessed[i] = false;
		winCount = 0;
		wrongCount = 0;
		timeToSet = 0;
		triedKeyCount = 0;
		}
	public void tryCatch() {
		scanner = null;
		file = new File("HangmanWords.txt");
		try{
			scanner = new Scanner(file);
		}
		catch(FileNotFoundException e){
			System.exit(1);
		}
	}
	public void paintComponent(Graphics g){
	if(canRun==false) return;
		g.setColor(Color.WHITE);
		if(canWhite && winCount<word.length() && wrongCount<6) g.fillRect(0, 0, screenWidth, screenHeight);
		if(winCount==word.length() && timeToSet==0 && isTwo==false) {
			userWon();
			youScore++;
			timeToSet++;
		}
		else if(winCount==word.length() && timeToSet==0 && isTwo) {
			userWon();
			if(whichPlayer==1)youScore++;
			else compScore++;
			timeToSet++;
		}
		if(wrongCount==6 && timeToSet==0 && isTwo==false) {
			userLost();
			compScore++;
			timeToSet++;
		}
		else if(wrongCount==6 && timeToSet==0 && isTwo) {
			userLost();
			if(whichPlayer==1)compScore++;
			else youScore++;
			timeToSet++;
		}
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.BLACK);
		g.drawRect(1100, 10, 255, 660);
		g.drawRect(10, 10, 1090, 330);
		g.drawRect(10, 340, 1090, 330);
		g.drawRect(10, 340, 540, 330);
		g.setFont(new Font("Courier", Font.BOLD, 50));
		if(winCount==word.length()){
			g.drawString("You Won!", 835, 50);
			g.drawString("(This time)", 775, 100);
		}
		else if(wrongCount==6){
			g.drawString("You Lost!", 735, 50);
			g.drawString("Too Stressed?!", 675, 100);
		}
		g.drawString("Possible Letters", 37, 380);
		g.drawString("Score", 740, 380);
		if(isTwo==false)g.drawString("You"+" "+youScore, 565, 450);
		else g.drawString(pName1+" "+youScore, 565, 450);
		if(isTwo==false)g.drawString("Computer"+" "+compScore, 565, 600);
		else g.drawString(pName2+" "+compScore, 565, 600);
		g.drawString("Word", 490, 50);
		g.drawLine(1125, 650, 1330, 650);
		g.drawLine(1330, 650, 1330, 30);
		g.drawLine(1330, 30, 1225, 30);
		g.drawLine(1225, 30, 1225, 150);
		g.setFont(new Font("Courier", Font.BOLD, 25));
		int count = 0;
		for(int i=0; i<13; i++){
			if(redLetters[i]==0) g.setColor(Color.BLACK);
			else if(redLetters[i]==1) g.setColor(Color.GREEN);
			else if(redLetters[i]==2) g.setColor(Color.RED);
			g.drawString(letters[i], 40+i*38, 475);
		}
		for(int i=13; i<26; i++){
			if(redLetters[i]==0) g.setColor(Color.BLACK);
			else if(redLetters[i]==1) g.setColor(Color.GREEN);
			else if(redLetters[i]==2) g.setColor(Color.RED);
			g.drawString(letters[i], 40+count*38, 575);
			count++;
		}
		int spot = 20;
		g.setFont(new Font("Courier", Font.BOLD, 50));
		for(int i=0; i<word.length(); i++, spot+=85) {
			g.setColor(Color.BLACK);
			g.fillRect(spot, 300, 75, 5);
			if(isGuessed[i]==false) g.setColor(Color.WHITE);
			else g.setColor(Color.BLACK);
			if(wrongCount==6 && isGuessed[i]==false) g.setColor(Color.RED);
			g.drawString(wordArray[i], spot+25, 285);
		}	
		g.setColor(Color.BLACK);
		if(wrongCount>=1) g.drawOval(1175, 150, 100, 100);
		if(wrongCount>=2) g.drawLine(1225, 250, 1225, 350);
		if(wrongCount>=3) g.drawLine(1225, 300, 1300, 225);
		if(wrongCount>=4) g.drawLine(1225, 300, 1150, 225);
		if(wrongCount>=5) g.drawLine(1225, 350, 1275, 450);
		if(wrongCount>=6) g.drawLine(1225, 350, 1175, 450);
	}
	public void userWon() {
		playAgain = new JButton("Play Again");
		playAgain.setBounds(825, 125, 100, 100);
		playAgain.addActionListener(this);
		panel.add(playAgain);
		homeScreen = new JButton("Home Screen");
		homeScreen.setBounds(950, 125, 100, 100);
		homeScreen.addActionListener(this);
		panel.add(homeScreen);
		frame.repaint();
	}
	public void userLost() {
		playAgain = new JButton("Play Again");
		playAgain.setBounds(775, 125, 100, 100);
		playAgain.addActionListener(this);
		panel.add(playAgain);
		homeScreen = new JButton("Home Screen");
		homeScreen.setBounds(880, 125, 100, 100);
		homeScreen.addActionListener(this);
		panel.add(homeScreen);
		frame.repaint();
	}
	public void keyTyped(KeyEvent e) {
		if(winCount==word.length() || wrongCount==6) return;
		boolean isWrong = true;
		input = ""+e.getKeyChar();
		input = input.toUpperCase();
		for(int i=0; i<wordArray.length; i++) 
		if(input.equals(wordArray[i])) {
			if(isGuessed[i]==false)winCount++;
			isWrong = false;
			isGuessed[i] = true;
			if(isGuessed[i]) for(int j=0; j<letters.length; j++) if(input.equals(letters[j])) redLetters[j] = 1;
		}
		if(isWrong) {
			if(checkLetter()==false) wrongCount++;
			for(int j=0; j<letters.length; j++)	if(input.equals(letters[j])) redLetters[j] = 2;
		}
		repaint();
	}
	public boolean checkLetter() {
		for(int i=0; i<6; i++) {
			if(triedKeys[i]!=null && input.equals(triedKeys[i])) return true;
		}
		if(triedKeyCount>5) return true;
		triedKeys[triedKeyCount] = input;
		triedKeyCount++;
		return false;
	}
	public void keyPressed(KeyEvent e){}
	public void keyReleased(KeyEvent e){}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==next) {
			pName1 = player1Name.getText();
			pName2 = player2Name.getText();
			panel.remove(player1Name);
			panel.remove(player2Name);
			panel.remove(next);
			panel.remove(name1);
			panel.remove(name2);
			enterWord = new JLabel(pName1+", enter a word for "+pName2+". Maximum of 12 letters.", SwingConstants.CENTER);
			enterWord.setBounds(0, 200, screenWidth, 100);//350 200 1300 100
			enterWord.setFont(new Font("Courier", Font.BOLD, 25));
			panel.add(enterWord);
			twoPlayerWord = new JTextField();
			twoPlayerWord.setBounds(535, 300, 300, 50);
			twoPlayerWord.setFont(new Font("Courier", Font.BOLD, 25));
			panel.add(twoPlayerWord);
			next2 = new JButton("Next");
			next2.setBounds(535, 385, 300, 50);
			panel.add(next2);
			next2.addActionListener(this);
			frame.repaint();
		}
		if(e.getSource()==next2) {
			word = twoPlayerWord.getText();
			if(word.length()>12){
			notAllowed = true;
			again = new JLabel("Please enter a shorter word.", SwingConstants.CENTER);
			again.setBounds(0, 500, screenWidth, 50);
			again.setFont(new Font("Courier", Font.BOLD, 25));
			again.setForeground(Color.RED);
			panel.add(again);
			frame.repaint();
			return;
			}
			else if(word.length()<13 && notAllowed){panel.remove(again);notAllowed=false;}
			word = word.toUpperCase();
			panel.remove(twoPlayerWord);
			panel.remove(next2);
			panel.remove(enterWord);
			frame.remove(panel);
			panel = new JPanel();
			panel.setBounds(0, 0, screenWidth, screenHeight);
			panel.setBackground(Color.white);
			frame.add(panel);
			runTwo();
			setWord();
			frame.repaint();
			runTwo();
		}
		if(e.getSource()==onePlayer) {
			frame.remove(panel);
			panel = new JPanel();
			panel.setBounds(0, 0, screenWidth, screenHeight);
			panel.setBackground(Color.white);
			frame.add(panel);
			setWord();
			frame.repaint();
			runOne();
		}
		else if(e.getSource()==twoPlayer) {
			panel.remove(onePlayer);
			panel.remove(twoPlayer);
			panel.remove(label);
			player1Name = new JTextField();
			player1Name.setBounds(700, 200, 300, 50);
			player1Name.setFont(new Font("Courier", Font.BOLD, 50));
			panel.add(player1Name);
			player2Name = new JTextField();
			player2Name.setBounds(700, 300, 300, 50);
			player2Name.setFont(new Font("Courier", Font.BOLD, 50));
			panel.add(player2Name);
			name1 = new JLabel("Player 1 Name: ");
			name1.setBounds(275, 200, 500, 50);
			name1.setFont(new Font("Courier", Font.BOLD, 50));
			panel.add(name1);
			name2 = new JLabel("Player 2 Name: ");
			name2.setBounds(275, 300, 500, 50);
			name2.setFont(new Font("Courier", Font.BOLD, 50));
			panel.add(name2);
			next = new JButton("Next");
			next.setBounds(275, 400, 725, 100);
			next.addActionListener(this);
			panel.add(next);
			frame.repaint();
		}
		if(e.getSource()==playAgain) {
			if(isTwo==false) {
			panel.remove(playAgain);
			panel.remove(homeScreen);
			setWord();
			frame.requestFocus();
			frame.repaint();
			}
			else {
				panel.remove(playAgain);
				panel.remove(homeScreen);
				canRun=false;
				
			if(whichPlayer==0) {
				whichPlayer = 1;
				enterWord = new JLabel(pName2+", enter a word for "+pName1+". Maximum of 12 letters.", SwingConstants.CENTER);
			}
			else if(whichPlayer==1) {
				whichPlayer = 0;
				enterWord = new JLabel(pName1+", enter a word for "+pName2+". Maximum of 12 letters.", SwingConstants.CENTER);
			}
			enterWord.setBounds(0, 200, screenWidth, 100);
			enterWord.setFont(new Font("Courier", Font.BOLD, 25));
			panel.add(enterWord);
			twoPlayerWord = new JTextField();
			twoPlayerWord.setBounds(535, 300, 300, 50);
			twoPlayerWord.setFont(new Font("Courier", Font.BOLD, 25));
			panel.add(twoPlayerWord);
			next2 = new JButton("Next");
			next2.setBounds(535, 385, 300, 50);
			panel.add(next2);
			next2.addActionListener(this);
			frame.repaint();
			}
		}
		if(e.getSource()==homeScreen) {
			panel.remove(playAgain);
			panel.remove(homeScreen);
			canRun = false;
			frame.remove(panel);
			panel = new JPanel();
			panel.setBounds(0, 0, screenWidth, screenHeight);
			panel.setBackground(Color.white);
			panel.setLayout(null);
			label = new JLabel("Hangman");
			label.setBounds(475, 0, 500, 100);
			label.setFont(new Font("Courier", Font.BOLD, 100));
			panel.add(label);
			onePlayer = new JButton("One Player");
			onePlayer.setBounds(480, 200, 410, 100);
			panel.add(onePlayer);
			onePlayer.addActionListener(this);
			twoPlayer = new JButton("Two Player");
			twoPlayer.setBounds(480, 300, 410, 100);
			panel.add(twoPlayer);
			twoPlayer.addActionListener(this);
			frame.add(panel);
			youScore = 0;
			compScore = 0;
			isTwo = false;
			frame.repaint();
		}
	}
}