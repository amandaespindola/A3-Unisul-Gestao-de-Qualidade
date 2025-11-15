package view;

import model.Aluno;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import dao.AlunoDAO;

public class GerenciaAlunos extends javax.swing.JFrame {

	private AlunoDAO alunoDAO;
	private int linhaSelecionada = -1;

	public GerenciaAlunos() {
		initComponents();
		this.alunoDAO = new AlunoDAO();
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
		setBackground(new java.awt.Color(80, 80, 80));
		setResizable(false);

		bCadastro.setText("Cadastrar novo");
		bCadastro.setToolTipText("");
		bCadastro.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				bCadastroActionPerformed(evt);
			}
		});

		bEditar.setText("Editar");
		bEditar.setToolTipText("");
		bEditar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				bEditarActionPerformed(evt);
			}
		});

		bDeletar.setText("Deletar");
		bDeletar.setToolTipText("");
		bDeletar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				bDeletarActionPerformed(evt);
			}
		});

		jTableAlunos.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] { { null, null, null, null, null }, { null, null, null, null, null },
						{ null, null, null, null, null }, { null, null, null, null, null } },
				new String[] { "ID", "Nome", "Idade", "Curso", "Fase" }) {
			boolean[] canEdit = new boolean[] { false, false, false, false, true };

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		jTableAlunos.setSelectionForeground(new java.awt.Color(239, 239, 239));
		jTableAlunos.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				jTableAlunosMouseClicked(evt);
			}
		});
		jScrollPane2.setViewportView(jTableAlunos);
		if (jTableAlunos.getColumnModel().getColumnCount() > 0) {
			jTableAlunos.getColumnModel().getColumn(0).setMinWidth(40);
			jTableAlunos.getColumnModel().getColumn(0).setMaxWidth(40);
			jTableAlunos.getColumnModel().getColumn(1).setMinWidth(400);
			jTableAlunos.getColumnModel().getColumn(1).setMaxWidth(400);
			jTableAlunos.getColumnModel().getColumn(2).setMinWidth(60);
			jTableAlunos.getColumnModel().getColumn(2).setMaxWidth(60);
			jTableAlunos.getColumnModel().getColumn(3).setMinWidth(300);
			jTableAlunos.getColumnModel().getColumn(3).setMaxWidth(300);
		}

		jLabel1.setFont(new java.awt.Font("Tahoma", 1, 40)); // NOI18N
		jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		jLabel1.setText("Cadastro de Alunos");

		refresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/refresh.png"))); // NOI18N
		refresh.setText("  Atualizar tabela");
		refresh.setToolTipText("CTRL+R");
		refresh.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				refreshActionPerformed(evt);
			}
		});

		export.setText("Exportar para Excel");
		export.setToolTipText("CTRL+E");
		export.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				exportActionPerformed(evt);
			}
		});

		menu.setForeground(new java.awt.Color(239, 239, 239));
		menu.setText("Arquivo");

		menuGerenciaProfessores.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P,
				java.awt.event.InputEvent.CTRL_DOWN_MASK));
		menuGerenciaProfessores.setText("Gerência de Professores");
		menuGerenciaProfessores.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menuGerenciaProfessoresActionPerformed(evt);
			}
		});
		menu.add(menuGerenciaProfessores);

		menuExport.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E,
				java.awt.event.InputEvent.CTRL_DOWN_MASK));
		menuExport.setText("Exportar para Excel");
		menuExport.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menuExportActionPerformed(evt);
			}
		});
		menu.add(menuExport);

		menuRefresh.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R,
				java.awt.event.InputEvent.CTRL_DOWN_MASK));
		menuRefresh.setText("Atualizar tabela");
		menuRefresh.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menuRefreshActionPerformed(evt);
			}
		});
		menu.add(menuRefresh);

		jMenuItem1.setText("Sobre");
		jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItem1ActionPerformed(evt);
			}
		});
		menu.add(jMenuItem1);

		menuLeave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S,
				java.awt.event.InputEvent.CTRL_DOWN_MASK));
		menuLeave.setText("Sair");
		menuLeave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menuLeaveActionPerformed(evt);
			}
		});
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
	}// </editor-fold>//GEN-END:initComponents

	private void exportXls() throws IOException {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos Excel", "xls");

		chooser.setFileFilter(filter);
		chooser.setDialogTitle("Salvar arquivo");
		chooser.setAcceptAllFileFilterUsed(false);

		if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			String path = chooser.getSelectedFile().toString().concat(".xls");
			try {
				File fileXLS = new File(path);
				if (fileXLS.exists()) {
					fileXLS.delete();
				}
				fileXLS.createNewFile();
				Workbook book = new HSSFWorkbook();
				FileOutputStream file = new FileOutputStream(fileXLS);
				Sheet sheet = book.createSheet("Minha folha de trabalho 1");
				sheet.setDisplayGridlines(true);

				for (int i = 0; i < this.jTableAlunos.getRowCount(); i++) {
					Row row = sheet.createRow(i);
					for (int j = 0; j < this.jTableAlunos.getColumnCount(); j++) {
						Cell cell = row.createCell(j);
						if (i == 0) {
							cell.setCellValue(this.jTableAlunos.getColumnName(j));
						}
					}
				}

				int firstRow = 1;

				for (int linha = 0; linha < this.jTableAlunos.getRowCount(); linha++) {
					Row row2 = sheet.createRow(firstRow);
					firstRow++;
					for (int coluna = 0; coluna < this.jTableAlunos.getColumnCount(); coluna++) {
						Cell cell2 = row2.createCell(coluna);
						Object valor = this.jTableAlunos.getValueAt(linha, coluna);
						if (valor instanceof Double) {
							cell2.setCellValue(Double.parseDouble(valor.toString()));
						} else if (valor instanceof Float) {
							cell2.setCellValue(Float.parseFloat(valor.toString()));
						} else if (valor instanceof Integer) {
							cell2.setCellValue(Integer.parseInt(valor.toString()));
						} else {
							cell2.setCellValue(String.valueOf(valor));
						}
					}
				}
				book.write(file);
				file.close();
			} catch (IOException | NumberFormatException e) {
				throw e;
			}
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
		int linha = this.jTableAlunos.getSelectedRow();

		if (linha != -1) {

			String id = this.jTableAlunos.getValueAt(linha, 0).toString();
			String nome = this.jTableAlunos.getValueAt(linha, 1).toString();
			String idade = this.jTableAlunos.getValueAt(linha, 2).toString();
			String curso = this.jTableAlunos.getValueAt(linha, 3).toString();
			String fase = this.jTableAlunos.getValueAt(linha, 4).toString();

			// mantém a lógica original: só o primeiro caractere (ex: "3ª" → "3")
			fase = String.valueOf(fase.charAt(0));

			String[] dadosParaEdicao = new String[5];
			dadosParaEdicao[0] = id;
			dadosParaEdicao[1] = nome;
			dadosParaEdicao[2] = idade;
			dadosParaEdicao[3] = curso;
			dadosParaEdicao[4] = fase;

			EditarAluno tela = new EditarAluno(dadosParaEdicao);
			tela.setVisible(true);

		} else {
			JOptionPane.showMessageDialog(null, "Selecione um cadastro para alterar");
		}
	}// GEN-LAST:event_bEditarActionPerformed

	private void jTableAlunosMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTableAlunosMouseClicked
		// Mantido vazio apenas para o GUI Builder (caso queira usar no futuro)
	}// GEN-LAST:event_jTableAlunosMouseClicked

	private void bDeletarActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			if (this.jTableAlunos.getSelectedRow() == -1) {
				throw new Mensagens("Selecione um cadastro para deletar");
			}

			int id = Integer.parseInt(this.jTableAlunos.getValueAt(this.jTableAlunos.getSelectedRow(), 0).toString());

			String[] options = { "Sim", "Não" };
			int resp = JOptionPane.showOptionDialog(null, "Tem certeza que deseja apagar este cadastro?",
					"Confirmar exclusão", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options,
					options[1]);

			if (resp == 0) { // SIM
				if (this.alunoDAO.delete(id)) {
					JOptionPane.showMessageDialog(rootPane, "Cadastro apagado com sucesso!");
				} else {
					JOptionPane.showMessageDialog(rootPane, "Erro ao apagar cadastro no banco!");
				}
			}

		} catch (Mensagens e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} finally {
			carregaTabela();
		}
	}

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
			Logger.getLogger(GerenciaAlunos.class.getName()).log(Level.SEVERE, null, ex);
		}
	}// GEN-LAST:event_menuExportActionPerformed

	private void exportActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_exportActionPerformed
		try {
			this.exportXls();
		} catch (IOException ex) {
			Logger.getLogger(GerenciaAlunos.class.getName()).log(Level.SEVERE, null, ex);
		}
	}// GEN-LAST:event_exportActionPerformed

	private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem1ActionPerformed
		Sobre tela = new Sobre();
		tela.setVisible(true);
	}// GEN-LAST:event_jMenuItem1ActionPerformed

	@SuppressWarnings("unchecked")
	public void carregaTabela() {
		DefaultTableModel modelo = (DefaultTableModel) this.jTableAlunos.getModel();
		modelo.setNumRows(0);

		List<Aluno> minhalista = alunoDAO.getMinhaLista();

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

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new GerenciaAlunos().setVisible(true);
			}
		});
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
