package dad.fam_com_cristo.gui;

import java.awt.Dimension;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import dad.fam_com_cristo.Main;

import java.awt.Font;

/**
 * Classe que representa a SplashScreen de abertura do programa.
 * @author D�rio Pereira
 *
 */
public class Splash extends JWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1303656694735837681L;
	private JProgressBar progressBar;

	/**
	 * Create the frame.
	 */
	public Splash() {
		setMinimumSize(new Dimension(675, 420));
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(50, 360, 575, 35);
		progressBar.setStringPainted(true);
		getContentPane().add(progressBar);
		
		JLabel lblNewLabel = new JLabel(Main.TITLE);
		lblNewLabel.setFont(new Font("Dialog", Font.PLAIN, 15));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(50, 290, 575, 35);
		getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setIcon(new ImageIcon(Splash.class.getResource("/splash.gif")));
		lblNewLabel_1.setBounds(0, 0, 675, 280);
		getContentPane().add(lblNewLabel_1);
		
		JLabel lblVerso = new JLabel("Vers\u00E3o " + Main.VERSION);
		lblVerso.setHorizontalAlignment(SwingConstants.CENTER);
		lblVerso.setFont(new Font("Roboto Medium", Font.PLAIN, 13));
		lblVerso.setBounds(250, 330, 175, 20);
		getContentPane().add(lblVerso);

	}
	
	/**
	 * Incrementa a porcentagem para o valor definido em i.
	 * @param i - valor que se pretende ficar na barra de progresso.
	 */
	public void incrementar(int i){
		progressBar.setValue(i);
	}
}
