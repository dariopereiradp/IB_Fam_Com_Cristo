package dad.fam_com_cristo.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import dad.fam_com_cristo.table.models.TableModelFuncionario;
import dad.recursos.Utils;
import mdlaf.utils.MaterialImageFactory;
import mdlaf.utils.icons.MaterialIconFont;

import javax.swing.JTable;

/**
 * Classe que mostra os funcionários que estão registrados no sistema.
 * @author Dário Pereira
 *
 */
public class GerirFuncionarios extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7591257121742629450L;
	private final JPanel contentPanel = new JPanel();
	private JTable table;


	/**
	 * Create the dialog.
	 */
	public GerirFuncionarios() {
		super(DataGui.getInstance(), ModalityType.DOCUMENT_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Gerir Funcionários");
		setBounds(100, 100, 1100, 350);
		setMinimumSize(new Dimension(1100, 350));
		getContentPane().setLayout(new BorderLayout());
		getRootPane().setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED));
		contentPanel.setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			table = TableModelFuncionario.getInstance().uploadDataBase().getSmallTable();
			JScrollPane jsp = new JScrollPane(table);
			contentPanel.add(jsp, BorderLayout.CENTER);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setIcon(MaterialImageFactory.getInstance().getImage(
		                MaterialIconFont.CHECK,
		                Utils.getInstance().getCurrentTheme().getColorIcons()));
				Utils.personalizarBotao(okButton);
				okButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
						
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
	
	/**
	 * Torna o diálogo visível.
	 */
	public void open(){
		setVisible(true);
	}

}
