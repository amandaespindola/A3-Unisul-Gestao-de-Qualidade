package utils;

import java.util.function.Consumer;

import javax.swing.JTable;

/**
 * Classe utilitária para operações relacionadas a {@link JTable}.
 * <p>
 * Fornece métodos auxiliares para facilitar a manipulação de tabelas,
 * evitando repetição de código em pontos diferentes da aplicação.
 * </p>
 *
 * A classe possui um construtor privado para impedir instância, pois sua
 * finalidade é apenas disponibilizar métodos estáticos.
 */
public class TableUtils {

	/**
     * Construtor privado para impedir criação de instâncias da classe.
     * Como se trata de uma classe utilitária, todos os métodos são estáticos.
     */
	private TableUtils() {
	}

	/**
     * Adiciona um listener para capturar eventos de clique do mouse em uma tabela.
     * <p>
     * Quando o usuário clicar na {@link JTable}, o método executará a ação
     * fornecida através do {@link Consumer}, recebendo o {@link java.awt.event.MouseEvent}
     * correspondente.
     * </p>
     *
     * @param table  a tabela na qual o listener será adicionado (não pode ser {@code null})
     * @param action ação a ser executada ao clicar sobre a tabela, recebendo o evento
     *               de mouse como parâmetro (não pode ser {@code null})
     *
     * @throws NullPointerException caso {@code table} ou {@code action} seja {@code null}
     */
	public static void addMouseClickListener(JTable table, Consumer<java.awt.event.MouseEvent> action) {

		table.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				action.accept(evt);
			}
		});
	}
}
