package view;

import dao.AlunoDAO;
import model.Aluno;
import utils.ValidadorInput;
import utils.ViewUtils;

import java.io.IOException;
import java.util.ArrayList;

import utils.ExcelExporter;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class GerenciaAlunos extends javax.swing.JFrame {

	private final transient AlunoDAO alunoDAO;
	private int linhaSelecionada = -1;

	public GerenciaAlunos() {
		initComponents();
		this.alunoDAO = new AlunoDAO();
		this.carregaTabela();
	}

	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		bCadastro = new javax.swing.JButton();
		bEditar = new javax.swing.JButton();
		bDeletar = new javax.swing.JButton();
		jScrollPane2 = new javax.swing.JScrollPane();
		jTableAlunos = new javax.swing.JTable();
		jLabel1 = new javax.swing.JLabel();
		refresh = new javax.swing.JButton();
		export = new javax.swing.JButton();
		jMenuBar1 = new javax.swing.JMenuBar();
		menu = new javax.swing.JMenu();
		menuGerenciaProfessores = new javax.swing.JMenuItem();
		menuExport = new javax.swing.JMenuItem();
		menuRefresh = new javax.swing.JMenuItem();
		jMenuItem1 = new javax.swing.JMenuItem();
		menuLeave = new javax.swing.JMenuItem();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Gerência de Alunos");
		setResizable(false);

		bCadastro.setText("Cadastrar novo");
		bCadastro.addActionListener(this::bCadastroActionPerformed);

		bEditar.setText("Editar");
		bEditar.addActionListener(this::bEditarActionPerformed);

		bDeletar.setText("Deletar");
		bDeletar.addActionListener(this::bDeletarActionPerformed);

		jTableAlunos.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {},
				new String[] { "ID", "Nome", "Idade", "Curso", "Fase" }) {
			boolean[] canEdit = new boolean[] { false, false, false, false, true };

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		jTableAlunos.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				jTableAlunosMouseClicked(evt);
			}
		});
		jScrollPane2.setViewportView(jTableAlunos);

		jLabel1.setFont(new java.awt.Font("Tahoma", 1, 40)); // NOI18N
		jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		jLabel1.setText("Cadastro de Alunos");

		refresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/refresh.png"))); // ✅ caminho corrigido
		refresh.setText("  Atualizar tabela");

		export.setText("Exportar para Excel");

		// ✅ Centraliza configuração dos botões e menus
		ViewUtils.configurarBotoesGerencia(refresh, menuRefresh, menuExport, this::exportXls, this::carregaTabela);

		menu.setText("Arquivo");

		menuGerenciaProfessores.setText("Gerência de Professores");
		menuGerenciaProfessores.addActionListener(this::menuGerenciaProfessoresActionPerformed);
		menu.add(menuGerenciaProfessores);

		menuExport.setText("Exportar para Excel");
		menu.add(menuExport);

		menuRefresh.setText("Atualizar tabela");
		menu.add(menuRefresh);

		jMenuItem1.setText("Sobre");
		jMenuItem1.addActionListener(this::jMenuItem1ActionPerformed);
		menu.add(jMenuItem1);

		menuLeave.setText("Sair");
		menuLeave.addActionListener(this::menuLeaveActionPerformed);
		menu.add(menuLeave);

		jMenuBar1.add(menu);
		setJMenuBar(jMenuBar1);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 859, Short.MAX_VALUE)
						.addGroup(layout.createSequentialGroup()
								.addComponent(refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 143,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(64, 64, 64)
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
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE))
						.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap()
				.addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 99,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(bCadastro)
						.addComponent(bEditar).addComponent(bDeletar).addComponent(refresh).addComponent(export,
								javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
				.addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
				.addContainerGap()));

		pack();
		setLocationRelativeTo(null);
		java.util.Objects
				.requireNonNull((java.util.function.Consumer<java.awt.event.ActionEvent>) this::refreshActionPerformed);
		java.util.Objects.requireNonNull(
				(java.util.function.Consumer<java.awt.event.ActionEvent>) this::menuRefreshActionPerformed);
		java.util.Objects.requireNonNull(
				(java.util.function.Consumer<java.awt.event.ActionEvent>) this::menuExportActionPerformed);
		java.util.Objects
				.requireNonNull((java.util.function.Consumer<java.awt.event.ActionEvent>) this::exportActionPerformed);
	}// </editor-fold>//GEN-END:initComponents

	private void exportXls() {
		try {
			ExcelExporter.exportTableToExcel(jTableAlunos);
			JOptionPane.showMessageDialog(this, "Arquivo exportado com sucesso!");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Erro ao exportar arquivo: " + e.getMessage(), "Erro de Exportação",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void menuGerenciaProfessoresActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuGerenciaProfessoresActionPerformed
		GerenciaProfessores tela = new GerenciaProfessores();
		tela.setVisible(true);
		this.dispose();
	}// GEN-LAST:event_menuGerenciaProfessoresActionPerformed

	private void menuLeaveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuLeaveActionPerformed
		System.exit(0);
	}// GEN-LAST:event_menuLeaveActionPerformed

	private void bCadastroActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_bCadastroActionPerformed
		CadastroAluno tela = new CadastroAluno();
		tela.setVisible(true);

	}// GEN-LAST:event_bCadastroActionPerformed

	private void bEditarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_bEditarActionPerformed
		if (this.linhaSelecionada != -1) {

			String[] dadosParaEdicao = new String[5];

			String id = this.jTableAlunos.getValueAt(this.linhaSelecionada, 0).toString();
			String nome = this.jTableAlunos.getValueAt(this.linhaSelecionada, 1).toString();
			String idade = this.jTableAlunos.getValueAt(this.linhaSelecionada, 2).toString();
			String curso = this.jTableAlunos.getValueAt(this.linhaSelecionada, 3).toString();
			String fase = this.jTableAlunos.getValueAt(this.linhaSelecionada, 4).toString();

			String faseLimpa = ValidadorInput.removerMascara(fase);

			dadosParaEdicao[0] = id;
			dadosParaEdicao[1] = nome;
			dadosParaEdicao[2] = idade;
			dadosParaEdicao[3] = curso;
			dadosParaEdicao[4] = faseLimpa;

			EditarAluno editar = new EditarAluno(dadosParaEdicao);
			editar.setVisible(true);

			this.linhaSelecionada = -1;

		} else {
			JOptionPane.showMessageDialog(null, "Selecione um aluno na tabela para editar.");
		}
	}// GEN-LAST:event_bEditarActionPerformed

	private void jTableAlunosMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTableAlunosMouseClicked
		if (evt.getClickCount() >= 1 && this.jTableAlunos.getSelectedRow() != -1) {
			this.linhaSelecionada = this.jTableAlunos.getSelectedRow();
		}
	}// GEN-LAST:event_jTableAlunosMouseClicked

	private void bDeletarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_bDeletarActionPerformed
		try {
			// validando dados da interface gráfica.
			int id = 0;

			if (this.jTableAlunos.getSelectedRow() == -1) {
				throw new Mensagens("Selecione um cadastro para deletar");
			} else {
				id = Integer.parseInt(this.jTableAlunos.getValueAt(this.jTableAlunos.getSelectedRow(), 0).toString());
			}

			String[] options = { "Sim", "Não" };
			int respostaUsuario = JOptionPane.showOptionDialog(null, "Tem certeza que deseja apagar este cadastro?",
					"Confirmar exclusão", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options,
					options[1]);

			if (respostaUsuario == 0) {

				// envia os dados para o AlunoDAO processar e trata a resposta
				if (this.alunoDAO.delete(id)) {
					JOptionPane.showMessageDialog(rootPane, "Cadastro apagado com sucesso!");
				} else {
					JOptionPane.showMessageDialog(rootPane, "Erro ao apagar o cadastro no banco de dados.");
				}
			}
		} catch (Mensagens erro) {
			JOptionPane.showMessageDialog(null, erro.getMessage());
		} finally {
			// atualiza a tabela.
			carregaTabela();
		}
	}// GEN-LAST:event_bDeletarActionPerformed

	private void refreshActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_refreshActionPerformed
		assert evt != null || evt == null;
		this.carregaTabela();
	}// GEN-LAST:event_refreshActionPerformed

	private void menuRefreshActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuRefreshActionPerformed
		assert evt != null || evt == null;
		this.carregaTabela();
	}// GEN-LAST:event_menuRefreshActionPerformed

	private void menuExportActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuExportActionPerformed
		assert evt != null || evt == null;
		this.exportXls();

	}// GEN-LAST:event_menuExportActionPerformed

	private void exportActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_exportActionPerformed
		assert evt != null || evt == null;
		this.exportXls();

	}// GEN-LAST:event_exportActionPerformed

	private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem1ActionPerformed
		Sobre tela = new Sobre();
		tela.setVisible(true);
	}// GEN-LAST:event_jMenuItem1ActionPerformed

	public void carregaTabela() {
		DefaultTableModel modelo = (DefaultTableModel) this.jTableAlunos.getModel();
		modelo.setNumRows(0);

		ArrayList<Aluno> minhalista;
		minhalista = this.alunoDAO.getMinhaLista();

		for (Aluno a : minhalista) {
			modelo.addRow(new Object[] { a.getId(), a.getNome(), a.getIdade(), a.getCurso(), a.getFase() + "ª", });
		}
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
		// (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the default
		 * look and feel. For details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(GerenciaAlunos.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(GerenciaAlunos.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(GerenciaAlunos.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(GerenciaAlunos.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		}
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(() -> new GerenciaAlunos().setVisible(true));

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
	private javax.swing.JTable jTableAlunos;
	private javax.swing.JMenu menu;
	private javax.swing.JMenuItem menuExport;
	private javax.swing.JMenuItem menuGerenciaProfessores;
	private javax.swing.JMenuItem menuLeave;
	private javax.swing.JMenuItem menuRefresh;
	private javax.swing.JButton refresh;
	// End of variables declaration//GEN-END:variables
}
