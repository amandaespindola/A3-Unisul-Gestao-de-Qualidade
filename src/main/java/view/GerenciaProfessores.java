package view;

import dao.ProfessorDAO;
import model.Professor;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import utils.ExcelExporter;
import utils.LookAndFeelHelper;
import utils.ValidadorInput;
import utils.ViewUtils;

public class GerenciaProfessores extends javax.swing.JFrame {

	private final transient ProfessorDAO professorDAO;
	private int linhaSelecionada = -1;

	public GerenciaProfessores() {
		initComponents();
		this.professorDAO = new ProfessorDAO();
		this.carregaTabela();
	}

	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		bCadastro = new javax.swing.JButton();
		bEditar = new javax.swing.JButton();
		bDeletar = new javax.swing.JButton();
		jScrollPane2 = new javax.swing.JScrollPane();
		jTableProfessores = new javax.swing.JTable();
		jLabel1 = new javax.swing.JLabel();
		refresh = new javax.swing.JButton();
		export = new javax.swing.JButton();
		jMenuBar1 = new javax.swing.JMenuBar();
		menu = new javax.swing.JMenu();
		menuGerenciaAluno = new javax.swing.JMenuItem();
		menuExport = new javax.swing.JMenuItem();
		menuRefresh = new javax.swing.JMenuItem();
		jMenuItem1 = new javax.swing.JMenuItem();
		menuLeave = new javax.swing.JMenuItem();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Gerência de Professores");
		setBackground(new java.awt.Color(80, 80, 80));
		setResizable(false);

		bCadastro = ViewUtils.criarBotao("Cadastrar novo", this::bCadastroActionPerformed);

		bEditar = ViewUtils.criarBotao("Editar", this::bEditarActionPerformed);

		bDeletar = ViewUtils.criarBotao("Deletar", this::bDeletarActionPerformed);

