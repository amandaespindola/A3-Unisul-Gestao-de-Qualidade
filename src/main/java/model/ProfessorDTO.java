package model;

/**
 * Data Transfer Object (DTO) para a entidade {@link Professor}.
 * <p>
 * Utilizado para transportar dados de professores entre camadas do sistema
 * sem expor diretamente a entidade {@link Professor}.
 * Herdando os atributos de {@link ProfessorBase}, contém informações como
 * campus, CPF, contato, título e salário.
 * </p>
 */
public class ProfessorDTO extends ProfessorBase{

}
