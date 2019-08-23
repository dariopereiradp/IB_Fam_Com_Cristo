package dad.fam_com_cristo.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import java.util.Date;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.MaskFormatter;

import com.toedter.calendar.JDateChooser;

import dad.fam_com_cristo.Membro;
import dad.fam_com_cristo.table.AtualizaMembro;
import dad.fam_com_cristo.table.CompositeCommand;
import dad.fam_com_cristo.table.TableModelMembro;
import dad.recursos.ImageViewer;
import mdlaf.animation.MaterialUIMovement;
import mdlaf.utils.MaterialColors;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import dad.fam_com_cristo.Tipo_Membro;
import dad.fam_com_cristo.Estado_Civil;
import dad.fam_com_cristo.Sexo;
import dad.fam_com_cristo.Sim_Nao;

import javax.swing.border.MatteBorder;

public class MembroDetail extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3749457691601361568L;
	private Membro membro;
	private JTextField nome, profissao, endereco;
	private JTextArea observacoes;
	private JFormattedTextField telefone;
	private JDateChooser data_nascimento, data_batismo, membro_desde;
	private JTextField email;
	private JComboBox<Sexo> sexo;
	private JComboBox<String> igreja_origem;
	private JComboBox<Tipo_Membro> tipo_membro;
	private JComboBox<Estado_Civil> estado_civil;
	private JComboBox<Sim_Nao> batizado_cb;
	private JButton ok, b_imprimir, editar, salvar, apagar;
	private JLabel image;
	private JTextField idade;
	private JLabel lblDataDeBatismo, lblMembroDesde;

	public MembroDetail(Membro membro) {
		super(DataGui.getInstance(), ModalityType.DOCUMENT_MODAL);
		this.membro = membro;
		System.out.println(membro);
		this.setTitle(membro.getNome());
		inicializar();
		preencher();
		savedState();
	}

	public MembroDetail() {
		super(DataGui.getInstance(), "Novo Membro", ModalityType.DOCUMENT_MODAL);
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {

			}

		});
		inicializar();
		editState();
	}

	private void inicializar() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(new Dimension(1000, 520));
		setMinimumSize(new Dimension(1000, 520));
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());

		JPanel principal = new JPanel(new BorderLayout());
		JPanel botoesPrincipais = new JPanel();
		JPanel cimaPanel = new JPanel(new BorderLayout());
		JPanel infoPanelWithButtons = new JPanel(new BorderLayout());
		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(new MatteBorder(1, 1, 1, 0, (Color) new Color(0, 0, 0)));
		JPanel rightPanel = new JPanel(new BorderLayout());
		JPanel imagePanel = new JPanel(new MigLayout("al center center, wrap, gapy 15"));
		imagePanel.setBorder(new MatteBorder(1, 0, 1, 1, (Color) new Color(0, 0, 0)));
		JPanel botoesSecund = new JPanel(new MigLayout("", "[100px][120px][100px][200px][100px]", "[30px]"));
		botoesSecund.setBorder(new MatteBorder(0, 1, 1, 1, (Color) new Color(0, 0, 0)));
		botoesPrincipais
				.setLayout(new MigLayout("", "[150px][150px][100px][][][][][][][][][][][][][][][150px]", "[30px]"));

		apagar = new JButton("Apagar");
		if (membro == null) {
			apagar.setText("Cancelar");
		}
		apagar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (membro != null) {
					int ok = JOptionPane.showOptionDialog(null, "Tem a certeza que quer apagar esse membro?",
							"Apagar membro", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
							new ImageIcon(getClass().getResource("/FC_SS.jpg")), Main.OPTIONS, Main.OPTIONS[1]);
					if (ok == JOptionPane.YES_OPTION) {
						int[] rows = new int[1];
						rows[0] = TableModelMembro.getInstance().getRow(membro);
						TableModelMembro.getInstance().removerMembro(rows);
						dispose();
					}
				} else {
					dispose();
				}
			}
		});
		botoesPrincipais.add(apagar, "cell 0 0,alignx left,aligny center");
		apagar.setBackground(MaterialColors.RED_400);
		personalizarBotao(apagar);

		ok = new JButton("Ok");
		ok.setBackground(MaterialColors.LIGHT_BLUE_200);
		personalizarBotao(ok);
		botoesPrincipais.add(ok, "cell 17 0,alignx right,aligny center");

		editar = new JButton("Editar");
		editar.setBackground(MaterialColors.YELLOW_300);
		personalizarBotao(editar);
		botoesSecund.add(editar, "cell 0 0");

		b_imprimir = new JButton("Imprimir");
		b_imprimir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				save(false);
				membro.savePdf();
			}
		});
		b_imprimir.setBackground(new Color(50, 205, 50));
		botoesSecund.add(b_imprimir, "flowy,cell 3 0");
		personalizarBotao(b_imprimir);

		salvar = new JButton("Salvar");
		salvar.setBackground(MaterialColors.LIGHT_GREEN_300);
		personalizarBotao(salvar);
		botoesSecund.add(salvar, "cell 17 0");
		salvar.setEnabled(false);

		nome = new JTextField("");
		nome.setBounds(68, 45, 448, 25);
		nome.setEditable(false);
		
		idade = new JTextField("");
		idade.setEditable(false);
		idade.setBounds(70, 186, 44, 25);
		infoPanel.add(idade);

		data_nascimento = new JDateChooser();
		data_nascimento.setBounds(164, 80, 166, 25);
		data_nascimento.setLocale(new Locale("pt", "BR"));
		data_nascimento.setDateFormatString("dd/MM/yyyy");
		data_nascimento.setMaxSelectableDate(new Date());
		data_nascimento.setDate(new Date());

		profissao = new JTextField("");
		profissao.setBounds(603, 45, 191, 25);
		profissao.setEditable(false);

		endereco = new JTextField("");
		endereco.setBounds(120, 114, 672, 25);
		endereco.setEditable(false);

		MaskFormatter maskPhone;

		try {
			maskPhone = new MaskFormatter("(##) # ####-####");
			maskPhone.setCommitsOnValidEdit(true);
			telefone = new JFormattedTextField(maskPhone);
			telefone.setBounds(120, 150, 178, 25);
		} catch (ParseException e1) {
			telefone = new JFormattedTextField();
			e1.printStackTrace();
		}
		infoPanel.setLayout(null);

		telefone.setFont(new Font("Arial", Font.PLAIN, 15));
		telefone.setColumns(12);
		infoPanel.add(telefone);

		JLabel label = new JLabel("Nome: ");
		label.setBounds(5, 45, 63, 25);
		infoPanel.add(label);
		infoPanel.add(nome);
		JLabel label_1 = new JLabel("Data de Nascimento: ");
		label_1.setBounds(5, 80, 159, 25);
		infoPanel.add(label_1);
		infoPanel.add(data_nascimento);
		JLabel label_2 = new JLabel("Profissão: ");
		label_2.setBounds(524, 45, 75, 25);
		infoPanel.add(label_2);
		infoPanel.add(profissao);
		JLabel label_3 = new JLabel("Endereço: ");
		label_3.setBounds(5, 115, 105, 25);
		infoPanel.add(label_3);
		infoPanel.add(endereco);
		JLabel label_4 = new JLabel("Igreja de Origem: ");
		label_4.setBounds(5, 255, 149, 25);
		infoPanel.add(label_4);
		JLabel label_5 = new JLabel("Observações: ");
		label_5.setBounds(4, 335, 110, 60);
		infoPanel.add(label_5);
		JLabel label_6 = new JLabel("Motivo de Saída: ");
		label_6.setBounds(668, -43, 83, 14);
		infoPanel.add(label_6);
		JLabel lblEstadoCivil = new JLabel("Sexo:");
		lblEstadoCivil.setBounds(340, 80, 63, 25);
		infoPanel.add(lblEstadoCivil);

		infoPanelWithButtons.add(infoPanel, BorderLayout.CENTER);

		observacoes = new JTextArea();
		observacoes.setLineWrap(true);
		JScrollPane jsp = new JScrollPane(observacoes);
		jsp.setBorder(new LineBorder(Color.BLACK, 1));
		jsp.setBounds(118, 335, 672, 60);

		infoPanel.add(jsp);

		sexo = new JComboBox<Sexo>();
		sexo.setBounds(405, 80, 130, 25);
		sexo.setModel(new DefaultComboBoxModel<Sexo>(Sexo.values()));
		sexo.setSelectedIndex(0);
		infoPanel.add(sexo);

		JLabel lInfo = new JLabel("Informa\u00E7\u00F5es Pessoais");
		lInfo.setBounds(10, 8, 250, 32);
		lInfo.setFont(new Font("Dialog", Font.BOLD, 17));
		infoPanel.add(lInfo);

		JLabel lInfo2 = new JLabel("Vida Crist\u00E3");
		lInfo2.setBounds(6, 210, 250, 32);
		lInfo2.setFont(new Font("Dialog", Font.BOLD, 17));
		infoPanel.add(lInfo2);

		JLabel lblTelefone = new JLabel("Telefone: ");
		lblTelefone.setBounds(5, 150, 105, 25);
		infoPanel.add(lblTelefone);

		igreja_origem = new JComboBox<String>();
		igreja_origem.setBounds(164, 255, 340, 25);
		igreja_origem
				.setModel(new DefaultComboBoxModel<String>(new String[] { "Igreja Batista Fam\u00EDlias com Cristo" }));
		igreja_origem.setSelectedIndex(0);
		igreja_origem.setEditable(true);
		infoPanel.add(igreja_origem);

		JLabel lblBatizado = new JLabel("Batizado? ");
		lblBatizado.setBounds(319, 295, 84, 25);
		infoPanel.add(lblBatizado);

		JLabel lblTipo = new JLabel("Tipo:");
		lblTipo.setBounds(514, 255, 56, 25);
		infoPanel.add(lblTipo);

		tipo_membro = new JComboBox<Tipo_Membro>();
		tipo_membro.setBounds(572, 255, 220, 25);
		tipo_membro.setModel(new DefaultComboBoxModel<Tipo_Membro>(Tipo_Membro.values()));
		tipo_membro.setSelectedIndex(0);
		infoPanel.add(tipo_membro);

		JLabel label_7 = new JLabel("Estado Civil:");
		label_7.setBounds(545, 80, 95, 25);
		infoPanel.add(label_7);

		estado_civil = new JComboBox<Estado_Civil>();
		estado_civil.setBounds(643, 80, 149, 25);
		estado_civil.setModel(new DefaultComboBoxModel<Estado_Civil>(Estado_Civil.values()));
		estado_civil.setSelectedIndex(0);
		infoPanel.add(estado_civil);

		data_batismo = new JDateChooser();
		data_batismo.setBounds(601, 295, 191, 25);
		data_batismo.setLocale(new Locale("pt", "BR"));
		data_batismo.setDateFormatString("dd/MM/yyyy");
		data_batismo.setEnabled(false);
		data_batismo.setDate(new Date());
		infoPanel.add(data_batismo);

		lblDataDeBatismo = new JLabel("Data de Batismo: ");
		lblDataDeBatismo.setBounds(466, 295, 132, 25);
		infoPanel.add(lblDataDeBatismo);

		lblMembroDesde = new JLabel("Membro desde:");
		lblMembroDesde.setBounds(5, 295, 130, 25);
		infoPanel.add(lblMembroDesde);

		membro_desde = new JDateChooser();
		membro_desde.setLocale(new Locale("pt", "BR"));
		membro_desde.setDateFormatString("dd/MM/yyyy");
		membro_desde.setBounds(135, 295, 178, 25);
		infoPanel.add(membro_desde);
		membro_desde.setDate(new Date());
		membro_desde.setEnabled(false);

		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setBounds(317, 150, 86, 25);
		infoPanel.add(lblEmail);

		email = new JTextField((String) null);
		email.setEditable(false);
		email.setBounds(405, 150, 387, 25);
		infoPanel.add(email);

		batizado_cb = new JComboBox<Sim_Nao>();
		batizado_cb.setModel(new DefaultComboBoxModel<Sim_Nao>(Sim_Nao.values()));
		batizado_cb.setSelectedIndex(0);
		batizado_cb.setEnabled(false);
		batizado_cb.setBounds(393, 295, 70, 25);
		infoPanel.add(batizado_cb);
		
		JLabel lblIdade = new JLabel("Idade: ");
		lblIdade.setBounds(5, 186, 63, 25);
		infoPanel.add(lblIdade);

		tipo_membro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tipo_membro.getSelectedIndex() == 1 || tipo_membro.getSelectedIndex() == 2
						|| tipo_membro.getSelectedIndex() == 3) {
					batizado_cb.setSelectedIndex(1);
					data_batismo.setEnabled(true);
					membro_desde.setEnabled(true);
					batizado_cb.setEnabled(false);
					data_batismo.setVisible(true);
					lblDataDeBatismo.setVisible(true);
					lblMembroDesde.setVisible(true);
					membro_desde.setVisible(true);
				} else if (tipo_membro.getSelectedIndex() == 0) {
					batizado_cb.setSelectedIndex(0);
					data_batismo.setEnabled(false);
					membro_desde.setEnabled(false);
					batizado_cb.setEnabled(false);
					data_batismo.setVisible(false);
					lblDataDeBatismo.setVisible(false);
					lblMembroDesde.setVisible(false);
					membro_desde.setVisible(false);
				}
				if (tipo_membro.getSelectedIndex() == 4) {
					batizado_cb.setSelectedIndex(0);
					membro_desde.setEnabled(true);
					data_batismo.setEnabled(false);
					batizado_cb.setEnabled(true);
					lblMembroDesde.setVisible(true);
					membro_desde.setVisible(true);
				}
			}
		});

		batizado_cb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (batizado_cb.getSelectedIndex() == 0) {
					data_batismo.setEnabled(false);
					data_batismo.setVisible(false);
					lblDataDeBatismo.setVisible(false);
				} else {
					data_batismo.setEnabled(true);
					data_batismo.setVisible(true);
					lblDataDeBatismo.setVisible(true);
				}
			}
		});

		infoPanelWithButtons.add(botoesSecund, BorderLayout.SOUTH);

		image = new JLabel();
		image.setHorizontalAlignment(JLabel.CENTER);
		image.setVerticalAlignment(JLabel.CENTER);
		image.setMinimumSize(new Dimension(177, 236));
		if (membro != null && membro.getImg() != null)
			image.setIcon(new ImageIcon(membro.getImg().getImage().getScaledInstance(177, 236, Image.SCALE_DEFAULT)));
		else
			image.setText("         Sem Imagem         ");
		image.setBorder(new LineBorder(Color.BLACK, 3));

		final class Add implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (save(false)) {
					membro.addImg();
					if (membro.getImg() != null) {
						image.setText(null);
						image.setIcon(new ImageIcon(
								membro.getImg().getImage().getScaledInstance(177, 236, Image.SCALE_DEFAULT)));
					}
					editState();
				}
			}
		}

		final class Apagar implements ActionListener {

			public void apagar() {
				int ok = JOptionPane.showOptionDialog(DataGui.getInstance(),
						"Tem certeza que quer apagar a foto do membro?\n(Não é possível voltar atrás, a não ser adicionando uma nova imagem!)",
						"APAGAR", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
						new ImageIcon(getClass().getResource("/DAD_SS.jpg")), Main.OPTIONS, Main.OPTIONS[1]);
				if (ok == JOptionPane.YES_OPTION) {
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
		if (membro != null && membro.getImg() == null) {
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
				editState();
			}
		});

		final class Salvar implements ActionListener {

			private boolean close;

			public Salvar(boolean close) {
				this.close = close;
			}

			@Override
			public void actionPerformed(ActionEvent e) {
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

				if (salvar.isEnabled()) {
					int ok = JOptionPane.showOptionDialog(null,
							"Tem certeza que quer fechar essa janela? Todas as informações não salvas serão perdidas!",
							"Fechar", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
							new ImageIcon(getClass().getResource("/FC_SS.jpg")), Main.OPTIONS, Main.OPTIONS[1]);
					if (ok == JOptionPane.YES_OPTION) {
						dispose();
					}
				} else
					dispose();
			}

		});

		addImage.addActionListener(new Add());

	}

	protected void editState() {
		nome.setEditable(true);
		profissao.setEditable(true);
		data_nascimento.setEnabled(true);
		sexo.setEnabled(true);
		estado_civil.setEnabled(true);
		endereco.setEditable(true);
		telefone.setEditable(true);
		email.setEditable(true);
		igreja_origem.setEnabled(true);
		tipo_membro.setEnabled(true);
		membro_desde.setEnabled(true);
		data_batismo.setEnabled(true);
		observacoes.setEditable(true);
		if(membro!=null && membro.getTipo_membro()==Tipo_Membro.EX_MEMBRO)
			batizado_cb.setEnabled(true);
		if(batizado_cb.getSelectedIndex()==0){
			data_batismo.setEnabled(false);
			data_batismo.setVisible(false);
			lblDataDeBatismo.setVisible(false);
			lblMembroDesde.setVisible(false);
			membro_desde.setVisible(false);
		}
		editar.setEnabled(false);
		salvar.setEnabled(true);
	}

	protected void savedState() {
		nome.setEditable(false);
		profissao.setEditable(false);
		data_nascimento.setEnabled(false);
		sexo.setEnabled(false);
		estado_civil.setEnabled(false);
		endereco.setEditable(false);
		telefone.setEditable(false);
		email.setEditable(false);
		igreja_origem.setEnabled(false);
		tipo_membro.setEnabled(false);
		membro_desde.setEnabled(false);
		data_batismo.setEnabled(false);
		observacoes.setEditable(false);
		batizado_cb.setEnabled(false);
		editar.setEnabled(true);
		salvar.setEnabled(false);
	}

	private void preencher() {
		if (membro != null) {
			nome.setText(membro.getNome());
			profissao.setText(membro.getProfissao());
			data_nascimento.setDate(membro.getData_nascimento());
			sexo.setSelectedItem(membro.getSexo());
			estado_civil.setSelectedItem(membro.getEstado_civil());
			endereco.setText(membro.getEndereco());
			telefone.setText(membro.getTelefone());
			email.setText(membro.getEmail());
			igreja_origem.setSelectedItem(membro.getIgreja_origem());
			tipo_membro.setSelectedItem(membro.getTipo_membro());
			batizado_cb.setSelectedItem(membro.eBatizado());
			membro_desde.setDate(membro.getMembro_desde());
			data_batismo.setDate(membro.getData_batismo());
			observacoes.setText(membro.getObservacoes());
			idade.setText(String.valueOf(membro.getIdade()));
		}
	}

	public boolean save(boolean close) {
		if (nome.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(null, "Nome em branco! Introduza um pelo menos o nome do membro para salvar!",
					"Salvar", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/FC_SS.jpg")));
			return false;
		} else {
			String nome = this.nome.getText();
			Date data_nascimento = this.data_nascimento.getDate();
			Sexo sexo = (Sexo) this.sexo.getSelectedItem();
			Estado_Civil estado_civil = (Estado_Civil) this.estado_civil.getSelectedItem();
			String profissao = this.profissao.getText();
			String endereco = this.endereco.getText();
			String telefone = this.telefone.getText().replace("-", "").replace("(", "").replace(")", "").replace(" ",
					"");
			String email = this.email.getText();
			String igreja_origem = (String) this.igreja_origem.getSelectedItem();
			Tipo_Membro tipo_membro = (Tipo_Membro) this.tipo_membro.getSelectedItem();
			Sim_Nao batizado = (Sim_Nao) batizado_cb.getSelectedItem();
			Date membro_desde = this.membro_desde.getDate();
			Date data_batismo = this.data_batismo.getDate();
			String observacoes = this.observacoes.getText();
			ImageIcon img = (ImageIcon) image.getIcon();

			if (membro == null) {
				membro = new Membro(nome, data_nascimento, sexo, estado_civil, profissao, endereco, telefone, email,
						igreja_origem, tipo_membro, batizado, membro_desde, data_batismo, observacoes, img);
				TableModelMembro.getInstance().addMembro(membro);
				apagar.setText("Apagar");
			} else {
				System.out.println("Aqui");
				TableModelMembro.getInstance().getUndoManager().execute(new CompositeCommand(
						"Atualizar detalhes do membro",
						new AtualizaMembro(TableModelMembro.getInstance(), "Nome", membro, nome),
						new AtualizaMembro(TableModelMembro.getInstance(), "Data_Nascimento", membro, data_nascimento),
						new AtualizaMembro(TableModelMembro.getInstance(), "Sexo", membro, sexo),
						new AtualizaMembro(TableModelMembro.getInstance(), "Estado_Civil", membro, estado_civil),
						new AtualizaMembro(TableModelMembro.getInstance(), "Profissao", membro, profissao),
						new AtualizaMembro(TableModelMembro.getInstance(), "Endereco", membro, endereco),
						new AtualizaMembro(TableModelMembro.getInstance(), "Telefone", membro, telefone),
						new AtualizaMembro(TableModelMembro.getInstance(), "Email", membro, email),
						new AtualizaMembro(TableModelMembro.getInstance(), "Igreja_Origem", membro, igreja_origem),
						new AtualizaMembro(TableModelMembro.getInstance(), "Tipo_Membro", membro, tipo_membro),
						new AtualizaMembro(TableModelMembro.getInstance(), "Batizado", membro, batizado),
						new AtualizaMembro(TableModelMembro.getInstance(), "Membro_Desde", membro, membro_desde),
						new AtualizaMembro(TableModelMembro.getInstance(), "Data_Batismo", membro, data_batismo),
						new AtualizaMembro(TableModelMembro.getInstance(), "Observacoes", membro, observacoes)));
				TableModelMembro.getInstance().fireTableDataChanged();
			}
			savedState();
			idade.setText(String.valueOf(membro.getIdade()));
			if (close) {
				dispose();
			}
			return true;
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