		jTableProfessores.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] { { null, null, null, null, null, null, null, null },
						{ null, null, null, null, null, null, null, null },
						{ null, null, null, null, null, null, null, null },
						{ null, null, null, null, null, null, null, null } },
				new String[] { "ID", "Nome", "Idade", "Campus", "CPF", "Contato", "Título", "Salário" }) {
			boolean[] canEdit = new boolean[] { false, false, false, false, false, false, false, false };

            @Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		jTableProfessores.setSelectionForeground(new java.awt.Color(239, 239, 239));
		jTableProfessores.addMouseListener(new java.awt.event.MouseAdapter() {

			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				jTableProfessoresMouseClicked(evt);
			}
		});
		jScrollPane2.setViewportView(jTableProfessores);
		if (jTableProfessores.getColumnModel().getColumnCount() > 0) {
			jTableProfessores.getColumnModel().getColumn(0).setMinWidth(40);
			jTableProfessores.getColumnModel().getColumn(0).setMaxWidth(40);
			jTableProfessores.getColumnModel().getColumn(2).setMinWidth(40);
			jTableProfessores.getColumnModel().getColumn(2).setMaxWidth(40);
			jTableProfessores.getColumnModel().getColumn(7).setMinWidth(75);
			jTableProfessores.getColumnModel().getColumn(7).setMaxWidth(75);
		}

		jLabel1.setFont(new java.awt.Font("Tahoma", 1, 40)); // NOI18N
		jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		jLabel1.setText("Cadastro de Professores");

		refresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/refresh.png"))); // NOI18N
		refresh = ViewUtils.criarBotao(" Atualizar tabela", this::refreshActionPerformed);

		export = ViewUtils.criarBotao("Exportar para Excel", this::exportActionPerformed);

		menu.setForeground(new java.awt.Color(239, 239, 239));
		menu.setText("Arquivo");

		menuGerenciaAluno.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A,
				java.awt.event.InputEvent.CTRL_DOWN_MASK));
		menuGerenciaAluno.setText("Gerência de Alunos");
		menuGerenciaAluno.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menuGerenciaAlunoActionPerformed(evt);
			}
		});
		menu.add(menuGerenciaAluno);

		menuExport = ViewUtils.criarMenuItem("Exportar para Excel", java.awt.event.KeyEvent.VK_E,
				this::menuExportActionPerformed);

		menu.add(menuExport);

		menuRefresh = ViewUtils.criarMenuItem("Atualizar tabela", java.awt.event.KeyEvent.VK_R,
				this::menuRefreshActionPerformed);

		menu.add(menuRefresh);

		jMenuItem1.setText("Sobre");
		jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItem1ActionPerformed(evt);
			}
		});
		menu.add(jMenuItem1);

		menuLeave = ViewUtils.criarMenuItem("Sair", java.awt.event.KeyEvent.VK_S, this::menuLeaveActionPerformed);

		menu.add(menuLeave);

		jMenuBar1.add(menu);

		setJMenuBar(jMenuBar1);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 859, Short.MAX_VALUE)
								.addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addGroup(layout.createSequentialGroup()
										.addComponent(refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 143,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(65, 65, 65)
										.addComponent(bCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 144,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(bEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 144,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(bDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, 144,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(export, javax.swing.GroupLayout.PREFERRED_SIZE, 143,
												javax.swing.GroupLayout.PREFERRED_SIZE)))
						.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 99,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(bCadastro).addComponent(bEditar).addComponent(bDeletar)
										.addComponent(export, javax.swing.GroupLayout.PREFERRED_SIZE, 22,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addComponent(refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 22,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
						.addContainerGap()));

		pack();
		setLocationRelativeTo(null);
	}// </editor-fold>//GEN-END:initComponents

	private void exportXls() throws IOException {
		ExcelExporter.exportTableToExcel(jTableProfessores);
	}

	private void menuGerenciaAlunoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuGerenciaAlunoActionPerformed
		GerenciaAlunos tela = new GerenciaAlunos();
		tela.setVisible(true);
		this.dispose();
	}// GEN-LAST:event_menuGerenciaAlunoActionPerformed

	private void menuLeaveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuLeaveActionPerformed
		System.exit(0);
	}// GEN-LAST:event_menuLeaveActionPerformed

	private void bCadastroActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_bCadastroActionPerformed
		try {
			CadastroProfessor tela = new CadastroProfessor();
			tela.setVisible(true);
		} catch (ParseException ex) {
			Logger.getLogger(GerenciaProfessores.class.getName()).log(Level.SEVERE, null, ex);
		}

	}// GEN-LAST:event_bCadastroActionPerformed

	private void bEditarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_bEditarActionPerformed
		if (this.linhaSelecionada != -1) {

			String[] dadosParaEdicao = new String[8];

			String id = this.jTableProfessores.getValueAt(this.linhaSelecionada, 0).toString();
			String nome = this.jTableProfessores.getValueAt(this.linhaSelecionada, 1).toString();
			String idade = this.jTableProfessores.getValueAt(this.linhaSelecionada, 2).toString();
			String campus = this.jTableProfessores.getValueAt(this.linhaSelecionada, 3).toString();
			String cpf = this.jTableProfessores.getValueAt(this.linhaSelecionada, 4).toString();
			String contato = this.jTableProfessores.getValueAt(this.linhaSelecionada, 5).toString();
			String titulo = this.jTableProfessores.getValueAt(this.linhaSelecionada, 6).toString();
			String salario = this.jTableProfessores.getValueAt(this.linhaSelecionada, 7).toString();

			// Remove máscara do salário usando ValidadorInput
			String salarioLimpo = ValidadorInput.removerMascara(salario);

			dadosParaEdicao[0] = nome;
			dadosParaEdicao[1] = idade;
			dadosParaEdicao[2] = campus;
			dadosParaEdicao[3] = cpf;
			dadosParaEdicao[4] = contato;
			dadosParaEdicao[5] = titulo;
			dadosParaEdicao[6] = salarioLimpo;
			dadosParaEdicao[7] = id;

			try {
				EditarProfessor editar = new EditarProfessor(dadosParaEdicao);
				editar.setVisible(true);
			} catch (ParseException ex) {
				Logger.getLogger(GerenciaProfessores.class.getName()).log(Level.SEVERE, null, ex);
			}

			this.linhaSelecionada = -1;

		} else {
			JOptionPane.showMessageDialog(null, "Selecione um professor na tabela para editar.");
		}
	}// GEN-LAST:event_bEditarActionPerformed

	private void jTableProfessoresMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTableProfessoresMouseClicked
		if (this.jTableProfessores.getSelectedRow() != -1) {
			this.linhaSelecionada = this.jTableProfessores.getSelectedRow();
		}
	}// GEN-LAST:event_jTableProfessoresMouseClicked

	private void bDeletarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_bDeletarActionPerformed
		try {
			int id = 0;

			if (this.jTableProfessores.getSelectedRow() == -1) {
				throw new Mensagens("Selecione um cadastro para deletar");
			} else {
				id = Integer.parseInt(
						this.jTableProfessores.getValueAt(this.jTableProfessores.getSelectedRow(), 0).toString());
			}

			String[] options = { "Sim", "Não" };
			int respostaUsuario = JOptionPane.showOptionDialog(null, "Tem certeza que deseja apagar este cadastro?",
					"Confirmar exclusão", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options,
					options[1]);

			if (respostaUsuario == 0) {
				if (this.professorDAO.delete(id)) {
					JOptionPane.showMessageDialog(rootPane, "Cadastro apagado com sucesso!");
				} else {
					JOptionPane.showMessageDialog(rootPane, "Erro ao apagar o cadastro no banco de dados.");
				}
			}
		} catch (Mensagens erro) {
			JOptionPane.showMessageDialog(rootPane, erro.getMessage());
		} finally {
			carregaTabela();
		}
	}// GEN-LAST:event_bDeletarActionPerformed

	private void refreshActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_refreshActionPerformed
		this.carregaTabela();
	}// GEN-LAST:event_refreshActionPerformed

	private void menuRefreshActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuRefreshActionPerformed
		this.carregaTabela();
	}// GEN-LAST:event_menuRefreshActionPerformed

	private void menuExportActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuExportActionPerformed
		try {
			this.exportXls();
		} catch (IOException ex) {
			Logger.getLogger(GerenciaProfessores.class.getName()).log(Level.SEVERE, null, ex);
		}
	}// GEN-LAST:event_menuExportActionPerformed

	private void exportActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_exportActionPerformed
		menuExportActionPerformed(evt);
	}// GEN-LAST:event_exportActionPerformed

	private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem1ActionPerformed
		Sobre tela = new Sobre();
		tela.setVisible(true);
	}// GEN-LAST:event_jMenuItem1ActionPerformed

	@SuppressWarnings("unchecked")
	public void carregaTabela() {
		DefaultTableModel modelo = (DefaultTableModel) this.jTableProfessores.getModel();
		modelo.setNumRows(0);

		ArrayList<Professor> minhalista = professorDAO.getMinhaLista();

		NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

		for (Professor a : minhalista) {
			modelo.addRow(new Object[] { a.getId(), a.getNome(), a.getIdade(), a.getCampus(), a.getCpf(),
					a.getContato(), a.getTitulo(), nf.format(a.getSalario()) });
		}
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		LookAndFeelHelper.aplicarNimbus();
		java.awt.EventQueue.invokeLater(() -> new GerenciaProfessores().setVisible(true));
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton bCadastro;
	private javax.swing.JButton bDeletar;
	private javax.swing.JButton bEditar;
	private javax.swing.JButton export;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JMenuBar jMenuBar1;
	private javax.swing.JMenuItem jMenuItem1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JTable jTableProfessores;
	private javax.swing.JMenu menu;
	private javax.swing.JMenuItem menuExport;
	private javax.swing.JMenuItem menuGerenciaAluno;
	private javax.swing.JMenuItem menuLeave;
	private javax.swing.JMenuItem menuRefresh;
	private javax.swing.JButton refresh;
	// End of variables declaration//GEN-END:variables
}
