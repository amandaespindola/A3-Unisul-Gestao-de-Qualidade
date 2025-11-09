package Model;

import java.util.*;
import DAO.AlunoDAO;
import java.sql.SQLException;

public class Aluno extends Pessoa {

    // Atributos
    private String curso;
    private int fase;
    private final AlunoDAO dao;

    public Aluno() {
        this.dao = new AlunoDAO();
    }

    public Aluno(String curso, int fase) {
        this.curso = curso;
        this.fase = fase;
        this.dao = new AlunoDAO();
    }

    public Aluno(String curso, int fase, int id, String nome, int idade) {
        super(id, nome, idade);
        this.curso = curso;
        this.fase = fase;
        this.dao = new AlunoDAO();
    }

    // Métodos GET e SET
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
        return "\n ID: " + this.getId()
                + "\n Nome: " + this.getNome()
                + "\n Idade: " + this.getIdade()
                + "\n Curso: " + this.getCurso()
                + "\n Fase:" + this.getFase()
                + "\n -----------";
    }

  
    // Retorna a Lista de Alunos (objetos)
    public ArrayList getMinhaLista() {
        return dao.getMinhaLista();
    }

    // Cadastra novo aluno
    public boolean InsertAlunoBD(String curso, int fase, String nome, int idade) throws SQLException {
        Aluno objeto = new Aluno(curso, fase, 0, nome, idade);
        return dao.insert(objeto);
    }

    // Deleta um aluno específico pelo seu campo ID
    public boolean DeleteAlunoBD(int id) {
        dao.delete(id);
        return true;
    }

    // Edita um aluno específico pelo seu campo ID
    public boolean UpdateAlunoBD(String curso, int fase, int id, String nome, int idade) {
        Aluno objeto = new Aluno(curso, fase, id, nome, idade);
        dao.update(objeto);
        return true;
    }

    // carrega dados de um aluno específico pelo seu ID
    public Aluno carregaAluno(int id) {
        dao.findById(id);
        return null;
    }

    // retorna o maior ID da nossa base de dados
    public int obterMaiorId() throws SQLException {
        return dao.obterMaiorId();
    }
}
