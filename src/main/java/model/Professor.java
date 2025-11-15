package model;

import java.util.List;
import dao.ProfessorDAO;

public class Professor extends ProfessorBase {

    private final ProfessorDAO dao = new ProfessorDAO();

    public Professor() {
    }

    public Professor(String campus, String cpf, String contato, String titulo, double salario) {
        this.campus = campus;
        this.cpf = cpf;
        this.contato = contato;
        this.titulo = titulo;
        this.salario = salario;
    }

    public Professor(ProfessorDTO dto) {
        super(dto.getId(), dto.getNome(), dto.getIdade());
        this.campus = dto.getCampus();
        this.cpf = dto.getCpf();
        this.contato = dto.getContato();
        this.titulo = dto.getTitulo();
        this.salario = dto.getSalario();
    }

    @Override
    public String toString() {
        return "\nID: " + getId()
                + "\nNome: " + getNome()
                + "\nIdade: " + getIdade()
                + "\nCampus: " + campus
                + "\nCPF: " + cpf
                + "\nContato: " + contato
                + "\nTítulo: " + titulo
                + "\nSalário: " + salario
                + "\n-----------";
    }

    // ======== MÉTODOS DE NEGÓCIO ========
    public List<Professor> getMinhaLista() {
        return dao.getMinhaLista();
    }

    public boolean inserirProfessorBD(ProfessorDTO dto) {
        dto.setId(obterMaiorId() + 1);
        return dao.insert(new Professor(dto));
    }

    public boolean deletarProfessorBD(int id) {
        return dao.delete(id);
    }

    public boolean atualizarProfessorBD(ProfessorDTO dto) {
        return dao.update(new Professor(dto));
    }

    public Professor carregaProfessor(int id) {
        return dao.findById(id);
    }

    public int obterMaiorId() {
        return dao.obterMaiorId();
    }
}
