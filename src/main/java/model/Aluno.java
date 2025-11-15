package model;

import java.util.List;
import dao.AlunoDAO;

public class Aluno extends Pessoa {

    private String curso;
    private int fase;

    // DAO interno
    private final AlunoDAO dao = new AlunoDAO();

    public Aluno() {
    }

    public Aluno(String curso, int fase) {
        this.curso = curso;
        this.fase = fase;
    }

    public Aluno(String curso, int fase, int id, String nome, int idade) {
        super(id, nome, idade);
        this.curso = curso;
        this.fase = fase;
    }

    // Getters e Setters
    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public int getFase() {
        return fase;
    }

    public void setFase(int fase) {
        this.fase = fase;
    }

    @Override
    public String toString() {
        return "\nID: " + getId()
                + "\nNome: " + getNome()
                + "\nIdade: " + getIdade()
                + "\nCurso: " + curso
                + "\nFase: " + fase
                + "\n-----------";
    }

    // ======== MÉTODOS DE NEGÓCIO ========
    public List<Aluno> getMinhaLista() {
        return dao.getMinhaLista();
    }

    public boolean inserirAlunoBD(String curso, int fase, String nome, int idade) {
        Aluno novo = new Aluno(curso, fase, 0, nome, idade);
        return dao.insert(novo);
    }

    public boolean deletarAlunoBD(int id) {
        return dao.delete(id);
    }

    public boolean atualizarAlunoBD(String curso, int fase, int id, String nome, int idade) {
        Aluno atualizado = new Aluno(curso, fase, id, nome, idade);
        return dao.update(atualizado);
    }

    public Aluno carregaAluno(int id) {
        return dao.findById(id);
    }

    public int obterMaiorId() {
        return dao.obterMaiorId();
    }
}
