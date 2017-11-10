package br.com.caelum.livraria.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.livraria.dao.AutorDao;
import br.com.caelum.livraria.dao.LivroDao;
import br.com.caelum.livraria.modelo.Autor;
import br.com.caelum.livraria.modelo.Livro;
import br.com.caelum.livraria.util.RedirectView;

@ViewScoped
@Named
public class LivroBean implements Serializable{

	private static final long serialVersionUID = 1L;

	private Livro livro = new Livro();

	private Integer livroID;

	private List<Livro> livros;
	
	@Inject
	LivroDao livroDao;
	
	@Inject
	AutorDao autorDao;

	public Integer getLivroID() {
		return livroID;
	}

	public void setLivroID(Integer livroID) {
		this.livroID = livroID;
	}

	public void setLivro(Livro livro) {
		this.livro = livro;
	}

	public List<Livro> getLivros() {
		if(this.livros == null){
			this.livros = livroDao.listaTodos();			
		}
		return livros;
	}

	public Livro getLivro() {
		return livro;
	}

	public void gravar() {
		System.out.println("Gravando livro " + this.livro.getTitulo());

		if (livro.getAutores().isEmpty()) {
			//throw new RuntimeException("Livro deve ter pelo menos um Autor.");
			FacesContext.getCurrentInstance().addMessage("autor", new FacesMessage("Livro deve ter pelo menos um Autor."));
			return;
		}

		if(this.livro.getId() == null){
			livroDao.adiciona(this.livro);
			this.livros = livroDao.listaTodos();
		} else {
			livroDao.atualiza(this.livro); 
		}

		this.livro = new Livro();
	}

	public void carregar(Livro livro){
		System.out.println("Carregando Livro");
		this.livro = livro;
	}

	public void remover(Livro livro){
		System.out.println("Removendo Livro");
		livroDao.remove(livro);
		this.livros = livroDao.listaTodos();
	}

	public void removerAutorDoLivro(Autor autor){
		this.livro.removeAutor(autor);
		System.out.println("Autor removido");
	}


	private Integer autorId;

	public void setAutorId(Integer autorId) {
		this.autorId = autorId;
	}

	public Integer getAutorId() {
		return autorId;
	}

	public List<Autor> getAutores() {
		return autorDao.listaTodos();
	}

	public void gravarAutor() {
		Autor autor = autorDao.buscaPorId(this.autorId);
		this.livro.adicionaAutor(autor);
	}

	public List<Autor> getAutoresDoLivro() {
		return this.livro.getAutores();
	}

	public RedirectView formAutor(){
		System.out.println("Chamando o formulario do autor");
		return new RedirectView("autor");
	}

	public void comecaComDigitoUm(FacesContext fc, UIComponent component, Object value) throws ValidatorException{
		String valor = value.toString();
		if(!valor.startsWith("9")){
			throw new ValidatorException(new FacesMessage("ISBN: Deve começar com 9"));
		}

	}

	public void carregaPelaID() {
		this.livro = livroDao.buscaPorId(this.livroID);
	}

	public boolean precoEhMenor(Object valorColuna, Object filtroDigitado, Locale locale) { // java.util.Locale

		//tirando espaços do filtro
		String textoDigitado = (filtroDigitado == null) ? null : filtroDigitado.toString().trim();

		System.out.println("Filtrando pelo " + textoDigitado + ", Valor do elemento: " + valorColuna);

		// o filtro é nulo ou vazio?
		if (textoDigitado == null || textoDigitado.equals("")) {
			return true;
		}

		// elemento da tabela é nulo?
		if (valorColuna == null) {
			return false;
		}

		try {
			// fazendo o parsing do filtro para converter para Double
			Double precoDigitado = Double.valueOf(textoDigitado);
			Double precoColuna = (Double) valorColuna;

			// comparando os valores, compareTo devolve um valor negativo se o value é menor do que o filtro
			return precoColuna.compareTo(precoDigitado) < 0;

		} catch (NumberFormatException e) {

			// usuario nao digitou um numero
			return false;
		}
	}

}
