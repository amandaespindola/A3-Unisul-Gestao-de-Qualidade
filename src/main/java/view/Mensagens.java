	package view;
	
	/**
	 * Exceção personalizada usada para representar mensagens de erro
	 * específicas da aplicação.
	 *
	 * <p>Essa classe estende {@link Exception} e permite que o sistema
	 * lance erros com textos definidos pelo desenvolvedor.</p>
	 */
	public class Mensagens extends Exception {
	
		/**
	     * Cria uma nova exceção contendo a mensagem fornecida.
	     *
	     * @param msg texto descritivo do erro
	     */
	    public Mensagens(String msg) {
	        super(msg);
	    }
	}
