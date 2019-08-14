package dad.fam_com_cristo.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.ParseException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.MaskFormatter;

import com.toedter.calendar.JDateChooser;

import dad.fam_com_cristo.Membro;
import dad.fam_com_cristo.table.TableModelMembro;
import dad.recursos.ImageViewer;
import mdlaf.animation.MaterialUIMovement;
import mdlaf.utils.MaterialColors;
import net.miginfocom.swing.MigLayout;

public class MembroDetail extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3749457691601361568L;
	private Membro membro;
	private JTextField nome, profissao, endereco, igreja_origem, observacoes, motivo_saida, casado;
	private JFormattedTextField telefone;
	private JDateChooser data_nascimento, data_batismo, data_termino;

	public MembroDetail(Membro membro) {
		this.membro = membro;
		System.out.println(membro);
		this.setTitle(membro.getNome());
		setSize(new Dimension(750, 500));
		setMinimumSize(new Dimension(750, 500));
		getContentPane().setLayout(new BorderLayout());

		JPanel principal = new JPanel(new BorderLayout());
		JPanel botoesPrincipais = new JPanel();
		// emprestimos = EmprestimoPanel.getInstance().getSmallTable(l);
		JPanel cimaPanel = new JPanel(new BorderLayout());
		JPanel infoPanelWithButtons = new JPanel(new BorderLayout());
		JPanel infoPanel = new JPanel(new GridLayout(10, 2));
		JPanel rightPanel = new JPanel(new BorderLayout());
		JPanel imagePanel = new JPanel(new MigLayout("al center center, wrap, gapy 15"));
		JPanel botoesSecund = new JPanel(new BorderLayout());
		botoesPrincipais
				.setLayout(new MigLayout("", "[79px][129px][45px][][][][][][][][][][150px][][][][][]", "[27px]"));
		botoesSecund.setLayout(new MigLayout("", "[79px][100px][][240px][][][][][]", "[27px]"));

		JButton apagar = new JButton("Apagar");
		apagar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int ok = JOptionPane.showConfirmDialog(null, "Tem a certeza que quer apagar esse livro?",
						"Apagar livro", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
						new ImageIcon(getClass().getResource("/DAD_SS.jpg")));
				if (ok == JOptionPane.YES_OPTION) {
					int[] rows = new int[1];
					rows[0] = TableModelMembro.getInstance().getRow(membro);
					TableModelMembro.getInstance().removeUser(rows);
					dispose();
				}
			}
		});
		botoesPrincipais.add(apagar, "cell 0 0,alignx left,aligny center");
		apagar.setBackground(MaterialColors.RED_400);
		personalizarBotao(apagar);

		JButton ok = new JButton("Ok");
		ok.setBackground(MaterialColors.LIGHT_BLUE_200);
		personalizarBotao(ok);
		botoesPrincipais.add(ok, "cell 17 0,alignx left,aligny center");

		JButton editar = new JButton("Editar");
		editar.setBackground(MaterialColors.YELLOW_300);
		personalizarBotao(editar);
		botoesSecund.add(editar, "cell 0 0,alignx left,aligny center");

		JButton salvar = new JButton("Salvar");
		salvar.setBackground(MaterialColors.LIGHT_GREEN_300);
		personalizarBotao(salvar);
		botoesSecund.add(salvar, "cell 17 0,alignx left,aligny center");
		salvar.setEnabled(false);

		nome = new JTextField(membro.getNome());
		nome.setEditable(false);
		profissao = new JTextField(membro.getProfissao());
		profissao.setEditable(false);
		endereco = new JTextField(membro.getEndereco());
		endereco.setEditable(false);
		igreja_origem = new JTextField(membro.getIgreja_origem());
		igreja_origem.setEditable(false);
		observacoes = new JTextField(String.valueOf(membro.getObservacoes()));
		observacoes.setEditable(false);
		motivo_saida = new JTextField(String.valueOf(membro.getMotivo_saida()));
		motivo_saida.setEditable(false);
		casado = new JTextField(membro.isCasado() ? "Sim" : "Não");
		casado.setEditable(false);
		
		MaskFormatter maskPhone;

		try {
			maskPhone = new MaskFormatter("(##) # ####-####");
			maskPhone.setCommitsOnValidEdit(true);
			telefone = new JFormattedTextField(maskPhone);
		} catch (ParseException e1) {
			telefone = new JFormattedTextField();
			e1.printStackTrace();
		}
		
		telefone.setFont(new Font("Arial", Font.PLAIN, 15));
		telefone.setBounds(90, 162, 181, 20);
		telefone.setColumns(12);
		telefone.setText(membro.getTelefone());
		telefone.setEditable(false);

		infoPanel.add(new JLabel("Nome: "));
		infoPanel.add(nome);
		infoPanel.add(new JLabel("Profissão: "));
		infoPanel.add(profissao);
		infoPanel.add(new JLabel("Endereço: "));
		infoPanel.add(endereco);
		infoPanel.add(new JLabel("Igreja de Origem: "));
		infoPanel.add(igreja_origem);
		infoPanel.add(new JLabel("Observações: "));
		infoPanel.add(observacoes);
		infoPanel.add(new JLabel("Motivo de Saída: "));
		infoPanel.add(motivo_saida);
		infoPanel.add(new JLabel("Casado? "));
		infoPanel.add(casado);

		infoPanelWithButtons.add(infoPanel, BorderLayout.CENTER);
		infoPanelWithButtons.add(botoesSecund, BorderLayout.SOUTH);

		JLabel image = new JLabel();
		image.setHorizontalAlignment(JLabel.CENTER);
		image.setVerticalAlignment(JLabel.CENTER);
		image.setMinimumSize(new Dimension(177, 236));
		if (membro.getImg() != null)
			image.setIcon(new ImageIcon(membro.getImg().getImage().getScaledInstance(177, 236, Image.SCALE_DEFAULT)));
		else
			image.setText("         Sem Imagem         ");
		image.setBorder(new LineBorder(Color.BLACK, 3));

		final class Add implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				membro.addImg();
				if (membro.getImg() != null) {
					image.setText(null);
					image.setIcon(
							new ImageIcon(membro.getImg().getImage().getScaledInstance(177, 236, Image.SCALE_DEFAULT)));
				}

			}
		}

		final class Apagar implements ActionListener {

			public void apagar() {
				int ok = JOptionPane.showConfirmDialog(DataGui.getInstance(),
						"Tem certeza que quer apagar a imagem do livro?\n(Não é possível voltar atrás, a não ser adicionando uma nova imagem!)",
						"APAGAR", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,
						new ImageIcon(getClass().getResource("/DAD_SS.jpg")));
				if (ok == JOptionPane.OK_OPTION) {
					membro.setImg(null);
					image.setIcon(null);
					File f = new File(Membro.imgPath + membro.getId() + ".jpg");
					f.delete();
				}
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				apagar();
			}
		}

		JPopupMenu menuApagar = new JPopupMenu();
		JMenuItem mAdd = new JMenuItem();
		if (membro.getImg() == null) {
			mAdd.setText("Adicionar Imagem");
		} else
			mAdd.setText("Alterar Imagem");
		mAdd.addActionListener(new Add());
		menuApagar.add(mAdd);
		JMenuItem mApagar = new JMenuItem("Apagar");
		menuApagar.add(mApagar);
		mApagar.addActionListener(new Apagar());

		menuApagar.addPopupMenuListener(new PopupMenuListener() {

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						if (image.getIcon() == null) {
							mAdd.setText("Adicionar Imagem");
							mApagar.setEnabled(false);
						} else {
							mAdd.setText("Alterar Imagem");
							mApagar.setEnabled(true);
						}
					}
				});
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {

			}
		});

		image.setComponentPopupMenu(menuApagar);

		image.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				int count = evt.getClickCount();
				if (count == 2) {
					ImageViewer.show(membro.getImg());
				}
			}

		});

		JButton addImage = new JButton();
		if (image.getIcon() == null)
			addImage.setText("Adicionar Imagem");
		else
			addImage.setText("Alterar imagem");
		addImage.setBackground(MaterialColors.BLUE_GRAY_500);
		personalizarBotao(addImage);
		imagePanel.add(image);
		imagePanel.add(addImage, "center");

		rightPanel.add(imagePanel, BorderLayout.CENTER);

		cimaPanel.add(infoPanelWithButtons, BorderLayout.CENTER);
		cimaPanel.add(rightPanel, BorderLayout.EAST);

		principal.add(cimaPanel, BorderLayout.CENTER);

		getContentPane().add(principal, BorderLayout.CENTER);
		getContentPane().add(botoesPrincipais, BorderLayout.SOUTH);

		PropertyChangeListener listener = new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (image.getIcon() == null) {
					addImage.setText("Adicionar Imagem");
					image.setText("         Sem Imagem         ");
				} else {
					addImage.setText("Alterar Imagem");
					image.setText("");
				}

			}
		};

		image.addPropertyChangeListener(listener);

		editar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				nome.setEditable(true);
				profissao.setEditable(true);
				endereco.setEditable(true);
				igreja_origem.setEditable(true);
				observacoes.setEditable(true);
				editar.setEnabled(false);
				salvar.setEnabled(true);

			}
		});

		final class Salvar implements ActionListener {

			private boolean close;

			public Salvar(boolean close) {
				this.close = close;
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				editar.setEnabled(true);
				salvar.setEnabled(false);
				if (close && nome.isEditable())
					save(close);
				else if (close && !nome.isEditable()) {
					dispose();
				} else
					save(close);
			}
		}

		salvar.addActionListener(new Salvar(false));

		ok.addActionListener(new Salvar(true));

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				if (nome.isEditable())
					save(true);
				else {
					dispose();
				}
			}

		});

		addImage.addActionListener(new Add());
	}

	public void save(boolean close) {
		nome.setEditable(false);
		profissao.setEditable(false);
		endereco.setEditable(false);
		igreja_origem.setEditable(false);
		observacoes.setEditable(false);

		TableModelMembro.getInstance().fireTableDataChanged();

		if (close) {
			dispose();
		}

	}

	public void personalizarBotao(JButton jb) {
		jb.setFont(new Font("Roboto", Font.PLAIN, 15));
		MaterialUIMovement.add(jb, MaterialColors.GRAY_300, 5, 1000 / 30);
	}

	public void open() {
		setVisible(true);

	}

}
