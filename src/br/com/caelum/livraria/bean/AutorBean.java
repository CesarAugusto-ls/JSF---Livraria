package br.com.caelum.livraria.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.RollbackException;

import br.com.caelum.livraria.dao.AutorDao;
import br.com.caelum.livraria.modelo.Autor;
import br.com.caelum.livraria.util.RedirectView;

@ViewScoped
@Named
public class AutorBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Autor autor = new Autor();

	@Inject
	private AutorDao dao;

	private Integer autorID;

	public Integer getAutorID() {
		return autorID;
	}

	public void setAutorID(Integer autorID) {
		this.autorID = autorID;
	}

	public void carregaAutorPeloID() {
		this.autor = this.dao.buscaPorId(autorID);
	}

	public void setAutor(Autor autor) {
		this.autor = autor;
	}

	public Autor getAutor() {
		return autor;
	}

	public List<Autor> getAutores() {
		return this.dao.listaTodos();
	}

	public RedirectView gravar() {
		System.out.println("Gravando autor " + this.autor.getNome());

		if (this.autor.getId() == null) {
			this.dao.adiciona(this.autor);
			return new RedirectView("livro");
		} else {
			this.dao.atualiza(this.autor);
		}

		this.autor = new Autor();

		return new RedirectView("autor");
	}

	public void carregar(Autor autor) {
		System.out.println("Carregando Autor");
		this.autor = autor;
	}

	public void remover(Autor autor) {
		try {
			System.out.println("Removendo Autor");
			this.dao.remove(autor);
		} catch (RollbackException re) {
			System.out.println("ERRO CAPTURADO: " + re);
		}
	}

}
