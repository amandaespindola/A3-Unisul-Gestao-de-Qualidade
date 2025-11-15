package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import utils.Constantes;
import utils.LookAndFeelHelper;
import utils.ViewUtils;

public class Sobre extends JFrame {

	public Sobre() {
		initComponents();
	}

	private void initComponents() {

		setTitle("Sobre");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(420, 350);
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(10, 10));

		// ---------------------------------------
		// TÍTULO E SUBTÍTULOS
		// ---------------------------------------
		JPanel topo = new JPanel(new GridLayout(3, 1));

		JLabel lblTitulo = ViewUtils.criarLabelTitulo(Constantes.UIConstants.SOBRE_INSTITUICAO);
		lblTitulo.setFont(new Font(Constantes.UIConstants.DEFAULT_FONT, Font.BOLD, 14));

		JLabel lblUC = new JLabel(Constantes.UIConstants.SOBRE_UC, SwingConstants.CENTER);
		lblUC.setFont(new Font(Constantes.UIConstants.DEFAULT_FONT, Font.PLAIN, 11));

		JLabel lblCurso = new JLabel(Constantes.UIConstants.SOBRE_CURSO, SwingConstants.CENTER);
		lblCurso.setFont(new Font(Constantes.UIConstants.DEFAULT_FONT, Font.PLAIN, 11));

		topo.add(lblTitulo);
		topo.add(lblUC);
		topo.add(lblCurso);

		// ---------------------------------------
		// SEPARADOR
		// ---------------------------------------
		JSeparator sep1 = new JSeparator();
		JSeparator sep2 = new JSeparator();

		// ---------------------------------------
		// LISTA DE INTEGRANTES
		// ---------------------------------------
		JPanel integrantes = new JPanel(new GridLayout(3, 2, 10, 5));

		integrantes.add(ViewUtils.criarLabel(Constantes.UIConstants.SOBRE_INTEGRANTE_1, ""));
		integrantes.add(ViewUtils.criarLabel(Constantes.UIConstants.SOBRE_INTEGRANTE_4, ""));

		integrantes.add(ViewUtils.criarLabel(Constantes.UIConstants.SOBRE_INTEGRANTE_2, ""));
		integrantes.add(ViewUtils.criarLabel(Constantes.UIConstants.SOBRE_INTEGRANTE_5, ""));

		integrantes.add(ViewUtils.criarLabel(Constantes.UIConstants.SOBRE_INTEGRANTE_3, ""));
		integrantes.add(ViewUtils.criarLabel(Constantes.UIConstants.SOBRE_INTEGRANTE_6, ""));

		// ---------------------------------------
		// DATA
		// ---------------------------------------
		JLabel lblData = new JLabel(Constantes.UIConstants.SOBRE_DATA, SwingConstants.CENTER);
		lblData.setFont(new Font(Constantes.UIConstants.DEFAULT_FONT, Font.BOLD, 12));

		// ---------------------------------------
		// ADICIONAR COMPONENTES
		// ---------------------------------------
		add(topo, BorderLayout.NORTH);
		add(sep1, BorderLayout.CENTER);

		JPanel centro = new JPanel(new BorderLayout());
		centro.add(integrantes, BorderLayout.CENTER);
		centro.add(sep2, BorderLayout.SOUTH);

		add(centro, BorderLayout.CENTER);
		add(lblData, BorderLayout.SOUTH);
	}

	// MAIN
	public static void main(String[] args) {
		LookAndFeelHelper.aplicarNimbus();
		EventQueue.invokeLater(() -> new Sobre().setVisible(true));
	}
}
