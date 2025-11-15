package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.WindowConstants;
import javax.swing.SwingConstants;

import utils.Constantes;

import com.formdev.flatlaf.FlatDarkLaf;

public class TelaPrincipal extends javax.swing.JFrame {

	private static final Logger logger = Logger.getLogger(TelaPrincipal.class.getName());

                private static final String DEFAULT_FONT = Constantes.UIConstants.DEFAULT_FONT;

	public TelaPrincipal() {
		initComponents();
	}

	private void initComponents() {

		setTitle("SisUni - Principal");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);

		// Borda azul igual ao vídeo
		Color blueBorder = new Color(120, 155, 255);
		Border border = BorderFactory.createLineBorder(blueBorder, 2);

		// ============================
		// TÍTULO
		// ============================
		JLabel lblTitulo = new JLabel("SisUni - Sistema de Gerenciamento Universitário", SwingConstants.CENTER);
		lblTitulo.setFont(new Font(DEFAULT_FONT, Font.BOLD, 18));

		// ============================
		// BOTÕES (sem destaque)
		// ============================
		JButton btnAlunos = new JButton("Alunos");
		btnAlunos.setFont(new Font(DEFAULT_FONT, Font.PLAIN, 20));
		btnAlunos.setPreferredSize(new Dimension(360, 70));
		btnAlunos.setBorder(border);
		btnAlunos.setFocusPainted(false);

		JButton btnProfessores = new JButton("Professores");
		btnProfessores.setFont(new Font(DEFAULT_FONT, Font.PLAIN, 20));
		btnProfessores.setPreferredSize(new Dimension(360, 70));
		btnProfessores.setBorder(border);
		btnProfessores.setFocusPainted(false);


		btnAlunos.addActionListener(e -> abrirAlunos());
		btnProfessores.addActionListener(e -> abrirProfessores());

		// ============================
		// LAYOUT CENTRALIZADO VERTICALMENTE
		// ============================
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);

		layout.setAutoCreateGaps(true);

		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(lblTitulo, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(btnAlunos, 360, 360, 360).addComponent(btnProfessores, 360, 360, 360));

		layout.setVerticalGroup(layout.createSequentialGroup().addGap(20) // TÍTULO colado no topo, igual ao vídeo

				.addComponent(lblTitulo).addGap(80)

				.addComponent(btnAlunos, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE).addGap(20) 																												// entre
				.addComponent(btnProfessores, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)

				.addGap(0, 0, Short.MAX_VALUE));

		// Janela igual ao vídeo
		setSize(520, 420);
		setLocationRelativeTo(null);
	}

	// -------------------------
	// AÇÕES
	// -------------------------

	private void abrirAlunos() {
		new GerenciaAlunos().setVisible(true);
		dispose();
	}

	private void abrirProfessores() {
		new GerenciaProfessores().setVisible(true);
		dispose();
	}

	// -------------------------
	// MAIN
	// -------------------------
	public static void main(String[] args) {

		try {
			FlatDarkLaf.setup();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Falha ao inicializar tema FlatDarkLaf", e);
		}

		EventQueue.invokeLater(() -> new TelaPrincipal().setVisible(true));
	}
}
