package view;

/**
 * Classe de exceção personalizada para mensagens específicas do sistema.
 * Estende a classe {@link Exception}.
 */
public class Mensagens extends Exception {

    /**
     * Construtor que recebe uma mensagem específica para a exceção.
     *
     * @param msg A mensagem da exceção.
     */
    public Mensagens(String msg) {
        super(msg);
    }
}
