package model;

public class ProfessorDTO extends ProfessorBase {

    public ProfessorDTO() {
    }

    public ProfessorDTO(int id, String nome, int idade,
            String campus, String cpf, String contato,
            String titulo, double salario) {
        super(id, nome, idade);
        this.campus = campus;
        this.cpf = cpf;
        this.contato = contato;
        this.titulo = titulo;
        this.salario = salario;
    }
}
