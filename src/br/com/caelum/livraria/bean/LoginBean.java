package br.com.caelum.livraria.bean;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import br.com.caelum.livraria.dao.UsuarioDao;
import br.com.caelum.livraria.modelo.Usuario;
import br.com.caelum.livraria.util.RedirectView;

@ViewScoped
@Named
public class LoginBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Usuario usuario = new Usuario();

	public Usuario getUsuario() {
		return usuario;
	}

	public String efetuaLogin(){
		System.out.println("Fazendo Login do ususario " + this.usuario.getEmail());

		FacesContext context = FacesContext.getCurrentInstance();
		boolean existe = new UsuarioDao().existe(this.usuario);
		if(existe){
			context.getExternalContext().getSessionMap().put("usuarioLogado", this.usuario);			
			return new RedirectView("livro").toString();
		}

		//context.addMessage("login:email", new FacesMessage("Usuario não encontrado"));
		context.getExternalContext().getFlash().setKeepMessages(true);
		context.addMessage(null, new FacesMessage("Usuario não encontrado"));
		
		return new RedirectView("livro").toString();
	}
	public String deslogar(){
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().remove("usuarioLogado");
		return new RedirectView("livro").toString();
	}
}