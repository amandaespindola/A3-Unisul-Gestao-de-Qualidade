package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import com.formdev.flatlaf.FlatDarkLaf;

import utils.Constantes;
import utils.ViewUtils;

public class TelaPrincipal extends javax.swing.JFrame {

	private static final Logger logger = Logger.getLogger(TelaPrincipal.class.getName());

	public TelaPrincipal() {
		initComponents();
	}

	private void initComponents() {

		setTitle("SisUni - Principal");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setSize(400, 320);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(10, 10));

		// ---------------------------
		// TÍTULO
		// ---------------------------
		JLabel lblTitulo = new JLabel(Constantes.UIConstants.TITULO_SISTEMA, JLabel.CENTER);
		lblTitulo.setFont(new Font(Constantes.UIConstants.DEFAULT_FONT, Font.BOLD, 16));
		add(lblTitulo, BorderLayout.NORTH);

		// ---------------------------
		// BOTÕES (Centro)
		// ---------------------------
		JPanel centro = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 25));

		JButton bAlunos = ViewUtils.criarBotao(Constantes.UIConstants.BTN_ALUNOS, e -> abrirAlunos());
		bAlunos.setFont(new Font(Constantes.UIConstants.DEFAULT_FONT, Font.PLAIN, 18));
		centro.add(bAlunos);

		JButton bProfessores = ViewUtils.criarBotao(Constantes.UIConstants.BTN_PROFESSORES, e -> abrirProfessores());
		bProfessores.setFont(new Font(Constantes.UIConstants.DEFAULT_FONT, Font.PLAIN, 18));
		centro.add(bProfessores);

		add(centro, BorderLayout.CENTER);

		// ---------------------------
		// MENU
		// ---------------------------
		JMenuBar menuBar = new JMenuBar();
		JMenu menuArquivo = new JMenu("Arquivo");

		JMenuItem itemAlunos = new JMenuItem("Gerenciar Alunos");
		itemAlunos.addActionListener(e -> abrirAlunos());

		JMenuItem itemProfessores = new JMenuItem("Gerenciar Professores");
		itemProfessores.addActionListener(e -> abrirProfessores());

		JMenuItem itemSobre = new JMenuItem("Sobre");
		itemSobre.addActionListener(e -> new Sobre().setVisible(true));

		JMenuItem itemSair = new JMenuItem("Sair");
		itemSair.addActionListener(e -> System.exit(0));

		menuArquivo.add(itemAlunos);
		menuArquivo.add(itemProfessores);
		menuArquivo.add(itemSobre);
		menuArquivo.add(itemSair);
		menuBar.add(menuArquivo);

		setJMenuBar(menuBar);
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
